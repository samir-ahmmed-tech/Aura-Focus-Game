# 🌌 AuraFocus — Ambient Productivity, Cognitive Gaming & Zen Audio Suite

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF.svg?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202024-4285F4.svg?logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-36-3DDC84.svg?logo=android&logoColor=white)](https://developer.android.com)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![AdMob Ready](https://img.shields.io/badge/Google%20AdMob-Integrated-F4B400.svg?logo=google&logoColor=white)](https://admob.google.com)

**AuraFocus** is a futuristic, all-in-one ambient focus and self-enhancement application built natively with **Modern Jetpack Compose**. Combining neuro-acoustic soundscapes, high-performance cognitive brain training, deep productivity tools, gamified Zen habit tracking, and a customizable glassmorphic visualizer engine, AuraFocus turns daily productivity into an immersive sensory ritual.

---

## ✨ Key Features & Architecture

### 🎵 1. Next-Gen Ambient Music Engine & 5 Visualizer Layouts
* **Zero-Clipping Responsive Layouts**: Redesigned circular cores and horizontal decks with strictly bounded containers, adaptive typography, and overflow handling.
* **5 Selectable Player Layouts** (Switchable instantly from the Hub screen):
  1. **Vinyl Disc Orbit** *(Circular)* — Concentric vinyl grooved turntable with rotating needle and frosted center core.
  2. **Sacred Mandala Orbit** *(Circular)* — Sacred geometry rotating mandalas with pulse glow rings.
  3. **Futuristic Wave Deck** *(Horizontal)* — Linear audio spectrum visualizer with waveform level indicators.
  4. **Retro Synthwave Cassette** *(Horizontal)* — Dual spinning reels with tape counter and vintage neon VU meter.
  5. **Minimalist Glass Soundbar** *(Horizontal)* — Ultra-compact sleek soundbar with scrubbing waveform and telemetry pills.
* **Universal MediaStore Auto-Scanner**:
  * Automatically detects and imports all on-device audio files, downloaded ambient tracks, and voice notes.
  * Granular scoped storage support for Android 13+ (`READ_MEDIA_AUDIO`) and legacy Android (`READ_EXTERNAL_STORAGE`).
* **Interactive Permission Rationale**:
  * In-app glassmorphic permission banner with direct fallback to Android App Settings if system prompts are suppressed.

---

### 🧠 2. 26 Cognitive Brain Games Catalog
Train attention, processing speed, working memory, and mental resilience:
| ID | Game Title | Cognitive Domain | Quotient |
| :--- | :--- | :--- | :--- |
| `g1`–`g22` | Core Cognitive Games | Memory, Speed, Reaction, Stroop, Logic | IQ / MQ / RQ |
| `g23` | **Synapse Speed Sort** *(New)* | Executive Function & Dynamic Rule Switching | EX |
| `g24` | **Spatial Path Trace** *(New)* | Visuospatial Working Memory & Constellation Recall | SQ |
| `g25` | **Dual N-Back Audio Echo** *(New)* | Auditory Frequency 2-Back Working Memory | MQ |
| `g26` | **Focus Radar Saccade** *(New)* | Inhibitory Control & Anti-Saccadic Eye Reflexes | ESQ |

All games sync with the comprehensive **Analytics & History** dashboard, tracking IQ, Executive Quotient (EX), Spatial Quotient (SQ), Emotional Stability (ESQ), Memory Quotient (MQ), and Reaction Quotient (RQ).

---

### 🛠️ 3. 20 Deep Productivity & Mindfulness Tools
Supercharge your daily workflow with biological rhythm tracking and distraction shields:
| ID | Tool Title | Focus Area |
| :--- | :--- | :--- |
| `t1`–`t17` | Pomodoro, Kanban, Matrix, Habits, Tree | Deep Work, Organization, Zen Habit Growth |
| `t18` | **Ultradian Rhythm Cycle** *(New)* | 90-min biological focus waves & 20-min nervous system recovery |
| `t19` | **Digital Distraction Blocker** *(New)* | 10-minute dopamine urge surfing and impulse trigger logger |
| `t20` | **Binaural Soundscape Mixer** *(New)* | 3-channel binaural carrier, organic rain/ocean, and 528Hz Solfeggio tones |

---

### 💎 4. Unified Reusable Glassmorphism Design System
All components live in `com.example.ui.components.GlassComponents.kt`:
* `GlassCard` — Translucent frosted-glass container with specular borders.
* `GlassBanner` — Contextual action rationale alerts.
* `GlassDialog` — Modal overlays with backdrop blur simulation.
* `GlassLinearProgressBar` & `GlassCircularProgressBar` — Neon gradient progress indicators.
* `GlassMetricPill` — Telemetry and status pills.
* `GlassButton`, `GlassChip`, `GlassTextField`, `GlassSlider`.

---

### 🌅 5. Morning Daily Mood & Productivity Check-In
* Prompts the user once every morning upon opening the app.
* Captures emotional energy (Radiant, Calm, Focused, Sluggish, Anxious) and daily focus goals.
* Automatically records logs into the habit tracker and gamification store for long-term psychological analytics.

---

### ⚙️ 6. Customizable Home Landing Screen
* Choose your default home landing tab directly from the **Hub Settings**:
  * 🎯 **Focus** (Pomodoro & Zen Tree)
  * 🎵 **Music** (Ambient Soundscapes & Visualizer)
  * 🎮 **Games** (Cognitive Training Arena)
  * 🛠️ **Tools** (Productivity Powerhouses)
  * ⚙️ **Hub** (Settings & Customization)

---

## 🔒 Security & Crash Prevention (5,000+ Users Certified)
See [SECURITY.md](SECURITY.md) for the full 18-point verification matrix:
1. **Zero Secret Leaks**: Secrets Gradle Plugin isolates keys in `.env`; `.gitignore` locks down keys and `.jks` keystores.
2. **Input Sanitization**: `AuraInputSanitizer` cleanses HTML entities, control codes, and length limits.
3. **UI Rate Limiting**: Debounced click handlers prevent spam and duplicate writes.
4. **Memory Leak Protection**: Strict `DisposableEffect` hooks release audio resources and visualizer loops.
5. **ProGuard / R8 Hardening**: Full rules in `app/proguard-rules.pro` stripping sensitive logs and obfuscating bytecode.

---

## 💰 AdMob Monetization Architecture
AuraFocus comes pre-architected with `AuraAdMobManager` (`com.example.util.AuraAdMobManager.kt`):
* Preconfigured with official **Google Mobile Ads Test IDs**:
  * Banner Test ID: `ca-app-pub-3940256099942544/6300978111`
  * Interstitial Test ID: `ca-app-pub-3940256099942544/1033173712`
  * Rewarded Video Test ID: `ca-app-pub-3940256099942544/5224354917`

### Activating Live AdMob for Google Play:
1. In `app/build.gradle.kts`, ensure Google Mobile Ads SDK is added:
   ```kotlin
   implementation("com.google.android.gms:play-services-ads:23.6.0")
   ```
2. In `app/src/main/AndroidManifest.xml`, replace the test App ID with your AdMob App ID:
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX"/>
   ```
3. In `AuraAdMobManager.kt`, set `isAdMobEnabled = true` and insert your live ad unit IDs.

---

## 🚀 Building & Releasing for Google Play Store

### 1. Generate an Upload Keystore (One-Time Setup)
```bash
keytool -genkey -v -keystore my-upload-key.jks \
  -alias upload -keyalg RSA -keysize 2048 -validity 10000 \
  -dname "CN=AuraFocus, OU=Mobile, O=Aura, L=City, ST=State, C=US" \
  -storepass YOUR_STORE_PASSWORD -keypass YOUR_KEY_PASSWORD
```

### 2. Export Keystore Environment Variables
```bash
export KEYSTORE_PATH="$(pwd)/my-upload-key.jks"
export STORE_PASSWORD="YOUR_STORE_PASSWORD"
export KEY_PASSWORD="YOUR_KEY_PASSWORD"
```

### 3. Build Production Artifacts
* **To build a signed Release APK**:
  ```bash
  ./gradlew assembleRelease
  ```
  *Output:* `app/build/outputs/apk/release/app-release.apk`

* **To build an Android App Bundle (AAB for Google Play Store upload)**:
  ```bash
  ./gradlew bundleRelease
  ```
  *Output:* `app/build/outputs/bundle/release/app-release.aab`

---

## 🛠️ Project Structure
```
Aura-Focus-Game/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml          # Permissions & App configuration
│   │   └── java/com/example/
│   │       ├── MainActivity.kt          # Navigation, Morning Check-in, System Banners
│   │       ├── audio/
│   │       │   └── MediaStoreAudioScanner.kt # Robust on-device audio scanning
│   │       ├── data/
│   │       │   └── GamificationManager.kt   # Persistent state, XP, Tree levels
│   │       ├── ui/
│   │       │   ├── components/
│   │       │   │   └── GlassComponents.kt   # Glassmorphic design library
│   │       │   └── screens/
│   │       │       ├── MusicPlayerScreen.kt # 5 Player layouts & Audio visualizer
│   │       │       ├── BrainGamesScreen.kt  # 26 Cognitive games catalog
│   │       │       ├── ToolsScreen.kt       # 20 Productivity tools catalog
│   │       │       ├── HubScreen.kt         # Customization & Settings
│   │       │       ├── AnalyticsAndHistoryScreen.kt # Quotient tracking
│   │       │       └── ZenGardenScreen.kt   # Tree & Habit growth
│   │       └── util/
│   │           ├── AuraAdMobManager.kt      # AdMob architecture
│   │           └── AuraInputSanitizer.kt    # Anti-XSS, rate limiting & security
│   ├── proguard-rules.pro               # ProGuard / R8 optimization rules
│   └── build.gradle.kts                 # Dependencies and build configuration
├── .gitignore                           # Git ignore rules protecting keys
├── SECURITY.md                          # 18-point security and reliability audit
└── README.md                            # Complete documentation
```

---

## 🤝 Contributing & License
Developed with passion for mindfulness and high-performance focus. Distributed under the **MIT License**.
