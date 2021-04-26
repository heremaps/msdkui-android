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

import com.here.android.mpa.routing.Route
import com.here.msdkuiapp.base.BaseActivity
import com.here.msdkuiapp.base.BaseContract
import com.here.testutils.BaseTest
import com.here.testutils.argumentCaptor
import com.here.testutils.captureSafe
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Tests for [RouteDescriptionListPresenter].
 */
class RouteDescriptionListPresenterTest : BaseTest() {

    private lateinit var presenter: RouteDescriptionListPresenter

    @Mock
    private lateinit var mockActivity: BaseActivity

    @Mock
    private lateinit var mockBaseContract: BaseContract<RoutingContracts.RouteDescriptionList>

    @Mock
    private lateinit var mockContract: RoutingContracts.RouteDescriptionList

    @Before
    override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        `when`(mockBaseContract.getCurrentViewContract()).thenReturn(mockContract)
        presenter = RouteDescriptionListPresenter()
        with(presenter) {
            onAttach(mockActivity, mockBaseContract)
            context = activityContext
        }
    }

    @Test
    fun testMakeUiDataReadyAndUpdateRoute() {
        val captor = argumentCaptor<ArrayList<Route>>()
        val mockRoute = MockUtils.mockRoute()
        val routesList = listOf<Route>(mockRoute)

        presenter.trafficMode = true

        presenter.updateRoutes(routesList)
        presenter.makeUiDataReady()

        verify(mockContract).onUiDataReady(eq(true), captureSafe(captor))

        assertEquals(captor.value[0], mockRoute)
    }
}