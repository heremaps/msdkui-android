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

package com.here.msdkuiapp.common.mapselection

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.common.GeoCoordinate
import com.here.msdkui.common.ThemeUtil
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.R
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.android.synthetic.main.waypoint_selection.view.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric

/**
 * Tests for [WaypointSelectionFragment].
 */
class WaypointSelectionFragmentTest : BaseTest() {

    private lateinit var fragment: WaypointSelectionFragment

    @Mock
    private lateinit var presenter: WaypointSelectionPresenter

    @Before
    public override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        fragment = Robolectric.buildFragment(WaypointSelectionFragment::class.java).create().get()
        fragment.presenter = presenter
    }

    @Test
    public fun testInit() {
        assertNotNull(fragment.presenter)
        val inflater = mock(LayoutInflater::class.java)
        val mockView = mock(View::class.java)
        `when`(inflater.inflate(anyInt(), any(), anyBoolean())).thenReturn(mockView)
        fragment.onCreateView(inflater, mock(ViewGroup::class.java), null)
        verify(inflater).inflate(anyInt(), any(), ArgumentMatchers.anyBoolean())
    }

    @Test
    public fun testUiInit() {
        doAnswer { fragment.onUiUpdate("test", true) }.`when`(presenter).updateUI()
        fragment.onViewCreated(mock(View::class.java), null)
        verify(presenter).onAttach(anySafe(), anySafe())
        verify(presenter).setUpActionBar(anySafe())
        verify(presenter).updateUI()
        // update ui should change the background color as it wil be called after geocoding.
        val background = fragment.view.ws_parent.background
        assertTrue(background is ColorDrawable)
        assertThat((background as ColorDrawable).color, `is`(ThemeUtil.getColor(activityContext, R.attr.colorAccent)))
        assertThat(fragment.view.ws_parent.visibility, `is`(View.VISIBLE))
        assertNotNull(fragment.getCurrentViewContract())
    }

    @Test
    public fun testUpdatePositions() {
        fragment.updateCord(mock(GeoCoordinate::class.java))
        verify(presenter).updateCord(anySafe())
        fragment.updatePosition(entry = mock(WaypointEntry::class.java))
        verify(presenter).updatePosition(anyInt(), anySafe())
    }
}