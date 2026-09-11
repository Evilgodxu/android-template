package com.template.evilgodxu.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.template.evilgodxu.screens.home.compact.HomeCompactAssembly
import com.template.evilgodxu.screens.home.expanded.HomeExpandedAssembly
import com.template.evilgodxu.windowSize.WindowSizeClass
import com.template.evilgodxu.windowSize.rememberWindowSizeClass

// 页面入口：按窗口尺寸类分发形态，不含布局
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit,
) {
    when (rememberWindowSizeClass()) {
        WindowSizeClass.Compact -> HomeCompactAssembly(modifier, onOpenSettings)
        WindowSizeClass.Medium, WindowSizeClass.Expanded -> HomeExpandedAssembly(modifier, onOpenSettings)
    }
}