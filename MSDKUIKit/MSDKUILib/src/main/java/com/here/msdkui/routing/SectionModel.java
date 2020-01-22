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

package com.here.msdkui.routing;

import android.graphics.drawable.Drawable;

/**
 * Defines the model of a {@link SectionBar}.
 */
public final class SectionModel {

    private Bounds mBounds;
    private Drawable mDrawable;
    private int mColor;

    /**
     * Constructs a new instance using default bounds.
     */
    public SectionModel() {
        setBounds(new Bounds(0.0d, 1.0d));
    }

    /**
     * Gets the bounds of this model.
     * @return the bounds of this model.
     */
    public Bounds getBounds() {
        return mBounds;
    }

    /**
     * Sets the bounds of this model. These bounds can be set to the {@link SectionBar} to define the
     * allowed range of the scale factor. By default, the scale factor represents a normalized value between
     * [0, 1], whereas 0 would lead to a width of 0 and 1 would lead to an unscaled width. Be careful when exceeding
     * these thresholds.
     * @param bounds the bounds to use for this model.
     * @return an instance of this class.
     */
    public SectionModel setBounds(final Bounds bounds) {
        this.mBounds = bounds;
        return this;
    }

    /**
     * Gets the drawable of this model.
     * @return the drawable of this model.
     */
    public Drawable getDrawable() {
        return mDrawable;
    }

    /**
     * Sets the drawable to be used for this model.
     * @param drawable the drawable to use.
     * @return an instance of this class.
     */
    public SectionModel setDrawable(final Drawable drawable) {
        this.mDrawable = drawable;
        return this;
    }

    /**
     * Gets the color of this model.
     * @return the color of this model.
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Sets the color of this model.
     * @param color the color to use for this model.
     * @return an instance of this class.
     */
    public SectionModel setColor(final int color) {
        this.mColor = color;
        return this;
    }

    /**
     * A class that defines upper and lower bounds.
     */
    public static final class Bounds {
        private final double mLower;
        private final double mUpper;

        /**
         * Constructs a new instance using the lower and upper values to define the bounds.
         * @param lower
         *         a lower bound.
         * @param upper
         *         an upper bound.
         */
        public Bounds(final double lower, final double upper) {
            mLower = lower;
            mUpper = upper;
        }

        /**
         * Gets the lower boundary.
         * @return the lower boundary.
         */
        public double getLower() {
            return mLower;
        }

        /**
         * Gets the upper boundary.
         * @return the upper boundary.
         */
        public double getUpper() {
            return mUpper;
        }
    }
}
