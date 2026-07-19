# Birr Income Tracker - Blueprint

## Overview

Birr Income Tracker (formerly Birr Ledger) is a **React Native (Expo) Android app** for tracking daily income with Ethiopian calendar support. Built with Expo SDK 54 and React Native 0.81.

**App name:** Birr Income Tracker  
**Package ID:** `com.birr.income.tracker`  
**Version:** 1.0.0  
**Target arch:** armeabi-v7a

---

## Project Structure

```
birr-income-app/
├── source/                      # All source code
│   ├── app/                     # Expo Router pages
│   │   ├── _layout.tsx          # Root layout (providers, fonts, splash)
│   │   ├── index.tsx            # Main home screen
│   │   └── +not-found.tsx       # 404 fallback
│   ├── components/              # React Native UI components
│   │   ├── EntryModal.tsx       # Add/edit income entry modal
│   │   ├── ErrorBoundary.tsx    # Error boundary wrapper
│   │   ├── ErrorFallback.tsx    # Error fallback UI
│   │   ├── KeyboardAwareScrollViewCompat.tsx
│   │   ├── MonthCalendar.tsx    # Ethiopian month calendar
│   │   ├── RangeAnalyzer.tsx    # Date range analysis
│   │   ├── SelectField.tsx      # Dropdown select field
│   │   └── StatsOverview.tsx    # Income statistics cards
│   ├── constants/
│   │   └── colors.ts            # Theme colors
│   ├── context/
│   │   └── EntriesContext.tsx    # Global entries state (React Context)
│   ├── hooks/
│   │   └── useColors.ts         # Theme color hook
│   ├── lib/
│   │   ├── ethiopian-calendar.ts # Ethiopian date conversion
│   │   └── seed-data.ts         # Sample/seed data
│   ├── assets/
│   │   └── images/icon.png      # App icon
│   ├── android/                 # Android native code (Gradle + Kotlin)
│   │   ├── app/
│   │   │   ├── build.gradle     # App-level Gradle config
│   │   │   ├── proguard-rules.pro
│   │   │   ├── debug.keystore   # Debug signing
│   │   │   ├── release.keystore # Release signing (pass: birr123)
│   │   │   └── src/
│   │   │       ├── main/
│   │   │       │   ├── AndroidManifest.xml
│   │   │       │   ├── java/com/birr/income/tracker/
│   │   │       │   │   ├── MainActivity.kt
│   │   │       │   │   └── MainApplication.kt
│   │   │       │   └── res/     # Android resources (icons, styles, colors)
│   │   │       ├── debug/AndroidManifest.xml
│   │   │       └── debugOptimized/AndroidManifest.xml
│   │   ├── build.gradle         # Root Gradle config
│   │   ├── settings.gradle
│   │   ├── gradle.properties    # Build properties (arch, SDK versions)
│   │   └── gradle/wrapper/
│   │       └── gradle-wrapper.properties
│   ├── server/
│   │   ├── serve.js             # Static production server
│   │   └── templates/landing-page.html
│   ├── scripts/
│   │   └── build.js             # Expo static build script
│   ├── package.json             # Dependencies (npm/pnpm)
│   ├── pnpm-lock.yaml           # pnpm lockfile
│   ├── pnpm-workspace.yaml
│   ├── app.json                 # Expo config
│   ├── tsconfig.json
│   ├── babel.config.js
│   └── metro.config.js
├── releases/                    # Built APKs
│   └── v1.0.0/
│       └── BirrIncomeTracker-armeabi-v7a-release.apk (33MB)
├── BLUEPRINT.md                 # This file
└── .gitignore
```

---

## How It Was Built

### 1. Tech Stack

