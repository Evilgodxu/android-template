package com.template.evilgodxu.localization

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.LocaleList
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.evilgodxu.LocalTemplateActivityViewModel
import com.template.evilgodxu.data.settings.AppLanguage
import java.util.Locale

// 应用语言转 Locale；systemLocale 为跟随系统时的实际语言
fun AppLanguage.toLocale(systemLocale: Locale = Locale.getDefault()): Locale = when (this) {
    AppLanguage.SYSTEM -> systemLocale
    AppLanguage.CHINESE -> Locale.SIMPLIFIED_CHINESE
    AppLanguage.ENGLISH -> Locale.ENGLISH
}

// 语言管理器：负责语言转 Locale 与创建本地化上下文；语言来源由界面层经 CompositionLocal 提供
class LocalizationManager(
    private val context: Context,
) {
    // 构造时捕获系统语言。此时进程默认语言尚未被应用内切换改写，
    // 确保跟随系统始终解析为真实系统语言
    private val systemLocale: Locale = Locale.getDefault()

    // 将指定语言转为 Locale；跟随系统时解析为构造时捕获的真实系统语言
    fun localeFor(language: AppLanguage): Locale = language.toLocale(systemLocale)

    // 以指定语言创建本地化上下文，供 Compose 层通过 LocalContext 提供资源
    fun createLocalizedContext(locale: Locale): Context {
        val config = Configuration(context.resources.configuration).apply {
            setLocales(LocaleList(locale))
        }
        return context.createConfigurationContext(config)
    }
}

// 提供本地化 Context 的 Composable，语言切换无需重建 Activity
@Composable
fun ProvideLocalizedContext(
    localizationManager: LocalizationManager,
    content: @Composable () -> Unit,
) {
    val appUiState by LocalTemplateActivityViewModel.current.uiState.collectAsStateWithLifecycle()
    val localizedContext = localizationManager.createLocalizedContext(
        localizationManager.localeFor(appUiState.language),
    )
    // LocalActivity 的默认值派生自 LocalContext.current（沿 ContextWrapper 链解包）；
    // 替换为本地化 Context 后会解包不到 Activity（底层是 Application），导致依赖 Activity 的
    // 能力失效（如首页双击退出的 finish()），因此显式提供进入时的宿主 Activity
    val activity = checkNotNull(LocalContext.current.findActivity())
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalActivity provides activity,
    ) {
        content()
    }
}

// 沿 ContextWrapper 链向上查找宿主 Activity
private tailrec fun Context.findActivity(): Activity? = when {
    this is Activity -> this
    this is ContextWrapper -> baseContext.findActivity()
    else -> null
}