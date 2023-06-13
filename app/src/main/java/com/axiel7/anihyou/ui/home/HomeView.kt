package com.axiel7.anihyou.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.axiel7.anihyou.App
import com.axiel7.anihyou.R
import com.axiel7.anihyou.data.PreferencesDataStore.AIRING_ON_MY_LIST_PREFERENCE_KEY
import com.axiel7.anihyou.data.PreferencesDataStore.rememberPreference
import com.axiel7.anihyou.data.model.media.AnimeSeason
import com.axiel7.anihyou.type.MediaSort
import com.axiel7.anihyou.type.MediaType
import com.axiel7.anihyou.ui.composables.DefaultScaffoldWithMediumTopAppBar
import com.axiel7.anihyou.ui.composables.OnBottomReached
import com.axiel7.anihyou.ui.composables.media.AiringAnimeHorizontalItem
import com.axiel7.anihyou.ui.composables.media.AiringAnimeHorizontalItemPlaceholder
import com.axiel7.anihyou.ui.composables.media.MEDIA_ITEM_VERTICAL_HEIGHT
import com.axiel7.anihyou.ui.composables.media.MEDIA_POSTER_SMALL_HEIGHT
import com.axiel7.anihyou.ui.composables.media.MediaItemVertical
import com.axiel7.anihyou.ui.composables.media.MediaItemVerticalPlaceholder
import com.axiel7.anihyou.ui.composables.scores.SmallScoreIndicator
import com.axiel7.anihyou.ui.theme.AniHyouTheme
import com.axiel7.anihyou.utils.DateUtils.secondsToLegibleText
import com.axiel7.anihyou.utils.UNKNOWN_CHAR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navigateToMediaDetails: (mediaId: Int) -> Unit,
    navigateToAnimeSeason: (AnimeSeason) -> Unit,
    navigateToCalendar: () -> Unit,
    navigateToExplore: (MediaType, MediaSort) -> Unit,
    navigateToNotifications: () -> Unit,
) {
    val viewModel: HomeViewModel = viewModel()
    val listState = rememberLazyListState()
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    LaunchedEffect(viewModel) {
        viewModel.getUnreadNotificationCount()
    }

    listState.OnBottomReached(buffer = 0) {
        viewModel.addNextInfo()
    }

    DefaultScaffoldWithMediumTopAppBar(
        title = stringResource(R.string.home),
        modifier = modifier,
        actions = {
            BadgedBox(
                badge = {
                    if (viewModel.unreadNotificationCount > 0) {
                        Badge {
                            Text(text = viewModel.unreadNotificationCount.toString())
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        navigateToNotifications()
                        viewModel.unreadNotificationCount = 0
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.notifications_24),
                    contentDescription = stringResource(R.string.notifications)
                )
            }
        },
        scrollBehavior = topAppBarScrollBehavior,
        contentWindowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .padding(padding),
            state = listState,
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(viewModel.infos) { item ->
                when (item) {
                    HomeInfo.AIRING -> HomeAiringContent(
                        viewModel = viewModel,
                        navigateToCalendar = navigateToCalendar,
                        navigateToMediaDetails = navigateToMediaDetails,
                    )
                    HomeInfo.THIS_SEASON -> HomeThisSeasonContent(
                        viewModel = viewModel,
                        navigateToAnimeSeason = navigateToAnimeSeason,
                        navigateToMediaDetails = navigateToMediaDetails,
                    )
                    HomeInfo.TRENDING_ANIME -> HomeTrendingAnimeContent(
                        viewModel = viewModel,
                        navigateToExplore = navigateToExplore,
                        navigateToMediaDetails = navigateToMediaDetails,
                    )
                    HomeInfo.NEXT_SEASON -> HomeNextSeasonContent(
                        viewModel = viewModel,
                        navigateToAnimeSeason = navigateToAnimeSeason,
                        navigateToMediaDetails = navigateToMediaDetails,
                    )
                    HomeInfo.TRENDING_MANGA -> HomeTrendingMangaContent(
                        viewModel = viewModel,
                        navigateToExplore = navigateToExplore,
                        navigateToMediaDetails = navigateToMediaDetails,
                    )
                }
            }
        }//: LazyColumn
    }//: Scaffold
}

