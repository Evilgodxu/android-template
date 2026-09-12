package com.template.evilgodxu.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.template.evilgodxu.LocalMainViewModel
import com.template.evilgodxu.data.settings.AppLanguage
import com.template.evilgodxu.data.settings.ThemeMode
import com.template.evilgodxu.screens.settings.compact.SettingsCompactAssembly
import com.template.evilgodxu.screens.settings.expanded.SettingsExpandedAssembly
import com.template.evilgodxu.theme.LocalThemeTransitionController
import com.template.evilgodxu.update.LocalAppUpdateViewModel
import com.template.evilgodxu.windowsize.WindowSizeClass
import com.template.evilgodxu.windowsize.rememberWindowSizeClass

// 页面入口：装配状态、按窗口尺寸类分发形态与跨形态副作用，不含布局
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val activityViewModel = LocalMainViewModel.current
    val updateViewModel = LocalAppUpdateViewModel.current
    // 页面级 ViewModel：作用域跟随导航条目，随页面出栈回收
    val settingsViewModel: SettingsViewModel = viewModel()
    val appUiState by activityViewModel.uiState.collectAsStateWithLifecycle()
    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val onThemeReveal: (Offset) -> Unit = LocalThemeTransitionController.current::revealAt

    // 选中主题：先从点击位置播放圆形揭示动效，再写入主题并关闭弹窗
    val onThemeSelected: (ThemeMode) -> Unit = { mode ->
        onThemeReveal(settingsUiState.pendingThemeClickPosition)
        activityViewModel.setThemeMode(mode)
        settingsViewModel.dismissThemeDialog()
    }
    // 选中语言：写入语言并关闭弹窗
    val onLanguageSelected: (AppLanguage) -> Unit = { language ->
        settingsViewModel.dismissLanguageDialog()
        activityViewModel.setLanguage(language)
    }

    when (rememberWindowSizeClass()) {
        WindowSizeClass.Compact -> SettingsCompactAssembly(
            uiState = appUiState,
            settingsUiState = settingsUiState,
            updateMessages = updateViewModel.messages,
            onBack = onBack,
            onShowThemeDialog = settingsViewModel::showThemeDialog,
            onDismissThemeDialog = settingsViewModel::dismissThemeDialog,
            onThemeSelected = onThemeSelected,
            onShowLanguageDialog = settingsViewModel::showLanguageDialog,
            onDismissLanguageDialog = settingsViewModel::dismissLanguageDialog,
            onLanguageSelected = onLanguageSelected,
            onCheckForUpdate = updateViewModel::checkNow,
            modifier = modifier,
        )
        WindowSizeClass.Medium, WindowSizeClass.Expanded -> SettingsExpandedAssembly(
            uiState = appUiState,
            settingsUiState = settingsUiState,
            updateMessages = updateViewModel.messages,
            onBack = onBack,
            onShowThemeDialog = settingsViewModel::showThemeDialog,
            onDismissThemeDialog = settingsViewModel::dismissThemeDialog,
            onThemeSelected = onThemeSelected,
            onShowLanguageDialog = settingsViewModel::showLanguageDialog,
            onDismissLanguageDialog = settingsViewModel::dismissLanguageDialog,
            onLanguageSelected = onLanguageSelected,
            onCheckForUpdate = updateViewModel::checkNow,
            modifier = modifier,
        )
    }
}
