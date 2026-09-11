<div align="center">

# Template

**A modern, out-of-the-box Android app template built with Jetpack Compose, MVVM and a modular architecture.**

**English** | [简体中文](README.zh-CN.md)

![License](https://img.shields.io/badge/license-MIT-green)
![Platform](https://img.shields.io/badge/platform-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-2.4.20-purple)
![AGP](https://img.shields.io/badge/AGP-9.4.0-blue)
![Gradle](https://img.shields.io/badge/Gradle-9.7.0-blue)
![Compose BOM](https://img.shields.io/badge/Compose%20BOM-2026.08.00-blue)
![minSdk](https://img.shields.io/badge/minSdk-33-orange)
![targetSdk](https://img.shields.io/badge/targetSdk-37-orange)

</div>

**Template** is a production-ready starting point for new Android apps. The project skeleton and everyday plumbing are already wired up, so you can focus on business features instead of boilerplate.

## Features

- **Single-Activity architecture** with Jetpack Compose + Material 3, edge-to-edge rendering, and per-orientation system-bar visibility
- **MVVM with unidirectional data flow (UDF)** — app-wide state (theme / language / version) is aggregated in an Activity-scoped `TemplateActivityViewModel` and consumed across the tree via a `CompositionLocal`; each screen adds its own immutable `UiState` exposed as a `StateFlow`. Events flow up, state flows down
- **Atomic UI decomposition + adaptive assemblies** — the screen entry dispatches by window size class to a `CompactAssembly` / `ExpandedAssembly`, which composes self-contained, single-responsibility components from `component/`; components depend strictly downward and never couple back to the assembly
- **Navigation3** with typed routes and an explicit back stack (double-back-to-exit on the root)
- **Manual DI** — the `Application` owns a single `AppContainer`; the Activity and ViewModels pull dependencies from it
- **DataStore Preferences** persistence as the single source of truth behind a repository
- **Theme modes** — System / Light / Dark, with a circular reveal transition animation when switching
- **In-app language switching** — 简体中文 / English / Follow System, hot-swapped at runtime without recreating the Activity (per-language resource bundles disabled so switching always works)
- **Crash log manager** — uncaught and caught exceptions written to app-specific external storage, chaining to the system default handler; today's log can be shared from the Settings page
- **Version update check** — the Settings page queries the latest GitHub release and prompts when a newer version is available
- **Optimized build setup** — R8 + resource shrinking for release, signed release build, `arm64-v8a`-only ABI filter, deterministic APK naming

## Screens

| Screen | Contents |
| --- | --- |
| Home | Welcome and project overview cards, entry to Settings |
| Settings | Appearance (theme), Language, and About (version, share today's crash log, check for updates, GitHub link) |

## Tech Stack

| Layer | Technology |
| --- | --- |
| Language | Kotlin 2.4.20 |
| UI | Jetpack Compose (BOM 2026.08.00) + Material 3 |
| Navigation | AndroidX Navigation3 1.1.7 |
| DI | Manual DI (app-level `AppContainer`) |
| Persistence | DataStore Preferences 1.2.1 |
| Serialization | kotlinx.serialization 1.11.0 |
| Lifecycle | androidx.lifecycle 2.11.0, activity-compose 1.13.0 |
| Build | AGP 9.4.0, Gradle 9.7.0, refreshVersions 0.60.6 |

## Project Structure

```
.
├── app/
│   └── src/main/
│       ├── kotlin/com/template/evilgodxu/
│       │   ├── data/                    # Data layer (single source of truth)
│       │   │   ├── repository/          #   SettingsRepository contract + DataStore impl
│       │   │   └── settings/            #   Settings keys, enums & state
│       │   ├── log/                     # CrashLogManager (crash & exception logging)
│       │   ├── navigation/              # Navigation3 typed routes + NavHost
│       │   ├── screens/                 # Feature modules
│       │   │   ├── home/                #   Home (Screen + ViewModel + UiState + assemblies)
│       │   │   │   ├── compact/         #     Narrow-window assembly
│       │   │   │   └── expanded/        #     Wide-window assembly
│       │   │   └── settings/            #   Settings feature
│       │   │       ├── compact/         #     Narrow-window assembly
│       │   │       ├── expanded/        #     Wide-window assembly
│       │   │       ├── content/         #     Screen-level composition unit (shared by assemblies)
│       │   │       └── dialog/          #     Screen-specific dialogs (theme/language selection)
│       │   ├── theme/                   # Material 3 color scheme & typography
│       │   ├── localization/            # In-app localization (LocalizationManager)
│       │   ├── windowSize/              # Window size classes
│       │   ├── ui/                      # Cross-screen shared UI
│       │   │   ├── component/           #   Atomic layout components (SectionCard/AppTopBar/…)
│       │   │   ├── icons/               #   Vector icons
│       │   │   └── dialog/              #   SingleChoiceDialog
│       │   ├── update/                  # Latest-release check (GitHub API)
│       │   ├── AppContainer.kt          # Manual DI container (app-level)
│       │   ├── TemplateActivity.kt
│       │   ├── TemplateActivityViewModel.kt   # Activity-scoped global UI state holder
│       │   ├── TemplateAppUiState.kt          # App-level UI state
│       │   └── TemplateApplication.kt
│       └── res/                         # Resources (values / values-en)
├── gradle/
│   ├── libs.versions.toml               # Version catalog (dependencies)
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Architecture

### State management — MVVM + UDF

The app follows **MVVM with unidirectional data flow (UDF)**, forming a closed loop where state flows down and events flow up. State is split across two tiers:

- **App-level global state** — `TemplateActivityViewModel` (Activity-scoped) aggregates app-wide UI state (`themeMode`, `language`, `version`) into the immutable `TemplateAppUiState`, exposed as a `StateFlow` and provided to the UI tree via the `LocalTemplateActivityViewModel` `CompositionLocal`. Theme, localization and screen UIs all consume this single source; the UI layer never touches the data source directly.
- **Screen-level local state** — each screen's `{ScreenName}ViewModel` owns a `MutableStateFlow<{ScreenName}UiState>` as its single source of UI truth, exposed as an immutable `StateFlow` (e.g. `HomeViewModel` + `HomeUiState`).
- **Model** — the repository layer. `SettingsRepository` abstracts `DataStore Preferences`, which is the single source of truth for persisted settings; it is manually constructed and injected via `AppContainer` (swap-friendly for tests). User intents are received as plain methods (`setThemeMode`, `setLanguage`) and written back through the repository.

The typical flow: `DataStore → Repository → ViewModel → UiState → UI` for state, and the reverse path for events.

### UI composition — atomic decomposition + adaptive assemblies

Code is organized with a **modular pattern driven by window size classes**, mirroring Material's adaptive guidance:

- `{ScreenName}Screen.kt` — a thin screen entry that hoists state and events, dispatches to an assembly by window size class, and hosts cross-form effects. It contains **no layout code**.
- `{ScreenName}CompactAssembly.kt` / `{ScreenName}ExpandedAssembly.kt` — own screen-level layout scaffolding (Scaffold, top bar, scroll container) and **assemble reusable atomic components**. The displayed form is decided jointly by window size class and screen rotation state; `if`-based layout branching is avoided.
- `ui/component/` — atomic, single-responsibility UI units (`Welcome`, `About`, `Appearance`, `AppInfo`, `SectionCard`, `AppTopBar`, …) named by semantics rather than generic suffixes. Dependencies point strictly downward: an assembly may compose components, but a component never composes back into an assembly, so the tree stays uncoupled. Atomic components live together in top-level `ui/component/`; screen-level composition units and screen-specific dialogs stay in the feature package.

DI is manual: `TemplateApplication` builds a fully-populated `AppContainer` (DataStore, repository, localization manager, version) at startup, and the Activity / ViewModels pull dependencies from it (ViewModels get constructor args via a `viewModelFactory`). No framework or reflection.

Shared cross-feature code is hoisted to the top level (`data/`, `ui/`, `theme/`, `localization/`, `windowSize/`, `log/`, `update/`); code used by a single feature stays inside that feature module.

## Getting Started

### Prerequisites

- JDK 21
- Android Studio (latest stable recommended)
- Android SDK with API 37 (`compileSdk`)

### Build

```bash
git clone https://github.com/Evilgodxu/android-template.git
cd android-template

# Debug build
./gradlew assembleDebug

# Release build (requires signing config, see below)
./gradlew assembleRelease
```

APKs are emitted as `Template-<versionName>-arm64.apk` under `app/build/outputs/apk/`.

### Release Signing

The release build reads signing credentials from `local.properties` in the project root:

```properties
KEYSTORE_PASSWORD=your_store_password
KEY_ALIAS=jh
KEY_PASSWORD=your_key_password
```

The keystore file is expected at `jh.keystore` in the project root (adjust `storeFile` in `app/build.gradle.kts` if needed). `jh.keystore` and `local.properties` are git-ignored — never commit them.

## Customizing the Template

- **Rename the application / package**: update `namespace` and `applicationId` in `app/build.gradle.kts`, move the Kotlin sources under `app/src/main/kotlin/`, and update the manifest. Avoid a name collision with the existing `com.template.evilgodxu`.
- **App name**: edit `app_name` in `app/src/main/res/values/strings.xml`.
- **Theme colors**: edit `app/src/main/kotlin/.../theme/Color.kt`.
- **Supported ABIs**: adjust `ndk.abiFilters` in `app/build.gradle.kts` (currently `arm64-v8a`).
- **Add a new screen**: create a `screens/<name>/` feature module with its `UiState` + `ViewModel` + `Compact`/`Expanded` assemblies and atomic components under `ui/component/`, register the route in `navigation/Screen.kt`, and add it to `AppNavHost`.

## License

[MIT](LICENSE) © 2026 Evilgodxu
