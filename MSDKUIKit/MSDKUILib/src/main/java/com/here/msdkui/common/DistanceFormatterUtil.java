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

package com.here.msdkui.common;

import android.content.Context;

import com.here.msdkui.R;
import com.here.msdkui.common.measurements.LengthConverter;
import com.here.msdkui.common.measurements.Measurement;
import com.here.msdkui.common.measurements.MeasurementUnit;
import com.here.msdkui.common.measurements.UnitSystem;

import java.text.NumberFormat;

/**
 * A convenience class to convert distance in meters to a string representation.
 */
public final class DistanceFormatterUtil {

    public static final int THOUSAND = 1000;
    private static final int TEN = 10;
    private static final int TWO_DIGITS = 2;

    private static final int METER_THRESHOLD_10 = 10;
    private static final int METER_THRESHOLD_200 = 200; // This is 1/5 of one kilometer.
    private static final int METER_THRESHOLD_975 = 975; // Special case where roundNear50 will result in a unit change.
    private static final int METER_THRESHOLD = 999;
    private static final int KM_THRESHOLD = 9950;

    private static final int YARDS_THRESHOLD_10 = 10;
    private static final int YARDS_THRESHOLD_350 = 350; // This is around 1/5 of one mile.
    private static final int YARDS_THRESHOLD_1750 = 1750; // Special case where roundNear50 will result in a unit change.
    private static final int YARDS_THRESHOLD = 1759;

    private static final int FEET_THRESHOLD_10 = 10;
    private static final int FEET_THRESHOLD_1050 = 1050; // This is around 1/5 of one mile.
    private static final int FEET_THRESHOLD_5275 = 5275; // Special case where roundNear50 will result in a unit change.
    private static final int FEET_THRESHOLD = 5279;


    private static final NumberFormat FORMATTER = NumberFormat.getInstance();

    private DistanceFormatterUtil() {
    }

    /**
     * Converts a given distance in meters to a string representation. The distance will be rounded
     * based on the provided unit system. See:
     * {@link DistanceFormatterUtil#formatInMetricSystem(Context, long)},
     * {@link DistanceFormatterUtil#formatInImperialUsSystem(Context, long)},
     * {@link DistanceFormatterUtil#formatInImperialUkSystem(Context, long)}.
     *
     * @param context
     *         the required context.
     * @param distance
     *         the distance in meters.
     * @param system
     *         the unit system {@link UnitSystem}.
     *
     * @return a string representation including unit.
     */
    public static String format(final Context context, final long distance, final UnitSystem system) {
        switch (system) {
            case METRIC:
                return formatInMetricSystem(context, distance);
            case IMPERIAL_UK:
                return formatInImperialUkSystem(context, distance);
            case IMPERIAL_US:
                return formatInImperialUsSystem(context, distance);
            default: // metric
                return formatInMetricSystem(context, distance);
        }
    }

    /**
     * Converts given distance in meters to a string representation rounded to meters or kilometers.
     *
     * @param context
     *         the required context.
     * @param distance
     *         the distance in meters.
     *
     * @return a string representation including unit.
     */
    public static String formatInMetricSystem(final Context context, final long distance) {
        final String value;
        final String unit;
        if (distance < METER_THRESHOLD) {
            value = FORMATTER.format(distance);
            unit = context.getString(R.string.msdkui_unit_meter);
        } else if (distance < KM_THRESHOLD) {
            value = FORMATTER.format(roundToSignificantDigits((double) distance / THOUSAND, TWO_DIGITS));
            unit = context.getString(R.string.msdkui_unit_kilometer);
        } else {
            value = FORMATTER.format(Math.round((double) distance / THOUSAND));
            unit = context.getString(R.string.msdkui_unit_kilometer);
        }
        return String.format(context.getString(R.string.msdkui_distance_value_with_unit), value, unit);
    }

