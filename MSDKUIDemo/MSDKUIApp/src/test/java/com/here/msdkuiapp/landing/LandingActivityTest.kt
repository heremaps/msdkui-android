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

package com.here.msdkuiapp.landing

import androidx.recyclerview.widget.RecyclerView
import com.here.android.mpa.common.PositioningManager
import com.here.android.mpa.guidance.NavigationManager
import com.here.android.mpa.guidance.VoiceCatalog
import com.here.msdkuiapp.R
import com.here.msdkuiapp.guidance.GuidanceRouteSelectionActivity
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import com.here.msdkuiapp.position.AppPositioningManager
import com.here.msdkuiapp.routing.RoutingActivity
import com.here.testutils.BaseTest
import com.here.testutils.argumentCaptor
import com.here.testutils.captureSafe
import junit.framework.Assert.assertNotNull
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

/**
 * Tests for [LandingActivity].
 */
class LandingActivityTest : BaseTest() {

    private lateinit var activityController: ActivityController<LandingActivity>

    @Before
    override fun setUp() {
        activityController = Robolectric.buildActivity(LandingActivity::class.java)
    }

    @Test
    fun testUI() {
        val activity = activityController.create().get()
        // creating activities should setup the list
        val list = activity.findViewById<RecyclerView>(R.id.landing_list)
        assertNotNull(list)
        assertNotNull(list.adapter)
        // there should be 2 row
        assertThat(list.adapter!!.itemCount, `is`(2))
    }

    @Test
    fun testClickOnListShouldOpenRespectiveScreen() {
        val activity = activityController.create().visible().get()
        // creating activities should setup the list
        val list = activity.findViewById<RecyclerView>(R.id.landing_list)
        assertNotNull(list)
        list.getChildAt(0).performClick()
        assertActivityStarted(activity, RoutingActivity::class.java)
        list.getChildAt(1).performClick()
        assertActivityStarted(activity, GuidanceRouteSelectionActivity::class.java)
    }

    @Test
    fun testBackPress() {
        val activity = activityController.create().visible().get()
        val mockPositioningManager = mock(AppPositioningManager::class.java)
        appPositioningManager = mockPositioningManager
        `when`(mockPositioningManager.isActive).thenReturn(true)
        activity.onBackPressed()
        verify(mockPositioningManager).stop()
    }

    @Test
    fun testVoicePackageDownload() {
        val activity = activityController.get()
        val mockVoiceCatalog = mock(VoiceCatalog::class.java)
        navigationManager = mock(NavigationManager::class.java, RETURNS_DEEP_STUBS)
        `when`(mockVoiceCatalog.isLocalVoiceSkin(ArgumentMatchers.anyLong())).thenReturn(false)
        val voiceCatalogDownloadListenerCapture = argumentCaptor<VoiceCatalog.OnDownloadDoneListener>()
        activity.downloadVoicePackage(mockVoiceCatalog)
        verify(mockVoiceCatalog).downloadVoice(ArgumentMatchers.anyLong(), captureSafe(voiceCatalogDownloadListenerCapture))
        voiceCatalogDownloadListenerCapture.value.onDownloadDone(VoiceCatalog.Error.NONE)
        verify(navigationManager!!).voiceGuidanceOptions

        `when`(mockVoiceCatalog.isLocalVoiceSkin(ArgumentMatchers.anyLong())).thenReturn(true)
        activity.downloadVoicePackage(mockVoiceCatalog)
        verify(navigationManager!!, atLeastOnce()).voiceGuidanceOptions
    }

}