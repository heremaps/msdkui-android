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

package com.here.msdkuiapp.routing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkuiapp.R
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertNotNull
import kotlinx.android.synthetic.main.option_panel.view.optionpanel_container
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Tests for [OptionPanelFragment].
 */
class OptionPanelFragmentTest : BaseTest() {

    private lateinit var fragment: OptionPanelFragment

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockPresenter: OptionPanelPresenter

    @Mock
    private lateinit var optionPanelFragmentListenerMock: OptionPanelFragment.Listener

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockRouteOptions: RouteOptions

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockDynamicPenalty: DynamicPenalty

    @Before
    public override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        val mocks = mock(RouteOptions::class.java)
        fragment =  OptionPanelFragment.newInstance().apply {
            presenter = mockPresenter
            `when`(presenter.routeOptions).thenReturn(mockRouteOptions)
            `when`(presenter.dynamicPenalty).thenReturn(mockDynamicPenalty)
        }
        `when`(mockRouteOptions.routeType).thenReturn(RouteOptions.Type.BALANCED)
        addFrag(fragment)
    }

    @Test
    fun testPresenterInit() {
        fragment = OptionPanelFragment.newInstance()
        assertNotNull(fragment.presenter)
    }

    @Test
    fun testOnCreateView() {
        fragment.listener = mock(OptionPanelFragment.Listener::class.java)
        val inflaterMock = mock(LayoutInflater::class.java)
        val viewGroupMock = mock(ViewGroup::class.java)
        fragment.onCreateView(inflaterMock, viewGroupMock, null)
        verify(inflaterMock).inflate(R.layout.option_panel, viewGroupMock, false)
        // check null because current coordinator base activity is null and it will be assigned
        assertNull(fragment.listener)
    }

    @Test
    fun testOnViewCreated() {
        with(verify(mockPresenter)) {
            onAttach(anySafe(), anySafe())
            updateActionBar(anySafe())
            makeUiDataReady()
        }
    }

    @Test
    fun testOnSubPanelClicked() {
        fragment.listener = optionPanelFragmentListenerMock
        val panelsMock = mock(Panels::class.java)
        fragment.onSubPanelClicked(panelsMock, null)
        verify(optionPanelFragmentListenerMock).openSubPanel(panelsMock, null)
    }

    @Test
    fun testTrafficChanged() {
        fragment.listener = optionPanelFragmentListenerMock
        fragment.trafficChanged(Route.TrafficPenaltyMode.OPTIMAL)
        verify(optionPanelFragmentListenerMock).trafficChanged(Route.TrafficPenaltyMode.OPTIMAL)
    }

    @Test
    fun testOnUiDataReady() {
        val viewMock1 = mock(View::class.java)
        val viewMock2 = mock(View::class.java)
        val viewsCollection = listOf<View>(viewMock1, viewMock2)
        fragment.onUiDataReady(viewsCollection)
        assertEquals(fragment.view!!.optionpanel_container.childCount, 2)
    }

    @Test
    fun testGetRowView() {
        assertNotNull(fragment.getRowView(R.string.msdkui_routing_options_title))
    }

    @Test
    fun testUpdateOptions() {
        fragment.updateOptions()
        verify(mockPresenter).updateOptions()
    }

    @Test
    fun testSetAndGetRouteOptions() {
        fragment = OptionPanelFragment.newInstance()
        fragment.routeOptions = mockRouteOptions
        assertEquals(fragment.routeOptions, mockRouteOptions)
    }

    @Test
    fun testSetAndGetDynamicPenalty() {
        fragment = OptionPanelFragment.newInstance()
        fragment.dynamicPenalty = mockDynamicPenalty
        assertEquals(fragment.dynamicPenalty, mockDynamicPenalty)
    }
}