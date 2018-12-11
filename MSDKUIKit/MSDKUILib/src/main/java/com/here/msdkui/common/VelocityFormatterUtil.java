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
import com.here.msdkui.common.measurements.Measurement;
import com.here.msdkui.common.measurements.MeasurementUnit;
import com.here.msdkui.common.measurements.SpeedConverter;
import com.here.msdkui.common.measurements.UnitSystems;

/**
 * A convenience class to convert velocity in meter per second to velocity in a different unit.
 */
public final class VelocityFormatterUtil {
    private VelocityFormatterUtil() {}

    /**
     * Converts velocity expressed in meters per second to specified unit system.
     *
     * @param metersPerSecond
     *         velocity in meters per second.
     * @param system
     *         the unit system {@link UnitSystems}.
     *
     * @return velocity in specified unit system.
     */
    public static int format(final double metersPerSecond, final UnitSystems system) {
        SpeedConverter speedConverter = new SpeedConverter();
        Measurement speedMeterPerSecond = new Measurement(metersPerSecond,
                MeasurementUnit.METERS_PER_SECOND);
        double ret;
        switch (system) {
            case METRIC:
                ret = speedConverter.convert(speedMeterPerSecond.getValue(),
                        speedMeterPerSecond.getUnit(), MeasurementUnit.KILOMETERS_PER_HOUR).getValue();
                break;
            case IMPERIAL_UK:
                ret = speedConverter.convert(speedMeterPerSecond.getValue(),
                        speedMeterPerSecond.getUnit(), MeasurementUnit.MILES_PER_HOUR).getValue();
                break;
            case IMPERIAL_US:
                ret = speedConverter.convert(speedMeterPerSecond.getValue(),
                        speedMeterPerSecond.getUnit(), MeasurementUnit.MILES_PER_HOUR).getValue();
                break;
            default: // metric
                ret = speedConverter.convert(speedMeterPerSecond.getValue(),
                        speedMeterPerSecond.getUnit(), MeasurementUnit.KILOMETERS_PER_HOUR).getValue();
                break;
        }
        return (int) Math.round(ret);
    }

    /**
     * Returns abbreviation of velocity string in specified unit system.
     *
     * @param context
     *         the required context.
     * @param system
     *         the unit system {@link UnitSystems}.
     *
     * @return abbreviation of velocity string in specified unit system.
     */
    public static String getVelocityString(final Context context, final UnitSystems system) {
        switch (system) {
            case METRIC:
                return context.getString(R.string.msdkui_unit_km_per_h);
            case IMPERIAL_UK:
                return context.getString(R.string.msdkui_unit_miles_per_hour);
            case IMPERIAL_US:
                return context.getString(R.string.msdkui_unit_miles_per_hour);
            default: // metric
                return context.getString(R.string.msdkui_unit_km_per_h);
        }
    }
}
