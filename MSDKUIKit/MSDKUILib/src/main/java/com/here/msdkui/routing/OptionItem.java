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
import android.widget.LinearLayout;

/**
 * A base class for all views that show option items.
 */
public abstract class OptionItem extends LinearLayout {

    private ItemType mItemType;
    private OnChangedListener mOnChangedListener;
    private int mId;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public OptionItem(final Context context) {
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
    public OptionItem(final Context context, final AttributeSet attrs) {
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
    public OptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
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
    public OptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(VERTICAL);
    }

    /**
     * Gets the {@link ItemType} of the item.
     * @return the type of the item.
     */
    public ItemType getItemType() {
        return mItemType;
    }

    /**
     * Sets the {@link ItemType} of the option item.
     * @param itemType the type to set.
     */
    void setItemType(final ItemType itemType) {
        mItemType = itemType;
    }

    /**
     * Gets the item id.
     * @return the id of the item.
     */
    public int getItemId() {
        return mId;
    }

    /**
     * Sets the item id.
     * @param id the id to set.
     */
    public void setItemId(int id) {
        mId = id;
    }

    /**
     * Notifies the associated {@link OnChangedListener}.
     */
    public void notifyChange() {
        if (mOnChangedListener != null) {
            mOnChangedListener.onChanged(this);
        }
    }

    /**
     * Set a listener to get notified when the {@link OptionItem} was changed.
     * @param listener the listener to set.
     */
    public void setListener(final OnChangedListener listener) {
        mOnChangedListener = listener;
    }

    /**
     * All the available option item types.
     */
    public enum ItemType {

        /**
         * Boolean type option item.
         */
        BOOLEAN_OPTION_ITEM,

        /**
         * Single choice type option item.
         */
        SINGLE_CHOICE_OPTION_ITEM,

        /**
         * Multiple choice type option item.
         */
        MULTIPLE_CHOICE_OPTION_ITEM,

        /**
         * Number type option item.
         */
        NUMBER_OPTION_ITEM
    }

    /**
     * A listener interface to notify when the {@link OptionItem} was changed.
     */
    public interface OnChangedListener {
        /**
         * Called when the item was changed.
         *
         * @param item the changed {@link OptionItem}.
         */
        void onChanged(OptionItem item);
    }
}
