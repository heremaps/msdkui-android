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
import com.here.android.mpa.routing.Route
import com.here.msdkuiapp.R
import com.here.msdkuiapp.common.Util
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNotNull
import kotlinx.android.synthetic.main.route_description_list.view.route_description_list
import kotlinx.android.synthetic.main.route_description_list.view.route_description_list_heading
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Tests for [RouteDescriptionListFragment].
 */
class RouteDescriptionListFragmentTest : BaseTest() {

    private lateinit var fragment: RouteDescriptionListFragment

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var presenterMock: RouteDescriptionListPresenter

    @Mock
    private lateinit var routeListMock: List<Route>

    @Mock
    private lateinit var listenerMock: RouteDescriptionListFragment.Listener

    @Before
    public override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        fragment = RouteDescriptionListFragment()
        fragment.presenter = presenterMock
        addFrag(fragment)
    }

    @Test
    fun testPresenterInit() {
        fragment = RouteDescriptionListFragment.newInstance()
        assertNotNull(fragment.presenter)
    }

    @Test
    fun testSetUserVisibleHint() {
        fragment.userVisibleHint = false
        fragment.userVisibleHint = true

        // test when fragment is not resumed
        fragment = RouteDescriptionListFragment.newInstance().apply {
            presenter = presenterMock
            userVisibleHint = true
        }
        addFrag(fragment)

        verify(presenterMock, atLeastOnce()).makeUiDataReady()
    }

    @Test
    fun testOnCreateView() {
        val inflaterMock = mock(LayoutInflater::class.java)
        val viewGroupMock = mock(ViewGroup::class.java)
        fragment.onCreateView(inflaterMock, viewGroupMock, null)
        verify(inflaterMock).inflate(R.layout.route_description_list, viewGroupMock, false)
    }

    @Test
    fun testOnViewCreated() {
        fragment.listener = mock(RouteDescriptionListFragment.Listener::class.java)
        with(verify(presenterMock)) {
            onAttach(anySafe(), anySafe())
            makeUiDataReady()
        }
        assertNotNull(fragment.listener)
        assertEquals(fragment.view!!.route_description_list.unitSystem,
                Util.getLocaleUnit())
    }

    @Test
    fun testOnUiDataReadyAndItemClickedListener() {
        val routeMock = MockUtils.MockRouteBuilder().route
        val route2Mock = MockUtils.MockRouteBuilder().route

        val routeList = listOf<Route>(routeMock, route2Mock)
        fragment.updateRoutes(routeList)
        fragment.onUiDataReady(false, routeList)
        assertEquals(fragment.view!!.route_description_list_heading.visibility, View.GONE)
        with(fragment.view!!.route_description_list) {
            assertFalse(isTrafficEnabled)
            assertEquals(routes, routeList)
        }
    }

    @Test
    fun testUpdateRoutes() {
        fragment.updateRoutes(routeListMock)
        with(verify(presenterMock)) {
            updateRoutes(routeListMock)
            makeUiDataReady()
        }
    }

    @Test
    fun testUpdateTitle() {
        fragment.updateTitle(false)
        assertEquals(fragment.view!!.route_description_list_heading.visibility, View.VISIBLE)
        fragment.updateTitle(true)
        assertEquals(fragment.view!!.route_description_list_heading.visibility, View.GONE)
    }

    @Test
    fun testSetAndGetTraffic() {
        fragment = RouteDescriptionListFragment.newInstance()
        fragment.traffic = false
        assertFalse(fragment.traffic)
    }
}