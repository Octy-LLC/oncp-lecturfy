package com.lecturfy.screens.overlay

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.lecturfy.theme.AppTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun BottomBar(
    snackbarHostState: SnackbarHostState,
    sharedPreferences: SharedPreferences,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showMainOverlayList = listOf<String>(
        "Settings", "UploadFile", "UploadedFiles",
    )

    val showCustomOverlayList: Map<String, @Composable () -> Unit> = hashMapOf(

    )

    val CustomOverlayScreen = showCustomOverlayList[currentRoute]

    when {

        CustomOverlayScreen != null -> {
            CustomOverlayScreen()
        }

        currentRoute in showMainOverlayList -> {
            Column(Modifier.fillMaxWidth()) {
                HorizontalDivider(thickness = 1.dp, color = AppTheme.colors.borderColor2)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 11.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    navController.navigate(
                                        "UploadFile",
                                        navOptions {
                                            popUpTo("Initial") { inclusive = true }
                                        }
                                    )
                                }
                            )
                            .weight(1f)
                    ) {
                        Tab(
                            icon = Icons.Filled.AddCircleOutline,
                            isSelected = currentRoute in listOf("UploadFile", "UploadedFiles"),
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    navController.navigate("Settings",
                                        navOptions {
                                            popUpTo("Initial") { inclusive = true }
                                        }
                                    )
                                })
                            .weight(1f)
                    ) {
                        Tab(
                            icon = Icons.Filled.AccountCircle,
                            isSelected = currentRoute == "Settings",
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }
        }

        else -> {}
    }
}

@Composable
private fun Tab(
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .size(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) AppTheme.colors.backgroundColor2 else Color.Transparent)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) AppTheme.colors.textColor2 else AppTheme.colors.headerColor2,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center)
        )
    }
}