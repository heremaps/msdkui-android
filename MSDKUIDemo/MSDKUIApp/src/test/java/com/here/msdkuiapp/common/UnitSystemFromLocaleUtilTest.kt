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

package com.here.msdkuiapp.common

import com.here.msdkui.common.measurements.UnitSystems
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Locale

/**
 * Tests for [UnitSystemFromLocaleUtil]
 */
class UnitSystemFromLocaleUtilTest {

    @Test
    fun testGet() {
        Locale.setDefault(Locale("en", "US"))
        assertEquals(UnitSystemFromLocaleUtil.get(), UnitSystems.IMPERIAL_US)
        Locale.setDefault(Locale("en", "GB"))
        assertEquals(UnitSystemFromLocaleUtil.get(), UnitSystems.IMPERIAL_UK)
        Locale.setDefault(Locale("pl", "PL"))
        assertEquals(UnitSystemFromLocaleUtil.get(), UnitSystems.METRIC)
    }
}