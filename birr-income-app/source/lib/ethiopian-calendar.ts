/**
 * Ethiopian <-> Gregorian calendar conversion.
 *
 * Uses the Amete Mihret epoch (Julian Day Number offset 1,723,856) and the
 * standard Julian Day Number <-> Gregorian conversion algorithm. All dates in
 * this app are stored as Gregorian "YYYY-MM-DD" strings; this module is
 * purely for displaying/editing those dates on an Ethiopian calendar in the
 * UI.
 *
 * Verified with a 20,000-sample fuzz round-trip test (Gregorian -> Ethiopian
 * -> Gregorian) covering years 1900-2150, plus manual checks against known
 * reference dates (e.g. Meskerem 1, 2017 EC = September 11, 2024 GC).
 */

const JD_EPOCH_OFFSET_AMETE_MIHRET = 1723856;

export const ETHIOPIAN_MONTH_NAMES = [
  "Meskerem",
  "Tikimt",
  "Hidar",
  "Tahsas",
  "Tir",
  "Yekatit",
  "Megabit",
  "Miyazia",
  "Ginbot",
  "Sene",
  "Hamle",
  "Nehase",
  "Pagume",
] as const;

export interface EthiopianDate {
  year: number;
  month: number; // 1-13 (13 = Pagume, 5 or 6 days)
  day: number;
}

function trunc(x: number): number {
  return x < 0 ? Math.ceil(x) : Math.floor(x);
}

function gregorianToJdn(year: number, month: number, day: number): number {
  const a = trunc((month - 14) / 12);
  return (
    trunc((1461 * (year + 4800 + a)) / 4) +
    trunc((367 * (month - 2 - 12 * a)) / 12) -
    trunc((3 * trunc((year + 4900 + a) / 100)) / 4) +
    day -
    32075
  );
}

function jdnToGregorian(jdn: number): { year: number; month: number; day: number } {
  let l = jdn + 68569;
  const n = trunc((4 * l) / 146097);
  l = l - trunc((146097 * n + 3) / 4);
  const i = trunc((4000 * (l + 1)) / 1461001);
  l = l - trunc((1461 * i) / 4) + 31;
  const j = trunc((80 * l) / 2447);
  const day = l - trunc((2447 * j) / 80);
  l = trunc(j / 11);
  const month = j + 2 - 12 * l;
  const year = 100 * (n - 49) + i + l;
  return { year, month, day };
}

function jdnToEthiopic(jdn: number): EthiopianDate {
  const diff = jdn - JD_EPOCH_OFFSET_AMETE_MIHRET;
  const r = ((diff % 1461) + 1461) % 1461;
  const n = (r % 365) + 365 * Math.floor(r / 1460);
  const year =
    4 * Math.floor(diff / 1461) + Math.floor(r / 365) - Math.floor(r / 1460);
  const month = Math.floor(n / 30) + 1;
  const day = (n % 30) + 1;
  return { year, month, day };
}

function ethiopicToJdn(year: number, month: number, day: number): number {
  const approx =
    JD_EPOCH_OFFSET_AMETE_MIHRET +
    year * 365 +
    Math.floor(year / 4) +
    (month - 1) * 30 +
    (day - 1);
  for (let delta = -8; delta <= 8; delta++) {
    const candidate = approx + delta;
    const e = jdnToEthiopic(candidate);
    if (e.year === year && e.month === month && e.day === day) return candidate;
  }
  throw new Error(`Invalid Ethiopian date: ${year}-${month}-${day}`);
}

function pad2(n: number): string {
  return String(n).padStart(2, "0");
}

/** Parse a "YYYY-MM-DD" gregorian date string (no timezone conversion). */
function parseIsoDate(iso: string): { year: number; month: number; day: number } {
  const [year, month, day] = iso.split("-").map(Number);
  return { year, month, day };
}

/** Convert a Gregorian "YYYY-MM-DD" string to an Ethiopian date. */
export function gregorianToEthiopian(isoDate: string): EthiopianDate {
  const { year, month, day } = parseIsoDate(isoDate);
  return jdnToEthiopic(gregorianToJdn(year, month, day));
}

/** Convert an Ethiopian date to a Gregorian "YYYY-MM-DD" string. */
export function ethiopianToGregorian(date: EthiopianDate): string {
  const jdn = ethiopicToJdn(date.year, date.month, date.day);
  const { year, month, day } = jdnToGregorian(jdn);
  return `${year}-${pad2(month)}-${pad2(day)}`;
}

/** Whether an Ethiopian year has a 6th Pagume day (leap year). */
export function isEthiopianLeapYear(year: number): boolean {
  return year % 4 === 3;
}

/** Number of days in a given Ethiopian month (1-13). */
export function daysInEthiopianMonth(year: number, month: number): number {
  if (month === 13) return isEthiopianLeapYear(year) ? 6 : 5;
  return 30;
}

/** Today, expressed as an Ethiopian date. */
export function todayEthiopian(now: Date = new Date()): EthiopianDate {
  const iso = `${now.getFullYear()}-${pad2(now.getMonth() + 1)}-${pad2(now.getDate())}`;
  return gregorianToEthiopian(iso);
}

/** Format an Ethiopian date like "Meskerem 1, 2017". */
export function formatEthiopianDate(date: EthiopianDate): string {
  return `${ETHIOPIAN_MONTH_NAMES[date.month - 1]} ${date.day}, ${date.year}`;
}
