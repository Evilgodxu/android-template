package com.template.evilgodxu.update

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import java.io.File
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 下载状态：前台服务写入，界面层消费
sealed interface UpdateDownloadState {
    data object Idle : UpdateDownloadState

    data class Downloading(val percent: Int) : UpdateDownloadState

    data class Completed(val file: File) : UpdateDownloadState

    data class Failed(val failure: UpdateDownloadFailure) : UpdateDownloadState
}

// 下载协调器（应用级单例）：对外暴露下载状态、启动前台下载服务，并由服务回写进度与结果
class UpdateDownloadCoordinator(private val appContext: Context) {

    private val _state = MutableStateFlow<UpdateDownloadState>(UpdateDownloadState.Idle)
    val state: StateFlow<UpdateDownloadState> = _state.asStateFlow()

    fun start(info: UpdateInfo) {
        _state.value = UpdateDownloadState.Downloading(0)
        ContextCompat.startForegroundService(
            appContext,
            UpdateDownloadService.createIntent(appContext, info),
        )
    }

    // 以下由 UpdateDownloadService 回调
    fun onProgress(percent: Int) {
        _state.value = UpdateDownloadState.Downloading(percent)
    }

    fun onCompleted(file: File) {
        _state.value = UpdateDownloadState.Completed(file)
    }

    fun onFailed(failure: UpdateDownloadFailure) {
        _state.value = UpdateDownloadState.Failed(failure)
    }

    fun reset() {
        _state.value = UpdateDownloadState.Idle
    }

    // 应用内点击安装后，撤掉服务留下的完成通知
    fun clearNotification() {
        UpdateNotifications.cancel(appContext)
    }
}
