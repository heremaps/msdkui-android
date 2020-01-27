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
 * Utility class for basic length conversions.
 */
public class LengthConverter extends Converter {

    private static final double FOOT_IN_METERS = 0.3048;
    private static final double YARD_IN_METERS = 0.9144;
    private static final double MILE_IN_METERS = 1609.344;

    @Override
    protected double toBaseUnit(double in, MeasurementUnit unit) {
        switch (unit) {
            case METER:
                return in;

            case KILOMETER:
                return in * KILOMETER_IN_METERS;

            case YARD:
                return in * YARD_IN_METERS;

            case FOOT:
                return in * FOOT_IN_METERS;

            case MILE:
                return in * MILE_IN_METERS;

            default:
                return in;
        }
    }

    @Override
    protected double fromBaseTo(double in, MeasurementUnit unit) {
        switch (unit) {
            case METER:
                return in;

            case KILOMETER:
                return in / KILOMETER_IN_METERS;

            case YARD:
                return in / YARD_IN_METERS;

            case FOOT:
                return in / FOOT_IN_METERS;

            case MILE:
                return in / MILE_IN_METERS;

            default:
                return in;
        }
    }
}
