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
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.msdkui.R;
import com.here.msdkui.common.ThemeUtil;

import java.util.Collections;
import java.util.List;

/**
 * An adapter class that acts as a bridge between a {@link WaypointEntry} list and the {@link WaypointsListViewHolder}
 * views that will be shown in a {@link WaypointList}.
 */
public class WaypointListAdapter extends RecyclerView.Adapter<WaypointListAdapter.WaypointsListViewHolder> {
    @NonNull
    private final List<WaypointEntry> mWaypointEntryList;
    @NonNull
    private final ItemTouchHelper mItemTouchHelper;
    private WaypointList.Listener mListener;
    private RecyclerView mRecyclerView;

    /**
     * Constructs a new instance using a list of {@link WaypointEntry} elements.
     *
     * @param entries
     *         a list of {@link WaypointEntry} objects.
     */
    public WaypointListAdapter(@NonNull final List<WaypointEntry> entries) {
        super();
        mWaypointEntryList = entries;
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(entries));
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mWaypointEntryList.size();
    }

    @Override
    public WaypointsListViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new WaypointsListViewHolder(getRowView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final WaypointsListViewHolder holder, final int position) {

        final WaypointEntry entry = mWaypointEntryList.get(position);

        // set waypoint entry.
        if (entry != null && holder.itemView instanceof WaypointItem) {
            ((WaypointItem) holder.itemView).setWaypointEntry(entry);
        }

        // set entry click listener.
        holder.getEntryView().setOnClickListener(v -> {
            final int currentPos = holder.getAdapterPosition();
            notifyEntryClicked(currentPos, mWaypointEntryList.get(currentPos));
        });

        final DraggableImageView draggableView = (DraggableImageView) holder.getDraggableView();
        if (entry != null && entry.isDraggable()) {
            draggableView.setVisibility(View.VISIBLE);
            draggableView.setContentDescription(String.format(draggableView.getContext().
                    getString(R.string.msdkui_drag_waypoint_button), position + 1));
            // set touch listener for drag.
            draggableView.setOnTouchListener(new DraggableTouchListener(holder, mItemTouchHelper));
        } else {
            holder.getDraggableView().setVisibility(View.GONE);
        }

        final View removableView = holder.getRemovableView();
        if (entry != null && entry.isRemovable()) {
            removableView.setVisibility(View.VISIBLE);
            removableView.setContentDescription(String.format(removableView.getContext().
                    getString(R.string.msdkui_remove_waypoint_button), position + 1));
            // set listener for removal.
            removableView.setOnClickListener(v -> {
                final WaypointEntry removedEntry = mWaypointEntryList.remove(holder.getAdapterPosition());
                notifyEntryRemoved(holder.getAdapterPosition(), removedEntry);
                notifyItemRemoved(holder.getAdapterPosition());
            });
        } else {
            holder.getRemovableView().setVisibility(View.GONE);
        }

        final TextView entryView = holder.getEntryView();
        if (entry != null) {
            entryView.setTextColor(ThemeUtil.getColor(entryView.getContext(),
                    entry.isValid() ? R.attr.colorForegroundLight : R.attr.colorHintLight));
        }

        updateViewHolderAccordingToPosition(position, holder);
    }

    void notifyEntryAdded(final int index, final WaypointEntry entry) {
        updateEntry();
        if (mListener != null) {
            mListener.onEntryAdded(index, entry);
        }
    }

    void notifyEntryUpdated(final int index, final WaypointEntry entry) {
        updateEntry();
        if (mListener != null) {
            mListener.onEntryUpdated(index, entry);
        }
    }

    /**
     * Method to notify entry removed.
     */
    void notifyEntryRemoved(final int index, final WaypointEntry entry) {
        updateEntry();
        if (mListener != null) {
            mListener.onEntryRemoved(index, entry);
        }
    }

    /**
     * Method to notify entry clicked.
     */
    private void notifyEntryClicked(final int index, final WaypointEntry entry) {
        if (mListener != null) {
            mListener.onEntryClicked(index, entry);
        }
    }

    /**
     * Method to notify entry drag.
     */
    void notifyEntryDragged(final int fromIdx, final int toIdx) {
        if (mListener != null) {
            mListener.onEntryDragged(fromIdx, toIdx);
        }
    }

    void updateEntry() {
        if (mRecyclerView == null) {
            return;
        }
        final int minCount = ((WaypointList) mRecyclerView).getMinWaypointItems();
        for (final WaypointEntry item : mWaypointEntryList) {
            item.setRemovable(minCount < mWaypointEntryList.size());
        }
        notifyDataSetChanged();
    }

    /**
     * Gets view for generating row in list.
     */
    private View getRowView(final Context context) {
        return new WaypointItem(context);
    }

    /**
     * Sets a listener to get notified on user interactions done on the {@link WaypointList}.
     * @param listener the listener to set.
     */
    public void setListener(final WaypointList.Listener listener) {
        mListener = listener;
    }

    /**
     * Control from / to prefix for not valid waypoint entries.
     */
    void updateViewHolderAccordingToPosition(final int position, final WaypointsListViewHolder holder) {
        final WaypointEntry waypointEntry = mWaypointEntryList.get(position);
        final TextView entryView = holder.getEntryView();
        String formatString = null;
        if (position == 0) {
            formatString = entryView.getContext().getString(R.string.msdkui_rp_from);
        } else if (position == mWaypointEntryList.size() - 1) {
            formatString = entryView.getContext().getString(R.string.msdkui_rp_to);
        }

        if (waypointEntry == null || waypointEntry.isValid()) {
            if (formatString == null) {
                entryView.setContentDescription(entryView.getText());
            } else {
                entryView.setContentDescription(String.format(formatString, entryView.getText()));
            }
            return;
        }
        final String waypointEntryName = waypointEntry.getLabel(entryView.getContext(), "");
        if (formatString == null) {
            entryView.setText(waypointEntryName);
        } else {
            entryView.setText(String.format(formatString, waypointEntryName));
        }
    }

    /**
     * ItemTouchHelper callback
     */
    private final class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private final List<WaypointEntry> mWaypointEntries;
        private int mDragFrom = -1;
        private int mDragTo = -1;

        ItemTouchHelperCallback(final List<WaypointEntry> entries) {
            super();
            mWaypointEntries = entries;
        }

        @Override
        public int getMovementFlags(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
            final WaypointEntry entry = mWaypointEntries.get(viewHolder.getAdapterPosition());
            final int dragFlags = entry.isDraggable() ?
                    ItemTouchHelper.UP | ItemTouchHelper.DOWN :
                    ItemTouchHelper.ACTION_STATE_IDLE;
            return makeMovementFlags(dragFlags, ItemTouchHelper.ACTION_STATE_IDLE);
        }

        @Override
        public boolean onMove(final RecyclerView recyclerView, final RecyclerView.ViewHolder startViewHolder,
                final RecyclerView
                        .ViewHolder endViewHolder) {

            if (startViewHolder.getItemViewType() != endViewHolder.getItemViewType()) {
                return false;
            }

            final int endPosition = endViewHolder.getAdapterPosition();

            if (!mWaypointEntries.get(endPosition).isDraggable()) {
                return false;
            }

            final int startPosition = startViewHolder.getAdapterPosition();

            if (mDragFrom == -1) {
                mDragFrom = startPosition;
            }
            mDragTo = endPosition;

            // update content description
            ((WaypointsListViewHolder) startViewHolder).swapContentDescriptions(
                    (WaypointsListViewHolder) endViewHolder);

            // move items.
            moveElements(startPosition, endPosition);

            updateViewHolderAccordingToPosition(startPosition, (WaypointsListViewHolder) endViewHolder);
            updateViewHolderAccordingToPosition(endPosition, (WaypointsListViewHolder) startViewHolder);

            // notify moved to adapter.
            notifyItemMoved(startPosition, endPosition);

            return true;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
            // do nothing.
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            if (mDragFrom != -1 && mDragTo != -1 && mDragFrom != mDragTo) {
                notifyEntryDragged(mDragFrom, mDragTo);
            }
            mDragTo = -1;
            mDragFrom = -1;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        private void moveElements(final int startPosition, final int endPosition) {
            int start = startPosition;
            int end = endPosition;
            if (startPosition > endPosition) {
                start = endPosition;
                end = startPosition;
            }
            Collections.rotate(mWaypointEntries.subList(start, end + 1), -1);
        }
    }

    /**
     * The view holder used for this adapter.
     */
    public class WaypointsListViewHolder extends RecyclerView.ViewHolder {

        // Draggable view.
        private final DraggableImageView mDraggableView;

        // Removable view.
        private final ImageView mRemovableView;

        // Entry  view.
        private final TextView mEntryView;

        /**
         * View holder constructor
         */
        WaypointsListViewHolder(final View view) {
            super(view);
            mDraggableView = view.findViewById(R.id.drag_icon);
            mRemovableView = view.findViewById(R.id.remove_icon);
            mEntryView = view.findViewById(R.id.waypoint_label);
        }

        /**
         * Gets draggable view associated with this view holder.
         */
        ImageView getDraggableView() {
            return mDraggableView;
        }

        /**
         * Gets removable view associated with this view holder.
         */
        ImageView getRemovableView() {
            return mRemovableView;
        }

        /**
         * Gets entry view associated with this view holder.
         */
        TextView getEntryView() {
            return mEntryView;
        }

        /**
         * Swap the content descriptions.
         */
        void swapContentDescriptions(WaypointsListViewHolder endViewHolder) {
            final View startDragView = getDraggableView();
            final View endDragView = endViewHolder.getDraggableView();
            final View startRemoveView = getRemovableView();
            final View endRemoveView = endViewHolder.getRemovableView();
            final CharSequence startDragViewContentDescription = startDragView.getContentDescription();
            final CharSequence startRemoveViewContentDescription = startRemoveView.getContentDescription();
            // start swapping
            startDragView.setContentDescription(endDragView.getContentDescription());
            startRemoveView.setContentDescription(endRemoveView.getContentDescription());
            endDragView.setContentDescription(startDragViewContentDescription);
            endRemoveView.setContentDescription(startRemoveViewContentDescription);
        }
    }

    /**
     * Touch listener for draggable view.
     */
    private static class DraggableTouchListener implements View.OnTouchListener {

        // View holder associated with this touch listener.
        private final WaypointsListViewHolder mHolder;

        private final ItemTouchHelper mHelper;

        // Variable to identify click event for accessibility service.
        private boolean mDownTouch;

        /**
         * Create {@code DraggableTouchListener} with view holder.
         */
        DraggableTouchListener(@NonNull final WaypointsListViewHolder holder,
                @NonNull final ItemTouchHelper helper) {
            mHolder = holder;
            mHelper = helper;
        }

        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            // listening for the down and up touch events for ClickableViewAccessibility.
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownTouch = true;
                    mHelper.startDrag(mHolder);
                    return true;
                case MotionEvent.ACTION_UP:
                    if (mDownTouch) {
                        mDownTouch = false;
                        mHolder.getDraggableView().performClick(); // enable accessibility services
                        // to perform this action for a user who cannot click the touchscreen.
                        return true;
                    }
                default:
                    return false;
            }
        }
    }
}
