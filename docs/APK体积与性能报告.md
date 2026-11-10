# APK 体检报告：Template-1.0.0-arm64.apk

- 应用：`com.template.evilgodxu` 1.0.0
- 体积：1.04 MiB（1087093 字节）
- 条目：33，容器开销 31.7 KiB
- 构建工具：build-tools 37.0.0
- ZIP 对齐：通过
- 后处理可回收：67 B

## 结论

- **后处理已无空间**：可回收 67 B，产物已按最优形式打包，重写只会更换签名块而不带来收益。
- **运行性能无法由后处理提升**：R8 在编译期完成代码优化、DEX 布局与 Baseline Profile 生成，改写其产物会导致优化失效甚至性能回退。
- **构建期缺口 2 项**：
  - Startup Profile / DEX 布局优化（未启用）：缺少 startup-prof.txt，启动路径的类会散落在 DEX 中并触发额外缺页
  - 签名方案（待确认）：未显式声明 v1/v2/v3 方案，不同构建工具版本的默认值可能变化

## 体积构成

| 分组 | 条目 | 原始 | 占用 | 占比 |
| --- | ---: | ---: | ---: | ---: |
| `classes.dex` | 1 | 1.79 MiB | 939.8 KiB | 91.2% |
| `res/` | 14 | 34.5 KiB | 33.5 KiB | 3.3% |
| `lib/**/*.so` | 2 | 20.0 KiB | 20.0 KiB | 1.9% |
| `resources.arsc` | 1 | 16.3 KiB | 16.3 KiB | 1.6% |
| `kotlin/` | 8 | 52.7 KiB | 12.1 KiB | 1.2% |
| `assets/` | 2 | 6.2 KiB | 6.2 KiB | 0.6% |
| `AndroidManifest.xml` | 1 | 7.2 KiB | 2.0 KiB | 0.2% |
| `META-INF/` | 4 | 182 B | 179 B | 0.0% |

## 最大条目

| 条目 | 原始 | 占用 | 方式 |
| --- | ---: | ---: | --- |
| `classes.dex` | 1.79 MiB | 939.8 KiB | DEFLATE |
| `resources.arsc` | 16.3 KiB | 16.3 KiB | STORED |
| `lib/arm64-v8a/libdatastore_shared_counter.so` | 10.1 KiB | 10.1 KiB | STORED |
| `lib/arm64-v8a/libandroidx.graphics.path.so` | 9.9 KiB | 9.9 KiB | STORED |
| `res/-6.webp` | 7.6 KiB | 7.6 KiB | STORED |
| `res/j_.webp` | 5.8 KiB | 5.8 KiB | STORED |
| `assets/dexopt/baseline.prof` | 5.5 KiB | 5.5 KiB | STORED |
| `kotlin/kotlin.kotlin_builtins` | 30.8 KiB | 5.4 KiB | DEFLATE |
| `res/u5.webp` | 3.8 KiB | 3.8 KiB | STORED |
| `res/sK.webp` | 3.8 KiB | 3.8 KiB | STORED |
| `res/fq.webp` | 2.8 KiB | 2.8 KiB | STORED |
| `res/Sn.webp` | 2.8 KiB | 2.8 KiB | STORED |
| `AndroidManifest.xml` | 7.2 KiB | 2.0 KiB | DEFLATE |
| `res/qs.webp` | 1.9 KiB | 1.9 KiB | STORED |
| `kotlin/collections/collections.kotlin_builtins` | 8.4 KiB | 1.8 KiB | DEFLATE |

## 存储方式偏差

| 条目 | 体积变化 | 原因 |
| --- | ---: | --- |
| `META-INF/services/cl` | -2 B | deflate 无收益，改回未压缩存储以减少解压开销 |
| `META-INF/services/q4` | -2 B | deflate 无收益，改回未压缩存储以减少解压开销 |
| `kotlin/collections/collections.kotlin_builtins` | -18 B | 已 deflate，压缩级别提升至 9 |
| `kotlin/kotlin.kotlin_builtins` | -36 B | 已 deflate，压缩级别提升至 9 |
| `kotlin/reflect/reflect.kotlin_builtins` | -9 B | 已 deflate，压缩级别提升至 9 |

## 构建期优化清单

| 项 | 状态 | 说明 | 参考 |
| --- | --- | --- | --- |
| R8 代码缩减与优化 | 已启用 | 已通过 optimization { enable = true } 启用，R8 会移除未引用代码并重写热点逻辑 | [官方文档](https://developer.android.com/topic/performance/app-optimization/enable-app-optimization) |
| 资源缩减 | 已启用 | 新 DSL 下优化型资源缩减随 optimization{} 默认启用，代码与资源引用会被合并裁剪 | [官方文档](https://developer.android.com/topic/performance/app-optimization/enable-app-optimization) |
| R8 完整模式 | 已启用 | 未显式关闭，R8 使用完整优化集合 | [官方文档](https://developer.android.com/topic/performance/app-optimization/enable-app-optimization) |
| 资源限定符裁剪 | 已启用 | 已声明语言限定符，未支持语言的库资源不会进入包内 | [官方文档](https://developer.android.com/topic/performance/reduce-apk-size) |
| ABI 维度收敛 | 已启用 | 已限定 ABI，避免多套原生库同时入包 | [官方文档](https://developer.android.com/topic/performance/reduce-apk-size) |
| 原生库打包方式 | 已启用 | 原生库保持未压缩，安装时无需解压即可 mmap 加载 | [官方文档](https://developer.android.com/guide/practices/page-sizes) |
| 16 KiB 页对齐 | 已启用 | 未压缩条目已按 4 字节与 16 KiB 页双重要求对齐，兼容 4 KiB 与 16 KiB 页设备 | [官方文档](https://developer.android.com/tools/zipalign) |
| Baseline Profile | 已启用 | 包内含 assets/dexopt/baseline.prof，ART 会在安装期预编译热点路径 | [官方文档](https://developer.android.com/topic/performance/baselineprofiles/overview) |
| Startup Profile / DEX 布局优化 | 未启用 | 缺少 startup-prof.txt，启动路径的类会散落在 DEX 中并触发额外缺页 | [官方文档](https://developer.android.com/topic/performance/startupprofiles/dex-layout-optimizations) |
| 签名方案 | 待确认 | 未显式声明 v1/v2/v3 方案，不同构建工具版本的默认值可能变化 | [官方文档](https://developer.android.com/tools/apksigner) |
| 打包排除规则 | 已启用 | 已排除重复的 META-INF 元数据与第三方签名文件 | [官方文档](https://developer.android.com/topic/performance/reduce-apk-size) |