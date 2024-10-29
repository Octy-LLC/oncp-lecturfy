package com.lecturfy.screens.overlay

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import kotlinx.coroutines.CoroutineScope
import androidx.navigation.NavHostController
import androidx.compose.material3.DrawerState
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lecturfy.screens.OverlayuploadedfilestopbarScreen

@Composable
fun TopBar(
  snackbarHostState: SnackbarHostState,
  sharedPreferences: SharedPreferences,
  coroutineScope: CoroutineScope,
  drawerState: DrawerState,
  navController: NavHostController
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val showMainOverlayList = listOf<String>(
    "Settings","UploadFile",
  )

  val showCustomOverlayList: Map<String, @Composable () -> Unit> = hashMapOf(
    "UploadedFiles" to { OverlayuploadedfilestopbarScreen(
      snackbarHostState=snackbarHostState,
      sharedPreferences=sharedPreferences,
      coroutineScope=coroutineScope,
      drawerState=drawerState,
      navController=navController
    ) },
  )

  val CustomOverlayScreen = showCustomOverlayList[currentRoute]

  when {

    CustomOverlayScreen != null -> {
      CustomOverlayScreen()
    }

    currentRoute in showMainOverlayList -> {
    }

    else -> {}
  }
}