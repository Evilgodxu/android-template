package com.template.evilgodxu.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.template.evilgodxu.R
import com.template.evilgodxu.data.settings.AppLanguage
import com.template.evilgodxu.ui.icons.AppIcons

// 语言设置项
@Composable
fun Language(language: AppLanguage, onLanguageSelected: (AppLanguage) -> Unit, onShowDialog: () -> Unit) {
    SectionCard(title = stringResource(R.string.settings_section_language)) {
        SettingsClickableItem(
            icon = AppIcons.Language,
            title = stringResource(R.string.settings_language_title),
            subtitle = when (language) {
                AppLanguage.SYSTEM -> stringResource(R.string.language_system)
                AppLanguage.CHINESE -> stringResource(R.string.language_chinese)
                AppLanguage.ENGLISH -> stringResource(R.string.language_english)
            },
            onClick = onShowDialog,
        )
    }
}