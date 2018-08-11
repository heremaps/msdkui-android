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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions

import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.espresso.impl.core.CoreActions.clickWithNoConstraint
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData
import com.here.msdkuiapp.espresso.impl.views.route.useractions.RouteActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerBarMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerAddWaypointButton
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarLocationTitle
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarRightImageIcon
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarRoutePlannerTitle
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarRouteResultTitle
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarWaypointLabel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerRemoveWaypointButton
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerSwapImageButton

/**
 * Route planner specific actionbar related actions
 */
object RoutePlannerBarActions {

    /**
     * Tap on right tick image button to confirm a waypoint selection
     * @return [String] location label
     */
    fun tapOnTickButton(): String {
        val location = CoreMatchers.getTextView(onPlannerBarWaypointLabel)
        onPlannerBarRightImageIcon.perform(click())
        return location
    }

    /**
     * Tap on right arrow image button to expand/collapse route planner view
     */
    fun tapOnRighArrowButton(): RoutePlannerBarActions {
        onPlannerBarRightImageIcon.perform(click())
        return this
    }

    /**
     * Tap on swap/reverse waypoints order image list view
     */
    fun tapOnSwapWaypointButton(): RoutePlannerBarActions {
        onPlannerSwapImageButton.check(matches(isDisplayed())).perform(clickWithNoConstraint())
        return this
    }

    /**
     * Tap on waypoint plus/add image button
     */
    fun tapOnAddWaypointButton(): RoutePlannerBarMatchers {
        onPlannerAddWaypointButton.check(matches(isDisplayed())).perform(clickWithNoConstraint())
        return RoutePlannerBarMatchers
    }

    /**
     * Tap on waypoint remove image button
     */
    fun tapOnRemoveWaypointButton(removeWaypointItem: RoutingTestData.RemoveWaypointBtn): RoutePlannerMatchers {
        onPlannerRemoveWaypointButton(removeWaypointItem).check(matches(isDisplayed())).perform(click())
        return RoutePlannerMatchers
    }

    /**
     * Wait for route planner location address visible on actionbar
     */
    fun waitForLocationDisplayed(): RoutePlannerBarActions {
        onRootView.perform(waitForCondition(onPlannerBarLocationTitle, isVisible = false))
        return this
    }

    /**
     * Wait and check route planner panel collapsed on actionbar
     */
    fun waitForRoutePlannerCollapsed(): RouteActions {
        onRootView.perform(waitForCondition(onPlannerBarRouteResultTitle))
        return RouteActions
    }

    /**
     * Wait and check route planner panel expanded on actionbar
     */
    fun waitForRoutePlannerExpanded(): RoutePlannerBarActions {
        onRootView.perform(waitForCondition(onPlannerBarRoutePlannerTitle))
        return this
    }
}