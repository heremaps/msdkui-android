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

import com.here.android.mpa.guidance.NavigationManager
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewData
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewPresenter
import com.here.msdkui.guidance.GuidanceEstimatedArrivalView
import com.here.msdkuiapp.common.Util
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.testutils.BaseTest
import junit.framework.Assert
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.util.*

/**
 * Tests for [GuidanceEstimatedArrivalFragment].
 */
class GuidanceEstimatedArrivalFragmentTest :BaseTest() {

    lateinit var guidanceEstimatedArrivalFragment: GuidanceEstimatedArrivalFragment

    @Before
    fun setup() {
        super.setUp()
        navigationManager = mock(NavigationManager::class.java)
        guidanceEstimatedArrivalFragment = GuidanceEstimatedArrivalFragment.newInstance()
    }

    @Test
    fun testPanelCreation() {
        addFrag(guidanceEstimatedArrivalFragment,
                GuidanceEstimatedArrivalFragment::class.java.name)
        assertNotNull(guidanceEstimatedArrivalFragment)
        assertNotNull(guidanceEstimatedArrivalFragment.view)
        assertEquals(
                (guidanceEstimatedArrivalFragment.view as GuidanceEstimatedArrivalView).unitSystem,
                Util.getLocaleUnit())
    }

    @Test
    fun testOnPause() {
        guidanceEstimatedArrivalFragment.viewPresenter = mock(GuidanceEstimatedArrivalViewPresenter::class.java)
        guidanceEstimatedArrivalFragment.onPause()
        verify(guidanceEstimatedArrivalFragment.viewPresenter!!).pause()
    }

    @Test
    fun testOnDataChangedCallback() {
        // calling onManeuverData will update panel
        addFrag(guidanceEstimatedArrivalFragment, GuidanceNextManeuverFragment::class.java.name)
        val data = mock(GuidanceEstimatedArrivalViewData::class.java)
        val date = mock(Date::class.java)
        `when`(data.eta).thenReturn(date)
        guidanceEstimatedArrivalFragment.onDataChanged(data)
        Assert.assertNotNull((guidanceEstimatedArrivalFragment.view as GuidanceEstimatedArrivalView).estimatedArrivalData)
    }
}