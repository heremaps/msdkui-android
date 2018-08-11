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

import MockUtils.mockRoute
import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import com.here.android.mpa.common.PositioningManager
import com.here.android.mpa.guidance.NavigationManager
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteResult
import com.here.msdkuiapp.base.BasePermissionActivity
import com.here.msdkuiapp.common.Provider
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.msdkuiapp.guidance.SingletonHelper.positioningManager
import com.here.msdkuiapp.isLocationOk
import com.here.msdkuiapp.map.MapFragmentWrapper
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import com.here.testutils.captureSafe
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


/**
 * Test for [GuidanceCoordinator].
 */
class GuidanceCoordinatorTest : BaseTest() {

    private lateinit var guidanceCoordinator: GuidanceCoordinator

    @Mock
    private lateinit var mockContext : BasePermissionActivity

    @Mock
    private lateinit var mockFragmentTransaction: FragmentTransaction

    @Mock
    private lateinit var mockFragmentManager: FragmentManager

    @Mock
    private lateinit var mockMap: Map

    @Mock
    private lateinit var mockNavigationManager: NavigationManager

    @Mock
    private lateinit var mockProvider: Provider

    @Mock
    private lateinit var mockDeserializationResult: Route.DeserializationResult

    @Mock
    private lateinit var mockMapFragment: MapFragmentWrapper

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(mockFragmentManager.beginTransaction()).thenReturn(mockFragmentTransaction)
        navigationManager = mockNavigationManager
        positioningManager = mock(PositioningManager::class.java)
        guidanceCoordinator = GuidanceCoordinator(mockContext, mockFragmentManager).apply {
            mapFragment = mockMapFragment
            `when`(mapFragment!!.map).thenReturn(mockMap)
            route = mockRoute()
            isSimulation = false
            provider = mockProvider
        }
        mockDeserializationResult = mock(Route.DeserializationResult::class.java)
        mockDeserializationResult.error = Route.SerializerError.NONE
        mockDeserializationResult.route = mockRoute()
    }

    @Test
    fun testRerouteListenerAndTrafficRerouteListener() {
        val mockRoute = mockRoute()
        val mockRerouteResult = mock(RouteResult::class.java)

        `when`(mockRerouteResult.route).thenReturn(mockRoute)
        guidanceCoordinator.rerouteListener.onRerouteEnd(mockRerouteResult)
        assertEquals(guidanceCoordinator.route, mockRerouteResult.route)

        guidanceCoordinator.trafficRerouteListener.onTrafficRerouted(mockRerouteResult)
        assertEquals(guidanceCoordinator.route, mockRerouteResult.route)

        verify(mockMapFragment, times(2)).renderRoute(anySafe(), ArgumentMatchers.anyBoolean())
    }

    @Test
    fun testIfStartSuccessfulPanelShouldBAdded() {
        startCoordinatorAndDeserializeRoute()
        verify(mockNavigationManager).addRerouteListener(anySafe())
        verify(mockNavigationManager).addTrafficRerouteListener(anySafe())

        verify(mockMapFragment).renderRoute(anySafe(), ArgumentMatchers.anyBoolean())
        verify(mockMap).setCenter(anySafe(), anySafe())

        val fragmentCapture = argumentCaptor<Fragment>()
        verify(mockFragmentTransaction, times(2)).replace(anyInt(), fragmentCapture.capture(), anyString())
        val fragment = fragmentCapture.allValues[0] as? GuidanceManeuverPanelFragment
        assertNotNull(fragment)
        assertEquals(fragment?.route, mockDeserializationResult.route)

        with(mockNavigationManager) {
            setMap(anySafe())
            startNavigation(anySafe())
        }
    }

    @Test
    fun testWhenContextIsLocationOkEqualToFalse() {
        startCoordinatorAndDeserializeRoute(false)
        verify(mockContext).startActivity(anySafe())
    }

    @Test
    fun testIsRouteValidWhenRouteIsNull() {
        mockDeserializationResult.route = null
        startCoordinatorAndDeserializeRoute()
        verify(mockContext).startActivity(anySafe())
    }

    @Test
    fun testIsRouteValidWhenRouteStartIsNull() {
        mockDeserializationResult.route = mockRoute(mockStart = null)
        startCoordinatorAndDeserializeRoute()
        verify(mockContext).startActivity(anySafe())
    }

    @Test
    fun testIsRouteValidWhenRouteStartThrowsException() {
        val route = mockRoute()
        mockDeserializationResult.route = route
        `when`(route.start).thenThrow(NullPointerException::class.java)
        startCoordinatorAndDeserializeRoute()
        verify(mockContext).startActivity(anySafe())
    }

    @Test
    fun testStartNavigation() {
        startCoordinatorAndDeserializeRoute()
        verify(mockNavigationManager).startNavigation(anySafe())
    }

    @Test
    fun testStartSimulation() {
        guidanceCoordinator.isSimulation = true
        startCoordinatorAndDeserializeRoute()
        verify(mockNavigationManager).simulate(anySafe(), anyLong())
    }

    @Test
    fun testBackPress() {
        guidanceCoordinator.onBackPressed()
        // verify it stop navigation manager
        verify(navigationManager!!).stop()
    }

    @Test
    fun testListenersRegistration() {
        startCoordinatorAndDeserializeRoute()
        verify(mockNavigationManager).addRerouteListener(any())
        verify(mockNavigationManager).addTrafficRerouteListener(any())
    }

    @Test
    fun testDestroy() {
        guidanceCoordinator.destroy()
        verify(mockNavigationManager).removeRerouteListener(anySafe())
        verify(mockNavigationManager).removeTrafficRerouteListener(anySafe())
    }

    private fun startCoordinatorAndDeserializeRoute(isLocationOk: Boolean = true) {
        guidanceCoordinator.start()
        val captor = argumentCaptor<() -> Unit>()
        verify(guidanceCoordinator.mapFragment!!).start(captor.capture())
        captor.value.invoke()   // map engine start is successful.
        val deserializeCallbackCaptor = argumentCaptor<Route.DeserializationCallback>()
        verify(mockProvider).provideDeserialize(anySafe(), captureSafe(deserializeCallbackCaptor))
        `when`(mockContext.isLocationOk).thenReturn(isLocationOk)
        deserializeCallbackCaptor.value.onDeserializationComplete(mockDeserializationResult)
    }
}