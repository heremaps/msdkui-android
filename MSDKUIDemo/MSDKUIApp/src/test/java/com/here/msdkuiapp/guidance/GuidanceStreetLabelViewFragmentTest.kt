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

import com.here.android.mpa.routing.Route
import com.here.msdkui.guidance.GuidanceStreetLabelView
import com.here.msdkui.guidance.GuidanceStreetLabelData
import com.here.msdkui.guidance.GuidanceStreetLabelPresenter
import com.here.testutils.BaseTest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Tests for [GuidanceStreetLabelFragment].
 */
class GuidanceStreetLabelViewFragmentTest : BaseTest() {

    private lateinit var guidanceStreetLabelFragment: GuidanceStreetLabelFragment

    @Before
    override fun setUp() {
        super.setUp()
        guidanceStreetLabelFragment = GuidanceStreetLabelFragment.newInstance()
    }

    @Test
    fun testCurrentStreetFragmentCreation() {
        guidanceStreetLabelFragment.route = mock(Route::class.java)
        guidanceStreetLabelFragment.presenter = mock(GuidanceStreetLabelPresenter::class.java)
        addFrag(guidanceStreetLabelFragment, GuidanceStreetLabelFragment::class.java.name)
        assertNotNull(guidanceStreetLabelFragment)
        assertNotNull(guidanceStreetLabelFragment.view)
    }

    @Test
    fun testRouteSetGet() {
        guidanceStreetLabelFragment.route = mock(Route::class.java)
        assertNotNull(guidanceStreetLabelFragment.route)
    }

    @Test
    fun testCallbacks() {
        val streetName : String = "StreetName"
        guidanceStreetLabelFragment.route = mock(Route::class.java)
        guidanceStreetLabelFragment.presenter = mock(GuidanceStreetLabelPresenter::class.java)
        addFrag(guidanceStreetLabelFragment,
                GuidanceStreetLabelFragment::class.java.name)
        val data = mock(GuidanceStreetLabelData::class.java)
        `when`(data.currentStreetName).thenReturn(streetName)
        guidanceStreetLabelFragment.onDataChanged(data)
        assertEquals(streetName, (guidanceStreetLabelFragment.view as GuidanceStreetLabelView).guidanceCurrentStreetData.currentStreetName)
    }

    @Test
    fun testPausingAndResuming() {
        val labelPresenter : GuidanceStreetLabelPresenter = mock(GuidanceStreetLabelPresenter::class.java)
        guidanceStreetLabelFragment.presenter = labelPresenter
        guidanceStreetLabelFragment.onPause()
        verify(labelPresenter).pause()

        guidanceStreetLabelFragment.onResume()
        verify(labelPresenter).resume()
    }
}
