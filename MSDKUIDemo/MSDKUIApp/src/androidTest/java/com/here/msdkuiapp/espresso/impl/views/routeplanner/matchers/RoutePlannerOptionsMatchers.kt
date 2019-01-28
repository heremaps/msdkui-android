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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.espresso.impl.testdata.Constants.CAR_AVOID_TRAFFIC
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.RouteType.TYPE_FASTEST
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.AvoidTrafficType.TYPE_BEST
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_SCOOTER
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_CAR
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_WALK
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_TRUCK
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_BICYCLE
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TunnelsAllowedType.NOT_DEFINED
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarOptionsTitleView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerOptionsActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerBarActions


/**
 * Options panel specific matchers on Route Planner
 */
object RoutePlannerOptionsMatchers {

    /**
     * Check options view title on actionbar
     */
    fun checkOptionsDisplayed(): RoutePlannerOptionsMatchers {
        onPlannerBarOptionsTitleView.check(matches(isDisplayed()))
        return this
    }

    /**
     * Check options items for particular transportation mode
     */
    fun checkOptionsForTransportType(transportType: TransportType): RoutePlannerBarActions {
        when (transportType) {
            TYPE_CAR -> checkCarTransportModeOptions()
            TYPE_TRUCK -> checkTruckTransportModeOptions()
            TYPE_WALK, TYPE_BICYCLE -> checkWalkAndBicycleTransportModeOptions()
            TYPE_SCOOTER -> checkScooterTransportModeOptions()
        }
        return RoutePlannerBarActions
    }

    /**
     * Check 'Car' transportation mode related options
     */
    private fun checkCarTransportModeOptions() {
        // Check 'Route type' setting on option panel
        RoutePlannerOptionsActions.tapOnRouteTypeItem().selectRouteTypeItem(TYPE_FASTEST)
        // Check 'Avoid traffic' setting on option panel
        RoutePlannerOptionsActions.tapOnAvoidTrafficItem(CAR_AVOID_TRAFFIC).selectAvoidTrafficItem(TYPE_BEST)
        // Check 'Route Options' setting on option panel
        RoutePlannerOptionsActions.tapOnRouteOptionsItem().tapOnBackRoutePlannerButton()
    }

    /**
     * Check 'Truck' transportation mode related options
     */
    private fun checkTruckTransportModeOptions() {
        // Check 'Avoid traffic' setting on option panel
        RoutePlannerOptionsActions.tapOnAvoidTrafficItem().selectAvoidTrafficItem(TYPE_BEST)
        // Check 'Route Options' setting on option panel
        RoutePlannerOptionsActions.tapOnRouteOptionsItem().tapOnBackRoutePlannerButton()
        // Check 'Tunnels Allowed' setting on option panel
        RoutePlannerOptionsActions.tapOnTunnelsAllowedItem().selectTunnelsAllowedItem(NOT_DEFINED)
        // Check 'Hazardous materials' setting on option panel
        RoutePlannerOptionsActions.tapOnHazardousMaterialsItem().tapOnBackRoutePlannerButton()
        // Check 'Truck options' setting on option panel
        RoutePlannerOptionsActions.tapOnTruckOptionsItem().tapOnBackRoutePlannerButton()
    }

    /**
     * Check 'Walk' & 'Bicycle' transportation mode related options
     */
    private fun checkWalkAndBicycleTransportModeOptions() {
        // Check 'Route Options' setting on option panel
        RoutePlannerOptionsActions.tapOnRouteOptionsItem().tapOnBackRoutePlannerButton()
    }

    /**
     * Check 'Scooter' transportation mode related options
     */
    private fun checkScooterTransportModeOptions() {
        // Check 'Avoid traffic' setting on option panel
        RoutePlannerOptionsActions.tapOnAvoidTrafficItem().selectAvoidTrafficItem(TYPE_BEST)
        // Check 'Route Options' setting on option panel
        RoutePlannerOptionsActions.tapOnRouteOptionsItem().tapOnBackRoutePlannerButton()
    }
}