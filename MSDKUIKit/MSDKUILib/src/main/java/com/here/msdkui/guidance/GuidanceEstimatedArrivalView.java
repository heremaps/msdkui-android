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
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.here.android.mpa.guidance.NavigationManager;
import com.here.msdkui.R;
import com.here.msdkui.common.DateFormatterUtil;
import com.here.msdkui.common.DistanceFormatterUtil;
import com.here.msdkui.common.TimeFormatterUtil;

/**
 * A view that shows estimated arrival information, like estimated time of arrival (ETA), distance to
 * destination and remaining travel time.
 */
public class GuidanceEstimatedArrivalView extends FrameLayout {

    private GuidanceEstimatedArrivalData mData;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public GuidanceEstimatedArrivalView(Context context) {
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
    public GuidanceEstimatedArrivalView(Context context, @Nullable AttributeSet attrs) {
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
    public GuidanceEstimatedArrivalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    public GuidanceEstimatedArrivalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.guidance_arrival_info, this);
    }

    private void populate(@Nullable GuidanceEstimatedArrivalData data) {
        final TextView eta = (TextView) findViewById(R.id.eta);
        final TextView distance = (TextView) findViewById(R.id.distance);
        final TextView duration = (TextView) findViewById(R.id.duration);
        final String etaText =
                data == null || data.getEta().equals(NavigationManager.INVALID_ETA_DATE) ?
                        getContext().getString(R.string.msdkui_value_not_available) :
                        DateFormatterUtil.format(getContext(), data.getEta());
        eta.setText(etaText);

        final String distanceText = data == null || data.getDistance() < 0 ?
                getContext().getString(R.string.msdkui_value_not_available) :
                DistanceFormatterUtil.formatDistanceForUI(getContext(), data.getDistance());
        distance.setText(distanceText);

        final String durationText = data == null || data.getDuration() < 0 ?
                getContext().getString(R.string.msdkui_value_not_available) :
                TimeFormatterUtil.format(getContext(), data.getDuration());
        duration.setText(durationText);
    }

    /**
     * Gets current {@link GuidanceEstimatedArrivalData}.
     *
     * @return data used to populate this view.
     */
    @Nullable public GuidanceEstimatedArrivalData getEstimatedArrivalData() {
        return mData;
    }

    /**
     * Sets the @{link Date} of estimated arrival.
     *
     * @param data
     *         the data to set.
     */
    public void setEstimatedArrivalData(@Nullable GuidanceEstimatedArrivalData data) {
        mData = data;
        populate(data);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (mData == null) {
            return superState;
        }
        final SavedState savedState = new SavedState(superState);
        savedState.setStateToSave(this.mData);
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setEstimatedArrivalData(savedState.getSavedState());
    }

    /**
     * State class to save internal data on activity re-creation.
     */
    public static class SavedState extends BaseSavedState {
        /**
         * Creator for parcelable.
         */
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        private GuidanceEstimatedArrivalData mStateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            if (in.readByte() != 0) {
                mStateToSave = GuidanceEstimatedArrivalData.CREATOR.createFromParcel(in);
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            if (mStateToSave == null) {
                out.writeByte((byte) 0);
            } else {
                out.writeByte((byte) 1);
                mStateToSave.writeToParcel(out, flags);
            }
        }

        /**
         * Gets the saved states.
         *
         * @return saved instance of {@link GuidanceEstimatedArrivalData}.
         */
        @Nullable GuidanceEstimatedArrivalData getSavedState() {
            return mStateToSave;
        }

        /**
         * Sets the state to be saved.
         *
         * @param state
         *         an instance of {@link GuidanceEstimatedArrivalData} to be saved.
         */
        void setStateToSave(@Nullable GuidanceEstimatedArrivalData state) {
            this.mStateToSave = state;
        }
    }
}
