package com.lecturfy.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(
    colorScheme: AppColorScheme = AppColorScheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme {
        CompositionLocalProvider(LocalAppColors provides colorScheme) {
            content()
        }
    }
}

object AppTheme {
    val colors: AppColorScheme
        @Composable
        get() = LocalAppColors.current
}