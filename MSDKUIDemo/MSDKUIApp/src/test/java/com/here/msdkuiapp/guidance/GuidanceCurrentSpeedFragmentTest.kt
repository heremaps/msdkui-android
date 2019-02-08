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

import com.here.android.mpa.common.PositioningManager
import com.here.android.mpa.guidance.NavigationManager
import com.here.msdkui.guidance.GuidanceSpeedData
import com.here.msdkui.guidance.GuidanceSpeedPresenter
import com.here.msdkuiapp.common.Util
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import com.here.msdkuiapp.position.AppPositioningManager

import com.here.testutils.BaseTest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.android.synthetic.main.guidance_current_speed.guidance_current_speed
import kotlinx.android.synthetic.main.guidance_current_speed.view.guidance_current_speed
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Tests for class [GuidanceCurrentSpeedFragment].
 */
class GuidanceCurrentSpeedFragmentTest : BaseTest() {

    private lateinit var guidanceCurrentSpeedFragment: GuidanceCurrentSpeedFragment

    @Mock
    private lateinit var mockGuidanceCurrentSpeedPresenter: GuidanceSpeedPresenter

    @Before
    override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        guidanceCurrentSpeedFragment = GuidanceCurrentSpeedFragment.newInstance()
        guidanceCurrentSpeedFragment.presenter = mockGuidanceCurrentSpeedPresenter
    }

    @Test
    fun testPauseAndResume() {
        guidanceCurrentSpeedFragment.presenter = mockGuidanceCurrentSpeedPresenter
        assertNotNull(guidanceCurrentSpeedFragment.presenter)
        guidanceCurrentSpeedFragment.onPause()
        verify(mockGuidanceCurrentSpeedPresenter).pause()
        guidanceCurrentSpeedFragment.onResume()
        verify(mockGuidanceCurrentSpeedPresenter).resume()
    }

    @Test
    fun testFragmentCreation() {
        addFrag(guidanceCurrentSpeedFragment, GuidanceCurrentSpeedFragment::class.java.name)
        assertNotNull(guidanceCurrentSpeedFragment)
        assertNotNull(guidanceCurrentSpeedFragment.view)
        assertEquals(guidanceCurrentSpeedFragment.view!!.guidance_current_speed.unitSystem,
                Util.getLocaleUnit())
    }

    @Test
    fun testFragmentCreationWithNotInitializedPresenter() {
        navigationManager = mock(NavigationManager::class.java)
        appPositioningManager = mock(AppPositioningManager::class.java, RETURNS_DEEP_STUBS)
        guidanceCurrentSpeedFragment = GuidanceCurrentSpeedFragment.newInstance()
        addFrag(guidanceCurrentSpeedFragment, GuidanceSpeedLimitFragment::class.java.name)
        assertNotNull(guidanceCurrentSpeedFragment.presenter)
    }

    @Test
    fun testSetterGetterPanelPresenter() {
        guidanceCurrentSpeedFragment.presenter = mockGuidanceCurrentSpeedPresenter
        assertNotNull(guidanceCurrentSpeedFragment.presenter)
    }

    @Test
    fun testOnPause() {
        guidanceCurrentSpeedFragment.onPause()
        verify(mockGuidanceCurrentSpeedPresenter).pause()
    }

    @Test
    fun testOnResume() {
        guidanceCurrentSpeedFragment.onResume()
        verify(mockGuidanceCurrentSpeedPresenter).resume()
    }

    @Test
    fun testCallback() {
        val speed = 60.0
        addFrag(guidanceCurrentSpeedFragment, GuidanceCurrentSpeedFragment::class.java.name)
        val data = Mockito.mock(GuidanceSpeedData::class.java)
        `when`(data.currentSpeed).thenReturn(speed)
        `when`(data.isValid).thenReturn(true)
        guidanceCurrentSpeedFragment.onDataChanged(data)
        assertEquals(speed, (guidanceCurrentSpeedFragment.guidance_current_speed).currentSpeedData.currentSpeed)
    }
}