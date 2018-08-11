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
}
