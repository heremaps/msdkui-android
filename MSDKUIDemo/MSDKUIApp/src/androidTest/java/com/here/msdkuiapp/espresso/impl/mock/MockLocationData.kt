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

import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TestPlace
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TestPlace.TEST_PLACE_GERMANY_BERLIN

/**
 * Mock location data provider to use in UI tests
 * @param mockLocation [MockLocation] to start / stop mocking location
 * @param isMocked [Boolean] false by default to disable mock location
 * @param testPlace [TestPlace] enum with test places coordinates
 */
data class MockLocationData(
        var mockLocation: MockLocation? = null,
        var isMocked: Boolean = false,
        var testPlace: TestPlace = TEST_PLACE_GERMANY_BERLIN)