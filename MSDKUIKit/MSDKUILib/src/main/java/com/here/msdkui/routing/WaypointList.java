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
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.here.android.mpa.routing.RouteWaypoint;
import com.here.msdkui.R;
import com.here.msdkui.common.ThemeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A view that shows a list of {@link WaypointEntry} items. By default, the list contains two empty items.
 * Dragging and removing of items is supported via dedicated buttons. By default, removing items is only available when
 * the list contains more than two items.
 */
public class WaypointList extends RecyclerView {

    private final List<WaypointEntry> mWaypointEntries = new ArrayList<>();
    private WaypointListAdapter mAdapter;
    private int mMinWaypointCount = 2;
    private int mMaxWaypointCount = 16;
    private int mVisibleWaypointCount = 4;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public WaypointList(final Context context) {
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
    public WaypointList(final Context context, final AttributeSet attrs) {
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
    public WaypointList(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        setLayoutManager(layoutManager);
        setHasFixedSize(true);
        setItemAnimator(new DefaultItemAnimator());
        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                layoutManager.getOrientation());
        final Drawable verticalDivider = getVerticalDivider(context);
        dividerItemDecoration.setDrawable(verticalDivider);
        addItemDecoration(dividerItemDecoration);
        setBackgroundColor(ThemeUtil.getColor(getContext(), R.attr.colorBackgroundBrand));
        mAdapter = new WaypointListAdapter(mWaypointEntries);
        setAdapter(mAdapter);
        addMinWaypointsItem();
    }

    private void addMinWaypointsItem() {
        for (int i = mWaypointEntries.size(); i < mMinWaypointCount; i++) {
            addEmptyEntry();
        }
        mAdapter.notifyDataSetChanged();
    }

    private Drawable getVerticalDivider(final Context context) {
        final Drawable verticalDivider = ContextCompat.getDrawable(context, R.drawable.list_item_divider);
        final int dividerColor = getListDividerColor(context);
        verticalDivider.setColorFilter(dividerColor, PorterDuff.Mode.SRC_IN);
        return verticalDivider;
    }

    @ColorInt
    private int getListDividerColor(final Context context) {
        final TypedValue typedValue = new TypedValue();
        final Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorDividerLight, typedValue, true);
        return typedValue.data;
    }

    /**
     * Adds a new empty {@link WaypointEntry} at the end of this list.
     *
     * @return the added {@link WaypointEntry}.
     * @throws IllegalArgumentException if {@link #getMaxWaypointItems()} count is exceeded.
     */
    public final WaypointEntry addEmptyEntry() {
        final WaypointEntry entry = new WaypointEntry(R.string.msdkui_waypoint_select_location);
        addEntry(entry);
        return entry;
    }

    /**
     * Inserts a new empty {@link WaypointEntry} at the specified index of this list.
     *
     * @param index the index where the waypoint should be inserted.
     * @return the added {@link WaypointEntry}.
     * @throws IllegalArgumentException
     *         if one of the following criteria is met:
     *         <ul>
     *         <li>if {@code index < 0 || index > size()}.</li>
     *         <li>if {@link #getMaxWaypointItems()} count is exceeded.</li>
     *         </ul>
     * @see WaypointList#insertEntry(int, WaypointEntry)
     */
    public final WaypointEntry insertEmptyEntry(final int index) {
        final WaypointEntry entry = new WaypointEntry(R.string.msdkui_waypoint_select_location);
        insertEntry(index, entry);
        return entry;
    }

