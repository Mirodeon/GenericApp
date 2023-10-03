package com.mirodeon.genericapp.network.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertNetwork(var context: Context, var view: View) {

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            ContextCompat.getSystemService(context, ConnectivityManager::class.java)
        val currentNetwork = connectivityManager?.activeNetwork
        val networkCapabilities =
            connectivityManager?.getNetworkCapabilities(currentNetwork)
        return networkCapabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun initialNetworkState() {
        GlobalScope.launch {
            infoNetwork(if (isNetworkAvailable()) "Network found" else "Network not found")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun registerForNetworkStateChanges() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                Thread.sleep(5000)
            }
            val connectivityManager =
                ContextCompat.getSystemService(context, ConnectivityManager::class.java)
            connectivityManager?.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    infoNetwork("Network available")
                }

                override fun onLost(network: Network) {
                    infoNetwork("Network lost")
                }
            })
        }
    }

    private fun infoNetwork(text: String) {
        MainScope().launch {
            Snackbar.make(
                view,
                "Network \n${text}.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}