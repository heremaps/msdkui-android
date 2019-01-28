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

package com.here.msdkuiapp.routing

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkui.routing.WaypointList
import com.here.msdkuiapp.R
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.android.synthetic.main.route_planner.view.option_panel
import kotlinx.android.synthetic.main.route_planner.view.swap_list
import kotlinx.android.synthetic.main.route_planner.view.transport_panel
import kotlinx.android.synthetic.main.route_planner.view.travel_time_panel
import kotlinx.android.synthetic.main.route_planner.view.waypoint_add
import kotlinx.android.synthetic.main.route_planner.view.waypoint_list
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

/**
 * Tests for [RouteDescriptionListFragment].
 */
class RoutePlannerFragmentTest : BaseTest() {

    private lateinit var fragment: RoutePlannerFragment

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var presenter: RoutePlannerPresenter

    @Mock
    private lateinit var waypointListenerMock: WaypointList.Listener

    @Before
    public override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        fragment = RoutePlannerFragment.newInstance()
        fragment.presenter = presenter
        `when`(presenter.waypointListener).thenReturn(waypointListenerMock)
        addFrag(fragment)
    }

    @Test
    fun testPresenterInit() {
        fragment = RoutePlannerFragment.newInstance()
        assertNotNull(fragment.presenter)
    }

    @Test
    fun testSetAndGetTrafficMode() {
        fragment.trafficMode = Route.TrafficPenaltyMode.DISABLED
        verify(presenter).trafficMode = Route.TrafficPenaltyMode.DISABLED
        assertNotNull(presenter.trafficMode)
    }

    @Test
    fun testOnCreateView() {
        val inflaterMock = mock(LayoutInflater::class.java)
        val viewGroupMock = mock(ViewGroup::class.java)
        val inflatedView = mock(View::class.java)
        `when`(inflaterMock.inflate(R.layout.route_planner, viewGroupMock, false)).thenReturn(inflatedView)
        fragment.onCreateView(inflaterMock, viewGroupMock, null)
        with(verify(presenter, atLeastOnce())) {
            appActionBar = anySafe()
            coordinatorListener = anySafe()
        }
        verify(inflaterMock).inflate(R.layout.route_planner, viewGroupMock, false)
        verify(inflatedView).isClickable = true
    }

    @Test
    fun testUpdateList() {
        with(fragment) {
            updateList(true)
            with(view!!) {
                assertEquals(waypoint_list.visibility, View.VISIBLE)
                assertEquals(waypoint_add.visibility, View.VISIBLE)
                assertEquals(swap_list.visibility, View.VISIBLE)
                updateList(false)
                assertEquals(waypoint_list.visibility, View.GONE)
                assertEquals(waypoint_add.visibility, View.GONE)
                assertEquals(swap_list.visibility, View.GONE)
            }
        }
    }

    @Test
    fun testOnViewCreated() {
        val waypointList = fragment.view!!.waypoint_list
        with(verify(presenter)) {
            onAttach(anySafe(), anySafe())
            updateActionBar(anyBoolean(), anyBoolean(), anyBoolean())
            makeWaypointListData(waypointList.entries, true)
            makeTravelTimeData(fragment.view!!.travel_time_panel.time, fragment.view!!.travel_time_panel.timeType)
            makeSwapReady()
        }
        fragment.view!!.option_panel.performClick()
        verify(presenter).openOptionPanel()

        // tests makeWaypointAddReady()
        fragment.view!!.waypoint_add.performClick()
        verify(waypointListenerMock).onEntryClicked(anyInt(), anySafe())

        // tests makeTransportModeReady()
        fragment.view!!.transport_panel.selected = 1
        verify(presenter).transportModeSelected(anySafe())
    }

    @Test
    fun testOnWaypointListDataReady() {
        val waypointEntry1 = mock(WaypointEntry::class.java)
        val waypointEntry2 = mock(WaypointEntry::class.java)
        val waypointEntry3 = mock(WaypointEntry::class.java)

        val entriesMap = SparseArray<WaypointEntry>().apply {
            put(0, waypointEntry1)
            put(1, waypointEntry2)
            put(2, waypointEntry3)
        }
        fragment.onWaypointListDataReady(entriesMap)
        assertEquals(fragment.view!!.waypoint_list.entries[0], waypointEntry1)
        assertEquals(fragment.view!!.waypoint_list.entries[1], waypointEntry2)
        assertEquals(fragment.view!!.waypoint_list.entries[2], waypointEntry3)
    }

    @Test
    fun testOnTravelTimeDataReady() {
        val calendar = Calendar.getInstance()
        var time = calendar.getTime()
        fragment.onTravelTimeDataReady(time, RouteOptions.TimeType.ARRIVAL)
        assertEquals(fragment.view!!.travel_time_panel.time, time)
        assertEquals(fragment.view!!.travel_time_panel.timeType, RouteOptions.TimeType.ARRIVAL)
        calendar.time = Date();
        calendar.add(Calendar.DATE, 1);
        time = calendar.time
        fragment.view!!.travel_time_panel.time = time
        verify(presenter).timeChanged(time, RouteOptions.TimeType.ARRIVAL)
    }

    @Test
    fun testOnSwapReady() {
        fragment.onSwapReady(true)
        assertTrue(fragment.view!!.swap_list.isEnabled)
        fragment.view!!.swap_list.performClick()
        verify(presenter).swapClicked()
    }

    @Test
    fun testUpdateWaypoint() {
        val waypointEntry = mock(WaypointEntry::class.java)
        fragment.updateWaypoint(0, waypointEntry)
        verify(presenter).updateWaypoint(0, waypointEntry, fragment.view!!.waypoint_list.entries)
    }

    @Test
    fun testReset() {
        fragment.reset()
        verify(presenter).reset(fragment.view!!.waypoint_list.entries)
        assertNotNull(fragment.view!!.travel_time_panel.time)
    }

    @Test
    fun testCalculateRoute() {
        with(fragment) {
            userVisibleHint = false
            calculateRoute()
            userVisibleHint = true
            calculateRoute()
        }
        verify(presenter).calculateRoute(fragment.view!!.waypoint_list.entries)
    }

    @Test
    fun testUpdateActionBar() {
        fragment.updateActionBar(false, false, false)
        verify(presenter).updateActionBar(false, false, false)
    }

    @Test
    fun testWaypointSelectionCancelled() {
        val waypointList = fragment.view!!.waypoint_list
        val entry = mock(WaypointEntry::class.java)

        fragment.waypointSelectionCancelled(null, null)
        fragment.waypointSelectionCancelled(0, null)
        fragment.waypointSelectionCancelled(1, null)
        // All above calls should not remove any entry from waypointList because of incorrect
        // index and min waypoint items requirement.
        assertEquals(waypointList.entriesCount, 2)

        waypointList.addEntry(entry)
        `when`(entry.isValid).thenReturn(true)
        fragment.waypointSelectionCancelled(2, entry)
        assertEquals(waypointList.entriesCount, 3)

        `when`(entry.isValid).thenReturn(false)
        fragment.waypointSelectionCancelled(2, entry)
        assertEquals(waypointList.entriesCount, 2)
    }
}