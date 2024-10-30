package com.lecturfy.screens

import android.content.SharedPreferences
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.lecturfy.components.google.GoogleSignInHelper
import com.lecturfy.components.google.launchGoogleSignIn
import com.lecturfy.components.system.NavController
import com.lecturfy.components.system.NavControllerViewModelDirectionEnum
import com.lecturfy.components.system.SnackbarEntity
import com.lecturfy.components.text.AnnotatedText
import com.lecturfy.components.text.AnnotatedTextPart
import com.lecturfy.theme.AppTheme
import com.lecturfy.viewmodels.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

            .fillMaxSize()

            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 16.dp,
            )
            .background(AppTheme.colors.accentColor1)
        ,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Column(
            modifier =
            Modifier,
        ) {

            Text(
                text = "First name",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = AppTheme.colors.headerColor2,
            )

            OutlinedTextField(
                value = state.userProfile.firstName ?: "",
                onValueChange = { viewModel.setUserprofileFirstName(it) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AppTheme.colors.backgroundColor1,
                    unfocusedContainerColor = AppTheme.colors.backgroundColor1,
                    errorContainerColor = AppTheme.colors.backgroundColor1,
                    disabledContainerColor = AppTheme.colors.backgroundColor1,
                    focusedBorderColor = AppTheme.colors.borderColor1,
                    unfocusedBorderColor = AppTheme.colors.borderColor1,
                    errorBorderColor = AppTheme.colors.borderColor1,
                    disabledBorderColor = AppTheme.colors.borderColor1,
                    focusedTextColor = AppTheme.colors.headerColor2,
                    errorTextColor = AppTheme.colors.headerColor2,
                    disabledTextColor = AppTheme.colors.headerColor2,
                    cursorColor = AppTheme.colors.headerColor2,
                ),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.headerColor2,
                    lineHeight = 20.sp,
                ),
                modifier =
                Modifier
                    .fillMaxWidth(1f)
                    .padding(
                        top = 8.dp,
                        bottom = 16.dp,
                    ),
            )

            Text(
                text = "Last name",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = AppTheme.colors.headerColor2,
            )
            OutlinedTextField(
                value = state.userProfile.lastName ?: "",
                onValueChange = { viewModel.setUserprofileLastName(it) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AppTheme.colors.backgroundColor1,
                    unfocusedContainerColor = AppTheme.colors.backgroundColor1,
                    errorContainerColor = AppTheme.colors.backgroundColor1,
                    disabledContainerColor = AppTheme.colors.backgroundColor1,
                    focusedBorderColor = AppTheme.colors.borderColor1,
                    unfocusedBorderColor = AppTheme.colors.borderColor1,
                    errorBorderColor = AppTheme.colors.borderColor1,
                    disabledBorderColor = AppTheme.colors.borderColor1,
                    focusedTextColor = AppTheme.colors.headerColor2,
                    errorTextColor = AppTheme.colors.headerColor2,
                    disabledTextColor = AppTheme.colors.headerColor2,
                    cursorColor = AppTheme.colors.headerColor2,
                ),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.headerColor2,
                    lineHeight = 20.sp,
                ),
                modifier =
                Modifier
                    .fillMaxWidth(1f)
                    .padding(
                        top = 8.dp,
                        bottom = 24.dp,
                    ),
            )


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
                    ),

                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 28.dp,
                color = AppTheme.colors.headerColor2,
            )

            Row(
                modifier =
                Modifier

                    .padding(
                        top = 10.dp,
                        bottom = 10.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = state.userSettings.shouldSendEmailOnTranscribe,
                    onCheckedChange = { viewModel.setUsersettingsShouldSendEmailOnTranscribe(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = AppTheme.colors.accentColor1,
                        checkedTrackColor = AppTheme.colors.accentColor2,
                        checkedBorderColor = AppTheme.colors.accentColor2,
                        uncheckedThumbColor = AppTheme.colors.accentColor1,
                        uncheckedTrackColor = AppTheme.colors.borderColor1,
                        uncheckedBorderColor = AppTheme.colors.borderColor1,
                    ),
                    modifier =
                    Modifier
                        .padding(
                            end = 8.dp,
                        ),
                )

                AnnotatedText(
                    parts = listOf(
                        AnnotatedTextPart.TextPart(
                            "Receive email alerts after transcripts are generated"
                        )

                    ),
                    modifier =
                    Modifier,

                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.dp,
                    color = AppTheme.colors.textColor2,
                )
            }


            AnnotatedText(
                parts = listOf(
                    AnnotatedTextPart.LinkPart(
                        "Contact the Lecturfy team",
                        // TODO add actual link to lecturfy support
                        "https://www.lecturfy.com/contact"
                    )
                ),
                modifier =
                Modifier,

                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.dp,
            )

            Column(
                modifier =
                Modifier,
            ) {
                Button(
                    onClick = label@{
                        coroutineScope.launch {

                            val accessTokenLocal = sharedPreferences.getString("accessTokenLocal", "emptyToken")!!


                            viewModel.setUserprofiletoupdateFirstName(state.userProfile.firstName)


                            viewModel.setUserprofiletoupdateLastName(state.userProfile.lastName)


                            var updatedProfile = viewModel.userProfilepatchProfile(
                                profileEntity = state.userProfileToUpdate,
                                authorizationHeaderParam = accessTokenLocal,
                            )
                            if (updatedProfile == null) return@launch


                            var updatedSettings = viewModel.userSettingspatch(
                                settingsEntity = state.userSettings,
                                authorizationHeaderParam = accessTokenLocal,
                            )
                            if (updatedSettings == null) return@launch


                        }
                    },
                    contentPadding = PaddingValues(),
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    shape =
                    RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp,
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp,
                    ),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.accentColor2,


                        ),
                    elevation = ButtonDefaults.buttonElevation(


                    ),
                ) {


                    AnnotatedText(
                        parts = listOf(
                            AnnotatedTextPart.TextPart(
                                "Save changes"
                            )

                        ),
                        modifier =
                        Modifier
                            .padding(
                                top = 16.dp,
                                bottom = 16.dp,
                            ),

                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.dp,
                        color = AppTheme.colors.accentColor1,
                    )
                }

            }
            Spacer(Modifier.weight(1f))

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
                    .padding(
                        bottom = 16.dp,
                    )
                    .border(
                        width = 1.dp,
                        color = AppTheme.colors.borderColor3,
                        shape = CircleShape
                    ),
                shape = CircleShape,
                contentPadding = PaddingValues(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.accentColor1,
                    contentColor = AppTheme.colors.contentColor1,
                    ),
                elevation = ButtonDefaults.buttonElevation(


                ),
            ) {


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
                        ),
                    fontSize = 14.sp,
                    lineHeight = 20.dp,
                    fontWeight = FontWeight.Medium,
                    color = AppTheme.colors.contentColor1,
                )
            }
        }
    }


}
