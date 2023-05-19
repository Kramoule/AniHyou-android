package com.axiel7.anihyou.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.axiel7.anihyou.ui.base.TabRowItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> DefaultTabRowWithPager(
    tabs: Array<TabRowItem<T>>,
    modifier: Modifier = Modifier,
    isTabScrollable: Boolean = false,
    pageContent: @Composable (Int) -> Unit,
) {
    val state = rememberPagerState()
    val scope = rememberCoroutineScope()
    val isPagerScrolling by remember {
        derivedStateOf {
            state.currentPageOffsetFraction != 0f
        }
    }

    Column(modifier =  modifier) {
        val tabsLayout = @Composable { tabs.forEachIndexed { index, item ->
            Tab(
                selected = state.currentPage == index,
                onClick = { scope.launch { state.animateScrollToPage(index) } },
                text = if (item.title != null) {{
                    Text(text = stringResource(item.title))
                }} else null,
                icon = if (item.icon != null) {{
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.value.toString()
                    )
                }} else null,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }}

        if (isTabScrollable) {
            ScrollableTabRow(
                selectedTabIndex = state.currentPage,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    RoundedTabRowIndicator(tabPositions[state.currentPage])
                },
                tabs = tabsLayout
            )
        } else {
            TabRow(
                selectedTabIndex = state.currentPage,
                indicator = { tabPositions ->
                    RoundedTabRowIndicator(tabPositions[state.currentPage])
                },
                tabs = tabsLayout
            )
        }

        HorizontalPager(
            pageCount = tabs.size,
            state = state,
            key = { tabs[it].value!! }
        ) {
            Column(
                // workaround for this bug https://github.com/google/accompanist/issues/1050
                modifier = if (isPagerScrolling) Modifier.height(500.dp) else Modifier
            ) {
                pageContent(it)
            }
        }
    }//: Column
}