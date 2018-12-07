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

package com.here.msdkuiapp.espresso.impl.views.route.screens

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withParent
import android.view.View
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIndex
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ROUTE_RESULT_1
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf

/**
 * Route ViewInteractions selectors
 */
object RouteView {

    /**
     * @return The [Matcher]<[View]> on route description view
     */
    val onRouteDescriptionView: Matcher<View>
        get() = withId(R.id.route_description_list)

    /**
     * @return The [ViewInteraction] route description list on route
     */
    val onRouteDescriptionList: ViewInteraction
        get() = onView(withId(R.id.route_description_list))

    /**
     * @return The [ViewInteraction] maneuver description list on route
     */
    val onRouteManeuversList: ViewInteraction
        get() = onView(withId(R.id.guidance_maneuver_list))

    /**
     * @return The [ViewInteraction] duration time of the first item in routes list
     */
    val onRouteListItemDuration: ViewInteraction
        get() = onRouteListItemDuration(ROUTE_RESULT_1)

    /**
     * @return The [ViewInteraction] icon type of the first item in routes list
     */
    val onRouteListItemIconType: ViewInteraction
        get() = onRouteListItemIconType(ROUTE_RESULT_1)

    /**
     * @return The [ViewInteraction] delay information of the first item in routes list
     */
    val onRouteListItemDelayInformation: ViewInteraction
        get() = onRouteListItemDelayInformation(ROUTE_RESULT_1)

    /**
     * @return The [ViewInteraction] address & distance details of the first item in routes list
     */
    val onRouteListItemDetails: ViewInteraction
        get() = onRouteListItemDetails(ROUTE_RESULT_1)

    /**
     * @return The [ViewInteraction] arrival time of the first item in routes list
     */
    val onRouteListItemArrival: ViewInteraction
        get() = onRouteListItemArrival(ROUTE_RESULT_1)

    /**
     * @return The [ViewInteraction] duration time on route overview
     */
    val onRouteOverviewDuration: ViewInteraction
        get() = onView(allOf(ViewMatchers.withId(R.id.desc_time), withParent(withParent(withId(R.id.description)))))

    /**
     * @return The [ViewInteraction] delay information time on route overview
     */
    val onRouteOverviewDelayInformation: ViewInteraction
        get() = onView(allOf(withId(R.id.desc_traffic_warning), withParent(withParent(withId(R.id.description)))))

    /**
     * @return The [ViewInteraction] address & distance details on route overview
     */
    val onRouteOverviewDetails: ViewInteraction
        get() = onView(Matchers.allOf(ViewMatchers.withId(R.id.desc_details), ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.description)))))

    /**
     * @return The [ViewInteraction] arrival time on route overview
     */
    val onRouteOverviewArrival: ViewInteraction
        get() = onView(Matchers.allOf(ViewMatchers.withId(R.id.desc_arrival), ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.description)))))

    /**
     * @return The [String] instruction text on maneuver description list
     */
    val onManeuverInstructionText: String
        get() = CoreMatchers.getTextById(R.string.msdkui_arrive)

    /**
     * @return The [ViewInteraction] line progress bar information on route overview
     */
    val onRouteDescLineBar: ViewInteraction
        get() = onRouteDescLineBar(ROUTE_RESULT_1)

    /**
     * @return The [ViewInteraction] route item displayed on route overview
     */
    val onRouteOverviewItem: ViewInteraction
        get() = onView(withId(R.id.description))

    /**
     * @return The [ViewInteraction] destination on route overview
     */
    val onDestinationText: ViewInteraction
        get() = onView(withId(R.id.destination))

    /**
     * @return The [ViewInteraction] distance on maneuver description list
     */
    fun onManeuverDistance(item: Int): ViewInteraction = onView(withIndex(withId(R.id.maneuver_distance_view), item))

    /**
     * @return The [ViewInteraction] icon type on maneuver description list
     */
    fun onManeuverIconType(item: Int): ViewInteraction = onView(withIndex(withId(R.id.maneuver_icon_view), item))

    /**
     * @return The [ViewInteraction] instruction on maneuver description list
     */
    fun onManeuverInstruction(item: Int): ViewInteraction = onView(withIndex(withId(R.id.maneuver_instruction_view), item))

    /**
     * @return The [ViewInteraction] location address on maneuver description list
     */
    fun onManeuverAddress(item: Int): ViewInteraction = onView(withIndex(withId(R.id.maneuver_address_view), item))

    /**
     * @return The [ViewInteraction] duration time on route description list
     */
    fun onRouteListItemDuration(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_time), item))

    /**
     * @return The [ViewInteraction] icon type on route description list
     */
    fun onRouteListItemIconType(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_type_icon), item))

    /**
     * @return The [ViewInteraction] delay information on route description list
     */
    fun onRouteListItemDelayInformation(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_traffic_warning), item))

    /**
     * @return The [ViewInteraction] address & distance details on route description list
     */
    fun onRouteListItemDetails(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_details), item))

    /**
     * @return The [ViewInteraction] arrival on route description list
     */
    fun onRouteListItemArrival(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_arrival), item))

    /**
     * @return The [ViewInteraction] line progress bar information on route description list
     */
    fun onRouteDescLineBar(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_bar), item))

    /**
     * @return The [ViewInteraction] for see steps button on route maneuver list.
     */
    val onSeeManeuverSteps: ViewInteraction get() = onView(withId(R.id.see_steps))
}