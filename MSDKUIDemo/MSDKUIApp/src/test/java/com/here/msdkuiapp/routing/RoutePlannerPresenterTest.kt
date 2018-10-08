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

import com.here.android.mpa.routing.CoreRouter
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.RouteOptions
import com.here.android.mpa.routing.RouteResult
import com.here.android.mpa.routing.RoutePlan
import com.here.android.mpa.routing.RoutingError
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseActivity
import com.here.msdkuiapp.base.BaseContract
import com.here.msdkuiapp.common.AppActionBar
import com.here.msdkuiapp.common.Provider
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.Date

/**
 * Tests for [RoutePlannerPresenter].
 */
class RoutePlannerPresenterTest {

    private lateinit var presenter: RoutePlannerPresenter

    @Mock
    private lateinit var mockActivity: BaseActivity

    @Mock
    private lateinit var mockBaseContract: BaseContract<RoutingContracts.RoutePlanner>

    @Mock
    private lateinit var mockContract: RoutingContracts.RoutePlanner

    @Mock
    private lateinit var mockProvider: Provider

    @Mock
    private lateinit var mockCoordinatorListener: RoutePlannerFragment.Listener

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockRouteOptions: RouteOptions

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockDynamicPenalty: DynamicPenalty

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockWaypointEntry1: WaypointEntry

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockWaypointEntry2: WaypointEntry


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(mockBaseContract.getCurrentViewContract()).thenReturn(mockContract)
        presenter = RoutePlannerPresenter()
        presenter.provider = mockProvider
        presenter.state.provider = mockProvider
        `when`(mockProvider.providesRouteOptions()).thenReturn(mockRouteOptions)
        `when`(mockProvider.providesDynamicPenalty()).thenReturn(mockDynamicPenalty)
        presenter.coordinatorListener = mockCoordinatorListener
        `when`(mockActivity.getString(anyInt())).thenReturn("")
        presenter.onAttach(mockActivity, mockBaseContract)

    }

    @Test
    fun testSettersAndGetters() {
        presenter.trafficMode = Route.TrafficPenaltyMode.OPTIMAL
        assertNotNull(presenter.trafficMode)
    }

    @Test
    fun testWaypointListener() {
        presenter.waypointListener.onEntryClicked(0, mockWaypointEntry1)
        verify(mockCoordinatorListener).onEntryClicked(0, mockWaypointEntry1)

        presenter.state.entryList.add(mockWaypointEntry1)
        presenter.waypointListener.onEntryRemoved(0, mockWaypointEntry1)
        assertTrue(presenter.state.entryList.isEmpty())

        presenter.state.entryList.add(mockWaypointEntry1)
        presenter.state.entryList.add(mockWaypointEntry2)
        presenter.waypointListener.onEntryDragged(0, 1)
        assertEquals(presenter.state.entryList[0], mockWaypointEntry2)
        assertEquals(presenter.state.entryList[1], mockWaypointEntry1)

        presenter.waypointListener.onEntryAdded(0, mockWaypointEntry1)
        presenter.waypointListener.onEntryUpdated(0, mockWaypointEntry1)
        // nothing to test here
    }

    @Test
    fun testActionBarAndNotifyListTitleChanges() {
        val appActionBar = spy(AppActionBar(mockActivity))
        presenter.appActionBar = appActionBar
        with(appActionBar) {
            presenter.updateActionBar(true, true, true)
            verify(this).setBack(true)
            verify(this).setTitle(value = "")
            verify(mockCoordinatorListener).onTitleChange(anyBoolean())
            verify(this).setRightIcon(visible = false)

            val listenerCaptor = argumentCaptor<(() -> Unit)>()
            `when`(mockWaypointEntry1.isValid).thenReturn(true)
            presenter.state.entryList.add(mockWaypointEntry1)

            presenter.state.isExpanded = true
            presenter.updateActionBar(false, false, true)
            verify(this).setRightIcon(anyBoolean(), eq(R.drawable.ic_collapse), anyString(), listenerCaptor.capture())
            listenerCaptor.value.invoke()
            assertEquals(presenter.state.isExpanded, false)
            verify(mockContract).updateList(false)

            presenter.state.isExpanded = false
            presenter.updateActionBar(false, false, true)
            verify(this, atLeastOnce()).setRightIcon(anyBoolean(), eq(R.drawable.ic_expande), anyString(), listenerCaptor.capture())
            listenerCaptor.value.invoke()
            assertEquals(presenter.state.isExpanded, true)
            verify(mockContract).updateList(false)
        }
    }

    @Test
    fun testCalculateRoute() {
        val entryList = ArrayList<WaypointEntry>()
        entryList.add(mockWaypointEntry1)
        `when`(mockWaypointEntry1.isValid).thenReturn(true)
        entryList.add(mockWaypointEntry2)
        `when`(mockWaypointEntry2.isValid).thenReturn(true)

        val mockRoutePlan = mock(RoutePlan::class.java)
        val mockCoreRouter = mock(CoreRouter::class.java)
        `when`(mockProvider.provideRoutePlan()).thenReturn(mockRoutePlan)
        `when`(mockProvider.providesCoreRouter()).thenReturn(mockCoreRouter)

        val listenerCaptor = argumentCaptor<CoreRouter.Listener>()
        `when`(mockWaypointEntry1.isValid).thenReturn(true)
        presenter.state.entryList.add(mockWaypointEntry1)

        presenter.calculateRoute(entryList)

        verify(mockContract).onProgress(true)
        verify(mockProvider).provideRoutePlan()
        verify(mockRoutePlan).addWaypoint(mockWaypointEntry1.routeWaypoint)
        verify(mockRoutePlan).addWaypoint(mockWaypointEntry2.routeWaypoint)
        verify(mockProvider).providesCoreRouter()
        verify(mockRouteOptions).setTime(presenter.state.travelDate, presenter.state.travelType)
        verify(mockRouteOptions).routeCount = 5
        verify(mockRoutePlan).routeOptions = mockRouteOptions
        assertEquals(mockDynamicPenalty.trafficPenaltyMode, presenter.state.dynamicPenalty.trafficPenaltyMode)
        verify(mockCoreRouter).dynamicPenalty = mockDynamicPenalty
        verify(mockCoreRouter).calculateRoute(eq(mockRoutePlan), listenerCaptor.capture())

        val routeList = ArrayList<RouteResult>()
        routeList.add(mock(RouteResult::class.java))
        listenerCaptor.value.onCalculateRouteFinished(routeList, mock(RoutingError::class.java))
        verify(mockContract).onProgress(false)
        verify(mockCoordinatorListener).onRouteCalculated(routeList.map { it.route })

        // also test with empty route list
        routeList.clear()
        listenerCaptor.value.onCalculateRouteFinished(routeList, mock(RoutingError::class.java))
        verify(mockContract).onRoutingFailed(anyString())

        listenerCaptor.value.onProgress(0)
        // nothing to check
    }

    @Test
    fun testMakeWaypointListData() {
        val mockWaypointEntry = mock(WaypointEntry::class.java)
        val entryList = ArrayList<WaypointEntry>()
        entryList.add(mockWaypointEntry)
        entryList.add(mockWaypointEntry)
        entryList.add(mockWaypointEntry)

        presenter.makeWaypointListData(entryList, true)
        verify(mockContract).updateList(anyBoolean())

        entryList.clear()
        entryList.add(mockWaypointEntry1)
        entryList.add(mockWaypointEntry2)
        presenter.makeWaypointListData(entryList, false)

        verify(mockContract, times(2)).onWaypointListDataReady(anySafe())
    }

    @Test
    fun testMakeSwapReady() {
        presenter.makeSwapReady()
        verify(mockContract).onSwapReady(false)

        presenter.state.entryList.add(mockWaypointEntry1)
        `when`(mockWaypointEntry1.isValid).thenReturn(true)
        presenter.state.entryList.add(mockWaypointEntry2)
        `when`(mockWaypointEntry2.isValid).thenReturn(true)
        presenter.makeSwapReady()
        verify(mockContract).onSwapReady(true)
    }

    @Test
    fun testMakeTravelTimeData() {
        val mockTime = mock(Date::class.java)
        val mockTimeType = mock(RouteOptions.TimeType::class.java)

        presenter.makeTravelTimeData(mockTime, mockTimeType)
        assertEquals(presenter.state.travelDate, mockTime)
        assertEquals(presenter.state.travelType, mockTimeType)
        verify(mockContract).onTravelTimeDataReady(mockTime, mockTimeType)
    }

    @Test
    fun testTransportModeSelected() {
        `when`(mockRouteOptions.transportMode).thenReturn(RouteOptions.TransportMode.TRUCK)
        presenter.transportModeSelected(RouteOptions.TransportMode.TRUCK)
        // nothing happens because transport mode is the same

        presenter.transportModeSelected(RouteOptions.TransportMode.CAR)
        verify(mockRouteOptions).transportMode = RouteOptions.TransportMode.CAR
    }

    @Test
    fun testSwapClicked() {
        presenter.state.entryList.add(mockWaypointEntry1)
        presenter.state.entryList.add(mockWaypointEntry2)
        presenter.swapClicked()
        assertEquals(presenter.state.entryList[0], mockWaypointEntry2)
        assertEquals(presenter.state.entryList[1], mockWaypointEntry1)
    }

    @Test
    fun testTimeChanged() {
        val mockTime = mock(Date::class.java)
        val mockTimeType = mock(RouteOptions.TimeType::class.java)

        presenter.timeChanged(mockTime, mockTimeType)
        assertEquals(presenter.state.travelDate, mockTime)
        assertEquals(presenter.state.travelType, mockTimeType)
    }

    @Test
    fun testOpenOptionPanel() {
        presenter.openOptionPanel()
        verify(mockCoordinatorListener).onOptionPanelClicked(mockRouteOptions, mockDynamicPenalty)
    }

    @Test
    fun testUpdateWaypoint() {
        val mockWaypointEntry = mock(WaypointEntry::class.java)
        presenter.state.entryList.add(mockWaypointEntry1)
        presenter.state.entryList.add(mockWaypointEntry2)

        presenter.updateWaypoint(0, mockWaypointEntry, null)
        assertEquals(presenter.state.entryList[0], mockWaypointEntry)

        presenter.updateWaypoint(2, mockWaypointEntry, null)
        assertEquals(presenter.state.entryList[2], mockWaypointEntry)
    }

    @Test
    fun testReset() {
        val mockWaypointEntry = mock(WaypointEntry::class.java)
        val entryList = ArrayList<WaypointEntry>()
        entryList.add(mockWaypointEntry)
        presenter.state.entryList.add(mockWaypointEntry1)
        presenter.state.entryList.add(mockWaypointEntry2)

        presenter.reset(entryList)
        assertEquals(presenter.state.entryList[0], mockWaypointEntry)
    }
}