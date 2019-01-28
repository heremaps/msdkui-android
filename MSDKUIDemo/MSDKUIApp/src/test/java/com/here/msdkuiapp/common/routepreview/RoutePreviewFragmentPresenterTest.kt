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

package com.here.msdkuiapp.common.routepreview

import android.app.AlertDialog
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.PositioningManager
import com.here.android.mpa.routing.*
import com.here.android.mpa.search.ErrorCode
import com.here.android.mpa.search.Location
import com.here.android.mpa.search.ResultListener
import com.here.android.mpa.search.ReverseGeocodeRequest2
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.GuidanceContracts
import com.here.msdkuiapp.MSDKUIApplication
import com.here.msdkuiapp.base.BaseActivity
import com.here.msdkuiapp.base.BaseContract
import com.here.msdkuiapp.common.AppActionBar
import com.here.msdkuiapp.common.Provider
import com.here.msdkuiapp.guidance.SingletonHelper
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.shadows.ShadowAlertDialog

/**
 * Tests for [RoutePreviewFragmentPresenter].
 */
class RoutePreviewFragmentPresenterTest : BaseTest() {

    private lateinit var presenter: RoutePreviewFragmentPresenter

    @Mock
    private lateinit var mockActivity: BaseActivity

    @Mock
    private lateinit var mockContract: BaseContract<GuidanceContracts.RoutePreview>

    @Mock
    private lateinit var mockRoutePreview: GuidanceContracts.RoutePreview

    @Mock
    private lateinit var mockProvider: Provider

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockPositioningManager: PositioningManager

    @Before
    override fun setUp() {
        MockitoAnnotations.initMocks(this)
        SingletonHelper.positioningManager = mockPositioningManager
        `when`(mockContract.getCurrentViewContract()).thenReturn(mockRoutePreview)
        presenter = RoutePreviewFragmentPresenter()
        presenter.provider = mockProvider
        `when`(mockActivity.getString(anyInt())).thenReturn("")
        presenter.onAttach(mockActivity, mockContract)
    }

    @Test
    fun testActionBar() {
        val appActionBar = spy(AppActionBar(mockActivity))
        with(appActionBar) {
            presenter.updateActionBar(this)
            verify(this).setBack(true)
            verify(this).setTitle(value = "")
            verify(this).setRightIcon(visible = false)
        }
    }

    @Test
    fun testRouteCalculationWithOneWaypoint() {

        // calculate route with one waypoint, should fail
        `when`(mockPositioningManager.hasValidPosition()).thenReturn(false)

        val mockRouteWaypoint = mock(RouteWaypoint::class.java)
        `when`(mockProvider.providesRouteWaypoint(anySafe())).thenReturn(mockRouteWaypoint)

        val entry = mock(WaypointEntry::class.java)
        `when`(entry.routeWaypoint).thenReturn(mockRouteWaypoint)

        presenter.setWaypoint(entry)

        presenter.doSetup()
        verify(mockRoutePreview).onProgress(visible = true)
        verify(mockRoutePreview).onProgress(visible = false)
        verify(mockRoutePreview).routingFailed(anyString())
    }

    @Test
    fun testRouteCalculationWithTwoWaypoint() {

        assertNotNull(presenter.provider)
        val mockRouteWaypoint = mock(RouteWaypoint::class.java, RETURNS_DEEP_STUBS)
        `when`(mockRouteWaypoint.originalPosition.isValid).thenReturn(true)
        `when`(mockProvider.providesRouteWaypoint(anySafe())).thenReturn(mockRouteWaypoint)
        val mockRoutePlan = mock(RoutePlan::class.java, RETURNS_DEEP_STUBS)
        `when`(mockProvider.provideRoutePlan()).thenReturn(mockRoutePlan)
        val mockCoreRouter = mock(CoreRouter::class.java, RETURNS_DEEP_STUBS)
        `when`(mockProvider.providesCoreRouter()).thenReturn(mockCoreRouter)

        `when`(mockPositioningManager.hasValidPosition()).thenReturn(true)

        val entry = mock(WaypointEntry::class.java)
        `when`(entry.routeWaypoint).thenReturn(mockRouteWaypoint)

        presenter.setWaypoint(entry)

        presenter.doSetup()
        verify(mockRoutePreview).onProgress(visible = true)
        verify(mockRoutePlan, times(2)).addWaypoint(any(RouteWaypoint::class.java))

        val captor = argumentCaptor<CoreRouter.Listener>()
        verify(mockCoreRouter).calculateRoute(anySafe(), captor.capture())
        captor.value.onCalculateRouteFinished(listOf(mock(RouteResult::class.java, RETURNS_DEEP_STUBS)), RoutingError.NONE)

        verify(mockRoutePreview).onProgress(visible = false)

        verify(mockRoutePreview).populateUI(anySafe(), anySafe(), anyBoolean(), anyBoolean())
    }

