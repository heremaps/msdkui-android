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

import android.app.Activity
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseActivity
import com.here.msdkuiapp.base.BaseContract
import com.here.msdkuiapp.common.AppActionBar
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Tests for [SubOptionPanelPresenter].
 */
class SubOptionPanelPresenterTest : BaseTest() {

    private lateinit var presenter: SubOptionPanelPresenter

    @Mock
    private lateinit var mockAppActionBar: AppActionBar

    @Mock
    private lateinit var mockActivity: BaseActivity

    @Mock
    private lateinit var mockBaseContract: BaseContract<RoutingContracts.SubOptionPanel>

    @Mock
    private lateinit var mockContract: RoutingContracts.SubOptionPanel

    @Before
    override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        `when`(mockBaseContract.getCurrentViewContract()).thenReturn(mockContract)
        presenter = SubOptionPanelPresenter()
        with(presenter) {
            onAttach(mockActivity, mockBaseContract)
            context = activityContext
        }
    }

    @Test
    fun testSettersAndGetters() {
        with(presenter) {
            routeOptions = mock(RouteOptions::class.java)
            assertNotNull(routeOptions)
            type = Panels.TRUCK
            assertNotNull(type)
        }
    }

    @Test
    fun testUpdateActionBar() {
        val appActionBar = spy(AppActionBar(fragmentActivity as Activity))
        with(appActionBar) {
            presenter.updateActionBar(this)
            verify(this).setBack(true, R.drawable.ic_arrow_back_black_24dp)
            verify(this).setTitle(value = getString(R.string.msdkui_app_options))
            verify(this).setRightIcon(visible = false)
        }
    }

    @Test
    fun testMakeUiDataReady() {
        with(presenter) {
            routeOptions = mock(RouteOptions::class.java, RETURNS_DEEP_STUBS)

            // first the default case
            makeUiDataReady(mockAppActionBar)

            type = Panels.DRIVE
            makeUiDataReady(mockAppActionBar)
            verify(mockAppActionBar).setTitle(value = getString(R.string.msdkui_routing_options_title))

            type = Panels.HAZARDOUS
            makeUiDataReady(mockAppActionBar)
            verify(mockAppActionBar).setTitle(value = getString(R.string.msdkui_hazardous_materials_title))

            type = Panels.TRUCK
            makeUiDataReady(mockAppActionBar)
            verify(mockAppActionBar).setTitle(value = getString(R.string.msdkui_truck_options_title))
        }
        verify(mockContract, times(4)).onUiDataReady(anySafe())
    }
}