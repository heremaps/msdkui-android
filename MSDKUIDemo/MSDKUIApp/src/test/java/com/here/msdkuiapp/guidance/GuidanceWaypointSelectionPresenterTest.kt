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

package com.here.msdkuiapp.guidance

import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.search.*
import com.here.msdkuiapp.GuidanceContracts
import com.here.msdkuiapp.base.BaseContract
import com.here.msdkuiapp.common.AppActionBar
import com.here.msdkuiapp.common.Provider
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import com.here.msdkuiapp.position.AppPositioningManager
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Tests for [GuidanceWaypointSelectionPresenter].
 */
class GuidanceWaypointSelectionPresenterTest : BaseTest() {

    private lateinit var presenter: GuidanceWaypointSelectionPresenter

    @Mock
    private lateinit var mockContractImpl: BaseContract<GuidanceContracts.GuidanceWaypointSelection>

    @Mock
    private lateinit var mockContract: GuidanceContracts.GuidanceWaypointSelection

    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockProvider: Provider

    @Mock
    private lateinit var mockCoordinatorListener: GuidanceWaypointSelectionFragment.Listener

    @Mock
    private lateinit var mockPositioningManager: AppPositioningManager

    @Before
    override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        presenter = GuidanceWaypointSelectionPresenter()
        presenter.provider = mockProvider
        `when`(mockContractImpl.getCurrentViewContract()).thenReturn(mockContract)
        presenter.onAttach(activityContext!!, mockContractImpl)
        presenter.coordinatorListener = mockCoordinatorListener
        appPositioningManager = mockPositioningManager
    }

    @Test
    fun testActionBarAndClickOnRightIcon() {
        val actionBar = mock(AppActionBar::class.java)
        presenter.setUpActionBar(actionBar)
        verify(actionBar).setBack(anyBoolean(), anyInt(), anySafe())
        verify(actionBar).setTitle(anyBoolean(), anyString(), anyBoolean())
        val clickListenerCaptor = argumentCaptor<() -> Unit>()
        verify(actionBar).setRightIcon(anyBoolean(), anyInt(), anyString(), clickListenerCaptor.capture())

        val mockCord = mock(GeoCoordinate::class.java)
        `when`(mockCord.isValid).thenReturn(true)
        presenter.updateCord(mockCord)
        clickListenerCaptor.value.invoke()
        verify(mockCoordinatorListener).onWaypointSelected(anySafe())
    }

    @Test
    fun testOnLocationReady() {
        presenter.onLocationReady()
        verify(mockContract).onUiUpdate(anyString(), eq(false), eq(false))
        verify(mockPositioningManager).initPositioning(anySafe())
    }

    @Test
    fun testUpdateCord() {
        val mockCord = mock(GeoCoordinate::class.java)
        val request = mock(ReverseGeocodeRequest::class.java)

        `when`(mockCord.isValid).thenReturn(true)
        `when`(mockProvider.providesReverseGeocodeRequest(mockCord)).thenReturn(request)
        presenter.updateCord(mockCord)
        verify(mockProvider).providesRouteWaypoint(mockCord)
        verify(mockContract).onProgress(eq(true))

        val captor = argumentCaptor<ResultListener<Location>>()
        verify(request).execute(captor.capture())

        captor.value.onCompleted(mock(Location::class.java, RETURNS_DEEP_STUBS), ErrorCode.NONE)
        verify(mockContract).onProgress(eq(false))
        verify(mockContract).onUiUpdate(anyString(), anyBoolean(), anyBoolean())
    }

    @Test
    fun testUpdateUi() {
        val mockCord = mock(GeoCoordinate::class.java)
        `when`(mockCord.isValid).thenReturn(true)
        presenter.updateCord(mockCord)
        presenter.updateUI()
        verify(mockContract).onUiUpdate(anyString(), eq(true), eq(true))
    }
}