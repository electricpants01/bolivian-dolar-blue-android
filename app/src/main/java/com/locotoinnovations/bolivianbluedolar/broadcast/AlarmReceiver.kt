package com.locotoinnovations.bolivianbluedolar.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.locotoinnovations.bolivianbluedolar.network.DataResult
import com.locotoinnovations.bolivianbluedolar.ui.screen.BinanceSearchRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
            // Collect the buy price
            val buyPriceResult = binanceSearchRepository.getBuyPrice().first()
            val sellPriceResult = binanceSearchRepository.getSellPrice().first()

            // Prepare notification content
            val notificationContent = buildNotificationContent(buyPriceResult, sellPriceResult)

            // Send the notification
            sendNotification(context, notificationContent)
        }

        job.invokeOnCompletion { throwable ->
            throwable?.let {
                // Handle errors if needed
                Log.e("AlarmReceiver", "Error retrieving prices: ${it.message}")
            }
        }
    }

    private fun buildNotificationContent(
        buyPriceResult: DataResult<Double>,
        sellPriceResult: DataResult<Double>
    ): String {
        return buildString {
            append("Prices:\n")
            when (buyPriceResult) {
                is DataResult.Success -> append("Buy Price: ${buyPriceResult.data}\n")
                is DataResult.Failure -> append("Buy Price Error: ${buyPriceResult}\n")
            }
            when (sellPriceResult) {
                is DataResult.Success -> append("Sell Price: ${sellPriceResult.data}\n")
                is DataResult.Failure -> append("Sell Price Error: ${sellPriceResult}\n")
            }
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
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Daily Price Update")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}