| Layer | Technology |
|---|---|
| Framework | React Native (Expo SDK 54) |
| Language | TypeScript, Kotlin |
| Routing | Expo Router v6 |
| State | React Context + TanStack React Query |
| UI | react-native-reanimated, gesture-handler, SVG |
| Calendar | Custom Ethiopian calendar lib |
| Icons | @expo/vector-icons (Feather) |
| Fonts | Inter (Google Fonts via expo-font) |
| Android Build | Gradle 8.14.3, AGP, Kotlin |
| JS Engine | Hermes |
| Packaging | pnpm (workspace) |

### 2. Key Android Build Settings

From `source/android/gradle.properties`:
- **SDK:** compileSdk, targetSdk from Expo defaults
- **Architecture:** `armeabi-v7a` only (smaller APK)
- **New Architecture:** enabled (Fabric renderer)
- **Hermes:** enabled (faster JS execution)
- **Minify:** enabled via R8
- **Edge-to-edge:** enabled

From `source/android/app/build.gradle`:
- **Signing:** Release keystore (`release.keystore`, alias `birr-income`, pass `birr123`)
- **Application ID:** `com.birr.income.tracker`

### 3. Build Steps

#### Prerequisites
- Node.js >= 18
- pnpm (`npm install -g pnpm`)
- Android Studio / Android SDK (API 35+)
- Java 17+ (for Gradle)
- Environment variables if building on Replit:
  - `REPLIT_DEV_DOMAIN` or `EXPO_PUBLIC_DOMAIN`

#### Step 1: Install dependencies
```bash
cd source
pnpm install
```

#### Step 2: Prebuild (generate Android native project)
```bash
pnpm exec expo prebuild --platform android --clean
```

#### Step 3: Build APK
```bash
cd android
./gradlew assembleRelease
```
APK output: `android/app/build/outputs/apk/release/app-release.apk`

#### Step 4: Locate the APK
```bash
ls -lh android/app/build/outputs/apk/release/app-release.apk
```

### 4. Build Script (Alternative)

The included `scripts/build.js` is designed for **Replit deployment** and creates a static Expo Go deployment (not a standalone APK). It:
1. Starts Metro bundler
2. Downloads JS bundles for iOS/Android
3. Downloads assets
4. Generates manifests
5. Creates a landing page

**For standalone APK builds, use the Gradle method above instead.**

### 5. Development
```bash
cd source
pnpm exec expo start
```

---

## How the App Works

### Data Flow
1. `EntriesContext` (React Context) manages all income entries
2. Entries are stored in **AsyncStorage** (local persistence)
3. `StatsOverview` shows total income, current month, etc.
4. `MonthCalendar` displays dates with income indicators (Ethiopian calendar)
5. `RangeAnalyzer` lets users analyze income over custom date ranges
6. `EntryModal` handles adding/editing individual entries

### Key Features
- **Ethiopian Calendar** - Full month views with Ethiopian date system
- **Income Tracking** - Add, edit, delete daily income entries
- **Statistics** - Overview cards showing totals, averages, trends
- **Range Analysis** - Filter and analyze income between dates
- **Dark Mode** - Automatic theme support (light/dark based on system)

---

## Key Files to Edit

| File | Purpose |
|---|---|
| `source/app/index.tsx` | Main screen layout and components |
| `source/context/EntriesContext.tsx` | Data management logic |
| `source/components/EntryModal.tsx` | Entry form UI and validation |
| `source/lib/ethiopian-calendar.ts` | Ethiopian date conversion logic |
| `source/lib/seed-data.ts` | Default/sample data |
| `source/constants/colors.ts` | Theme colors |
| `source/android/app/build.gradle` | Android build config |
| `source/android/gradle.properties` | Build properties (arch, SDK) |
| `source/package.json` | Dependencies and scripts |
| `source/app.json` | Expo app config (name, icon, package) |

---

## Rebuilding the APK After Changes

```bash
cd source
pnpm install                    # if deps changed
pnpm exec expo prebuild --platform android --clean  # regen native
cd android
./gradlew assembleRelease       # build APK
```

The new APK will be at `source/android/app/build/outputs/apk/release/app-release.apk`.
