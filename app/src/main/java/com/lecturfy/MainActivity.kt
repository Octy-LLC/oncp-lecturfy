package com.lecturfy

import android.os.Bundle
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import android.content.SharedPreferences
import javax.inject.Inject
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.navigation.compose.rememberNavController
import com.lecturfy.components.utils.getSavedLocale
import com.lecturfy.components.utils.saveLocale
import com.lecturfy.components.utils.updateLocale
import androidx.compose.runtime.remember
import com.lecturfy.screens.overlay.TopBar
import com.lecturfy.screens.overlay.BottomBar
import com.lecturfy.screens.overlay.ConditionalDrawer
import com.lecturfy.theme.AppTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var localizationContext: Context
    @Inject lateinit var sharedPreferences: SharedPreferences

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                val currentLocale = getSavedLocale(this)
                localizationContext = updateLocale(this, currentLocale)
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()

                val drawerState = rememberDrawerState(DrawerValue.Closed)
                
                CompositionLocalProvider(LocalContext provides localizationContext) {
                AppTheme{
                    ConditionalDrawer(
                        navController = navController,
                        coroutineScope = coroutineScope,
                        sharedPreferences = sharedPreferences,
                        drawerState = drawerState,
                        snackbarHostState = snackbarHostState,
                    ) {
                        Scaffold(
                            snackbarHost = { SnackbarHost(snackbarHostState) },
                            topBar = {
                                TopBar(
                                    navController = navController,
                                    coroutineScope = coroutineScope,
                                    sharedPreferences = sharedPreferences,
                                    drawerState = drawerState,
                                    snackbarHostState = snackbarHostState
                                )
                            },
                            bottomBar = {
                                BottomBar(
                                    navController = navController,
                                    coroutineScope = coroutineScope,
                                    sharedPreferences = sharedPreferences,
                                    drawerState = drawerState,
                                    snackbarHostState = snackbarHostState
                                )
                            }
                        ) { padding ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                NavGraph(
                                    snackbarHostState = snackbarHostState,
                                    sharedPreferences = sharedPreferences,
                                    coroutineScope = coroutineScope,
                                    drawerState = drawerState,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}