    /**
     * Converts given distance in meters to a string representation rounded to yards or miles.
     *
     * @param context
     *         the required context.
     * @param distance
     *         the distance in meters.
     *
     * @return a string representation including unit.
     */
    public static String formatInImperialUsSystem(final Context context, final long distance) {
        final String value;
        final String unit;
        LengthConverter lengthConverter = new LengthConverter();
        Measurement distanceMeters = new Measurement(distance, MeasurementUnit.METER);
        final double distanceYards = lengthConverter.convert(distanceMeters.getValue(),
                distanceMeters.getUnit(), MeasurementUnit.YARD).getValue();
        final double distanceMiles = lengthConverter.convert(distanceMeters.getValue(),
                distanceMeters.getUnit(), MeasurementUnit.MILE).getValue();
        if (distanceYards < YARDS_THRESHOLD) {
            value = FORMATTER.format(Math.round(distanceYards));
            unit = context.getString(R.string.msdkui_unit_yard);
        } else if (distanceMiles <= TEN) {
            value = FORMATTER.format(roundToSignificantDigits(distanceMiles, TWO_DIGITS));
            unit = context.getString(R.string.msdkui_unit_mile);
        } else {
            value = FORMATTER.format(Math.round(distanceMiles));
            unit = context.getString(R.string.msdkui_unit_mile);
        }
        return String.format(context.getString(R.string.msdkui_distance_value_with_unit), value, unit);
    }

    /**
     * Converts given distance in meters to a string representation rounded to feet or miles.
     *
     * @param context
     *         the required context.
     * @param distance
     *         the distance in meters.
     *
     * @return a string representation including unit.
     */
    public static String formatInImperialUkSystem(final Context context, final long distance) {
        final String value;
        final String unit;
        LengthConverter lengthConverter = new LengthConverter();
        Measurement distanceMeters = new Measurement(distance, MeasurementUnit.METER);
        final double distanceFeet = lengthConverter.convert(distanceMeters.getValue(),
                distanceMeters.getUnit(), MeasurementUnit.FOOT).getValue();
        final double distanceMiles = lengthConverter.convert(distanceMeters.getValue(),
                distanceMeters.getUnit(), MeasurementUnit.MILE).getValue();
        if (distanceFeet < FEET_THRESHOLD) {
            value = FORMATTER.format(Math.round(distanceFeet));
            unit = context.getString(R.string.msdkui_unit_foot);
        } else if (distanceMiles <= TEN) {
            value = FORMATTER.format(roundToSignificantDigits(distanceMiles, TWO_DIGITS));
            unit = context.getString(R.string.msdkui_unit_mile);
        } else {
            value = FORMATTER.format(Math.round(distanceMiles));
            unit = context.getString(R.string.msdkui_unit_mile);
        }
        return String.format(context.getString(R.string.msdkui_distance_value_with_unit), value, unit);
    }

    /**
     * Converts given distance in meters to a string representation rounded to specified unit system.
     * For details of unit system specific see:
     * {@link DistanceFormatterUtil#formatDistanceInMetricSystem(Context, long)},
     * {@link DistanceFormatterUtil#formatDistanceInImperialUkSystem(Context, long)},
     * {@link DistanceFormatterUtil#formatDistanceInImperialUsSystem(Context, long)}.
     *
     * @param context
     *         the required context.
     * @param distance
     *         the distance in meters.
     * @param system
     *         the unit system {@link UnitSystem}.
     *
     * @return a string representation including unit.
     */
    public static String formatDistance(final Context context, final long distance, final UnitSystem system) {
        switch (system) {
            case METRIC:
                return formatDistanceInMetricSystem(context, distance);
            case IMPERIAL_UK:
                return formatDistanceInImperialUkSystem(context, distance);
            case IMPERIAL_US:
                return formatDistanceInImperialUsSystem(context, distance);
            default: // metric
                return formatDistanceInMetricSystem(context, distance);
        }
    }

