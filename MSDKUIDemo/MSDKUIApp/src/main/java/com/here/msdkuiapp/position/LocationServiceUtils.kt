/*
 * Copyright (C) 2017-2018 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.here.msdkuiapp.position

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient
import com.here.msdkuiapp.common.PermissionRequestCode

/**
 * Util class to deal with location services.
 */
object LocationServiceUtils {

    /**
     * Callback for location services status.
     */
    interface LocationServiceStateListener {

        /**
         * Indicates location service is enabled.
         */
        fun onServiceOk()

        /**
         * Indicates user denied enabling of location service.
         */
        fun onServiceFailed()
    }

    /**
     * Checks location services status and notified to user via listener. In case, location services are disabled,
     * it will ask permission to enable it.
     *
     * @param context activity context to check & request permission.
     * @param client [SettingsClient] [LocationServices] settings client.
     * @param listener [LocationServiceStateListener] to notify the status of location services.
     * @see [onActivityResult]
     */
    fun checkLocationServices(context: Context, client: SettingsClient, listener: LocationServiceStateListener?) {

        if (isServiceOk(context)) {
            listener?.onServiceOk()
            return
        }

        val locationRequest = LocationRequest();
        val settingsRequest = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

        val responseTask = client.checkLocationSettings(settingsRequest.build());
        if (responseTask.isComplete) {
            return
        }
        responseTask.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)
                listener?.onServiceOk()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(context as Activity, PermissionRequestCode.LOCATION_SERVICE_REQ_CODE)
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    /**
     * Checks if location service is enabled.
     *
     * @param context activity context to check location service status.
     * @return true if enabled, false otherwise.
     */
    fun isServiceOk(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Result of location services status. Asking permission to enable location services will result in [Activity.onActivityResult].
     * call this method from [Activity.onActivityResult] to get notified for location services status.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param listener [LocationServiceStateListener] to get notified.
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, listener: LocationServiceStateListener?) {
        if (requestCode == PermissionRequestCode.LOCATION_SERVICE_REQ_CODE) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> listener?.onServiceFailed()
                else -> listener?.onServiceOk()
            }
        }
    }
}