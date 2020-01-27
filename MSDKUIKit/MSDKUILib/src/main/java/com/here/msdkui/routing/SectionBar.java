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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

/**
 * A view that shows a section bar indicating the length of a route. A scale factor can be set to adjust the width.
 * This view is one of the available sections that is shown in a {@link RouteDescriptionItem}.
 */
public final class SectionBar extends LinearLayout {

    private BarView mBar;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public SectionBar(final Context context) {
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
    public SectionBar(final Context context, final AttributeSet attrs) {
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
    public SectionBar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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
    public SectionBar(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mBar = createBar();
        addView(mBar);
    }

    /**
     * Binds a list of {@link SectionModel} elements to this view.
     * @param models the list of models to use.
     * @param scale the scale factor to use in order to adapt the width of this view.
     *              By default, the scale factor has a range from [0, 1], if not specified differently
     *              in the {@link SectionModel}. The default width of this view is calculated using a scale
     *              factor of 0, whereas 1 results in an unscaled width representing the longest route.
     */
    public void bind(final List<SectionModel> models, final float scale) {
        mBar.setModels(models);
        mBar.setScale(scale);
        mBar.invalidate();
    }

    private BarView createBar() {
        final BarView view = new BarView(getContext());
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        view.setPivotX(0f);
        return view;
    }

    /**
     * Bar view.
     */
    private static class BarView extends View {
        private Rect mBounds;
        private List<SectionModel> mModels;
        private float mScale;

        BarView(Context context) {
            super(context);
            mBounds = new Rect();
        }

        void setScale(final float scale) {
            mScale = scale;
        }

        void setModels(final List<SectionModel> models) {
            mModels = models;
        }

        @Override
        protected void onDraw(final Canvas canvas) {
            final List<SectionModel> models = mModels;

            if (models == null) {
                return;
            }

            int left;
            int right;
            mBounds.setEmpty();

            for (final SectionModel model : models) {
                left = (int) (model.getBounds().getLower() * getWidth() * mScale);
                right = (int) (model.getBounds().getUpper() * getWidth() * mScale);
                mBounds.set(left, 0, right, getHeight());
                model.getDrawable().setBounds(mBounds);
                model.getDrawable().draw(canvas);
            }
        }
    }
}