    /**
     * Converts a given distance in meters to a string representation rounded to meters or kilometers.
     *
     * Some examples:
     *
     * <ul>
     * <li> If the given distance is greater than 10 km, then the returned value will be rounded to its nearest division
     * of 1 km. For example, 10.37 km will be rounded to 10 km. </li>
     * <li> If the given distance is between 1 km and 10 km, then the returned value will be rounded to its nearest
     * division of 100 m. For example, 9.37 km will be rounded to 9.30 km. </li>
     * <li> If the given distance is between 200 m and 1 km, then the returned value will be rounded to its nearest
     * division of 50 m. For example, 340 m will be rounded to 300 m. </li>
     * <li> If the given distance is between 10 m and 200 m, then the returned value will be rounded to its nearest
     * division of 10 m. For example, 165 m will be rounded to 170 m. </li>
     * <li> If the given distance is less than 10 m, then the returned value will be rounded to its nearest division of
     * 1 m. For example, 1.3 m will be rounded to 1 m. </li>
     * </ul>
     *
     * @param context
     *         the required context.
     * @param distance
     *         the distance in meters.
     *
     * @return a string representation including unit.
     */
    public static String formatDistanceInMetricSystem(final Context context, final long distance) {
        final String value;
        final String unit;
        if (distance < METER_THRESHOLD_975) {
            unit = context.getString(R.string.msdkui_unit_meter);
        } else {
            unit = context.getString(R.string.msdkui_unit_kilometer);
        }
        if (distance < METER_THRESHOLD_10) {
            value = FORMATTER.format(distance);
        } else if (distance < METER_THRESHOLD_200) {
            value = FORMATTER.format(roundNear10(distance));
        } else if (distance < METER_THRESHOLD_975) {
            value = FORMATTER.format(roundNear50(distance));
        } else if (distance < METER_THRESHOLD) {
            value = FORMATTER.format(roundToSignificantDigits((double) roundNear50(distance) / THOUSAND, TWO_DIGITS));
        } else if (distance <= KM_THRESHOLD) {
            value = FORMATTER.format(roundToSignificantDigits((double) distance / THOUSAND, TWO_DIGITS));
        } else {
            value = FORMATTER.format(Math.round((double) distance / THOUSAND));
        }
        return String.format(context.getString(R.string.msdkui_distance_value_with_unit), value, unit);
    }

    /**
     * Converts a given distance in meters to a string representation rounded to yards or miles.
     *
     * Some examples:
     *
     * <ul>
     * <li> If the given distance is greater than 10 miles, then the returned value will be rounded to its nearest division
     * of 1 mile. For example, 10.37 miles will be rounded to 10 miles. </li>
     * <li> If the given distance is between 1 mile and 10 miles, then the returned value will be rounded to its nearest
     * division of 0.1 mile. For example, 9.37 miles will be rounded to 9.30 miles. </li>
     * <li> If the given distance is between 350 yd and 1 mile, then the returned value will be rounded to its nearest
     * division of 50 yd. For example, 540 yd will be rounded to 500 yd. </li>
     * <li> If the given distance is between 10 yd and 350 yd, then the returned value will be rounded to its nearest
     * division of 10 yd. For example, 132 yd will be rounded to 130 yd. </li>
     * <li> If the given distance is less than 10 yd, then the returned value will be rounded to its nearest division of
     * 1 yd. For example, 1.3 yd will be rounded to 1 yd. </li>
     * </ul>
     *
     * @param context
     *         the required context.
     * @param distance
     *         the distance in meters.
     *
     * @return a string representation including unit.
     */
    public static String formatDistanceInImperialUsSystem(final Context context, final long distance) {
        final String value;
        final String unit;
        LengthConverter lengthConverter = new LengthConverter();
        Measurement distanceMeters = new Measurement(distance, MeasurementUnit.METER);
        final double distanceYards = lengthConverter.convert(distanceMeters.getValue(),
                distanceMeters.getUnit(), MeasurementUnit.YARD).getValue();
        final double distanceMiles = lengthConverter.convert(distanceMeters.getValue(),
                distanceMeters.getUnit(), MeasurementUnit.MILE).getValue();
        if (distanceYards < YARDS_THRESHOLD_1750) {
            unit = context.getString(R.string.msdkui_unit_yard);
        } else {
            unit = context.getString(R.string.msdkui_unit_mile);
        }
        if (distanceYards < YARDS_THRESHOLD_10) {
            value = FORMATTER.format(Math.round(distanceYards));
        } else if (distanceYards < YARDS_THRESHOLD_350) {
            value = FORMATTER.format(roundNear10((long) distanceYards));
        } else if (distanceYards < YARDS_THRESHOLD_1750) {
            value = FORMATTER.format(roundNear50((long) distanceYards));
        } else if (distanceYards < YARDS_THRESHOLD) {
            value = FORMATTER.format(roundToSignificantDigits(distanceMiles, TWO_DIGITS));
        } else if (distanceMiles <= TEN) {
            value = FORMATTER.format(roundToSignificantDigits(distanceMiles, TWO_DIGITS));
        } else {
            value = FORMATTER.format(Math.round(distanceMiles));
        }
        return String.format(context.getString(R.string.msdkui_distance_value_with_unit), value, unit);
    }

