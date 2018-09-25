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

package com.here.msdkuiapp.routing

import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkui.routing.ManeuverDescriptionItem
import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BasePermissionActivity
import com.here.msdkuiapp.common.mapselection.WaypointSelectionFragment
import com.here.msdkuiapp.map.MapFragmentWrapper
import com.here.testutils.BaseTest
import com.here.testutils.argumentCaptor
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Test for [RoutingCoordinator].
 */
class RoutingCoordinatorTest : BaseTest() {

    private lateinit var coordinator: RoutingCoordinator

    @Mock
    private lateinit var mockFragmentTransaction: FragmentTransaction

    @Mock
    private lateinit var mockFragmentManager: FragmentManager

    @Mock
    private lateinit var mockMapFragment: MapFragmentWrapper

    @Mock
    private lateinit var mockContext: BasePermissionActivity

    @Mock
    private lateinit var mockFragment: Fragment

    @Mock
    private lateinit var mockRoutePlannerFragment: RoutePlannerFragment

    @Mock
    private lateinit var mockWaypointSelectionFragment: WaypointSelectionFragment

    @Mock
    private lateinit var mockRouteDescriptionListFragment: RouteDescriptionListFragment

    @Mock
    private lateinit var mockManeuverListFragment: ManeuverListFragment

    @Mock
    private lateinit var mockOptionPanelFragment: OptionPanelFragment

    @Mock
    private lateinit var mockSubOptionPanelFragment: SubOptionPanelFragment

