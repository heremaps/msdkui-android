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

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.common.GeoCoordinate
import com.here.msdkui.common.ThemeUtil
import com.here.msdkuiapp.R
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import kotlinx.android.synthetic.main.waypoint_selection.view.*
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric

/**
 * Test for [GuidanceWaypointSelectionFragment].
 */
class GuidanceWaypointSelectionFragmentTest : BaseTest() {

    private lateinit var fragment: GuidanceWaypointSelectionFragment

    @Mock
    private lateinit var presenter: GuidanceWaypointSelectionPresenter

    @Before
    public override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        fragment = GuidanceWaypointSelectionFragment.newInstance()
        doAnswer { fragment.onUiUpdate("test", withColor = true) }.`when`(presenter).updateUI()
        fragment.presenter = presenter
        addFrag(fragment)
    }

    @Test
    fun testOnCreate() {
        verify(presenter).onAttach(anySafe(), anySafe())
    }

    @Test
    fun testInit() {
        assertNotNull(fragment.presenter)
        val inflater = mock(LayoutInflater::class.java)
        val mockView = mock(View::class.java)
        `when`(inflater.inflate(Mockito.anyInt(), ArgumentMatchers.any(), anyBoolean())).thenReturn(mockView)
        fragment.onCreateView(inflater, mock(ViewGroup::class.java), null)
        verify(inflater).inflate(Mockito.anyInt(), ArgumentMatchers.any(), ArgumentMatchers.anyBoolean())
    }

    @Test
    fun testUiInit() {
        verify(presenter).setUpActionBar(anySafe())
        verify(presenter).updateUI()
        // update ui should change the background color as it wil be called after geocoding.
        val background = fragment.view!!.ws_parent.background
        assertTrue(background is ColorDrawable)
        MatcherAssert.assertThat((background as ColorDrawable).color, Matchers.`is`(ThemeUtil.getColor(activityContext, R.attr.colorAccent)))
        MatcherAssert.assertThat(fragment.view!!.ws_parent.visibility, Matchers.`is`(View.VISIBLE))
        assertNotNull(fragment.getCurrentViewContract())
    }

    @Test
    fun testUpdateCord() {
        val geoCord = mock(GeoCoordinate::class.java)
        fragment.updateCord(geoCord)
        verify(presenter).updateCord(geoCord)
    }
}