package com.example.lostify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.lostify.data.AppContextHolder
import com.example.lostify.data.PreferenceManager
import com.example.lostify.navigation.LostifyNavHost
import com.example.lostify.ui.theme.LostifyTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContextHolder.context = applicationContext
        enableEdgeToEdge()

        setContent {

            LostifyTheme {

                val context = LocalContext.current
                val preferenceManager = remember { PreferenceManager(context) }

                val isFirstLaunch by preferenceManager
                    .isFirstLaunch
                    .collectAsState(initial = null)

                val auth = FirebaseAuth.getInstance()

                val isUserLoggedInState = remember {
                    mutableStateOf(auth.currentUser != null)
                }

                DisposableEffect(Unit) {
                    val listener = FirebaseAuth.AuthStateListener {
                        isUserLoggedInState.value = it.currentUser != null
                    }
                    auth.addAuthStateListener(listener)

                    onDispose {
                        auth.removeAuthStateListener(listener)
                    }
                }

                if (isFirstLaunch != null) {

                    LostifyNavHost(
                        isFirstLaunch = isFirstLaunch!!,
                        isUserLoggedIn = isUserLoggedInState.value,
                        onOnboardingFinished = {
                            CoroutineScope(Dispatchers.IO).launch {
                                preferenceManager.setFirstLaunchCompleted()
                            }
                        }
                    )
                }
            }
        }
    }
}