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

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Build
import com.here.msdkuiapp.R
import com.here.msdkuiapp.position.LocationPermissionFragment
import com.here.msdkuiapp.position.LocationPermissionPresenter
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.Assert
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController
import org.robolectric.util.ReflectionHelpers


class BasePermissionActivityTest : BaseTest() {

    class BaseBasePermissionActivityImpl : BasePermissionActivity() {
        override fun onLocationReady() {
        }
    }

    @Mock
    private lateinit var mockCoordinator: BaseFragmentCoordinator

    @Mock
    private lateinit var mockLocationPermissionFragment: LocationPermissionFragment

    @Mock
    private lateinit var mockLockationPermissionPresenter: LocationPermissionPresenter

    private lateinit var activityController: ActivityController<BaseBasePermissionActivityImpl>

    @Before
    public override fun setUp() {
        MockitoAnnotations.initMocks(this)
        activityController = Robolectric.buildActivity(BaseBasePermissionActivityImpl::class.java)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testBehaviourIfViewNotPresent() {
        val activity = activityController.create().get()
        assertNotNull(activity.baseFragmentCoordinator)
        activity.baseFragmentCoordinator = mockCoordinator
        activityController.start()
    }

    @Test
    fun testBehaviourIfViewIsPresent() {
        val activity = activityController.create().get()
        activity.setContentView(R.layout.activity_guidance_map_selection)
        activity.baseFragmentCoordinator = mockCoordinator
        activityController.start()
        // permission fragment should be added.
        verify(mockCoordinator).addFragment(anyInt(), anySafe<Class<Fragment>>(), anyBoolean())
    }

    @Test
    fun testOnActivityResult() {
        val activity = activityController.create().get()
        `when`(mockCoordinator.getFragment(anyInt())).thenReturn(mockLocationPermissionFragment)
        activity.baseFragmentCoordinator = mockCoordinator
        activity.onActivityResult(1, 1, Intent())
        verify(mockLocationPermissionFragment).onActivityResult(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), anySafe())
    }

    @Test
    fun testOnRequestPermissionsResult() {
        val activity = activityController.create().get()
        `when`(mockCoordinator.getFragment(anyInt())).thenReturn(mockLocationPermissionFragment)
        activity.baseFragmentCoordinator = mockCoordinator
        activity.onRequestPermissionsResult(1, arrayOf<String>(), IntArray(1))
        verify(mockLocationPermissionFragment).onRequestPermissionsResult(anyInt(), anySafe(), anySafe())
    }

    @Test
    fun testIsLocationOkReturnsDefaultFalse() {
        val activity = activityController.create().get()
        activity.baseFragmentCoordinator = mockCoordinator
        Assert.assertEquals(activity.isLocationOk(), false)

        `when`(mockCoordinator.getFragment(anyInt())).thenReturn(mockLocationPermissionFragment)
        Assert.assertEquals(activity.isLocationOk(), false)
    }

    @Test
    fun testIsLocationOk() {
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 19)
        val activity = activityController.create().get()
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val shadowLocationManager = shadowOf(locationManager)
        shadowLocationManager.setProviderEnabled(GPS_PROVIDER, false)
        assertFalse(activity.isLocationOk())
        shadowLocationManager.setProviderEnabled(GPS_PROVIDER, true)
        assertTrue(activity.isLocationOk())
    }
}