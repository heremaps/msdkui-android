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

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link TimeFormatterUtil}.
 */
public class TimeFormatterUtilTest extends RobolectricTest {

    private static final long SECOND_IN_MILLISECONDS = 1000L;
    private static final int MINUTE_IN_SECONDS = 60;
    private static final int DAY_IN_HOURS = 24;

    @Test
    public void testFormatWhenTimeIsLessThanZero() {
        String timeString = TimeFormatterUtil.format(getApplicationContext(), -1);
        assertThat(timeString, isEmptyString());
    }

    @Test
    public void testFormatWhenTimeIsZero() {
        String timeString = TimeFormatterUtil.format(getApplicationContext(), 0);
        assertThat(timeString, is("0 min"));
    }

    @Test
    public void testFormatWhenTimeIsInSec() {
        final int min = 50;  // 50 sec
        String timeString = TimeFormatterUtil.format(getApplicationContext(), min * SECOND_IN_MILLISECONDS);
        assertThat(timeString, is("50 s"));
    }

    @Test
    public void testFormatWhenTimeIsInMin() {
        final int min = 10 * MINUTE_IN_SECONDS;  // 10 Sec * 60
        String timeString = TimeFormatterUtil.format(getApplicationContext(), min * SECOND_IN_MILLISECONDS);
        assertThat(timeString, is("10 min"));
    }

    @Test
    public void testFormatWhenTimeIsInHours() {
        final int min = 65 * MINUTE_IN_SECONDS; // 65 sec * 60
        String timeString = TimeFormatterUtil.format(getApplicationContext(), min * SECOND_IN_MILLISECONDS);
        assertThat(timeString, is("1 h 5 min"));
    }

    @Test
    public void testFormatWhenTimeIsInHour() {
        final int min = 60 * MINUTE_IN_SECONDS; // 65 sec * 60
        String timeString = TimeFormatterUtil.format(getApplicationContext(), min * SECOND_IN_MILLISECONDS);
        assertThat(timeString, is("1 h"));
    }

    @Test
    public void testFormatWhenTimeIsInDays() {
        final int min = DAY_IN_HOURS * 65 * MINUTE_IN_SECONDS;  // 65 sec * 60
        String timeString = TimeFormatterUtil.format(getApplicationContext(), min * SECOND_IN_MILLISECONDS);
        assertThat(timeString, is("1 d 2 h"));
    }

    @Test
    public void testFormatWhenTimeIsInDay() {
        final int min = DAY_IN_HOURS * 60 * MINUTE_IN_SECONDS;  // 65 sec * 60
        String timeString = TimeFormatterUtil.format(getApplicationContext(), min * SECOND_IN_MILLISECONDS);
        assertThat(timeString, is("1 d"));
    }

    @Test
    public void testFormatFromSeconds() {
        final int min = DAY_IN_HOURS * 60 * MINUTE_IN_SECONDS;
        String timeString = TimeFormatterUtil.format(getApplicationContext(), min);
        assertThat(timeString, is("1 d"));
    }
}
