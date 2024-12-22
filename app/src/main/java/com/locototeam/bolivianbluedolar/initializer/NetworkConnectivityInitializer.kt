package com.locototeam.bolivianbluedolar.initializer

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.locotoinnovations.core.network.NetworkProvider

@Suppress("unused")
class NetworkConnectivityInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val application = context as Application
        NetworkProvider.init(application = application)
//        NetworkConnectivityManager.init(application = application)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}