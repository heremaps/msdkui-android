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

package com.here.msdkuiapp.guidance

import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.WaypointEntry
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.TestCase.assertNotNull
import kotlinx.android.synthetic.main.guidance_route_preview.view.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.shadows.ShadowAlertDialog

/**
 * Tests for [GuidanceRoutePreviewFragment]
 */
class GuidanceRoutePreviewFragmentTest : BaseTest() {

    private lateinit var fragment: GuidanceRoutePreviewFragment

    @Mock
    private lateinit var presenter: GuidanceRoutePreviewFragmentPresenter

    @Mock
    private lateinit var mockContext: Context

    @Before
    public override fun setUp() {
        MockitoAnnotations.initMocks(this)
        fragment = Robolectric.buildFragment(GuidanceRoutePreviewFragment::class.java).create().get()
        fragment.presenter = presenter
    }

    @Test
    public fun testInit() {
        assertNotNull(fragment.presenter)

        // first time
        fragment.onViewCreated(mock(View::class.java), null)
        with(verify(presenter)) {
            onAttach(anySafe(), anySafe())
            calculateRoute()
            updateActionBar(anySafe())
        }

        `when`(presenter.context).thenReturn(mockContext)
        // call again
        fragment.onViewCreated(mock(View::class.java), null)
        verify(presenter).populateUI()

        assertNotNull(fragment.getCurrentViewContract())

        fragment.view.go.performClick()
        verify(presenter).startGuidance(false)

        fragment.view.go.performLongClick()
        val alertDialog = ShadowAlertDialog.getLatestAlertDialog()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
        verify(presenter).startGuidance(true)
    }

    @Test
    fun testProgressBarVisibility() {
        assertNotNull(fragment.view)
        fragment.onProgress(true)
        assertThat(fragment.view.route_preview_progress_bar.visibility, `is`(View.VISIBLE))
        fragment.onProgress(false)
        assertThat(fragment.view.route_preview_progress_bar.visibility, `is`(View.GONE))
    }

    @Test
    fun testPopulateUI() {
        val entry = mock(WaypointEntry::class.java)
        `when`(entry.name).thenReturn("name")
        val route = mock(Route::class.java, RETURNS_DEEP_STUBS)
        fragment.populateUI(entry, route)
        assertThat(fragment.view.destination.text.toString(), containsString("name"))
    }

    @Test
    fun testRoutingFailed() {
        fragment.routingFailed("failed")
        assertThat(fragment.view.visibility, `is`(View.VISIBLE))
        assertThat(fragment.view.description.visibility, `is`(View.INVISIBLE))
        assertThat(fragment.view.error_message.visibility, `is`(View.VISIBLE))
    }
}