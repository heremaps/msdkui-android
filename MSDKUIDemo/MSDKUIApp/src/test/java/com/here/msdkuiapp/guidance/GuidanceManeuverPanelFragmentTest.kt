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
import com.here.msdkui.guidance.GuidanceManeuverData
import com.here.msdkui.guidance.GuidanceManeuverPanel
import com.here.msdkuiapp.GuidanceContracts
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.testutils.BaseTest
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Tests for [GuidanceManeuverPanelFragment].
 */
class GuidanceManeuverPanelFragmentTest : BaseTest() {

    lateinit var guidanceManeuverPanelFragment: GuidanceManeuverPanelFragment

    @Before
    fun setup() {
        super.setUp()
        navigationManager = mock(NavigationManager::class.java)
        guidanceManeuverPanelFragment = GuidanceManeuverPanelFragment.newInstance()
    }

    @Test
    fun testPanelCreation() {
        guidanceManeuverPanelFragment.route = mock(Route::class.java)
        addFrag(guidanceManeuverPanelFragment, GuidanceManeuverPanelFragment::class.java.name)
        assertNotNull(guidanceManeuverPanelFragment)
        assertNotNull(guidanceManeuverPanelFragment.view)
    }

    @Test
    fun testSetterGetter() {
        guidanceManeuverPanelFragment.route = mock(Route::class.java)
        assertNotNull(guidanceManeuverPanelFragment.route)
    }

    @Test
    fun testCallbacks() {
        // calling onManeuverData will update panel
        guidanceManeuverPanelFragment.route = mock(Route::class.java)
        addFrag(guidanceManeuverPanelFragment, GuidanceManeuverPanelFragment::class.java.name)
        val data = mock(GuidanceManeuverData::class.java)
        guidanceManeuverPanelFragment.onDataChanged(data)
        assertNotNull((guidanceManeuverPanelFragment.view as GuidanceManeuverPanel).maneuverData)
        guidanceManeuverPanelFragment.onDestinationReached()
    }
}