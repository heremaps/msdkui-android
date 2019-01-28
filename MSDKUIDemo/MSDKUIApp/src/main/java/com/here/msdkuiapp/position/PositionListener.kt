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

package com.here.msdkuiapp.position

import com.here.android.mpa.common.GeoPosition
import com.here.android.mpa.common.PositioningManager
import com.here.msdkuiapp.guidance.SingletonHelper.positioningManager
import java.lang.ref.WeakReference

/**
 * Listens & notified when first time position is available.
 */
class PositionListener : PositioningManager.OnPositionChangedListener {

    /**
     * Set listener to get notified when the first time position is available.
     */
    var listener: Listener? = null
        set(value) {
            positioningManager?.run {
                if (hasValidPosition()) {
                    value?.onPositionAvailable()
                } else {
                    addListener(WeakReference(this@PositionListener))
                }
            }
            field = value
        }

    override fun onPositionFixChanged(method: PositioningManager.LocationMethod?, status: PositioningManager.LocationStatus?) {

    }

    override fun onPositionUpdated(method: PositioningManager.LocationMethod?, position: GeoPosition?, isMapMatched: Boolean) {
        if (position?.isValid == true && positioningManager?.hasValidPosition() == true) {
            listener?.onPositionAvailable()
            positioningManager?.removeListener(this@PositionListener)
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