    @Test
    fun testRouteCalculationWithTwoWaypointWhenRoutingFail() {

        val mockRouteWaypoint = mock(RouteWaypoint::class.java, RETURNS_DEEP_STUBS)
        `when`(mockRouteWaypoint.originalPosition.isValid).thenReturn(true)
        `when`(mockProvider.providesRouteWaypoint(anySafe())).thenReturn(mockRouteWaypoint)
        val mockRoutePlan = mock(RoutePlan::class.java, RETURNS_DEEP_STUBS)
        `when`(mockProvider.provideRoutePlan()).thenReturn(mockRoutePlan)
        val mockCoreRouter = mock(CoreRouter::class.java, RETURNS_DEEP_STUBS)
        `when`(mockProvider.providesCoreRouter()).thenReturn(mockCoreRouter)

        `when`(mockPositioningManager.hasValidPosition()).thenReturn(true)

        val entry = mock(WaypointEntry::class.java)
        `when`(entry.routeWaypoint).thenReturn(mockRouteWaypoint)

        presenter.setWaypoint(entry)

        presenter.doSetup()
        verify(mockRoutePreview).onProgress(visible = true)
        verify(mockRoutePlan, times(2)).addWaypoint(any(RouteWaypoint::class.java))

        val captor = argumentCaptor<CoreRouter.Listener>()
        verify(mockCoreRouter).calculateRoute(anySafe(), captor.capture())
        captor.value.onCalculateRouteFinished(listOf(mock(RouteResult::class.java, RETURNS_DEEP_STUBS)),
                RoutingError.INVALID_PARAMETERS)

        verify(mockRoutePreview).onProgress(visible = false)

        verify(mockRoutePreview).routingFailed(anyString())
    }

    @Test
    fun testStartGuidance() {
        testRouteCalculationWithTwoWaypoint()
        val mockBaseApplication = mock(MSDKUIApplication::class.java)
        `when`(mockActivity.applicationContext).thenReturn(mockBaseApplication)
        presenter.startGuidance(false)
        verify(mockActivity).startActivity(anySafe())
    }

    @Test
    fun toggleSteps() {
        presenter.toggleSteps()
        verify(mockRoutePreview).toggleSteps(true)
    }

    @Test
    fun testPopulateUI() {
        presenter.doSetup()  // now the error messages is filled
        presenter.populateUI()
        // route is null so routing fail should be notified.
        verify(mockRoutePreview, times(1)).routingFailed(ArgumentMatchers.anyString())
    }

    @Test
    fun testGeocodingCodingWhenSettingRoute() {

        val mockReq = mock(ReverseGeocodeRequest2::class.java)
        `when`(mockProvider.providesReverseGeocodeRequest(anySafe())).thenReturn(mockReq)
        val captor = argumentCaptor<ResultListener<Location>>()
        with(presenter) {
            val mockRoute = mock(Route::class.java)
            `when`(mockRoute.destination).thenReturn(mock(GeoCoordinate::class.java))
            setRoute(mockRoute, false)
            doSetup()
        }
        verify(mockRoutePreview).onProgress(visible = true)
        verify(mockProvider).providesReverseGeocodeRequest(anySafe())
        verify(mockReq).execute(captor.capture())
        captor.value.onCompleted(mock(Location::class.java, RETURNS_DEEP_STUBS), ErrorCode.NONE)
        verify(mockRoutePreview).onProgress(eq(false))
        verify(mockRoutePreview).populateUI(anySafe(), anySafe(), anyBoolean(), anyBoolean())
    }

    @Test
    fun testStartSimulation() {
        presenter.onAttach(applicationContext, mockContract)
        presenter.showStartSimulationAlertDialog()
        val alertDialog = ShadowAlertDialog.getLatestAlertDialog()
        assertNotNull(alertDialog)
    }
}