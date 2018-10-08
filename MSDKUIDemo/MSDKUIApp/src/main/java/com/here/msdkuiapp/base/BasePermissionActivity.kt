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

package com.here.msdkuiapp.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.here.msdkuiapp.R
import com.here.msdkuiapp.common.PermissionsUtils
import com.here.msdkuiapp.position.LocationPermissionFragment
import com.here.msdkuiapp.position.LocationServiceUtils
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Activity class that can be extended if location permission needs to be check on every activity on start. the child
 * activity will receive onLocationReady callback when location service & location permission both are ok.
 *
 * Please also ensure that your activity layout contain a empty view with id permissionId.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
abstract class BasePermissionActivity : BaseActivity(), LocationPermissionFragment.Listener {

    lateinit var baseFragmentCoordinator: BaseFragmentCoordinator

    private val locationPermissionFragment: LocationPermissionFragment? = null
        get() = field
                ?: baseFragmentCoordinator.getFragment(R.id.permissionId) as? LocationPermissionFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseFragmentCoordinator = BaseFragmentCoordinator(fragmentManager)
    }

    override fun onStart() {
        super.onStart()
        addPermissionView()
    }

    private fun addPermissionView() {
        findViewById<View>(R.id.permissionId)
                ?: throw IllegalArgumentException("Please add a view with id permissionId in your main layout resource")
        with(baseFragmentCoordinator) {
            getFragment(R.id.permissionId) ?: addFragment(R.id.permissionId,
                    LocationPermissionFragment::class.java)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        locationPermissionFragment?.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionFragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Determines if locations permission & service are ok.
     *
     * @return true if location permission & service are ok, false otherwise.
     */
    fun isLocationOk(): Boolean {
        return LocationServiceUtils.isServiceOk(this) && PermissionsUtils.isLocationPermissionOk(this)
    }

    abstract override fun onLocationReady()
}