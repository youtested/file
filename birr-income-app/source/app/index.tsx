import React, { useState } from 'react';
import { ScrollView, StyleSheet, Text, View } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useColors } from '@/hooks/useColors';
import { Feather } from '@expo/vector-icons';
import { EntriesProvider, useEntries } from '@/context/EntriesContext';
import { StatsOverview } from '@/components/StatsOverview';
import { MonthCalendar } from '@/components/MonthCalendar';
import { RangeAnalyzer } from '@/components/RangeAnalyzer';
import { EntryModal } from '@/components/EntryModal';

function HomeScreen() {
  const colors = useColors();
  const insets = useSafeAreaInsets();
  const { isLoading, summary } = useEntries();
  const [selectedDate, setSelectedDate] = useState<string | null>(null);

  return (
    <View style={{ flex: 1, backgroundColor: colors.background }}>
      <ScrollView
        contentContainerStyle={{
          paddingTop: insets.top + 20,
          paddingBottom: insets.bottom + 40,
          paddingHorizontal: 16,
          gap: 18,
        }}
        showsVerticalScrollIndicator={false}
      >
        <View style={styles.headerRow}>
          <View style={[styles.logoMark, { backgroundColor: colors.primary }]}>
            <Feather name="book-open" size={18} color={colors.primaryForeground} />
          </View>
          <View>
            <Text style={[styles.title, { color: colors.foreground }]}>Birr Ledger</Text>
            <Text style={[styles.subtitle, { color: colors.mutedForeground }]}>
              Your daily income diary
            </Text>
          </View>
        </View>

        {isLoading ? (
          <View style={styles.loadingBox}>
            <Text style={{ color: colors.mutedForeground }}>Loading your ledger…</Text>
          </View>
        ) : (
          <>
            <StatsOverview summary={summary} />
            <MonthCalendar onSelectDate={setSelectedDate} />
            <RangeAnalyzer />
          </>
        )}
      </ScrollView>

      <EntryModal isoDate={selectedDate} onClose={() => setSelectedDate(null)} />
    </View>
  );
}

export default function Index() {
  return (
    <EntriesProvider>
      <HomeScreen />
    </EntriesProvider>
  );
}

const styles = StyleSheet.create({
  headerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
  },
  logoMark: {
    width: 40,
    height: 40,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    fontSize: 24,
    fontWeight: '800',
  },
  subtitle: {
    fontSize: 13,
    fontWeight: '500',
    marginTop: 1,
  },
  loadingBox: {
    paddingVertical: 60,
    alignItems: 'center',
  },
});
