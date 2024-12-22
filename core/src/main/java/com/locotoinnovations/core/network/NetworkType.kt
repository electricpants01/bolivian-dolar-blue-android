package com.locotoinnovations.core.network

enum class NetworkType(private val string: String) {
    WIFI("wifi"),
    CELLULAR("wwan"),
    NONE("none");

    override fun toString() = string
}