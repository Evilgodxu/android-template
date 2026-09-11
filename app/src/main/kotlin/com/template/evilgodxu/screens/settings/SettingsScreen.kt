package com.template.evilgodxu.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.evilgodxu.LocalTemplateActivityViewModel
import com.template.evilgodxu.screens.settings.compact.SettingsCompactAssembly
import com.template.evilgodxu.screens.settings.expanded.SettingsExpandedAssembly
import com.template.evilgodxu.theme.LocalThemeTransitionController
import com.template.evilgodxu.windowSize.WindowSizeClass
import com.template.evilgodxu.windowSize.rememberWindowSizeClass

// 页面入口：装配状态、按窗口尺寸类分发形态与跨形态副作用，不含布局
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appUiState by LocalTemplateActivityViewModel.current.uiState.collectAsStateWithLifecycle()
    val activityViewModel = LocalTemplateActivityViewModel.current
    val onThemeClick: (Offset) -> Unit = LocalThemeTransitionController.current::revealAt
    when (rememberWindowSizeClass()) {
        WindowSizeClass.Compact -> SettingsCompactAssembly(
            uiState = appUiState,
            updateCheck = activityViewModel.updateCheck,
            onBack = onBack,
            onThemeSelected = activityViewModel::setThemeMode,
            onLanguageSelected = activityViewModel::setLanguage,
            onThemeClick = onThemeClick,
            onCheckForUpdate = activityViewModel::checkForUpdate,
            modifier = modifier,
        )
        WindowSizeClass.Medium, WindowSizeClass.Expanded -> SettingsExpandedAssembly(
            uiState = appUiState,
            updateCheck = activityViewModel.updateCheck,
            onBack = onBack,
            onThemeSelected = activityViewModel::setThemeMode,
            onLanguageSelected = activityViewModel::setLanguage,
            onThemeClick = onThemeClick,
            onCheckForUpdate = activityViewModel::checkForUpdate,
            modifier = modifier,
        )
    }
}