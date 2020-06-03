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

package com.here.msdkuiapp.common.routepreview

import MockUtils.mockRoute
import android.content.Context
import android.os.Bundle
import android.view.View
import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.R
import com.here.msdkuiapp.common.Constant
import com.here.msdkuiapp.common.Util
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.android.synthetic.main.guidance_route_preview.view.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Tests for [RoutePreviewFragment]
 */
class RoutePreviewFragmentTest : BaseTest() {

    private lateinit var fragment: RoutePreviewFragment

    @Mock
    private lateinit var mockPresenter: RoutePreviewFragmentPresenter

    @Mock
    private lateinit var mockContext: Context

    @Before
    public override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        fragment = RoutePreviewFragment.newInstance()
        fragment.presenter = mockPresenter
        assertEquals(fragment.rootViewExist(), false)
        addFrag(fragment)
    }

    @Test
    public fun testInit() {
        assertNotNull(fragment.presenter)

        with(verify(mockPresenter)) {
            onAttach(anySafe(), anySafe())
            doSetup()
            updateActionBar(anySafe())
        }

        `when`(mockPresenter.context).thenReturn(mockContext)
        // call again
        fragment.onViewCreated(mock(View::class.java), null)
        verify(mockPresenter).populateUI()

        assertNotNull(fragment.getCurrentViewContract())

        fragment.view!!.go.performClick()
        verify(mockPresenter).startGuidance(false)

        fragment.view!!.go.performLongClick()
        verify(mockPresenter).showStartSimulationAlertDialog()
    }

    @Test
    fun testInitWithBundle() {
        val bundle = Bundle().apply {
            putInt(Constant.GO_VISIBILITY, View.GONE)
        }
        `when`(mockPresenter.onAttach(anySafe(), anySafe())).thenReturn(true)
        fragment = RoutePreviewFragment.newInstance().apply {
            arguments = bundle
            presenter = mockPresenter
        }
        addFrag(fragment)
        assertNotNull(fragment.view)
        assertThat(fragment.view!!.go.visibility, `is`(View.GONE))
    }

    @Test
    fun testProgressBarVisibility() {
        assertNotNull(fragment.view)
        fragment.onProgress(true)
        assertThat(fragment.view!!.route_preview_progress_bar.visibility, `is`(View.VISIBLE))
        fragment.onProgress(false)
        assertThat(fragment.view!!.route_preview_progress_bar.visibility, `is`(View.GONE))
    }

    @Test
    fun testPopulateUI() {
        val entry = WaypointEntry("name")
        fragment.populateUI(entry, mockRoute(), false)
        assertThat(fragment.view!!.destination.text.toString(), containsString("name"))
        assertEquals(fragment.view!!.description.unitSystem, Util.getLocaleUnit())
        assertEquals(fragment.view!!.guidance_maneuver_list.unitSystem, Util.getLocaleUnit())
    }

    @Test
    fun testRoutingFailed() {
        fragment.routingFailed("failed")
        with(fragment.view!!) {
            assertThat(visibility, `is`(View.VISIBLE))
            assertThat(description.visibility, `is`(View.INVISIBLE))
            assertThat(error_message.visibility, `is`(View.VISIBLE))
        }
    }

    @Test
    fun testRootViewExist() {
        assertEquals(fragment.rootViewExist(), true)
    }

    @Test
    fun testManeuverSteps() {
        `when`(mockPresenter.context).thenReturn(mockContext)
        fragment.onViewCreated(mock(View::class.java), null)
        assertNotNull(fragment.view)
        val seeStepView = fragment.view!!.see_steps
        assertThat(seeStepView.text.toString(),
                `is`(applicationContext.getString(R.string.msdkui_app_guidance_button_showmaneuvers)))
        assertThat(fragment.view!!.guidance_maneuver_list.visibility, `is`(View.GONE))
        fragment.toggleSteps(true)
        assertThat(seeStepView.text.toString(),
                `is`(applicationContext.getString(R.string.msdkui_app_guidance_button_showmap)))
        assertThat(fragment.view!!.guidance_maneuver_list.visibility, `is`(View.VISIBLE))
    }

    @Test
    fun testSettingWaypoint() {
        val entry = mock(WaypointEntry::class.java)
        fragment.setWaypoint(entry, true)
        verify(mockPresenter).setWaypoint(anySafe(), ArgumentMatchers.eq(true))
    }

    @Test
    fun testSettingRoute() {
        val route = mock(Route::class.java)
        fragment.setRoute(route, true)
        verify(mockPresenter).setRoute(anySafe(), ArgumentMatchers.eq(true))
    }
}