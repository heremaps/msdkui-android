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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.screens

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withTagValue
import android.view.View
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

/**
 * Route planner specific actionbar related ViewInteractions selectors
 */
object RoutePlannerBarView {

    /**
     * @return The [ViewInteraction] confirmation right image view icon on action bar
     */
    val onPlannerBarRightImageIcon: ViewInteraction
        get() = onView(withId(R.id.ac_right_icon))

    /**
     * @return The [ViewInteraction] confirmation back image view icon on action bar
     */
    val onPlannerBarBackImageIcon: ViewInteraction
        get() = onView(withId(R.id.ac_back_button))

    /**
     * @return The [ViewInteraction] Choose waypoint label on action bar
     */
    val onPlannerBarWaypointLabel: ViewInteraction
        get() = onView(withId(R.id.waypoint_label))

    /**
     * @return The [ViewInteraction] route planner title text view on action bar
     */
    val onPlannerBarRoutePlannerTitleView: ViewInteraction
        get() = onView(withText(R.string.msdkui_app_rp_teaser_title))

    /**
     * @return The [Matcher]<[View]> route planner title text view on action bar
     */
    val onPlannerBarRoutePlannerTitle: Matcher<View>
        get() = withText(R.string.msdkui_app_rp_teaser_title)

    /**
     * @return The [Matcher]<[View]> Routes title text view for routes results on action bar
     */
    val onPlannerBarRouteResultTitle: Matcher<View>
        get() = withText(R.string.msdkui_app_route_results_title)

    /**
     * @return The [ViewInteraction] Options title text view on action bar
     */
    val onPlannerBarOptionsTitle: ViewInteraction
        get() = onView(withText(R.string.msdkui_app_options))

    /**
     * @return The [ViewInteraction] Choose waypoint title text view on action bar
     */
    val onPlannerBarChooseWaypointTitle: ViewInteraction
        get() = onView(withText(R.string.msdkui_waypoint_select_location))

    /**
     * @return The [Matcher]<[View]> Tap the map location title text view on action bar
     */
    val onPlannerBarLocationTitle: Matcher<View>
        get() = withText(R.string.msdkui_app_rp_waypoint_subtitle)

    /**
     * @return The [ViewInteraction] add/plus image view button on route planner
     */
    val onPlannerAddWaypointButton: ViewInteraction
        get() = onView(withId(R.id.waypoint_add))

    /**
     * @return The [ViewInteraction] swap waypoints list items image button on route planner
     */
    val onPlannerSwapImageButton: ViewInteraction
        get() = onView(withId(R.id.swap_list))

    /**
     * @return The [ViewInteraction] remove image view button on route planner
     */
    fun onPlannerRemoveWaypointButton(button: RoutingTestData.RemoveWaypointBtn): ViewInteraction {
        return onView(withContentDescription(button.value))
    }

    /**
     * @return The [ViewInteraction] Route planner app title text view by given view selector id on action bar
     */
    fun onPlannerBarRoutePlannerTitle(titleText: Int): ViewInteraction {
        return onView(withText(titleText))
    }

    /**
     * @return The [ViewInteraction] confirmation back image view icon on action bar
     */
    fun onPlannerBarBackImageIcon(backBtnDrawableId: Int): ViewInteraction {
        return onView(allOf(withId(R.id.ac_back_button), withTagValue(`is`(backBtnDrawableId as Any))))
    }

    /**
     * @return The [ViewInteraction] confirmation right image view icon on action bar
     */
    fun onPlannerBarRightImageIcon(rightIconDrawableId: Int): ViewInteraction {
        return onView(allOf(withId(R.id.ac_right_icon), withTagValue(`is`(rightIconDrawableId as Any))))
    }
}