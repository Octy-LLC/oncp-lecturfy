package com.lecturfy.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable

@Composable
fun RowScope.CustomNavigationBarItem(
    label: @Composable (() -> Unit)? = null,
    icon: @Composable () -> Unit,
    alwaysShowLabel: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
    colors: NavigationBarItemColors
) {
    NavigationBarItem(
        icon = icon,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        selected = selected,
        onClick = onClick,
        colors = colors
    )
}
