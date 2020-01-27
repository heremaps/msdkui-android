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
 * Utility class used for speed conversions.
 */
public class SpeedConverter extends Converter {

    private static final double SECONDS_IN_HOUR = 3600;

    @Override
    protected double toBaseUnit(double in, MeasurementUnit unit) {
        switch (unit) {
            case METERS_PER_SECOND:
                return in;

            case KILOMETERS_PER_HOUR:
                return (in * KILOMETER_IN_METERS) / SECONDS_IN_HOUR;

            case MILES_PER_HOUR:
                return (new LengthConverter().convert(in,
                        MeasurementUnit.MILE, MeasurementUnit.METER)).getValue() / SECONDS_IN_HOUR;

            default:
                return in;
        }
    }

    @Override
    protected double fromBaseTo(double in, MeasurementUnit unit) {
        switch (unit) {
            case METERS_PER_SECOND:
                return in;

            case KILOMETERS_PER_HOUR:
                return (in * SECONDS_IN_HOUR) / KILOMETER_IN_METERS;

            case MILES_PER_HOUR:
                return new LengthConverter().convert(in, MeasurementUnit.METER,
                        MeasurementUnit.MILE).getValue() * SECONDS_IN_HOUR;

            default:
                return in;
        }
    }
}
