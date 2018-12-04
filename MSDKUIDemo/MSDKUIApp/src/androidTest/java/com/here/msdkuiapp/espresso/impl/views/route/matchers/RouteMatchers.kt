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

package com.here.msdkuiapp.espresso.impl.views.route.matchers

import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.v7.widget.RecyclerView
import android.view.View
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ROUTE_RESULT_1
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_CAR
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_TRUCK
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onDestinationText
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onManeuverAddress
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onManeuverDistance
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onManeuverIconType
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onManeuverInstruction
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onManeuverInstructionText
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescArrival
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescDelayInformation
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescDetails
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescDuration
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescIconType
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescLineBar
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescriptionList
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteOverviewItem
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onSeeManeuverSteps
import com.here.msdkuiapp.espresso.impl.views.route.useractions.RouteActions
import com.here.msdkuiapp.espresso.impl.views.route.useractions.RouteBarActions
import com.here.msdkuiapp.espresso.impl.views.route.utils.RouteData
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerMatchers.withDrawableIcons
import org.hamcrest.Description
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher

/**
 * Route overview panel matchers
 */
object RouteMatchers {

    /**
     * Get number of Route list items
     */
    fun getItemsListCount(viewInteraction: ViewInteraction): Int {
        var itemsCount = 0
        val matcher = object : TypeSafeMatcher<View>() {

            override fun matchesSafely(item: View): Boolean {
                itemsCount = (item as RecyclerView).adapter!!.itemCount
                return true
            }

            override fun describeTo(description: Description) {
                description.appendText("has $itemsCount items")
            }
        }
        viewInteraction.check(matches(matcher))
        return itemsCount
    }

    /**
     * Check Route result list description information
     */
    fun checkRouteResultsItemsDisplayed(transportType: TransportType, item: Int): RouteMatchers {
        // Check Duration
        onRouteDescDuration(item).check(matches(isDisplayed()))
        // Check Icon type
        onRouteDescIconType(item).check(matches(isDisplayed()))
        // Check Delay information for Car & Truck, except the Walk & Bicycle
        when (transportType) {
            TYPE_CAR, TYPE_TRUCK -> onRouteDescDelayInformation(item).check(matches(isDisplayed()))
        }
        // Check Distance | Street road names
        onRouteDescDetails(item).check(matches(isDisplayed()))
        // Check Arrival time
        onRouteDescArrival(item).check(matches(isDisplayed()))
        // Check Line progress bar
        onRouteDescLineBar(item).check(matches(isDisplayed()))
        return this
    }

    /**
     * Check Route item description information on route overview
     */
    fun checkRouteOverviewItemsDisplayed(): RouteMatchers {
        //FIXME: MSDKUI-1457
        onDestinationText.check(matches(isDisplayed()))
        // Check Route item on route overview
        onRouteOverviewItem.check(matches(isDisplayed()))
        // Check Duration
        //onRouteDescDuration.check(matches(isDisplayed()))
        // Check Distance | Street road names
        // onRouteDescDetails.check(matches(isDisplayed()))
        // Check Arrival time
        // onRouteDescArrival.check(matches(isDisplayed()))
        // Check Line progress bar DOES NOT displayed on route overview
        onRouteDescLineBar.check(matches(not(isDisplayed())))
        return this
    }

    /**
     * Check Route list description information updated
     */
    fun withUpdatedRouteData(routeData: RouteData): RouteMatchers {
        routeData.run {
            onRouteDescDuration(routeItem).check(matches(not(withText(duration))))
            onRouteDescDetails(routeItem).check(matches(not(withText(details))))
            onRouteDescArrival(routeItem).check(matches(not(withText(arrival))))
        }
        return this
    }

    /**
     * Check Route overview item data with selected from route display list
     */
    fun withRouteOverviewData(routeData: RouteData): RouteBarActions {
        routeData.run {
            onRouteDescDuration.check(matches(withText(duration)))
            onRouteDescDetails.check(matches(withText(details)))
            onRouteDescArrival.check(matches(withText(arrival)))
            when (transportType) {
                TYPE_CAR, TYPE_TRUCK -> onRouteDescDelayInformation.check(matches(withText(traffic)))
                else -> print("WALK & BICYCLE transportation types does not contain Traffic information!")
            }
        }
        return RouteBarActions
    }

    /**
     * Checks if maneuver steps button is visible.
     */
    fun checkSeeManeuverStepsButtonDisplayed(): RouteBarActions {
        onSeeManeuverSteps.check(matches(isDisplayed()))
        return RouteBarActions
    }

    /**
     * Check Route result list description information
     */
    fun checkManeuverResultList(): RouteBarActions {
        val itemsCount = getItemsListCount(RouteView.onManeuverDescriptionList)
        for (item in 0 until itemsCount) {
            RouteActions.scrollToManeuverItem(item).checkManeuverItemsDisplayed()
        }
        return RouteBarActions
    }

    /**
     * Check Maneuver item description information on route overview
     */
    private fun checkManeuverItemsDisplayed(index: Int = 1): RouteMatchers {
        val instruction = CoreMatchers.getText(onManeuverInstruction(index))
        // Check icon type in maneuver list item
        onManeuverIconType(index).check(matches(isDisplayed()))
        // Check instruction in maneuver list item
        onManeuverInstruction(index).check(matches(isDisplayed()))
        // Check location address in maneuver list item
        onManeuverAddress(index).check(matches(isDisplayed()))
        // Check distance information displaying in maneuver list item
        if (onManeuverInstructionText.contentEquals(instruction)) {
            // Check distance DOES NOT displayed in maneuver list for last 'Arrive' item
            onManeuverDistance(index).check(matches(not(isDisplayed())))
        } else {
            // Check distance displayed in maneuver list, except the last 'Arrive' item
            onManeuverDistance(index).check(matches(isDisplayed()))
        }
        return this
    }

    /**
     * Check transportation image icon for the first description list item
     */
    fun checkTransportIconDisplayed(transportType: TransportType): RouteMatchers {
        onRouteDescriptionList.check(matches(withDrawableIcons(transportType.imageIcon, ROUTE_RESULT_1)))
        return this
    }

    /**
     * Check transportation image icons for all description list items
     */
    fun checkAllTransportIconDisplayed(transportType: TransportType): RouteMatchers {
        val itemsCount: Int = getItemsListCount(onRouteDescriptionList)
        for (item in 0 until itemsCount) {
            onRouteDescriptionList.check(matches(withDrawableIcons(transportType.imageIcon, item)))
        }
        return this
    }
}