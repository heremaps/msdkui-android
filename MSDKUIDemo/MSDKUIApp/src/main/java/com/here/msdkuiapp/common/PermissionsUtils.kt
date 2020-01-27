/*
 * Copyright (C) 2017-2020 HERE Europe B.V.
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

package com.here.msdkuiapp.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Util class to handle android OS permissions.
 */
object PermissionsUtils {

    /**
     * Base interface class for all permission listeners.
     */
    interface StateListener {
    }

    /**
     * Callback for location permission status.
     */
    interface PositioningStateListener : StateListener {

        /**
         * Indicates location permission is ok.
         */
        fun onLocationPermissionOK()

        /**
         * Indicates location permission is denied by user.
         */
        fun onLocationPermissionFailed()
    }

    /**
     * Callback for storage permission status.
     */
    interface StorageStateListener : StateListener {

        /**
         * Indicates storage permission is ok.
         */
        fun onStoragePermissionOK()

        /**
         * Indicates storage permission is denied by user.
         */
        fun onStoragePermissionFailed()
    }

    /**
     * Checks location permission and notified to user via listener. In case, app don't have permission for position
     * it will ask permission to enable it.
     *
     * @param context activity context to check & request permission.
     * @param listener [PositioningStateListener] to notify the status of location permission.
     * @see [onRequestPermissionsResult]
     */
    fun checkLocationPermission(context: Context, listener: PositioningStateListener?) {
        if (isLocationPermissionOk(context).not()) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PermissionRequestCode.POSITIONING_REQ_CODE)
        } else {
            listener?.onLocationPermissionOK()
        }
    }

    /**
     * Checks if location permission is enabled.
     *
     * @param context activity context to check permission.
     * @return true if enabled, false otherwise.
     */
    fun isLocationPermissionOk(context: Context): Boolean {
        return (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks storage permission and notified to user via listener. In case, app don't have permission for storage
     * it will ask permission to enable it.
     *
     * @param context activity context to check & request permission.
     * @param listener [PositioningStateListener] to notify the status of storage permission.
     * @see [onRequestPermissionsResult]
     */
    fun checkStoragePermission(context: Context, listener: StorageStateListener?) {
        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            listener?.onStoragePermissionOK()
            return
        }
        // if positioning is already ready, ignore the check
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PermissionRequestCode.STORAGE_REQ_CODE)
        } else {
            listener?.onStoragePermissionOK()
        }
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value [PackageManager.PERMISSION_GRANTED].
     *
     * @see Activity.onRequestPermissionsResult
     */
    private fun isPermissionGranted(grantResults: IntArray): Boolean {
        // At least one result must be checked.
        if (grantResults.isEmpty()) {
            return false
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * Result of permission result. Asking permission from user returns the result in [Activity.onRequestPermissionsResult].
     * call this method from [Activity.onRequestPermissionsResult] to get notified for storage or location permission
     * result.
     *
     * @param requestCode The request code passed in {@link #requestPermissions(String[], int)}.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     * @param listener [StateListener] to  get notified for storage or location permission result.
     */
    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray,
                                   listener: StateListener?) {

        if (requestCode == PermissionRequestCode.STORAGE_REQ_CODE) {
            val storageLister = listener as? StorageStateListener
            if (PermissionsUtils.isPermissionGranted(grantResults)) {
                storageLister?.onStoragePermissionOK()
            } else {
                storageLister?.onStoragePermissionFailed()
            }
        }

        if (requestCode == PermissionRequestCode.POSITIONING_REQ_CODE) {
            val positionListener = listener as? PositioningStateListener
            if (PermissionsUtils.isPermissionGranted(grantResults)) {
                positionListener?.onLocationPermissionOK()
            } else {
                positionListener?.onLocationPermissionFailed()
            }
        }

    }
}
