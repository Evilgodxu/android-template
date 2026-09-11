package com.template.evilgodxu

import android.content.Context
import android.content.pm.PackageManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.template.evilgodxu.data.repository.DataStoreSettingsRepository
import com.template.evilgodxu.data.repository.SettingsRepository
import com.template.evilgodxu.localization.LocalizationManager

// 手动 DI 容器：Application 启动时构造并持有全部依赖
// 数据源经构造注入仓库，便于单元测试替换
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    val settingsRepository: SettingsRepository by lazy {
        DataStoreSettingsRepository(preferencesDataStore)
    }

    val localizationManager: LocalizationManager by lazy {
        LocalizationManager(appContext)
    }

    // 应用版本号：冷启动读取一次
    val appVersion: String = readAppVersion()

    private val preferencesDataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create {
            appContext.preferencesDataStoreFile("settings")
        }
    }

    private fun readAppVersion(): String =
        appContext.packageManager
            .getPackageInfo(appContext.packageName, PackageManager.PackageInfoFlags.of(0L))
            .versionName.orEmpty()
}