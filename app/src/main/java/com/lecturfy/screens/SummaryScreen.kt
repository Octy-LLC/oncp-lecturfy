package com.lecturfy.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import com.lecturfy.components.text.AnnotatedText
import com.lecturfy.components.text.AnnotatedTextPart
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import com.lecturfy.theme.AppTheme
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
import com.lecturfy.viewmodels.SummaryViewModel
import com.lecturfy.R
import com.lecturfy.components.utils.getSavedLocale
import com.lecturfy.components.utils.saveLocale
import com.lecturfy.components.utils.updateLocale
import com.lecturfy.components.utils.FileUtils

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    sharedPreferences: SharedPreferences,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: SummaryViewModel = hiltViewModel(),
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

    
Column(
    modifier = 
Modifier
        
        .width(100.dp)
        
        .height(100.dp)
        
        .padding(
            start = 16.dp,
            
            end = 16.dp,
            
            )
,
){
    
    Row(
        modifier = 
    Modifier
            
            .fillMaxWidth(1f)
    ,
    ) {
        
        
        
        Button(
            onClick = label@{ 
            coroutineScope.launch {
            
            navController.navigate(
                "UploadedFiles"
            )
            }
             },
            modifier = 
        Modifier
        ,
            
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(android.graphics.Color.parseColor("#FFFFFF")), 
              
              
              
            ),
            elevation = ButtonDefaults.buttonElevation(
              
              
              
              
              
            ),) {
            
            Icon(
              imageVector = Icons.Filled.Menu,
              contentDescription = "", 
              tint = Color(android.graphics.Color.parseColor("#17191D")),
              modifier = 
            Modifier
            
            )
        }}
    
    Row(
        modifier = 
    Modifier
            
            .padding(
                
                top = 4.dp,
                
                bottom = 12.dp,
                )
    ,
    ) {
        
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          "Summary"
        )
        
            ),
            modifier = 
        Modifier
                
                .padding(
                    start = 4.dp,
                    top = 10.dp,
                    
                    bottom = 10.dp,
                    )
        ,
            
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 28.dp,
        )
    }
    
    Row(
        modifier = 
    Modifier
            
            .padding(
                
                top = 10.dp,
                
                bottom = 10.dp,
                )
    ,
    ) {
        
        
        
        Button(
            onClick = label@{ 
            coroutineScope.launch {
            
            val transcriptionId = navController.currentBackStackEntry?.arguments?.getString("id") ?:  "" 
            
            
            navController.navigate(
               "Transcription/${transcriptionId}"
            )
            }
             },
            modifier = 
        Modifier
                
                .clip(
        RoundedCornerShape(
          topStart = 8.dp,
          topEnd = 8.dp,
          bottomStart = 8.dp,
          bottomEnd = 8.dp,
        )
        )
                
                .border(
                    
                    width = 1.dp,
                    color = AppTheme.colors.borderColor2,
                )
        ,
            shape = 
        RoundedCornerShape(
          topStart = 8.dp,
          topEnd = 8.dp,
          bottomStart = 8.dp,
          bottomEnd = 8.dp,
        )
        ,
            
            colors = ButtonDefaults.buttonColors(
              containerColor = AppTheme.colors.accentColor1, 
              
              
              
            ),
            elevation = ButtonDefaults.buttonElevation(
              
              
              
              
              
            ),) {
            
            
            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
              "Transcription"
            )
            
                ),
                modifier = 
            Modifier
                    
                    .padding(
                        start = 12.dp,
                        top = 10.dp,
                        end = 12.dp,
                        bottom = 10.dp,
                        )
            ,
                
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.dp,
            )
        }    
        
        
        Button(
            onClick = label@{  },
            modifier = 
        Modifier
                
                .clip(
        RoundedCornerShape(
          topStart = 8.dp,
          topEnd = 8.dp,
          bottomStart = 8.dp,
          bottomEnd = 8.dp,
        )
        )
                
                .border(
                    
                    width = 1.dp,
                    color = AppTheme.colors.accentColor2,
                )
        ,
            shape = 
        RoundedCornerShape(
          topStart = 8.dp,
          topEnd = 8.dp,
          bottomStart = 8.dp,
          bottomEnd = 8.dp,
        )
        ,
            
            colors = ButtonDefaults.buttonColors(
              containerColor = AppTheme.colors.backgroundColor2, 
              
              
              
            ),
            elevation = ButtonDefaults.buttonElevation(
              
              
              
              
              
            ),) {
            
            
            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
              "Summary"
            )
            
                ),
                modifier = 
            Modifier
                    
                    .padding(
                        start = 12.dp,
                        top = 10.dp,
                        end = 12.dp,
                        bottom = 10.dp,
                        )
            ,
                
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.dp,
            )
        }}
    
    
    AnnotatedText(
        parts = listOf(
            AnnotatedTextPart.LinkPart(
      state.summary.linkToFile,
      state.summary.linkToFile
    )
    
        ),
        modifier = 
    Modifier
    ,
        
        fontSize = 18.sp,
        textAlign = TextAlign.Left,
        lineHeight = 28.dp,
    )
}

    
}
