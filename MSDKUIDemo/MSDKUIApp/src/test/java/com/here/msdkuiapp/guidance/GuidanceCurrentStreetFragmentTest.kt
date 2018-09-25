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

import com.here.android.mpa.routing.Route
import com.here.msdkui.guidance.GuidanceCurrentStreet
import com.here.msdkui.guidance.GuidanceCurrentStreetData
import com.here.msdkui.guidance.GuidanceCurrentStreetPresenter
import com.here.testutils.BaseTest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Tests for [GuidanceCurrentStreetFragment].
 */
class GuidanceCurrentStreetFragmentTest : BaseTest() {

    lateinit var guidanceCurrentStreetFragment: GuidanceCurrentStreetFragment

    @Before
    override fun setUp() {
        super.setUp()
        guidanceCurrentStreetFragment = GuidanceCurrentStreetFragment.newInstance()
    }

    @Test
    fun testCurrentStreetFragmentCreation() {
        guidanceCurrentStreetFragment.route = mock(Route::class.java)
        addFrag(guidanceCurrentStreetFragment, GuidanceCurrentStreetFragment::class.java.name)
        assertNotNull(guidanceCurrentStreetFragment)
        assertNotNull(guidanceCurrentStreetFragment.view)
    }

    @Test
    fun testRouteSetGet() {
        guidanceCurrentStreetFragment.route = mock(Route::class.java)
        assertNotNull(guidanceCurrentStreetFragment.route)
    }

    @Test
    fun testCallbacks() {
        val streetName : String = "StreetName"
        guidanceCurrentStreetFragment.route = mock(Route::class.java)
        addFrag(guidanceCurrentStreetFragment, GuidanceCurrentStreetFragment::class.java.name)
        val data = mock(GuidanceCurrentStreetData::class.java)
        `when`(data.currentStreetName).thenReturn(streetName)
        guidanceCurrentStreetFragment.onDataChanged(data)
        assertEquals(streetName, (guidanceCurrentStreetFragment.view as GuidanceCurrentStreet).guidanceCurrentStreetData.currentStreetName)
    }

    @Test
    fun testPausingAndResuming() {
        val presenter : GuidanceCurrentStreetPresenter = mock(GuidanceCurrentStreetPresenter::class.java)
        guidanceCurrentStreetFragment.mPanelPresenter = presenter
        guidanceCurrentStreetFragment.onPause()
        verify(presenter).pause()

        guidanceCurrentStreetFragment.onResume()
        verify(presenter).resume()
    }
}
