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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.here.msdkui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A view that displays an option item with a set of checkboxes. More than one checkbox can be selected at a time.
 */
public class MultipleChoiceOptionItem extends OptionItem implements CompoundButton.OnCheckedChangeListener {

    private final List<ItemRow> mItemList = new ArrayList<>();

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public MultipleChoiceOptionItem(final Context context) {
        super(context);
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
    public MultipleChoiceOptionItem(final Context context, final AttributeSet attrs) {
        super(context, attrs);
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
    public MultipleChoiceOptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr) {
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
    public MultipleChoiceOptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Gets the list of labels accompanying the checkboxes.
     *
     * @return a list of strings containing the label.
     */
    public List<String> getLabels() {
        final List<String> labelList = new ArrayList<>(mItemList.size());
        for (final ItemRow item : mItemList) {
            labelList.add(item.getLabelView().getText().toString());
        }
        return labelList;
    }

    /**
     * Sets the list of labels accompanying the checkboxes.
     *
     * @param labels the list of labels to set.
     * @return an instance of this class to allow chaining using the Builder pattern,
     * @see OptionItemBuilders.MultipleChoiceOptionItemBuilder
     * @throws IllegalArgumentException when labels is null.
     */
    public MultipleChoiceOptionItem setLabels(final List<String> labels) {
        if (labels == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_labels_null));
        }
        for (final String label : labels) {
            final View view = View.inflate(getContext(), R.layout.multiple_option_item, null);
            final TextView labelView = (TextView) view.findViewById(R.id.multiple_item_label);
            labelView.setText(label);
            final CheckBox selectionView = (CheckBox) view.findViewById(R.id.multiple_item_value);
            selectionView.setOnCheckedChangeListener(this);
            mItemList.add(getRow(labelView, selectionView));
            addView(view);
        }
        return this;
    }

    /**
     * Gets row of this option item.
     */
    private ItemRow getRow(final TextView labelView, final CheckBox selectionView) {
        final ItemRow item = new ItemRow();
        item.setLabelView(labelView).setSelectionView(selectionView);
        return item;
    }

    /**
     * Selects checkboxes based on the list of given labels and unselects others.
     * @param labels the list of labels to be selected.
     */
    public void selectLabels(final List<String> labels) {
        for (final ItemRow item : mItemList) {
            if (labels.contains(item.getLabelView().getText().toString())) {
                item.getSelectionView().setChecked(true);
            } else {
                item.getSelectionView().setChecked(false);
            }
        }
    }

    /**
     * Gets a list of labels, that are currently selected.
     *
     * @return a list of labels of the selected checkboxes.
     */
    public List<String> getSelectedLabels() {
        final List<String> indexList = new ArrayList<>();
        for (final ItemRow item : mItemList) {
            if (item.getSelectionView().isChecked()) {
                indexList.add(item.getLabelView().getText().toString());
            }
        }
        return indexList;
    }

    /**
     * Indicates if a checkbox is in checked or unchecked state.
     * @param id the id of the item.
     * @return true if the checkbox is selected, false otherwise.
     */
    public boolean isItemSelected(final String id) {
        for (final ItemRow item : mItemList) {
            if (item.getLabelView().getText().toString().equals(id)) {
                return item.getSelectionView().isChecked();
            }
        }
        return false;
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        notifyChange();
    }

    /**
     * A class that represents one row of a {@link MultipleChoiceOptionItem}.
     */
    public static class ItemRow {
        private TextView mLabelView;
        private CheckBox mSelectionView;

        /**
         * Gets the associated label {@link TextView}.
         * @return the {@link TextView} of the label.
         */
        public TextView getLabelView() {
            return mLabelView;
        }

        /**
         * Sets the associated label {@link TextView}.
         * @param label the {@link TextView} to set.
         * @return an instance of this class.
         */
        public ItemRow setLabelView(final TextView label) {
            this.mLabelView = label;
            return this;
        }

        /**
         * Gets the associated {@link CheckBox}.
         * @return the associated {@link CheckBox}.
         */
        public CheckBox getSelectionView() {
            return mSelectionView;
        }

        /**
         * Sets the associated {@link CheckBox}.
         * @param checkBox the {@link CheckBox} to set.
         * @return an instance of this class.
         */
        public ItemRow setSelectionView(final CheckBox checkBox) {
            this.mSelectionView = checkBox;
            return this;
        }
    }
}
