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

package com.here.msdkui.common;

import android.content.Context;

import com.here.msdkui.R;

import java.text.NumberFormat;

/**
 * A convenience class to convert distance in meters to a string representation.
 */
public final class DistanceFormatterUtil {

    private static final int METER_THRESHOLD = 998;
    private static final int KM_THRESHOLD = 9950;
    private static final int THOUSAND = 1000;
    private static final int KM_DIGIT = 2;

    private DistanceFormatterUtil() {
        // empty constructor
    }

    /**
     * Converts given distance in meters to a string representation rounded to meters or kilometers.
     *
     * @param context
     *         the required context.
     * @param distance
     *         the distance in meters.
     *
     * @return a string representation including unit or "-- m" if distance is smaller than 0.
     */
    public static String format(final Context context, final long distance) {
        final String value;
        final String unit;
        final NumberFormat numberFormat = NumberFormat.getInstance();
        if (distance < 0) { // invalid
            value = context.getString(R.string.msdkui_distance_value_not_available);
            unit = context.getString(R.string.msdkui_unit_meter);
        } else if (distance < METER_THRESHOLD) {
            value = numberFormat.format(distance);
            unit = context.getString(R.string.msdkui_unit_meter);
        } else if (distance < KM_THRESHOLD) {
            value = numberFormat.format(roundToSignificantDigits((double) distance / THOUSAND, KM_DIGIT));
            unit = context.getString(R.string.msdkui_unit_kilometer);
        } else {
            value = numberFormat.format(Math.round((double) distance / THOUSAND));
            unit = context.getString(R.string.msdkui_unit_kilometer);
        }
        return String.format(context.getString(R.string.msdkui_distance_value_with_unit), value, unit);
    }

    /**
     * Round to given significant digit.
     *
     * @param number
     *         input number.
     * @param significantDigits
     *         digits that should be considered for rounding.
     *
     * @return a rounded value.
     */
    private static double roundToSignificantDigits(final double number, final int significantDigits) {
        if (number == 0) {
            return 0;
        }

        final int exponent = (int) (Math.floor(Math.log10(Math.abs(number))) + 1 - significantDigits);
        final double factor = Math.pow(10.0, exponent);

        return Math.round(number / factor) * factor;
    }
}
