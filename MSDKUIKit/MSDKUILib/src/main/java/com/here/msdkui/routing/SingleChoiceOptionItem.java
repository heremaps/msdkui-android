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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.here.msdkui.R;

import java.util.List;

/**
 * A view that shows an option item with a set of checkboxes. Only one checkbox can be selected at a time.
 */
public class SingleChoiceOptionItem extends OptionItem implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinner;
    private String mTitle;
    private List<String> mLabels;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public SingleChoiceOptionItem(final Context context) {
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
    public SingleChoiceOptionItem(final Context context, final AttributeSet attrs) {
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
    public SingleChoiceOptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr) {
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
     * Requires Lollipop (API Level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SingleChoiceOptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Gets the list of labels accompanying the checkboxes.
     *
     * @return the list of labels.
     */
    public List<String> getLabels() {
        return mLabels;
    }

    /**
     * Gets the title of this {@code SingleChoiceOptionItem}.
     *
     * @return a title of this item.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Sets the list of labels accompanying the checkboxes.
     *
     * @param title the title to show. Can be null, if not, title should be shown.
     * @param labels the list of labels.
     * @return an instance of this class.
     * @throws IllegalArgumentException if labels is null.
     */
    public SingleChoiceOptionItem setLabels(final String title, final List<String> labels) {

        if (labels == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_labels_null));
        }

        View.inflate(getContext(), R.layout.single_option_item, this);

        final TextView titleView = (TextView) findViewById(R.id.single_item_label);
        if (title == null) {
            titleView.setVisibility(GONE);
        } else {
            titleView.setText(title);
        }

        mTitle = title;
        mLabels = labels;

        mSpinner = (Spinner) findViewById(R.id.single_item_value);
        mSpinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_row_item, labels));
        mSpinner.setOnItemSelectedListener(this);
        return this;
    }

    /**
     * Selects the checkbox for the given label.
     * @param label the label to be selected. If null, nothing will be selected.
     */
    public void selectLabel(final String label) {
        if (label == null || mLabels == null || mSpinner == null) {
            return;
        }
        final int index = mLabels.indexOf(label);
        if (index != -1) {
            mSpinner.setSelection(index);
        }
    }

    /**
     * Selects the checkbox for the label at the given index.
     *
     * @param index the index of the label.
     */
    public void selectIndex(final int index) {
        if (mSpinner != null) {
            mSpinner.setSelection(index);
        }
    }

    /**
     * Gets the label of the selected checkbox.
     *
     * @return the selected item's label or null, if no labels have been set.
     */
    public String getSelectedItemLabel() {
        if (mLabels == null || mSpinner == null) {
            return null;
        }
        return mLabels.get(mSpinner.getSelectedItemPosition());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        notifyChange();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
