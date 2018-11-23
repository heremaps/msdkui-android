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

package com.here.msdkuiapp

import android.os.Build
import com.here.msdkuiapp.common.PermissionRequestCode
import com.here.msdkuiapp.landing.LandingActivity
import com.here.testutils.BaseTest
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController
import org.robolectric.shadows.ShadowToast
import org.robolectric.util.ReflectionHelpers

/**
 * Tests for [SplashActivity].
 */
class SplashActivityTest : BaseTest() {

    private lateinit var activityController: ActivityController<SplashActivity>

    @Before
    override fun setUp() {
        activityController = Robolectric.buildActivity(SplashActivity::class.java)
    }

    @Test
    fun testOnCreateSdk19() {
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 19)
        val activity = activityController.create().start().resume().get()
        assertActivityStarted(activity, LandingActivity::class.java)
        assertTrue(activity.isFinishing)
    }

    @Test
    fun testOnRequestPermissionsResultOK() {
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 24)
        val activity = activityController.get()
        activity.onRequestPermissionsResult(PermissionRequestCode.STORAGE_REQ_CODE, emptyArray(), intArrayOf(0))
        assertActivityStarted(activity, LandingActivity::class.java)
        assertTrue(activity.isFinishing)
    }

    @Test
    fun testOnRequestPermissionsResultFailed() {
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 24)
        val activity = activityController.get()
        activity.onRequestPermissionsResult(PermissionRequestCode.STORAGE_REQ_CODE, emptyArray(), intArrayOf())
        assertEquals(ShadowToast.getTextOfLatestToast(), getString(R.string.msdkui_app_storage_permission_not_granted))
        assertTrue(activity.isFinishing)
    }
}