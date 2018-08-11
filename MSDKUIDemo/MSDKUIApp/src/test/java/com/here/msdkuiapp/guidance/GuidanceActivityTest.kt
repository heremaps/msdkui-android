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

package com.here.msdkuiapp.guidance

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import com.here.msdkuiapp.common.Constant.ROUTE_KEY
import com.here.msdkuiapp.landing.LandingActivity
import com.here.testutils.BaseTest
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController
import org.robolectric.util.ReflectionHelpers

/**
 * Tests for [GuidanceActivity].
 */
class GuidanceActivityTest : BaseTest() {

    private lateinit var activityController: ActivityController<GuidanceActivity>

    @Mock
    private lateinit var mockCoordinator: GuidanceCoordinator

    @Before
    override fun setUp() {
        MockitoAnnotations.initMocks(this)
        val bundle = Bundle()
        bundle.putByteArray(ROUTE_KEY, byteArrayOf())
        activityController = Robolectric.buildActivity(GuidanceActivity::class.java, Intent().putExtras(bundle))
    }

    @Test
    fun testUIWhenLocationIsOk() {
        with(activityController) {
            get().guidanceCoordinator = mockCoordinator
            assertNotNull(get().guidanceCoordinator)
        }
        val locationManager = activityController.get().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val shadowLocationManager = shadowOf(locationManager)
        shadowLocationManager.setProviderEnabled(LocationManager.GPS_PROVIDER, true)
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 19)
        Assert.assertTrue(activityController.get().isLocationOk())
        activityController.create().get()
        verify(mockCoordinator).start()
    }

    @Test
    fun testUIWhenLocationIsNotOk() {
        with(activityController) {
            get().guidanceCoordinator = mockCoordinator
            assertNotNull(get().guidanceCoordinator)
            create()
        }

        val startedIntent = shadowOf(activityController.get()).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(LandingActivity::class.java, shadowIntent.intentClass)
    }


    @Test
    fun testDestroy() {
        with(activityController) {
            get().guidanceCoordinator = mockCoordinator
            assertNotNull(get().guidanceCoordinator)
            create()
        }

        activityController.destroy()
        verify(mockCoordinator).destroy()
    }

    @Test
    fun testSetAndGetCoordinator() {
        with(activityController) {
            get().coordinator = mockCoordinator
            assertNotNull(get().coordinator)
        }
    }

    @Test
    fun testBackPressWhenCoordinator() {
        `when`(mockCoordinator.onBackPressed()).thenReturn(false)
        val activity = activityController.get()
        with(activity) {
            guidanceCoordinator = mockCoordinator
            onBackPressed()
        }
        verify(mockCoordinator).onBackPressed()
        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(LandingActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun testBackPressWhenNoCoordinator() {
        `when`(mockCoordinator.onBackPressed()).thenReturn(false)
        val activity = activityController.get()
        activity.onBackPressed()
        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(LandingActivity::class.java, shadowIntent.intentClass)
    }
}