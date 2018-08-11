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

package com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions

import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriverNavigationView.onRouteOverviewStartNaviBtn
import com.here.msdkuiapp.espresso.impl.views.guidance.useractions.GuidanceActions

/**
 * Drive Navigation & Overview related actions
 */
object DriveNavigationActions {

    /**
     * Tap on start navigation button to open guidance view
     */
    fun tapOnStartNavigationBtn(): GuidanceActions {
        onRouteOverviewStartNaviBtn.check(matches(isDisplayed())).perform(click())
        return GuidanceActions
    }
}