@Composable
fun HomeAiringContent(
    viewModel: HomeViewModel,
    navigateToCalendar: () -> Unit,
    navigateToMediaDetails: (mediaId: Int) -> Unit
) {
    val airingOnMyList by rememberPreference(AIRING_ON_MY_LIST_PREFERENCE_KEY, App.airingOnMyList)

    LaunchedEffect(airingOnMyList) {
        if (airingOnMyList == true && viewModel.airingAnimeOnMyList.isEmpty())
            viewModel.getAiringAnimeOnMyList()
        else if (viewModel.airingAnime.isEmpty())
            viewModel.getAiringAnime()
    }

    // Airing
    HorizontalListHeader(
        text = stringResource(R.string.airing),
        onClick = navigateToCalendar
    )
    HomeLazyRow(
        minHeight = MEDIA_POSTER_SMALL_HEIGHT.dp
    ) {
        if (airingOnMyList == true) {
            items(viewModel.airingAnimeOnMyList) { item ->
                AiringAnimeHorizontalItem(
                    title = item.title?.userPreferred ?: "",
                    subtitle = stringResource(R.string.airing_in,
                        item.nextAiringEpisode?.timeUntilAiring?.toLong()?.secondsToLegibleText() ?: UNKNOWN_CHAR
                    ),
                    imageUrl = item.coverImage?.large,
                    score = if (item.meanScore != null) "${item.meanScore}%" else null,
                    onClick = {
                        navigateToMediaDetails(item.id)
                    }
                )
            }
        }
        else {
            items(viewModel.airingAnime) { item ->
                AiringAnimeHorizontalItem(
                    title = item.media?.title?.userPreferred ?: "",
                    subtitle = stringResource(R.string.airing_in, item.timeUntilAiring.toLong().secondsToLegibleText()),
                    imageUrl = item.media?.coverImage?.large,
                    score = if (item.media?.meanScore != null) "${item.media.meanScore}%" else null,
                    onClick = {
                        navigateToMediaDetails(item.media!!.id)
                    }
                )
            }
        }
        if (viewModel.isLoadingAiring) {
            items(10) {
                AiringAnimeHorizontalItemPlaceholder()
            }
        }
    }
}

