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
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.here.msdkui.R;

/**
 * A view that shows an option item with one checkbox.
 */
public class BooleanOptionItem extends OptionItem implements CompoundButton.OnCheckedChangeListener {

    private TextView mLabelView;
    private Switch mValue;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public BooleanOptionItem(final Context context) {
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
    public BooleanOptionItem(final Context context, final AttributeSet attrs) {
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
    public BooleanOptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr) {
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
    public BooleanOptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater.from(context)
                .inflate(R.layout.boolean_option_item, this);
        mLabelView = (TextView) findViewById(R.id.boolean_item_label);
        mValue = (Switch) findViewById(R.id.boolean_item_value);
        mValue.setOnCheckedChangeListener(this);
    }

    /**
     * Gets the current state of the checkbox.
     * @return true if the checkbos is checked, false otherwise.
     */
    public boolean isChecked() {
        return mValue.isChecked();
    }

    /**
     * Sets the state of the checkbox.
     * @param checked true if the checkbox should be checked, false otherwise.
     */
    public void setChecked(final boolean checked) {
        mValue.setChecked(checked);
    }

    /**
     * Gets the label accompanying the checkbox.
     * @return the text that is shown beneath the checkbox.
     */
    public String getLabel() {
        return mLabelView.getText().toString();
    }

    /**
     * Sets the label accompanying the checkbox.
     * @param label the text that should be shown beneath the checkbox.
     * @return an instance of this class.
     */
    public BooleanOptionItem setLabel(final String label) {
        if (label == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_label_null));
        }
        mLabelView.setText(label);
        return this;
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        notifyChange();
    }
}
