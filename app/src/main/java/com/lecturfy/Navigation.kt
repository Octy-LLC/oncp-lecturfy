package com.lecturfy

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import android.content.SharedPreferences
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import kotlinx.coroutines.CoroutineScope
import androidx.navigation.navDeepLink
import androidx.navigation.NavHostController
import com.lecturfy.screens.*

@Composable
fun NavGraph(
   snackbarHostState: SnackbarHostState,
   sharedPreferences: SharedPreferences,
   coroutineScope: CoroutineScope,
   drawerState: DrawerState,
   navController: NavHostController = rememberNavController()
){
   NavHost(navController = navController, startDestination = "Initial") {
      composable("Initial", ) {
         InitialScreen(navController, snackbarHostState,sharedPreferences, coroutineScope, drawerState)
      }
      composable("LogIn", ) {
         LoginScreen(navController, snackbarHostState,sharedPreferences, coroutineScope, drawerState)
      }
      composable("Settings", ) {
         SettingsScreen(navController, snackbarHostState,sharedPreferences, coroutineScope, drawerState)
      }
      composable("UploadFile", ) {
         UploadfileScreen(navController, snackbarHostState,sharedPreferences, coroutineScope, drawerState)
      }
      composable("UploadedFiles", ) {
         UploadedfilesScreen(navController, snackbarHostState,sharedPreferences, coroutineScope, drawerState)
      }
      composable(
         "Transcription/{id}",
         
         arguments = listOf(
            navArgument("id") { type = NavType.StringType }
         )
      ) { backStackEntry ->
         TranscriptionScreen(navController, snackbarHostState,sharedPreferences, coroutineScope, drawerState)
      }
      composable(
         "Summary/{id}",
         
         arguments = listOf(
            navArgument("id") { type = NavType.StringType }
         )
      ) { backStackEntry ->
         SummaryScreen(navController, snackbarHostState,sharedPreferences, coroutineScope, drawerState)
      }
   }
}
