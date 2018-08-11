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

package com.here.msdkuiapp.espresso.impl.views.map.useractions

import com.here.msdkuiapp.espresso.impl.core.CoreActions.tapIn
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.Constants.Gestures.LONG_PRESS
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.utils.DestinationData
import com.here.msdkuiapp.espresso.impl.views.map.screens.MapView.onMapFragmentWrapper
import com.here.msdkuiapp.espresso.impl.views.map.screens.MapView.onMapFragmentWrapperView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.utils.WaypointData

/**
 * Map view panel actions
 */
object MapActions {

    /**
     * Select waypoint items on map view by given coordinates
     */
    fun tapOnMap(waypointData: WaypointData): MapActions {
        waypointData.run { onMapFragmentWrapper.perform(tapIn(point.X, point.Y)) }
        return this
    }

    /**
     * Single tap to select destination point on map view by given coordinates
     */
    fun tapOnMap(destinationData: DestinationData): MapActions {
        destinationData.run { onMapFragmentWrapper.perform(tapIn(point.X, point.Y)) }
        return this
    }

    /**
     * Long press to select destination point on map view by given coordinates
     */
    fun longPressOnMap(destinationData: DestinationData): MapActions {
        destinationData.run { onMapFragmentWrapper.perform(tapIn(point.X, point.Y, gestures = LONG_PRESS)) }
        return this
    }

    /**
     * Wait for map view becomes enabled
     */
    fun waitForMapViewEnabled(): MapActions {
        onRootView.perform(waitForCondition(onMapFragmentWrapperView, isEnabled = true))
        return this
    }
}