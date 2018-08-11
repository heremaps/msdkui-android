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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarRoutePlannerTitle
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarBackImageIcon
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarLocationTitle
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarOptionsTitle
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarRightImageIcon
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarWaypointLabel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerFromText
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerToText
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointLocationView
import org.hamcrest.Matchers

/**
 * Router planner specific actionbar matchers
 */
object RoutePlannerBarMatchers {

    /**
     * Check default waypoint location label text on action bar
     */
    fun checkWaypointDefaultLabel(): RoutePlannerBarMatchers {
        onPlannerWaypointLocationView.check(matches(isDisplayed()))
        return this
    }

    /**
     * Check waypoint location label text by given [String] on action bar
     */
    fun checkWaypoinLocationLabel(textView: String): RoutePlannerBarMatchers {
        if ((textView.startsWith(onPlannerFromText)) || (textView.startsWith(onPlannerToText))) {
            onPlannerBarWaypointLabel.check(matches(onPlannerBarLocationTitle))
        } else {
            onPlannerWaypointLocationView(textView).check(matches(isDisplayed()))
        }
        return this
    }

    /**
     * Check options view title on actionbar
     */
    fun checkOptionsDisplayed(): RoutePlannerBarMatchers {
        onPlannerBarOptionsTitle.check(matches(isDisplayed()))
        return this
    }

    /**
     * Check app title on action bar
     */
    fun checkActionbarDisplayed(backBtnDrawableId: Int = R.drawable.ic_arrow_back_black_24dp,
                                backBtnVisibility: Boolean = true,
                                titleText: Int = R.string.msdkui_app_rp_teaser_title,
                                rightIconDrawableId: Int = R.drawable.ic_info_outline_black_24dp,
                                rightIconVisibility: Boolean = true): RoutePlannerBarMatchers {
        // Check Back button visibility
        if (backBtnVisibility) {
            onPlannerBarBackImageIcon(backBtnDrawableId).check(matches(isDisplayed()))
            onPlannerBarBackImageIcon.check(matches(isDisplayed()))
        } else {
            onPlannerBarBackImageIcon.check(matches(Matchers.not(isDisplayed())))
        }
        // Check Title is visible
        onPlannerBarRoutePlannerTitle(titleText).check(matches(isDisplayed()))
        // Check Right icon visibility
        if (rightIconVisibility) {
            onPlannerBarRightImageIcon(rightIconDrawableId).check(matches(isDisplayed()))
            onPlannerBarRightImageIcon.check(matches(isDisplayed()))
        } else {
            onPlannerBarRightImageIcon.check(matches(Matchers.not(isDisplayed())))
        }
        return this
    }
}