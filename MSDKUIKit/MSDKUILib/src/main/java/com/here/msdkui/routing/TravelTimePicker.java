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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.content.DialogInterface;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.here.android.mpa.routing.RouteOptions;
import com.here.msdkui.R;

import java.util.Calendar;
import java.util.Date;

/**
 * A dialog that shows a {@link DatePicker} and a {@link TimePicker} and allows to set a
 * date, time and a {@link com.here.android.mpa.routing.RouteOptions.TimeType} that could be used
 * for route calculation.
 * By default the selected time is of type {@link com.here.android.mpa.routing.RouteOptions.TimeType#DEPARTURE}.
 */
public class TravelTimePicker extends DialogFragment {

    // saving states variables
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String TYPE = "type";
    public static final String TAB_SELECTED = "tab_type";
    public static final String MIN_DAY = "minDay";
    public static final String MAX_DAY = "maxDay";
    public static final String TIME_DIALOG_OPEN = "dialogOpenState";

    private long mMinDate = -1;
    private long mMaxDate = -1;

    private final Calendar mCalendar = Calendar.getInstance();
    private TabLayout mTabLayout;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private RouteOptions.TimeType mTimeType = RouteOptions.TimeType.DEPARTURE;
    private Variety mVariety = Variety.DEPARTURE;
    private OnTimePickedListener mOnTimePickedListener;
    private final DialogInterface.OnShowListener mOnShowListener = new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleClick();
                }
            });
        }
    };

    /**
     * An enum that defines the {@link com.here.android.mpa.routing.RouteOptions.TimeType} that
     * can be set using this picker.
     * Default is {@link Variety#DEPARTURE} which will result in
     * {@link com.here.android.mpa.routing.RouteOptions.TimeType#DEPARTURE} when a time and date is picked.
     *
     * <p>
     *     Please note, only when setting {@link Variety#BOTH}, the dialog will contain two buttons to select the
     *     {@link com.here.android.mpa.routing.RouteOptions.TimeType}, otherwise no additional button will be shown.
     * </p>
     */
    public enum Variety {

        /**
         * Defines a state with two buttons to select {@code DEPARTURE} or {@code ARRIVAL}. Default selection is
         * {@code DEPARTURE}.
         */
        BOTH,

        /**
         * Defines the type as {@code DEPARTURE}. This will show the picker without any button on top and
         * the picked time will be of type {@link com.here.android.mpa.routing.RouteOptions.TimeType#DEPARTURE}.
         */
        DEPARTURE,

        /**
         * Defines the type as {@code ARRIVAL}. This will show the picker without any button on top and
         * the picked time will be of type {@link com.here.android.mpa.routing.RouteOptions.TimeType#ARRIVAL}.
         */
        ARRIVAL
    }

    /**
     * Constructs a new instance using the {@link Variety#DEPARTURE} type by default.
     * @return an instance of this class.
     */
    public static TravelTimePicker newInstance() {
        return newInstance(Variety.DEPARTURE);
    }

    /**
     * Constructs a new instance using the given {@link Variety}.
     *
     * @param type defines the {@link Variety} time type.
     * @return an instance of this class.
     */
    public static TravelTimePicker newInstance(Variety type) {
        final TravelTimePicker travelTimePicker = new TravelTimePicker();
        final Bundle args = new Bundle();
        args.putInt(TYPE, type.ordinal());
        travelTimePicker.setArguments(args);
        return travelTimePicker;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setNegativeButton(R.string.msdkui_cancel, null)
                .setPositiveButton(R.string.msdkui_ok, null);

        final View view = View.inflate(getActivity(), R.layout.travel_time_picker, null);
        dialogBuilder.setView(view);

        mTabLayout = (TabLayout) view.findViewById(R.id.picker_tab);
        mDatePicker = (DatePicker) view.findViewById(R.id.travel_date);
        mTimePicker = (TimePicker) view.findViewById(R.id.travel_time);
        mTimePicker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));

        updateDefaultValues();
        updateTab(savedInstanceState);
        updateDatePicker(savedInstanceState);
        updateTimePicker(savedInstanceState);
        updateVisibility(savedInstanceState);

        final Dialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(mOnShowListener);
        return dialog;
    }

    private void updateDefaultValues() {
        mVariety = Variety.values()[getArguments().getInt(TYPE)];
        if (mVariety == Variety.ARRIVAL) {
            mTimeType = RouteOptions.TimeType.ARRIVAL;
        } else {
            mTimeType = RouteOptions.TimeType.DEPARTURE;
        }
    }

    private void updateVisibility(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.getBoolean(TIME_DIALOG_OPEN)) {
            return;
        }
        mDatePicker.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);
        mTimePicker.setVisibility(View.VISIBLE);
    }

    void handleClick() {
        if (mDatePicker.getVisibility() == View.VISIBLE) {
            mDatePicker.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.GONE);
            mTimePicker.setVisibility(View.VISIBLE);
        } else {
            int hour = 0;
            int min = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = mTimePicker.getHour();
                min = mTimePicker.getMinute();
            } else {
                hour = mTimePicker.getCurrentHour();
                min = mTimePicker.getCurrentMinute();
            }
            mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(),
                    hour, min);
            mTimeType = RouteOptions.TimeType.values()[mTabLayout.getSelectedTabPosition()];
            if (mOnTimePickedListener != null) {
                mOnTimePickedListener.onTimePicked(mCalendar.getTime(), mTimeType);
            }
            dismiss();
        }
    }

    private void updateTab(final Bundle savedInstanceState) {
        if (mTabLayout == null) {
            return;
        }
        mTabLayout.setVisibility(View.VISIBLE);
        int type = mTimeType.value();
        if (savedInstanceState != null) {
            type = savedInstanceState.getInt(TAB_SELECTED);
        }
        final TabLayout.Tab tab = mTabLayout.getTabAt(type);
        if (tab != null) {
            tab.select();
        }

        if (mVariety != Variety.BOTH) {
            mTabLayout.setVisibility(View.GONE);
        }
    }

    private void updateTimePicker(Bundle savedInstanceState) {
        if (mTimePicker == null) {
            return;
        }

        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int min = mCalendar.get(Calendar.MINUTE);

        if (savedInstanceState != null) {
            hour = savedInstanceState.getInt(HOUR);
            min = savedInstanceState.getInt(MINUTE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(min);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(min);
        }
    }

    private void updateDatePicker(Bundle savedInstanceState) {
        if (mDatePicker == null) {
            return;
        }

        // Set default values.
        if (mMinDate == -1) {
            mMinDate = mDatePicker.getMinDate();
        }

        if (mMaxDate == -1) {
            mMaxDate = mDatePicker.getMaxDate();
        }

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        if (savedInstanceState != null) {
            year = savedInstanceState.getInt(YEAR);
            month = savedInstanceState.getInt(MONTH);
            day = savedInstanceState.getInt(DAY);
            mMinDate = savedInstanceState.getLong(MIN_DAY);
            mMaxDate = savedInstanceState.getLong(MAX_DAY);
        }

        setMinDate(mMinDate);
        setMaxDate(mMaxDate);

        mDatePicker.updateDate(year, month, day);
    }

    /**
     * Shows this picker fragment using the given date.
     *
     * @param manager the {@link FragmentManager} where this fragment will be added to.
     * @param date the {@link Date} to set by default.
     *
     * @deprecated this method will not work from release 2.0.
     * Please use {@link #open(FragmentManager, Date)} instead.
     */
    public void open(final android.app.FragmentManager manager, final Date date) {
    }

    /**
     * Shows this picker fragment using the current date.
     * @param manager the {@link FragmentManager} where this fragment will be added to.
     *  @deprecated this method will not work from release 2.0. Please use
     *  {@link #open(FragmentManager)} instead.
     */
    public void open(final android.app.FragmentManager manager) {
    }


    /**
     * Shows this picker fragment using the given date.
     *
     * @param manager the {@link FragmentManager} where this fragment will be added to.
     * @param date the {@link Date} to set by default.
     */
    public void open(final FragmentManager manager, final Date date) {
        mCalendar.setTime(date);
        show(manager, TravelTimePicker.class.getName());
    }

    /**
     * Shows this picker fragment using the current date.
     * @param manager the {@link FragmentManager} where this fragment will be added to.
     */
    public void open(final FragmentManager manager) {
        open(manager, new Date());
    }

    /**
     * Gets the current date shown by this picker.
     *
     * @return the current set {@link Date}.
     */
    public Date getTime() {
        return mCalendar.getTime();
    }

    /**
     * Sets a new date to be shown by this picker.
     *
     * @param time the new {@link Date} to set.
     */
    public void setTime(final Date time) {
        if (time == null) {
            throw new IllegalArgumentException(getActivity().getString(R.string.msdkui_exception_date_null));
        }
        mCalendar.setTime(time);
        updateDatePicker(null);
        updateTimePicker(null);
    }

    /**
     * Gets the current set {@link com.here.android.mpa.routing.RouteOptions.TimeType}.
     * <p>Default is {@link com.here.android.mpa.routing.RouteOptions.TimeType#DEPARTURE}.</p>
     *
     * @return the current {@link com.here.android.mpa.routing.RouteOptions.TimeType}.
     */
    public RouteOptions.TimeType getTimeType() {
        return mTimeType;
    }

    /**
     * Sets a new {@link com.here.android.mpa.routing.RouteOptions.TimeType}.
     * <p>Please note: Setting time type will not change {@link Variety}.</p>
     *
     * @param type the new {@link com.here.android.mpa.routing.RouteOptions.TimeType} to set.
     */
    public void setTimeType(final RouteOptions.TimeType type) {
        if (type == null) {
            throw new IllegalArgumentException(getActivity().getString(R.string.msdkui_exception_time_type_null));
        }
        mTimeType = type;
        updateTab(null);
    }

    /**
     * Gets the {@link DatePicker} associated with this instance.
     *
     * @return the {@link DatePicker}.
     */
    public DatePicker getDatePicker() {
        return mDatePicker;
    }

    /**
     * Gets the {@link TimePicker} associated with this instance.
     *
     * @return the {@link TimePicker}.
     */
    public TimePicker getTimePicker() {
        return mTimePicker;
    }

    /**
     * Sets the minimal date in milliseconds since January 1, 1970 00:00:00
     * in {@link TimeZone#getDefault()} time zone.
     *
     * @param minDate the minimal supported date.
     * @return an instance of this class.
     */
    public TravelTimePicker setMinDate(long minDate) {
        mMinDate = minDate;
        if (mDatePicker != null) {
            mDatePicker.setMinDate(minDate);
        }
        return this;
    }

    /**
     * Sets the maximal date in milliseconds since January 1, 1970 00:00:00
     * in {@link TimeZone#getDefault()} time zone.
     *
     * @param maxDate the maximal supported date.
     * @return an instance of this class.
     */
    public TravelTimePicker setMaxDate(long maxDate) {
        mMaxDate = maxDate;
        if (mDatePicker != null) {
            mDatePicker.setMaxDate(maxDate);
        }
        return this;
    }

    /**
     * Gets the maximal date supported by this instance in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @return the maximal supported date if time was set, otherwise -1.
     */
    public long getMaxDate() {
        return mMaxDate;
    }

    /**
     * Gets the minimal date supported by this instance in
     * milliseconds since January 1, 1970 00:00:00 in
     * {@link TimeZone#getDefault()} time zone.
     *
     * @return the minimal supported date if time was set, otherwise -1.
     */
    public long getMinDate() {
        return mMinDate;
    }

    /**
     * Gets the {@link Variety}.
     * @return the {@link Variety} that is currently set.
     */
    public Variety getVariety() {
        return mVariety;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mDatePicker != null) {
            outState.putInt(YEAR, mDatePicker.getYear());
            outState.putInt(MONTH, mDatePicker.getMonth());
            outState.putInt(DAY, mDatePicker.getDayOfMonth());
            outState.putLong(MIN_DAY, mDatePicker.getMinDate());
            outState.putLong(MAX_DAY, mDatePicker.getMaxDate());
        }

        if (mTimePicker != null) {
            outState.putInt(HOUR, mTimePicker.getCurrentHour());
            outState.putInt(MINUTE, mTimePicker.getCurrentMinute());
            outState.putBoolean(TIME_DIALOG_OPEN, mTimePicker.isShown());
        }

        if (mTabLayout != null) {
            outState.putInt(TAB_SELECTED, mTabLayout.getSelectedTabPosition());
        }
    }

    /**
     * Sets the {@link TravelTimePanel.OnTimeChangedListener} to get notified when a time was selected.
     *
     * @param listener the {@link OnTimePickedListener} to set.
     */
    public void setOnTimePickedListener(final OnTimePickedListener listener) {
        mOnTimePickedListener = listener;
    }

    /**
     * An interface definition for a callback to be invoked when a time was selected.
     */
    public interface OnTimePickedListener {
        /**
         * Called when a new time was selected..
         *
         * @param time the time set.
         * @param type the {@link com.here.android.mpa.routing.RouteOptions.TimeType} the time was set for.
         */
        void onTimePicked(Date time, RouteOptions.TimeType type);
    }
}
