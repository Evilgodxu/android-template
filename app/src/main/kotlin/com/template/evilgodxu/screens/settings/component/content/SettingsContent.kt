package com.template.evilgodxu.screens.settings.component.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.template.evilgodxu.AppUiState
import com.template.evilgodxu.screens.settings.component.appearance.Appearance
import com.template.evilgodxu.screens.settings.component.info.AppInfo
import com.template.evilgodxu.screens.settings.component.language.Language

// 设置项列表：两种形态共用同一份内容，形态差异只体现在外层的宽度约束
@Composable
fun SettingsContent(
    uiState: AppUiState,
    onShowThemeDialog: (Offset) -> Unit,
    onShowLanguageDialog: () -> Unit,
    onCheckForUpdate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Appearance(themeMode = uiState.themeMode, onThemeClick = onShowThemeDialog)
        Language(language = uiState.language, onShowDialog = onShowLanguageDialog)
        AppInfo(version = uiState.version, onCheckForUpdate = onCheckForUpdate)
    }
}
