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

package com.here.msdkuiapp.espresso.tests.routing.ui

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.isEnabled
import com.here.msdkuiapp.R
import com.here.msdkuiapp.SplashActivity
import com.here.msdkuiapp.espresso.impl.annotation.CompatibilityUITest
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withListSize
import com.here.msdkuiapp.espresso.impl.views.map.screens.MapView.onMapFragmentWrapper
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerBarMatchers.checkActionbarDisplayed
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerAddWaypointButtonView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerSwapImageButtonView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onOptionPanel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointList
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onTransportationPanel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onTravelTimePanel
import com.here.msdkuiapp.espresso.tests.TestBase
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test

class RoutePlannerUITests: TestBase<SplashActivity>(SplashActivity::class.java) {

    @Before
    fun prepare() {
        CoreActions().enterRoutePlanner()
    }

    /**
     * MSDKUI-126: Select "From" on the map
     */
    @Test
    @CompatibilityUITest
    fun testDefaultUI_whenRoutePlannerOpen() {
        // Check that Route planner has two visible waypoints items by default
        onPlannerWaypointList.check(matches(isDisplayed())).check(matches(withListSize(2)))
        // Map view is visible by default
        onMapFragmentWrapper.check(matches(isDisplayed()))
        // Check Swap button is visible and disabled by default.
        onPlannerSwapImageButtonView.check(matches(isDisplayed())).check(matches(not(isEnabled())))
        // Check Add/Plus button is visible to add more waypoints
        onPlannerAddWaypointButtonView.check(matches(isDisplayed()))
        // Check Transportation panel is visible
        onTransportationPanel.check(matches(isDisplayed()))
        // Check Travel time panel is visible
        onTravelTimePanel.check(matches(isDisplayed()))
        // Check Option panel button should be visible
        onOptionPanel.check(matches(isDisplayed()))
        // Check Actionbar title and controls
        checkActionbarDisplayed(rightIconVisibility = false,
                rightIconDrawableId = R.drawable.ic_info_outline_black_24dp)
    }
}