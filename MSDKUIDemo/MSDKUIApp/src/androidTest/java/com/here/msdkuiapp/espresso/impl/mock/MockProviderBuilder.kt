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

package com.here.msdkuiapp.espresso.impl.mock

import android.location.Criteria
import android.location.LocationManager

/**
 * Mock provider builder to create test location providers
 */
data class MockProviderBuilder(
        private var name: String? = null,
        private var requiresNetwork: Boolean = false,
        private val requiresSatellite: Boolean = false,
        private val requiresCell: Boolean = false,
        private val hasMonetaryCost: Boolean = false,
        private var supportsAltitude: Boolean = false,
        private var supportsSpeed: Boolean = false,
        private val supportsBearing: Boolean = false,
        private var powerRequirement: Int = Criteria.NO_REQUIREMENT,
        private var accuracy: Int = Criteria.ACCURACY_LOW) {

    fun add(locationManager: LocationManager) {
        locationManager.addTestProvider(name,
                requiresNetwork, requiresSatellite, requiresCell, hasMonetaryCost,
                supportsAltitude, supportsSpeed, supportsBearing,
                powerRequirement, accuracy)
    }
}