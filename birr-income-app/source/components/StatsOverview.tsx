import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { useColors } from '@/hooks/useColors';
import { Feather } from '@expo/vector-icons';
import { formatEthiopianDate, gregorianToEthiopian } from '@/lib/ethiopian-calendar';
import type { Summary } from '@/context/EntriesContext';

function formatBirr(amount: number): string {
  return `ETB ${Math.round(amount).toLocaleString('en-US')}`;
}

function StatCard({
  icon,
  label,
  value,
  sub,
  accentColor,
}: {
  icon: keyof typeof Feather.glyphMap;
  label: string;
  value: string;
  sub?: string;
  accentColor?: string;
}) {
  const colors = useColors();
  return (
    <View style={[styles.card, { backgroundColor: colors.card, borderColor: colors.border }]}>
      <View style={styles.cardHeader}>
        <Feather name={icon} size={14} color={accentColor ?? colors.mutedForeground} />
        <Text style={[styles.cardLabel, { color: colors.mutedForeground }]}>{label}</Text>
      </View>
      <Text style={[styles.cardValue, { color: colors.foreground }]} numberOfLines={1}>
        {value}
      </Text>
      {sub ? (
        <Text style={[styles.cardSub, { color: colors.mutedForeground }]} numberOfLines={1}>
          {sub}
        </Text>
      ) : null}
    </View>
  );
}

export function StatsOverview({ summary }: { summary: Summary }) {
  const colors = useColors();

  return (
    <View style={{ gap: 10 }}>
      <View style={styles.grid}>
        <StatCard
          icon="calendar"
          label="This Month"
          value={formatBirr(summary.totalThisMonth)}
        />
        <StatCard icon="trending-up" label="All Time" value={formatBirr(summary.totalAllTime)} />
        <StatCard
          icon="zap"
          label="Streak"
          value={`${summary.currentStreakDays}d`}
          sub={`Best ${summary.longestStreakDays}d`}
          accentColor={colors.accent}
        />
        <StatCard
          icon="check-circle"
          label="Active Days"
          value={String(summary.activeDaysCount)}
          sub={
            summary.activeDaysCount > 0
              ? `Avg ${formatBirr(summary.averagePerActiveDay)}`
              : undefined
          }
        />
      </View>

      {summary.bestDay && (
        <View
          style={[
            styles.bestDayBanner,
            { backgroundColor: colors.secondary, borderColor: colors.border },
          ]}
        >
          <View style={styles.bestDayIconWrap}>
            <Feather name="award" size={16} color={colors.accentForeground} />
          </View>
          <View style={{ flex: 1 }}>
            <Text style={[styles.bestDayLabel, { color: colors.mutedForeground }]}>
              Personal best day
            </Text>
            <Text style={[styles.bestDayDate, { color: colors.foreground }]}>
              {formatEthiopianDate(gregorianToEthiopian(summary.bestDay.date))}
            </Text>
          </View>
          <Text style={[styles.bestDayAmount, { color: colors.primary }]}>
            {formatBirr(summary.bestDay.amount)}
          </Text>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  grid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 10,
  },
  card: {
    flexBasis: '47%',
    flexGrow: 1,
    borderWidth: 1,
    borderRadius: 16,
    padding: 14,
    gap: 6,
  },
  cardHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  cardLabel: {
    fontSize: 11,
    fontWeight: '700',
    textTransform: 'uppercase',
    letterSpacing: 0.4,
  },
  cardValue: {
    fontSize: 20,
    fontWeight: '800',
  },
  cardSub: {
    fontSize: 11,
    fontWeight: '500',
  },
  bestDayBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    borderWidth: 1,
    borderRadius: 16,
    padding: 14,
  },
  bestDayIconWrap: {
    width: 34,
    height: 34,
    borderRadius: 17,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'rgba(209,159,71,0.35)',
  },
  bestDayLabel: {
    fontSize: 11,
    fontWeight: '700',
    textTransform: 'uppercase',
    letterSpacing: 0.4,
  },
  bestDayDate: {
    fontSize: 15,
    fontWeight: '700',
    marginTop: 2,
  },
  bestDayAmount: {
    fontSize: 16,
    fontWeight: '800',
  },
});
