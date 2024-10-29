package com.lecturfy.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.lecturfy.MainActivity

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionsElseNavigateBack(
    permissions: List<String>,
    navController: NavHostController,
    mainActivity: MainActivity,
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(Unit) {
        multiplePermissionsState.launchMultiplePermissionRequest()
    }

    if (!multiplePermissionsState.allPermissionsGranted) {
        LaunchedEffect(multiplePermissionsState.allPermissionsGranted) {
            if (!multiplePermissionsState.allPermissionsGranted) {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                } else {
                    mainActivity.finish()
                }
            }
        }
    }
}

