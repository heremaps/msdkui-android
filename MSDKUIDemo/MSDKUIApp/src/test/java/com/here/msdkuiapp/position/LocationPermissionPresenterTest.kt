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

import android.content.Context
import android.location.LocationManager
import android.os.Build
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.util.ReflectionHelpers

/**
 * Tests for [LocationPermissionPresenter].
 */
class LocationPermissionPresenterTest : BaseTest() {

    private lateinit var presenter: LocationPermissionPresenter

    @Mock
    private lateinit var locationPermissionFragmentListener: LocationPermissionFragment.Listener

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockContext: Context

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockLocationManager: LocationManager

    @Before
    fun setup() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        presenter = LocationPermissionPresenter()
        presenter.apply {
            context = mockContext
            coordinatorListener = locationPermissionFragmentListener
        }
        assertNotNull(presenter.coordinatorListener)
    }

    @Test
    fun testOnServiceOk() {
        mockContextAndLocationManagerBeforeOnLocationPermissionOK()
        presenter.onServiceOk()
        verify(locationPermissionFragmentListener).onLocationReady()
    }

    @Test
    fun testOnServiceFailed() {
        presenter.onServiceFailed()
        verify(mockContext).startActivity(anySafe())
    }

    @Test
    fun testOnLocationPermissionOK() {
        mockContextAndLocationManagerBeforeOnLocationPermissionOK()
        presenter.onLocationPermissionOK()
        verify(locationPermissionFragmentListener).onLocationReady()
    }

    @Test
    fun testOnLocationPermissionFailed() {
        presenter.onLocationPermissionFailed()
        verify(mockContext).startActivity(anySafe())
    }

    @Test
    fun testOnStart() {
        mockContextAndLocationManagerBeforeOnLocationPermissionOK()
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 19)
        presenter.start()

        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 21)
        presenter.start()

        verify(locationPermissionFragmentListener, times(2)).onLocationReady()
    }

    private fun mockContextAndLocationManagerBeforeOnLocationPermissionOK() {
        `when`(mockContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(mockLocationManager)
        `when`(mockLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true)
    }
}