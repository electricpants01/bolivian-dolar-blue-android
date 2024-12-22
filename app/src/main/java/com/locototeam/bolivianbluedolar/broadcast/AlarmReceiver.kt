package com.locototeam.bolivianbluedolar.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.locotoinnovations.core.repository.BinanceSearchRepository
import com.locototeam.bolivianbluedolar.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    // Inject the BinanceSearchRepository using Dagger Hilt
    @Inject
    lateinit var binanceSearchRepository: BinanceSearchRepository

    override fun onReceive(context: Context, intent: Intent?) {
        // Launch a coroutine to retrieve prices and send a notification
        retrievePricesAndSendNotification(context)
    }

    private fun retrievePricesAndSendNotification(context: Context) {
        // Use a CoroutineScope to collect Flow results
        val job = CoroutineScope(Dispatchers.IO).launch {
            val (buy , sell ) = binanceSearchRepository.fetchDolarBlueData(0)
            buildNotificationContent(buy, sell)
        }

        job.invokeOnCompletion { throwable ->
            throwable?.let {
                // Handle errors if needed
                Log.e("AlarmReceiver", "Error retrieving prices: ${it.message}")
            }
        }
    }

    private fun buildNotificationContent(
        buy: Double,
        sell: Double,
    ): String {
        return buildString {
            append("Precio compra: ${String.format(locale = null,"%.2f", buy)}\n")
            append("Precio venta: ${String.format(locale = null,"%.2f", sell)}\n")
        }
    }

    private fun sendNotification(context: Context, content: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "daily_notification_channel"
        val channelName = "Daily Notifications"

        // Create a notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Actualizacion de dolar blue Bolivia")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}