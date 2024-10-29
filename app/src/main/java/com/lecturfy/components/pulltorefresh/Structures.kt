package com.lecturfy.components.pulltorefresh

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshLazyColumn(
    modifier: Modifier = Modifier,
    onRefresh: suspend () -> Unit,
    content: LazyListScope.() -> Unit,
    arrangement: Arrangement.Vertical = Arrangement.Top,
    alignment: Alignment.Horizontal = Alignment.End
) {
    val state = rememberPullToRefreshState()

    if (state.isRefreshing) {
        LaunchedEffect(Unit) {
            onRefresh()
            state.endRefresh()
        }
    }

    val nestedScrollConnection = state.nestedScrollConnection

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(state.nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
                .then(modifier)
                .nestedScroll(nestedScrollConnection),
            horizontalAlignment = alignment,
            verticalArrangement = arrangement
        ) {
            content()
        }
        PullToRefreshContainer(
            state = state,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshLazyRow(
    modifier: Modifier = Modifier,
    onRefresh: suspend () -> Unit,
    content: LazyListScope.() -> Unit,
    arrangement: Arrangement.Horizontal = Arrangement.Start,
    alignment: Alignment.Vertical = Alignment.Top
) {
    val state = rememberPullToRefreshState()

    if (state.isRefreshing) {
        LaunchedEffect(Unit) {
            onRefresh()
            state.endRefresh()
        }
    }

    val nestedScrollConnection = state.nestedScrollConnection

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(state.nestedScrollConnection)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
                .then(modifier)
                .nestedScroll(nestedScrollConnection),
            horizontalArrangement = arrangement,
            verticalAlignment = alignment
        ) {
            content()
        }
        PullToRefreshContainer(
            state = state,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
