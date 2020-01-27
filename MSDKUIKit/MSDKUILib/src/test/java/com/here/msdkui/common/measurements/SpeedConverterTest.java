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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests for {@link SpeedConverter}.
 */
public class SpeedConverterTest extends RobolectricTest {

    @Test
    public void testConversions() {
        SpeedConverter speedConverter = new SpeedConverter();
        // metric to metric conversion
        final double sourceMetersPerSec = 100;
        final double expectedKmPerHour = 360;
        final MeasurementUnit metricUnitMetersPerSec = MeasurementUnit.METERS_PER_SECOND;
        final MeasurementUnit metricUnitKilometersPerHour = MeasurementUnit.KILOMETERS_PER_HOUR;
        Measurement metricToMetricSource = new Measurement(sourceMetersPerSec, metricUnitMetersPerSec);

        // meters/sec -> kilometers/hour
        Measurement convertedToKmPerH = speedConverter.convert(metricToMetricSource.getValue(),
                metricToMetricSource.getUnit(), metricUnitKilometersPerHour);
        assertThat(convertedToKmPerH.getValue(), is(expectedKmPerHour));
        assertThat(convertedToKmPerH.getUnit(), is(metricUnitKilometersPerHour));

        // kilometers/hour -> meters/sec
        Measurement convertedBackToM = speedConverter.convert(convertedToKmPerH.getValue(),
                convertedToKmPerH.getUnit(), metricUnitMetersPerSec);
        assertThat(convertedBackToM.getValue(), is(sourceMetersPerSec));
        assertThat(convertedBackToM.getUnit(), is(metricUnitMetersPerSec));

        // cross-system conversions
        final double expectedMetersPerSecInMilerPerHour = 223.694;
        final MeasurementUnit imperialUnitMilesPerHour = MeasurementUnit.MILES_PER_HOUR;
        Measurement metricToImperialSource = new Measurement(sourceMetersPerSec, metricUnitMetersPerSec);

        // meters/sec -> miles/hour
        Measurement convertedMetersPerSecToMph = speedConverter.convert(metricToImperialSource.getValue(),
                metricToImperialSource.getUnit(), imperialUnitMilesPerHour);
        assertThat(Converter.round(convertedMetersPerSecToMph.getValue(), 3),
                is(expectedMetersPerSecInMilerPerHour));
        assertThat(convertedMetersPerSecToMph.getUnit(), is(imperialUnitMilesPerHour));

        // miles/hour -> kilometers/hour
        Measurement convertedMphToKmh = speedConverter.convert(convertedMetersPerSecToMph.getValue(),
                convertedMetersPerSecToMph.getUnit(), metricUnitKilometersPerHour);
        assertThat(Converter.round(convertedMphToKmh.getValue(), 2),
                is(expectedKmPerHour));
        assertThat(convertedMphToKmh.getUnit(), is(metricUnitKilometersPerHour));

        // miles/hour -> meters/sec
        Measurement convertedMphToMetersPerSec = speedConverter.convert(convertedMetersPerSecToMph.getValue(),
                convertedMetersPerSecToMph.getUnit(), metricUnitMetersPerSec);
        assertThat(convertedMphToMetersPerSec.getValue(), is(sourceMetersPerSec));
        assertThat(convertedMphToMetersPerSec.getUnit(), is(metricUnitMetersPerSec));
    }
}
