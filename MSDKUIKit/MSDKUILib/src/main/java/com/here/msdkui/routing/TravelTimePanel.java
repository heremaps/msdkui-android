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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.here.android.mpa.routing.RouteOptions;
import com.here.msdkui.R;
import com.here.msdkui.common.DateFormatterUtil;

import java.util.Date;

/**
 * A view that shows the currently selected time. It also allows to open a {@link TravelTimePicker} to set a
 * new time.
 */
public final class TravelTimePanel extends RelativeLayout implements TravelTimePicker.OnTimePickedListener {
    private static final int TIME_OUT = 200;
    private TextView mTravelText;
    private TextView mTravelTime;
    private OnTimeChangedListener mOnTimeChangedListener;
    private Date mDate = new Date();
    private RouteOptions.TimeType mTimeType = RouteOptions.TimeType.DEPARTURE;
    private TravelTimePicker mPicker;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public TravelTimePanel(final Context context) {
        this(context, null, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     */
    public TravelTimePanel(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     *
     * @param defStyleAttr
     *         a default style attribute.
     */
    public TravelTimePanel(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     *
     * @param defStyleAttr
     *         a default style attribute.
     *
     * @param defStyleRes
     *         a default style resource.
     *
     * Requires Lollipop (API level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TravelTimePanel(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.travel_time_panel, this);
        mTravelText = (TextView) findViewById(R.id.travel_time_text);
        mTravelTime = (TextView) findViewById(R.id.travel_time_details);
        final FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
        final Fragment fragment = manager.findFragmentByTag(TravelTimePicker.class.getName());
        if (fragment == null) {
            mPicker = TravelTimePicker.newInstance();
        } else {
            mPicker = (TravelTimePicker) fragment;
        }
        mPicker.setOnTimePickedListener(this);
        setOnClickListener(v -> {
            setEnabled(false); // Prevent multiple clicks till dialog is open.
            if (!mPicker.isVisible()) {
                // Open the picker with current time, if set time lies in the past.
                final long currentTime = new Date().getTime();
                final long setTime = getTime().getTime();
                final long openPickerTime = setTime - currentTime < 0 ? currentTime : setTime;
                mPicker.setTimeType(mTimeType);
                mPicker.open(manager, new Date(openPickerTime));
            }
            enableAfterTimeout();
        });
        displayDateOnUi();
        displayTimeTypeOnUi();
    }

    void enableAfterTimeout() {
        new Handler().postDelayed(() -> setEnabled(true), TIME_OUT);
    }

    /**
     * Gets the associated {@link TravelTimePicker}.
     * @return the associated {@link TravelTimePicker}.
     */
    public TravelTimePicker getPicker() {
        return mPicker;
    }

    /**
     * Gets the currently displayed time.
     *
     * @return a {@link Date} representing the currently displayed time.
     */
    public Date getTime() {
        return (Date) mDate.clone();
    }

    /**
     * Sets the time to display.
     *
     * <p>By default, it is showing the current date using
     * {@link com.here.android.mpa.routing.RouteOptions.TimeType#DEPARTURE}.</p>
     *
     * @param time the time to set.
     * @return an instance of this class.
     * @throws IllegalArgumentException if time is null.
     */
    public TravelTimePanel setTime(final Date time) {
        if (time == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_date_null));
        }
        if (mOnTimeChangedListener != null && mDate != null && !mDate.equals(time)) {
            mOnTimeChangedListener.onTimeChanged(time, getTimeType());
        }
        mDate = (Date) time.clone();
        displayDateOnUi();
        return this;
    }

    /**
     * Gets the {@link com.here.android.mpa.routing.RouteOptions.TimeType} currently shown.
     *
     * @return the {@link com.here.android.mpa.routing.RouteOptions.TimeType}.
     * By default this is {@link com.here.android.mpa.routing.RouteOptions.TimeType#DEPARTURE}.
     */
    public RouteOptions.TimeType getTimeType() {
        return mTimeType;
    }

    /**
     * Sets the {@link com.here.android.mpa.routing.RouteOptions.TimeType} to use.
     * By default {@link com.here.android.mpa.routing.RouteOptions.TimeType#DEPARTURE} is used.
     *
     * @param type the {@link com.here.android.mpa.routing.RouteOptions.TimeType} to set.
     * @return an instance of this class.
     * @throws IllegalArgumentException if {@link com.here.android.mpa.routing.RouteOptions.TimeType} is null.
     */
    public TravelTimePanel setTimeType(final RouteOptions.TimeType type) {
        if (type == null) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_time_type_null));
        }
        if (mOnTimeChangedListener != null && mTimeType != null && !mTimeType.equals(type)) {
            mOnTimeChangedListener.onTimeChanged(getTime(), type);
        }
        mTimeType = type;
        displayTimeTypeOnUi();
        return this;
    }

    /**
     * Display the time on UI.
     */
    private void displayDateOnUi() {
        mTravelTime.setText(DateFormatterUtil.format(mDate));
    }

    /**
     * Display the time type on UI.
     */
    private void displayTimeTypeOnUi() {
        String text = getContext().getString(R.string.msdkui_depart_at);
        if (mTimeType == RouteOptions.TimeType.ARRIVAL) {
            text = getContext().getString(R.string.msdkui_arrive_at);
        }
        mTravelText.setText(text);
    }

    /**
     * Sets an {@link OnTimeChangedListener} to get notified when the displayed time has changed.
     *
     * @param listener the {@link OnTimeChangedListener} to set.
     */
    public void setOnTimeChangedListener(final OnTimeChangedListener listener) {
        mOnTimeChangedListener = listener;
    }

    @Override
    public void onTimePicked(final Date date, final RouteOptions.TimeType type) {
        setTime(date);
        setTimeType(type);
    }

    /**
     * An interface definition for a callback to be invoked when the displayed time has changed.
     */
    public interface OnTimeChangedListener {

        /**
         * Called when the time has changed.
         *
         * @param date the current time that is displayed.
         * @param type the current time type that is displayed.
         */
        void onTimeChanged(Date date, RouteOptions.TimeType type);
    }
}
