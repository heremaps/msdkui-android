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

import android.content.res.Configuration
import com.here.android.mpa.guidance.NavigationManager
import com.here.msdkui.common.ThemeUtil
import com.here.msdkui.guidance.GuidanceSpeedData
import com.here.msdkui.guidance.GuidanceSpeedPresenter
import com.here.msdkuiapp.R
import com.here.msdkuiapp.common.Util
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import com.here.msdkuiapp.position.AppPositioningManager

import com.here.testutils.BaseTest
import kotlinx.android.synthetic.main.guidance_speed_fragment.guidance_current_speed
import kotlinx.android.synthetic.main.guidance_speed_fragment.view.guidance_current_speed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric

/**
 * Tests for class [GuidanceSpeedFragment].
 */
class GuidanceSpeedFragmentTest : BaseTest() {

    private lateinit var guidanceSpeedFragment: GuidanceSpeedFragment

    @Mock
    private lateinit var mockGuidanceCurrentSpeedPresenter: GuidanceSpeedPresenter

    @Before
    override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        guidanceSpeedFragment = GuidanceSpeedFragment.newInstance()
        guidanceSpeedFragment.presenter = mockGuidanceCurrentSpeedPresenter
    }

    @Test
    fun testPauseAndResume() {
        guidanceSpeedFragment.presenter = mockGuidanceCurrentSpeedPresenter
        assertNotNull(guidanceSpeedFragment.presenter)
        guidanceSpeedFragment.onPause()
        verify(mockGuidanceCurrentSpeedPresenter).pause()
        guidanceSpeedFragment.onResume()
        verify(mockGuidanceCurrentSpeedPresenter).resume()
    }

    @Test
    fun testFragmentCreation() {
        addFrag(guidanceSpeedFragment, GuidanceSpeedFragment::class.java.name)
        assertNotNull(guidanceSpeedFragment)
        assertNotNull(guidanceSpeedFragment.view)
        assertEquals(guidanceSpeedFragment.view!!.guidance_current_speed.unitSystem,
                Util.getLocaleUnit())
    }

    @Test
    fun testFragmentCreationWithNotInitializedPresenter() {
        navigationManager = mock(NavigationManager::class.java)
        appPositioningManager = mock(AppPositioningManager::class.java, RETURNS_DEEP_STUBS)
        guidanceSpeedFragment = GuidanceSpeedFragment.newInstance()
        addFrag(guidanceSpeedFragment, GuidanceSpeedLimitFragment::class.java.name)
        assertNotNull(guidanceSpeedFragment.presenter)
    }

    @Test
    fun testBackgroundInLandscapeMode() {
        fragmentActivity!!.resources.configuration.orientation = Configuration.ORIENTATION_LANDSCAPE
        addFrag(guidanceSpeedFragment, GuidanceSpeedFragment::class.java.name)
        assertNotNull(guidanceSpeedFragment)
        assertNotNull(guidanceSpeedFragment.view)
        assertNotNull(guidanceSpeedFragment.view!!.guidance_current_speed.background)
    }

    @Test
    fun testSetterGetterPanelPresenter() {
        guidanceSpeedFragment.presenter = mockGuidanceCurrentSpeedPresenter
        assertNotNull(guidanceSpeedFragment.presenter)
    }

    @Test
    fun testOnPause() {
        guidanceSpeedFragment.onPause()
        verify(mockGuidanceCurrentSpeedPresenter).pause()
    }

    @Test
    fun testOnResume() {
        guidanceSpeedFragment.onResume()
        verify(mockGuidanceCurrentSpeedPresenter).resume()
    }

    @Test
    fun testCallback() {
        val speed = 60.0
        addFrag(guidanceSpeedFragment, GuidanceSpeedFragment::class.java.name)
        val data = Mockito.mock(GuidanceSpeedData::class.java)
        `when`(data.currentSpeed).thenReturn(speed)
        `when`(data.isValid).thenReturn(true)
        guidanceSpeedFragment.onDataChanged(data)
        val view = guidanceSpeedFragment.guidance_current_speed
        assertEquals(speed, view.currentSpeedData.currentSpeed, .001)
        // test speeding now
        `when`(data.isSpeeding).thenReturn(true)
        guidanceSpeedFragment.onDataChanged(data)
        assertEquals(view.valueTextColor, ThemeUtil.getColor(fragmentActivity, R.attr.colorNegative))
    }

    @Test
    fun testCallbackInLandscape() {
        fragmentActivity!!.resources.configuration.orientation = Configuration.ORIENTATION_LANDSCAPE
        val speed = 60.0
        addFrag(guidanceSpeedFragment, GuidanceSpeedFragment::class.java.name)
        val data = Mockito.mock(GuidanceSpeedData::class.java)
        `when`(data.currentSpeed).thenReturn(speed)
        `when`(data.isValid).thenReturn(true)
        guidanceSpeedFragment.onDataChanged(data)
        val view = guidanceSpeedFragment.guidance_current_speed
        assertEquals(view.valueTextColor, ThemeUtil.getColor(fragmentActivity, R.attr.colorForegroundLight))
    }
}