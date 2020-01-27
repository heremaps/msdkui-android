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
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
 * A view that shows the next maneuver view for guidance. The view consumes the data contained in
 * {@link GuidanceManeuverData}.
 */
public class GuidanceManeuverView extends BaseView {

    public static final int ICON_GONE = 0;
    private State mState;

    /**
     * Represent different ui supported by this view.
     */
    private enum ViewMode {
        /**
         * Represent ui view one.
         */
        SCREEN1,

        /**
         * Represent another ui view.
         */
        SCREEN2
    }

    /**
     * Constructs a new instance.
     *
     * @param context the required {@link Context}.
     */
    public GuidanceManeuverView(@NonNull Context context) {
        this(context, null);
    }

    /**
     * Constructs a new instance.
     *
     * @param context the required {@link Context}.
     * @param attrs   a set of attributes.
     */
    public GuidanceManeuverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context      the required {@link Context}.
     * @param attrs        a set of attributes.
     * @param defStyleAttr a default style attribute.
     */
    public GuidanceManeuverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
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
    public GuidanceManeuverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * Initializes the view based on GuidanceManeuverView_viewMode and orientation.
     *
     * @param context activity or application context.
     */
    private void init(@NonNull final Context context, @Nullable AttributeSet attributeSet) {
        ViewMode screen = ViewMode.SCREEN1;
        if (attributeSet != null) {
            final TypedArray typedArray = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.GuidanceManeuverView);
            if (typedArray.hasValue(R.styleable.GuidanceManeuverView_viewMode)) {
                final int value = typedArray.getInt(R.styleable.GuidanceManeuverView_viewMode, 2);
                if (value == 1) {
                    screen = ViewMode.SCREEN2;
                }
            }
            typedArray.recycle();
        }
        if (screen == ViewMode.SCREEN2) {
            LayoutInflater.from(context).inflate(R.layout.guidance_maneuver_view_screen2, this);
        } else {
            LayoutInflater.from(context).inflate(R.layout.guidance_maneuver_view_screen1, this);
        }
        if (getBackground() == null) {
            setBackgroundColor(ThemeUtil.getColor(getContext(), R.attr.colorBackgroundDark));
        }
        setViewState(State.NO_DATA);
    }

    private void populateDefaultState() {
        ImageView iconView = findViewById(R.id.maneuverIconView);
        iconView.setImageResource(R.drawable.ic_car_position_marker);
        final TextView defaultViewTextView = findViewById(R.id.defaultViewText);
        defaultViewTextView.setVisibility(VISIBLE);
        defaultViewTextView.setText(getContext().getString(R.string.msdkui_maneuverpanel_nodata));
        findViewById(R.id.busyStateProgressBar).setVisibility(View.GONE);
        findViewById(R.id.distanceView).setVisibility(View.GONE);
        findViewById(R.id.extraIconView).setVisibility(View.GONE);
        findViewById(R.id.infoView1).setVisibility(View.GONE);
        findViewById(R.id.infoView2).setVisibility(View.GONE);
    }

    private void populateBusyProgressBarView() {
        findViewById(R.id.maneuverIconView).setVisibility(INVISIBLE);
        findViewById(R.id.busyStateProgressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.distanceView).setVisibility(View.GONE);
        findViewById(R.id.extraIconView).setVisibility(View.GONE);
        final TextView defaultViewTextView = findViewById(R.id.defaultViewText);
        defaultViewTextView.setVisibility(VISIBLE);
        defaultViewTextView.setText(getContext().getString(R.string.msdkui_maneuverpanel_updating));
        findViewById(R.id.infoView1).setVisibility(View.GONE);
        findViewById(R.id.infoView2).setVisibility(View.GONE);
    }

    private void populateDistanceView(@NonNull GuidanceManeuverData maneuverData) {
        final TextView distanceView = findViewById(R.id.distanceView);
        if (maneuverData.getDistance() == null) {
            distanceView.setVisibility(View.GONE);
        } else {
            distanceView.setVisibility(View.VISIBLE);
            distanceView.setText(DistanceFormatterUtil.formatDistance(
                    getContext(), maneuverData.getDistance(), mUnitSystem));
        }
    }

    private void populateInfoView1(@NonNull GuidanceManeuverData maneuverData) {
        final TextView infoView1 = findViewById(R.id.infoView1);
        if (maneuverData.getInfo1() == null) {
            infoView1.setVisibility(View.GONE);
        } else {
            infoView1.setVisibility(View.VISIBLE);
            infoView1.setText(maneuverData.getInfo1());
        }
    }

    private void populateInfoView2(@NonNull GuidanceManeuverData maneuverData) {
        final TextView infoView2 = findViewById(R.id.infoView2);
        if (maneuverData.getInfo2() == null) {
            infoView2.setVisibility(View.GONE);
        } else {
            infoView2.setVisibility(View.VISIBLE);
            infoView2.setText(maneuverData.getInfo2());
        }
    }

    private void populateIconView(@NonNull GuidanceManeuverData maneuverData) {
        final ImageView iconView = findViewById(R.id.maneuverIconView);
        if (maneuverData.getIconId() == -1) {
            iconView.setVisibility(View.INVISIBLE);
            iconView.setTag(0);
        } else if (maneuverData.getIconId() == ICON_GONE) {
            iconView.setVisibility(View.GONE);
            iconView.setTag(maneuverData.getIconId());
        } else {
            iconView.setVisibility(View.VISIBLE);
            iconView.setImageResource(maneuverData.getIconId());
            iconView.setTag(maneuverData.getIconId());
        }
    }

    private void populateExtraIconView(@NonNull GuidanceManeuverData maneuverData) {
        final ImageView extraIconView = findViewById(R.id.extraIconView);
        if (maneuverData.getNextRoadIcon() == null) {
            extraIconView.setVisibility(View.GONE);
        } else {
            extraIconView.setVisibility(View.VISIBLE);
            extraIconView.setImageBitmap(maneuverData.getNextRoadIcon());
        }
    }

    /**
     * Populate the UI with {@link GuidanceManeuverData}.
     * Please note that setting a null field in {@link GuidanceManeuverData} will put the respective child view's
     * visibility to {@code View.GONE}.
     *
     * @param maneuverData The {@link GuidanceManeuverData} to use.
     */
    private void populate(@Nullable GuidanceManeuverData maneuverData) {
        if (maneuverData == null) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        findViewById(R.id.busyStateProgressBar).setVisibility(View.GONE);
        findViewById(R.id.defaultViewText).setVisibility(View.GONE);
        populateIconView(maneuverData);
        populateExtraIconView(maneuverData);
        populateDistanceView(maneuverData);
        populateInfoView1(maneuverData);
        populateInfoView2(maneuverData);
    }

    /**
     * Gets the maneuver data which is being used for UI population.
     *
     * @return a {@link GuidanceManeuverData} instance.
     * @deprecated This method will be removed. Please see {@link #getViewState()}.
     */
    public @Nullable
    GuidanceManeuverData getManeuverData() {
        return mState.mGuidanceManeuverData;
    }

    /**
     * Sets the {@link GuidanceManeuverData} which will be used for UI population.
     * Please note that setting a null field in {@link GuidanceManeuverData} will put the respective child view's
     * visibility to {@code View.GONE}.
     *
     * @param maneuverData the {@link GuidanceManeuverData} to populate the UI.
     * @see #setViewState(com.here.msdkui.guidance.GuidanceManeuverView.State)
     * @deprecated This method will be removed. Please see {@link #getViewState()}.
     */
    public void setManeuverData(@NonNull GuidanceManeuverData maneuverData) {
        mState = new State(maneuverData);
        populate(maneuverData);
    }

    /**
     * Gets the {@link com.here.msdkui.guidance.GuidanceManeuverView.State GuidanceManeuverView.State} of this view which was used for
     * UI population.
     *
     * @return GuidanceManeuverView.State
     */
    public State getViewState() {
        return mState;
    }

    /**
     * Sets the {@link com.here.msdkui.guidance.GuidanceManeuverView.State GuidanceManeuverView.State} which will be used for UI population.
     * Please note that setting null will set the visibility of the {@code GuidanceManeuverView} to {@code View.GONE}.
     * Setting a field in {@link GuidanceManeuverData} to null will put the respective child view's visibility to {@code View.GONE}.
     *
     * @param state the {@link com.here.msdkui.guidance.GuidanceManeuverView.State GuidanceManeuverView.State} to populate the UI.
     *              Please note that in case of null, the {@code GuidanceManeuverView} will be gone.
     */
    public void setViewState(@Nullable final State state) {
        mState = state;
        if (state == null) {
            setVisibility(GONE);
            return;
        }
        setVisibility(View.VISIBLE);
        if (State.NO_DATA.equals(mState)) {
            populateDefaultState();
        } else if (State.UPDATING.equals(mState)) {
            populateBusyProgressBarView();
        } else {
            populate(state.mGuidanceManeuverData);
        }
    }

    /**
     * Highlights the maneuver section (info2) of the view using the provided color.
     *
     * @param color the color to highlight a maneuver.
     */
    public void highLightManeuver(int color) {
        ((TextView) findViewById(R.id.infoView2)).setTextColor(color);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState savedState = new SavedState(superState);
        savedState.mSaveDataEnabled = this.mSaveStateEnabled;
        if (mSaveStateEnabled) {
            savedState.setViewState(this.mState);
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
        if (mSaveStateEnabled && savedState.getViewState() != null) {
            setViewState(savedState.getViewState());
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

        private State mState;
        private boolean mSaveDataEnabled;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            mSaveDataEnabled = in.readByte() == 1;
            int value = in.readByte();
            if (value == 1) {
                mState = State.CREATOR.createFromParcel(in);
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte(mSaveDataEnabled ? (byte) 1 : (byte) 0);
            if (mState == null) {
                out.writeByte((byte) 0);
                return;
            }
            out.writeByte((byte) 1);
            mState.writeToParcel(out, flags);
        }

        /**
         * Gets the saved state.
         */
        @Nullable
        State getViewState() {
            return mState;
        }

        /**
         * Sets the maneuver data to be saved.
         */
        void setViewState(@Nullable State state) {
            this.mState = state;
        }
    }

    /**
     * Represents all the supported guidance maneuver view states.
     * <p>
     * Note - to compare the different states, please use {@link #equals(Object)} method.
     */
    public static class State implements Parcelable {

        /**
         * Represents the default state of the view without maneuver data.
         */
        public static final State NO_DATA = new State(0);

        /**
         * Represents a loading state where the view is awaiting maneuver data.
         */
        public static final State UPDATING = new State(1);

        public static final Creator<State> CREATOR = new Creator<State>() {
            @Override
            public State createFromParcel(Parcel in) {
                return new State(in);
            }

            @Override
            public State[] newArray(int size) {
                return new State[size];
            }
        };

        private GuidanceManeuverData mGuidanceManeuverData;
        private int mStatusCode;

        private State(int code) {
            this.mStatusCode = code;
        }

        /**
         * Represents a state where the view contains maneuver data.
         *
         * @param data {@link com.here.msdkui.guidance.GuidanceManeuverData} to populate the view.
         */
        public State(@NonNull GuidanceManeuverData data) {
            mGuidanceManeuverData = data;
        }

        protected State(Parcel in) {
            mGuidanceManeuverData = in.readParcelable(GuidanceManeuverData.class.getClassLoader());
            mStatusCode = in.readInt();
        }

        /**
         * Gets the {@link com.here.msdkui.guidance.GuidanceManeuverData} state used to populate the view.
         *
         * @return data {@link com.here.msdkui.guidance.GuidanceManeuverData}.
         */
        public GuidanceManeuverData getData() {
            return mGuidanceManeuverData;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(mGuidanceManeuverData, flags);
            dest.writeInt(mStatusCode);
        }

        @NonNull
        @Override
        public String toString() {
            return "GuidanceManeuverView.State(guidanceManeuverData=" + this.mGuidanceManeuverData +
                    ", statusCode=" + this.mStatusCode + ")";
        }

        @Override
        public int hashCode() {
            return (this.mGuidanceManeuverData != null ? this.mGuidanceManeuverData.hashCode() : 0) * 31 + this.mStatusCode;
        }

        @Override
        public boolean equals(Object var1) {
            if (this != var1) {
                if (var1 instanceof State) {
                    State var2 = (State) var1;
                    if (areEqual(this.mGuidanceManeuverData, var2.mGuidanceManeuverData) && this.mStatusCode == var2.mStatusCode) {
                        return true;
                    }
                }
                return false;
            } else {
                return true;
            }
        }

        private boolean areEqual(Object first, Object second) {
            return first == null ? second == null : first.equals(second);
        }
    }
}
