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

package com.here.msdkuiapp.espresso.tests

import android.app.Activity
import android.app.Instrumentation
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.here.msdkuiapp.espresso.impl.mock.MockLocation
import com.here.msdkuiapp.espresso.impl.mock.MockLocationData
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Base class for automated tests.
 */
@RunWith(AndroidJUnit4::class)
abstract class TestBase<T: Activity>(activityClass: Class<T>): ActivityTestRule<T>(activityClass) {

    @Rule
    @JvmField
    val activityRule: ActivityTestRule<T> = ActivityTestRule(activityClass)

    /**
     * Grant demo app popups and alerts permissions
     */
    @Rule
    @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)

    /**
     * Mocking location service. Set parameter 'isMocked = true' to activate mock location
     */
    val mockLocationData: MockLocationData = MockLocationData(MockLocation(getInstrumentation().targetContext))

    /**
     * Gets instrumentation
     */
    private fun getInstrumentation(): Instrumentation = InstrumentationRegistry.getInstrumentation()
}