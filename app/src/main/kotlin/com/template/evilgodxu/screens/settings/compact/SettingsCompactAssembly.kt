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
import com.template.evilgodxu.UpdateCheckOutcome
import com.template.evilgodxu.R
import com.template.evilgodxu.data.settings.AppLanguage
import com.template.evilgodxu.data.settings.ThemeMode
import com.template.evilgodxu.screens.settings.content.SettingsContent
import com.template.evilgodxu.ui.component.AppTopBar
import com.template.evilgodxu.ui.icons.AppIcons
import kotlinx.coroutines.flow.StateFlow

// 设置页窄屏组装器：全宽单列布局
@Composable
fun SettingsCompactAssembly(
    uiState: AppUiState,
    updateCheck: StateFlow<UpdateCheckOutcome?>,
    onBack: () -> Unit,
    onThemeSelected: (ThemeMode) -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
    onThemeClick: (Offset) -> Unit,
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
            updateCheck = updateCheck,
            onThemeSelected = onThemeSelected,
            onLanguageSelected = onLanguageSelected,
            onThemeClick = onThemeClick,
            onCheckForUpdate = onCheckForUpdate,
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding),
        )
    }
}