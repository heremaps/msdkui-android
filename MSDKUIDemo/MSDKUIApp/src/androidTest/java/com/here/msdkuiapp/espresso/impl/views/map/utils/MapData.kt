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

package com.here.msdkuiapp.espresso.impl.views.map.utils

class MapData(val X: Double, val Y: Double) {

    /**
     * Generate random cooodinates for Map view
     */
    companion object {

        /**
         * @return [MapData] with X & Y coordinates
         */
        val randMapPoint: MapData get() = MapData(this.randMapCoord, this.randMapCoord)

        /**
         * @return [Double] map view coordinates in range 0.1 - 0.9
         */
        private val randMapCoord: Double get() = Math.random() * (0.9 - 0.1) + 0.1
    }
}