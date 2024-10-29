package com.lecturfy.components.pulltorefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshStructure(
    onRefresh: suspend () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val state = rememberPullToRefreshState()

    if (state.isRefreshing) {
        LaunchedEffect(Unit) {
            onRefresh()
            state.endRefresh()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(state.nestedScrollConnection)
    ) {
        content()
        androidx.compose.material3.pulltorefresh.PullToRefreshContainer(
            state = state,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}