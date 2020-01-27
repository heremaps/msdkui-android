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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.here.msdkui.R;

/**
 * A view that shows the current street the user is driving on. This view consumes data contained in
 * {@link GuidanceStreetLabelData}.
 */
public class GuidanceStreetLabelView extends LinearLayout {

    private GuidanceStreetLabelData mGuidanceStreetLabelData;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public GuidanceStreetLabelView(Context context) {
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
    public GuidanceStreetLabelView(Context context, @Nullable AttributeSet attrs) {
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
    public GuidanceStreetLabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    public GuidanceStreetLabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.guidance_current_street, this);
        setVisibility(GONE);
        if (getBackground() == null) {
            setBackground(ContextCompat.getDrawable(getContext(), R.drawable.current_street_name_bg));
        }
    }

    /**
     * Sets current street data {@link GuidanceStreetLabelData} which will be used to populate UI.
     * Please note setting null GuidanceStreetLabelData or null field inside the data will set the visibility of the
     * view to {@code View.GONE}.
     *
     * @param currentStreetData -
     *          the data that should be used to populate this view.
     */
    public void setCurrentStreetData(@Nullable final GuidanceStreetLabelData currentStreetData) {
         mGuidanceStreetLabelData = currentStreetData;
        if (currentStreetData == null || currentStreetData.getCurrentStreetName() == null ||
                currentStreetData.getCurrentStreetName().isEmpty()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }
        final TextView currentStreetLabelView = findViewById(R.id.guidance_current_street_text);
        currentStreetLabelView.setText(currentStreetData.getCurrentStreetName());
        refresh();
    }

    /**
     * Refresh the view to re-calculate the radius of view.
     */
    public void refresh()  {
        Drawable backgroundDrawable = getBackground();
        if (backgroundDrawable instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) backgroundDrawable;
            post(() -> drawable.setCornerRadius(getHeight() >> 1));
            drawable.setColorFilter(mGuidanceStreetLabelData.getBackgroundColor(), PorterDuff.Mode.SRC);
        }
    }

    /**
     * Gets current street data.
     *
     * @return the data that this view was populated with.
     */
    public GuidanceStreetLabelData getGuidanceCurrentStreetData() {
        return mGuidanceStreetLabelData;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
       final Parcelable superState = super.onSaveInstanceState();
        final GuidanceStreetLabelView.SavedState savedState = new GuidanceStreetLabelView.SavedState(superState);
        savedState.setStateToSave(this.mGuidanceStreetLabelData);
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof GuidanceStreetLabelView.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        final GuidanceStreetLabelView.SavedState savedState = (GuidanceStreetLabelView.SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.getSavedState() != null) {
            setCurrentStreetData(savedState.getSavedState());
        }
    }

    /**
     * State class to save internal data on activity re-creation.
     */
    static class SavedState extends BaseSavedState {
        /**
         * Creator for parcelable.
         */
        public static final Parcelable.Creator<GuidanceStreetLabelView.SavedState> CREATOR =
                new Parcelable.Creator<GuidanceStreetLabelView.SavedState>() {
                    public GuidanceStreetLabelView.SavedState createFromParcel(Parcel in) {
                        return new GuidanceStreetLabelView.SavedState(in);
                    }

                    public GuidanceStreetLabelView.SavedState[] newArray(int size) {
                        return new GuidanceStreetLabelView.SavedState[size];
                    }
                };
        private GuidanceStreetLabelData mStateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            if (in.readByte() != 0) {
                mStateToSave = GuidanceStreetLabelData.CREATOR.createFromParcel(in);
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

        GuidanceStreetLabelData getSavedState() {
            return mStateToSave;
        }

        void setStateToSave(GuidanceStreetLabelData mStateToSave) {
            this.mStateToSave = mStateToSave;
        }
    }
}
