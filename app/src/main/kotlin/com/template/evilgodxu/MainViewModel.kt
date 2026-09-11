package com.template.evilgodxu

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.evilgodxu.data.repository.SettingsRepository
import com.template.evilgodxu.data.settings.AppLanguage
import com.template.evilgodxu.data.settings.ThemeMode
import com.template.evilgodxu.update.AppUpdateChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 版本检查结果：latest 为线上版本号，null 表示检查失败（网络异常或非 200）
data class UpdateCheckOutcome(val latest: String?)

// 应用级（Activity 作用域）UI 状态持有者：以 DataStore 为单一事实源，聚合主题、语言与版本，
// 供全局主题、本地化与各页面 UI 共同消费，UI 层不直连数据源。
class MainViewModel(
    private val settingsRepository: SettingsRepository,
    appVersion: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState(version = appVersion))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val _updateCheck = MutableStateFlow<UpdateCheckOutcome?>(null)
    val updateCheck: StateFlow<UpdateCheckOutcome?> = _updateCheck.asStateFlow()

    init {
        // 数据流回流更新 UI 状态，写入后经同一持有者广播
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _uiState.update { it.copy(themeMode = settings.themeMode) }
            }
        }
        viewModelScope.launch {
            settingsRepository.appLanguage.collect { language ->
                _uiState.update { it.copy(language = language) }
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { settingsRepository.saveThemeMode(mode) }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch { settingsRepository.setAppLanguage(language) }
    }

    // 发起版本检查：先复位结果以便重复点击重新触发，随后在后台拉取线上版本号
    fun checkForUpdate() {
        viewModelScope.launch {
            _updateCheck.value = null
            _updateCheck.value = UpdateCheckOutcome(AppUpdateChecker.fetchLatestVersion())
        }
    }
}

// 供界面树消费的 CompositionLocal，由宿主 Activity 提供
val LocalMainViewModel = staticCompositionLocalOf<MainViewModel> {
    error("MainViewModel is not provided")
}
