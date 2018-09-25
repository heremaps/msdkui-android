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
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.here.msdkui.R;

/**
 * A view that shows the current street the user is driving on. This view consumes data contained in
 * {@link GuidanceCurrentStreetData}.
 */
public class GuidanceCurrentStreet extends FrameLayout {

    private GuidanceCurrentStreetData mGuidanceCurrentStreetData;

    /**
     * Constructs a new instance.
     */
    public GuidanceCurrentStreet(Context context) {
        this(context, null);
    }

    /**
     * Constructs a new instance.
     */
    public GuidanceCurrentStreet(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs a new instance.
     */
    public GuidanceCurrentStreet(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Constructs a new instance.
     * Requires Lollipop (API level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GuidanceCurrentStreet(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.guidance_current_street, this);
    }

    /**
     * Sets current street data.
     *
     * @param currentStreetData -
     *          the data that should be used to populate this view.
     */
    public void setCurrentStreetData(@NonNull final GuidanceCurrentStreetData currentStreetData) {
        final View container = findViewById(R.id.guidance_current_street_view);
        if (currentStreetData.getCurrentStreetName() == null || currentStreetData.getCurrentStreetName().isEmpty()) {
            container.setVisibility(GONE);
            return;
        } else {
            container.setVisibility(VISIBLE);
        }

        final TextView currentStreetLabelView = findViewById(R.id.guidance_current_street_text);
        currentStreetLabelView.setText(currentStreetData.getCurrentStreetName());
        container.getBackground().setColorFilter(currentStreetData.getBackgroundColor(), PorterDuff.Mode.SRC);
        mGuidanceCurrentStreetData = new GuidanceCurrentStreetData(currentStreetData.getCurrentStreetName(),
                currentStreetData.getBackgroundColor());
    }

    /**
     * Gets current street data.
     *
     * @return the data that this view was populated with.
     */
    public GuidanceCurrentStreetData getGuidanceCurrentStreetData() {
        return mGuidanceCurrentStreetData;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (mGuidanceCurrentStreetData == null) {
            return superState;
        }
        final GuidanceCurrentStreet.SavedState savedState = new GuidanceCurrentStreet.SavedState(superState);
        savedState.setStateToSave(this.mGuidanceCurrentStreetData);
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof GuidanceCurrentStreet.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        final GuidanceCurrentStreet.SavedState savedState = (GuidanceCurrentStreet.SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setCurrentStreetData(savedState.getSavedState());
    }

    /**
     * State class to save internal data on activity re-creation.
     */
    static class SavedState extends BaseSavedState {
        /**
         * Creator for parcelable.
         */
        public static final Parcelable.Creator<GuidanceCurrentStreet.SavedState> CREATOR =
                new Parcelable.Creator<GuidanceCurrentStreet.SavedState>() {
                    public GuidanceCurrentStreet.SavedState createFromParcel(Parcel in) {
                        return new GuidanceCurrentStreet.SavedState(in);
                    }

                    public GuidanceCurrentStreet.SavedState[] newArray(int size) {
                        return new GuidanceCurrentStreet.SavedState[size];
                    }
                };
        private GuidanceCurrentStreetData mStateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            mStateToSave = GuidanceCurrentStreetData.CREATOR.createFromParcel(in);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            mStateToSave.writeToParcel(out, flags);
        }

        GuidanceCurrentStreetData getSavedState() {
            return mStateToSave;
        }

        void setStateToSave(GuidanceCurrentStreetData mStateToSave) {
            this.mStateToSave = mStateToSave;
        }
    }
}
