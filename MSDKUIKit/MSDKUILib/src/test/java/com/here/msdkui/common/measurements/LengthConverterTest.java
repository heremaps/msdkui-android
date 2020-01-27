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
 * Tests for {@link LengthConverter}.
 */
public class LengthConverterTest extends RobolectricTest {

    @Test
    public void testConversions() {
        LengthConverter lengthConverter = new LengthConverter();
        // metric to metric conversion
        final double sourceMeters = 1000;
        final double expectedKm = 1;
        final MeasurementUnit metricUnitMeter = MeasurementUnit.METER;
        final MeasurementUnit metricUnitKilometer = MeasurementUnit.KILOMETER;
        Measurement metricToMetricSource = new Measurement(sourceMeters, metricUnitMeter);

        // meters -> kilometers
        Measurement convertedToKm = lengthConverter.convert(metricToMetricSource.getValue(),
                metricToMetricSource.getUnit(), metricUnitKilometer);
        assertThat(convertedToKm.getValue(), is(expectedKm));
        assertThat(convertedToKm.getUnit(), is(metricUnitKilometer));

        // kilometers -> meters
        Measurement convertedBackToM = lengthConverter.convert(convertedToKm.getValue(),
                convertedToKm.getUnit(), metricUnitMeter);
        assertThat(convertedBackToM.getValue(), is(sourceMeters));
        assertThat(convertedBackToM.getUnit(), is(metricUnitMeter));

        // imperial to imperial conversion
        final double sourceYards = 1760;
        final double expectedMiles = 1;
        final double expectedFoot = 5280;
        final MeasurementUnit imperialUnitYard = MeasurementUnit.YARD;
        final MeasurementUnit imperialUnitMile = MeasurementUnit.MILE;
        final MeasurementUnit imperialUnitFoot = MeasurementUnit.FOOT;
        Measurement imperialToImperialSource = new Measurement(sourceYards, imperialUnitYard);

        // yards -> miles
        Measurement convertedToMi = lengthConverter.convert(imperialToImperialSource.getValue(),
                imperialToImperialSource.getUnit(), imperialUnitMile);
        assertThat(convertedToMi.getValue(), is(expectedMiles));
        assertThat(convertedToMi.getUnit(), is(imperialUnitMile));

        // miles -> yards
        Measurement convertedBackToYd = lengthConverter.convert(convertedToMi.getValue(),
                convertedToMi.getUnit(), imperialUnitYard);
        assertThat(convertedBackToYd.getValue(), is(sourceYards));
        assertThat(convertedBackToYd.getUnit(), is(imperialUnitYard));

        // yards -> foot
        Measurement convertedToFoot = lengthConverter.convert(imperialToImperialSource.getValue(),
                imperialToImperialSource.getUnit(), imperialUnitFoot);
        assertThat(convertedToFoot.getValue(), is(expectedFoot));
        assertThat(convertedToFoot.getUnit(), is(imperialUnitFoot));

        // foot -> miles
        Measurement convertedFootToMi = lengthConverter.convert(convertedToFoot.getValue(),
                convertedToFoot.getUnit(), imperialUnitMile);
        assertThat(convertedFootToMi.getValue(), is(expectedMiles));
        assertThat(convertedFootToMi.getUnit(), is(imperialUnitMile));

        // cross-system conversions
        final double expectedMetersInYards = 1093.61;
        final double expectedMetersInFeet = 3280.84;
        final double expectedMetersInMiles = 0.621371;
        Measurement metricToImperialSource = new Measurement(sourceMeters, metricUnitMeter);

        // meters -> yards
        Measurement convertedMetersToYards = lengthConverter.convert(metricToImperialSource.getValue(),
                metricToImperialSource.getUnit(), imperialUnitYard);
        assertThat(Converter.round(convertedMetersToYards.getValue(), 2),
                is(expectedMetersInYards));
        assertThat(convertedMetersToYards.getUnit(), is(imperialUnitYard));

        // meters -> foot
        Measurement convertedMetersToFoot = lengthConverter.convert(metricToImperialSource.getValue(),
                metricToImperialSource.getUnit(), imperialUnitFoot);
        assertThat(Converter.round(convertedMetersToFoot.getValue(), 2),
                is(expectedMetersInFeet));
        assertThat(convertedMetersToFoot.getUnit(), is(imperialUnitFoot));

        // meters -> miles
        Measurement convertedMetersToMiles = lengthConverter.convert(metricToImperialSource.getValue(),
                metricToImperialSource.getUnit(), imperialUnitMile);
        assertThat(Converter.round(convertedMetersToMiles.getValue(), 6), is(expectedMetersInMiles));
        assertThat(convertedMetersToMiles.getUnit(), is(imperialUnitMile));
    }
}
