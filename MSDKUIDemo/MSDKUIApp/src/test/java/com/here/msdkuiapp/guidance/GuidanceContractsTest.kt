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

package com.here.msdkuiapp.guidance

import com.here.msdkui.guidance.GuidanceManeuverData
import com.here.msdkuiapp.GuidanceContracts
import junit.framework.Assert.assertNotNull
import org.junit.Test

/**
 * Test for [GuidanceContracts].
 */
class GuidanceContractsTest {

    @Test
    fun testInit() {
        val contracts = GuidanceContract()
        assertNotNull(contracts.getCurrentViewContract())
    }

    class GuidanceContract : GuidanceContracts.ManeuverPanel {
        override fun onManeuverData(data: GuidanceManeuverData) {

        }

        override fun onDestinationReached() {

        }

        override fun getCurrentViewContract(): GuidanceContracts.ManeuverPanel {
            return this
        }

    }
}