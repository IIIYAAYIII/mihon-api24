package eu.kanade.tachiyomi.util.system

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import logcat.LogPriority
import tachiyomi.core.common.util.system.logcat

data class NetworkState(
    val isConnected: Boolean,
    val isValidated: Boolean,
    val isWifi: Boolean,
) {
    val isOnline = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        isConnected && isValidated
    } else {
        isConnected
    }
}

@Suppress("DEPRECATION")
fun Context.activeNetworkState(): NetworkState {
    val cm = connectivityManager
    val activeNet = cm.activeNetwork
    val capabilities = if (activeNet != null) cm.getNetworkCapabilities(activeNet) else null

    val isConnected = cm.activeNetworkInfo?.isConnected ?: false
    val isValidated = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ?: isConnected

    val isWifiTransport = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
        capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true

    val isWifiLegacy = try {
        cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.isConnected == true ||
            cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET)?.isConnected == true ||
            cm.activeNetworkInfo?.let {
                it.type == ConnectivityManager.TYPE_WIFI || it.type == ConnectivityManager.TYPE_ETHERNET
            } == true
    } catch (_: Throwable) {
        false
    }

    val hasWifiUnderlying = try {
        if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true ||
            cm.activeNetworkInfo?.type == ConnectivityManager.TYPE_VPN
        ) {
            cm.allNetworks.any { net ->
                val caps = cm.getNetworkCapabilities(net)
                caps?.let {
                    it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                } == true
            }
        } else {
            false
        }
    } catch (_: Throwable) {
        false
    }

    val isWifi = isWifiTransport || isWifiLegacy || hasWifiUnderlying

    return NetworkState(
        isConnected = isConnected,
        isValidated = isValidated,
        isWifi = isWifi,
    )
}

fun Context.networkStateFlow() = callbackFlow {
    trySend(activeNetworkState())

    val networkCallback = object : NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            trySend(activeNetworkState())
        }
        override fun onLost(network: Network) {
            trySend(activeNetworkState())
        }
    }

    try {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    } catch (e: Throwable) {
        logcat(LogPriority.ERROR, e) { "Failed to register network callback" }
    }
    awaitClose {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (_: Throwable) {
        }
    }
}
