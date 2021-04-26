/*
 * Copyright (C) 2017-2021 HERE Europe B.V.
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

package com.here.msdkuiapp.position

import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.GeoPosition
import com.here.android.mpa.common.MapEngine
import com.here.android.mpa.common.PositioningManager
import java.lang.ref.WeakReference

/**
 * Class to wrap sdk positioning manager to add more functionality to it.
 */
class AppPositioningManager private constructor(): PositioningManager.OnPositionChangedListener {

    companion object {

        @Volatile private var INSTANCE: AppPositioningManager? = null

        fun getInstance(): AppPositioningManager =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: AppPositioningManager().also { INSTANCE = it }
                }
    }

    /**
     * Sets a custom location for your current location. if not set, default custom location will be
     * your current location.
     */
    var customLocation: GeoCoordinate? = null

    /**
     * Determines if there is a valid position.
     */
    val isValidPosition
    get() = customLocation != null

    /**
     * Gets sdk positioning manager.
     */
    internal var sdkPositioningManager: PositioningManager? = null
        get() = field ?: if (MapEngine.isInitialized()) PositioningManager.getInstance() else null

    /**
     * Determines if positioning manager is active.
     */
    val isActive
        get() = sdkPositioningManager?.run { isActive } ?: false

    /**
     * Set listener to get notified when the first time position is available.
     */
    var listener: Listener? = null
        set(value) {
            sdkPositioningManager?.run {
                customLocation?.run {
                    value?.onPositionAvailable()
                } ?: run {
                    if (hasValidPosition()) {
                        customLocation = position.coordinate
                        value?.onPositionAvailable()
                    } else {
                        addListener(WeakReference(this@AppPositioningManager))
                    }
                }
            }
            field = value
        }

    /**
     * Init Positioning.
     *
     * @param positionChangeListener listener to get notified when position is available.
     */
    fun initPositioning(positionChangeListener: Listener) {
        sdkPositioningManager?.run {
            if (!isActive) {
                start(PositioningManager.LocationMethod.GPS_NETWORK)
            }
            listener = positionChangeListener
        }
    }

    /**
     * Stop Positioning.
     */
    fun stop() {
        sdkPositioningManager?.stop()
        reset()
    }

    /**
     * Reset the current location.
     */
    fun reset() {
        customLocation = null
    }

    override fun onPositionFixChanged(method: PositioningManager.LocationMethod?, status: PositioningManager.LocationStatus?) {

    }

    override fun onPositionUpdated(method: PositioningManager.LocationMethod?, position: GeoPosition?, isMapMatched: Boolean) {
        if (position?.isValid == true && sdkPositioningManager?.hasValidPosition() == true) {
            customLocation = position.coordinate
            listener?.onPositionAvailable()
            sdkPositioningManager?.removeListener(this@AppPositioningManager)
        }
    }

    /**
     * Callbacks notified when first time position is available.
     */
    interface Listener {

        /**
         * Indicated position is available.
         */
        fun onPositionAvailable()
    }
}