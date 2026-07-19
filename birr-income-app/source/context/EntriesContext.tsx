import AsyncStorage from '@react-native-async-storage/async-storage';
import React, {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { SEED_ENTRIES } from '@/lib/seed-data';

export interface IncomeEntry {
  date: string; // "YYYY-MM-DD" (Gregorian)
  amount: number;
  note: string | null;
}

export interface RangeSummary {
  from: string;
  to: string;
  total: number;
  count: number;
  average: number;
}

export interface Summary {
  totalAllTime: number;
  totalThisMonth: number;
  currentStreakDays: number;
  longestStreakDays: number;
  activeDaysCount: number;
  averagePerActiveDay: number;
  bestDay: { date: string; amount: number } | null;
  firstEntryDate: string | null;
  lastEntryDate: string | null;
}

interface EntriesContextValue {
  isLoading: boolean;
  entriesByDate: Record<string, IncomeEntry>;
  getEntry: (date: string) => IncomeEntry | undefined;
  upsertEntry: (date: string, amount: number, note: string | null) => Promise<void>;
  deleteEntry: (date: string) => Promise<void>;
  summary: Summary;
  getRangeSummary: (from: string, to: string) => RangeSummary;
}

const STORAGE_KEY = 'birr_income_entries_v1';
const MS_PER_DAY = 24 * 60 * 60 * 1000;

const EntriesContext = createContext<EntriesContextValue | null>(null);

function toUtcMs(isoDate: string): number {
  return Date.parse(`${isoDate}T00:00:00.000Z`);
}

function computeSummary(entries: IncomeEntry[]): Summary {
  if (entries.length === 0) {
    return {
      totalAllTime: 0,
      totalThisMonth: 0,
      currentStreakDays: 0,
      longestStreakDays: 0,
      activeDaysCount: 0,
      averagePerActiveDay: 0,
      bestDay: null,
      firstEntryDate: null,
      lastEntryDate: null,
    };
  }

  const sorted = [...entries].sort((a, b) => (a.date < b.date ? -1 : 1));
  const totalAllTime = sorted.reduce((sum, e) => sum + e.amount, 0);
  const activeDaysCount = sorted.length;
  const averagePerActiveDay = totalAllTime / activeDaysCount;

  const now = new Date();
  const currentMonthPrefix = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
  const totalThisMonth = sorted
    .filter((e) => e.date.startsWith(currentMonthPrefix))
    .reduce((sum, e) => sum + e.amount, 0);

  let bestDay = sorted[0];
  for (const e of sorted) {
    if (e.amount > bestDay.amount) bestDay = e;
  }

  let longestStreakDays = 1;
  let runLength = 1;
  for (let i = 1; i < sorted.length; i++) {
    const gapDays = (toUtcMs(sorted[i].date) - toUtcMs(sorted[i - 1].date)) / MS_PER_DAY;
    runLength = gapDays === 1 ? runLength + 1 : 1;
    if (runLength > longestStreakDays) longestStreakDays = runLength;
  }

  const dateSet = new Set(sorted.map((e) => e.date));
  const todayIso = now.toISOString().slice(0, 10);
  const todayMs = toUtcMs(todayIso);
  const yesterdayIso = new Date(todayMs - MS_PER_DAY).toISOString().slice(0, 10);

  let currentStreakDays = 0;
  let anchorMs: number | null = null;
  if (dateSet.has(todayIso)) anchorMs = todayMs;
  else if (dateSet.has(yesterdayIso)) anchorMs = todayMs - MS_PER_DAY;

  if (anchorMs !== null) {
    let cursorMs = anchorMs;
    while (dateSet.has(new Date(cursorMs).toISOString().slice(0, 10))) {
      currentStreakDays++;
      cursorMs -= MS_PER_DAY;
    }
  }

  return {
    totalAllTime,
    totalThisMonth,
    currentStreakDays,
    longestStreakDays,
    activeDaysCount,
    averagePerActiveDay,
    bestDay: { date: bestDay.date, amount: bestDay.amount },
    firstEntryDate: sorted[0].date,
    lastEntryDate: sorted[sorted.length - 1].date,
  };
}

export function EntriesProvider({ children }: { children: React.ReactNode }) {
  const [isLoading, setIsLoading] = useState(true);
  const [entriesByDate, setEntriesByDate] = useState<Record<string, IncomeEntry>>({});

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const raw = await AsyncStorage.getItem(STORAGE_KEY);
        if (raw) {
          const parsed: IncomeEntry[] = JSON.parse(raw);
          if (!cancelled) {
            const map: Record<string, IncomeEntry> = {};
            for (const e of parsed) map[e.date] = e;
            setEntriesByDate(map);
          }
        } else {
          // First launch: seed from the bundled historical ledger.
          const map: Record<string, IncomeEntry> = {};
          for (const e of SEED_ENTRIES) map[e.date] = e;
          if (!cancelled) setEntriesByDate(map);
          await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(SEED_ENTRIES));
        }
      } finally {
        if (!cancelled) setIsLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  const persist = useCallback(async (map: Record<string, IncomeEntry>) => {
    await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(Object.values(map)));
  }, []);

  const upsertEntry = useCallback(
    async (date: string, amount: number, note: string | null) => {
      setEntriesByDate((prev) => {
        const next = { ...prev, [date]: { date, amount, note } };
        void persist(next);
        return next;
      });
    },
    [persist],
  );

  const deleteEntry = useCallback(
    async (date: string) => {
      setEntriesByDate((prev) => {
        if (!(date in prev)) return prev;
        const next = { ...prev };
        delete next[date];
        void persist(next);
        return next;
      });
    },
    [persist],
  );

  const getEntry = useCallback(
    (date: string) => entriesByDate[date],
    [entriesByDate],
  );

  const summary = useMemo(
    () => computeSummary(Object.values(entriesByDate)),
    [entriesByDate],
  );

  const getRangeSummary = useCallback(
    (from: string, to: string): RangeSummary => {
      const inRange = Object.values(entriesByDate).filter(
        (e) => e.date >= from && e.date <= to,
      );
      const total = inRange.reduce((sum, e) => sum + e.amount, 0);
      const count = inRange.length;
      return { from, to, total, count, average: count > 0 ? total / count : 0 };
    },
    [entriesByDate],
  );

  const value = useMemo<EntriesContextValue>(
    () => ({
      isLoading,
      entriesByDate,
      getEntry,
      upsertEntry,
      deleteEntry,
      summary,
      getRangeSummary,
    }),
    [isLoading, entriesByDate, getEntry, upsertEntry, deleteEntry, summary, getRangeSummary],
  );

  return <EntriesContext.Provider value={value}>{children}</EntriesContext.Provider>;
}

export function useEntries(): EntriesContextValue {
  const ctx = useContext(EntriesContext);
  if (!ctx) throw new Error('useEntries must be used within an EntriesProvider');
  return ctx;
}
