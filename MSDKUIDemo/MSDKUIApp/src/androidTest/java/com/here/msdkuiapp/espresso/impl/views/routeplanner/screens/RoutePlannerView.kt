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
import android.view.View
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getTextById
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIndex
import com.here.msdkuiapp.espresso.impl.testdata.Constants.FROM_TO_PATTERN
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.Matcher

/**
 * Route planner ViewInteractions selectors
 */
object RoutePlannerView {

    /**
     * @return The [ViewInteraction] for the route planner list view.
     */
    val onPlannerWaypointList: ViewInteraction
        get() = onView(withId(R.id.waypoint_list))

    /**
     * @return The [ViewInteraction] Choose waypoint location view on route planner
     */
    val onPlannerWaypointLocationView: ViewInteraction
        get() = onView(withText(R.string.msdkui_waypoint_select_location))

    /**
     * @return The [String] Choose waypoint location text on route planner
     */
    val onPlannerWaypointLocationText: String
        get() = getTextById(R.string.msdkui_waypoint_select_location)

    /**
     * @return The [Matcher]<[View]> "From:" text view on route planner
     */
    val onPlannerFromTextView: Matcher<View>
        get() = withText(startsWith(onPlannerFromText))

    /*
     * @return The [String] "From:" text on route planner
     */
    val onPlannerFromText: String
        get() = getTextById(R.string.msdkui_rp_from).replace(FROM_TO_PATTERN, "").trim()

    /**
     * @return The [Matcher]<[View]> "To:" text view on route planner
     */
    val onPlannerToTextView: Matcher<View>
        get() = withText(startsWith(onPlannerToText))

    /**
     * @return The [String] "From:" text on route planner
     */
    val onPlannerToText: String
        get() = getTextById(R.string.msdkui_rp_to).replace(FROM_TO_PATTERN, "").trim()

    /**
     * @return The [ViewInteraction] transport mode types on transportation panel
     */
    val onTransportationPanel: ViewInteraction
        get() = onView(withId(R.id.transport_panel))

    /**
     * @return The [ViewInteraction] option image on option panel
     */
    val onOptionPanel: ViewInteraction
        get() = onView(withId(R.id.option_panel))

    /**
     * @return The [ViewInteraction] travel time panel
     */
    val onTravelTimePanel: ViewInteraction
        get() = onView(withId(R.id.travel_time_panel))

    /**
     * @return The [ViewInteraction] 'OK' button on date picker
     */
    val onDatePickerOkButton: ViewInteraction
        get() = onView(withText(R.string.msdkui_ok))

    /**
     * @return The [ViewInteraction] date picker id
     */
    val onDatePicker: ViewInteraction
        get() = onView(withId(R.id.travel_date))

    /**
     * @return The [ViewInteraction] Given waypoint location view on route planner
     */
    fun onPlannerWaypointLocationView(textView: String?): ViewInteraction {
        return onView(withText(textView))
    }

    /**
     * @return The [ViewInteraction] Choose waypoint label on route planner
     */
    fun onPlannerWaypointLocationLabel(waypointItem: WaypointItem): ViewInteraction {
        return onView(withIndex(withId(R.id.waypoint_label), waypointItem.value))
    }

    /**
     * @return The [ViewInteraction] for transportation panel
     */
    fun onPlannerTransportPanel(transporType: TransporType): ViewInteraction {
        return onView(withContentDescription(transporType.value))
    }
}