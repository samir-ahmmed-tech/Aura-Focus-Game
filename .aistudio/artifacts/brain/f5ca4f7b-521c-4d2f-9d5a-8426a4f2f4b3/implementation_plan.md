# AuraFocus: Ambient Productivity — Master Implementation Plan

A 100% offline-first Android super-productivity suite engineered with Jetpack Compose, Room SQLite, and a Glassmorphism design system. It brings together an Ambient Focus Space with local audio playback, interactive Kanban & To-Do management, GitHub-style streak habit tracking, deep UI customization, and full JSON database export/import.

## User Review & Critical Decisions

> [!IMPORTANT]
> **Confirmed Choices from Phase 1 Interview:**
> - **Platform & Engine:** Native Android Jetpack Compose with Room SQLite for maximum battery efficiency, zero latency, and hardware audio playback.
> - **App Name:** **AuraFocus: Ambient Productivity** (`res/values/strings.xml` and `metadata.json` updated in Phase 1).
> - **Default Home View:** **Ambient Focus Space with Pomodoro Timer** as the premier landing experience, with seamless bottom navigation into Kanban Tasks, Habit Heatmaps, and the Customization Hub.
> - **Custom HTML/CSS Snippet Translation:** Any HTML/CSS snippets for *"Ambient Sessions"*, *"Edge Settings"*, and *"3D Card Decks"* will be translated into native Jetpack Compose glassmorphic components (`drawBehind`, radial/linear gradient brushes, card rotation depth, and backdrop blurs).

---

## 1. Overview & Core Concept

- **What It Does**: AuraFocus is a distraction-free, zero-cloud personal operating system for work and wellness. It merges time-boxing, task execution, habit building, and ambient acoustic focus into a unified, glassmorphic workspace that functions completely offline.
- **Target Audience**: Developers, creators, students, and deep-work professionals who demand zero cloud lock-in, data privacy, and a responsive, beautiful mobile interface.
- **Key Value**: 
  - Complete data sovereignty (Room SQLite on-device storage + JSON export/import).
  - Multi-sensory focus (Pomodoro cycle + built-in generative ambient sounds + local MP3/WAV playback via Android SAF).
  - Flexible productivity paradigms (agile Kanban boards, recurring checklists, and GitHub-style habit contribution matrices).

---

## 2. User Experience & Visual Design

### Visual Identity & Theme
- **Aesthetic Direction**: Premium Glassmorphism with Cyber Neon and Aurora Pastel accents on an ultra-deep obsidian background (`#0A0B10`). Frosted acrylic cards, translucent borders (`#FFFFFF` with 8–18% alpha), and subtle radial ambient glows behind primary active elements.
- **Dynamic Themes**:
  1. *Aurora Dark (Default)*: Deep indigo-black base with teal (`#00F2FE`) and violet (`#4FACFE`) glass glows.
  2. *Cyber Neon*: Pure obsidian (`#07080D`) with electric lime (`#00FF87`) and magenta (`#60EFFF`) accents.
  3. *Solar Light*: Translucent frosted quartz (`#F8FAFC`) with deep slate typography and warm amber glows.
- **Typography & Hierarchy**: Bold headline numerals for the countdown clock, rounded clean sans-serif for task headers, and monospace badges for priority tags and timer intervals.
- **Micro-Interactions**:
  - Haptic feedback on timer triggers, task completion checkmarks, and Kanban card moves.
  - 3D card elevation tilt and scale spring animations (`spring(dampingRatio = 0.75f)`).
  - Pulsing circular progress rings that breathe during focus mode.

### Canonical Layout & Navigation
- **Navigation**: Clean glass floating bottom bar with icons for:
  1. ⏱️ **Focus**: Pomodoro timer, ambient soundscapes, local audio player dock.
  2. 📋 **Tasks**: Kanban boards (Backlog, In Progress, Review, Done) & quick To-Do list.
  3. 📈 **Habits**: Daily streak grid (GitHub-style heat matrix) & consistency graphs.
  4. ⚙️ **Hub**: Customization (toggles, themes, glass opacity, backup JSON import/export).
- **Edge-to-Edge**: Full edge-to-edge rendering with proper `WindowInsets.safeDrawing` and navigation bar padding.
- **Monetization Container**: Dedicated non-intrusive bottom banner slot, hidden/empty by default, ready for zero-friction AdMob activation in the final phase.

---

## 3. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        MainActivity & Theme                            │
│           (AuraFocusTheme, Glassmorphic Scaffold, BottomNav)           │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
    ┌───────────────────────────────┼──────────────────────────────┐
    ▼                               ▼                              ▼
┌──────────────────┐      ┌──────────────────┐      ┌────────────────────┐
│   Focus Space    │      │   Smart Tasks    │      │   Habits & Stats   │
│ ──────────────── │      │ ──────────────── │      │ ────────────────── │
│ • Pomodoro Engine│      │ • Kanban Board   │      │ • GitHub Grid (SVG)│
│ • Ambient Audio  │      │ • Task List      │      │ • Streak Analytics │
│ • Local MP3 Dock │      │ • Subtasks/Tags  │      │ • Consistency Chart│
└─────────┬────────┘      └─────────┬────────┘      └──────────┬─────────┘
          │                         │                          │
          └─────────────────────────┼──────────────────────────┘
                                    ▼
               ┌────────────────────────────────────────┐
               │         AuraFocus ViewModel            │
               │   (StateFlow, Audio Service, AudioSAF) │
               └────────────────────┬───────────────────┘
                                    ▼
               ┌────────────────────────────────────────┐
               │          AuraFocus Repository          │
               │       (Room Database + JSON IO)        │
               └────────────────────┬───────────────────┘
                                    ▼
               ┌────────────────────────────────────────┐
               │              Room SQLite               │
               │  • TaskEntity      • HabitEntity       │
               │  • HabitLogEntity  • SessionLogEntity  │
               │  • SettingsEntity  • AudioTrackEntity  │
               └────────────────────────────────────────┘
