import React, { useMemo, useState } from 'react';
import { Pressable, StyleSheet, Text, View } from 'react-native';
import { useColors } from '@/hooks/useColors';
import { Feather } from '@expo/vector-icons';
import * as Haptics from 'expo-haptics';
import {
  daysInEthiopianMonth,
  ETHIOPIAN_MONTH_NAMES,
  ethiopianToGregorian,
  gregorianToEthiopian,
  todayEthiopian,
  type EthiopianDate,
} from '@/lib/ethiopian-calendar';
import { useEntries } from '@/context/EntriesContext';

const WEEKDAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

interface MonthCalendarProps {
  onSelectDate: (isoDate: string) => void;
}

export function MonthCalendar({ onSelectDate }: MonthCalendarProps) {
  const colors = useColors();
  const { entriesByDate } = useEntries();
  const today = useMemo(() => todayEthiopian(), []);
  const [cursor, setCursor] = useState<{ year: number; month: number }>({
    year: today.year,
    month: today.month,
  });

  const dayCount = daysInEthiopianMonth(cursor.year, cursor.month);

  const cells = useMemo(() => {
    const items: { day: number; iso: string; weekday: number }[] = [];
    for (let day = 1; day <= dayCount; day++) {
      const date: EthiopianDate = { year: cursor.year, month: cursor.month, day };
      const iso = ethiopianToGregorian(date);
      const weekday = new Date(`${iso}T00:00:00.000Z`).getUTCDay();
      items.push({ day, iso, weekday });
    }
    return items;
  }, [cursor, dayCount]);

  const leadingBlanks = cells.length > 0 ? cells[0].weekday : 0;

  function goToMonth(delta: number) {
    Haptics.selectionAsync();
    setCursor((prev) => {
      let month = prev.month + delta;
      let year = prev.year;
      if (month < 1) {
        month = 13;
        year -= 1;
      } else if (month > 13) {
        month = 1;
        year += 1;
      }
      return { year, month };
    });
  }

  function jumpToToday() {
    Haptics.selectionAsync();
    setCursor({ year: today.year, month: today.month });
  }

  const isCurrentMonth = cursor.year === today.year && cursor.month === today.month;
  const todayIso = ethiopianToGregorian(today);

  return (
    <View style={[styles.card, { backgroundColor: colors.card, borderColor: colors.border }]}>
      <View style={styles.header}>
        <View style={{ flexDirection: 'row', alignItems: 'baseline', gap: 6, flexShrink: 1 }}>
          <Text style={[styles.monthName, { color: colors.foreground }]} numberOfLines={1}>
            {ETHIOPIAN_MONTH_NAMES[cursor.month - 1]}
          </Text>
          <Text style={[styles.yearName, { color: colors.mutedForeground }]}>{cursor.year}</Text>
        </View>
        <View style={styles.navGroup}>
          {!isCurrentMonth && (
            <Pressable
              onPress={jumpToToday}
              style={[styles.todayButton, { borderColor: colors.border }]}
              testID="jump-to-today"
            >
              <Text style={[styles.todayButtonText, { color: colors.primary }]}>Today</Text>
            </Pressable>
          )}
          <Pressable
            onPress={() => goToMonth(-1)}
            style={[styles.navButton, { backgroundColor: colors.secondary }]}
            testID="prev-month"
          >
            <Feather name="chevron-left" size={18} color={colors.foreground} />
          </Pressable>
          <Pressable
            onPress={() => goToMonth(1)}
            style={[styles.navButton, { backgroundColor: colors.secondary }]}
            testID="next-month"
          >
            <Feather name="chevron-right" size={18} color={colors.foreground} />
          </Pressable>
        </View>
      </View>

      <View style={styles.weekdayRow}>
        {WEEKDAYS.map((wd) => (
          <View key={wd} style={styles.weekdayCell}>
            <Text style={[styles.weekdayText, { color: colors.mutedForeground }]}>{wd}</Text>
          </View>
        ))}
      </View>

      <View style={styles.grid}>
        {Array.from({ length: leadingBlanks }).map((_, i) => (
          <View key={`blank-${i}`} style={styles.dayCellWrapper} />
        ))}
        {cells.map(({ day, iso }) => {
          const entry = entriesByDate[iso];
          const isToday = iso === todayIso;
          return (
            <View key={iso} style={styles.dayCellWrapper}>
              <Pressable
                onPress={() => {
                  Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
                  onSelectDate(iso);
                }}
                style={({ pressed }) => [
                  styles.dayCell,
                  {
                    backgroundColor: entry ? colors.primary : colors.background,
                    borderColor: isToday ? colors.accent : colors.border,
                    borderWidth: isToday ? 2 : 1,
                    opacity: pressed ? 0.75 : 1,
                    transform: [{ scale: pressed ? 0.95 : 1 }],
                  },
                ]}
                testID={`day-${iso}`}
              >
                <Text
                  style={[
                    styles.dayNumber,
                    { color: entry ? colors.primaryForeground : colors.foreground },
                  ]}
                >
                  {day}
                </Text>
                {entry && (
                  <Text
                    style={[styles.dayAmount, { color: colors.primaryForeground }]}
                    numberOfLines={1}
                  >
                    {Math.round(entry.amount)}
                  </Text>
                )}
                {entry?.note ? (
                  <View style={[styles.noteDot, { backgroundColor: colors.accent }]} />
                ) : null}
              </Pressable>
            </View>
          );
        })}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    borderWidth: 1,
    borderRadius: 20,
    padding: 16,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 14,
    gap: 8,
  },
  monthName: {
    fontSize: 22,
    fontWeight: '700',
  },
  yearName: {
    fontSize: 15,
    fontWeight: '500',
  },
  navGroup: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  todayButton: {
    borderWidth: 1,
    borderRadius: 999,
    paddingHorizontal: 10,
    paddingVertical: 6,
  },
  todayButtonText: {
    fontSize: 12,
    fontWeight: '700',
  },
  navButton: {
    width: 32,
    height: 32,
    borderRadius: 16,
    alignItems: 'center',
    justifyContent: 'center',
  },
  weekdayRow: {
    flexDirection: 'row',
    marginBottom: 6,
  },
  weekdayCell: {
    flex: 1,
    alignItems: 'center',
  },
  weekdayText: {
    fontSize: 11,
    fontWeight: '700',
    textTransform: 'uppercase',
    letterSpacing: 0.5,
  },
  grid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
  dayCellWrapper: {
    width: `${100 / 7}%`,
    padding: 3,
  },
  dayCell: {
    aspectRatio: 1,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
    position: 'relative',
  },
  dayNumber: {
    fontSize: 14,
    fontWeight: '700',
  },
  dayAmount: {
    fontSize: 9,
    fontWeight: '600',
    marginTop: 1,
  },
  noteDot: {
    position: 'absolute',
    top: 5,
    right: 5,
    width: 5,
    height: 5,
    borderRadius: 2.5,
  },
});
