package com.template.evilgodxu.windowsize

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

// 窗口宽度尺寸类：compact <600dp、medium 600-839dp、expanded >=840dp
enum class WindowSizeClass {
    Compact,
    Medium,
    Expanded,
}

// 读取当前窗口宽度尺寸类，配置变化时自动重组
@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val widthDp = LocalConfiguration.current.screenWidthDp
    return when {
        widthDp >= 840 -> WindowSizeClass.Expanded
        widthDp >= 600 -> WindowSizeClass.Medium
        else -> WindowSizeClass.Compact
    }
}