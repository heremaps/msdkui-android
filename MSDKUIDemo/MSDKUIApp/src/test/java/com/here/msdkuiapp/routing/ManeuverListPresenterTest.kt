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
import android.content.res.Configuration
import android.view.View
import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.ManeuverDescriptionItem
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseActivity
import com.here.msdkuiapp.base.BaseContract
import com.here.msdkuiapp.common.AppActionBar
import com.here.testutils.BaseTest
import com.here.testutils.argumentCaptor
import com.here.testutils.captureSafe
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Tests for [ManeuverListPresenter].
 */
class ManeuverListPresenterTest : BaseTest() {

    private lateinit var presenter: ManeuverListPresenter

    @Mock
    private lateinit var mockActivity: BaseActivity

    @Mock
    private lateinit var mockBaseContract: BaseContract<RoutingContracts.ManeuverList>

    @Mock
    private lateinit var mockContract: RoutingContracts.ManeuverList

    @Before
    override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        `when`(mockBaseContract.getCurrentViewContract()).thenReturn(mockContract)
        presenter = ManeuverListPresenter()
        with(presenter) {
            onAttach(mockActivity, mockBaseContract)
            context = activityContext
        }
    }

    @Test
    fun testItemClickedListenerAndRouteItemClicked() {
        val mockCoordinatorListener = mock(ManeuverListFragment.Listener::class.java)
        val mockManeuverDescriptionItem = mock(ManeuverDescriptionItem::class.java)
        val mockView = mock(View::class.java)
        presenter.coordinatorListener = mockCoordinatorListener

        presenter.itemClickedListener.onItemClicked(0, mockManeuverDescriptionItem)
        verify(mockCoordinatorListener).onManeuverClicked(0, mockManeuverDescriptionItem)

        presenter.itemClickedListener.onItemLongClicked(0, mockView)
        // nothing to check

        presenter.routeItemClicked(Configuration.ORIENTATION_LANDSCAPE)
        verify(mockCoordinatorListener).zoomToRoute(false)
    }

    @Test
    fun testUpdateActionBar() {
        val appActionBar = spy(AppActionBar(fragmentActivity as Activity))
        with(appActionBar) {
            presenter.updateActionBar(this)
            verify(this).setBack(true, id = R.drawable.ic_arrow_back_black_24dp)
            verify(this).setTitle(value = getString(R.string.msdkui_app_route_preview_title))
            verify(this).setRightIcon(visible = false)
        }
    }

    @Test
    fun testPopulateConfigChanges() {
        val mockCoordinatorListener = mock(ManeuverListFragment.Listener::class.java)
        presenter.coordinatorListener = mockCoordinatorListener

        presenter.populateConfigChanges(Configuration.ORIENTATION_LANDSCAPE)
        verify(mockContract).updateConfigChanges(false)
        verify(mockCoordinatorListener).zoomToRoute(false)
    }

    @Test
    fun testMakeUiDataReadyAndUpdateRoute() {
        val captor = argumentCaptor<Route>()
        val mockRoute = MockUtils.mockRoute()

        presenter.updateRoute(mockRoute, true)
        presenter.makeUiDataReady()

        verify(mockContract).onUiDataReady(eq(true), captureSafe(captor))

        assertEquals(captor.value, mockRoute)
    }
}