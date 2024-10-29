package com.lecturfy.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionsIfNeededElseShowContent(
    permissions: List<String>,
    content: @Composable () -> Unit,
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(multiplePermissionsState.permissions) {
        multiplePermissionsState.launchMultiplePermissionRequest()
    }

    if (!multiplePermissionsState.allPermissionsGranted) {
        content()
    }
}