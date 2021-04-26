/*
 * Copyright (C) 2017-2021 HERE Europe B.V.
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.here.android.mpa.common.PositioningManager
import com.here.android.mpa.guidance.NavigationManager
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.routing.RouteResult
import com.here.android.mpa.routing.RoutingError
import com.here.msdkuiapp.MSDKUIApplication
import com.here.msdkuiapp.base.BasePermissionActivity
import com.here.msdkuiapp.common.Provider
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import com.here.msdkuiapp.map.MapFragmentWrapper
import com.here.msdkuiapp.position.AppPositioningManager
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import junit.framework.Assert.*
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
    private lateinit var mockContext: BasePermissionActivity

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
    private lateinit var mockMapFragment: MapFragmentWrapper

    @Mock
    private lateinit var mockMSDKUIApplication: MSDKUIApplication

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(mockFragmentManager.beginTransaction()).thenReturn(mockFragmentTransaction)
        navigationManager = mockNavigationManager
        appPositioningManager = mock(AppPositioningManager::class.java)
        `when`(mockContext.applicationContext).thenReturn(mockMSDKUIApplication)
        guidanceCoordinator = GuidanceCoordinator(mockContext, mockFragmentManager).apply {
            mapFragment = mockMapFragment
            `when`(mapFragment!!.map).thenReturn(mockMap)
            val mockRoute = mockRoute()
            `when`(mockMSDKUIApplication.route).thenReturn(mockRoute)
            isSimulation = false
            provider = mockProvider
        }
    }

    @Test
    fun testRerouteListenerAndTrafficRerouteListener() {
        val mockRoute = mockRoute()
        val mockRerouteResult = mock(RouteResult::class.java)

        `when`(mockRerouteResult.route).thenReturn(mockRoute)
        guidanceCoordinator.rerouteListener.onRerouteEnd(mockRerouteResult, mock(RoutingError::class.java))
        assertEquals(guidanceCoordinator.route, mockRerouteResult.route)

        guidanceCoordinator.trafficRerouteListener.onTrafficRerouted(mockRerouteResult)
        assertEquals(guidanceCoordinator.route, mockRerouteResult.route)

        verify(mockMapFragment, times(2)).renderRoute(anySafe(), ArgumentMatchers.anyBoolean())
    }

    @Test
    fun testIfStartSuccessfulPanelShouldBAdded() {
        startCoordinator()
        verify(mockNavigationManager).addRerouteListener(anySafe())
        verify(mockNavigationManager).addTrafficRerouteListener(anySafe())

        verify(mockMapFragment).renderRoute(anySafe(), ArgumentMatchers.anyBoolean())
        verify(mockMap).setCenter(anySafe(), anySafe())

        val fragmentCapture = argumentCaptor<Fragment>()
        verify(mockFragmentTransaction, times(6)).replace(anyInt(), fragmentCapture.capture(), anyString())
        val fragment = fragmentCapture.allValues[0] as? GuidanceManeuverFragment
        assertNotNull(fragment)

        with(mockNavigationManager) {
            setMap(anySafe())
            startNavigation(anySafe())
        }
    }

    @Test
    fun testGuidanceFinishedSetProperly() {
        assertFalse(guidanceCoordinator.didGuidanceFinished)
        guidanceCoordinator.navigationManagerEventListener.onEnded(NavigationManager.NavigationMode.NAVIGATION)
        assertTrue(guidanceCoordinator.didGuidanceFinished)
    }

    @Test
    fun testWhenContextIsLocationOkEqualToFalse() {
        startCoordinator(false)
        verify(mockContext).startActivity(anySafe())
    }

    @Test
    fun testIsRouteValidWhenRouteIsNull() {
        `when`(mockMSDKUIApplication.route).thenReturn(null)
        startCoordinator()
        verify(mockContext).startActivity(anySafe())
    }

    @Test
    fun testStartNavigation() {
        startCoordinator()
        verify(mockNavigationManager).startNavigation(anySafe())
    }

    @Test
    fun testStartSimulation() {
        guidanceCoordinator.isSimulation = true
        startCoordinator()
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
        startCoordinator()
        verify(mockNavigationManager).addRerouteListener(any())
        verify(mockNavigationManager).addTrafficRerouteListener(any())
        verify(mockNavigationManager).addNavigationManagerEventListener(any())
    }

    @Test
    fun testDestroy() {
        guidanceCoordinator.destroy()
        verify(mockNavigationManager).removeRerouteListener(anySafe())
        verify(mockNavigationManager).removeTrafficRerouteListener(anySafe())
        verify(mockNavigationManager).removeNavigationManagerEventListener(anySafe())
    }

    private fun startCoordinator(isLocationOk: Boolean = true) {
        `when`(mockContext.isLocationOk()).thenReturn(isLocationOk)
        guidanceCoordinator.start()
        val captor = argumentCaptor<() -> Unit>()
        verify(guidanceCoordinator.mapFragment!!).start(captor.capture())
        captor.value.invoke()   // map engine start is successful.
    }
}