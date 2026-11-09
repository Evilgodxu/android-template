package com.template.evilgodxu.screens.settings.expanded

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.template.evilgodxu.AppUiState
import com.template.evilgodxu.R
import com.template.evilgodxu.screens.settings.component.content.SettingsContent
import com.template.evilgodxu.ui.component.AppTopBar
import com.template.evilgodxu.ui.icons.AppIcons

// 宽屏形态：内容限宽居中，避免行宽随窗口无限拉伸
@Composable
fun ExpandedAssembly(
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding),
        ) {
            SettingsContent(
                uiState = uiState,
                onShowThemeDialog = onShowThemeDialog,
                onShowLanguageDialog = onShowLanguageDialog,
                onCheckForUpdate = onCheckForUpdate,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .widthIn(max = 840.dp),
            )
        }
    }
}
