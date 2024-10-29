package com.lecturfy.components.system

import androidx.compose.material3.SnackbarDuration

data class SnackbarEntity(
    val message: String,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = false,
    val duration: SnackbarDuration
)
