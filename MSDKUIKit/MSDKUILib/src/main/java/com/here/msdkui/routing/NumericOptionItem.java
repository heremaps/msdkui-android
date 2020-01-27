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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.here.msdkui.R;

import java.text.NumberFormat;
import java.text.ParseException;


/**
 * A view that shows an option item to set a numeric value.
 */
public class NumericOptionItem extends OptionItem {

    private TextView mLabelView;
    private TextView mValueView;
    private Number mOutput;
    private int mInputType = InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public NumericOptionItem(final Context context) {
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
    public NumericOptionItem(final Context context, final AttributeSet attrs) {
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
    public NumericOptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr) {
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
    public NumericOptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.numeric_option_item, this);
        mLabelView = (TextView) findViewById(R.id.numeric_item_label);
        mValueView = (TextView) findViewById(R.id.numeric_item_value);
        mValueView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                showDialog();
            }
        });
    }

    /**
     * Gets the value associated with the item.
     * @return a {@link Number} or null if no value was set.
     */
    public Number getValue() {
        return mOutput;
    }

    /**
     * Sets the {@link Number} value for the item.
     * @param value the value to set.
     * @return an instance of this class.
     */
    public NumericOptionItem setValue(final Number value) {
        mOutput = value;
        if (value == null || Float.isNaN(value.floatValue())) {
            mValueView.setText(getContext().getString(R.string.msdkui_undefined));
        } else {
            mValueView.setText(String.valueOf(value));
        }
        notifyChange();
        return this;
    }

    /**
     * Gets the label of the item.
     * @return the label as string.
     */
    public String getLabel() {
        return mLabelView.getText().toString();
    }

    /**
     * Sets the label id for the item.
     * @param labelId the id to set.
     * @return an instance of the class.
     * @throws IllegalArgumentException if labelId is null.
     */
    public NumericOptionItem setLabel(final String labelId) {
        if (labelId == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_label_null));
        }
        mLabelView.setText(labelId);
        return this;
    }

    /**
     * Shows an alert dialog to confirm the value that was set.
     */
    protected void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getLabel());
        final View editTextView = LayoutInflater.from(getContext()).inflate(R.layout.numeric_option_item_edittext, this, false);
        builder.setView(editTextView);
        final EditText inputText = (EditText) editTextView.findViewById(R.id.numeric_item_value_text);
        inputText.setInputType(mInputType);
        builder.setPositiveButton(getContext().getText(R.string.msdkui_ok), new PositiveDialogOnClickListener(inputText));
        builder.setNegativeButton(getContext().getText(R.string.msdkui_cancel), new NegativeDialogOnClickListener());
        builder.show();
    }

    /**
     * Sets input type for the item.
     * @param inputType the type to set, possible values: {@link android.text.InputType}.
     * @return an instance of the class.
     */
    public NumericOptionItem setInputType(int inputType) {
        mInputType = inputType;
        return this;
    }

    /**
     * A listener that is used to notify on positive button clicks.
     */
    private class PositiveDialogOnClickListener implements DialogInterface.OnClickListener {
        private final EditText mInputText;

        /**
         * Constructs a new instance.
         * @param editText an {@link EditText} containing the numeric value for the item.
         */
        PositiveDialogOnClickListener(@NonNull EditText editText) {
            mInputText = editText;
        }

        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            try {
                final String value = mInputText.getText().toString();
                NumericOptionItem.this.setValue(NumberFormat.getInstance().parse(value));
            } catch (ParseException e) {
                setValue(null);
            }
        }

    }

    /**
     * A listener that is used to notify on negative button clicks.
     */
    private static class NegativeDialogOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            dialog.cancel();
        }
    }
}
