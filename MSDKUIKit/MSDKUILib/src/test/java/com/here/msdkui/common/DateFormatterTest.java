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

import java.util.Calendar;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests for {@link DateFormatterUtil}.
 */
public class DateFormatterTest extends RobolectricTest {

    @Test
    public void testFormatDateString() {
        final String dateString = "01-01-2018 01:01:01"; // any date string.
        Calendar calendar = DateFormatterUtil.format(dateString);
        assertThat(calendar.get(Calendar.YEAR), is(2018));
        assertThat(calendar.get(Calendar.DAY_OF_YEAR), is(1));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), is(1));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), is(1));
    }

    @Test
    public void testDateShortening() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
                0);
        final String date = DateFormatterUtil.format(calendar.getTime());
        assertThat(date, containsString("PM"));
    }

    @Test
    public void testFormatDate() {
        Calendar calendar = Calendar.getInstance();
        final int date = 1;
        final int month = 1;
        final int year = 1;
        final int min = 1;
        final int sec = 1;
        calendar.set(year, month, date, min, sec);
        String dateString = DateFormatterUtil.format(getApplicationContext(), calendar.getTime());
        assertThat(dateString, containsString("Feb"));
    }

    public void testInvalidDateFormatting() {
        assertNull(DateFormatterUtil.format("invalid"));
    }

    public void testInvalidDateFormattingWithNull() {
        assertNull(DateFormatterUtil.format((String) null));
    }
}
