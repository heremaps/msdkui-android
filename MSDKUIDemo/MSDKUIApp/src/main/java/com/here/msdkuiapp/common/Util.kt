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

package com.here.msdkuiapp.common

import com.here.msdkui.common.measurements.UnitSystem
import java.util.Locale

/**
 * Common util class.
 */
object Util {

    private const val UNITED_STATES_COUNTRY_CODE = "US"
    private const val UNITED_KINGDOM_COUNTRY_CODE = "GB"

    /**
     * Returns unit system for current locale.
     *
     * @return unit system [UnitSystem] for current locale.
     */
    fun getLocaleUnit(): UnitSystem {
          when (Locale.getDefault().country) {
              UNITED_STATES_COUNTRY_CODE -> return UnitSystem.IMPERIAL_US
              UNITED_KINGDOM_COUNTRY_CODE -> return UnitSystem.IMPERIAL_UK
              else -> return UnitSystem.METRIC
        }
    }
}
