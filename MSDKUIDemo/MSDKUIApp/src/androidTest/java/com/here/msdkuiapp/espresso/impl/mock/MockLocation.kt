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

import android.content.Context
import android.location.LocationManager
import android.util.Log
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.GeoPosition
import com.here.android.mpa.common.MapEngine
import com.here.android.mpa.common.PositioningManager
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TestPlace
import com.here.msdkuiapp.espresso.impl.utils.PreconditionsUtils.checkNotNull
import junit.framework.Assert.fail
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * Mock location set mock location position
 */
class MockLocation(private val targetContext: Context, private var mockLocationProvider: MockLocationManager? = null) {

    private val logTag = MockLocation::class.java.simpleName

    fun setMockLocation(testPlace: TestPlace) {
        startMocking()
        Log.d(logTag, String.format("Set mock location lat: - [%s], long - [%s]",
                testPlace.latitude, testPlace.longitude))
        val testMockLocationProvider = checkNotNull(mockLocationProvider,
                "can't set mock location before provider is started!")
        testMockLocationProvider.start(testPlace)
        waitForPositionFix(testPlace)
    }

    fun stopMocking() {
        Log.d(logTag, "Stop mocking location")
        shutDownMockLocationProvider(mockLocationProvider)
        mockLocationProvider = null
    }

    private fun startMocking() {
        Log.d(logTag, "Start mocking location")
        shutDownMockLocationProvider(mockLocationProvider)
        val context = checkNotNull(targetContext)
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        waitUntilProviderIsReady(checkNotNull(locationManager), LocationManager.GPS_PROVIDER)
        mockLocationProvider = MockLocationManager(locationManager)
    }

    private fun shutDownMockLocationProvider(mockLocationProvider: MockLocationManager?) {
        Log.d(logTag, "Shout down mock location provider")
        if (mockLocationProvider == null) {
            return
        }
        if (mockLocationProvider.isStarted) {
            mockLocationProvider.stop()
        }
    }

    private fun waitUntilProviderIsReady(locationManager: LocationManager, providerName: String) {
        if (!isLocationProviderActive(locationManager, providerName)) {
            throw IllegalStateException(String.format(
                    "Provider: - [%s] not found in list of active providers: - [%s]. "
                            + "Either the location provider is disabled or not initialized. "
                            + "Try changing location settings on device.",
                    providerName, locationManager.allProviders)
            )
        }
    }

    private fun isLocationProviderActive(locationManager: LocationManager, providerName: String): Boolean {
        return locationManager.allProviders.stream().anyMatch { p -> p.equals(providerName, ignoreCase = true) }
    }

    private fun waitForPositionFix(testPlace: TestPlace) {
        waitForPositionFixed(object: GeoCoordinateCondition {
            override fun check(coordinate: GeoCoordinate): Boolean {
                val coordinate2 = GeoCoordinate(testPlace.latitude, testPlace.longitude)
                return coordinate2.distanceTo(coordinate) < testPlace.distanceAccuracy
            }
        })
    }

    private fun waitForPositionFixed(condition: GeoCoordinateCondition) {
        val positioningManager = getPositioningManager

        var latch = CountDownLatch(1)
        // need to be stack variable here to avoid GC collecting this
        val listener = object : PositioningManager.OnPositionChangedListener {

            var locationStatus: PositioningManager.LocationStatus = PositioningManager.LocationStatus.OUT_OF_SERVICE

            override fun onPositionUpdated(locationMethod: PositioningManager.LocationMethod,
                                           position: GeoPosition, isMapMatched: Boolean) {
                if (position.isValid && condition.check(position.coordinate)
                        && locationStatus == PositioningManager.LocationStatus.AVAILABLE && latch.count > 0) {
                    latch.countDown()
                }
            }

            override fun onPositionFixChanged(arg0: PositioningManager.LocationMethod,
                                              locStatus: PositioningManager.LocationStatus) {
                locationStatus = locStatus
            }
        }

        positioningManager?.addListener(WeakReference<PositioningManager.OnPositionChangedListener>(listener))

        try {
            latch.await(500, MILLISECONDS)
        } catch (e: InterruptedException) {
            fail("Timeout waiting for Location status available!")
        }

    }

    private val getPositioningManager: PositioningManager?
            = if (MapEngine.isInitialized()) PositioningManager.getInstance() else null

    @FunctionalInterface
    interface GeoCoordinateCondition {
        fun check(coordinate: GeoCoordinate): Boolean
    }

}