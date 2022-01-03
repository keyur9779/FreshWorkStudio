package com.app.freshworkstudio.utils.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.app.freshworkstudio.FreshWorkApp

/**
 *Broadcast receiver for network change event.
 */
class NetworkChangeReceiver() {

    fun registerNetworkEvent() {

        val builder = NetworkRequest.Builder()

        FreshWorkApp.connectivityManager.registerNetworkCallback(
            builder.build(),
            networkCallback
        )
    }

    private val mNetowrkListenerMap = arrayListOf<NetworkChangeListener>()
    fun addNetowrkListener(networkChangeListener: NetworkChangeListener) {
        mNetowrkListenerMap.add(networkChangeListener)

    }

    fun removeNetowrkListener(networkChangeListener: NetworkChangeListener) {
        mNetowrkListenerMap.remove(networkChangeListener)

    }


    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            sendNetworkEvent()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
        }
    }

    private fun sendNetworkEvent() {

        val iterator = mNetowrkListenerMap.iterator()
        while (iterator.hasNext()) {
            val reference = iterator.next()
            if (reference == null) {
                iterator.remove()
                continue
            }
            if (reference != null) {
                reference.onNetworkChange(true)
            }
        }
    }


    // Interface to send opt to listeners
    interface NetworkChangeListener {
        fun onNetworkChange(isNetworkAvailable: Boolean)
    }
}