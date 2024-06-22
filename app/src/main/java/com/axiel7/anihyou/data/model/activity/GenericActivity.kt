package com.axiel7.anihyou.data.model.activity

import androidx.compose.runtime.Immutable
import com.axiel7.anihyou.ActivityDetailsQuery
import com.axiel7.anihyou.fragment.ActivityReplyFragment
import com.axiel7.anihyou.type.ActivityType

@Immutable
data class GenericActivity(
    val id: Int,
    val type: ActivityType,
    val createdAt: Int,
    val text: String?,
    val isLiked: Boolean?,
    val likeCount: Int,
    val replyCount: Int,
    val userId: Int?,
    val username: String?,
    val avatarUrl: String?,
    val replies: List<ActivityReplyFragment>?,
) {
    fun updateLikeStatus(isLiked: Boolean) = copy(
        isLiked = isLiked,
        likeCount = if (isLiked) likeCount + 1 else likeCount - 1
    )
}

fun ActivityDetailsQuery.OnTextActivity.toGenericActivity() = GenericActivity(
    id = textActivityFragment.id,
    type = ActivityType.TEXT,
    createdAt = textActivityFragment.createdAt,
    text = textActivityFragment.text,
    isLiked = textActivityFragment.isLiked,
    likeCount = textActivityFragment.likeCount,
    replyCount = textActivityFragment.replyCount,
    userId = textActivityFragment.user?.id,
    username = textActivityFragment.user?.name,
    avatarUrl = textActivityFragment.user?.avatar?.medium,
    replies = replies?.filterNotNull()?.map { it.activityReplyFragment }
)

fun ActivityDetailsQuery.OnListActivity.toGenericActivity() = GenericActivity(
    id = listActivityFragment.id,
    type = ActivityType.MEDIA_LIST,
    createdAt = listActivityFragment.createdAt,
    text = listActivityFragment.text(),
    isLiked = listActivityFragment.isLiked,
    likeCount = listActivityFragment.likeCount,
    replyCount = listActivityFragment.replyCount,
    userId = user?.id,
    username = user?.name,
    avatarUrl = user?.avatar?.medium,
    replies = replies?.filterNotNull()?.map { it.activityReplyFragment }
)

fun ActivityDetailsQuery.OnMessageActivity.toGenericActivity() = GenericActivity(
    id = messageActivityFragment.id,
    type = ActivityType.MESSAGE,
    createdAt = messageActivityFragment.createdAt,
    text = messageActivityFragment.message,
    isLiked = messageActivityFragment.isLiked,
    likeCount = messageActivityFragment.likeCount,
    replyCount = messageActivityFragment.replyCount,
    userId = messageActivityFragment.messenger?.id,
    username = messageActivityFragment.messenger?.name,
    avatarUrl = messageActivityFragment.messenger?.avatar?.medium,
    replies = replies?.filterNotNull()?.map { it.activityReplyFragment }
)
