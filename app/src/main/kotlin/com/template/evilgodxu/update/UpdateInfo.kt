package com.template.evilgodxu.update

// 线上可用的更新包信息：版本号、更新内容、下载地址与 GitHub 提供的 SHA-256
data class UpdateInfo(
    val version: String,
    val changelog: String,
    val apkUrl: String,
    val apkSize: Long,
    val apkSha256: String,
)

// 安装包本地文件名：取下载地址最后一段，剔除查询串，兜底为固定名
val UpdateInfo.apkFileName: String
    get() = apkUrl.substringAfterLast('/').substringBefore('?').ifBlank { "update.apk" }

// 检查结果：已是最新 / 有新版本 / 检查失败 / 版本更新但缺少可用安装包
sealed interface UpdateCheckResult {
    data object UpToDate : UpdateCheckResult

    data class Available(val info: UpdateInfo) : UpdateCheckResult

    data object CheckFailed : UpdateCheckResult

    data object IncompleteRelease : UpdateCheckResult
}
