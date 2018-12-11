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
import java.util.Locale

/**
 * Util class to get unit system for current locale.
 */
object UnitSystemFromLocaleUtil {

    private const val UNITED_STATES_COUNTRY_CODE = "US"
    private const val UNITED_KINGDOM_COUNTRY_CODE = "GB"

    /**
     * Returns unit system for current locale.
     *
     * @return unit system [UnitSystems] for current locale.
     */
    fun get(): UnitSystems {
          when (Locale.getDefault().country) {
              UNITED_STATES_COUNTRY_CODE -> return UnitSystems.IMPERIAL_US
              UNITED_KINGDOM_COUNTRY_CODE -> return UnitSystems.IMPERIAL_UK
              else -> return UnitSystems.METRIC
        }
    }
}
