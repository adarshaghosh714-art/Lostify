
package com.example.lostify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

                // Onboarding state
                val isFirstLaunch by preferenceManager
                    .isFirstLaunch
                    .collectAsState(initial = true)

                // Firebase login check
                val isUserLoggedIn = FirebaseAuth
                    .getInstance()
                    .currentUser != null

                LostifyNavHost(
                    isFirstLaunch = isFirstLaunch,
                    isUserLoggedIn = isUserLoggedIn,
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

