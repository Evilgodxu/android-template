package com.template.evilgodxu.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.template.evilgodxu.screens.home.compact.HomeCompactAssembly
import com.template.evilgodxu.screens.home.expanded.HomeExpandedAssembly
import com.template.evilgodxu.update.LocalAppUpdateViewModel
import com.template.evilgodxu.windowsize.WindowSizeClass
import com.template.evilgodxu.windowsize.rememberWindowSizeClass

// 页面入口：触发每日更新检查，按窗口尺寸类分发形态，不含布局
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit,
) {
    DailyUpdateCheckEffect()
    when (rememberWindowSizeClass()) {
        WindowSizeClass.Compact -> HomeCompactAssembly(modifier, onOpenSettings)
        WindowSizeClass.Medium, WindowSizeClass.Expanded -> HomeExpandedAssembly(modifier, onOpenSettings)
    }
}

// 进入首页触发每日首次自动检查；发现新版本交由应用级更新对话框呈现，自动检查本身静默
@Composable
private fun DailyUpdateCheckEffect() {
    val updateViewModel = LocalAppUpdateViewModel.current
    LaunchedEffect(Unit) { updateViewModel.checkDailyOnce() }
}