@Composable
fun HomeThisSeasonContent(
    viewModel: HomeViewModel,
    navigateToAnimeSeason: (AnimeSeason) -> Unit,
    navigateToMediaDetails: (mediaId: Int) -> Unit,
) {
    LaunchedEffect(viewModel) {
        if (viewModel.thisSeasonAnime.isEmpty()) viewModel.getThisSeasonAnime()
    }

    // This season
    HorizontalListHeader(
        text = viewModel.nowAnimeSeason.localized(),
        onClick = {
            navigateToAnimeSeason(viewModel.nowAnimeSeason)
        }
    )
    HomeLazyRow(
        minHeight = MEDIA_ITEM_VERTICAL_HEIGHT.dp
    ) {
        items(viewModel.thisSeasonAnime) { item ->
            MediaItemVertical(
                title = item.title?.userPreferred ?: "",
                imageUrl = item.coverImage?.large,
                modifier = Modifier.padding(start = 8.dp),
                subtitle = {
                    item.meanScore?.let { score ->
                        SmallScoreIndicator(score = "${score}%")
                    }
                },
                minLines = 2,
                onClick = { navigateToMediaDetails(item.id) }
            )
        }
        if (viewModel.isLoadingThisSeason) {
            items(10) {
                MediaItemVerticalPlaceholder(
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun HomeTrendingAnimeContent(
    viewModel: HomeViewModel,
    navigateToExplore: (MediaType, MediaSort) -> Unit,
    navigateToMediaDetails: (mediaId: Int) -> Unit,
) {
    LaunchedEffect(viewModel) {
        if (viewModel.trendingAnime.isEmpty()) viewModel.getTrendingAnime()
    }

    // Trending Anime
    HorizontalListHeader(
        text = stringResource(R.string.trending_now),
        onClick = {
            navigateToExplore(MediaType.ANIME, MediaSort.TRENDING_DESC)
        }
    )
    HomeLazyRow(
        minHeight = MEDIA_ITEM_VERTICAL_HEIGHT.dp
    ) {
        items(viewModel.trendingAnime) { item ->
            MediaItemVertical(
                title = item.title?.userPreferred ?: "",
                imageUrl = item.coverImage?.large,
                modifier = Modifier.padding(start = 8.dp),
                subtitle = {
                    item.meanScore?.let { score ->
                        SmallScoreIndicator(score = "${score}%")
                    }
                },
                minLines = 2,
                onClick = { navigateToMediaDetails(item.id) }
            )
        }
        if (viewModel.isLoadingTrendingAnime) {
            items(10) {
                MediaItemVerticalPlaceholder(
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun HomeNextSeasonContent(
    viewModel: HomeViewModel,
    navigateToAnimeSeason: (AnimeSeason) -> Unit,
    navigateToMediaDetails: (mediaId: Int) -> Unit,
) {
    LaunchedEffect(viewModel) {
        if (viewModel.nextSeasonAnime.isEmpty()) viewModel.getNextSeasonAnime()
    }

    // Next season
    HorizontalListHeader(
        text = stringResource(R.string.next_season),
        onClick = {
            navigateToAnimeSeason(viewModel.nextAnimeSeason)
        }
    )
    HomeLazyRow(
        minHeight = MEDIA_ITEM_VERTICAL_HEIGHT.dp
    ) {
        items(viewModel.nextSeasonAnime) { item ->
            MediaItemVertical(
                title = item.title?.userPreferred ?: "",
                imageUrl = item.coverImage?.large,
                modifier = Modifier.padding(start = 8.dp),
                subtitle = {
                    item.meanScore?.let { score ->
                        SmallScoreIndicator(score = "${score}%")
                    }
                },
                minLines = 2,
                onClick = { navigateToMediaDetails(item.id) }
            )
        }
        if (viewModel.isLoadingNextSeason) {
            items(10) {
                MediaItemVerticalPlaceholder(
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun HomeTrendingMangaContent(
    viewModel: HomeViewModel,
    navigateToExplore: (MediaType, MediaSort) -> Unit,
    navigateToMediaDetails: (mediaId: Int) -> Unit,
) {
    LaunchedEffect(viewModel) {
        if (viewModel.trendingManga.isEmpty()) viewModel.getTrendingManga()
    }

    // Trending Manga
    HorizontalListHeader(
        text = stringResource(R.string.trending_manga),
        onClick = {
            navigateToExplore(MediaType.MANGA, MediaSort.TRENDING_DESC)
        }
    )
    HomeLazyRow(
        minHeight = MEDIA_ITEM_VERTICAL_HEIGHT.dp
    ) {
        items(viewModel.trendingManga) { item ->
            MediaItemVertical(
                title = item.title?.userPreferred ?: "",
                imageUrl = item.coverImage?.large,
                modifier = Modifier.padding(start = 8.dp),
                subtitle = {
                    item.meanScore?.let { score ->
                        SmallScoreIndicator(score = "${score}%")
                    }
                },
                minLines = 2,
                onClick = { navigateToMediaDetails(item.id) }
            )
        }
        if (viewModel.isLoadingTrendingManga) {
            items(10) {
                MediaItemVerticalPlaceholder(
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun HorizontalListHeader(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Box(modifier = Modifier.clickable(onClick = onClick ?: {})) {
        Row(
            modifier = modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            if (onClick != null) {
                Icon(
                    painter = painterResource(R.drawable.arrow_forward_24),
                    contentDescription = "arrow"
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeLazyRow(
    minHeight: Dp = MEDIA_POSTER_SMALL_HEIGHT.dp,
    content: LazyListScope.() -> Unit
) {
    val state = rememberLazyListState()
    LazyRow(
        modifier = Modifier
            .padding(top = 8.dp)
            .sizeIn(minHeight = minHeight),
        state = state,
        contentPadding = PaddingValues(horizontal = 8.dp),
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
        content = content
    )
}

@Preview
@Composable
fun HomeViewPreview() {
    AniHyouTheme {
        HomeView(
            navigateToMediaDetails = { },
            navigateToAnimeSeason = { },
            navigateToCalendar = { },
            navigateToExplore = { _, _ -> },
            navigateToNotifications = {},
        )
    }
}