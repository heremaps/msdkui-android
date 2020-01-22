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

import com.here.msdkui.R;

/**
 * A convenience class to convert milliseconds to a string representation.
 */
public final class TimeFormatterUtil {

    private static final int HOUR_IN_SECONDS = 3600;
    private static final int MINUTE_IN_SECONDS = 60;
    private static final int DAY_IN_HOURS = 24;
    private static final long SECOND_IN_MILLISECONDS = 1000L;

    private TimeFormatterUtil() {
    }

    /**
     * Converts milliseconds to a string representation showing days, hours and minutes.
     *
     * @param context
     *         the required context.
     * @param milliseconds
     *         time in milliseconds
     *
     * @return a formatted string.
     */
    public static String format(final Context context, final long milliseconds) {

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
     * Converts seconds to a string representation showing days, hours and minutes.
     *
     * @param context
     *         the required context.
     * @param seconds
     *         time in seconds.
     *
     * @return a formatted string.
     */
    public static String format(final Context context, final int seconds) {
        return format(context, seconds * SECOND_IN_MILLISECONDS);
    }
}
