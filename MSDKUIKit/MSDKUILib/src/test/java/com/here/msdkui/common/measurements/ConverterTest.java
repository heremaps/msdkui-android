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

import com.here.RobolectricTest;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Tests for {@link Converter}.
 */
public class ConverterTest extends RobolectricTest {

    @Test
    public void testIsSpeed() {
        assertTrue(Converter.isSpeed(MeasurementUnit.METERS_PER_SECOND));
        assertTrue(Converter.isSpeed(MeasurementUnit.KILOMETERS_PER_HOUR));
        assertTrue(Converter.isSpeed(MeasurementUnit.MILES_PER_HOUR));

        assertFalse(Converter.isSpeed(MeasurementUnit.METER));
        assertFalse(Converter.isSpeed(MeasurementUnit.KILOMETER));
        assertFalse(Converter.isSpeed(MeasurementUnit.FOOT));
        assertFalse(Converter.isSpeed(MeasurementUnit.YARD));
        assertFalse(Converter.isSpeed(MeasurementUnit.MILE));
    }

    @Test
    public void testIsMetric() {
        assertTrue(Converter.isMetric(MeasurementUnit.METER));
        assertTrue(Converter.isMetric(MeasurementUnit.KILOMETER));
        assertTrue(Converter.isMetric(MeasurementUnit.KILOMETERS_PER_HOUR));
        assertTrue(Converter.isMetric(MeasurementUnit.METERS_PER_SECOND));

        assertFalse(Converter.isMetric(MeasurementUnit.FOOT));
        assertFalse(Converter.isMetric(MeasurementUnit.YARD));
        assertFalse(Converter.isMetric(MeasurementUnit.MILE));
        assertFalse(Converter.isMetric(MeasurementUnit.MILES_PER_HOUR));
    }
}