```

### Data Entities & Schema
1. **TaskEntity**: `id`, `title`, `description`, `column` (BACKLOG, IN_PROGRESS, REVIEW, DONE), `priority` (LOW, MEDIUM, HIGH, URGENT), `dueDate`, `isCompleted`, `isRecurring`, `subtasksJson`, `createdAt`.
2. **HabitEntity**: `id`, `title`, `category`, `frequencyDays`, `colorHex`, `targetPerDay`, `createdAt`, `archived`.
3. **HabitLogEntity**: `id`, `habitId`, `date` (YYYY-MM-DD), `count`, `completed`.
4. **SessionLogEntity**: `id`, `type` (FOCUS, SHORT_BREAK, LONG_BREAK), `durationMinutes`, `completedAt`, `interruptedCount`.
5. **SettingsEntity**: `key`, `value` (theme, glass opacity, widget visibility flags, pomodoro intervals).

---

## 4. Master Phased Task List

Below is the complete project task list structured phase-by-phase. We will execute tasks sequentially with your explicit confirmation before proceeding across milestones.

### Phase 1: Foundation, App Identity & Glassmorphism Design System
- [x] **Task 1.1**: App Identity Setup — Configure `metadata.json` and `res/values/strings.xml` for "AuraFocus: Ambient Productivity".
- [x] **Task 1.2**: Glassmorphism Compose Design System — Build reusable `GlassCard`, `GlassSurface`, frosted backdrop modifiers, linear/radial gradient brushes, neon accent tokens, and dynamic theme switching in `Theme.kt`.
- [x] **Task 1.3**: Main Screen Navigation Shell — Implement edge-to-edge `Scaffold` with floating glass bottom navigation, safe-insets handling, and placeholder AdMob monetization container.

### Phase 2: Offline Persistence & Local Engine (Room SQLite)
- [ ] **Task 2.1**: Room Database Architecture — Define `AppDatabase`, Entities (`TaskEntity`, `HabitEntity`, `HabitLogEntity`, `SessionLogEntity`), and DAOs with reactive Kotlin `Flow` queries.
- [ ] **Task 2.2**: AuraFocus Repository & JSON Backup Engine — Implement CRUD operations, initial seed data, and a full JSON export/import system for complete offline backups.

### Phase 3: Ambient Focus Space (Pomodoro & 20+ Feature Audio Engine)
- [x] **Task 3.1**: Pomodoro Countdown & Infinite Flow Engine — Interval controller with customizable minutes slider, unlimited flow mode (♾️), pause, skip, reset, and glowing progress disc.
- [x] **Task 3.2**: 100% Real Binaural Brainwave Synthesizer & Soundscapes — Real dual-channel audio frequency entrainment (Gamma 40Hz, Beta 20Hz, Alpha 10Hz, Theta 6Hz, Delta 2Hz) and synthesized Rain/Stream/Pink noise via AudioTrack.
- [x] **Task 3.3**: 20+ Feature Music & Soundscape Studio Player — 15 standard music player features + 5 unique killer features (3-layer audio mixer, breathwork pacer, sleep timer, SAF audio import, speed control).

### Phase 4: Smart Task Management & 10 Productivity Tools
- [x] **Task 4.1**: Multi-Column Kanban Board & To-Do — Visual columns (Backlog, In Progress, Review, Done) with priority tags, status chips, and quick column shifting.
- [x] **Task 4.2**: 10 Productivity Power Tools Suite — Smart Kanban, Deep Habits, Daily Reflection Journal, Quick Notes, Eisenhower Matrix, Focus Session Logger, Breathwork Pacer, Stoic Quotes, Distraction Vault, and Database IO.

### Phase 5: Deep Habit Tracker & 10 Brain Mini-Games
- [x] **Task 5.1**: 20+ Feature Habit Tracker — 16-week GitHub heatmap, streak shields, time-of-day filters, batch check-in, completion notes, and streak milestone records.
- [x] **Task 5.2**: 10 Brain & Focus Mini-Games — Mental Math Sprint, Stroop Color Clash, Memory Matrix, Speed Sequence, Micro-Reaction, Digit Span, Anagrams, Spatial Rotation, Dual N-Back, and Zen Bubble Popper.

### Phase 6: Customization Hub (50+ Settings) & Phone Music Scanner
- [x] **Task 6.1**: Phone Music Auto-Scanner — MediaStore audio permission flow (`READ_MEDIA_AUDIO` / `READ_EXTERNAL_STORAGE`) to automatically scan and list all songs on phone.
- [x] **Task 6.2**: 50+ Settings Hub — 30 Focus & Productivity settings + 20 Normal Music Player settings with clean category toggles.
- [x] **Task 6.3**: Gamification Level & Reward System — Level tiers, XP progress meter header, streak flame, and unlockable achievement badges.

### Phase 7: Polish, Verification & Monetization Container Preparation
- [ ] **Task 7.1**: Micro-Interactions, Haptics & Accessibility — Add tactile vibration pulses, smooth spring animations, and `testTag` identifiers.
- [ ] **Task 7.2**: Final AdMob Container Hookup & Build Verification — Verify clean compile, robust offline performance, and complete feature coverage.
