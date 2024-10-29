package com.lecturfy.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import com.lecturfy.theme.AppTheme
import androidx.navigation.NavHostController
import android.content.ContextWrapper
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.media3.common.util.UnstableApi
import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import android.content.SharedPreferences
import androidx.compose.runtime.rememberCoroutineScope
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import com.google.android.gms.auth.api.signin.GoogleSignIn
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import com.lecturfy.components.google.GoogleSignInHelper
import com.lecturfy.components.google.launchGoogleSignIn
import com.lecturfy.components.*
import com.lecturfy.components.system.NavController
import com.lecturfy.components.system.NavControllerViewModelDirectionEnum
import com.lecturfy.components.system.SnackbarEntity
import com.lecturfy.viewmodels.InitialViewModel
import com.lecturfy.R
import com.lecturfy.components.utils.getSavedLocale
import com.lecturfy.components.utils.saveLocale
import com.lecturfy.components.utils.updateLocale
import com.lecturfy.components.utils.FileUtils

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    sharedPreferences: SharedPreferences,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: InitialViewModel = hiltViewModel(),
    ){

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val googleSignInHelper = remember { GoogleSignInHelper(context) }
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result?.data)
        val authCode = googleSignInHelper.handleSignInResult(task)
        state.googleSignInRequest?.setCode?.invoke(authCode)
        viewModel.resetGoogleSignInRequest()
    }

    LaunchedEffect(Unit) {
        viewModel.init()
    }
    
    

    LaunchedEffect(state.googleSignInRequest) {
        state.googleSignInRequest?.let {
            coroutineScope.launch {
                launchGoogleSignIn(signInLauncher, googleSignInHelper)
            }
        }
    }

    LaunchedEffect(state.snackbar) {
        state.snackbar?.let { ent: SnackbarEntity ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = ent.message,
                    actionLabel = ent.actionLabel,
                    duration = ent.duration,
                    withDismissAction = ent.withDismissAction
                )
                viewModel.clearSnackbar()
            }
        }
    }

    LaunchedEffect(state.navController) {
        state.navController?.let { ent: NavController ->
            coroutineScope.launch {
                if(ent.direction == NavControllerViewModelDirectionEnum.TO){
                    navController.navigate(ent.path)
                } else if(ent.direction == NavControllerViewModelDirectionEnum.BACK){
                    navController.navigateUp()
                }
                viewModel.clearNavController()
            }
        }
    }

    RequestPermissionsOnTheFirstLaunch(listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.READ_MEDIA_AUDIO,android.Manifest.permission.READ_MEDIA_VIDEO,))
    
Column(
    modifier = 
Modifier
        
        .background(AppTheme.colors.backgroundColor1)
,
){
}

    
}
