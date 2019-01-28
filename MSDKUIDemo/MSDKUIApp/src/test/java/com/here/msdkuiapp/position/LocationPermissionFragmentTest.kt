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

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import com.here.msdkuiapp.common.PermissionRequestCode
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.TestCase.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Tests for [LocationPermissionFragment].
 */
class LocationPermissionFragmentTest : BaseTest() {

    private lateinit var locationPermissionFragment: LocationPermissionFragment

    @Mock
    private lateinit var presenter: LocationPermissionPresenter

    @Before
    fun setup() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        locationPermissionFragment = LocationPermissionFragment.newInstance()
    }

    @Test
    fun testInitWithPresenterMocked() {
        locationPermissionFragment.presenter = presenter
        addFrag(locationPermissionFragment, LocationPermissionFragment::class.java.name)
        with(verify(presenter)) {
            onAttach(anySafe(), anySafe())
            assertNull(coordinatorListener)
            start()
        }
    }

    @Test
    fun testProviderChangeListenerRegistration() {
        locationPermissionFragment.presenter = presenter
        addFrag(locationPermissionFragment, LocationPermissionFragment::class.java.name)
        fragmentActivity?.sendBroadcast(Intent().setAction(LocationManager.PROVIDERS_CHANGED_ACTION))
        verify(presenter, times(2)).start()
    }

    @Test
    fun testProviderChangeListenerUnregistration() {
        locationPermissionFragment.presenter = presenter
        addFrag(locationPermissionFragment, LocationPermissionFragment::class.java.name)
        locationPermissionFragment.onDestroyView()
        fragmentActivity?.sendBroadcast(Intent().setAction(LocationManager.PROVIDERS_CHANGED_ACTION))
        verify(presenter, times(1)).start()
    }


    @Test
    fun testOnActivityResult() {
        locationPermissionFragment.presenter = presenter
        locationPermissionFragment.onActivityResult(PermissionRequestCode.LOCATION_SERVICE_REQ_CODE,
                Activity.RESULT_CANCELED, Intent())
        verify(presenter).onServiceFailed()
        locationPermissionFragment.onActivityResult(PermissionRequestCode.LOCATION_SERVICE_REQ_CODE,
                1, Intent())
        verify(presenter).onServiceOk()
    }

    @Test
    fun testOnRequestPermissionsResult() {
        locationPermissionFragment.presenter = presenter
        addFrag(locationPermissionFragment, LocationPermissionFragment::class.java.name)
        locationPermissionFragment.onRequestPermissionsResult(PermissionRequestCode.POSITIONING_REQ_CODE,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), intArrayOf(PackageManager.PERMISSION_GRANTED))
        verify(presenter).onLocationPermissionOK()
        locationPermissionFragment.onRequestPermissionsResult(PermissionRequestCode.POSITIONING_REQ_CODE,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), intArrayOf())
        verify(presenter).onLocationPermissionFailed()
    }
}