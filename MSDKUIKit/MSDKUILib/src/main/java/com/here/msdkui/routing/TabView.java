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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.here.msdkui.R;
import com.here.msdkui.common.ThemeUtil;

/**
 * A view that shows an icon and a label.
 */
public class TabView extends RelativeLayout {

    private ImageView mButtonIcon;
    private TextView mButtonLabel;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public TabView(final Context context) {
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
    public TabView(final Context context, final AttributeSet attrs) {
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
    public TabView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
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
     * Requires Lollipop (API Level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        LayoutInflater.from(context)
                .inflate(R.layout.tab_view, this);
        uiInit();
        attrsInit(attrs, defStyleAttr, defStyleRes);
    }

    /**
     * UI initialization.
     */
    private void uiInit() {
        mButtonIcon = (ImageView) findViewById(android.R.id.icon);
        mButtonLabel = (TextView) findViewById(android.R.id.text1);
    }

    /**
     * Attributes initialization.
     */
    private void attrsInit(final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        if (attrs != null) {
            final TypedArray typedArray = this.getContext()
                    .obtainStyledAttributes(attrs, R.styleable.TabView, defStyleAttr, defStyleRes);
            final int indexCount = typedArray.getIndexCount();
            for (int i = 0; i < indexCount; ++i) {
                final int attr = typedArray.getIndex(i);
                if (attr == R.styleable.TabView_icon) {
                    setIcon(typedArray, attr);
                } else if (attr == R.styleable.TabView_label) {
                    setLabel(typedArray, attr);
                }
            }
            typedArray.recycle();
        }
    }

    /**
     * Sets the label from {@link TypedArray}.
     */
    private void setLabel(final TypedArray typedArray, final int attr) {
        final String labelText = ThemeUtil.getString(typedArray, attr);
        if (labelText == null) {
            return;
        }
        mButtonLabel.setText(labelText);
    }

    /**
     * Sets the Icon from {@link TypedArray}.
     */
    private void setIcon(final TypedArray typedArray, final int attr) {
        final Drawable drawable = ThemeUtil.getDrawable(this.getContext(), typedArray, attr);
        if (drawable == null) {
            return;
        }
        if (attr == R.styleable.TabView_icon) {
            mButtonIcon.setImageDrawable(drawable);
        }
    }

    /**
     * Gets the icon associated with this view.
     * @return the drawable icon.
     */
    public Drawable getIcon() {
        return mButtonIcon.getDrawable();
    }

    /**
     * Sets the icon that should be associated with this view.
     * @param drawable the drawable icon.
     * @return an instance of this class.
     */
    public TabView setIcon(final Drawable drawable) {
        mButtonIcon.setImageDrawable(drawable);
        return this;
    }

    /**
     * Gets the label associated with this view..
     * @return the label displayed by this view.
     */
    public CharSequence getLabel() {
        return mButtonLabel.getText();
    }

    /**
     * Sets the label of this view.
     * @param label the label to use.
     * @return an instance of this class.
     */
    public TabView setLabel(final String label) {
        mButtonLabel.setText(label);
        return this;
    }
}