    /**
     * Adds a new {@link WaypointEntry} to the end of this list.
     *
     * @param entry the {@link WaypointEntry} to be added to this list.
     * @return true if entry was added successfully, false otherwise.
     * @throws IllegalArgumentException
     *         if one of the following criteria is met:
     *         <ul>
     *         <li>if entry is null.</li>
     *         <li>if {@link #getMaxWaypointItems()} count is exceeded.</li>
     *         </ul>
     */
    public final boolean addEntry(final WaypointEntry entry) {
        if (mWaypointEntries.size() == mMaxWaypointCount) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_waypoint_exceed_max));
        }
        if (entry == null) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_waypoint_entry_null));
        }
        boolean ret = false;
        ret = mWaypointEntries.add(entry);
        mAdapter.notifyEntryAdded(mWaypointEntries.size() - 1, entry);
        return ret;
    }

    /**
     * Inserts a new {@link WaypointEntry} at the specified index to this list.
     *
     * @param index
     *         the index where the new {@link WaypointEntry} should be added.
     * @param entry
     *         the {@link WaypointEntry} to be added to list.     *
     * @throws IllegalArgumentException
     *         if one of the following criteria is met:
     *         <ul>
     *         <li>if {@code index < 0 || index > size()}.</li>
     *         <li>if entry is null.</li>
     *         <li>if {@link #getMaxWaypointItems()} count is exceeded.</li>
     *         </ul>
     */
    public void insertEntry(final int index, final WaypointEntry entry) {
        if (index < 0 || index > mWaypointEntries.size()) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_out_of_index_range, 0,
                            mWaypointEntries.size()));
        }
        if (entry == null) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_waypoint_entry_null));
        }
        if (mWaypointEntries.size() == mMaxWaypointCount) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_waypoint_exceed_max));
        }
        mWaypointEntries.add(index, entry);
        mAdapter.notifyEntryAdded(index, entry);
    }

    /**
     * Updates an {@link WaypointEntry} at the specified index to this list.
     *
     * @param index
     *         the index of the {@link WaypointEntry} to be updated.
     * @param entry
     *         the {@link WaypointEntry} to be updated.
     * @throws IllegalArgumentException
     *         if {@code index < 0 || index > size()} or entry is null.
     */
    public void updateEntry(final int index, final WaypointEntry entry) {
        if (index < 0 || index > mWaypointEntries.size()) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_out_of_index_range, 0,
                            mWaypointEntries.size()));
        }
        if (entry == null) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_waypoint_entry_null));
        }
        mWaypointEntries.set(index, entry);
        mAdapter.notifyEntryUpdated(index, entry);
    }

    /**
     * Removes a {@link WaypointEntry} at the specified index to this list.
     *
     * @param index
     *         the index where the {@link WaypointEntry} should be removed.
     * @return the element that was removed from the list.
     * @throws IllegalArgumentException
     *         if one of the following criteria is met:
     *         <ul>
     *         <li>if {@code index < 0 || index > size()}.</li>
     *         <li>if {@link #getMaxWaypointItems()} count is exceeded.</li>
     *         </ul>
     */
    public WaypointEntry removeEntry(final int index) {
        if (index < 0 || index > mWaypointEntries.size()) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_out_of_index_range, 0,
                            mWaypointEntries.size()));
        }
        if (mMinWaypointCount == mWaypointEntries.size()) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_waypoint_fail_min));
        }
        final WaypointEntry removedEntry = mWaypointEntries.remove(index);
        mAdapter.notifyEntryRemoved(index, removedEntry);
        return removedEntry;
    }

    /**
     * Gets the minimum number of waypoints that this list should have.
     *
     * @return the minimum number of waypoints.
     */
    public int getMinWaypointItems() {
        return mMinWaypointCount;
    }

    /**
     * Sets the minimum number of waypoints that this list should have. Default is 2.
     *
     * @param count
     *         the number of minimum waypoints.
     * @throws IllegalArgumentException
     *         if count is less than 2 or greater than {@link #getMaxWaypointItems()}.
     */
    public void setMinWaypointItems(final int count) {
        if (count < 2 || count > mMaxWaypointCount) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_out_of_index_range, 2,
                            mMaxWaypointCount));
        }
        mMinWaypointCount = count;
        addMinWaypointsItem();
    }

    /**
     * The maximum number of waypoints that can be added to this list. Default is 16.
     *
     * @param count
     *         the maximum number of waypoints that can be added to this list.
     * @throws IllegalArgumentException
     *         if count is less than 2 or less than {@link #getMinWaypointItems()}.
     */
    public void setMaxWaypointItems(final int count) {
        if (count < 2 || count < mMinWaypointCount) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_out_of_index_range,
                            mMinWaypointCount, Integer.MAX_VALUE));
        }
        mMaxWaypointCount = count;
    }

    /**
     * Gets the maximum number of waypoints that can be added to this list.
     *
     * @return the maximum number of waypoints.
     */
    public int getMaxWaypointItems() {
        return mMaxWaypointCount;
    }

    /**
     * Sets the number of {@link WaypointItem} that should be visible without scrolling.
     * The visible area of the list will be expanded to match this number.
     * Items above that number will require scrolling.
     *
     * @param count
     *         the number of {@link WaypointItem} that should be visible without scrolling.
     * @throws IllegalArgumentException
     *         if count is less than {@link #getMinWaypointItems()} or greater than
     *         {@link #getMaxWaypointItems()}.
     */
    public void setVisibleNumberOfWaypointItems(final int count) {
        if (count < mMinWaypointCount || count > mMaxWaypointCount) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_out_of_index_range,
                            mMinWaypointCount, mMaxWaypointCount));
        }
        mVisibleWaypointCount = count;
    }

    /**
     * Gets the number of {@link WaypointItem} that should be visible without scrolling.
     *
     * @return the number of visible items.
     */
    public int getVisibleWaypointItems() {
        return mVisibleWaypointCount;
    }

    /**
     * Reset this list to its initial status. Warning: This will clear the list and add the
     * minimum number of empty waypoints.
     */
    public void reset() {
        mWaypointEntries.clear();
        addMinWaypointsItem();
    }

    /**
     * Reverse the order of the {@link WaypointEntry} elements.
     */
    public void reverse() {
        Collections.reverse(mWaypointEntries);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Gets the list of {@link WaypointEntry} elements contained in this list.
     *
     * @return the list of {@link WaypointEntry} elements.
     */
    public List<WaypointEntry> getEntries() {
        return mWaypointEntries;
    }

    /**
     * Sets a new list of {@link WaypointEntry} elements to this list. This will
     * override previous entries, if any.
     *
     * @param entries
     *         the list of {@link WaypointEntry} elements.
     * @throws IllegalArgumentException
     *         if given size of entries is less than {{@link #getMinWaypointItems()}} or
     *         more than {@link #getMaxWaypointItems()}.
     */
    public void setEntries(final List<WaypointEntry> entries) {
        if (entries.size() > mMaxWaypointCount) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_waypoint_exceed_max));
        }
        if (entries.size() < mMinWaypointCount) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_waypoint_failing_min));
        }
        mWaypointEntries.clear();
        mWaypointEntries.addAll(entries);
        mAdapter.updateEntry();
    }

    /**
     * Gets the list of {@link RouteWaypoint} elements contained in this list.
     *
     * @return the list of {@link RouteWaypoint} elements.
     */
    public List<RouteWaypoint> getRouteWaypoints() {
        final List<RouteWaypoint> waypointList = new ArrayList<>();
        for (final WaypointEntry entry : mWaypointEntries) {
            waypointList.add(entry.getRouteWaypoint());
        }
        return waypointList;
    }

    /**
     * Gets the number of {@link WaypointItem} elements contained in this list.
     *
     * @return the number of {@link WaypointItem} elements.
     */
    public int getEntriesCount() {
        return mWaypointEntries.size();
    }

    /**
     * Adds the listener to get notified on user interactions with this list.
     *
     * @param listener
     *         the {@link WaypointList.Listener} to get notified.
     */
    public void setListener(final WaypointList.Listener listener) {
        mAdapter.setListener(listener);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        final int count = mAdapter.getItemCount();
        final int oneItemHeight = computeVerticalScrollRange() / count;
        final int maxCount = count < mVisibleWaypointCount ? count : mVisibleWaypointCount;
        int newHeightSpec = heightSpec;
        if (oneItemHeight != 0) {
            newHeightSpec = MeasureSpec.makeMeasureSpec(oneItemHeight * maxCount, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, newHeightSpec);
    }

    /**
     * An interface for a listener to get notified on user interactions with this list.
     */
    public interface Listener {

        /**
         * Called when a user has clicked on a {@link WaypointEntry}.
         *
         * @param index
         *         the list index of the {@link WaypointEntry} that was clicked.
         * @param current
         *         the {@link WaypointEntry} that was clicked.
         */
        void onEntryClicked(int index, WaypointEntry current);

        /**
         * Called when the user has added a {@link WaypointEntry}.
         *
         * @param index
         *        the list index of the added {@link WaypointEntry}.
         * @param entry
         *         the added {@link WaypointEntry}.
         */
        void onEntryAdded(int index, WaypointEntry entry);

        /**
         * Called when the user has updated a {@link WaypointEntry}.
         *
         * @param index
         *         the list index of the updated {@link WaypointEntry}.
         * @param entry
         *         the updated {@link WaypointEntry}.
         */
        void onEntryUpdated(int index, WaypointEntry entry);

        /**
         * Called when the user has removed a {@link WaypointEntry}.
         *
         * @param index
         *         the list index of the removed {@link WaypointEntry}.
         * @param entry
         *         the removed {@link WaypointEntry}.
         */
        void onEntryRemoved(int index, WaypointEntry entry);

        /**
         * Called when the user has put one finger on the entry and moved it to another position.
         *
         * @param fromIdx
         *         the list index of the entry where the user has put one finger to start dragging.
         * @param toIdx
         *         the list index of the entry where the user has lifted the finger and stopped dragging.
         */
        void onEntryDragged(int fromIdx, int toIdx);
    }
}
