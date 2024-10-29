package com.lecturfy.screens

import android.content.SharedPreferences
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.lecturfy.components.google.GoogleSignInHelper
import com.lecturfy.components.google.launchGoogleSignIn
import com.lecturfy.components.system.NavController
import com.lecturfy.components.system.NavControllerViewModelDirectionEnum
import com.lecturfy.components.system.SnackbarEntity
import com.lecturfy.components.text.AnnotatedText
import com.lecturfy.components.text.AnnotatedTextPart
import com.lecturfy.viewmodels.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

            .background(Color(android.graphics.Color.parseColor("#7DDEB0"))),
    ) {

        Box(
            modifier =
            Modifier

                .fillMaxWidth(1f)

                .fillMaxHeight(0.47f),
        ) {

            AsyncImage(
                model = "https://firebasestorage.googleapis.com/v0/b/lecturfy-assets.appspot.com/o/login-image.png?alt=media&token=d32ea3b0-0b09-4c8b-b5a5-26f19fe52ac0",
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .align(BiasAlignment(horizontalBias = 0f, verticalBias = 0.33f))
                    .fillMaxWidth()
                    .padding(start = 19.dp, end = 19.dp),
            )
        }

        Column(
            modifier =
            Modifier

                .fillMaxWidth(1f)

                .fillMaxHeight(1f)

                .clip(
                    RoundedCornerShape(
                        topStart = 36.dp,
                        topEnd = 36.dp,
                    )
                )

                .background(Color(android.graphics.Color.parseColor("#F4F6F7")))

                .padding(

                    top = 20.dp,


                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
                        "Log in"
                    )

                ),
                modifier =
                Modifier
                    .padding(bottom = 6.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 38.dp,
                color = Color(0xFF17191D)
            )


            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.TextPart(
                        "Get study insights fast"
                    )

                ),
                modifier =
                Modifier,

                fontSize = 18.sp,
                lineHeight = 28.dp,
                color = Color(android.graphics.Color.parseColor("#323B49")),
            )

            Column(
                modifier =
                Modifier

                    .padding(

                        top = 24.dp,

                        bottom = 10.dp,
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Button(
                    onClick = label@{
                        coroutineScope.launch {

                            googleSignInHelper.configure(
                                webClientId = "155394191404-gcuv3p3nl9uh2in1rih17htlt5lii64n.apps.googleusercontent.com",
                                scopes = listOf()
                            )
                            val googleCode = viewModel.initiateGoogleSignIn()!!


                            viewModel.setGoogleauthvariableCode(googleCode)


                            var apiGoogleAuthResponse = viewModel.logingoogle(
                                codeEntity = state.googleAuthVariable,
                            )
                            if (apiGoogleAuthResponse == null) return@launch



                            if (apiGoogleAuthResponse.code() == 200) {

                                with(sharedPreferences.edit()) {
                                    putString("accessTokenLocal", apiGoogleAuthResponse.body()!!.accessTokenLocal)
                                    apply()
                                }

                            }


                        }
                    },
                    modifier =
                    Modifier

                        .border(
                            width = 1.dp,
                            color = Color(android.graphics.Color.parseColor("#DEDFE0")),
                            shape =
                            RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp,
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp,
                            )
                        ),
                    shape =
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp,
                    ),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(android.graphics.Color.parseColor("#FFFFFF")),


                        ),
                    elevation = ButtonDefaults.buttonElevation(


                    ),
                    contentPadding = PaddingValues()
                ) {

                    Row(
                        modifier =
                        Modifier

                            .padding(
                                start = 20.dp,
                                top = 13.dp,
                                end = 20.dp,
                                bottom = 13.dp,
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AsyncImage(
                            model = "https://firebasestorage.googleapis.com/v0/b/lecturfy-assets.appspot.com/o/google-logo-24x24.png?alt=media&token=b021761a-54c1-4532-b2ea-c1a968f9762c",
                            contentDescription = null,
                            modifier =
                            Modifier

                                .padding(


                                    end = 10.dp,

                                    )
                                .width(24.dp)

                                .height(24.dp),
                        )


                        AnnotatedText(
                            parts = listOf(
                                AnnotatedTextPart.TextPart(
                                    "Continue with Google"
                                )

                            ),
                            modifier =
                            Modifier,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            lineHeight = 22.dp,
                            color = Color(0xFF17191D)
                        )
                    }
                }
            }

            Column(
                modifier =
                Modifier

                    .fillMaxHeight(1f)

                    .padding(


                        bottom = 30.dp,
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                AnnotatedText(
                    parts = listOf(
                        AnnotatedTextPart.TextPart(
                            "By using the service, you agree to our"
                        )

                    ),
                    modifier =
                    Modifier,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.dp,
                    color = Color(android.graphics.Color.parseColor("#323B49")),
                )

                Row(
                    modifier =
                    Modifier,
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
                        Modifier,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 20.dp,
                        color = Color(android.graphics.Color.parseColor("#323B49")),
                    )


                    AnnotatedText(
                        parts = listOf(
                            AnnotatedTextPart.TextPart(
                                " and "
                            )

                        ),
                        modifier =
                        Modifier,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 20.dp,
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
                        Modifier,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 20.dp,
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
                        .padding(top = 12.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.dp,
                    color = Color(android.graphics.Color.parseColor("#4D5664")),
                )
            }
        }
    }


}
