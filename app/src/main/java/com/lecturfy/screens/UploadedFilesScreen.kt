package com.lecturfy.screens

import com.lecturfy.components.pulltorefresh.PullToRefreshLazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ButtonDefaults
import com.lecturfy.theme.AppTheme
import com.lecturfy.components.text.AnnotatedText
import com.lecturfy.components.text.AnnotatedTextPart
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.lecturfy.viewmodels.UploadedfilesViewModel
import com.lecturfy.R
import com.lecturfy.components.utils.getSavedLocale
import com.lecturfy.components.utils.saveLocale
import com.lecturfy.components.utils.updateLocale
import com.lecturfy.components.utils.FileUtils

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadedfilesScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    sharedPreferences: SharedPreferences,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: UploadedfilesViewModel = hiltViewModel(),
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

    
PullToRefreshLazyColumn(
    modifier = 
Modifier
        
        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
,
    onRefresh = label@{  },
    content = {
        
        items(state.transcriptions) { model ->
            
            
            
            Button(
                onClick = label@{ 
                coroutineScope.launch {
                
                navController.navigate(
                   "Transcription/${model.id}"
                )
                }
                 },
                modifier = 
            Modifier
                    
                    .fillMaxWidth(1f)
                    
                    .padding(
                        
                        
                        
                        bottom = 4.dp,
                        )
            ,
                
                colors = ButtonDefaults.buttonColors(
                  containerColor = AppTheme.colors.backgroundColor1, 
                  
                  
                  
                ),
                elevation = ButtonDefaults.buttonElevation(
                  
                  
                  
                  
                  
                ),) {
                
                
                AnnotatedText(
                    parts = listOf(
                        AnnotatedTextPart.TextPart(
                  model.title
                )
                
                    ),
                    modifier = 
                Modifier
                        
                        .padding(
                            start = 16.dp,
                            top = 10.dp,
                            
                            bottom = 10.dp,
                            )
                ,
                    
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Left,
                    lineHeight = 24.dp,
                )
            }}
    }
)

    
}
