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

import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkuiapp.base.BaseActivity
import com.here.msdkuiapp.base.BaseContract
import com.here.testutils.BaseTest
import com.here.testutils.argumentCaptor
import com.here.testutils.captureSafe
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Tests for [ManeuverRouteDescriptionPresenter].
 */
class ManeuverRouteDescriptionPresenterTest : BaseTest() {

    private lateinit var presenter: ManeuverRouteDescriptionPresenter

    @Mock
    private lateinit var mockActivity: BaseActivity

    @Mock
    private lateinit var mockBaseContract: BaseContract<RoutingContracts.ManeuverRouteDescription>

    @Mock
    private lateinit var mockContract: RoutingContracts.ManeuverRouteDescription

    @Before
    override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        `when`(mockBaseContract.getCurrentViewContract()).thenReturn(mockContract)
        presenter = ManeuverRouteDescriptionPresenter()
        with(presenter) {
            onAttach(mockActivity, mockBaseContract)
            context = activityContext
        }
    }

    @Test
    fun testMakeUiDataReadyAndUpdateRoute() {
        val captor = argumentCaptor<RouteDescriptionItem>()
        val mockRoute = MockUtils.mockRoute()

        presenter.updateRoute(mockRoute)
        presenter.makeUiDataReady()

        verify(mockContract).onUiDataReady(captureSafe(captor))

        assertEquals(captor.value.route, mockRoute)
    }
}