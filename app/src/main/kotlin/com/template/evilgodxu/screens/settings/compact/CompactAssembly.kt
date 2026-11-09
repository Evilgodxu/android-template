package com.template.evilgodxu.screens.settings.compact

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
import com.template.evilgodxu.screens.settings.component.content.SettingsContent
import com.template.evilgodxu.ui.component.AppTopBar
import com.template.evilgodxu.ui.icons.AppIcons

// 窄屏形态：全宽单列，内容占满可用宽度
@Composable
fun CompactAssembly(
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
        SettingsContent(
            uiState = uiState,
            onShowThemeDialog = onShowThemeDialog,
            onShowLanguageDialog = onShowLanguageDialog,
            onCheckForUpdate = onCheckForUpdate,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding),
        )
    }
}
