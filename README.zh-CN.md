<div align="center">

# Template

**一个开箱即用的现代化 Android 应用模板，基于 Jetpack Compose、MVVM 与模块化架构构建。**

[English](README.md) | **简体中文**

![License](https://img.shields.io/badge/license-MIT-green)
![Platform](https://img.shields.io/badge/platform-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-2.4.20-purple)
![AGP](https://img.shields.io/badge/AGP-9.4.0-blue)
![Gradle](https://img.shields.io/badge/Gradle-9.7.0-blue)
![Compose BOM](https://img.shields.io/badge/Compose%20BOM-2026.08.00-blue)
![minSdk](https://img.shields.io/badge/minSdk-33-orange)
![targetSdk](https://img.shields.io/badge/targetSdk-37-orange)

</div>

**Template** 是一个可直接上手的 Android 应用起点工程。工程骨架与常用基础设施已搭建完毕，你可以专注于业务功能，而非重复搭建样板代码。

## 特性

- **单 Activity 架构**：基于 Jetpack Compose + Material 3，支持边到边（edge-to-edge）渲染，并按屏幕方向显隐系统栏
- **MVVM + 单向数据流（UDF）**：应用级状态（主题 / 语言 / 版本）由 Activity 作用域的 `TemplateActivityViewModel` 聚合，经 `CompositionLocal` 供界面树消费；各页面另以不可变 `UiState` 经 `StateFlow` 暴露。事件自下而上、状态自上而下
- **原子化 UI 拆分 + 自适应组装器**：页面入口按窗口尺寸类分派到 `CompactAssembly` / `ExpandedAssembly`，由组装器组合 `component/` 下语义单一、自包含的组件；组件仅向下依赖、绝不反向耦合到组装器
- **Navigation3 导航**：类型安全路由 + 显式返回栈（根页面支持双击返回退出）
- **手动依赖注入**：`Application` 持有一站式 `AppContainer`，Activity 与 ViewModel 从容器取依赖
- **DataStore Preferences 持久化**：作为仓库层背后的唯一数据源（single source of truth）
- **主题模式**：跟随系统 / 浅色 / 深色，切换时带圆形扩散过渡动效
- **应用内多语言**：简体中文 / English / 跟随系统，运行时热切换、无需重建 Activity（已禁用按语言分包，保证切换始终生效）
- **崩溃日志管理**：未捕获异常与捕获异常写入应用专属外部目录，并链式调用系统默认处理器；设置页可一键分享今日日志
- **版本更新检查**：设置页查询 GitHub 最新 Release，发现新版本时提示
- **构建优化**：Release 启用 R8 + 资源压缩、签名构建，仅打 `arm64-v8a` ABI，APK 输出命名固定

## 页面

| 页面 | 内容 |
| --- | --- |
| 首页 | 欢迎与项目简介卡片，设置入口 |
| 设置 | 外观（主题）、语言、关于（版本、分享今日崩溃日志、检查更新、GitHub 链接） |

## 技术栈

| 层次 | 技术 |
| --- | --- |
| 语言 | Kotlin 2.4.20 |
| UI | Jetpack Compose（BOM 2026.08.00）+ Material 3 |
| 导航 | AndroidX Navigation3 1.1.7 |
| 依赖注入 | 手动 DI（Application 级 `AppContainer`） |
| 持久化 | DataStore Preferences 1.2.1 |
| 序列化 | kotlinx.serialization 1.11.0 |
| 生命周期 | androidx.lifecycle 2.11.0、activity-compose 1.13.0 |
| 构建 | AGP 9.4.0、Gradle 9.7.0、refreshVersions 0.60.6 |

## 项目结构

```
.
├── app/
│   └── src/main/
│       ├── kotlin/com/template/evilgodxu/
│       │   ├── data/                    # 数据层（唯一数据源）
│       │   │   ├── repository/          #   SettingsRepository 契约 + DataStore 实现
│       │   │   └── settings/            #   设置键、枚举与状态
│       │   ├── log/                     # 崩溃与异常日志（CrashLogManager）
│       │   ├── navigation/              # Navigation3 类型安全路由 + 导航宿主
│       │   ├── screens/                 # 页面模块
│       │   │   ├── home/                #   首页（Screen + ViewModel + UiState + 形态组装器）
│       │   │   │   ├── compact/         #     窄屏组装器
│       │   │   │   └── expanded/        #     宽屏组装器
│       │   │   └── settings/            #   设置页
│       │   │       ├── compact/         #     窄屏组装器
│       │   │       ├── expanded/        #     宽屏组装器
│       │   │       ├── content/         #     页面级组装单元（各尺寸组装器复用）
│       │   │       └── dialog/          #     页面专用弹窗（主题/语言选择）
│       │   ├── theme/                   # Material 3 配色与字体
│       │   ├── localization/            # 应用内多语言管理（LocalizationManager）
│       │   ├── windowSize/              # 窗口尺寸类
│       │   ├── ui/                      # 跨页面复用 UI
│       │   │   ├── component/           #   原子化布局组件（SectionCard/AppTopBar/通用组件）
│       │   │   ├── icons/               #   矢量图标
│       │   │   └── dialog/              #   单选弹窗（SingleChoiceDialog）
│       │   ├── update/                  # 最新版本检查（GitHub API）
│       │   ├── AppContainer.kt          # 手动 DI 容器（Application 级）
│       │   ├── TemplateActivity.kt
│       │   ├── TemplateActivityViewModel.kt   # Activity 作用域全局 UI 状态持有者
│       │   ├── TemplateAppUiState.kt          # 应用级 UI 状态
│       │   └── TemplateApplication.kt
│       └── res/                         # 资源（values / values-en）
├── gradle/
│   ├── libs.versions.toml               # 版本目录（依赖管理）
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## 架构

### 状态管理 —— MVVM + 单向数据流

应用采用 **MVVM + 单向数据流（UDF）**，状态自上而下流动、事件自下而上传递，形成闭环。状态分两层管理：

- **应用级全局状态**：`TemplateActivityViewModel`（Activity 作用域）将应用级 UI 状态（`themeMode`、`language`、`version`）聚合进不可变的 `TemplateAppUiState`，以 `StateFlow` 暴露，并通过 `LocalTemplateActivityViewModel` `CompositionLocal` 提供给界面树；主题、本地化与各页面 UI 共同消费这一唯一状态源，UI 层不直连数据源。
- **页面级局部状态**：每个页面的 `{ScreenName}ViewModel` 持有 `MutableStateFlow<{ScreenName}UiState>` 作为 UI 唯一状态源，对外暴露不可变 `StateFlow`（如 `HomeViewModel` + `HomeUiState`）。
- **Model（仓库层）**：`SettingsRepository` 抽象了 `DataStore Preferences`，持久化设置以 DataStore 为唯一事实源，经 `AppContainer` 手动构造注入便于测试替换；用户意图以普通方法接收（`setThemeMode`、`setLanguage`），经仓库写回。

典型流向：`DataStore → Repository → ViewModel → UiState → UI`（状态），事件则沿相反路径上行。

### UI 组合 —— 原子化拆分 + 自适应组装

代码遵循 **按窗口尺寸类拆分的模块化模式**（对齐 Material 自适应指南）：

- `{ScreenName}Screen.kt` —— 轻量页面入口：提升（hoist）状态与事件、按窗口尺寸类分派到组装器，并承载跨形态副作用，**不含布局代码**。
- `{ScreenName}CompactAssembly.kt` / `{ScreenName}ExpandedAssembly.kt` —— 负责页面级布局骨架（Scaffold、顶栏、滚动容器），并**组装可复用的原子组件**。显示形态由窗口尺寸类与屏幕旋转状态共同决定，避免 `if` 式布局分支。
- `ui/component/` —— 原子化、单一职责的 UI 单元（`Welcome`、`About`、`Appearance`、`AppInfo`、`SectionCard`、`AppTopBar` 等），按语义命名而非泛化后缀。依赖严格向下：组装器可组合组件，组件绝不反向组合进组装器，组件树因此保持解耦。原子组件统一置于顶层 `ui/component/`，页面级组装单元与页面专用弹窗保留在页面包内。

依赖注入采用手动 DI：`TemplateApplication` 在启动时构造成员齐全的 `AppContainer`（DataStore、仓库、语言管理器、版本号），`TemplateActivity` 与 ViewModel 自容器取依赖（ViewModel 经 `viewModelFactory` 注入构造参数），无框架反射。

通用能力被多个页面复用时上提至顶层（`data/`、`ui/`、`theme/`、`localization/`、`windowSize/`、`log/`、`update/`）；仅单页使用的代码保留在页面模块内。

## 快速开始

### 环境要求

- JDK 21
- Android Studio（建议最新稳定版）
- 包含 API 37（`compileSdk`）的 Android SDK

### 构建

```bash
git clone https://github.com/Evilgodxu/android-template.git
cd android-template

# 调试包
./gradlew assembleDebug

# 发布包（需先配置签名，见下文）
./gradlew assembleRelease
```

APK 输出为 `app/build/outputs/apk/` 下的 `Template-<版本号>-arm64.apk`。

### 发布签名

Release 构建从项目根目录的 `local.properties` 读取签名凭据：

```properties
KEYSTORE_PASSWORD=你的签名库密码
KEY_ALIAS=jh
KEY_PASSWORD=你的别名密码
```

签名库文件默认位于项目根目录 `jh.keystore`（如需调整请修改 `app/build.gradle.kts` 中的 `storeFile`）。`jh.keystore` 与 `local.properties` 已被 git 忽略，请勿提交。

## 定制模板

- **修改应用名 / 包名**：同步修改 `app/build.gradle.kts` 中的 `namespace` 与 `applicationId`，移动 `app/src/main/kotlin/` 下对应源码目录，并更新 Manifest，避免与现有 `com.template.evilgodxu` 冲突
- **应用显示名**：修改 `app/src/main/res/values/strings.xml` 中的 `app_name`
- **主题配色**：修改 `app/src/main/kotlin/.../theme/Color.kt`
- **支持的 ABI**：调整 `app/build.gradle.kts` 中的 `ndk.abiFilters`（当前仅 `arm64-v8a`）
- **新增页面**：在 `screens/<name>/` 下创建页面模块（`UiState` + `ViewModel` + `Compact`/`Expanded` 组装器，原子组件放 `ui/component/`），在 `navigation/Screen.kt` 注册路由，并加入 `AppNavHost`

## License

[MIT](LICENSE) © 2026 Evilgodxu
