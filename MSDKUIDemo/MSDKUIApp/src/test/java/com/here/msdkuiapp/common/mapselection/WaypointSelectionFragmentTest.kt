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

package com.here.msdkuiapp.common.mapselection

import android.support.v7.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.here.android.mpa.common.GeoCoordinate
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.R
import com.here.msdkuiapp.baseActivity
import com.here.msdkuiapp.map.MapFragmentWrapper
import com.here.msdkuiapp.routing.RoutingActivity
import com.here.msdkuiapp.routing.RoutingCoordinator
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyBoolean
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Tests for [WaypointSelectionFragment].
 */
class WaypointSelectionFragmentTest {

    private lateinit var fragment: WaypointSelectionFragment

    @Mock
    private lateinit var mockPresenter: WaypointSelectionPresenter

    @Mock
    private lateinit var mockMapFragment: MapFragmentWrapper

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockActivity: RoutingActivity

    @Mock
    private lateinit var mockView : View

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        fragment = spy(WaypointSelectionFragment.newInstance()).apply {
            presenter = mockPresenter
            mapFragment = mockMapFragment
            activityInstance = mockActivity
            `when`(baseActivity).thenReturn(mockActivity)
            `when`(view).thenReturn(mockView)
        }
        with(fragment) {
            assertNotNull(presenter)
            assertNotNull(mapFragment)
            assertNotNull(mapFragment)
        }
    }

    @Test
    fun testInit() {
        val inflater = mock(LayoutInflater::class.java)
        val mockView = mock(View::class.java)
        `when`(inflater.inflate(anyInt(), any(), anyBoolean())).thenReturn(mockView)
        fragment.onCreateView(inflater, mock(ViewGroup::class.java), null)
        verify(inflater).inflate(anyInt(), any(), ArgumentMatchers.anyBoolean())
    }

    @Test
    fun testOnViewCreated() {
        fragment.onViewCreated(mock(View::class.java), null)
        verify(mockPresenter).updateUI()
        val captor = argumentCaptor<() -> Unit>()
        verify(mockMapFragment).start(captor.capture())
        captor.value.invoke()
        verify(mockMapFragment).onTouch(ArgumentMatchers.anyBoolean(), anySafe())
    }

    @Test
    fun testTrafficGetSet() {
        fragment.traffic = true
        verify(fragment.presenter).trafficMode = true
        `when`(fragment.presenter.trafficMode).thenReturn(false)
        assertFalse(fragment.traffic)
    }

    @Test
    fun testUpdatePositions() {
        fragment.setGeoCoordinateForWaypoint(mock(GeoCoordinate::class.java))
        verify(mockPresenter).setGeoCoordinateForWaypoint(anySafe())
        fragment.updatePosition(entry = mock(WaypointEntry::class.java))
        verify(mockPresenter).updatePosition(anyInt(), anySafe())
    }

    @Test
    fun testOnPointSelectedOnMap() {
        val cords = mock(GeoCoordinate::class.java)
        fragment.onPointSelectedOnMap(cords)
        verify(mockPresenter).setGeoCoordinateForWaypoint(cords)
    }

    @Test
    fun testOnBackClicked() {
        val entry = mock(WaypointEntry::class.java)
        val mockRoutingCoordinator = mock(RoutingCoordinator::class.java)
        `when`(mockActivity.coordinator).thenReturn(mockRoutingCoordinator)
        fragment.onBackClicked(8, entry)
        verify(mockRoutingCoordinator).onWaypointSelectionCancelled(8, entry)
    }

    @Test
    fun testOnRightIconClick() {
        val entry = mock(WaypointEntry::class.java)
        `when`(entry.isValid).thenReturn(true)
        val mockRoutingCoordinator = mock(RoutingCoordinator::class.java)
        `when`(mockActivity.coordinator).thenReturn(mockRoutingCoordinator)
        fragment.onRightIconClicked(null, entry)
        verify(mockRoutingCoordinator).onWaypointSelected(null, entry)
    }

    @Test
    fun testOnUiUpdate() {
        `when`(mockView.findViewById<View>(R.id.selected_waypoint_label)).thenReturn(mock(TextView::class.java))
        val mockView1 = mock(RelativeLayout::class.java)
        `when`(mockView.findViewById<View>(R.id.ws_container)).thenReturn(mockView1)

        val mockImageView = mockRightIcon()
        fragment.onUiUpdate("", true)
        verify(mockView1).setBackgroundColor(ArgumentMatchers.anyInt())
        verify(mockImageView).setColorFilter(ArgumentMatchers.anyInt(), anySafe())

        fragment.onUiUpdate("", false)
        verify(mockImageView, times(2)).setColorFilter(ArgumentMatchers.anyInt(), anySafe())
    }

    @Test
    fun testOnProgressBehaviour() {
        val mockImageView = mockRightIcon()
        fragment.onProgress(true)
        verify(mockImageView).isEnabled = false
        fragment.onProgress(false)
        verify(mockImageView).isEnabled = true
    }

    private fun mockRightIcon() : ImageView {
        val mockView2 = mock(View::class.java)
        val mockImageView = mock(ImageView::class.java)
        `when`(mockView2.findViewById<ImageView>(R.id.ac_right_icon)).thenReturn(mockImageView)
        val mockActionBar = mock(ActionBar::class.java)
        `when`(mockActionBar.customView).thenReturn(mockView2)
        `when`(mockActivity.supportActionBar).thenReturn(mockActionBar)
        return mockImageView
    }
}