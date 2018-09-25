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
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TestPlace

/**
 * Mock location manager to start and stop GPS and Network location providers
 */
class MockLocationManager(private val locationManager: LocationManager) {

    private val GPS_PROVIDER = LocationManager.GPS_PROVIDER
    private val NETWORK_PROVIDER = LocationManager.NETWORK_PROVIDER

    var isStarted: Boolean = false
        private set

    fun stop() {
        if (!isStarted) return
        stopProvider(GPS_PROVIDER)
        stopProvider(NETWORK_PROVIDER)
        isStarted = false
    }

    fun start(testPlace: TestPlace): Boolean {
        if (isStarted) stop()
        startGpsProvider()
        startNetworkProvider()
        val location = createMockLocation(testPlace)
        locationManager.setTestProviderLocation(location.provider, location)
        return true
    }

    private fun stopProvider(providerName: String) {
        with(locationManager) {
            setTestProviderEnabled(providerName, false)
            clearTestProviderEnabled(providerName)
            clearTestProviderLocation(providerName)
            clearTestProviderStatus(providerName)
            removeTestProvider(providerName)
        }
    }

    private fun startGpsProvider() {
        MockProviderBuilder(
                name = GPS_PROVIDER,
                supportsAltitude = true,
                supportsSpeed = true,
                powerRequirement = Criteria.POWER_LOW,
                accuracy = Criteria.ACCURACY_HIGH)
                .add(locationManager)

        locationManager.setTestProviderEnabled(GPS_PROVIDER, true)
    }

    private fun startNetworkProvider() {
        MockProviderBuilder(
                name = NETWORK_PROVIDER,
                requiresNetwork = true,
                powerRequirement = Criteria.POWER_LOW,
                accuracy = Criteria.ACCURACY_LOW)
                .add(locationManager)

        locationManager.setTestProviderEnabled(NETWORK_PROVIDER, true)
    }

    private fun createMockLocation(testPlace: TestPlace): Location {
        // Create a new Location
        val result = Location(LocationManager.GPS_PROVIDER)
        with(testPlace) {
            result.latitude = latitude
            result.longitude = longitude
            result.altitude = altitude.toDouble()
            result.accuracy = distanceAccuracy
            result.time = System.currentTimeMillis()
            result.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }
        return result
    }
}
