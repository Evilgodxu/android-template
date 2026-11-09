package com.template.evilgodxu.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.template.evilgodxu.screens.home.compact.CompactAssembly
import com.template.evilgodxu.screens.home.expanded.ExpandedAssembly
import com.template.evilgodxu.update.LocalAppUpdateViewModel
import com.template.evilgodxu.windowsize.WindowSizeClass
import com.template.evilgodxu.windowsize.rememberWindowSizeClass

// 页面入口：承载跨形态副作用，按窗口尺寸类分派形态，不含布局
@Composable
fun HomeScreen(
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DailyUpdateCheckEffect()
    when (rememberWindowSizeClass()) {
        WindowSizeClass.Compact -> CompactAssembly(onOpenSettings = onOpenSettings, modifier = modifier)
        WindowSizeClass.Medium, WindowSizeClass.Expanded ->
            ExpandedAssembly(onOpenSettings = onOpenSettings, modifier = modifier)
    }
}

// 每日检查的应用级动作，故不进页面 ViewModel，避免仅做转发的中间层
@Composable
private fun DailyUpdateCheckEffect() {
    val updateViewModel = LocalAppUpdateViewModel.current
    LaunchedEffect(Unit) { updateViewModel.checkDailyOnce() }
}
