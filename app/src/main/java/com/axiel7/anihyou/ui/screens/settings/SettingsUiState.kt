package com.axiel7.anihyou.ui.screens.settings

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.axiel7.anihyou.data.model.notification.NotificationInterval
import com.axiel7.anihyou.fragment.UserSettings
import com.axiel7.anihyou.type.ScoreFormat
import com.axiel7.anihyou.ui.common.AppColorMode
import com.axiel7.anihyou.ui.common.DefaultTab
import com.axiel7.anihyou.ui.common.ItemsPerRow
import com.axiel7.anihyou.ui.common.ListStyle
import com.axiel7.anihyou.ui.common.Theme
import com.axiel7.anihyou.ui.common.state.UiState

@Immutable
data class SettingsUiState(
    val theme: Theme? = null,
    val useBlackColors: Boolean = false,
    val appColorMode: AppColorMode? = null,
    val appColor: Color? = null,
    val useGeneralListStyle: Boolean? = null,
    val generalListStyle: ListStyle? = null,
    val gridItemsPerRow: ItemsPerRow? = null,
    val airingOnMyList: Boolean? = null,
    val scoreFormat: ScoreFormat? = null,
    val defaultTab: DefaultTab = DefaultTab.LAST_USED,
    val isNotificationsEnabled: Boolean? = null,
    val notificationCheckInterval: NotificationInterval = NotificationInterval.DAILY,
    val userSettings: UserSettings? = null,
    val isLoggedIn: Boolean = false,
    override val error: String? = null,
    override val isLoading: Boolean = false,
) : UiState() {
    override fun setError(value: String?) = copy(error = value)
    override fun setLoading(value: Boolean) = copy(isLoading = value)
}
