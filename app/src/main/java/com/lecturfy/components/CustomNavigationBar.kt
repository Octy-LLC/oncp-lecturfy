package com.lecturfy.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomNavigationBar(
    onItemSelected: (String) -> Unit,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    content: @Composable RowScope.(onItemSelected: (String) -> Unit) -> Unit
) {
    NavigationBar(
        windowInsets = WindowInsets(36.dp),
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation
    ) {
        content(onItemSelected)
    }
}