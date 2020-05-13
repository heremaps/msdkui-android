/*
 * Copyright (C) 2017-2020 HERE Europe B.V.
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

import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
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
import com.here.msdkuiapp.espresso.impl.testdata.Constants.Gestures.SINGLE_TAP
import com.here.msdkuiapp.espresso.impl.views.map.screens.MapView.onMapFragmentWrapper
import com.here.msdkuiapp.espresso.impl.views.map.screens.MapView.onMapFragmentWrapperView
import com.here.msdkuiapp.espresso.impl.views.map.utils.MapData
import com.here.msdkuiapp.espresso.impl.views.routeplanner.utils.WaypointData

/**
 * Map view panel actions
 */
object MapActions: CoreActions() {

    /**
     * Tap MapView by [waypointData] location coordinates
     */
    fun tap(waypointData: WaypointData): MapActions {
        tap(waypointData.location)
        return this
    }

    /**
     * Tap MapView by [mapData] coordinates
     */
    fun tap(mapData: MapData): MapActions {
        mapData.run { onMapFragmentWrapper.perform(tapByGeoCoordinates(lat, lng)) }
        return this
    }

    /**
     * LongTap MapView by [mapData] coordinates
     */
    fun longTap(mapData: MapData): MapActions {
        mapData.run { onMapFragmentWrapper.perform(tapByGeoCoordinates(lat, lng, LONG_PRESS)) }
        return this
    }

    /**
     * Wait for map view becomes enabled
     */
    fun waitForMapViewEnabled(): MapActions {
        onRootView.perform(waitForCondition(onMapFragmentWrapperView, isEnabled = true))
        return this
    }

    /**
     * Single tap or Long Press on the view in given x & y.
     * @return [ViewAction]
     */
    private fun tapByGeoCoordinates(lat: Double, lng: Double, gestures: Constants.Gestures = SINGLE_TAP): ViewAction {
        return GeneralClickAction(
                gestures.value,
                CoordinatesProvider { view ->
                    val map = (view as MapView).map
                    val screenPos = IntArray(2)
                    view.getLocationOnScreen(screenPos)
                    val screenX: Float
                    val screenY: Float
                    map!!.setCenter(GeoCoordinate(lat, lng), Map.Animation.NONE)
                    screenX = screenPos[0] + view.width / 2f
                    screenY = screenPos[1] + view.height / 2f
                    floatArrayOf(screenX, screenY)
                },
                Press.FINGER,
                InputDevice.SOURCE_TOUCHSCREEN,
                MotionEvent.TOOL_TYPE_FINGER)
    }
}