package com.lecturfy.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
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
import com.lecturfy.theme.AppTheme
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import com.lecturfy.components.DropdownList
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.CircularProgressIndicator
import android.net.Uri
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
import com.lecturfy.viewmodels.UploadfileViewModel
import com.lecturfy.R
import com.lecturfy.components.utils.getSavedLocale
import com.lecturfy.components.utils.saveLocale
import com.lecturfy.components.utils.updateLocale
import com.lecturfy.components.utils.FileUtils

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadfileScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    sharedPreferences: SharedPreferences,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: UploadfileViewModel = hiltViewModel(),
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
    
    


val fileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
  viewModel.createFileDeferred(uri)
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
    verticalArrangement = Arrangement.SpaceBetween,
){
    
    Column(
        modifier = 
    Modifier
    ,
        horizontalAlignment = Alignment.CenterHorizontally,
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
        
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          "Upload new file"
        )
        
            ),
            modifier = 
        Modifier
                
                .padding(
                    
                    
                    
                    bottom = 40.dp,
                    )
        ,
            
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppTheme.colors.headerColor1,
            textAlign = TextAlign.Center,
            lineHeight = 38.dp,
        )
        
        
        
        Button(
            onClick = label@{ 
            coroutineScope.launch {
            
            viewModel.completeFileDeferred()
            fileLauncher.launch(
              "*/*"
            )
            val file = viewModel.state.value.fileDeferred?.await()
            if (file != null) {
              
              viewModel.setFiletouploadFilename(file.filename)
              
              
              viewModel.setFiletouploadExtension(file.extension)
              
              
              viewModel.setFiletouploadPath(file.path)
              
            }
            
            
            }
             },
            modifier = 
        Modifier
                
                .fillMaxWidth(1f)
                
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
              "Choose a file"
            )
            
                ),
                modifier = 
            Modifier
                    
                    .padding(
                        
                        top = 24.dp,
                        
                        bottom = 24.dp,
                        )
            ,
                
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 28.dp,
            )
        }    
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          state.fileToUpload.filename
        )
        
            ),
            modifier = 
        Modifier
                
                .padding(
                    
                    top = 16.dp,
                    
                    bottom = 26.dp,
                    )
        ,
            
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.dp,
        )
        
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          """Please select file: mp3, mp4, mpeg, mpga, m4a,
        wav and webm"""
        )
        
            ),
            modifier = 
        Modifier
                
                .padding(
                    
                    top = 16.dp,
                    
                    bottom = 26.dp,
                    )
        ,
            
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.dp,
        )
        
        
        AnnotatedText(
            parts = listOf(
                AnnotatedTextPart.TextPart(
          "Set audio language"
        )
        
            ),
            modifier = 
        Modifier
                
                .padding(
                    
                    
                    
                    bottom = 8.dp,
                    )
        ,
            
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.dp,
        )
        
        
        
        DropdownList(
          options = arrayOf( "Auto-detection", "en", "de",),
          selected = state.fileLanguage ?: "",
          onSelectionChange = { viewModel.setFilelanguage(it) },
          textFieldColors = TextFieldDefaults.textFieldColors(
          
          
          
          ),
          
          
          
          
        )}
    
    
    
    
    
    Button(
        onClick = label@{ 
        coroutineScope.launch {
        
        val accessTokenLocal = sharedPreferences.getString("accessTokenLocal", "emptyToken")!!
        
        
        
        val fileName = 
        "attachment; filename=\"" + "" + state.fileToUpload.filename
        
        
        
        
        
        val fileNameDot = 
        fileName + "" + "."
        
        
        
        
        
        val fileNameExt = 
        fileNameDot + "" + state.fileToUpload.extension
        
        
        
        
        
        val fileNameFull = 
        fileNameExt + "" + "\""
        
        
        
        
         viewModel.transcriptionsmake(
          authorizationHeaderParam=accessTokenLocal,
          contentdispositionHeaderParam=fileNameFull,
          fileEntity=state.fileToUpload.path,
          contentlanguageHeaderParam=state.fileLanguage,
        )
        
        }
         },
        modifier = 
    Modifier
            
            .fillMaxWidth(1f)
            
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
          "Transcribe"
        )
        
            ),
            modifier = 
        Modifier
                
                .padding(
                    
                    top = 24.dp,
                    
                    bottom = 24.dp,
                    )
        ,
            
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = 28.dp,
        )
    }}

    
}
