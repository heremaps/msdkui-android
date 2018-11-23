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

package com.here.msdkui.routing;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import com.here.msdkui.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Util class for reusable code.
 *
 * @see com.here.msdkui.common.DateFormatterUtil
 * @see com.here.msdkui.common.DistanceFormatterUtil
 * @see com.here.msdkui.common.TimeFormatterUtil
 * @deprecated This class will be removed in version 1.4.0
 */
// Suppressed PMD because this class will be removed.
@SuppressWarnings("PMD.ClassNamingConventions")
@Deprecated
public final class Utils {

    public static final int METER_THRESHOLD = 998;
    public static final int KM_THRESHOLD = 9950;
    public static final int THOUSAND = 1000;
    private static final int HOUR_IN_SECONDS = 3600;
    private static final int MINUTE_IN_SECONDS = 60;
    private static final int DAY_IN_HOURS = 24;
    private static final long SECOND_IN_MILLISECONDS = 1000L;
    public static final long DAY_IN_MILLISECONDS = SECOND_IN_MILLISECONDS * HOUR_IN_SECONDS * DAY_IN_HOURS;
    private static final int FORMAT_SHORT_TIME = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_TIME;
    private static final int FORMAT_DAY_AND_SHORT_MONTH = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH;

    private Utils() {
    }

    /**
     * Method to get date from string date.
     *
     * @param dateString
     *         a {@link String} containing date.
     *
     * @return date as {@link Calendar} instance.
     */
    public static Calendar stringToDate(final String dateString) {
        if (dateString == null || dateString.length() == 0) {
            return null;
        }
        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            final Date retDate = df.parse(dateString);
            final Calendar cal = Calendar.getInstance();
            cal.setTime(retDate);
            return cal;
        } catch (final ParseException e) {
            Log.e(Utils.class.getName(), e.getMessage());
        }
        return null;
    }

    /**
     * Method to get string date date from date.
     *
     * @param date
     *         a date to be converted to text.
     *
     * @return a date as {@link String}.
     */
    public static String dateToString(final Date date) {
        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());
        return df.format(date);
    }

    /**
     * Formats given distance with value and fixed unit (mt or km).
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param distance
     *         a distance value in meters.
     *
     * @return a distance as {@link String}.
     */
    public static String formatDistance(final Context context, final int distance) {
        final String value;
        final String unit;
        final NumberFormat numberFormat = NumberFormat.getInstance();
        if (distance < 0) { // invalid
            value = context.getString(R.string.msdkui_value_not_available);
            unit = context.getString(R.string.msdkui_unit_meter);
        } else if (distance < METER_THRESHOLD) {
            value = numberFormat.format(distance);
            unit = context.getString(R.string.msdkui_unit_meter);
        } else if (distance < KM_THRESHOLD) {
            value = numberFormat.format(roundToSignificantDigits((double) distance / THOUSAND, 2));
            unit = context.getString(R.string.msdkui_unit_kilometer);
        } else {
            value = numberFormat.format(Math.round((double) distance / THOUSAND));
            unit = context.getString(R.string.msdkui_unit_kilometer);
        }
        return String.format(context.getString(R.string.msdkui_distance_value_with_unit), value, unit);
    }

    /**
     * Round to given significant digit.
     *
     * @param number
     *         a value to be rounded.
     *
     * @param significantDigits
     *         a number of significant digits.
     *
     * @return a rounded value.
     */
    public static double roundToSignificantDigits(final double number, final int significantDigits) {
        if (number == 0) {
            return 0;
        }

        final int exponent = (int) (Math.floor(Math.log10(Math.abs(number))) + 1 - significantDigits);
        final double factor = Math.pow(10.0, exponent);

        return Math.round(number / factor) * factor;
    }

    /**
     * Round to multiple of given x.
     *
     * @param number
     *         a value to be rounded.
     *
     * @param x
     *         a rounding adjustment value.
     *
     * @return a rounded value.
     */
    public static int roundToMultipleOfX(final double number, final int x) {
        final int roundedValue = (int) Math.round(number);
        final int moduloOfRoundedValue = roundedValue % x;
        return roundedValue + (moduloOfRoundedValue > (x / 2) ? x - moduloOfRoundedValue : -moduloOfRoundedValue);
    }

    /**
     * Formats time duration.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param milliseconds
     *         duration expressed in milliseconds.
     *
     * @return formatted duration text.
     */
    public static String formatDuration(final Context context, final long milliseconds) {

        if (milliseconds < 0) {
            return "";
        }

        if (milliseconds == 0) {
            return context.getString(R.string.msdkui_minutes, 0);
        }

        final int seconds = (int) (milliseconds / SECOND_IN_MILLISECONDS);
        final int days = seconds / (HOUR_IN_SECONDS * DAY_IN_HOURS);
        final int hours = seconds / HOUR_IN_SECONDS % DAY_IN_HOURS;

        if (days > 0) {
            if (hours > 0) {
                return context.getString(R.string.msdkui_days_hours, days, hours);
            } else {
                return context.getString(R.string.msdkui_days, days);
            }
        }

        final int minutes = (seconds / MINUTE_IN_SECONDS) % MINUTE_IN_SECONDS;
        if (hours > 0) {
            if (minutes > 0) {
                return context.getString(R.string.msdkui_hours_minutes, hours, minutes);
            } else {
                return context.getString(R.string.msdkui_hours, hours);
            }
        }

        if (minutes > 0) {
            return context.getString(R.string.msdkui_minutes, minutes);
        }

        return context.getString(R.string.msdkui_seconds, (seconds / 10) * 10);
    }

    /**
     * Formats date.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param time
     *         expressed as {@link Date}.
     *
     * @return formatted date text.
     */
    public static String formatDate(final Context context, final Date time) {
        if (DateUtils.isToday(time.getTime())) {
            return DateUtils.formatDateTime(context, time.getTime(), FORMAT_SHORT_TIME);
        }

        return DateUtils.formatDateTime(context, time.getTime(),
                DateUtils.FORMAT_SHOW_TIME | FORMAT_DAY_AND_SHORT_MONTH);
    }
}
