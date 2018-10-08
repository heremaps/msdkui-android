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
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIndex
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ROUTE_RESULT_1
import org.hamcrest.Matcher
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
    val onManeuverDescriptionList: ViewInteraction
        get() = onView(withId(R.id.maneuver_route_list))

    /**
     * @return The [ViewInteraction] duration time on route overview
     */
    val onRouteDescDuration: ViewInteraction
        get() = onRouteDescDuration(ROUTE_RESULT_1)

    /**
     * @return The [ViewInteraction] icon type on route overview
     */
    val onRouteDescIconType: ViewInteraction
        get() = onRouteDescIconType(ROUTE_RESULT_1)

    /**
     * @return The [ViewInteraction] delay information on route overview
     */
    val onRouteDescDelayInformation: ViewInteraction
        get() = onRouteDescDealyInformation(ROUTE_RESULT_1)

    /**
     * @return The [ViewInteraction] address & distance details on route overview
     */
    val onRouteDescDetails: ViewInteraction
        get() = onRouteDescDetails(ROUTE_RESULT_1)

    /**
     * @return The [ViewInteraction] arrival on route overview
     */
    val onRouteDescArrival: ViewInteraction
        get() = onRouteDescArrival(ROUTE_RESULT_1)

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
        get() = onView(withId(R.id.route_item))

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
    fun onRouteDescDuration(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_time), item))

    /**
     * @return The [ViewInteraction] icon type on route description list
     */
    fun onRouteDescIconType(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_type_icon), item))

    /**
     * @return The [ViewInteraction] delay information on route description list
     */
    fun onRouteDescDealyInformation(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_traffic_warning), item))

    /**
     * @return The [ViewInteraction] address & distance details on route description list
     */
    fun onRouteDescDetails(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_details), item))

    /**
     * @return The [ViewInteraction] arrival on route description list
     */
    fun onRouteDescArrival(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_arrival), item))

    /**
     * @return The [ViewInteraction] line progress bar information on route description list
     */
    fun onRouteDescLineBar(item: Int): ViewInteraction = onView(withIndex(withId(R.id.desc_bar), item))
}