package com.lecturfy.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.TextField
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import com.lecturfy.theme.AppTheme
import androidx.compose.foundation.border
import com.lecturfy.components.text.AnnotatedText
import com.lecturfy.components.text.AnnotatedTextPart
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.SnackbarDuration
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
import com.lecturfy.viewmodels.SettingsViewModel
import com.lecturfy.R
import com.lecturfy.components.utils.getSavedLocale
import com.lecturfy.components.utils.saveLocale
import com.lecturfy.components.utils.updateLocale
import com.lecturfy.components.utils.FileUtils

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    sharedPreferences: SharedPreferences,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: SettingsViewModel = hiltViewModel(),
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
        
        .fillMaxHeight(1f)
        
        .padding(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
            )
,
    verticalArrangement = Arrangement.SpaceBetween,
){
    
    Column(
        modifier = 
    Modifier
    ,
    ){
        
        
        
        
        
        
        TextField(
            value = state.userProfile.firstName ?: "",
            onValueChange = { viewModel.setUserprofileFirstName(it) },
            modifier = 
        Modifier
                
                .fillMaxWidth(1f)
                
                .clip(
        RoundedCornerShape(
          topStart = 12.dp,
          topEnd = 12.dp,
          bottomStart = 12.dp,
          bottomEnd = 12.dp,
        )
        )
                
                .background(AppTheme.colors.backgroundColor1)
                
                .border(
                    
                    width = 1.dp,
                    color = AppTheme.colors.borderColor1,
                )
                
                .padding(
                    
                    
                    
                    bottom = 16.dp,
                    )
        ,
            placeholder = { 
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          "Your first name"
        )
        
            ),
            modifier = 
        Modifier
                
                .padding(
                    start = 20.dp,
                    top = 10.dp,
                    
                    bottom = 10.dp,
                    )
        ,
            
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.dp,
        )
         },
            label = { 
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          "First name"
        )
        
            ),
            modifier = 
        Modifier
        ,
            
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.dp,
        )
         },
            
            colors = TextFieldDefaults.textFieldColors(
              
              
              
              
            
              
              
              
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            ),)
        
        
        
        
        
        
        TextField(
            value = state.userProfile.lastName ?: "",
            onValueChange = { viewModel.setUserprofileLastName(it) },
            modifier = 
        Modifier
                
                .fillMaxWidth(1f)
                
                .clip(
        RoundedCornerShape(
          topStart = 12.dp,
          topEnd = 12.dp,
          bottomStart = 12.dp,
          bottomEnd = 12.dp,
        )
        )
                
                .background(AppTheme.colors.backgroundColor1)
                
                .border(
                    
                    width = 1.dp,
                    color = AppTheme.colors.borderColor1,
                )
                
                .padding(
                    
                    
                    
                    bottom = 16.dp,
                    )
        ,
            placeholder = { 
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          "Your last name"
        )
        
            ),
            modifier = 
        Modifier
                
                .padding(
                    start = 20.dp,
                    top = 10.dp,
                    
                    bottom = 10.dp,
                    )
        ,
            
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.dp,
        )
         },
            label = { 
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          "Last name"
        )
        
            ),
            modifier = 
        Modifier
        ,
            
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.dp,
        )
         },
            
            colors = TextFieldDefaults.textFieldColors(
              
              
              
              
            
              
              
              
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            
              
              
              
              
            ),)
        
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          "Notifications"
        )
        
            ),
            modifier = 
        Modifier
                
                .padding(
                    
                    
                    
                    bottom = 8.dp,
                    )
        ,
            
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 28.dp,
        )
        
        Row(
            modifier = 
        Modifier
                
                .padding(
                    
                    top = 10.dp,
                    
                    bottom = 10.dp,
                    )
        ,
        ) {
            
            Checkbox(
              checked = state.userSettings.shouldSendEmailOnTranscribe ?: false,
              onCheckedChange = { viewModel.setUsersettingsShouldSendEmailOnTranscribe(it) },
              modifier = 
            Modifier
            ,
              
              colors = CheckboxDefaults.colors(
                checkedColor = AppTheme.colors.accentColor2,
                uncheckedColor = AppTheme.colors.borderColor1,
                checkmarkColor = AppTheme.colors.accentColor1,
                
                
                
              ),  )    
            
            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
              "Receive email alerts after transcripts are generated"
            )
            
                ),
                modifier = 
            Modifier
            ,
                
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.dp,
            )
        }
        
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          "Contact the Lecturfy team"
        )
        
            ),
            modifier = 
        Modifier
        ,
            
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.dp,
        )
    }
    
    Column(
        modifier = 
    Modifier
    ,
    ){
        
        
        
        Button(
            onClick = label@{ 
            coroutineScope.launch {
            
            val accessTokenLocal = sharedPreferences.getString("accessTokenLocal", "emptyToken")!!
            
            
            viewModel.setUserprofiletoupdateFirstName(state.userProfile.firstName)
            
            
            viewModel.setUserprofiletoupdateLastName(state.userProfile.lastName)
            
            
            var updatedProfile =  viewModel.userProfilepatchProfile(
              profileEntity=state.userProfileToUpdate,
              authorizationHeaderParam=accessTokenLocal,
            )
            if(updatedProfile == null) return@launch
            
            
            
            var updatedSettings =  viewModel.userSettingspatch(
              settingsEntity=state.userSettings,
              authorizationHeaderParam=accessTokenLocal,
            )
            if(updatedSettings == null) return@launch
            
            
            }
             },
            modifier = 
        Modifier
                
                .clip(
        RoundedCornerShape(
          topStart = 24.dp,
          topEnd = 24.dp,
          bottomStart = 24.dp,
          bottomEnd = 24.dp,
        )
        )
        ,
            shape = 
        RoundedCornerShape(
          topStart = 24.dp,
          topEnd = 24.dp,
          bottomStart = 24.dp,
          bottomEnd = 24.dp,
        )
        ,
            
            colors = ButtonDefaults.buttonColors(
              containerColor = AppTheme.colors.accentColor2, 
              
              
              
            ),
            elevation = ButtonDefaults.buttonElevation(
              
              
              
              
              
            ),) {
            
            
            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
              "Update Settings"
            )
            
                ),
                modifier = 
            Modifier
                    
                    .padding(
                        
                        top = 16.dp,
                        
                        bottom = 16.dp,
                        )
            ,
                
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 20.dp,
            )
        }    
        
        
        Button(
            onClick = label@{ 
            coroutineScope.launch {
            
            with(sharedPreferences.edit()) {
              remove("accessTokenLocal")
              apply()
            }
            
            
            navController.navigate(
                "LogIn"
            )
            
            viewModel.setSnackbar(
              message = "Try service again",
              
              actionLabel = "You was logged out",
              
              duration = SnackbarDuration.Long,
            )
            }
             },
            modifier = 
        Modifier
                
                .fillMaxWidth(1f)
                
                .border(
                    
                    width = 1.dp,
                    color = AppTheme.colors.borderColor3,
                )
                
                .padding(
                    
                    
                    
                    bottom = 16.dp,
                    )
        ,
            
            colors = ButtonDefaults.buttonColors(
              containerColor = AppTheme.colors.accentColor1, 
              contentColor = AppTheme.colors.contentColor1, 
              
              
            ),
            elevation = ButtonDefaults.buttonElevation(
              
              
              
              
              
            ),) {
            
            
            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
              "Log out"
            )
            
                ),
                modifier = 
            Modifier
                    
                    .padding(
                        
                        top = 16.dp,
                        
                        bottom = 16.dp,
                        )
            ,
            )
        }}
}

    
}
