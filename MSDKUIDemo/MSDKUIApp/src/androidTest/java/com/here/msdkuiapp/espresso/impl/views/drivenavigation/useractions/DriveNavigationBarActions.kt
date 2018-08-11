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

import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.matchers.DriveNavigationBarMatchers
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationBarView.onDriveNavigationBarLocationTitle
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationBarView.onDriveNavigationBarTitle
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationBarView.onRouteOverviewBarTitle
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerBarActions

/**
 * Drive Navigation specific actionbar related actions
 */
object DriveNavigationBarActions {

    /**
     * Wait for drive navigation view opened
     */
    fun waitForDriveNavigationView(): DriveNavigationBarActions {
        onRootView.perform(waitForCondition(onDriveNavigationBarTitle))
        return this
    }

    /**
     * Wait for drive navigation destination address visible on actionbar
     */
    fun waitForDestinationNotDisplayed(): RoutePlannerBarActions {
        onRootView.perform(waitForCondition(onDriveNavigationBarLocationTitle, isVisible = false))
        return RoutePlannerBarActions
    }

    /**
     * Wait for current device location visible on actionbar
     */
    fun waitForDestinationDisplayed(): DriveNavigationBarMatchers {
        onRootView.perform(waitForCondition(onDriveNavigationBarLocationTitle))
        return DriveNavigationBarMatchers
    }

    /**
     * Wait for route overview opened
     */
    fun waitForRouteOverView(): DriveNavigationActions {
        onRootView.perform(waitForCondition(onRouteOverviewBarTitle))
        return DriveNavigationActions
    }
}