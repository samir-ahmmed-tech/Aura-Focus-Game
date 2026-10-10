# 🛡️ AuraFocus Security & Reliability Architecture

This document outlines the security framework, crash-prevention architecture, and data privacy safeguards implemented in **AuraFocus** to ensure safety, Google Play Store compliance, and resilience under high user concurrency (5,000+ active users without app crashes or memory bricks).

---

## 1. Zero-Trust API Key & Secret Management
* **Secrets Gradle Plugin**: API keys (such as `GEMINI_API_KEY` and third-party tokens) are isolated in local `.env` configuration files and never hardcoded in source files.
* **Git Isolation**: `.env`, `*.env.local`, `*.jks`, and `*.keystore` files are strictly added to `.gitignore`.
* **Zero Leak Guarantee**: No production secrets, tokens, or private credentials are committed to version control.

---

## 2. Input Sanitization & Anti-XSS Protection (`AuraInputSanitizer`)
* **Context Escaping**: All user inputs in Pomodoro task creation, habit tracking, notes, and search queries pass through `AuraInputSanitizer.sanitizeText()` which escapes HTML entities (`<`, `>`, `&`, `"`, `'`) and strips dangerous control characters.
* **Length Bounds Clamping**: All user-entered strings are clamped to a safe maximum length (e.g., 200 characters for task labels, 1,000 characters for notes) preventing buffer overruns and UI rendering stalls.
* **Unicode Normalization**: Zero-width and invisible control characters are sanitized to prevent visual spoofing.

---

## 3. UI Rate Limiting & Rapid Spam Protection
* **Debounced Interaction**: Critical user actions (XP claiming, tree watering, habit toggles, game submissions) are guarded with `AuraInputSanitizer.debounceAction()`.
* **Spam Prevention**: Rapid multi-finger tapping cannot trigger duplicate database writes, double-claims, or race conditions.

---

## 4. Crash Prevention & Memory Stability (5,000+ Concurrency)
* **Lifecycle-Aware Coroutines**: Asynchronous tasks, audio playback, and timers are bound to `viewModelScope` or `rememberCoroutineScope()`. No orphaned threads or memory leaks.
* **Resource Reclamation**: Audio visualizer waveforms and synthetic binaural tone generators strictly implement `onDispose` hooks to release hardware audio sinks immediately when leaving screens.
* **Bounded List Rendering**: All catalogs (26 games, 20 productivity tools, ambient playlists) use Compose `LazyColumn` and `LazyRow` with key-based recycling to prevent out-of-memory (OOM) exceptions even on low-RAM devices (2GB–4GB).
* **Safe State Fallbacks**: Serialization and deserialization in `GamificationManager` uses `try-catch` blocks with defensive default fallback states so corrupted JSON never crashes the app on launch.

---

## 5. MediaStore & Scoped Storage Hardening
* **Principle of Least Privilege**:
  * Android 13+ (API 33+): Only requests granular `READ_MEDIA_AUDIO`.
  * Android 12 and below (API 24–32): Requests `READ_EXTERNAL_STORAGE` with `android:maxSdkVersion="32"`.
* **Scoped Projections**: Queries to Android MediaStore query only required columns (`_ID`, `DISPLAY_NAME`, `TITLE`, `ARTIST`, `DURATION`).
* **Silent Failure Recovery**: If system permission dialogs are suppressed by the OS after prior denials, the app presents a non-intrusive Glassmorphic rationale banner that directly links to Android App Details Settings.

---

## 6. ProGuard / R8 Obfuscation & Shrinking (`proguard-rules.pro`)
* **Code Shrinking**: Dead code and unused Compose runtime components are stripped in release builds.
* **Name Obfuscation**: Package names, internal class structures, and sensitive algorithm names are obfuscated against reverse engineering.
* **Log Stripping**: System logging (`Log.d`, `Log.v`, `Log.i`) is automatically stripped out from release APKs to eliminate telemetry leaks.

---

## 7. AdMob & Monetization Policy Compliance
* **Official Test Unit IDs**: AuraFocus is shipped with Google Mobile Ads test IDs (`ca-app-pub-3940256099942544/...`), preventing accidental self-clicks and account suspensions during development.
* **Accidental Click Prevention**: Banner ad slots maintain dedicated minimum padding and borders to prevent false taps while interacting with game controls or music disks.
* **Zero Policy Violation**: Rewarded ads strictly require explicit user opt-in before invocation.

---

## 8. Summary of Security Matrix
| Checkpoint | Implementation Status | Verification |
| :--- | :--- | :--- |
| **API Keys** | Secrets Gradle Plugin + `.env` | Verified (No hardcoded keys) |
| **XSS & Injection** | `AuraInputSanitizer` | Verified (HTML entity escaping) |
| **Rate Limiting** | Debounce wrappers (350ms window) | Verified |
| **Keystore Safety** | `.gitignore` rules for `*.jks`/`*.keystore` | Verified |
| **Audio Leakage** | `DisposableEffect` releases | Verified |
| **Permission Handling** | Contextual Glass Banner + Fallback Settings | Verified |
| **ProGuard Rules** | Optimized `app/proguard-rules.pro` | Verified |
