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

import com.here.android.mpa.guidance.NavigationManager
import com.here.android.mpa.routing.Route
import com.here.msdkui.guidance.GuidanceNextManeuverData
import com.here.msdkui.guidance.GuidanceNextManeuverPanel
import com.here.msdkui.guidance.GuidanceNextManeuverPanelPresenter
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.testutils.BaseTest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Tests for [GuidanceNextManeuverPanelFragment]
 */
class GuidanceNextManeuverPanelFragmentTest : BaseTest() {

    private lateinit var guidanceNextManeuverPanelFragment: GuidanceNextManeuverPanelFragment

    @Mock
    private lateinit var mockPresenter: GuidanceNextManeuverPanelPresenter

    @Before
    fun setup() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        navigationManager = mock(NavigationManager::class.java)
        guidanceNextManeuverPanelFragment = GuidanceNextManeuverPanelFragment.newInstance()
        guidanceNextManeuverPanelFragment.panelPresenter = mockPresenter
    }

    @Test
    fun testPanelCreation() {
        guidanceNextManeuverPanelFragment.route = mock(Route::class.java)
        addFrag(guidanceNextManeuverPanelFragment, GuidanceNextManeuverPanelFragment::class.java.name)
        assertNotNull(guidanceNextManeuverPanelFragment)
        assertNotNull(guidanceNextManeuverPanelFragment.view)
    }

    @Test
    fun testSetterGetterRoute() {
        guidanceNextManeuverPanelFragment.route = mock(Route::class.java)
        assertNotNull(guidanceNextManeuverPanelFragment.route)
    }

    @Test
    fun testSetterGetterPanelPresenter() {
        guidanceNextManeuverPanelFragment.panelPresenter = mockPresenter
        assertNotNull(guidanceNextManeuverPanelFragment.panelPresenter)
    }

    @Test
    fun testOnPause() {
        guidanceNextManeuverPanelFragment.onPause()
        verify(mockPresenter).pause()
    }

    @Test
    fun testOnResume() {
        guidanceNextManeuverPanelFragment.onResume()
        verify(mockPresenter).resume()
    }

    @Test
    fun testCallbacks() {
        // calling onManeuverData will update panel
        guidanceNextManeuverPanelFragment.route = mock(Route::class.java)
        addFrag(guidanceNextManeuverPanelFragment, GuidanceNextManeuverPanelFragment::class.java.name)
        val data = mock(GuidanceNextManeuverData::class.java)
        guidanceNextManeuverPanelFragment.onDataChanged(data)
        assertEquals((guidanceNextManeuverPanelFragment.view as GuidanceNextManeuverPanel).nextManeuverData, data)
    }
}