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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.here.msdkui.R;

/**
 * A custom {@link RecyclerView} to simplify handling clicks on list items.
 */
public class CustomRecyclerView extends RecyclerView {

    private RouteDescriptionList.OnItemClickedListener mOnItemClickedListener;

    private final RouteDescriptionList.RecyclerItemClickListener mRecyclerItemClickListener = new RouteDescriptionList
            .RecyclerItemClickListener(getContext(), this, new RouteDescriptionList.OnItemClickedListener() {
        @Override
        public void onItemClicked(final int index, final View item) {
            onItemClickedOuter(index, item);
        }

        @Override
        public void onItemLongClicked(final int index, final View item) {
            onItemLongClickedOuter(index, item);
        }
    });

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public CustomRecyclerView(final Context context) {
        super(context);
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
     */
    public CustomRecyclerView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
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
     * @param defStyle
     *         a default style attribute.
     */
    public CustomRecyclerView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context context) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        setLayoutManager(layoutManager);
        setHasFixedSize(true);
        setItemAnimator(new DefaultItemAnimator());
        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getVerticalDivider(context));
        addItemDecoration(dividerItemDecoration);
        addOnItemTouchListener(mRecyclerItemClickListener);
    }

    private Drawable getVerticalDivider(final Context context) {
        final Drawable verticalDivider = ContextCompat.getDrawable(context, R.drawable.list_item_divider);
        final int dividerColor = getListDividerColor(context);
        if (verticalDivider != null) {
            verticalDivider.setColorFilter(dividerColor, PorterDuff.Mode.SRC_IN);
        }
        return verticalDivider;
    }

    @ColorInt
    private int getListDividerColor(final Context context) {
        final TypedValue typedValue = new TypedValue();
        final Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorDivider, typedValue, true);
        return typedValue.data;
    }

    /**
     * Sets {@link OnItemClickedListener} to get notifed when a list item was cicked or long pressed.
     *
     * @param listener
     *         {@link OnItemClickedListener}
     */
    public void setOnItemClickedListener(final OnItemClickedListener listener) {
        mOnItemClickedListener = listener;
    }

    void onItemClickedOuter(final int index, final View item) {
        if (mOnItemClickedListener != null) {
            mOnItemClickedListener.onItemClicked(index, item);
        }
    }

    void onItemLongClickedOuter(final int index, final View item) {
        if (mOnItemClickedListener != null) {
            mOnItemClickedListener.onItemLongClicked(index, item);
        }
    }

    /**
     * An interface to notify when a list item was clicked or long clicked.
     */
    public interface OnItemClickedListener {

        /**
         * Called when a user clicks on an item of this list.
         *
         * @param index
         *         index of row in list.
         * @param item
         *         selected item.
         */
        void onItemClicked(int index, View item);

        /**
         * Called when a user long clicks on an item of this list.
         *
         * @param index
         *         index of row in list.
         * @param item
         *         selected item.
         */
        void onItemLongClicked(int index, View item);
    }

    /**
     * Implementation of a {@link androidx.recyclerview.widget.RecyclerView.OnItemTouchListener}.
     */
    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private final OnItemClickedListener mListener;
        private final GestureDetector mGestureDetector;

        public RecyclerItemClickListener(final Context context, final RecyclerView recyclerView,
                final OnItemClickedListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(final MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(final MotionEvent e) {
                    final View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onItemLongClicked(recyclerView.getChildAdapterPosition(child), child);
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(final RecyclerView view, final MotionEvent e) {
            final View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClicked(view.getChildAdapterPosition(childView), childView);
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(final RecyclerView view, final MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(final boolean disallowIntercept) {
        }
    }
}
