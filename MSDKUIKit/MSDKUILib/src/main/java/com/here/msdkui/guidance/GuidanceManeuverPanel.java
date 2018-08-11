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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.here.msdkui.R;
import com.here.msdkui.common.DistanceFormatterUtil;

/**
 * A view that shows the next maneuver panel for guidance. This view consumes the data contained in
 * {@link GuidanceManeuverData}.
 */
public class GuidanceManeuverPanel extends RelativeLayout {

    private GuidanceManeuverData mManeuverData;

    /**
     * Constructs a new instance.
     */
    public GuidanceManeuverPanel(Context context) {
        this(context, null);
    }

    /**
     * Constructs a new instance.
     */
    public GuidanceManeuverPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs a new instance.
     */
    public GuidanceManeuverPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Constructs a new instance.
     * Requires Lollipop (API level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GuidanceManeuverPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * Init the Panel UI.
     *
     * @param context
     *         activity or application context.
     */
    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.guidance_maneuver_panel, this);
    }

    /**
     * Populate the UI with {@link GuidanceManeuverData}.
     *
     * @param maneuverData
     *         {@link GuidanceManeuverData}
     */
    private void populate(GuidanceManeuverData maneuverData) {

        final TextView distanceView = (TextView) findViewById(R.id.distanceView);
        final TextView infoView1 = (TextView) findViewById(R.id.infoView1);
        final TextView infoView2 = (TextView) findViewById(R.id.infoView2);
        final ImageView iconView = (ImageView) findViewById(R.id.maneuverIconView);

        if (maneuverData.getIconId() == 0) {
            iconView.setVisibility(View.GONE);
        } else {
            iconView.setVisibility(View.VISIBLE);
            iconView.setImageResource(maneuverData.getIconId());
        }

        if (maneuverData.getDistance() == 0) {
            distanceView.setVisibility(View.GONE);
        } else {
            distanceView.setVisibility(View.VISIBLE);
            distanceView.setText(DistanceFormatterUtil.format(getContext(), maneuverData.getDistance()));
        }

        if (maneuverData.getInfo1() == null) {
            infoView1.setVisibility(View.GONE);
        } else {
            infoView1.setVisibility(View.VISIBLE);
            infoView1.setText(maneuverData.getInfo1());
        }

        if (maneuverData.getInfo2() == null) {
            infoView2.setVisibility(View.GONE);
        } else {
            infoView2.setVisibility(View.VISIBLE);
            infoView2.setText(maneuverData.getInfo2());
        }
    }

    /**
     * Gets {@link GuidanceManeuverData} which is being used for UI population.
     */
    public GuidanceManeuverData getManeuverData() {
        return mManeuverData;
    }

    /**
     * Sets {@link GuidanceManeuverData} which will be used for UI population.
     *
     * @param maneuverData
     *         the {@link GuidanceManeuverData} to populate the UI.
     */
    public void setManeuverData(GuidanceManeuverData maneuverData) {
        mManeuverData = maneuverData;
        if (maneuverData == null) {
            return;
        }
        populate(maneuverData);
    }

    /**
     * Highlights maneuver section (info2) of panel using the provided color.
     *
     * @param color
     *         color that need to be set to highlights maneuver.
     */
    public void highLightManeuver(int color) {
        ((TextView) findViewById(R.id.infoView2)).setTextColor(color);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (mManeuverData == null) {
            return superState;
        }
        final SavedState savedState = new SavedState(superState);
        savedState.setStateToSave(this.mManeuverData);
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
        setManeuverData(savedState.getSavedState());
    }

    /**
     * State class to save internal data on activity re-creation.
     */
    static class SavedState extends BaseSavedState {
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
        private GuidanceManeuverData mStateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            mStateToSave = GuidanceManeuverData.CREATOR.createFromParcel(in);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            mStateToSave.writeToParcel(out, flags);
        }

        /**
         * Gets the saved states.
         */
        GuidanceManeuverData getSavedState() {
            return mStateToSave;
        }

        /**
         * Sets the state to be saved.
         */
        void setStateToSave(GuidanceManeuverData mStateToSave) {
            this.mStateToSave = mStateToSave;
        }
    }
}
