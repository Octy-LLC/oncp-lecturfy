package com.lecturfy.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionsOnTheFirstLaunch(
    permissions: List<String>
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val isFirstLaunch = remember { sharedPreferences.getBoolean("is_first_launch", true) }

    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            multiplePermissionsState.launchMultiplePermissionRequest()
            sharedPreferences.edit().putBoolean("is_first_launch", false).apply()
        }
    }
}
