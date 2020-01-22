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
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.here.msdkui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A base class for all views that show multiple instances of {@link OptionItem}.
 */
public abstract class OptionsPanel extends LinearLayout {

    private LinearLayout mContentView;
    private OptionsPanel.Listener mListener;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public OptionsPanel(final Context context) {
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
    public OptionsPanel(final Context context, final AttributeSet attrs) {
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
    public OptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr) {
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
    public OptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        setOrientation(VERTICAL);
        mContentView = new LinearLayout(context);
        mContentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .WRAP_CONTENT));
        mContentView.setOrientation(LinearLayout.VERTICAL);
        addView(mContentView);
        setBackgroundColor(Color.WHITE);
    }

    /**
     * Sets a list of option items. Each {@link OptionItem} will be added to this panel.
     * @deprecated Please use {@link #setOptionItems(List)} instead.
     * @param optionsSpecs the list of option items.
     */
    public void setOptionsSpecs(final List<OptionItem> optionsSpecs) {
        mContentView.removeAllViews();
        for (final OptionItem item : optionsSpecs) {
            mContentView.addView(item);
        }
        notifyOnOptionCreated(optionsSpecs);
    }

    /**
     * Sets a list of option items. Each {@link OptionItem} will be added to this panel.
     * @param options the list of option items.
     */
    public void setOptionItems(final List<OptionItem> options) {
        mContentView.removeAllViews();
        for (final OptionItem item : options) {
            mContentView.addView(item);
        }
        notifyOnOptionCreated(options);
    }

    /**
     * Sets a list of option items. Each {@link OptionItem} will be added to this panel.
     * @deprecated Please use {@link #setOptionItems(OptionItem, List)} instead.
     * @param parentItem the first item that should be added to this panel.
     * @param optionsSpecs the list of option items.
     */
    public void setOptionsSpecs(OptionItem parentItem, final List<OptionItem> optionsSpecs) {
        mContentView.removeAllViews();
        mContentView.addView(parentItem);
        for (final OptionItem item : optionsSpecs) {
            mContentView.addView(item);
            final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) item.getLayoutParams();
            final int margin = (int) getResources().getDimension(R.dimen.contentMarginHuge);
            lp.setMargins(margin, lp.topMargin, lp.rightMargin, lp.bottomMargin);
        }
        notifyOnOptionCreated(optionsSpecs);
    }

    /**
     * Sets a list of option items. Each {@link OptionItem} will be added to this panel.
     * @param parentItem the first item that should be added to this panel.
     * @param options the list of option items.
     */
    public void setOptionItems(OptionItem parentItem, final List<OptionItem> options) {
        mContentView.removeAllViews();
        mContentView.addView(parentItem);
        for (final OptionItem item : options) {
            mContentView.addView(item);
            final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) item.getLayoutParams();
            final int margin = (int) getResources().getDimension(R.dimen.contentMarginHuge);
            lp.setMargins(margin, lp.topMargin, lp.rightMargin, lp.bottomMargin);
        }
        notifyOnOptionCreated(options);
    }

    /**
     * Gets a list of option items.
     * @deprecated Please use {@link #getOptionItems()} instead.
     * @return a list of all children of this panel.
     */
    public List<OptionItem> getOptionsSpecs() {
        final List<OptionItem> itemList = new ArrayList<>();
        final int childCount = mContentView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            itemList.add((OptionItem) mContentView.getChildAt(i));
        }
        return itemList;
    }

    /**
     * Gets a list of option items.
     * @return a list of all children of this panel.
     */
    public List<OptionItem> getOptionItems() {
        final List<OptionItem> itemList = new ArrayList<>();
        final int childCount = mContentView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            itemList.add((OptionItem) mContentView.getChildAt(i));
        }
        return itemList;
    }

    /**
     * Set a listener to get the associated events for this panel.
     * @param listener the listener to set.
     */
    public void setListener(final OptionsPanel.Listener listener) {
        mListener = listener;
    }

    /**
     * Notifies listeners when a new option item has been created.
     * @param item the item that was created.
     */
    protected void notifyOnOptionCreated(final List<OptionItem> item) {
        if (mListener != null) {
            mListener.onOptionCreated(item);
        }
    }

    /**
     * Notifies listeners when an option item has been changed.
     * @param item the item that was changed.
     */
    protected void notifyOnOptionChanged(final OptionItem item) {
        if (mListener != null) {
            mListener.onOptionChanged(item);
        }
    }

    /**
     * Utility method to get a string from the context this view is running in.
     * @param id the resource id of the string.
     * @return the string.
     */
    public String getString(int id) {
        return getContext().getString(id);
    }

    /**
     * A listener to notfiy when an {@link OptionItem} was created or changed.
     */
    public interface Listener {

        /**
         * Called when an option item is created.
         *
         * @param item the item that was created.
         */
        void onOptionCreated(List<OptionItem> item);

        /**
         * Called when an option item is changed.
         *
         * @param item the item that was changed.
         */
        void onOptionChanged(OptionItem item);
    }
}
