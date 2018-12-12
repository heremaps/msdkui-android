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

import com.here.android.mpa.common.PositioningManager
import com.here.android.mpa.guidance.NavigationManager
import com.here.msdkui.guidance.GuidanceSpeedData
import com.here.msdkui.guidance.GuidanceSpeedLimitView
import com.here.msdkui.guidance.GuidanceSpeedPresenter
import com.here.msdkuiapp.common.Util
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.msdkuiapp.guidance.SingletonHelper.positioningManager

import com.here.testutils.BaseTest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Tests for class [GuidanceSpeedLimitFragment].
 */
class GuidanceSpeedLimitFragmentTest : BaseTest() {

    lateinit var guidanceSpeedLimitFragment: GuidanceSpeedLimitFragment

    @Mock
    private lateinit var mockPresenter: GuidanceSpeedPresenter

    @Before
    override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        guidanceSpeedLimitFragment = GuidanceSpeedLimitFragment.newInstance()
        guidanceSpeedLimitFragment.mPresenter = mockPresenter
    }

    @Test
    fun testFragmentCreation() {
        addFrag(guidanceSpeedLimitFragment, GuidanceSpeedLimitFragment::class.java.name)
        assertNotNull(guidanceSpeedLimitFragment)
        assertNotNull(guidanceSpeedLimitFragment.view)
        assertEquals(
                (guidanceSpeedLimitFragment.view as GuidanceSpeedLimitView).unitSystem,
                Util.getLocaleUnit())
    }

    @Test
    fun testFragmentCreationWithNotInitializedPresenter() {
        navigationManager = mock(NavigationManager::class.java)
        positioningManager = mock(PositioningManager::class.java)
        guidanceSpeedLimitFragment = GuidanceSpeedLimitFragment.newInstance()
        addFrag(guidanceSpeedLimitFragment, GuidanceSpeedLimitFragment::class.java.name)
        assertNotNull(guidanceSpeedLimitFragment.mPresenter)
    }

    @Test
    fun testOnPause() {
        guidanceSpeedLimitFragment.onPause()
        verify(mockPresenter).pause()
    }

    @Test
    fun testOnResume() {
        guidanceSpeedLimitFragment.onResume()
        verify(mockPresenter).resume()
    }

    @Test
    fun testCallback() {
        val speedLimit = 100.0
        addFrag(guidanceSpeedLimitFragment, GuidanceSpeedLimitFragment::class.java.name)
        val data = Mockito.mock(GuidanceSpeedData::class.java)
        `when`(data.currentSpeedLimit).thenReturn(speedLimit)
        guidanceSpeedLimitFragment.onDataChanged(data)
        assertEquals(speedLimit, (guidanceSpeedLimitFragment.view as GuidanceSpeedLimitView).currentSpeedData.currentSpeedLimit)
    }
}