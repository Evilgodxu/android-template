package com.template.evilgodxu.screens.settings.content

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.evilgodxu.AppUiState
import com.template.evilgodxu.UpdateCheckOutcome
import com.template.evilgodxu.R
import com.template.evilgodxu.data.settings.AppLanguage
import com.template.evilgodxu.data.settings.ThemeMode
import com.template.evilgodxu.screens.settings.dialog.LanguageSelectionDialog
import com.template.evilgodxu.screens.settings.dialog.ThemeSelectionDialog
import com.template.evilgodxu.ui.component.AppInfo
import com.template.evilgodxu.ui.component.Appearance
import com.template.evilgodxu.ui.component.Language
import com.template.evilgodxu.update.AppUpdateChecker
import kotlinx.coroutines.flow.StateFlow

// 页面级组装单元：设置列表 + 弹窗状态 + 更新检查结果反馈，供各尺寸组装器复用
@Composable
fun SettingsContent(
    uiState: AppUiState,
    updateCheck: StateFlow<UpdateCheckOutcome?>,
    onThemeSelected: (ThemeMode) -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
    onThemeClick: (Offset) -> Unit,
    onCheckForUpdate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var pendingThemeClickPosition by remember { mutableStateOf(Offset.Zero) }

    // 观察版本检查结果，完成后以 Toast 反馈（含失败与已是最新两种情况）
    val context = LocalContext.current
    val outcome by updateCheck.collectAsStateWithLifecycle()
    LaunchedEffect(outcome) {
        val result = outcome ?: return@LaunchedEffect
        val message = when {
            result.latest == null -> context.getString(R.string.settings_update_failed)
            !AppUpdateChecker.hasNewVersion(result.latest, uiState.version) ->
                context.getString(R.string.settings_update_latest)
            else -> context.getString(R.string.settings_update_available, result.latest)
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Appearance(
            themeMode = uiState.themeMode,
            onThemeClick = { position ->
                pendingThemeClickPosition = position
                showThemeDialog = true
            },
        )
        Language(uiState.language, onLanguageSelected, onShowDialog = { showLanguageDialog = true })
        AppInfo(uiState.version, onCheckForUpdate)
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = uiState.themeMode,
            onDismiss = { showThemeDialog = false },
            onThemeSelected = { mode ->
                onThemeClick(pendingThemeClickPosition)
                onThemeSelected(mode)
                showThemeDialog = false
            },
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = uiState.language,
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { language ->
                showLanguageDialog = false
                onLanguageSelected(language)
            },
        )
    }
}