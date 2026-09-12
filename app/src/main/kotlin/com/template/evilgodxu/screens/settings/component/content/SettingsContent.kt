package com.template.evilgodxu.screens.settings.component.content

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.template.evilgodxu.AppUiState
import com.template.evilgodxu.R
import com.template.evilgodxu.data.settings.AppLanguage
import com.template.evilgodxu.data.settings.ThemeMode
import com.template.evilgodxu.screens.settings.SettingsUiState
import com.template.evilgodxu.screens.settings.component.appearance.Appearance
import com.template.evilgodxu.screens.settings.component.dialog.LanguageSelectionDialog
import com.template.evilgodxu.screens.settings.component.dialog.ThemeSelectionDialog
import com.template.evilgodxu.screens.settings.component.info.AppInfo
import com.template.evilgodxu.screens.settings.component.language.Language
import com.template.evilgodxu.update.UpdateMessage
import kotlinx.coroutines.flow.Flow

// 页面级组装单元：设置列表 + 弹窗 + 更新检查结果反馈；
// 弹窗显隐由 SettingsViewModel 经 settingsUiState 下传，组件本身无状态
@Composable
fun SettingsContent(
    uiState: AppUiState,
    settingsUiState: SettingsUiState,
    updateMessages: Flow<UpdateMessage>,
    onShowThemeDialog: (Offset) -> Unit,
    onDismissThemeDialog: () -> Unit,
    onThemeSelected: (ThemeMode) -> Unit,
    onShowLanguageDialog: () -> Unit,
    onDismissLanguageDialog: () -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
    onCheckForUpdate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 手动检查更新的一次性提示以 Toast 反馈（已是最新 / 检查失败 / 缺少安装包）
    val context = LocalContext.current
    val upToDateText = stringResource(R.string.settings_update_latest)
    val checkFailedText = stringResource(R.string.settings_update_failed)
    val incompleteReleaseText = stringResource(R.string.settings_update_incomplete)
    // 以文案为键重建收集，避免语言切换后仍提示旧语言文案
    LaunchedEffect(upToDateText, checkFailedText, incompleteReleaseText) {
        updateMessages.collect { message ->
            val text = when (message) {
                UpdateMessage.UpToDate -> upToDateText
                UpdateMessage.CheckFailed -> checkFailedText
                UpdateMessage.IncompleteRelease -> incompleteReleaseText
            }
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Appearance(themeMode = uiState.themeMode, onThemeClick = onShowThemeDialog)
        Language(uiState.language, onLanguageSelected, onShowDialog = onShowLanguageDialog)
        AppInfo(uiState.version, onCheckForUpdate)
    }

    if (settingsUiState.showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = uiState.themeMode,
            onDismiss = onDismissThemeDialog,
            onThemeSelected = onThemeSelected,
        )
    }

    if (settingsUiState.showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = uiState.language,
            onDismiss = onDismissLanguageDialog,
            onLanguageSelected = onLanguageSelected,
        )
    }
}
