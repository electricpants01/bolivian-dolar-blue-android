package com.locotoinnovations.bolivianbluedolar

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.locotoinnovations.bolivianbluedolar.broadcast.AlarmReceiver
import com.locotoinnovations.bolivianbluedolar.ui.screen.MainScreen
import com.locotoinnovations.bolivianbluedolar.ui.theme.BolivianBlueDolarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Permission request code for notifications
    private val notificationPermissionRequestCode = 101

    // Check if the app has notification permission
    val shouldShowNotificationButton: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        } else {
            // Permission is not needed for versions lower than Android 13
            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BolivianBlueDolarTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        shouldShowNotificationButton = shouldShowNotificationButton,
                        onRequestNotificationPermission = {
                            requestNotificationPermission { granted ->
                                if (granted) {
                                    // Show Snackbar when permission is granted
                                    showSnackbar(snackbarHostState, "Notification permission granted")
                                    // Schedule your daily notification here
                                    scheduleDailyNotification(this@MainActivity)
                                } else {
                                    showSnackbar(snackbarHostState, "Notification permission denied")
                                }
                            }
                        })
                }
            }
        }
    }

    // Function to request notification permission for API 33+ (Android 13 and above)
    private fun requestNotificationPermission(onGranted: (Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                notificationPermissionRequestCode
            )
        } else {
            // Permission not required, proceed with the onGranted action
            onGranted(true)
        }
    }

    // Function to show the Snackbar
    private fun showSnackbar(snackbarHostState: SnackbarHostState, message: String) {
        // Using a new thread to show the Snackbar
        lifecycleScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationPermissionRequestCode) {
            // Check if the POST_NOTIFICATIONS permission was granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, take any necessary action
                scheduleDailyNotification(this@MainActivity)
            } else {
                // Permission denied, handle appropriately
                // Optionally show a message or fallback
            }
        }
    }

    // Call your method to schedule daily notifications
    private fun scheduleDailyNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Set the time to 8:00 AM
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        // If the time is already past for today, schedule for tomorrow
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule the alarm to repeat daily
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}