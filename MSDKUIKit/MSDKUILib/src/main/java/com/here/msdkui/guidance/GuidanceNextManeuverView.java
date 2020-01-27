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

package com.here.msdkui.guidance;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.msdkui.R;
import com.here.msdkui.common.BaseView;
import com.here.msdkui.common.DistanceFormatterUtil;
import com.here.msdkui.common.ThemeUtil;

/**
 * A view that shows maneuver after next maneuver.
 */
public class GuidanceNextManeuverView extends BaseView {

    private GuidanceNextManeuverData mNextManeuverData;

    /**
     * Constructs a new instance.
     *
     * @param context the required {@link Context}.
     */
    public GuidanceNextManeuverView(Context context) {
        this(context, null);
    }

    /**
     * Constructs a new instance.
     *
     * @param context the required {@link Context}.
     * @param attrs   a set of attributes.
     */
    public GuidanceNextManeuverView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context      the required {@link Context}.
     * @param attrs        a set of attributes.
     * @param defStyleAttr a default style attribute.
     */
    public GuidanceNextManeuverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Constructs a new instance.
     *
     * @param context      the required {@link Context}.
     * @param attrs        a set of attributes.
     * @param defStyleAttr a default style attribute.
     * @param defStyleRes  a default style resource.
     *                     <p>
     *                     Requires Lollipop (API level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GuidanceNextManeuverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.guidance_next_maneuver_panel, this);
        setVisibility(View.GONE);
        if (getBackground() == null) {
            setBackgroundColor(ThemeUtil.getColor(getContext(), R.attr.colorBackgroundViewDark));
        }
    }

    /**
     * Populate the UI with {@link GuidanceNextManeuverData}.
     *
     * @param nextManeuverData {@link GuidanceNextManeuverData}
     */
    private void populate(GuidanceNextManeuverData nextManeuverData) {
        if (nextManeuverData == null) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);

        final ImageView iconView = findViewById(R.id.nextManeuverIconView);
        if (nextManeuverData.getIconId() == null) {
            iconView.setVisibility(View.GONE);
        } else {
            iconView.setVisibility(View.VISIBLE);
            iconView.setImageResource(nextManeuverData.getIconId());
        }

        final TextView maneuverDistance = findViewById(R.id.nextManeuverDistance);
        if (nextManeuverData.getDistance() == null) {
            maneuverDistance.setVisibility(View.GONE);
        } else {
            maneuverDistance.setVisibility(View.VISIBLE);
            maneuverDistance.setText(DistanceFormatterUtil.format(
                    getContext(), nextManeuverData.getDistance(), mUnitSystem));
        }

        final TextView streetName = findViewById(R.id.afterNextManeuverStreetName);
        if (nextManeuverData.getStreetName() == null) {
            streetName.setVisibility(View.GONE);
        } else {
            streetName.setVisibility(View.VISIBLE);
            streetName.setText(nextManeuverData.getStreetName());
        }

        final TextView dotView = findViewById(R.id.dot);
        dotView.setVisibility(nextManeuverData.getDistance() == null || nextManeuverData.getStreetName() == null ?
                View.GONE : View.VISIBLE);

        if (maneuverDistance.getVisibility() == GONE && streetName.getVisibility() == GONE) {
            setIconEndMargin(0);
        }
    }

    /**
     * Sets icon end margin.
     * @param margin integer to represent margin size.
     */
    void setIconEndMargin(final int margin) {
        final ImageView iconView = findViewById(R.id.nextManeuverIconView);
        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
                iconView.getLayoutParams();
        layoutParams.setMarginEnd(margin);
    }

    /**
     * Gets next maneuver data which is being used for UI population.
     *
     * @return {@link GuidanceNextManeuverData}
     */
    public @Nullable
    GuidanceNextManeuverData getNextManeuverData() {
        return mNextManeuverData;
    }

    /**
     * Sets {@link GuidanceNextManeuverData} which will be used to populate UI. Please note
     * setting null data will set the visibility of the layout to GONE. It can be useful to prevent
     * showing outdated or irrelevant data. for example, when the next maneuver is too far away
     * and setting null field in {@link GuidanceNextManeuverData} will put the respective child view's visibility
     * to {@code View.GONE}.
     *
     * @param data the {@link GuidanceNextManeuverData} to populate the UI.
     * @see com.here.msdkui.guidance.GuidanceNextManeuverPresenter
     */
    public void setNextManeuverData(@Nullable GuidanceNextManeuverData data) {
        mNextManeuverData = data;
        populate(data);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState savedState = new SavedState(superState);
        savedState.mSaveDataEnabled = this.mSaveStateEnabled;
        if (mSaveStateEnabled) {
            savedState.setStateToSave(this.mNextManeuverData);
        }
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
        setSaveStateEnabled(savedState.mSaveDataEnabled);
        if (mSaveStateEnabled && savedState.getSavedState() != null) {
            setNextManeuverData(savedState.getSavedState());
        }
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
        private GuidanceNextManeuverData mStateToSave;
        private boolean mSaveDataEnabled;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            mSaveDataEnabled = in.readByte() == 1;
            if (in.readByte() != 0) {
                mStateToSave = GuidanceNextManeuverData.CREATOR.createFromParcel(in);
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte(mSaveDataEnabled ? (byte) 1 : (byte) 0);
            if (mStateToSave == null) {
                out.writeByte((byte) 0);
            } else {
                out.writeByte((byte) 1);
                mStateToSave.writeToParcel(out, flags);
            }
        }

        /**
         * Gets the saved states.
         */
        @Nullable
        GuidanceNextManeuverData getSavedState() {
            return mStateToSave;
        }

        /**
         * Sets the state to be saved.
         */
        void setStateToSave(@Nullable GuidanceNextManeuverData mStateToSave) {
            this.mStateToSave = mStateToSave;
        }
    }
}
