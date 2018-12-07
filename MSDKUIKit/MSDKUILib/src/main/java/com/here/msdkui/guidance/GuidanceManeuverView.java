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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.here.msdkui.R;
import com.here.msdkui.common.DistanceFormatterUtil;

/**
 * A view that shows the next maneuver panel for guidance. The view consumes the data contained in
 * {@link GuidanceManeuverData}.
 */
public class GuidanceManeuverView extends RelativeLayout {

    private static final String EMPTY_STRING = "";

    private GuidanceManeuverData mManeuverData;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public GuidanceManeuverView(Context context) {
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
    public GuidanceManeuverView(Context context, @Nullable AttributeSet attrs) {
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
    public GuidanceManeuverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    public GuidanceManeuverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    private void populateBusyProgressBarView(@Nullable GuidanceManeuverData maneuverData) {
        final ProgressBar busyProgressBar = findViewById(R.id.busyStateProgressBar);
        if (maneuverData != null &&
                maneuverData.getIconId() == -1 && maneuverData.getDistance() == -1 &&
                getContext().getString(R.string.msdkui_maneuverpanel_updating).equals(maneuverData.getInfo1()) &&
                EMPTY_STRING.equals(maneuverData.getInfo2())) {
            busyProgressBar.setVisibility(View.VISIBLE);
        } else {
            busyProgressBar.setVisibility(View.GONE);
        }
    }

    private void populateDistanceView(@Nullable GuidanceManeuverData maneuverData) {
        final TextView distanceView = findViewById(R.id.distanceView);
        if (maneuverData == null || maneuverData.getDistance() == -1) {
            distanceView.setVisibility(View.INVISIBLE);
        } else {
            distanceView.setVisibility(View.VISIBLE);
            distanceView.setText(
                    DistanceFormatterUtil.formatDistanceForUI(getContext(), maneuverData.getDistance()));
        }
    }

    private void populateInfoView1(@Nullable GuidanceManeuverData maneuverData) {
        final TextView infoView1 = findViewById(R.id.infoView1);
        if (maneuverData == null || maneuverData.getInfo1() == null) {
            infoView1.setVisibility(View.GONE);
        } else if (maneuverData.getInfo1().equals("")) {
            infoView1.setVisibility(View.INVISIBLE);
        } else {
            infoView1.setVisibility(View.VISIBLE);
            infoView1.setText(maneuverData.getInfo1());
        }
    }

    private void populateInfoView2(@Nullable GuidanceManeuverData maneuverData) {
        final TextView infoView2 = findViewById(R.id.infoView2);
        if (maneuverData == null || maneuverData.getInfo2() == null) {
            infoView2.setVisibility(View.GONE);
        } else if (maneuverData.getInfo2().equals("")) {
            infoView2.setVisibility(View.INVISIBLE);
        } else {
            infoView2.setVisibility(View.VISIBLE);
            infoView2.setText(maneuverData.getInfo2());
        }
    }

    private void populateIconView(@Nullable GuidanceManeuverData maneuverData) {
        final ImageView iconView = findViewById(R.id.maneuverIconView);
        if (maneuverData == null || maneuverData.getIconId() == -1) {
            iconView.setVisibility(View.INVISIBLE);
            iconView.setTag(0);
        } else if (maneuverData.getIconId() == 0) {
            iconView.setVisibility(View.GONE);
            iconView.setTag(maneuverData.getIconId());
        } else {
            iconView.setVisibility(View.VISIBLE);
            iconView.setImageResource(maneuverData.getIconId());
            iconView.setTag(maneuverData.getIconId());
        }
    }

    private void populateExtraIconView(@Nullable GuidanceManeuverData maneuverData) {
        final ImageView extraIconView = findViewById(R.id.extraIconView);
        if (maneuverData == null || maneuverData.getNextRoadIcon() == null) {
            extraIconView.setVisibility(View.INVISIBLE);
        } else {
            extraIconView.setVisibility(View.VISIBLE);
            extraIconView.setImageBitmap(maneuverData.getNextRoadIcon());
        }
    }

    /**
     * Populate the UI with {@link GuidanceManeuverData}. Setting null data will put the panel in waiting state.
     *
     * @param maneuverData
     *         The {@link GuidanceManeuverData} to use. In case of null, the loading state of the panel will be shown.
     */
    private void populate(@Nullable GuidanceManeuverData maneuverData) {
        populateBusyProgressBarView(maneuverData);
        populateIconView(maneuverData);
        populateExtraIconView(maneuverData);
        populateDistanceView(maneuverData);
        populateInfoView1(maneuverData);
        populateInfoView2(maneuverData);
    }

    /**
     * Gets maneuver data which is being used for UI population.
     *
     * @return a {@link GuidanceManeuverData} instance.
     */
    public @Nullable
    GuidanceManeuverData getManeuverData() {
        return mManeuverData;
    }

    /**
     * Sets the {@link GuidanceManeuverData} which will be used for UI population.
     *
     * @param maneuverData
     *         the {@link GuidanceManeuverData} to populate the UI. Please note that in case of null, the loading state
     *         of the panel will be shown.
     */
    public void setManeuverData(@Nullable GuidanceManeuverData maneuverData) {
        mManeuverData = maneuverData;
        populate(maneuverData);
    }

    /**
     * Highlights maneuver section (info2) of panel using the provided color.
     *
     * @param color
     *         the color to highlight a maneuver.
     */
    public void highLightManeuver(int color) {
        ((TextView) findViewById(R.id.infoView2)).setTextColor(color);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState savedState = new SavedState(superState);
        savedState.setManeuverData(this.mManeuverData);
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
        if (savedState.getManeuverData() != null) {
            setManeuverData(savedState.getManeuverData());
        }
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
        private GuidanceManeuverData mManeuverData;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            final boolean isGuidanceManeuverDataNotNull = in.readByte() != 0;
            if (isGuidanceManeuverDataNotNull) {
                mManeuverData = GuidanceManeuverData.CREATOR.createFromParcel(in);
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            if (mManeuverData == null) {
                out.writeByte((byte) (0));
            } else {
                out.writeByte((byte) (1));
                mManeuverData.writeToParcel(out, flags);
            }
        }

        /**
         * Gets the saved maneuver data.
         */
        @Nullable
        GuidanceManeuverData getManeuverData() {
            return mManeuverData;
        }

        /**
         * Sets the maneuver data to be saved.
         */
        void setManeuverData(@Nullable GuidanceManeuverData data) {
            this.mManeuverData = data;
        }
    }
}
