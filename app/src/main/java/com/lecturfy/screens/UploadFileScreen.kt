package com.lecturfy.screens

import android.content.SharedPreferences
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.lecturfy.components.DropdownList
import com.lecturfy.components.google.GoogleSignInHelper
import com.lecturfy.components.google.launchGoogleSignIn
import com.lecturfy.components.system.NavController
import com.lecturfy.components.system.NavControllerViewModelDirectionEnum
import com.lecturfy.components.system.SnackbarEntity
import com.lecturfy.components.text.AnnotatedText
import com.lecturfy.components.text.AnnotatedTextPart
import com.lecturfy.theme.AppTheme
import com.lecturfy.viewmodels.UploadfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
) {

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
                if (ent.direction == NavControllerViewModelDirectionEnum.TO) {
                    navController.navigate(ent.path)
                } else if (ent.direction == NavControllerViewModelDirectionEnum.BACK) {
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

            .height(100.dp),
    ) {

        Column(
            modifier =
            Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                modifier =
                Modifier

                    .fillMaxWidth(1f),
            ) {


                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "",
                    tint = Color(android.graphics.Color.parseColor("#17191D")),
                    modifier =
                    Modifier
                        .padding(start = 8.dp, top = 12.dp, bottom = 4.dp)
                        .clickable label@{
                            coroutineScope.launch {

                                navController.navigate(
                                    "UploadedFiles"
                                )
                            }
                        }
                        .padding(8.dp)
                        .size(24.dp)
                )
            }


            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
                        "Upload new file"
                    )

                ),
                modifier =
                Modifier

                    .padding(

                        top = 4.dp,
                        bottom = 40.dp,
                    ),

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
                contentPadding = PaddingValues(),
                modifier =
                Modifier

                    .fillMaxWidth(1f)
                    .padding(horizontal = 16.dp),
                shape = CircleShape,

                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.accentColor2,


                    ),
                elevation = ButtonDefaults.buttonElevation(


                ),
            ) {


                AnnotatedText(
                    parts = listOf(
                        AnnotatedTextPart.TextPart(
                            "Choose a file"
                        )

                    ),
                    modifier =
                    Modifier

                        .padding(

                            top = 28.dp,

                            bottom = 28.dp,
                        ),

                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.dp,
                    color = AppTheme.colors.accentColor1,
                )
            }

            if (state.fileToUpload.filename.isNotBlank()) {

                AnnotatedText(
                    parts = listOf(
                        AnnotatedTextPart.TextPart(
                            state.fileToUpload.filename
                        )

                    ),
                    modifier =
                    Modifier

                        .padding(

                            top = 20.dp,
                        ),

                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.dp,
                    color = Color(0xFF4D5664)
                )
            }


            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
                        """Please select file: mp3, mp4, mpeg, mpga, m4a, wav and webm"""
                    )

                ),
                modifier =
                Modifier

                    .padding(

                        top = 20.dp,
                        bottom = 36.dp,
                        start = 16.dp,
                        end = 16.dp,
                    ),

                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 20.dp,
                color = Color(0xFF4D5664)
            )


            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
                        "Set audio language"
                    )

                ),
                modifier =
                Modifier
                    .fillMaxWidth()

                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp,
                    ),

                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.dp,
                color = AppTheme.colors.headerColor2
            )



            DropdownList(
                options = arrayOf("Auto-detection", "en", "de"),
                selected = state.fileLanguage ?: "",
                onSelectionChange = { viewModel.setFilelanguage(it) },
                textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppTheme.colors.textColor2,
                    unfocusedTextColor = AppTheme.colors.textColor2,
                    cursorColor = AppTheme.colors.textColor2,
                    unfocusedContainerColor = AppTheme.colors.accentColor1,
                    focusedContainerColor = AppTheme.colors.accentColor1,
                    disabledContainerColor = AppTheme.colors.accentColor1,
                    errorContainerColor = AppTheme.colors.accentColor1,
                    disabledBorderColor = AppTheme.colors.borderColor1,
                    errorBorderColor = AppTheme.colors.borderColor1,
                    focusedBorderColor = AppTheme.colors.borderColor1,
                    unfocusedBorderColor = AppTheme.colors.borderColor1,
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(horizontal = 16.dp),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp,
                    color = AppTheme.colors.textColor2,
                )
            )
        }

        Spacer(Modifier.weight(1f))



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
                        authorizationHeaderParam = accessTokenLocal,
                        contentdispositionHeaderParam = fileNameFull,
                        fileEntity = state.fileToUpload.path,
                        contentlanguageHeaderParam = state.fileLanguage,
                    )

                }
            },
            contentPadding = PaddingValues(),

            modifier =
            Modifier

                .fillMaxWidth(1f)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            shape = CircleShape,

            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colors.accentColor2,


                ),
            elevation = ButtonDefaults.buttonElevation(


            ),
        ) {


            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
                        "Transcribe"
                    )

                ),
                modifier =
                Modifier

                    .padding(

                        top = 20.dp,

                        bottom = 20.dp,
                    ),

                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 20.dp,
                color = AppTheme.colors.accentColor1,
            )
        }
    }


}
