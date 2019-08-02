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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.RemoveWaypointBtn
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerBarMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerOptionsMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerAddWaypointButton
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerAddWaypointButtonView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBackImageButton
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarLocationTitle
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarRightImageIconCheck
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarRightImageIconExpand
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarRightImageIconView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarRoutePlannerTitle
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarWaypointLabel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerRemoveWaypointButtonView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerSwapImageButton
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerSwapImageButtonView

/**
 * Route planner specific actionbar related actions
 */
object RoutePlannerBarActions: CoreActions() {

    /**
     * Tap on right tick image button to confirm a waypoint selection
     * @return [String] location label
     */
    fun tapOnTickButton(): String {
        val location = CoreMatchers.getText(onPlannerBarWaypointLabel)
        onPlannerBarRightImageIconView.check(matches(isDisplayed())).perform(click())
        return location
    }

    /**
     * Tap on right arrow image button to expand/collapse route planner view
     */
    fun tapOnRightArrowButton(): RoutePlannerBarActions {
        onPlannerBarRightImageIconView.check(matches(isDisplayed())).perform(click())
        return this
    }

    /**
     * Tap on swap/reverse waypoints order image list view
     */
    fun tapOnSwapWaypointButton(): RoutePlannerBarActions {
        onPlannerSwapImageButtonView.check(matches(isDisplayed())).perform(clickWithNoConstraint())
        return this
    }

    /**
     * Tap on waypoint plus/add image button
     */
    fun tapOnAddWaypointButton(): RoutePlannerBarMatchers {
        onPlannerAddWaypointButtonView.check(matches(isDisplayed())).perform(clickWithNoConstraint())
        return RoutePlannerBarMatchers
    }

    /**
     * Tap on waypoint remove image button
     */
    fun tapOnRemoveWaypointButton(removeWaypointItem: RemoveWaypointBtn): RoutePlannerMatchers {
        onPlannerRemoveWaypointButtonView(removeWaypointItem).check(matches(isDisplayed())).perform(click())
        return RoutePlannerMatchers
    }

    /**
     * Tap on back image button
     */
    fun tapOnBackRoutePlannerButton(): RoutePlannerBarActions {
        onPlannerBackImageButton.check(matches(isDisplayed())).perform(click())
        return this
    }

    /**
     * Wait for route planner location address visible on actionbar
     */
    fun waitForLocationNotDisplayed(): RoutePlannerBarActions {
        onRootView.perform(waitForCondition(onPlannerBarLocationTitle, isVisible = false))
        return this
    }

    /**
     * Wait and check route planner panel expanded on actionbar
     */
    fun waitForRoutePlannerExpanded(): RoutePlannerBarActions {
        onRootView.perform(waitForCondition(onPlannerBarRoutePlannerTitle))
        return this
    }

    /**
     * Wait for Options panel displayed on route planner
     */
    fun waitForOptionsDisplayed(): RoutePlannerOptionsMatchers {
        onRootView.perform(waitForCondition(RoutePlannerBarView.onPlannerBarOptionsTitle))
        return RoutePlannerOptionsMatchers
    }


    /**
     * Wait and check 'Done / Check' right image button on actionbar
     */
    fun waitForRightImageIconCheck(): RoutePlannerBarActions {
        onRootView.perform(waitForCondition(onPlannerBarRightImageIconCheck))
        return this
    }

    /**
     * Wait and check 'Expand' right image button on actionbar
     */
    fun waitForRightImageIconExpand(): RoutePlannerBarActions {
        onRootView.perform(waitForCondition(onPlannerBarRightImageIconExpand))
        return this
    }

    /**
     * Wait and check 'Swap' image button on actionbar
     */
    fun waitForSwapWaypointsImageButton(): RoutePlannerBarActions {
        onRootView.perform(waitForCondition(onPlannerSwapImageButton, isEnabled = true))
        return this
    }

    /**
     * Wait and check 'Add' image button on actionbar
     */
    fun waitForAddWaypointImageButton(): RoutePlannerBarActions {
        onRootView.perform(waitForCondition(onPlannerAddWaypointButton, isEnabled = true))
        return this
    }
}