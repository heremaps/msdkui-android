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

    public static final int THOUSAND = 1000;

    private static final int METER_THRESHOLD_10 = 10;
    private static final int METER_THRESHOLD_200 = 200;
    private static final int METER_THRESHOLD_975 = 975; // special case where roundNear50 will result unit change.
    private static final int METER_THRESHOLD = 999;
    private static final int KM_THRESHOLD = 9950;
    private static final int KM_DIGIT = 2;

    private static final NumberFormat FORMATTER = NumberFormat.getInstance();

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
        if (distance < 0) { // invalid
            value = context.getString(R.string.msdkui_value_not_available);
            unit = context.getString(R.string.msdkui_unit_meter);
        } else if (distance < METER_THRESHOLD) {
            value = FORMATTER.format(distance);
            unit = context.getString(R.string.msdkui_unit_meter);
        } else if (distance < KM_THRESHOLD) {
            value = FORMATTER.format(roundToSignificantDigits((double) distance / THOUSAND, KM_DIGIT));
            unit = context.getString(R.string.msdkui_unit_kilometer);
        } else {
            value = FORMATTER.format(Math.round((double) distance / THOUSAND));
            unit = context.getString(R.string.msdkui_unit_kilometer);
        }
        return String.format(context.getString(R.string.msdkui_distance_value_with_unit), value, unit);
    }

    /**
     * Converts a given distance in meters to a string representation rounded to meters or kilometers.
     *
     * Some examples: --
     *
     * <ul>
     * <li> If the given distance is greater than 10 km, then the returned value will be rounded to its nearest division
     * of 1 km. For example, 10.37 km will be rounded to 10 km. </li>
     * <li> If the given distance is between 1 km and 10 km, then the returned value will be rounded to its nearest
     * division of 100 m. For example, 9.37 km will be rounded to 9.30 km. </li>
     * <li> If the given distance is between 200 m and 1 km, then the returned value will be rounded to its nearest
     * division of 50 m. For example, 340 m will be rounded to 300 m. </li>
     * <li> If the given distance is between 10 m and 200 m, then the returned value will be rounded to its nearest
     * division of 10 m. For example, 165 m will be rounded to 150 m. </li>
     * <li> If the given distance is less than 10 m, then the returned value will be rounded to its nearest division of
     * 1 m. For example, 1.3 m will be rounded to 1 m. </li>
     * </ul>
     *
     * @param context
     *         the required context.
     * @param distance
     *         the distance in meters.
     *
     * @return a string representation including unit or "-- m" if distance is smaller than 0.
     */
    public static String formatDistanceForUI(final Context context, long distance) {
        final String value;
        final String unit;
        if (distance < METER_THRESHOLD_975) {
            unit = context.getString(R.string.msdkui_unit_meter);
        } else {
            unit = context.getString(R.string.msdkui_unit_kilometer);
        }
        if (distance < 0) { // invalid
            value = context.getString(R.string.msdkui_value_not_available);
        } else if (distance < METER_THRESHOLD_10) {
            value = FORMATTER.format(distance);
        } else if (distance < METER_THRESHOLD_200) {
            value = FORMATTER.format(roundNear10(distance));
        } else if (distance < METER_THRESHOLD_975) {
            value = FORMATTER.format(roundNear50(distance));
        } else if (distance < METER_THRESHOLD) {
            value = FORMATTER.format(roundToSignificantDigits((double) roundNear50(distance) / THOUSAND, KM_DIGIT));
        } else if (distance < KM_THRESHOLD) {
            value = FORMATTER.format(roundToSignificantDigits((double) distance / THOUSAND, KM_DIGIT));
        } else {
            value = FORMATTER.format(Math.round((double) distance / THOUSAND));
        }
        return String.format(context.getString(R.string.msdkui_distance_value_with_unit), value, unit);
    }

    /**
     * Rounds the given number to it's nearest division of 50.
     *
     * Some examples: --
     * <ul>
     * <li> 162 will be rounded to 150. same way 180 will be rounded to 200. </li>
     * </ul>
     *
     * @param number
     *         input number for rounding off.
     *
     * @return rounded result.
     */
    private static double roundNear50(long number) {
        return (Math.round(((double) number / 100) * 2) / 2.0) * 100;
    }

    /**
     * Rounds the given number to it's nearest division of 10.
     *
     * Some examples: --
     * <ul>
     * <li> 162 will be rounded to 160. same way 157 will be rounded to 160 </li>
     * </ul>
     *
     * @param number
     *         input number for rounding off.
     *
     * @return rounded result.
     */
    private static double roundNear10(long number) {
        return Math.round((double) number / 10) * 10;
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
