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

/**
 * A class representing measurement of given quantity.
 */
public class Measurement {

    private double mValue;
    private MeasurementUnit mUnit;

    /**
     * Constructs a new instance using given value and unit.
     *
     * @param value
     *          a measured value.
     *
     * @param unit
     *          a {@link MeasurementUnit} for value.
     */
    public Measurement(double value, MeasurementUnit unit) {
        mValue = value;
        mUnit = unit;
    }

    /**
     * Returns a current value.
     *
     * @return measurement value.
     */
    public double getValue() {
        return mValue;
    }

    /**
     * Returns a current unit.
     *
     * @return a {@link MeasurementUnit}
     */
    public MeasurementUnit getUnit() {
        return mUnit;
    }

    /**
     * Converts a current value to the given unit.
     *
     * @param to
     *          a {@link MeasurementUnit} current value should be converted to.
     *
     * @return true if conversion was successful.
     */
    public boolean convert(MeasurementUnit to) {
        Measurement tmp = mUnit.getConverter().convert(mValue, mUnit, to);
        if (tmp != null) {
            mValue = tmp.getValue();
            mUnit = tmp.getUnit();
            return true;
        }
        return false;
    }

    /**
     * Returns a new instance of {@link Measurement} holding the converted value.
     *
     * @param to
     *          a {@link MeasurementUnit} current value should be converted to.
     *
     * @return an instance of {@link Measurement}.
     */
    public Measurement getConverted(MeasurementUnit to) {
        return mUnit.getConverter().convert(mValue, mUnit, to);
    }
}
