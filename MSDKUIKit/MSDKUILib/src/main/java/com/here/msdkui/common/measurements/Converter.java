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

package com.here.msdkui.common.measurements;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Base class for unit converters.
 */
public abstract class Converter {

    protected static final double KILOMETER_IN_METERS = 1000;

    /**
     * Converts a value in given {@link MeasurementUnit} to another unit.
     *
     * @param in
     *          a value to be converted.
     *
     * @param from
     *          a source {@link MeasurementUnit}.
     *
     * @param to
     *          a target {@link MeasurementUnit}.
     *
     * @return results of conversion as instance of {@link Measurement}.
     */
    public Measurement convert(double in, MeasurementUnit from, MeasurementUnit to) {
        if (!areUnitsCompatible(from, to)) {
            return null;
        }
        double val = toBaseUnit(in, from);
        val = fromBaseTo(val, to);
        return new Measurement(val, to);
    }

    /**
     * Converts a value of given unit to the base unit.
     *
     * @param in
     *          a value to be converted.
     *
     * @param unit
     *          a {@link MeasurementUnit} to be converted to base.
     *
     * @return converted value.
     */
    protected abstract double toBaseUnit(double in, MeasurementUnit unit);

    /**
     * Converts a value from base unit to unit of the given type.
     *
     * @param in
     *          a value to be converted.
     *
     * @param unit
     *          a target {@link MeasurementUnit}.
     *
     * @return converted value.
     */
    protected abstract double fromBaseTo(double in, MeasurementUnit unit);

    /**
     * Round a given value to the number of decimal places.
     *
     * @param value
     *          value to round.
     *
     * @param places
     *          number of decimal places.
     *
     * @return a rounded value.
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(String.valueOf(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    protected static boolean isSpeed(MeasurementUnit unit) {
        switch (unit) {
            case METERS_PER_SECOND:
            case KILOMETERS_PER_HOUR:
            case MILES_PER_HOUR:
                return true;

            default:
                return false;
        }
    }

    protected static boolean isLength(MeasurementUnit unit) {
        return !isSpeed(unit);
    }

    protected static boolean isMetric(MeasurementUnit unit) {
        switch (unit) {
            case KILOMETERS_PER_HOUR:
            case METERS_PER_SECOND:
            case KILOMETER:
            case METER:
                return true;

            default:
                return false;
        }
    }

    private static boolean areUnitsCompatible(MeasurementUnit u1, MeasurementUnit u2) {
        if (isSpeed(u1) == isSpeed(u2) || isLength(u1) == isLength(u2)) {
            return true;
        }
        return false;
    }
}
