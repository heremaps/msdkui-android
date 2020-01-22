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
 * All available measurement units.
 */
public enum MeasurementUnit {
    METER,
    KILOMETER,
    YARD,
    FOOT,
    MILE,
    METERS_PER_SECOND,
    KILOMETERS_PER_HOUR,
    MILES_PER_HOUR;

    /**
     * Returns a converter for this unit.
     *
     * @return an instance of {@link Converter}.
     */
    public Converter getConverter() {
        switch (this) {
            case MILE:
            case KILOMETER:
            case METER:
            case YARD:
            case FOOT:
                return new LengthConverter();

            case MILES_PER_HOUR:
            case KILOMETERS_PER_HOUR:
            case METERS_PER_SECOND:
                return new SpeedConverter();

            default:
                return null; //should never happen
        }
    }
}
