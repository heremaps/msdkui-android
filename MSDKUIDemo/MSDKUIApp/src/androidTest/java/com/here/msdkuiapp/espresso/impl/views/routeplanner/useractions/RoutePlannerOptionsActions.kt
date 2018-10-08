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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions

import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.RouteType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.AvoidTrafficType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TunnelsAllowedType
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerOptionsView.onOptionsPanelAvoidTrafficLabelView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerOptionsView.onOptionsPanelSpinnerView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerOptionsView.onOptionsPanelRouteOptionsSettingView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerOptionsView.onOptionsPanelContextMenuListView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerOptionsView.onOptionsPanelHazardousMaterialsSettingView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerOptionsView.onOptionsPanelRouteTypeLabelView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerOptionsView.onOptionsPanelTruckOptionsSettingView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerOptionsView.onOptionsPanelTunnelsAllowedLabelView

/**
 * Options panel specific actions
 */
object RoutePlannerOptionsActions {

    /**
     * Tap on Route Options setting on options panel
     */
    fun tapOnRouteOptionsItem(): RoutePlannerBarActions {
        onOptionsPanelRouteOptionsSettingView.check(matches(isDisplayed())).perform(click())
        return RoutePlannerBarActions
    }

    /**
     * Tap on Hazardous materials setting on options panel
     */
    fun tapOnHazardousMaterialsItem(): RoutePlannerBarActions {
        onOptionsPanelHazardousMaterialsSettingView.check(matches(isDisplayed())).perform(click())
        return RoutePlannerBarActions
    }

    /**
     * Tap on Truck options setting on options panel
     */
    fun tapOnTruckOptionsItem(): RoutePlannerBarActions {
        onOptionsPanelTruckOptionsSettingView.check(matches(isDisplayed())).perform(click())
        return RoutePlannerBarActions
    }

    /**
     * Tap on Route type setting on options panel
     */
    fun tapOnRouteTypeItem(item: Int = 0): RoutePlannerOptionsActions {
        onOptionsPanelRouteTypeLabelView.check(matches(isDisplayed()))
        onOptionsPanelSpinnerView(item).perform(click())
        return this
    }

    /**
     * Select Route Type item in context list view on option panel
     */
    fun selectRouteTypeItem(routeType: RouteType): RoutePlannerBarActions {
        onOptionsPanelContextMenuListView(routeType.item).check(matches(isDisplayed())).perform(click())
        return RoutePlannerBarActions
    }

    /**
     * Tap on Avoid Traffic setting on options panel
     */
    fun tapOnAvoidTrafficItem(item: Int = 0): RoutePlannerOptionsActions {
        onOptionsPanelAvoidTrafficLabelView.check(matches(isDisplayed()))
        onOptionsPanelSpinnerView(item).perform(click())
        return this
    }

    /**
     * Select 'Avoid Traffic' item in context list view on option panel
     */
    fun selectAvoidTrafficItem(avoidTrafficType: AvoidTrafficType): RoutePlannerBarActions {
        onOptionsPanelContextMenuListView(avoidTrafficType.item).check(matches(isDisplayed())).perform(click())
        return RoutePlannerBarActions
    }

    /**
     * Tap on 'Tunnels Allowed' setting on options panel
     */
    fun tapOnTunnelsAllowedItem(item: Int = 1): RoutePlannerOptionsActions {
        onOptionsPanelTunnelsAllowedLabelView.check(matches(isDisplayed()))
        onOptionsPanelSpinnerView(item).perform(click())
        return this
    }

    /**
     * Select 'Tunnels Allowed' item in context list view on option panel
     */
    fun selectTunnelsAllowedItem(tunnelsAllowedType: TunnelsAllowedType): RoutePlannerBarActions {
        onOptionsPanelContextMenuListView(tunnelsAllowedType.item).check(matches(isDisplayed())).perform(click())
        return RoutePlannerBarActions
    }
}