    /**
     * Converts a given distance in meters to a string representation rounded to feet or miles.
     *
     * Some examples:
     *
     * <ul>
     * <li> If the given distance is greater than 10 miles, then the returned value will be rounded to its nearest division
     * of 1 mile. For example, 10.37 miles will be rounded to 10 miles. </li>
     * <li> If the given distance is between 1 mile and 10 miles, then the returned value will be rounded to its nearest
     * division of 0.1 mile. For example, 9.37 miles will be rounded to 9.30 miles. </li>
     * <li> If the given distance is between 1050 ft and 1 mile, then the returned value will be rounded to its nearest
     * division of 50 ft. For example, 1240 ft will be rounded to 1200 ft. </li>
     * <li> If the given distance is between 10 ft and 1050 ft, then the returned value will be rounded to its nearest
     * division of 10 ft. For example, 132 ft will be rounded to 130 ft. </li>
     * <li> If the given distance is less than 10 ft, then the returned value will be rounded to its nearest division of
     * 1 ft. For example, 1.3 ft will be rounded to 1 ft. </li>
     * </ul>
     *
     * @param context
     *         the required context.
     * @param distance
     *         the distance in meters.
     *
     * @return a string representation including unit.
     */
    public static String formatDistanceInImperialUkSystem(final Context context, final long distance) {
        final String value;
        final String unit;
        LengthConverter lengthConverter = new LengthConverter();
        Measurement distanceMeters = new Measurement(distance, MeasurementUnit.METER);
        final double distanceFeet = lengthConverter.convert(distanceMeters.getValue(),
                distanceMeters.getUnit(), MeasurementUnit.FOOT).getValue();
        final double distanceMiles = lengthConverter.convert(distanceMeters.getValue(),
                distanceMeters.getUnit(), MeasurementUnit.MILE).getValue();
        if (distanceFeet < FEET_THRESHOLD_5275) {
            unit = context.getString(R.string.msdkui_unit_foot);
        } else {
            unit = context.getString(R.string.msdkui_unit_mile);
        }
        if (distanceFeet < FEET_THRESHOLD_10) {
            value = FORMATTER.format(Math.round(distanceFeet));
        } else if (distanceFeet < FEET_THRESHOLD_1050) {
            value = FORMATTER.format(roundNear10((long) distanceFeet));
        } else if (distanceFeet < FEET_THRESHOLD_5275) {
            value = FORMATTER.format(roundNear50((long) distanceFeet));
        } else if (distanceFeet < FEET_THRESHOLD) {
            value = FORMATTER.format(roundToSignificantDigits(distanceMiles, TWO_DIGITS));
        } else if (distanceMiles <= TEN) {
            value = FORMATTER.format(roundToSignificantDigits(distanceMiles, TWO_DIGITS));
        } else {
            value = FORMATTER.format(Math.round(distanceMiles));
        }
        return String.format(context.getString(R.string.msdkui_distance_value_with_unit), value, unit);
    }

    /**
     * Rounds the given number to it's nearest division of 50.
     *
     * Some examples:
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
     * Rounds the given number to its nearest division of 10.
     *
     * Some examples:
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
