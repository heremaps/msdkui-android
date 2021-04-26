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

package com.here.msdkuiapp.routing

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.widget.TextView
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BasePermissionActivity
import com.here.msdkuiapp.common.mapselection.WaypointSelectionFragment
import com.here.msdkuiapp.map.MapFragmentWrapper
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
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
    private lateinit var mockManeuverListFragment: RouteManeuverListFragment

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
        coordinator = RoutingCoordinator(mockContext, mockFragmentManager)
    }

    @Test
    fun testIfStartSuccessfulPanelShouldBeAdded() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(null)

        coordinator.start()

        val fragmentCapture = argumentCaptor<Fragment>()
        verify(mockFragmentTransaction).replace(eq(R.id.route_top_container), fragmentCapture.capture(), anyString())
        assertNotNull(fragmentCapture.value as? RoutePlannerFragment)

        val fragmentCapture2 = argumentCaptor<Fragment>()
        verify(mockFragmentTransaction).replace(eq(R.id.route_bottom_container), fragmentCapture2.capture(), anyString())
        assertNotNull(fragmentCapture2.value as? RoutingInstructionsFragment)
    }

    @Test
    fun testOnEntryClicked() {
        val mockWaypointEntry = mock(WaypointEntry::class.java)
        `when`(mockFragmentManager.findFragmentById(R.id.route_mid_container)).thenReturn(mockWaypointSelectionFragment)

        coordinator.onEntryClicked(0, mockWaypointEntry)
        verify(mockWaypointSelectionFragment).updatePosition(0, mockWaypointEntry)
    }

    @Test
    fun testOnWaypointSelectionCancelled() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockRoutePlannerFragment)
        coordinator.onWaypointSelectionCancelled(null, null)
        verify(mockRoutePlannerFragment).waypointSelectionCancelled(null)
    }

    @Test
    fun testOnWaypointSelected() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockRoutePlannerFragment)
        val mockWaypointEntry = mock(WaypointEntry::class.java)

        coordinator.onWaypointSelected(0, mockWaypointEntry)
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

        // test of startManeuver(route: Route, isTraffic: Boolean) that is called by
        // onItemSelected(index: Int?, item: RouteDescriptionItem)
        assertFalse(mockRoutePlannerFragment.userVisibleHint)
        assertFalse(mockFragment.userVisibleHint)
        verify(mockManeuverListFragment).setRoute(mockRouteDescriptionItem.route, mockRouteDescriptionItem.isTrafficEnabled)
    }

    @Test
    fun testRenderRoute() {
        val mockGeoCoordinate = mock(GeoCoordinate::class.java)
        val mockRoute = mock(Route::class.java)
        `when`(mockFragmentManager.findFragmentById(R.id.route_mid_container)).thenReturn(mockManeuverListFragment)
        coordinator.renderRoute(mockRoute)
        verify(mockManeuverListFragment).renderRoute(mockRoute)
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
        verify(mockSubOptionPanelFragment).routeOptions = mockRouteOptions
        verify(mockSubOptionPanelFragment).type = mockPanels
    }

    @Test
    fun testTrafficChanged() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockRoutePlannerFragment)
        var trafficMode = Route.TrafficPenaltyMode.DISABLED
        coordinator.trafficChanged(trafficMode)
        verify(mockRoutePlannerFragment).trafficMode = trafficMode
    }

    @Test
    fun testOnBackPressedCombination1() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockRoutePlannerFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_mid_container)).thenReturn(mockOptionPanelFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_bottom_container)).thenReturn(null)

        coordinator.onBackPressed()

        verify(mockOptionPanelFragment).updateOptions()
    }

    @Test
    fun testOnBackPressedCombination2() {
        `when`(mockFragmentManager.findFragmentById(R.id.route_top_container)).thenReturn(mockRoutePlannerFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_mid_container)).thenReturn(mockManeuverListFragment)
        `when`(mockFragmentManager.findFragmentById(R.id.route_bottom_container)).thenReturn(mockRouteDescriptionListFragment)

        `when`(mockRouteDescriptionListFragment.isVisible).thenReturn(true)

        coordinator.onBackPressed()

        verify(mockRoutePlannerFragment).reset()
        verify(mockFragmentTransaction).remove(mockRouteDescriptionListFragment)
        verify(mockRoutePlannerFragment).userVisibleHint = true
        verify(mockRouteDescriptionListFragment).userVisibleHint = true
    }

    @Test
    fun testRoutingFailedBehaviour() {
        val errReason = "reason"
        `when`(mockFragmentManager.findFragmentById(R.id.route_bottom_container)).thenReturn(mockRouteDescriptionListFragment)
        val mockTextView = mock(TextView::class.java)
        `when`(mockContext.findViewById<TextView>(anyInt())).thenReturn(mockTextView)
        coordinator.onRoutingFailed("reason")
        verify(mockRouteDescriptionListFragment).updateRoutes(anySafe())
        verify(mockTextView).text = eq(errReason)
    }
}