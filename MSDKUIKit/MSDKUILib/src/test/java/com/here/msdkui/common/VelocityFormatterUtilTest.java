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

import com.here.RobolectricTest;
import com.here.msdkui.R;
import com.here.msdkui.common.measurements.UnitSystems;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests for {@link TimeFormatterUtil}.
 */
public class VelocityFormatterUtilTest extends RobolectricTest {

    private static final double METERS_PER_SECOND_SPEED = 15;
    private static final double KILOMETERS_PER_HOUR_SPEED = 54;
    private static final double MILES_PER_HOUR_SPEED = 34;

    @Test
    public void testFormat() {
        assertEquals(
                VelocityFormatterUtil.format(METERS_PER_SECOND_SPEED, UnitSystems.METRIC),
                (int) KILOMETERS_PER_HOUR_SPEED);
        assertEquals(
                VelocityFormatterUtil.format(METERS_PER_SECOND_SPEED, UnitSystems.IMPERIAL_UK),
                (int) MILES_PER_HOUR_SPEED);
        assertEquals(
                VelocityFormatterUtil.format(METERS_PER_SECOND_SPEED, UnitSystems.IMPERIAL_US),
                (int) MILES_PER_HOUR_SPEED);
    }

    @Test
    public void testGetVelocityString() {
        assertEquals(
                VelocityFormatterUtil.getVelocityString(getApplicationContext(), UnitSystems.METRIC),
                getApplicationContext().getString(R.string.msdkui_unit_km_per_h));
        assertEquals(
                VelocityFormatterUtil.getVelocityString(getApplicationContext(), UnitSystems.IMPERIAL_UK),
                getApplicationContext().getString(R.string.msdkui_unit_miles_per_hour));
        assertEquals(
                VelocityFormatterUtil.getVelocityString(getApplicationContext(), UnitSystems.IMPERIAL_US),
                getApplicationContext().getString(R.string.msdkui_unit_miles_per_hour));
    }
}
