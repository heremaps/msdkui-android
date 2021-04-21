/*
 * Copyright (C) 2017-2021 HERE Europe B.V.
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

package com.here.msdkuiapp.about

import com.here.testutils.BaseTest
import junit.framework.Assert
import kotlinx.android.synthetic.main.activity_about.*
import org.junit.Before
import org.junit.Test
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

/**
 * Tests for [AboutActivity].
 */
class AboutActivityTest : BaseTest() {

    private lateinit var activityController: ActivityController<AboutActivity>

    @Before
    override fun setUp() {
        activityController = Robolectric.buildActivity(AboutActivity::class.java).create().start()
    }

    @Test
    fun testUi() {
        val activity = activityController.get()
        Assert.assertEquals(activity.app_version.text, com.here.msdkuiapp.BuildConfig.VERSION_NAME)
        Assert.assertEquals(activity.ui_kit_version.text, com.here.msdkui.BuildConfig.VERSION_NAME)
        Assert.assertEquals(activity.here_sdk_version.text, com.here.android.mpa.common.Version.getSdkVersion())
    }
}