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

package com.here.msdkui.routing;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.here.RobolectricTest;
import com.here.android.mpa.routing.RouteOptions;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Test for {@link TravelTimePicker}.
 */
public class TravelTimePickerTest extends RobolectricTest {

    private TravelTimePicker mTravelTimePicker;

    @Before
    public void setUp() {
        super.setUp();
        mTravelTimePicker = TravelTimePicker.newInstance();
    }

    @Test
    public void tesInitUI() {
        mTravelTimePicker.open(getSupportFragmentManager());
        final Dialog dialog = mTravelTimePicker.getDialog();
        assertNotNull(dialog.findViewById(R.id.picker_tab));
        assertNotNull(dialog.findViewById(R.id.travel_date));
        assertNotNull(dialog.findViewById(R.id.travel_time));
    }

    @Test
    public void cancelShouldDismissDialog() {
        mTravelTimePicker.open(getSupportFragmentManager());
        final Dialog dialog = mTravelTimePicker.getDialog();
        assertThat(dialog.isShowing(), is(true));
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE).performClick();
        assertThat(dialog.isShowing(), is(false));
    }

    @Test
    public void openShouldOpenDialogWithCurrentDateAndTime() {
        mTravelTimePicker.open(getSupportFragmentManager());
        final Dialog dialog = mTravelTimePicker.getDialog();
        final DatePicker picker = dialog.findViewById(R.id.travel_date);
        final Calendar calendar = Calendar.getInstance();
        assertThat(picker.getYear(), equalTo(calendar.get(Calendar.YEAR)));
        assertThat(picker.getMonth(), equalTo(calendar.get(Calendar.MONTH)));
        assertThat(picker.getDayOfMonth(), equalTo(calendar.get(Calendar.DAY_OF_MONTH)));
        final TimePicker timePicker = dialog.findViewById(R.id.travel_time);
        assertThat(timePicker.getCurrentHour(), equalTo(calendar.get(Calendar.HOUR_OF_DAY)));
        assertThat(timePicker.getCurrentMinute(), equalTo(calendar.get(Calendar.MINUTE)));
    }

    @Test
    public void openWithDateShouldOpenDialogWithThatDateAndTime() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + 1);
        mTravelTimePicker.setTimeType(RouteOptions.TimeType.DEPARTURE);
        mTravelTimePicker.open(getSupportFragmentManager(), calendar.getTime());
        final Dialog dialog = mTravelTimePicker.getDialog();
        final DatePicker picker = dialog.findViewById(R.id.travel_date);
        assertThat(picker.getYear(), equalTo(calendar.get(Calendar.YEAR)));
        assertThat(picker.getMonth(), equalTo(calendar.get(Calendar.MONTH)));
        assertThat(picker.getDayOfMonth(), equalTo(calendar.get(Calendar.DAY_OF_MONTH)));
        final TimePicker timePicker = dialog.findViewById(R.id.travel_time);
        assertThat(timePicker.getCurrentHour(), equalTo(calendar.get(Calendar.HOUR_OF_DAY)));
        assertThat(timePicker.getCurrentMinute(), equalTo(calendar.get(Calendar.MINUTE)));
    }

    @Test
    public void clickingOkShouldGiveCurrentTimeAndType() {
        final Calendar currentCalendar = Calendar.getInstance();
        mTravelTimePicker.open(getSupportFragmentManager());
        mTravelTimePicker.setOnTimePickedListener(new TravelTimePickerOnTimePickedListener(currentCalendar, RouteOptions.TimeType.ARRIVAL));
        final Dialog dialog = mTravelTimePicker.getDialog();
        ((TabLayout) dialog.findViewById(R.id.picker_tab)).getTabAt(1).select(); // click on arrival
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).performClick(); // click on date
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).performClick(); // click on time
    }

    @Test
    public void setTimeShouldSetTimeInDialog() {
        final Calendar nextDay = Calendar.getInstance();
        nextDay.set(nextDay.get(Calendar.YEAR), nextDay.get(Calendar.MONTH), nextDay.get(Calendar.DAY_OF_MONTH) + 1);
        mTravelTimePicker.setOnTimePickedListener(new TravelTimePickerOnTimePickedListener(nextDay, null));
        mTravelTimePicker.open(getSupportFragmentManager());
        mTravelTimePicker.setTime(nextDay.getTime());
        final Dialog dialog = mTravelTimePicker.getDialog();
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).performClick(); // click on date
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).performClick(); // click on time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mTravelTimePicker.getTime());
        assertThat(calendar.get(Calendar.YEAR), equalTo(nextDay.get(Calendar.YEAR)));
        assertThat(calendar.get(Calendar.MONTH), equalTo(nextDay.get(Calendar.MONTH)));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), equalTo(nextDay.get(Calendar.DAY_OF_MONTH)));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), equalTo(nextDay.get(Calendar.HOUR_OF_DAY)));
        assertThat(calendar.get(Calendar.MINUTE), equalTo(nextDay.get(Calendar.MINUTE)));
    }

    @Test
    public void setTimeTypeShouldSetTimeTypeInDialog() {
        mTravelTimePicker.setOnTimePickedListener(new TravelTimePickerOnTimePickedListener(null, RouteOptions.TimeType.ARRIVAL));
        mTravelTimePicker.open(getSupportFragmentManager());
        mTravelTimePicker.setTimeType(RouteOptions.TimeType.ARRIVAL);
        final Dialog dialog = mTravelTimePicker.getDialog();
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).performClick(); // click on date
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).performClick(); // click on time
        assertThat(mTravelTimePicker.getTimeType(), equalTo(RouteOptions.TimeType.ARRIVAL));
    }

    @Test
    public void testDefaultMaxMinDate() {
        // since picker is not opened
        assertThat(mTravelTimePicker.getMaxDate(), equalTo(-1L));
        assertThat(mTravelTimePicker.getMinDate(), equalTo(-1L));

        // open the picker
        mTravelTimePicker.open(getSupportFragmentManager());
        assertThat(mTravelTimePicker.getMaxDate(), is(not(equalTo(-1L))));
        assertThat(mTravelTimePicker.getMinDate(), is(not(equalTo(-1L))));
    }

    @Test
    public void testSetMaxDate() {
        Calendar originalDate = Calendar.getInstance();
        mTravelTimePicker.setMaxDate(originalDate.getTimeInMillis()); // set current date

        // open the picker
        mTravelTimePicker.open(getSupportFragmentManager());
        Calendar minDateCalendar = Calendar.getInstance();
        minDateCalendar.setTimeInMillis(mTravelTimePicker.getMaxDate());
        assertThat(minDateCalendar.get(Calendar.YEAR), equalTo(originalDate.get(Calendar.YEAR)));
        assertThat(minDateCalendar.get(Calendar.MONTH), equalTo(originalDate.get(Calendar.MONTH)));
        assertThat(minDateCalendar.get(Calendar.DAY_OF_MONTH), equalTo(originalDate.get(Calendar.DAY_OF_MONTH)));
    }

    @Test
    public void testSetMinDate() {
        Calendar originalDate = Calendar.getInstance();
        mTravelTimePicker.setMinDate(originalDate.getTimeInMillis()); // set current date
        // open the picker
        mTravelTimePicker.open(getSupportFragmentManager());
        Calendar minDateCalendar = Calendar.getInstance();
        minDateCalendar.setTimeInMillis(mTravelTimePicker.getMinDate());
        assertThat(minDateCalendar.get(Calendar.YEAR), equalTo(originalDate.get(Calendar.YEAR)));
        assertThat(minDateCalendar.get(Calendar.MONTH), equalTo(originalDate.get(Calendar.MONTH)));
        assertThat(minDateCalendar.get(Calendar.DAY_OF_MONTH), equalTo(originalDate.get(Calendar.DAY_OF_MONTH)));
    }

    @Test
    public void testTypeUIVisibility() {
        // open the picker, default is departure
        mTravelTimePicker.open(getSupportFragmentManager());
        Dialog dialog = mTravelTimePicker.getDialog();
        assertThat(dialog.findViewById(R.id.picker_tab).getVisibility(), equalTo(View.GONE));

        mTravelTimePicker = TravelTimePicker.newInstance(TravelTimePicker.Variety.BOTH);
        mTravelTimePicker.open(getSupportFragmentManager());
        dialog = mTravelTimePicker.getDialog();
        assertThat(dialog.findViewById(R.id.picker_tab).getVisibility(), equalTo(View.VISIBLE));
    }

    @Test
    public void testTypeDefaultValues() {
        assertThat(mTravelTimePicker.getTimeType(), equalTo(RouteOptions.TimeType.DEPARTURE));
        mTravelTimePicker = TravelTimePicker.newInstance(TravelTimePicker.Variety.ARRIVAL);
        mTravelTimePicker.open(getSupportFragmentManager());
        assertThat(mTravelTimePicker.getTimeType(), equalTo(RouteOptions.TimeType.ARRIVAL));

        // set time type
        mTravelTimePicker.setTimeType(RouteOptions.TimeType.DEPARTURE);
        assertThat(mTravelTimePicker.getTimeType(), equalTo(RouteOptions.TimeType.DEPARTURE));
    }

    @Test
    public void testSaveInstanceState() {
        Bundle bundle = new Bundle();
        Calendar calendar = Calendar.getInstance();
        mTravelTimePicker = TravelTimePicker.newInstance();
        mTravelTimePicker.open(getSupportFragmentManager());
        mTravelTimePicker.setTime(calendar.getTime());
        mTravelTimePicker.onSaveInstanceState(bundle);

        //check if time was correctly set in TravelTimePicker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            assertEquals(calendar.get(Calendar.HOUR_OF_DAY), mTravelTimePicker.getTimePicker().getHour());
            assertEquals(calendar.get(Calendar.MINUTE), mTravelTimePicker.getTimePicker().getMinute());
        } else {
            assertEquals(calendar.get(Calendar.HOUR_OF_DAY), mTravelTimePicker.getTimePicker().getCurrentHour().intValue());
            assertEquals(calendar.get(Calendar.MINUTE), mTravelTimePicker.getTimePicker().getCurrentMinute().intValue());
        }
        assertEquals(calendar.get(Calendar.YEAR), mTravelTimePicker.getDatePicker().getYear());
        assertEquals(calendar.get(Calendar.MONTH), mTravelTimePicker.getDatePicker().getMonth());

        //check if time was correctly set in bundle
        assertEquals(calendar.get(Calendar.YEAR), bundle.getInt(TravelTimePicker.YEAR));
        assertEquals(calendar.get(Calendar.MONTH), bundle.getInt(TravelTimePicker.MONTH));
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), bundle.getInt(TravelTimePicker.HOUR));
        assertEquals(calendar.get(Calendar.MINUTE), bundle.getInt(TravelTimePicker.MINUTE));
    }

    /**
     * Test listener
     */
    private static class TravelTimePickerOnTimePickedListener implements TravelTimePicker.OnTimePickedListener {
        private final Calendar mCurrentCalendar;
        private final RouteOptions.TimeType mTimeType;

        TravelTimePickerOnTimePickedListener(Calendar calendar, RouteOptions.TimeType timeType) {
            mCurrentCalendar = calendar;
            mTimeType = timeType;
        }

        @Override
        public void onTimePicked(final Date date, final RouteOptions.TimeType type) {
            if (mCurrentCalendar != null) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                assertThat(calendar.get(Calendar.YEAR), equalTo(mCurrentCalendar.get(Calendar.YEAR)));
                assertThat(calendar.get(Calendar.MONTH), equalTo(mCurrentCalendar.get(Calendar.MONTH)));
                assertThat(calendar.get(Calendar.DAY_OF_MONTH), equalTo(mCurrentCalendar.get(Calendar.DAY_OF_MONTH)));
                assertThat(calendar.get(Calendar.HOUR_OF_DAY), equalTo(mCurrentCalendar.get(Calendar.HOUR_OF_DAY)));
                assertThat(calendar.get(Calendar.MINUTE), equalTo(mCurrentCalendar.get(Calendar.MINUTE)));
            }
            if (mTimeType != null) {
                assertThat(type, equalTo(mTimeType));
            }
        }
    }
}
