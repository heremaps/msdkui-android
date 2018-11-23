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

package com.here.msdkui.guidance;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.here.msdkui.R;

/**
 * A view that shows the current speed the user is driving with. This view consumes data contained in
 * {@link GuidanceSpeedData}.
 * A user can provide a background with different appearances for the current speed. To do this
 * the attribute guidanceSpeedLimitExceeded should be used accordingly in a <code>Selector</code> you
 * can set as drawable in your layout file.
 * Here is an example of how to use the attribute. Define a background for GuidanceCurrentSpeedPanel in your layout file:
 * <pre>
 *     ...
 *     android:background="@drawable/my_bg_with_states"
 *     ...
 * </pre>
 * and define my_bg_with_states.xml in the following way:
 * <pre>
 * &#60;?xml version="1.0" encoding="utf-8"?&#62;
 * &#60;selector xmlns:android="http://schemas.android.com/apk/res/android"
 *  xmlns:app="http://schemas.android.com/apk/res-auto"&#62;
 *      &#60;item
 *          app:guidanceSpeedLimitExceeded="true"
 *          android:drawable="&#64;drawable/my_bg_with_states_warning_item" /&#62;
 *      &#60;item
 *          app:guidanceSpeedLimitExceeded="false"
 *          android:drawable="&#64;drawable/my_bg_with_states_regular_item" /&#62;
 * &#60;/selector&#62;
 * </pre>
 * The view will switch the background item depending on the data set in
 * {@link GuidanceCurrentSpeedPanel#setCurrentSpeedData(GuidanceSpeedData)}.
 */
public class GuidanceCurrentSpeedPanel extends RelativeLayout {

    private static final int[] STATE_SPEEDING = {
            R.attr.guidanceSpeedLimitExceeded
    };

    private GuidanceSpeedData mGuidanceSpeedData;
    private int mValueTextColor;
    private int mUnitTextColor;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public GuidanceCurrentSpeedPanel(Context context) {
        this(context, null);
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
    public GuidanceCurrentSpeedPanel(Context context, @Nullable AttributeSet attrs) {
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
    public GuidanceCurrentSpeedPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    public GuidanceCurrentSpeedPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        if (mGuidanceSpeedData != null && mGuidanceSpeedData.isSpeeding()) {
            final int[] state = super.onCreateDrawableState(extraSpace + 1);
            mergeDrawableStates(state, STATE_SPEEDING);
            return state;
        }
        return super.onCreateDrawableState(extraSpace);
    }

    private void init(final Context context) {
        mGuidanceSpeedData = new GuidanceSpeedData();
        LayoutInflater.from(context).inflate(R.layout.guidance_current_speed_panel, this);
    }

    private void populateUi(@Nullable GuidanceSpeedData data) {
        final String speedText = data == null || !data.isValid() ?
                getContext().getString(R.string.msdkui_value_not_available) :
                String.valueOf(data.getCurrentSpeed());
        final TextView speed = findViewById(R.id.guidance_current_speed_value);
        speed.setText(speedText);
    }

    /**
     * Sets the color for speed value text.
     *
     * @param color
     *         a color value in the form 0xAARRGGBB.
     */
    public void setValueTextColor(int color) {
        if (mValueTextColor == color) {
            return;
        }
        mValueTextColor = color;
        final TextView view = findViewById(R.id.guidance_current_speed_value);
        view.setTextColor(color);
    }

    /**
     * Sets the color for speed unit text.
     *
     * @param color
     *         a color value in the form 0xAARRGGBB.
     */
    public void setUnitTextColor(int color) {
        if (mUnitTextColor == color) {
            return;
        }
        mUnitTextColor = color;
        final TextView view = findViewById(R.id.guidance_current_speed_unit);
        view.setTextColor(color);
    }

    /**
     * Sets current speed data.
     *
     * @param data
     *         the data that should be used to populate this view.
     */
    public void setCurrentSpeedData(@Nullable GuidanceSpeedData data) {
        populateUi(data);
        if (data == null || !data.isValid()) {
            mGuidanceSpeedData.invalidate();
            return;
        }
        boolean refreshBackground = false;
        if (!mGuidanceSpeedData.isValid() || data.isSpeeding() != mGuidanceSpeedData.isSpeeding()) {
            refreshBackground = true;
        }
        mGuidanceSpeedData = new GuidanceSpeedData(data.getCurrentSpeed(), data.getCurrentSpeedLimit());
        if (refreshBackground) {
            post(this::refreshDrawableState);
        }
    }

    /**
     * Gets current speed data.
     *
     * @return the data that was used to populate this view.
     */
    public @NonNull GuidanceSpeedData getCurrentSpeedData() {
        return new GuidanceSpeedData(mGuidanceSpeedData.getCurrentSpeed(),
                mGuidanceSpeedData.getCurrentSpeedLimit());
    }

    /**
     * Gets current color for value text.
     *
     * @return a color value as integer.
     */
    public int getValueTextColor() {
        return mValueTextColor;
    }

    /**
     * Gets current color value for unit text.
     *
     * @return a color value as integer.
     */
    public int getUnitTextColor() {
        return mUnitTextColor;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (mGuidanceSpeedData == null) {
            return superState;
        }
        final GuidanceCurrentSpeedPanel.SavedState savedState = new GuidanceCurrentSpeedPanel.SavedState(superState);
        savedState.setStateToSave(this.mGuidanceSpeedData, mValueTextColor, mUnitTextColor);
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof GuidanceCurrentSpeedPanel.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        final GuidanceCurrentSpeedPanel.SavedState savedState = (GuidanceCurrentSpeedPanel.SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setCurrentSpeedData(savedState.getSavedSpeedData());
    }

    /**
     * State class to save internal data on activity re-creation.
     */
    static class SavedState extends BaseSavedState {
        /**
         * Creator for parcelable.
         */
        public static final Parcelable.Creator<GuidanceCurrentSpeedPanel.SavedState> CREATOR =
                new Parcelable.Creator<GuidanceCurrentSpeedPanel.SavedState>() {
                    public GuidanceCurrentSpeedPanel.SavedState createFromParcel(Parcel in) {
                        return new GuidanceCurrentSpeedPanel.SavedState(in);
                    }

                    public GuidanceCurrentSpeedPanel.SavedState[] newArray(int size) {
                        return new GuidanceCurrentSpeedPanel.SavedState[size];
                    }
                };
        private GuidanceSpeedData mData;
        private int mValueTextColor;
        private int mUnitTextColor;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            mData = GuidanceSpeedData.CREATOR.createFromParcel(in);
            mValueTextColor = in.readInt();
            mUnitTextColor = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            mData.writeToParcel(out, flags);
            out.writeInt(mValueTextColor);
            out.writeInt(mUnitTextColor);
        }

        GuidanceSpeedData getSavedSpeedData() {
            return mData;
        }

        int getSavedValueTextColor() {
            return mValueTextColor;
        }

        int getSavedUnitTextColor() {
            return mUnitTextColor;
        }

        void setStateToSave(GuidanceSpeedData data, int valueColor, int unitColor) {
            this.mData = data;
            this.mValueTextColor = valueColor;
            this.mUnitTextColor = unitColor;
        }
    }
}