    @Mock
    private lateinit var mockRoutes: List<Route>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(mockFragmentManager.beginTransaction()).thenReturn(mockFragmentTransaction)
        `when`(mockFragmentManager.findFragmentById(R.id.mapfragment_wrapper)).thenReturn(mockMapFragment)
        coordinator = RoutingCoordinator(mockContext, mockFragmentManager).apply {
        }
    }

    @Test
    fun testIfStartSuccessfulPanelShouldBeAdded() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(null)

        coordinator.start()

        val fragmentCapture = argumentCaptor<Fragment>()
        verify(mockFragmentTransaction).replace(eq(R.id.route_top_container), fragmentCapture.capture(), anyString())
        assertNotNull(fragmentCapture.value as? RoutePlannerFragment)

        val captor = argumentCaptor<() -> Unit>()
        verify(mockMapFragment).start(captor.capture())

        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockRoutePlannerFragment)
        captor.value.invoke()
        verify(mockMapFragment).traffic = anyBoolean()
        verify(mockMapFragment).onTouch(false)
    }

    @Test
    fun testOnEntryClicked() {
        val listenerCaptor = argumentCaptor<MapFragmentWrapper.Listener>()
        val mockWaypointEntry = mock(WaypointEntry::class.java)
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockWaypointSelectionFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_bottom_container)).thenReturn(mockFragment)

        coordinator.onEntryClicked(0, mockWaypointEntry)
        assertFalse(mockFragment.userVisibleHint)
        verify(mockMapFragment).userVisibleHint = true
        verify(mockMapFragment).clearMap()
        verify(mockWaypointSelectionFragment).updatePosition(0, mockWaypointEntry)
        verify(mockMapFragment).onTouch(eq(true), listenerCaptor.capture())
        val mockGeoCoordinate = mock(GeoCoordinate::class.java)
        listenerCaptor.value.onPointSelectedOnMap(mockGeoCoordinate)
        verify(mockWaypointSelectionFragment).updateCord(mockGeoCoordinate)
    }

    @Test
    fun testOnWaypointSelected() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockRoutePlannerFragment)
        val mockWaypointEntry = mock(WaypointEntry::class.java)

        coordinator.onWaypointSelected(0, mockWaypointEntry)
        verify(mockMapFragment).onTouch(false)
        verify(mockRoutePlannerFragment).userVisibleHint = true
        verify(mockRoutePlannerFragment).updateWaypoint(0, mockWaypointEntry)
        verify(mockRoutePlannerFragment).calculateRoute()
    }

    @Test
    fun testOnRouteCalculated() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_bottom_container)).thenReturn(mockRouteDescriptionListFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_mid_container)).thenReturn(mockFragment)

        coordinator.onRouteCalculated(mockRoutes)
        verify(mockFragmentTransaction).remove(mockFragment)
        assertFalse(mockMapFragment.userVisibleHint)
        verify(mockRouteDescriptionListFragment).userVisibleHint = true
        verify(mockRouteDescriptionListFragment).updateRoutes(mockRoutes)
    }

    @Test
    fun testOnTitleChanged() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_bottom_container)).thenReturn(mockRouteDescriptionListFragment)

        coordinator.onTitleChange(false)
        verify(mockRouteDescriptionListFragment).updateTitle(false)
    }

    @Test
    fun testOnItemSelected() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_mid_container)).thenReturn(mockManeuverListFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_bottom_container)).thenReturn(mockFragment)
        val mockRouteDescriptionItem = mock(RouteDescriptionItem::class.java, RETURNS_DEEP_STUBS)

        coordinator.onItemSelected(0, mockRouteDescriptionItem)
        verify(mockMapFragment).renderRoute(mockRouteDescriptionItem.route)

        // test of startManeuver(route: Route, isTraffic: Boolean) that is called by
        // onItemSelected(index: Int?, item: RouteDescriptionItem)
        assertFalse(mockRoutePlannerFragment.userVisibleHint)
        assertFalse(mockFragment.userVisibleHint)
        verify(mockMapFragment).userVisibleHint = true
        verify(mockManeuverListFragment).updateRoute(mockRouteDescriptionItem.route, mockRouteDescriptionItem.isTrafficEnabled)
    }

    @Test
    fun testOnManeuverClicked() {
        val mockManeuverDescriptionItem = mock(ManeuverDescriptionItem::class.java, RETURNS_DEEP_STUBS)

        coordinator.onManeuverClicked(0, mockManeuverDescriptionItem)
        verify(mockMapFragment).zoomToBoundingBox(mockManeuverDescriptionItem.maneuver.boundingBox)
    }

    @Test
    fun testZoomToRoute() {
        coordinator.zoomToRoute(false)
        verify(mockMapFragment).zoomToRoute(false)
    }

    @Test
    fun testOnPointSelectedOnMap() {
        val mockGeoCoordinate = mock(GeoCoordinate::class.java)

        // test when top container is not WaypointSelectionFragment
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockRoutePlannerFragment)
        coordinator.onPointSelectedOnMap(mockGeoCoordinate)
        verify(mockWaypointSelectionFragment, never()).updateCord(mockGeoCoordinate)

        // test when top container is WaypointSelectionFragment
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockWaypointSelectionFragment)
        coordinator.onPointSelectedOnMap(mockGeoCoordinate)
        verify(mockWaypointSelectionFragment).updateCord(mockGeoCoordinate)
    }

    @Test
    fun testOnOptionPanelClicked() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockWaypointSelectionFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_mid_container)).thenReturn(mockOptionPanelFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_bottom_container)).thenReturn(mockFragment)
        val mockRouteOptions = mock(RouteOptions::class.java)
        val mockDynamicPenalty = mock(DynamicPenalty::class.java)

        coordinator.onOptionPanelClicked(mockRouteOptions, mockDynamicPenalty)
        assertFalse(mockWaypointSelectionFragment.userVisibleHint)
        assertFalse(mockFragment.userVisibleHint)
        assertFalse(mockMapFragment.userVisibleHint)
        verify(mockMapFragment).onTouch(false)
        verify(mockOptionPanelFragment).dynamicPenalty = mockDynamicPenalty
        verify(mockOptionPanelFragment).routeOptions = mockRouteOptions
        verify(mockOptionPanelFragment).listener = coordinator
    }

    @Test
    fun testOpenSubPanel() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_mid_container)).thenReturn(mockSubOptionPanelFragment)
        val mockRouteOptions = mock(RouteOptions::class.java)
        val mockPanels = mock(Panels::class.java)

        coordinator.openSubPanel(mockPanels, mockRouteOptions)
        verify(mockSubOptionPanelFragment).routeOptions  = mockRouteOptions
        verify(mockSubOptionPanelFragment).type = mockPanels
    }

    @Test
    fun testTrafficChanged() {
        coordinator.trafficChanged(Route.TrafficPenaltyMode.DISABLED)
        coordinator.trafficChanged(Route.TrafficPenaltyMode.AVOID_LONG_TERM_CLOSURES)
        verify(mockMapFragment, times(2)).traffic = false

        coordinator.trafficChanged(Route.TrafficPenaltyMode.OPTIMAL)
        verify(mockMapFragment).traffic = true
    }

    @Test
    fun testOnBackPressedCombination1() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockWaypointSelectionFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_mid_container)).thenReturn(mockOptionPanelFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_bottom_container)).thenReturn(null)

        coordinator.onBackPressed()

        verify(mockMapFragment).clearMap()
        verify(mockMapFragment).onTouch(false)
        verify(mockOptionPanelFragment).updateOptions()
        verify(mockWaypointSelectionFragment).userVisibleHint = true
        verify(mockMapFragment).userVisibleHint = true
    }

    @Test
    fun testOnBackPressedCombination2and3() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockRoutePlannerFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_mid_container)).thenReturn(mockManeuverListFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_bottom_container)).thenReturn(mockRouteDescriptionListFragment)

        `when`(mockRouteDescriptionListFragment.isVisible).thenReturn(true)

        coordinator.onBackPressed()

        verify(mockMapFragment).clearMap()
        verify(mockRoutePlannerFragment).reset()
        verify(mockFragmentTransaction).remove(mockRouteDescriptionListFragment)
        verify(mockMapFragment, times(2)).userVisibleHint = true
        verify(mockRoutePlannerFragment).userVisibleHint = true
        verify(mockRouteDescriptionListFragment).userVisibleHint = true

        `when`(mockFragmentManager.findFragmentById(R.id.route_mid_container)).thenReturn(mockOptionPanelFragment)
        coordinator.onBackPressed()
        verify(mockRoutePlannerFragment).calculateRoute()
    }
}