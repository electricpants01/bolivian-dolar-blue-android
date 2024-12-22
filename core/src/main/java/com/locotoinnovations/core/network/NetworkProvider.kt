package com.locotoinnovations.core.network

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkProvider {

    companion object {
        private lateinit var application: Application

        @JvmStatic
        fun init(application: Application) {
            Companion.application = application
        }
    }

    fun isConnected() = getNetworkInfo()?.isConnected == true

    fun isConnectedOnWifi() = isConnected() && getNetworkInfo()?.type == ConnectivityManager.TYPE_WIFI

    fun getNetworkType(): NetworkType {
        return when {
            isConnectedOnWifi() -> NetworkType.WIFI
            isConnected() -> NetworkType.CELLULAR
            else -> NetworkType.NONE
        }
    }

    // Add suppress lint because this is already in the manifest
    @SuppressLint(value = ["MissingPermission"])
    private fun getNetworkInfo(): NetworkInfo? {
        return (application.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo
    }
}