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

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DistanceFormatterUtil}.
 */
public class DistanceFormatterTest extends RobolectricTest {

    // Tests of function: format

    @Test
    public void testDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(), -1);
        assertThat(formattedDistance, is("-- m"));
    }

    @Test
    public void testDistanceFormattingWhenDistanceInMeterRange() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(), 500);
        assertThat(formattedDistance, is("500 m"));
    }

    @Test
    public void testDistanceFormattingWhenDistanceInKMRange() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(), 1200);
        assertThat(formattedDistance, is("1.2 km"));
    }

    @Test
    public void testDistanceFormattingWhenDistanceInKMRange2() {
        String formattedDistance = DistanceFormatterUtil.format(getApplicationContext(), 10000);
        assertThat(formattedDistance, is("10 km"));
    }

    // Tests of function: formatYardMile

    @Test
    public void testYardMileDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.formatYardMile(getApplicationContext(), -1);
        assertThat(formattedDistance, is("-- yd"));
    }

    @Test
    public void testYardMileDistanceFormattingWhenDistanceInYardsRange() {
        String formattedDistance = DistanceFormatterUtil.formatYardMile(getApplicationContext(), 1050);
        assertThat(formattedDistance, is("1,148 yd"));
    }

    @Test
    public void testYardMileDistanceFormattingWhenDistanceInMilesRange() {
        String formattedDistance = DistanceFormatterUtil.formatYardMile(getApplicationContext(), 8167);
        assertThat(formattedDistance, is("5.1 mi"));
    }

    @Test
    public void testYardMileDistanceFormattingWhenDistanceInMilesRange2() {
        String formattedDistance = DistanceFormatterUtil.formatYardMile(getApplicationContext(), 19222);
        assertThat(formattedDistance, is("12 mi"));
    }

    // Tests of function: formatFootMile

    @Test
    public void testFootMileDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.formatFootMile(getApplicationContext(), -1);
        assertThat(formattedDistance, is("-- ft"));
    }

    @Test
    public void testFootMileDistanceFormattingWhenDistanceInYardsRange() {
        String formattedDistance = DistanceFormatterUtil.formatFootMile(getApplicationContext(), 1072);
        assertThat(formattedDistance, is("3,517 ft"));
    }

    @Test
    public void testFootMileDistanceFormattingWhenDistanceInMilesRange() {
        String formattedDistance = DistanceFormatterUtil.formatFootMile(getApplicationContext(), 7385);
        assertThat(formattedDistance, is("4.6 mi"));
    }

    @Test
    public void testFootMileDistanceFormattingWhenDistanceInMilesRange2() {
        String formattedDistance = DistanceFormatterUtil.formatFootMile(getApplicationContext(), 22197);
        assertThat(formattedDistance, is("14 mi"));
    }

    // Tests of function: formatDistanceForUI

    @Test
    public void testUIDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUI(getApplicationContext(), -1);
        assertThat(formattedDistance, is("-- m"));
    }

    @Test
    public void testDistanceFormattingWhenDistanceIsLessThan10() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUI(getApplicationContext(), 5);
        assertThat(formattedDistance, is("5 m"));
    }

    @Test
    public void testUIDistanceFormattingWhenDistanceIsLessThan200() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUI(getApplicationContext(), 180);
        assertThat(formattedDistance, is("180 m"));
    }

    @Test
    public void testUIDistanceFormattingWhenDistanceIsLessThan975() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUI(getApplicationContext(), 970);
        assertThat(formattedDistance, is("950 m"));
    }

    @Test
    public void testUIMeterChangeToKm() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUI(getApplicationContext(), 990);
        assertThat(formattedDistance, is("1 km"));
    }

    @Test
    public void testUIDistanceFormattingWhenDistanceIsLessThan10Km() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUI(getApplicationContext(), 9910);
        assertThat(formattedDistance, is("9.9 km"));
    }

    @Test
    public void testUIDistanceFormattingWhenDistanceIsMoreThan10Km() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUI(getApplicationContext(), 10400);
        assertThat(formattedDistance, is("10 km"));
    }

    @Test
    public void testUIDistanceFormattingWhenDistanceIsMoreThan1000Km() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUI(getApplicationContext(), 1000800);
        assertThat(formattedDistance, is("1,001 km"));
    }

    // Tests of function: formatDistanceForUIYardMile

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIYardMile(getApplicationContext(), -1);
        assertThat(formattedDistance, is("-- yd"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsLessThan10Yd() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIYardMile(getApplicationContext(), 7);
        assertThat(formattedDistance, is("8 yd"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsLessThan350Yd() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIYardMile(getApplicationContext(), 280);
        assertThat(formattedDistance, is("310 yd"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsLessThan1750Yd() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIYardMile(getApplicationContext(), 1140);
        assertThat(formattedDistance, is("1,250 yd"));
    }

    @Test
    public void testUIYardMileYardChangeToMile() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIYardMile(getApplicationContext(), 1605);
        assertThat(formattedDistance, is("1 mi"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsLessThan10Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIYardMile(getApplicationContext(), 15932);
        assertThat(formattedDistance, is("9.9 mi"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsMoreThan10Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIYardMile(getApplicationContext(), 25334);
        assertThat(formattedDistance, is("16 mi"));
    }

    @Test
    public void testUIYardMileDistanceFormattingWhenDistanceIsMoreThan1000Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIYardMile(getApplicationContext(), 1622607);
        assertThat(formattedDistance, is("1,008 mi"));
    }

    // Tests of function: formatDistanceForUIFeetMile

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceNotValid() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIFeetMile(getApplicationContext(), -1);
        assertThat(formattedDistance, is("-- ft"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsLessThan10Ft() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIFeetMile(getApplicationContext(), 2);
        assertThat(formattedDistance, is("7 ft"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsLessThan1050Ft() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIFeetMile(getApplicationContext(), 242);
        assertThat(formattedDistance, is("790 ft"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsLessThan5275FT() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIFeetMile(getApplicationContext(), 1324);
        assertThat(formattedDistance, is("4,350 ft"));
    }

    @Test
    public void testUIFeetMileFeetChangeToMile() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIFeetMile(getApplicationContext(), 1608);
        assertThat(formattedDistance, is("1 mi"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsLessThan10Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIFeetMile(getApplicationContext(), 12517);
        assertThat(formattedDistance, is("7.8 mi"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsMoreThan10Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIFeetMile(getApplicationContext(), 29145);
        assertThat(formattedDistance, is("18 mi"));
    }

    @Test
    public void testUIFeetMileDistanceFormattingWhenDistanceIsMoreThan1000Mi() {
        String formattedDistance = DistanceFormatterUtil.formatDistanceForUIFeetMile(getApplicationContext(), 1933248);
        assertThat(formattedDistance, is("1,201 mi"));
    }
}
