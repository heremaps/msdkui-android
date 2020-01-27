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

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A convenience class to convert {@link Date} to a readable string representation or to convert a formatted
 * string to a {@link Calendar} representation.
 */
public final class DateFormatterUtil {

    private static final int FORMAT_SHORT_TIME = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_TIME;
    private static final int FORMAT_DAY_AND_SHORT_MONTH = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH;

    private DateFormatterUtil() {
    }

    /**
     * Converts the given string of format <code>dd-MM-yyyy HH:mm:ss</code> to {@link Calendar}.
     *
     * @param dateString
     *         source string to be converted to {@link Calendar}.
     *
     * @return a {@link Calendar} or {@code null} when source string could not be parsed.
     */
    public static Calendar format(final String dateString) {
        if (dateString == null || dateString.length() == 0) {
            return null;
        }
        try {
            final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            final Date retDate = dateFormat.parse(dateString);
            final Calendar cal = Calendar.getInstance();
            cal.setTime(retDate);
            return cal;
        } catch (final ParseException e) {
            Log.e(DateFormatterUtil.class.getName(), e.getMessage());
        }
        return null;
    }

    /**
     * Converts {@link Date} to a string representation using <code>DateFormat.SHORT</code> and the
     * default locale of device.
     *
     * @param date
     *         the {@link Date} to be converted to string representation.
     *
     * @return a formatted string.
     */
    public static String format(final Date date) {
        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());
        return df.format(date);
    }

    /**
     * Converts {@link Date} to a short string representation using abbreviations.
     *
     * <p>In general, times are abbreviated by not showing the minutes if they are 0.
     * For example, instead of "3:00pm" the time would be abbreviated to "3pm".
     * If date is not today, month is shown as a 3-letter string.</p>
     *
     * @param context
     *         the required context.
     * @param date
     *         the {@link Date} to be converted to string representation.
     *
     * @return a string containing the formatted date.
     */
    public static String format(final Context context, final Date date) {
        if (DateUtils.isToday(date.getTime())) {
            return DateUtils.formatDateTime(context, date.getTime(), FORMAT_SHORT_TIME);
        }

        return DateUtils.formatDateTime(context, date.getTime(),
                DateUtils.FORMAT_SHOW_TIME | FORMAT_DAY_AND_SHORT_MONTH);
    }
}
