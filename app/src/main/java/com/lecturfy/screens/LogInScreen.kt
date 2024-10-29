package com.lecturfy.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import com.lecturfy.components.text.AnnotatedText
import com.lecturfy.components.text.AnnotatedTextPart
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.foundation.border
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
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
import com.lecturfy.viewmodels.LoginViewModel
import com.lecturfy.R
import com.lecturfy.components.utils.getSavedLocale
import com.lecturfy.components.utils.saveLocale
import com.lecturfy.components.utils.updateLocale
import com.lecturfy.components.utils.FileUtils

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    sharedPreferences: SharedPreferences,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: LoginViewModel = hiltViewModel(),
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
        
        .background(Color(android.graphics.Color.parseColor("#7DDEB0")))
,
){
    
    Column(
        modifier = 
    Modifier
            
            .fillMaxWidth(1f)
            
            .fillMaxHeight(0.5f)
    ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        
        AsyncImage(
            model = "https://firebasestorage.googleapis.com/v0/b/lecturfy-assets.appspot.com/o/login-image.png?alt=media&token=d32ea3b0-0b09-4c8b-b5a5-26f19fe52ac0",
            contentDescription = null,
            modifier = 
        Modifier
        ,
        )
    }
    
    Column(
        modifier = 
    Modifier
            
            .fillMaxWidth(1f)
            
            .fillMaxHeight(1f)
            
            .clip(
    RoundedCornerShape(
      topStart = 20.dp,
      topEnd = 20.dp,
    )
    )
            
            .background(Color(android.graphics.Color.parseColor("#F4F6F7")))
            
            .padding(
                
                top = 20.dp,
                
                
                )
    ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          "Log in"
        )
        
            ),
            modifier = 
        Modifier
        ,
            
            fontSize = 24.sp,
        )
        
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          "Get study insights fast"
        )
        
            ),
            modifier = 
        Modifier
        ,
            
            fontSize = 18.sp,
            color = Color(android.graphics.Color.parseColor("#323B49")),
        )
        
        Column(
            modifier = 
        Modifier
                
                .padding(
                    
                    top = 10.dp,
                    
                    bottom = 10.dp,
                    )
        ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            
            
            
            Button(
                onClick = label@{ 
                coroutineScope.launch {
                
                googleSignInHelper.configure(
                  webClientId = "155394191404-gcuv3p3nl9uh2in1rih17htlt5lii64n.apps.googleusercontent.com",
                  scopes = listOf()
                )
                val googleCode = viewModel.initiateGoogleSignIn()!!
                
                
                viewModel.setGoogleauthvariableCode(googleCode)
                
                
                var apiGoogleAuthResponse =  viewModel.logingoogle(
                  codeEntity=state.googleAuthVariable,
                )
                if(apiGoogleAuthResponse == null) return@launch
                
                
                
                if(apiGoogleAuthResponse.code() == 200){
                  
                  with(sharedPreferences.edit()) {
                    putString("accessTokenLocal", apiGoogleAuthResponse.body()!!.accessTokenLocal)
                    apply()
                  }
                  
                }
                
                
                }
                 },
                modifier = 
            Modifier
                    
                    .clip(
            RoundedCornerShape(
              topStart = 12.dp,
              topEnd = 12.dp,
              bottomStart = 12.dp,
              bottomEnd = 12.dp,
            )
            )
                    
                    .border(
                        
                        width = 1.dp,
                        color = Color(android.graphics.Color.parseColor("#DEDFE0")),
                    )
            ,
                shape = 
            RoundedCornerShape(
              topStart = 12.dp,
              topEnd = 12.dp,
              bottomStart = 12.dp,
              bottomEnd = 12.dp,
            )
            ,
                
                colors = ButtonDefaults.buttonColors(
                  containerColor = Color(android.graphics.Color.parseColor("#FFFFFF")), 
                  
                  
                  
                ),
                elevation = ButtonDefaults.buttonElevation(
                  
                  
                  
                  
                  
                ),) {
                
                Row(
                    modifier = 
                Modifier
                        
                        .padding(
                            start = 14.dp,
                            top = 14.dp,
                            end = 14.dp,
                            bottom = 14.dp,
                            )
                ,
                ) {
                    
                    AsyncImage(
                        model = "https://firebasestorage.googleapis.com/v0/b/lecturfy-assets.appspot.com/o/google-logo-24x24.png?alt=media&token=b021761a-54c1-4532-b2ea-c1a968f9762c",
                        contentDescription = null,
                        modifier = 
                    Modifier
                            
                            .width(24.dp)
                            
                            .height(24.dp)
                            
                            .padding(
                                
                                
                                end = 10.dp,
                                
                                )
                    ,
                    )
                    
                    
                    AnnotatedText(
                        parts = listOf(
                            AnnotatedTextPart.TextPart(
                      "Continue with Google"
                    )
                    
                        ),
                        modifier = 
                    Modifier
                    ,
                        
                        fontSize = 18.sp,
                        lineHeight = 22.dp,
                    )
                }
            }}
        
        Column(
            modifier = 
        Modifier
                
                .fillMaxHeight(1f)
                
                .padding(
                    
                    
                    
                    bottom = 30.dp,
                    )
        ,
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            
            
            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
              "By using the service, you agree to our"
            )
            
                ),
                modifier = 
            Modifier
            ,
                color = Color(android.graphics.Color.parseColor("#323B49")),
            )
            
            Row(
                modifier = 
            Modifier
            ,
                horizontalArrangement = Arrangement.Center,
            ) {
                
                
                AnnotatedText(
                    parts = listOf(
                        AnnotatedTextPart.LinkPart(
                  "Privacy Policy",
                  "https://www.lecturfy.com/privacy-policy"
                )
                
                    ),
                    modifier = 
                Modifier
                ,
                    color = Color(android.graphics.Color.parseColor("#323B49")),
                )
                
                
                AnnotatedText(
                    parts = listOf(
                        AnnotatedTextPart.TextPart(
                  " and "
                )
                
                    ),
                    modifier = 
                Modifier
                ,
                    color = Color(android.graphics.Color.parseColor("#323B49")),
                )
                
                
                AnnotatedText(
                    parts = listOf(
                        AnnotatedTextPart.LinkPart(
                  "Terms of Service",
                  "https://www.lecturfy.com/terms-of-service"
                )
                
                    ),
                    modifier = 
                Modifier
                ,
                    color = Color(android.graphics.Color.parseColor("#323B49")),
                )
            }
            
            
            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
              "© 2024. All Rights Reserved"
            )
            
                ),
                modifier = 
            Modifier
            ,
                color = Color(android.graphics.Color.parseColor("#4D5664")),
            )
        }
    }
}

    
}
