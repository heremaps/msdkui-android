/*
 * Copyright (C) 2017-2020 HERE Europe B.V.
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

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.PositioningManager
import com.here.android.mpa.guidance.NavigationManager
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BasePermissionActivity
import com.here.msdkuiapp.common.routepreview.RoutePreviewFragment
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import com.here.msdkuiapp.isLocationOk
import com.here.msdkuiapp.map.MapFragmentWrapper
import com.here.msdkuiapp.position.AppPositioningManager
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Test for [GuidanceRouteSelectionCoordinator].
 */
class GuidanceRouteSelectionCoordinatorTest : BaseTest() {

    private lateinit var selectionCoordinator: GuidanceRouteSelectionCoordinator

    @Mock
    private lateinit var mockFragmentTransaction: FragmentTransaction

    @Mock
    private lateinit var mockFragmentManager: FragmentManager

    @Mock
    private lateinit var mockPositioningManager: AppPositioningManager

    @Mock
    private lateinit var mockMapFragment: MapFragmentWrapper

    @Mock
    private lateinit var mockContext: BasePermissionActivity

    @Mock
    private lateinit var mockMap: Map

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(mockFragmentManager.beginTransaction()).thenReturn(mockFragmentTransaction)
        navigationManager = mock(NavigationManager::class.java)
        appPositioningManager = mockPositioningManager
        selectionCoordinator = GuidanceRouteSelectionCoordinator(mockContext, mockFragmentManager).apply {
            mapFragment = mockMapFragment
            `when`(mapFragment!!.map).thenReturn(mockMap)
        }
    }

    @Test
    fun testIfStartSuccessfulPanelShouldBeAdded() {
        selectionCoordinator.start()
        val fragmentCapture = argumentCaptor<Fragment>()
        verify(mockFragmentTransaction).replace(ArgumentMatchers.anyInt(), fragmentCapture.capture(), anyString())
        assertNotNull(fragmentCapture.value as? GuidanceWaypointSelectionFragment)
        val captor = argumentCaptor<() -> Unit>()
        verify(selectionCoordinator.mapFragment!!).start(captor.capture())
        `when`(mockContext.isLocationOk).thenReturn(true)
        captor.value.invoke()
        assertEquals(mockMapFragment.traffic, false)
    }

    @Test
    fun testPointSelectedOnMap() {
        val waypointSelectionFragment = mock(GuidanceWaypointSelectionFragment::class.java)
        `when`(mockFragmentManager.findFragmentById(anyInt())).thenReturn(waypointSelectionFragment)
        selectionCoordinator.onPointSelectedOnMap(mock(GeoCoordinate::class.java))
        verify(waypointSelectionFragment).updateCord(anySafe())
    }

    @Test
    fun testWaypointSelectionShouldStartRoutePreview() {
        val entry = mock(WaypointEntry::class.java)
        selectionCoordinator.onWaypointSelected(entry)
        // map should not be touchable now
        verify(selectionCoordinator.mapFragment!!).onTouch(false)
        val fragmentCapture = argumentCaptor<Fragment>()
        verify(mockFragmentTransaction).replace(ArgumentMatchers.anyInt(), fragmentCapture.capture(), anyString())
        assertNotNull(fragmentCapture.value as? RoutePreviewFragment)
    }

    @Test
    fun testRenderRoute() {
        selectionCoordinator.renderRoute(mock(Route::class.java, RETURNS_DEEP_STUBS))
        verify(selectionCoordinator.mapFragment!!).
                renderAndZoomTo(anySafe(), ArgumentMatchers.eq(false))
    }

    @Test
    fun testOnPositionAvailable() {
        selectionCoordinator.onPositionAvailable()
        `when`(mockPositioningManager.isValidPosition).thenReturn(true)

        verifyOnPositionAvailable()
    }

    @Test
    fun testOnBackPressed() {
        `when`(mockFragmentManager.findFragmentById(R.id.guidance_selection_bottom_container))
                .thenReturn(mock(Fragment::class.java))

        // required for verifyOnPositionAvailable()
        `when`(mockPositioningManager.isValidPosition).thenReturn(true)

        selectionCoordinator.onBackPressed()

        verify(mockFragmentTransaction).replace(eq(R.id.guidance_selection_top_container), anySafe(), anyString())
        verify(mockMapFragment).clearMap()
        verifyOnPositionAvailable()
    }

    private fun verifyOnPositionAvailable() {
        verify(mockMapFragment).showPositionIndicator(ArgumentMatchers.anyBoolean())
        verify(mockMap).setCenter(anySafe(), anySafe())
        verify(mockMapFragment).onTouch(ArgumentMatchers.anyBoolean(), anySafe())
    }
}