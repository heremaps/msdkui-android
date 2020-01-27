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

package com.here.msdkuiapp.guidance

import android.view.View
import com.here.android.mpa.guidance.NavigationManager
import com.here.android.mpa.routing.Route
import com.here.msdkui.guidance.GuidanceNextManeuverData
import com.here.msdkui.guidance.GuidanceNextManeuverView
import com.here.msdkui.guidance.GuidanceNextManeuverPresenter
import com.here.msdkuiapp.common.Util
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.testutils.BaseTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Tests for [GuidanceNextManeuverFragment]
 */
class GuidanceNextManeuverViewFragmentTest : BaseTest() {

    private lateinit var guidanceNextManeuverFragment: GuidanceNextManeuverFragment

    @Mock
    private lateinit var mockPresenter: GuidanceNextManeuverPresenter

    @Before
    fun setup() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        navigationManager = mock(NavigationManager::class.java)
        guidanceNextManeuverFragment = GuidanceNextManeuverFragment.newInstance()
        guidanceNextManeuverFragment.presenter = mockPresenter
    }

    @Test
    fun testPanelCreation() {
        guidanceNextManeuverFragment.route = mock(Route::class.java)
        addFrag(guidanceNextManeuverFragment,
                GuidanceNextManeuverFragment::class.java.name)
        assertNotNull(guidanceNextManeuverFragment)
        assertNotNull(guidanceNextManeuverFragment.view)
        assertEquals(
                (guidanceNextManeuverFragment.view as GuidanceNextManeuverView).unitSystem,
                Util.getLocaleUnit())
    }

    @Test
    fun testAttachingPresenter() {
        guidanceNextManeuverFragment.presenter = null
        addFrag(guidanceNextManeuverFragment,
                GuidanceNextManeuverFragment::class.java.name)
        assertNotNull(guidanceNextManeuverFragment.presenter)
    }

    @Test
    fun testSetterGetterRoute() {
        guidanceNextManeuverFragment.route = mock(Route::class.java)
        assertNotNull(guidanceNextManeuverFragment.route)
    }

    @Test
    fun testSetterGetterPanelPresenter() {
        guidanceNextManeuverFragment.presenter = mockPresenter
        assertNotNull(guidanceNextManeuverFragment.presenter)
    }

    @Test
    fun testOnPause() {
        guidanceNextManeuverFragment.onPause()
        verify(mockPresenter).pause()
    }

    @Test
    fun testOnResume() {
        guidanceNextManeuverFragment.onResume()
        verify(mockPresenter).resume()
    }

    @Test
    fun testCallbacks() {
        // calling onManeuverData will update panel
        guidanceNextManeuverFragment.route = mock(Route::class.java)
        addFrag(guidanceNextManeuverFragment,
                GuidanceNextManeuverFragment::class.java.name)
        val data = mock(GuidanceNextManeuverData::class.java)
        guidanceNextManeuverFragment.onDataChanged(data)
        assertEquals((guidanceNextManeuverFragment.view as GuidanceNextManeuverView).nextManeuverData, data)
        guidanceNextManeuverFragment.onDataChanged(null)
        assertThat((guidanceNextManeuverFragment.view as GuidanceNextManeuverView).visibility, `is`(View.GONE))
    }
}