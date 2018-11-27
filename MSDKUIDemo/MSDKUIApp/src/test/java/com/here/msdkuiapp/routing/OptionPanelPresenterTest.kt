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

import android.app.Activity
import android.view.View
import com.here.android.mpa.routing.*
import com.here.msdkui.routing.TrafficOptionsPanel
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseActivity
import com.here.msdkuiapp.base.BaseContract
import com.here.msdkuiapp.common.AppActionBar
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import com.here.testutils.captureSafe
import junit.framework.Assert.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

/**
 * Tests for [OptionPanelPresenter].
 */
class OptionPanelPresenterTest : BaseTest() {

    private lateinit var presenter: OptionPanelPresenter

    @Mock
    private lateinit var mockActivity: BaseActivity

    @Mock
    private lateinit var mockBaseContract: BaseContract<RoutingContracts.OptionPanel>

    @Mock
    private lateinit var mockContract: RoutingContracts.OptionPanel

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockRouteOptions: RouteOptions

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockDynamicPenalty: DynamicPenalty

    @Before
    override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        `when`(mockBaseContract.getCurrentViewContract()).thenReturn(mockContract)
        presenter = OptionPanelPresenter()
        with(presenter) {
            onAttach(mockActivity, mockBaseContract)
            context = activityContext
            routeOptions = mockRouteOptions
            dynamicPenalty = mockDynamicPenalty
        }
    }

    @Test
    fun testUpdateActionBar() {
        val appActionBar = spy(AppActionBar(fragmentActivity as Activity))
        with(appActionBar) {
            presenter.updateActionBar(this)
            verify(this).setBack(true)
            verify(this).setTitle(value = getString(R.string.msdkui_app_options))
            verify(this).setRightIcon(visible = false)
        }
    }

    @Test
    fun testSettersAndGetters() {
        with(presenter) {
            routeOptions = mockRouteOptions
            assertEquals(routeOptions, mockRouteOptions)
            dynamicPenalty = mockDynamicPenalty
            assertEquals(dynamicPenalty, mockDynamicPenalty)
        }
    }

    @Test
    fun testMakeUiDataReady() {
        val mockDriveView = mock(View::class.java)
        val mockDriveViewOnClickListenerCaptor = argumentCaptor<View.OnClickListener>()
        `when`(mockContract.getRowView(R.string.msdkui_routing_options_title)).thenReturn(mockDriveView)

        val mockHazardousView = mock(View::class.java)
        val mockHazardousViewOnClickListenerCaptor = argumentCaptor<View.OnClickListener>()
        `when`(mockContract.getRowView(R.string.msdkui_hazardous_materials_title)).thenReturn(mockHazardousView)

        val mockTruckView = mock(View::class.java)
        val mockTruckViewOnClickListenerCaptor = argumentCaptor<View.OnClickListener>()
        `when`(mockContract.getRowView(R.string.msdkui_truck_options_title)).thenReturn(mockTruckView)

        val viewsCaptor = argumentCaptor<Collection<View>>()

        presenter.makeUiDataReady()

        verify(mockDriveView).setOnClickListener(mockDriveViewOnClickListenerCaptor.capture())
        verify(mockHazardousView).setOnClickListener(mockHazardousViewOnClickListenerCaptor.capture())
        verify(mockTruckView).setOnClickListener(mockTruckViewOnClickListenerCaptor.capture())

        verify(mockContract).onUiDataReady(captureSafe(viewsCaptor))

        assertNotNull(presenter.panels[Panels.ROUTE_TYPE])

        assertNotNull(presenter.panels[Panels.TRAFFIC])
        (presenter.panels[Panels.TRAFFIC] as TrafficOptionsPanel).onChanged(null)
        verify(mockContract).trafficChanged(anySafe())
        (presenter.panels[Panels.TRAFFIC] as TrafficOptionsPanel).optionItems = emptyList()
        // nothing to verify because onOptionCreated() body is empty

        assertNotNull(presenter.panels[Panels.DRIVE])
        mockDriveViewOnClickListenerCaptor.value.onClick(null)
        verify(mockContract).onSubPanelClicked(Panels.DRIVE, mockRouteOptions)

        assertNotNull(presenter.panels[Panels.TUNNEL])

        assertNotNull(presenter.panels[Panels.HAZARDOUS])
        mockHazardousViewOnClickListenerCaptor.value.onClick(null)
        verify(mockContract).onSubPanelClicked(Panels.HAZARDOUS, mockRouteOptions)

        assertNotNull(presenter.panels[Panels.TRUCK])
        mockTruckViewOnClickListenerCaptor.value.onClick(null)
        verify(mockContract).onSubPanelClicked(Panels.TRUCK, mockRouteOptions)

        // for transportMode other than: TRUCK, BICYCLE, PEDESTRIAN, SCOOTER
        assertEquals(viewsCaptor.value.size, 3)

        // RouteOptions.TransportMode.TRUCK
        `when`(mockRouteOptions.transportMode).thenReturn(RouteOptions.TransportMode.TRUCK)
        presenter.makeUiDataReady()
        verify(mockContract, atLeastOnce()).onUiDataReady(captureSafe(viewsCaptor))
        assertEquals(viewsCaptor.value.size, 5)

        // RouteOptions.TransportMode.BICYCLE
        `when`(mockRouteOptions.transportMode).thenReturn(RouteOptions.TransportMode.BICYCLE)
        presenter.makeUiDataReady()
        verify(mockContract, atLeastOnce()).onUiDataReady(captureSafe(viewsCaptor))
        assertEquals(viewsCaptor.value.size, 1)

        // RouteOptions.TransportMode.PEDESTRIAN
        `when`(mockRouteOptions.transportMode).thenReturn(RouteOptions.TransportMode.PEDESTRIAN)
        presenter.makeUiDataReady()
        verify(mockContract, atLeastOnce()).onUiDataReady(captureSafe(viewsCaptor))
        assertEquals(viewsCaptor.value.size, 1)

        // RouteOptions.TransportMode.SCOOTER
        `when`(mockRouteOptions.transportMode).thenReturn(RouteOptions.TransportMode.SCOOTER)
        presenter.makeUiDataReady()
        verify(mockContract, atLeastOnce()).onUiDataReady(captureSafe(viewsCaptor))
        assertEquals(viewsCaptor.value.size, 2)
    }

    @Test
    fun testUpdateOptions() {
        with(presenter) {
            makeUiDataReady()

            routeOptions = null
            dynamicPenalty = null

            updateOptions()

            assertNotNull(routeOptions)
            assertNotNull(dynamicPenalty)
        }
    }
}