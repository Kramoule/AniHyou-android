package com.axiel7.anihyou.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object PreferencesDataStore {

    val ACCESS_TOKEN_PREFERENCE_KEY = stringPreferencesKey("access_token")
    val USER_ID_PREFERENCE_KEY = intPreferencesKey("user_id")

    val PROFILE_COLOR_PREFERENCE_KEY = stringPreferencesKey("profile_color")
    val SCORE_FORMAT_PREFERENCE_KEY = stringPreferencesKey("score_format")

    val THEME_PREFERENCE_KEY = stringPreferencesKey("theme")
    val LAST_TAB_PREFERENCE_KEY = intPreferencesKey("last_tab")

    val ANIME_LIST_SORT_PREFERENCE_KEY = stringPreferencesKey("anime_list_sort")
    val MANGA_LIST_SORT_PREFERENCE_KEY = stringPreferencesKey("manga_list_sort")

    val DEFAULT_HOME_TAB_PREFERENCE_KEY = intPreferencesKey("default_home_tab")
    val AIRING_ON_MY_LIST_PREFERENCE_KEY = booleanPreferencesKey("airing_on_my_list")

    val GENERAL_LIST_STYLE_PREFERENCE_KEY = stringPreferencesKey("list_display_mode")
    val USE_GENERAL_LIST_STYLE_PREFERENCE_KEY = booleanPreferencesKey("use_general_list_style")

    val GRID_ITEMS_PER_ROW_PREFERENCE_KEY = intPreferencesKey("grid_items_per_row")

    val ANIME_CURRENT_LIST_STYLE_PREFERENCE_KEY = stringPreferencesKey("anime_current_list_style")
    val ANIME_PLANNING_LIST_STYLE_PREFERENCE_KEY = stringPreferencesKey("anime_planning_list_style")
    val ANIME_COMPLETED_LIST_STYLE_PREFERENCE_KEY =
        stringPreferencesKey("anime_completed_list_style")
    val ANIME_PAUSED_LIST_STYLE_PREFERENCE_KEY = stringPreferencesKey("anime_paused_list_style")
    val ANIME_DROPPED_LIST_STYLE_PREFERENCE_KEY = stringPreferencesKey("anime_dropped_list_style")
    val ANIME_REPEATING_LIST_STYLE_PREFERENCE_KEY =
        stringPreferencesKey("anime_repeating_list_style")

    val MANGA_CURRENT_LIST_STYLE_PREFERENCE_KEY = stringPreferencesKey("manga_current_list_style")
    val MANGA_PLANNING_LIST_STYLE_PREFERENCE_KEY = stringPreferencesKey("manga_planning_list_style")
    val MANGA_COMPLETED_LIST_STYLE_PREFERENCE_KEY =
        stringPreferencesKey("manga_completed_list_style")
    val MANGA_PAUSED_LIST_STYLE_PREFERENCE_KEY = stringPreferencesKey("manga_paused_list_style")
    val MANGA_DROPPED_LIST_STYLE_PREFERENCE_KEY = stringPreferencesKey("manga_dropped_list_style")
    val MANGA_REPEATING_LIST_STYLE_PREFERENCE_KEY =
        stringPreferencesKey("anime_repeating_list_style")

    val NOTIFICATIONS_ENABLED_PREFERENCE_KEY = booleanPreferencesKey("enabled_notifications")
    val NOTIFICATION_INTERVAL_PREFERENCE_KEY = stringPreferencesKey("notification_interval")
    val LAST_NOTIFICATION_CREATED_AT_PREFERENCE_KEY =
        intPreferencesKey("last_notification_created_at")

    val APP_COLOR_MODE_PREFERENCE_KEY = stringPreferencesKey("app_color_mode")
    val DEFAULT_APP_COLOR_PREFERENCE_KEY = stringPreferencesKey("default_app_color")
    val APP_COLOR_PREFERENCE_KEY = stringPreferencesKey("app_color")

    val Context.defaultPreferencesDataStore by preferencesDataStore(name = "default")

    /**
     * Gets the value by blocking the main thread
     */
    fun <T> DataStore<Preferences>.getValueSync(
        key: Preferences.Key<T>
    ) = runBlocking { data.first() }[key]

    @Composable
    fun <T> rememberPreference(
        key: Preferences.Key<T>,
        defaultValue: T?,
    ): MutableState<T?> {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val state = remember {
            context.defaultPreferencesDataStore.data
                .map {
                    it[key] ?: defaultValue
                }
        }.collectAsState(initial = defaultValue)

        return remember {
            object : MutableState<T?> {
                override var value: T?
                    get() = state.value
                    set(value) {
                        coroutineScope.launch {
                            context.defaultPreferencesDataStore.edit {
                                if (value != null) it[key] = value
                            }
                        }
                    }

                override fun component1() = value
                override fun component2(): (T?) -> Unit = { value = it }
            }
        }
    }
}