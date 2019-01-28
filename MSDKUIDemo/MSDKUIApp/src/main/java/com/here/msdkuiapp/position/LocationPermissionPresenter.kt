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

import android.content.Intent
import com.google.android.gms.location.LocationServices
import com.here.msdkuiapp.GuidanceContracts
import com.here.msdkuiapp.base.BasePresenter
import com.here.msdkuiapp.common.PermissionsUtils
import com.here.msdkuiapp.landing.LandingActivity

/**
 * Class to handle logic for [LocationPermissionFragment].
 */
class LocationPermissionPresenter : BasePresenter<GuidanceContracts.GuidanceWaypointSelection>(),
        PermissionsUtils.PositioningStateListener,
        LocationServiceUtils.LocationServiceStateListener {

    private val state = State()
    var coordinatorListener: LocationPermissionFragment.Listener? = null

    override fun onServiceOk() {
        state.isInProgress = false
        coordinatorListener?.onLocationReady()
    }

    override fun onServiceFailed() {
        goToHome()
    }

    private fun goToHome() {
        state.isInProgress = false
        context!!.startActivity(Intent(context, LandingActivity::class.java))
    }

    override fun onLocationPermissionOK() {
        val client = LocationServices.getSettingsClient(context!!)
        LocationServiceUtils.checkLocationServices(context!!, client, this)
    }

    override fun onLocationPermissionFailed() {
        goToHome()
    }

    /*
    * Start checking the status of the position permission & location services.
     */
    fun start() {
        if (state.isInProgress.not()) {
            state.isInProgress = true
            PermissionsUtils.checkLocationPermission(context!!, this)
        }
    }

    private class State {
        var isInProgress = false
    }
}