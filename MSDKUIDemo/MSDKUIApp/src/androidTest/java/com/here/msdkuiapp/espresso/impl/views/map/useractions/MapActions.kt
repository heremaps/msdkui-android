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

import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.CoordinatesProvider
import android.support.test.espresso.action.GeneralClickAction
import android.support.test.espresso.action.Press
import android.view.InputDevice
import android.view.MotionEvent
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.mapping.MapView
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.Constants
import com.here.msdkuiapp.espresso.impl.testdata.Constants.Gestures.LONG_PRESS
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.utils.DestinationData
import com.here.msdkuiapp.espresso.impl.views.map.screens.MapView.onMapFragmentWrapper
import com.here.msdkuiapp.espresso.impl.views.map.screens.MapView.onMapFragmentWrapperView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.utils.WaypointData

/**
 * Map view panel actions
 */
object MapActions: CoreActions() {

    /**
     * Select waypoint items on map view by given coordinates
     */
    fun tapByRelativeCoordinates(waypointData: WaypointData): MapActions {
        waypointData.run { onMapFragmentWrapper.perform(tapIn(point.X, point.Y)) }
        return this
    }

    /**
     * Select waypoint item on map view by given geo coordinates
     */
    fun tapByGeoCoordinates(waypointData: WaypointData): MapActions {
        waypointData.point.run { onMapFragmentWrapper.perform(tapByGeoCoordinates(X, Y)) }
        return this
    }

    /**
     * Single tap or Long Press on the view in given x & y.
     * @return [ViewAction]
     */
    private fun tapByGeoCoordinates(lat: Double, lng: Double, gestures: Constants.Gestures = Constants.Gestures.SINGLE_TAP): ViewAction {
        return GeneralClickAction(
                gestures.value,
                CoordinatesProvider { view ->
                    val map = (view as MapView).map
                    val pixelResult = map.projectToPixel(GeoCoordinate(lat, lng)).result
                    val screenPos = IntArray(2)
                    view.getLocationOnScreen(screenPos)
                    val screenX: Float
                    val screenY: Float
                    if (pixelResult.x > view.width || pixelResult.y > view.height) {
                        map.setCenter(GeoCoordinate(lat, lng), Map.Animation.NONE)
                        screenX = screenPos[0] + view.width / 2f
                        screenY = screenPos[1] + view.height / 2f
                    } else {
                        screenX = screenPos[0] + pixelResult.x
                        screenY = screenPos[1] + pixelResult.y
                    }
                    floatArrayOf(screenX, screenY)
                },
                Press.FINGER,
                InputDevice.SOURCE_TOUCHSCREEN,
                MotionEvent.TOOL_TYPE_FINGER)
    }

    /**
     * Single tap to select destination point on map view by given coordinates
     */
    fun tapByRelativeCoordinates(destinationData: DestinationData): MapActions {
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