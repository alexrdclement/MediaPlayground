package com.alexrdclement.mediaplayground

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alexrdclement.mediaplayground.app.App
import com.alexrdclement.mediaplayground.app.navigation.rememberMediaPlaygroundNavController
import com.alexrdclement.palette.navigation.NavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberMediaPlaygroundNavController(
                initialDeeplink = intent?.getDeeplink(),
                buildSyntheticBackStack = intent.isNewTask,
                onBackStackEmpty = ::finish,
            ).also { navController = it }

            App(navController = navController)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        intent.getDeeplink()?.let { deeplink ->
            navController?.navigateToDeeplink(deeplink, replace = false)
        }
    }

    private fun Intent.getDeeplink(): String? {
        return data?.let { uri ->
            uri.path?.removePrefix("/") ?: ""
        }
    }

    private val Intent?.isNewTask: Boolean
        get() = (this?.flags ?: 0) and Intent.FLAG_ACTIVITY_NEW_TASK != 0
}
