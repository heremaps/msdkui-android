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

package com.here.msdkui.common;

import com.here.RobolectricTest;
import com.here.msdkui.common.measurements.UnitSystem;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DistanceFormatterUtil}.
 */
public class DistanceFormatterTest extends RobolectricTest {

    // Tests of format(Context, long, UnitSystem.METRIC)

    @Test
    public void testDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                -1, UnitSystem.METRIC);
        assertThat(formattedDistance, is("-1 m"));
    }

    @Test
    public void testDistanceFormattingWhenDistanceInMeterRange() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                500, UnitSystem.METRIC);
        assertThat(formattedDistance, is("500 m"));
    }

    @Test
    public void testDistanceFormattingWhenDistanceInKMRange() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                1200, UnitSystem.METRIC);
        assertThat(formattedDistance, is("1.2 km"));
    }

    @Test
    public void testDistanceFormattingWhenDistanceInKMRange2() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                10000, UnitSystem.METRIC);
        assertThat(formattedDistance, is("10 km"));
    }

    // Tests of format(Context, long, UnitSystem.IMPERIAL_US)

    @Test
    public void testYardMileDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                -1, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("-1 yd"));
    }

    @Test
    public void testYardMileDistanceFormattingWhenDistanceInYardsRange() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                1050, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("1,148 yd"));
    }

    @Test
    public void testYardMileDistanceFormattingWhenDistanceInMilesRange() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                8167, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("5.1 mi"));
    }

    @Test
    public void testYardMileDistanceFormattingWhenDistanceInMilesRange2() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                19222, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("12 mi"));
    }

    // Tests of format(Context, long, UnitSystem.IMPERIAL_UK)

    @Test
    public void testFootMileDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                -1, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("-3 ft"));
    }

    @Test
    public void testFootMileDistanceFormattingWhenDistanceInYardsRange() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                1072, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("3,517 ft"));
    }

    @Test
    public void testFootMileDistanceFormattingWhenDistanceInMilesRange() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                7385, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("4.6 mi"));
    }

    @Test
    public void testFootMileDistanceFormattingWhenDistanceInMilesRange2() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(),
                22197, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("14 mi"));
    }

    // Tests of formatDistance(Context, long, UnitSystem.METRIC)

    @Test
    public void testUIDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                -1, UnitSystem.METRIC);
        assertThat(formattedDistance, is("-1 m"));
    }

    @Test
    public void testDistanceFormattingWhenDistanceIsLessThan10() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                5, UnitSystem.METRIC);
        assertThat(formattedDistance, is("5 m"));
    }

    @Test
    public void testUIDistanceFormattingWhenDistanceIsLessThan200() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                180, UnitSystem.METRIC);
        assertThat(formattedDistance, is("180 m"));
    }

    @Test
    public void testUIDistanceFormattingWhenDistanceIsLessThan975() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                970, UnitSystem.METRIC);
        assertThat(formattedDistance, is("950 m"));
    }

    @Test
    public void testUIMeterChangeToKm() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                990, UnitSystem.METRIC);
        assertThat(formattedDistance, is("1 km"));
    }

    @Test
    public void testUIDistanceFormattingWhenDistanceIsLessThan10Km() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                9910, UnitSystem.METRIC);
        assertThat(formattedDistance, is("9.9 km"));
    }

    @Test
    public void testUIDistanceFormattingWhenDistanceIsMoreThan10Km() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                10400, UnitSystem.METRIC);
        assertThat(formattedDistance, is("10 km"));
    }

    @Test
    public void testUIDistanceFormattingWhenDistanceIsMoreThan1000Km() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                1000800, UnitSystem.METRIC);
        assertThat(formattedDistance, is("1,001 km"));
    }

    // Tests of formatDistance(Context, long, UnitSystem.IMPERIAL_US)

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                -1, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("-1 yd"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsLessThan10Yd() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                7, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("8 yd"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsLessThan350Yd() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                280, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("310 yd"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsLessThan1750Yd() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                1140, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("1,250 yd"));
    }

    @Test
    public void testUIYardMileYardChangeToMile() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                1605, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("1 mi"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsLessThan10Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                15932, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("9.9 mi"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsMoreThan10Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                25334, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("16 mi"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsMoreThan1000Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                1622607, UnitSystem.IMPERIAL_US);
        assertThat(formattedDistance, is("1,008 mi"));
    }

    // Tests of formatDistance(Context, long, UnitSystem.IMPERIAL_UK)

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                -1, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("-3 ft"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsLessThan10Ft() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                2, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("7 ft"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsLessThan1050Ft() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                242, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("790 ft"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsLessThan5275FT() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                1324, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("4,350 ft"));
    }

    @Test
    public void testUIFeetMileFeetChangeToMile() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                1608, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("1 mi"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsLessThan10Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                12517, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("7.8 mi"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsMoreThan10Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                29145, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("18 mi"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsMoreThan1000Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistance(getApplicationContext(),
                1933248, UnitSystem.IMPERIAL_UK);
        assertThat(formattedDistance, is("1,201 mi"));
    }
}
