import React, { useMemo, useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { useColors } from '@/hooks/useColors';
import { Feather } from '@expo/vector-icons';
import {
  daysInEthiopianMonth,
  ETHIOPIAN_MONTH_NAMES,
  ethiopianToGregorian,
  todayEthiopian,
} from '@/lib/ethiopian-calendar';
import { useEntries } from '@/context/EntriesContext';
import { SelectField } from '@/components/SelectField';

function formatBirr(amount: number): string {
  return `ETB ${Math.round(amount).toLocaleString('en-US')}`;
}

const MONTH_OPTIONS = ETHIOPIAN_MONTH_NAMES.map((name, i) => ({ label: name, value: i + 1 }));

export function RangeAnalyzer() {
  const colors = useColors();
  const { getRangeSummary } = useEntries();
  const today = useMemo(() => todayEthiopian(), []);

  const [fromMonth, setFromMonth] = useState(today.month);
  const [fromDay, setFromDay] = useState(1);
  const [fromYear, setFromYear] = useState(today.year);

  const [toMonth, setToMonth] = useState(today.month);
  const [toDay, setToDay] = useState(today.day);
  const [toYear, setToYear] = useState(today.year);

  const dayOptionsFrom = useMemo(
    () =>
      Array.from({ length: daysInEthiopianMonth(fromYear, fromMonth) }, (_, i) => ({
        label: String(i + 1),
        value: i + 1,
      })),
    [fromYear, fromMonth],
  );
  const dayOptionsTo = useMemo(
    () =>
      Array.from({ length: daysInEthiopianMonth(toYear, toMonth) }, (_, i) => ({
        label: String(i + 1),
        value: i + 1,
      })),
    [toYear, toMonth],
  );

  const yearOptions = useMemo(
    () =>
      Array.from({ length: 6 }, (_, i) => today.year - 4 + i).map((y) => ({
        label: String(y),
        value: y,
      })),
    [today.year],
  );

  const fromIso = ethiopianToGregorian({ year: fromYear, month: fromMonth, day: Math.min(fromDay, dayOptionsFrom.length) });
  const toIso = ethiopianToGregorian({ year: toYear, month: toMonth, day: Math.min(toDay, dayOptionsTo.length) });

  const orderedFrom = fromIso <= toIso ? fromIso : toIso;
  const orderedTo = fromIso <= toIso ? toIso : fromIso;
  const result = getRangeSummary(orderedFrom, orderedTo);

  return (
    <View style={[styles.card, { backgroundColor: colors.card, borderColor: colors.border }]}>
      <View style={styles.header}>
        <Feather name="bar-chart-2" size={16} color={colors.primary} />
        <Text style={[styles.title, { color: colors.foreground }]}>Range analysis</Text>
      </View>

      <Text style={[styles.sectionLabel, { color: colors.mutedForeground }]}>From date</Text>
      <View style={styles.selectRow}>
        <SelectField label="Month" value={fromMonth} options={MONTH_OPTIONS} onChange={setFromMonth} />
        <SelectField label="Day" value={fromDay} options={dayOptionsFrom} onChange={setFromDay} />
        <SelectField label="Year" value={fromYear} options={yearOptions} onChange={setFromYear} />
      </View>

      <Text style={[styles.sectionLabel, { color: colors.mutedForeground, marginTop: 14 }]}>
        To date
      </Text>
      <View style={styles.selectRow}>
        <SelectField label="Month" value={toMonth} options={MONTH_OPTIONS} onChange={setToMonth} />
        <SelectField label="Day" value={toDay} options={dayOptionsTo} onChange={setToDay} />
        <SelectField label="Year" value={toYear} options={yearOptions} onChange={setToYear} />
      </View>

      <View style={[styles.resultBox, { backgroundColor: colors.secondary, borderColor: colors.border }]}>
        <View style={styles.resultRow}>
          <Text style={[styles.resultLabel, { color: colors.mutedForeground }]}>Total income</Text>
          <Text style={[styles.resultTotal, { color: colors.primary }]}>
            {formatBirr(result.total)}
          </Text>
        </View>
        <View style={styles.resultMetaRow}>
          <View>
            <Text style={[styles.metaValue, { color: colors.foreground }]}>{result.count}</Text>
            <Text style={[styles.metaLabel, { color: colors.mutedForeground }]}>Active days</Text>
          </View>
          <View>
            <Text style={[styles.metaValue, { color: colors.foreground }]}>
              {formatBirr(result.average)}
            </Text>
            <Text style={[styles.metaLabel, { color: colors.mutedForeground }]}>Avg / day</Text>
          </View>
        </View>
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
    gap: 8,
    marginBottom: 14,
  },
  title: {
    fontSize: 17,
    fontWeight: '700',
  },
  sectionLabel: {
    fontSize: 11,
    fontWeight: '700',
    textTransform: 'uppercase',
    letterSpacing: 0.4,
    marginBottom: 8,
  },
  selectRow: {
    flexDirection: 'row',
    gap: 8,
  },
  resultBox: {
    marginTop: 18,
    borderWidth: 1,
    borderRadius: 16,
    padding: 16,
    gap: 14,
  },
  resultRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  resultLabel: {
    fontSize: 13,
    fontWeight: '600',
  },
  resultTotal: {
    fontSize: 22,
    fontWeight: '800',
  },
  resultMetaRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  metaValue: {
    fontSize: 16,
    fontWeight: '700',
  },
  metaLabel: {
    fontSize: 11,
    fontWeight: '600',
    marginTop: 2,
  },
});
