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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.msdkui.R;
import com.here.msdkui.common.DistanceFormatterUtil;

/**
 * A view that shows maneuver after next maneuver.
 */
public class GuidanceNextManeuverPanel extends FrameLayout {

    private GuidanceNextManeuverData mNextManeuverData;

    /**
     * Constructs a new instance.
     */
    public GuidanceNextManeuverPanel(Context context) {
        this(context, null);
    }

    /**
     * Constructs a new instance.
     */
    public GuidanceNextManeuverPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs a new instance.
     */
    public GuidanceNextManeuverPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Constructs a new instance.
     * Requires Lollipop (API level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GuidanceNextManeuverPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.guidance_next_maneuver_panel, this);
    }

    /**
     * Populate the UI with {@link GuidanceNextManeuverData}.
     *
     * @param nextManeuverData
     *         {@link GuidanceNextManeuverData}
     */
    private void populate(GuidanceNextManeuverData nextManeuverData) {

        final View afterNextManeuverContainer = (View) findViewById(R.id.afterNextManeuverContainer);
        final TextView maneuverDistance = (TextView) findViewById(R.id.nextManeuverDistance);
        final TextView streetName = (TextView) findViewById(R.id.afterNextManeuverStreetName);
        final ImageView iconView = (ImageView) findViewById(R.id.nextManeuverIconView);

        if (nextManeuverData == null) {
            afterNextManeuverContainer.setVisibility(View.GONE);
        } else {
            afterNextManeuverContainer.setVisibility(View.VISIBLE);
            iconView.setImageResource(nextManeuverData.getIconId());
            maneuverDistance.setText(DistanceFormatterUtil.format(getContext(), nextManeuverData.getDistance()));
            streetName.setText(nextManeuverData.getStreetName());
        }
    }

    /**
     * Gets next maneuver data which is being used for UI population.
     *
     * @return {@link GuidanceNextManeuverData}
     */
    public @Nullable GuidanceNextManeuverData getNextManeuverData() {
        return mNextManeuverData;
    }

    /**
     * Sets {@link GuidanceNextManeuverData} which will be used to populate UI.
     *
     * @param data
     *         the {@link GuidanceNextManeuverData} to populate the UI.
     */
    public void setNextManeuverData(@Nullable GuidanceNextManeuverData data) {
        mNextManeuverData = data;
        populate(data);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (mNextManeuverData == null) {
            return superState;
        }
        final SavedState savedState = new SavedState(superState);
        savedState.setStateToSave(this.mNextManeuverData);
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
        setNextManeuverData(savedState.getSavedState());
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

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            if (in.readByte() != 0) {
                mStateToSave = GuidanceNextManeuverData.CREATOR.createFromParcel(in);
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
         */
        @Nullable GuidanceNextManeuverData getSavedState() {
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
