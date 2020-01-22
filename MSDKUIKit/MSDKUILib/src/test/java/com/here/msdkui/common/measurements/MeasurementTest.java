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
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests for {@link Measurement}.
 */
public class MeasurementTest extends RobolectricTest {

    @Test
    public void testCreation() {
        Measurement measurement = new Measurement(10, MeasurementUnit.METER);
        assertNotNull(measurement);
    }

    @Test
    public void testValueGetter() {
        final double value = 10;
        Measurement measurement = new Measurement(value, MeasurementUnit.METER);
        assertThat(measurement.getValue(), is(value));
    }

    @Test
    public void testUnitGetter() {
        final MeasurementUnit unit = MeasurementUnit.KILOMETER;
        Measurement measurement = new Measurement(10, unit);
        assertThat(measurement.getUnit(), is(MeasurementUnit.KILOMETER));
    }

    @Test
    public void testConversionToCompatible() {
        final double sourceValue = 1;
        final double expectedValue = 1000;
        final MeasurementUnit sourceUnit = MeasurementUnit.KILOMETER;
        final MeasurementUnit expectedUnit = MeasurementUnit.METER;

        // test in-place conversion
        Measurement measurement1 = new Measurement(sourceValue, sourceUnit);
        assertTrue(measurement1.convert(expectedUnit));
        assertThat(measurement1.getValue(), is(expectedValue));
        assertThat(measurement1.getUnit(), is(expectedUnit));

        // test conversion with obtaining new measurement instance
        Measurement measurement2 = new Measurement(sourceValue, sourceUnit);
        Measurement converted = measurement2.getConverted(expectedUnit);
        assertThat(measurement2.getValue(), is(sourceValue));
        assertThat(measurement2.getUnit(), is(sourceUnit));
        assertThat(converted.getValue(), is(expectedValue));
        assertThat(converted.getUnit(), is(expectedUnit));
    }

    @Test
    public void testConverionToIncompatible() {
        final double sourceValue = 1;
        final MeasurementUnit sourceUnit = MeasurementUnit.METER;
        final MeasurementUnit targetUnit = MeasurementUnit.KILOMETERS_PER_HOUR;

        // test in-place conversion
        Measurement measurement = new Measurement(sourceValue, sourceUnit);
        assertFalse(measurement.convert(targetUnit));
        assertThat(measurement.getValue(), is(sourceValue));
        assertThat(measurement.getUnit(), is(sourceUnit));

        // test conversion with obtaining new measurement instance
        Measurement converted = measurement.getConverted(targetUnit);
        assertNull(converted);
        assertThat(measurement.getValue(), is(sourceValue));
        assertThat(measurement.getUnit(), is(sourceUnit));
    }
}
