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

package com.here.msdkui.common;

import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.here.msdkui.common.measurements.UnitSystem;

/**
 * An abstract base view class with unit system {@link UnitSystem}.
 */
public abstract class BaseView extends LinearLayout {

    protected UnitSystem mUnitSystem = UnitSystem.METRIC;
    protected boolean mSaveStateEnabled = true;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public BaseView(Context context) {
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
    public BaseView(Context context, @Nullable AttributeSet attrs) {
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
    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Sets unit system of this view.
     *
     * @param unitSystem
     *         unit system {@link UnitSystem}.
     */
    public void setUnitSystem(UnitSystem unitSystem) {
        this.mUnitSystem = unitSystem;
    }

    /**
     * Returns current unit system of this view.
     *
     * @return unit system {@link UnitSystem}.
     */
    public UnitSystem getUnitSystem() {
        return mUnitSystem;
    }

    /**
     * Controls whether the saving of this view's data is enabled. View's data will be saved in {@link #onSaveInstanceState}
     * and restored in {@link  #onRestoreInstanceState(android.os.Parcelable)}.
     *
     * @param saveStateEnabled Set to false to <em>disable</em> view's data saving, or true
     * (the default) to allow it.
     */
    protected void setSaveStateEnabled(boolean saveStateEnabled) {
        mSaveStateEnabled = saveStateEnabled;
    }

    /**
     * Indicates whether this view will save its data.
     *
     * @return Returns true if the view data saving is enabled, else false.
     */
    public boolean isSaveStateEnabled() {
        return mSaveStateEnabled;
    }
}
