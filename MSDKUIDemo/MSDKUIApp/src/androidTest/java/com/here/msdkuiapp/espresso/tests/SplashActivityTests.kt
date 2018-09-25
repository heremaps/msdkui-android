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

package com.here.msdkuiapp.espresso.tests

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.SplashActivity
import com.here.msdkuiapp.espresso.impl.annotation.CompatibilityUITest
import com.here.msdkuiapp.espresso.impl.core.CoreView.onLandingScreenDriverNavigationView
import com.here.msdkuiapp.espresso.impl.core.CoreView.onLandingScreenRoutePlannerView
import com.here.msdkuiapp.espresso.impl.core.CoreView.onPlannerBarAppNameTitleView
import org.junit.Test

/**
 * UI test of the application content
 */
class SplashActivityTests: TestBase<SplashActivity>(SplashActivity::class.java) {

    /**
     * MSDKUI-126: Select "From" on the map
     */
    @Test
    @CompatibilityUITest
    fun testSplashScreenContent() {
        // Check app title
        onPlannerBarAppNameTitleView.check(matches(isDisplayed()))
        // Check route planner on landing screen
        onLandingScreenRoutePlannerView.check(matches(isDisplayed()))
        // Check drive navigation on landing screen
        onLandingScreenDriverNavigationView.check(matches(isDisplayed()))
    }
}