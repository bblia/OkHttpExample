package com.example.okhttpexample.network

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.RemoteException
import android.view.View

class ConnectivityHelper(private val context: Application) {
    val isConnected: Boolean
        get() {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            try {
                val networkCapabilities =
                    connectivityManager.getNetworkCapabilities(network) ?: return false

                return when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            } catch (ex: RemoteException) {
                // In case we're unable to determine network connectivity, fallback to
                // isConnected true to prevent showing the connectivity banner unnecessarily
                return true
            }
        }
}

