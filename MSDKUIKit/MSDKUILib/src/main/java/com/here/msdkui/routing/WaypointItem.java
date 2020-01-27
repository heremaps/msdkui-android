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

import com.here.android.mpa.common.GeoCoordinate;
import com.here.msdkui.R;
import com.here.msdkui.common.ThemeUtil;

import java.util.Locale;


/**
 * A view that shows a {@link WaypointEntry} as a row of a {@link WaypointList}.
 * Note: Call {@link WaypointItem#setWaypointEntry(WaypointEntry)} to make this view visible.
 */
public class WaypointItem extends RelativeLayout {

    private ImageView mRemoveIcon;
    private DraggableImageView mDragIcon;
    private boolean mDragEnabled = true;
    private boolean mRemoveEnabled = true;
    private WaypointEntry mEntry;
    private WaypointItem.Listener mListener;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public WaypointItem(final Context context) {
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
    public WaypointItem(final Context context, final AttributeSet attrs) {
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
    public WaypointItem(final Context context, final AttributeSet attrs, final int defStyleAttr) {
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
    public WaypointItem(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.waypoint_item, this);
        uiInit();
        attrsInit(attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Initialization of the UI component.
     */
    private void uiInit() {
        mRemoveIcon = findViewById(R.id.remove_icon);
        mRemoveIcon.setOnClickListener(v -> {
            if (mListener != null && mRemoveEnabled) {
                mListener.onRemoveClicked(mEntry);
            }
        });

        mDragIcon = findViewById(R.id.drag_icon);
    }

    /**
     * Initialization of attributes.
     */
    private void attrsInit(final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        if (attrs == null) {
            return;
        }
        final TypedArray typedArray = this.getContext()
                .obtainStyledAttributes(attrs, R.styleable.WaypointItem, defStyleAttr, defStyleRes);
        final int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; ++i) {
            final int attr = typedArray.getIndex(i);
            if (attr == R.styleable.WaypointItem_removingEnabled) {
                setRemoveEnabled(typedArray.getBoolean(attr, false));
            } else if (attr == R.styleable.WaypointItem_draggingEnabled) {
                setDragEnabled(typedArray.getBoolean(attr, false));
            } else if (attr == R.styleable.WaypointItem_removingEnabledIcon || attr == R.styleable.WaypointItem_draggingEnabledIcon) {
                setIcon(typedArray, attr);
            }
        }
        typedArray.recycle();
    }

    /**
     * Set icon based on attr file
     */
    private void setIcon(final TypedArray typedArray, final int attr) {
        final Drawable drawable = ThemeUtil.getDrawable(this.getContext(), typedArray, attr);
        if (drawable == null) {
            return;
        }
        if (attr == R.styleable.WaypointItem_removingEnabledIcon) {
            setRemoveDrawable(drawable);
        } else if (attr == R.styleable.WaypointItem_draggingEnabledIcon) {
            setDragDrawable(drawable);
        }
    }

    /**
     * Gets the drawable that shows the "remove"-icon beneath the item.
     *
     * @return the {@link Drawable} that shows the "remove"-icon.
     */
    public Drawable getRemoveDrawable() {
        return mRemoveIcon.getBackground();
    }

    /**
     * Sets the drawable to be used for the "remove"-icon.
     *
     * @param removeIcon the {@link Drawable} to be used.
     */
    public void setRemoveDrawable(final Drawable removeIcon) {
        mRemoveIcon.setBackground(removeIcon);
    }

    /**
     * Gets the drawable that shows the "drag"-icon beneath the item.
     *
     * @return the {@link Drawable} that shows the "drag"-icon.
     */
    public Drawable getDragDrawable() {
        return mDragIcon.getBackground();
    }

    /**
     * Sets drawable to be used for the "drag"-icon.
     *
     * @param dragIcon the {@link Drawable} to be used.
     */
    public void setDragDrawable(final Drawable dragIcon) {
        mDragIcon.setBackground(dragIcon);
    }

    /**
     * Gets the {@link WaypointEntry} associated with this item.
     *
     * @return the {@link WaypointEntry} associated.
     */
    public WaypointEntry getWaypointEntry() {
        return mEntry;
    }

    /**
     * Sets the {@link WaypointEntry} to be associated with this instance.
     *
     * @param entry the {@link WaypointEntry} to set.
     */
    public void setWaypointEntry(final WaypointEntry entry) {
        if (entry == null) {
            return;
        }

        final TextView view = findViewById(R.id.waypoint_label);
        if (!entry.isValid()) {
            view.setText(entry.getLabel(getContext(), ""));
            return;
        }
        mEntry = entry;
        final GeoCoordinate cord = entry.getRouteWaypoint()
                .getOriginalPosition();
        final String position = String.format(Locale.ENGLISH, "%.5f", cord.getLatitude()) + "," + String.format(Locale.ENGLISH, "%.5f",
                cord.getLongitude());           // this is only for display, underlying data is
        // still same.
        view.setText(entry.getLabel(getContext(), position));
    }

    /**
     * Indicates whether this item is draggable.
     *
     * @return true if draggable, false otherwise.
     */
    public boolean isDragEnabled() {
        return mDragEnabled;
    }

    /**
     * Sets this instance to be draggable when used as element of a {@link WaypointList}.
     * The "drag"-icon will only be visible when this is set to true.
     *
     * @param dragEnabled true if item should be draggable, false otherwise.
     */
    public void setDragEnabled(final boolean dragEnabled) {
        mDragEnabled = dragEnabled;
        mDragIcon.setVisibility(mDragEnabled ? VISIBLE : GONE);
    }

    /**
     * Return whether this instance is removable.
     *
     * @return true if removable, false otherwise.
     */
    public boolean isRemoveEnabled() {
        return mRemoveEnabled;
    }

    /**
     * Sets this instance to be removable when used as an element of a {@link WaypointList}.
     * The "remove"-icon will only be visible when this is set to true.
     *
     * @param removeEnabled true if item should be removable, false otherwise.
     */
    public void setRemoveEnabled(final boolean removeEnabled) {
        mRemoveEnabled = removeEnabled;
        mRemoveIcon.setVisibility(mRemoveEnabled ? VISIBLE : GONE);
    }

    /**
     * Sets the {@link WaypointItem.Listener} to be notified when the item was removed or dragged.
     *
     * @param listener the {@link WaypointItem.Listener} to set.
     */
    public void setListener(final WaypointItem.Listener listener) {
        mListener = listener;
    }

    /**
     * A listener to notify when the item was removed or dragged.
     */
    public interface Listener {

        /**
         * Called when the item was removed from the list by the user.
         *
         * @param entry the {@link WaypointEntry} that was removed.
         */
        void onRemoveClicked(WaypointEntry entry);
    }
}
