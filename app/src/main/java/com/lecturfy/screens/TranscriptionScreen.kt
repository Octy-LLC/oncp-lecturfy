package com.lecturfy.screens

import android.content.SharedPreferences
import android.webkit.WebView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.lecturfy.components.google.GoogleSignInHelper
import com.lecturfy.components.google.launchGoogleSignIn
import com.lecturfy.components.system.NavController
import com.lecturfy.components.system.NavControllerViewModelDirectionEnum
import com.lecturfy.components.system.SnackbarEntity
import com.lecturfy.components.text.AnnotatedText
import com.lecturfy.components.text.AnnotatedTextPart
import com.lecturfy.theme.AppTheme
import com.lecturfy.viewmodels.TranscriptionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranscriptionScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    sharedPreferences: SharedPreferences,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: TranscriptionViewModel = hiltViewModel(),
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
            .background(AppTheme.colors.accentColor1)
            .fillMaxWidth()
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
                    .padding(start = 8.dp, top = 12.dp, bottom = 16.dp)
                    .clickable {
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

        Row(
            modifier =
            Modifier
                .padding(
                    top = 4.dp,
                    bottom = 12.dp,
                ),
        ) {
            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
                        state.transcription.title
                    )
                ),
                modifier =
                Modifier
                    .padding(
                        start = 16.dp,
                        top = 10.dp,
                        end = 16.dp,
                        bottom = 10.dp,
                    ),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 28.dp,
                color = AppTheme.colors.headerColor2,
            )
        }

        Row(
            modifier =
            Modifier
                .padding(
                    top = 10.dp,
                    bottom = 6.dp,
                    start = 16.dp
                ),
        ) {
            Button(
                onClick = label@{ },
                modifier =
                Modifier
                    .background(AppTheme.colors.backgroundColor2, shape = RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.accentColor2,
                        shape = RoundedCornerShape(8.dp),
                    ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.backgroundColor2,
                ),
                elevation = ButtonDefaults.buttonElevation(


                ),
            ) {
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
                        ),

                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.dp,
                    color = AppTheme.colors.textColor1,
                )
            }
            Button(
                onClick = label@{
                    coroutineScope.launch {

                        val transcriptionId = navController.currentBackStackEntry?.arguments?.getString("id") ?: ""


                        navController.navigate(
                            "Summary/${transcriptionId}"
                        )
                    }
                },
                modifier =
                Modifier
                    .padding(start = 15.dp)
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.borderColor2,
                        shape = RoundedCornerShape(8.dp),
                    ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.accentColor1,
                ),
                elevation = ButtonDefaults.buttonElevation(


                ),
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
                            start = 12.dp,
                            top = 10.dp,
                            end = 12.dp,
                            bottom = 10.dp,
                        ),

                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.dp,
                    color = AppTheme.colors.textColor1,
                )
            }
        }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f),
            factory = { context ->
                WebView(context).apply {
                    settings.defaultFontSize = 18
                    settings.defaultFixedFontSize = 18
                }
            }
        ) { webView ->
            webView.loadUrl(state.transcription.linkToFile)
        }
    }
}
