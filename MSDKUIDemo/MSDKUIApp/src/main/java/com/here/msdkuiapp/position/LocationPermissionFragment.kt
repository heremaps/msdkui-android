/*
 * Copyright (C) 2017-2019 HERE Europe B.V.
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

import android.support.v4.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import com.here.msdkuiapp.GuidanceContracts
import com.here.msdkuiapp.base.BaseContract
import com.here.msdkuiapp.common.PermissionsUtils
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions


/**
 * Fragment class to be added where you need permission for location & want to ensure location services are enabled.
 * In case user denied the permission, the current activity will be finished.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class LocationPermissionFragment : Fragment(), BaseContract<GuidanceContracts.GuidanceWaypointSelection> {

    var presenter = LocationPermissionPresenter()

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = LocationPermissionFragment()
    }

    private val gpsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.action?.run {
               if(this.matches(LocationManager.PROVIDERS_CHANGED_ACTION.toRegex()))  {
                   presenter.start()
               }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.run {
            onAttach(activity!!, this@LocationPermissionFragment)
            coordinatorListener = activity as? Listener
            activity!!.registerReceiver(gpsReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity!!.unregisterReceiver(gpsReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)
        LocationServiceUtils.onActivityResult(requestCode, resultCode, presenter)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtils.onRequestPermissionsResult(requestCode, grantResults, presenter)
    }

    /**
     * Callback to notify location is ready to use.
     */
    interface Listener {

        /**
         * Indicates location permission & services are ok and ready to use now.
         */
        fun onLocationReady()
    }
}