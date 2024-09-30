package com.locotoinnovations.bolivianbluedolar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.locotoinnovations.bolivianbluedolar.broadcast.AlarmSchedulerImpl
import com.locotoinnovations.bolivianbluedolar.ui.screen.MainScreen
import com.locotoinnovations.bolivianbluedolar.ui.theme.BolivianBlueDolarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var snackbarHostState: SnackbarHostState
    lateinit var scope: CoroutineScope

    // Launcher for the permission request
    val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // permission granted
            // Update the button state to hide it
            AlarmSchedulerImpl(this).schedule()
            notificationPermissionGranted()
        } else {
            // permission denied or forever denied
            scope.launch {
                snackbarHostState.showSnackbar("Permiso de notificacion denegado")
            }
        }
    }

    // MutableState for notification button visibility
//    private lateinit var shouldShowNotificationButton: MutableState<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BolivianBlueDolarTheme {
                snackbarHostState = remember { SnackbarHostState() }
                scope = rememberCoroutineScope()

                // Initialize the state for whether the notification button should be shown
//                shouldShowNotificationButton = remember {
//                    mutableStateOf(
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                            ContextCompat.checkSelfPermission(
//                                this, Manifest.permission.POST_NOTIFICATIONS
//                            ) != PackageManager.PERMISSION_GRANTED
//                        } else {
//                            // Permission is not needed for versions lower than Android 13
//                            false
//                        }
//                    )
//                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        snackbarHostState = snackbarHostState,
                        shouldShowNotificationButton = true,
                        onRequestNotificationPermission = {
                            requestNotificationPermission()
                        }
                    )
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // permission already granted
            notificationPermissionGranted()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // show rationale and then launch launcher to request permission
            } else {
                // first request or forever denied case
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun notificationPermissionGranted() {
        scope.launch {
            snackbarHostState.showSnackbar("Notificaciones diarias a las 8am")
        }
    }
}
