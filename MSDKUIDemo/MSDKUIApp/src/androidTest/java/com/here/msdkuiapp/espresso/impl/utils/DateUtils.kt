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

package com.here.msdkuiapp.espresso.impl.utils

import java.util.Calendar
import java.util.Locale

/**
 * Date & time map view related utils object class
 */
object DateUtils {

    /**
     * @return [Int] current year
     */
    val currentYear: Int
        get() = Calendar.getInstance().get(Calendar.YEAR)

    /**
     * @return [String] current month name in long format
     */
    val currentMonth: String
        get() =  Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
}