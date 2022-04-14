package com.arshadshah.nimaz.helperClasses.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast

/**
 * Checks if a device has network connection
 * @author Arshad Shah
 */
class NetworkChecker {

    /**
     * Checks all connections present on a device
     * @author Arshad Shah
     * @param context The context of the Application
     * @return True if connection is detected, False if connection is not detected
     */
    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }


    /**
     * Check if there is internet connection
     *
     * @param context the context of the Application
     * @param view The view to show snackbar on
     * */
    fun networkCheck(context: Context): Boolean {
        val networkCheck = isNetworkAvailable(context)
        return if (networkCheck) {
            Log.i("Network", "Network is Successfully connected")
            true
        } else {
            Toast.makeText(context, "No internet connection detected", Toast.LENGTH_SHORT).show()
            Log.i("Network", "Network is not connected")
            false
        }
    }
}
