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

package com.here.msdkuiapp.espresso.impl.views.route.useractions

import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeUp
import android.support.test.espresso.action.ViewActions.swipeDown
import com.here.msdkuiapp.espresso.impl.views.route.matchers.RouteMatchers
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescriptionList
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescriptionView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.v7.widget.RecyclerView
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.viewIsDisplayed
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ROUTE_RESULT_1
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType
import com.here.msdkuiapp.espresso.impl.views.route.matchers.RouteBarMatchers
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onManeuverDescriptionList
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescArrival
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescDuration
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerBarActions

/**
 * Route panel actions
 */
object RouteActions {

    /**
     * Tap on route item on route list
     */
    fun tapRouteItemtOnDescList(routeItem: Int): RouteBarMatchers {
        // Tap on waypoint item on Route planner if displayed
        onRouteDescriptionList.check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(routeItem, click()))
        return RouteBarMatchers
    }

    /**
     * Wait for route description list becomes visible on route panel
     */
    fun waitRouteDescriptionList(): RoutePlannerBarActions {
        onRootView.perform(waitForCondition(onRouteDescriptionView))
        return RoutePlannerBarActions
    }

    /**
     * Wait for route description list becomes enabled on route panel
     */
    fun waitRouteDescriptionEnabled(): RoutePlannerBarActions {
        onRootView.perform(waitForCondition(onRouteDescriptionView, isEnabled = true))
        return RoutePlannerBarActions
    }

    /**
     * Swipe Up route description result list on route panel
     */
    fun swipeUpRouteList(): RouteActions {
        onRouteDescriptionList.perform(swipeUp())
        return this
    }

    /**
     * Swipe Down route description result list on route panel
     */
    fun swipeDownRouteList(): RouteActions {
        onRouteDescriptionList.perform(swipeDown())
        return this
    }

    /**
     * Check Route result list description information
     */
    fun checkRouteResultsList(transportType: TransportType): RouteActions {
        for (i in 0 until RouteMatchers.getItemsListCount(onRouteDescriptionList)) {
            RouteMatchers.checkRouteResultsItemsDisplayed(transportType, i)
            if (i == 1) swipeUpRouteList()
        }
        return this
    }

    /**
     * Check Scroll Up & Down Route result list
     */
    fun scrollUpAndDownRouteList(): RouteActions {
        val itemsCount = RouteMatchers.getItemsListCount(onRouteDescriptionList) - 1
        if (!viewIsDisplayed(onRouteDescArrival(itemsCount))) swipeUpRouteList()
        // Check the first route item became visible
        onRouteDescArrival(itemsCount)
        if (!viewIsDisplayed(onRouteDescDuration(ROUTE_RESULT_1))) swipeDownRouteList()
        // Check the last route item became visible
        onRouteDescArrival(ROUTE_RESULT_1)
        return this
    }

    /**
     * Scroll to the item by given position in maneuver list
     */
    fun scrollToManeuverItem(position: Int): RouteMatchers {
        onManeuverDescriptionList
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
        return RouteMatchers
    }
}