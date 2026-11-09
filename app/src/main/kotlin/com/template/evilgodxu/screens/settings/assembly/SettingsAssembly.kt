package com.template.evilgodxu.screens.settings.assembly

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import com.template.evilgodxu.AppUiState
import com.template.evilgodxu.R
import com.template.evilgodxu.screens.settings.component.appearance.Appearance
import com.template.evilgodxu.screens.settings.component.info.AppInfo
import com.template.evilgodxu.screens.settings.component.language.Language
import com.template.evilgodxu.ui.component.AppTopBar
import com.template.evilgodxu.ui.component.ContentPane
import com.template.evilgodxu.ui.icons.AppIcons
import com.template.evilgodxu.windowsize.WindowSizeClass
import com.template.evilgodxu.windowsize.contentMaxWidth

// 设置页组装器：骨架为 Scaffold + 单栏内容区，形态差异经 ContentPane 的行宽上限表达
@Composable
fun SettingsAssembly(
    windowSizeClass: WindowSizeClass,
    uiState: AppUiState,
    onBack: () -> Unit,
    onShowThemeDialog: (Offset) -> Unit,
    onShowLanguageDialog: () -> Unit,
    onCheckForUpdate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.settings_title),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(AppIcons.ChevronLeft, stringResource(R.string.back))
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        ContentPane(
            maxContentWidth = windowSizeClass.contentMaxWidth,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding),
        ) {
            Appearance(themeMode = uiState.themeMode, onThemeClick = onShowThemeDialog)
            Language(language = uiState.language, onShowDialog = onShowLanguageDialog)
            AppInfo(version = uiState.version, onCheckForUpdate = onCheckForUpdate)
        }
    }
}
