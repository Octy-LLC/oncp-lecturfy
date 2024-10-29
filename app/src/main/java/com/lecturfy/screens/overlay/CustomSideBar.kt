package com.lecturfy.screens.overlay 

import android.content.SharedPreferences
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope

@Composable
fun ConditionalDrawer(
  snackbarHostState: SnackbarHostState,
  sharedPreferences: SharedPreferences,
  coroutineScope: CoroutineScope,
  navController: NavHostController,
  drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
  content: @Composable () -> Unit
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val showMainOverlayList = listOf<String>(
    "Settings","UploadFile","UploadedFiles",
  )

  val showCustomOverlayList: Map<String, @Composable () -> Unit> = hashMapOf(
    
  )

  val drawerContent = when {
    showCustomOverlayList.containsKey(currentRoute) -> {
      showCustomOverlayList[currentRoute]
    }
    currentRoute in showMainOverlayList -> {
      { 
      }
    }
    else -> null
  }

  if (drawerContent != null) {
    ModalNavigationDrawer(
      drawerState = drawerState,
      drawerContent = drawerContent
    ) {
      content()
    }
  } else {
    content()
  }
}