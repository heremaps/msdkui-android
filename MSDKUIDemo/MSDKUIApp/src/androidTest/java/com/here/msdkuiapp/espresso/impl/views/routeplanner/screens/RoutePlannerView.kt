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

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.view.View
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getTextById
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.viewIsDisplayed
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIndex
import com.here.msdkuiapp.espresso.impl.testdata.Constants.FROM_TO_PATTERN
import com.here.msdkuiapp.espresso.impl.testdata.Constants.TYPE_LORRY
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_TRUCK
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.Matcher

/**
 * Route planner ViewInteractions selectors
 */
object RoutePlannerView {

    /**
     * The [ViewInteraction] for the route planner list view.
     */
    val onPlannerWaypointList: ViewInteraction
        get() = onView(withId(R.id.waypoint_list))

    /**
     * The [String] Choose waypoint location text on route planner
     */
    val onPlannerWaypointLocationText: String
        get() = getTextById(R.string.msdkui_waypoint_select_location)

    /**
     * The [Matcher]<[View]> "From:" text view on route planner
     */
    val onPlannerFromTextView: Matcher<View>
        get() = withText(startsWith(onPlannerFromText))

    /**
     * The [String] "From:" text on route planner
     */
    val onPlannerFromText: String
        get() = getTextById(R.string.msdkui_rp_from).replace(FROM_TO_PATTERN, "").trim()

    /**
     * The [Matcher]<[View]> "To:" text view on route planner
     */
    val onPlannerToTextView: Matcher<View>
        get() = withText(startsWith(onPlannerToText))

    /**
     * The [String] "From:" text on route planner
     */
    val onPlannerToText: String
        get() = getTextById(R.string.msdkui_rp_to).replace(FROM_TO_PATTERN, "").trim()

    /**
     * The [ViewInteraction] transport mode types on transportation panel
     */
    val onTransportationPanel: ViewInteraction
        get() = onView(withId(R.id.transport_panel))

    /**
     * The [ViewInteraction] option image on option panel
     */
    val onOptionPanel: ViewInteraction
        get() = onView(withId(R.id.option_panel))

    /**
     * The [ViewInteraction] travel time panel
     */
    val onTravelTimePanel: ViewInteraction
        get() = onView(withId(R.id.travel_time_panel))

    /**
     * The [ViewInteraction] travel departure date/time
     */
    val onTravelDepartureDateTime: ViewInteraction
        get() = onView(withId(R.id.travel_time_details))

    /**
     * The [ViewInteraction] 'OK' button
     */
    val onOkButton: ViewInteraction
        get() = onView(withText(R.string.msdkui_ok))

    /**
     * The [ViewInteraction] 'Cancel' button
     */
    val onCancelButton: ViewInteraction
        get() = onView(withText(R.string.msdkui_cancel))

    /**
     * The [ViewInteraction] date picker by id
     */
    val onDatePicker: ViewInteraction
        get() = onView(withId(R.id.travel_date))

    /**
     * The [ViewInteraction] time picker by id
     */
    val onTimePicker: ViewInteraction
        get() = onView(withId(R.id.travel_time))

    /**
     * The [ViewInteraction] route planner instructions panel
     */
    val onRoutePlannerInstructions: ViewInteraction
        get() = onView(withId(R.id.route_planner_instructions))

    /**
     * The [ViewInteraction] for transportation type Lorry
     */
    private val onTransportPanelLorry: ViewInteraction
        get() = onView(withContentDescription(TYPE_LORRY))

    /**
     * The [ViewInteraction] Choose waypoint label on route planner
     */
    fun onPlannerWaypointLocationLabel(waypointItem: WaypointItem): ViewInteraction
            = onView(withIndex(withId(R.id.waypoint_label), waypointItem.index))

    /**
     * The [ViewInteraction] waypoint reorder DraggableImageView
     */
    fun onPlannerWaypointReorder(waypointItem: WaypointItem): ViewInteraction
            = onView(withIndex(withId(R.id.drag_icon), waypointItem.index))

    /**
     * The [ViewInteraction] for transportation panel
     */
    fun onPlannerTransportPanel(transportType: TransportType): ViewInteraction {
        when (transportType) {
            TYPE_TRUCK -> if (viewIsDisplayed(onTransportPanelLorry)) return onTransportPanelLorry
        }
        return onView(withContentDescription(transportType.value))
    }

    /**
     * The [Matcher]<[View]> for transportation panel view
     */
    fun onPlannerTransportPanelView(transportType: TransportType): Matcher<View> {
        when (transportType) {
            TYPE_TRUCK -> if (viewIsDisplayed(onTransportPanelLorry)) return withContentDescription(TYPE_LORRY)
        }
        return withContentDescription(transportType.value)
    }
}