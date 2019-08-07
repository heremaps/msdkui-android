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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.screens

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.*
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
    val onPlannerActionBarTitleView: ViewInteraction
        get() = onView(withId(R.id.ac_title))

    /**
     * @return The [ViewInteraction] confirmation right image view icon on action bar
     */
    val onPlannerBarRightImageIconView: ViewInteraction
        get() = onView(withId(R.id.ac_right_icon))

    /**
    * @return The [Matcher]<[View]> confirmation 'Done / Check' right image view icon on action bar
    */
    val onPlannerBarRightImageIconCheck: Matcher<View>
        get() = allOf(withId(R.id.ac_right_icon), withTagValue(`is`(R.drawable.ic_check_black_24dp as Any)))

    /**
     * @return The [Matcher]<[View]> confirmation 'Expand' right image view icon on action bar
     */
    val onPlannerBarRightImageIconExpand: Matcher<View>
        get() = allOf(withId(R.id.ac_right_icon), withTagValue(`is`(R.drawable.ic_expande as Any)))

    /**
     * @return The [ViewInteraction] confirmation back image view icon on action bar
     */
    val onPlannerBarBackImageIconView: ViewInteraction
        get() = onView(withId(R.id.ac_back_button))

    /**
     * @return The [ViewInteraction] Choose waypoint label on action bar
     */
    val onPlannerBarWaypointLabel: ViewInteraction
        get() = onView(withId(R.id.selected_waypoint_label))

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
     * @return The [ViewInteraction] Options title text view on action bar
     */
    val onPlannerBarOptionsTitleView: ViewInteraction
        get() = onView(withText(R.string.msdkui_app_options))

    /**
     * @return The [Matcher]<[View]> Options title text view on action bar
     */
    val onPlannerBarOptionsTitle: Matcher<View>
        get() = withText(R.string.msdkui_app_options)

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
    val onPlannerAddWaypointButtonView: ViewInteraction
        get() = onView(withId(R.id.waypoint_add))

    /**
     * @return The [Matcher]<[View]> add/plus image view button on route planner
     */
    val onPlannerAddWaypointButton: Matcher<View>
        get() = withId(R.id.waypoint_add)

    /**
     * @return The [ViewInteraction] swap waypoints list items image button on route planner
     */
    val onPlannerSwapImageButtonView: ViewInteraction
        get() = onView(withId(R.id.swap_list))

    /**
     * @return The [Matcher]<[View]> swap waypoints list items image button on route planner
     */
    val onPlannerSwapImageButton: Matcher<View>
        get() = withId(R.id.swap_list)

    /**
     * @return The [ViewInteraction] go back image button on route planner
     */
    val onPlannerBackImageButton: ViewInteraction
        get() = onView(withId(R.id.ac_back_button))

    /**
     * @return The [ViewInteraction] remove image view button on route planner
     */
    fun onPlannerRemoveWaypointButtonView(button: RoutingTestData.RemoveWaypointBtn): ViewInteraction
            = onView(withContentDescription(button.value))

    /**
     * @return The [ViewInteraction] Route planner app title text view by given view selector id on action bar
     */
    fun onPlannerBarRoutePlannerTitle(titleText: Int): ViewInteraction = onView(withText(titleText))

    /**
     * @return The [ViewInteraction] confirmation back image view icon on action bar
     */
    fun onPlannerBarBackImageIconView(backBtnDrawableId: Int): ViewInteraction
            = onView(allOf(withId(R.id.ac_back_button), withTagValue(`is`(backBtnDrawableId as Any))))

    /**
     * @return The [ViewInteraction] confirmation right image view icon on action bar
     */
    fun onPlannerBarRightImageIconView(rightIconDrawableId: Int): ViewInteraction
            = onView(allOf(withId(R.id.ac_right_icon), withTagValue(`is`(rightIconDrawableId as Any))))
}