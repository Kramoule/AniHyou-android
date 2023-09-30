package com.axiel7.anihyou.data.model.notification

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.axiel7.anihyou.R
import com.axiel7.anihyou.data.model.base.Localizable
import java.util.concurrent.TimeUnit

enum class NotificationInterval(
    val value: Long,
    val timeUnit: TimeUnit
) : Localizable {
    SIX_HOURS(6, TimeUnit.HOURS),
    TWELVE_HOURS(12, TimeUnit.HOURS),
    DAILY(1, TimeUnit.DAYS),
    TWO_DAYS(2, TimeUnit.DAYS),
    THREE_DAYS(3, TimeUnit.DAYS),
    WEEKLY(7, TimeUnit.DAYS);

    val stringRes
        get() = when (this) {
            SIX_HOURS -> R.string.every_six_hours
            TWELVE_HOURS -> R.string.every_twelve_hours
            DAILY -> R.string.daily
            TWO_DAYS -> R.string.every_two_days
            THREE_DAYS -> R.string.every_three_days
            WEEKLY -> R.string.weekly
        }

    @Composable
    override fun localized() = stringResource(stringRes)
}