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

/**
 * A convenience class to convert velocities expressed in different units.
 */
public final class VelocityConverterUtil {

    private static final long KM_IN_METERS = 1000;
    private static final long HOUR_IN_SECONDS = 3600;

    private VelocityConverterUtil() {}

    /**
     * Converts velocity expressed in meters per second to kilometers per hour.
     * @param metersPerSecond
     *                     velocity in meters per second.
     * @return velocity in kilometers per hour.
     */
    public static int toKmPerHour(final double metersPerSecond) {
        final double kmh = (metersPerSecond * HOUR_IN_SECONDS) / KM_IN_METERS;
        return (int) Math.round(kmh);
    }
}
