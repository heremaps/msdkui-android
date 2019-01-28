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

import com.here.android.mpa.guidance.NavigationManager
import com.here.android.mpa.routing.Route
import com.here.msdkui.guidance.GuidanceManeuverData
import com.here.msdkui.guidance.GuidanceManeuverView
import com.here.msdkui.guidance.GuidanceManeuverPresenter
import com.here.msdkuiapp.common.Util
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.testutils.BaseTest
import junit.framework.Assert.assertNotNull
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Tests for [GuidanceManeuverFragment].
 */
class GuidanceManeuverViewFragmentTest : BaseTest() {

    private lateinit var guidanceManeuverFragment: GuidanceManeuverFragment

    @Mock
    private lateinit var mockPresenter: GuidanceManeuverPresenter

    @Before
    fun setup() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        navigationManager = mock(NavigationManager::class.java)
        guidanceManeuverFragment = GuidanceManeuverFragment.newInstance()
    }

    @Test
    fun testPanelCreation() {
        guidanceManeuverFragment.route = mock(Route::class.java)
        addFrag(guidanceManeuverFragment, GuidanceManeuverFragment::class.java.name)
        assertNotNull(guidanceManeuverFragment)
        assertNotNull(guidanceManeuverFragment.view)
        assertEquals(
                (guidanceManeuverFragment.view as GuidanceManeuverView).unitSystem,
                Util.getLocaleUnit())
    }

    @Test
    fun testSetterGetter() {
        guidanceManeuverFragment.route = mock(Route::class.java)
        assertNotNull(guidanceManeuverFragment.route)
    }

    @Test
    fun testCallbacks() {
        // calling onManeuverData will update panel
        guidanceManeuverFragment.route = mock(Route::class.java)
        addFrag(guidanceManeuverFragment, GuidanceManeuverFragment::class.java.name)
        val data = mock(GuidanceManeuverData::class.java)
        guidanceManeuverFragment.onDataChanged(data)
        assertNotNull((guidanceManeuverFragment.view as GuidanceManeuverView).viewState)
        guidanceManeuverFragment.onDestinationReached()
    }

    @Test
    fun testPauseResume() {
        guidanceManeuverFragment.presenter = mockPresenter
        assertNotNull(guidanceManeuverFragment.presenter)
        guidanceManeuverFragment.onResume()
        verify(mockPresenter).resume()
        guidanceManeuverFragment.onPause()
        verify(mockPresenter).pause()
    }
}