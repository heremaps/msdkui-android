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
import android.database.DataSetObserver;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.here.android.mpa.routing.RouteOptions;
import com.here.msdkui.R;

/**
 * A view that shows all supported elements of {@link com.here.android.mpa.routing.RouteOptions.TransportMode}
 * with or without content fragment using a {@link TabLayout}. Each tab shows a transport mode.
 * Only one transport mode can be selected at a time.
 * This view can be customized by calling {@link TransportModePanel#setAdapter(TransportModePanelAdapter)}
 * to set a custom adapter. By default, this component uses the {@link SimpleTransportModePanelAdapter}.
 */
public class TransportModePanel extends LinearLayout implements TabLayout.OnTabSelectedListener {

    private ViewPager mViewPager;
    private TransportModePanelAdapter mPanelAdapter;
    private TabLayout mTabLayout;
    private OnSelectedListener mOnSelectedListener;
    private DataSetObserver mDataSetObserver;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public TransportModePanel(final Context context) {
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
    public TransportModePanel(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Init content.
     */
    private void init(final Context context) {
        setOrientation(VERTICAL);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        LayoutInflater.from(context).inflate(R.layout.transport_mode_panel, this);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setVisibility(GONE);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.addOnTabSelectedListener(this);
        mDataSetObserver = new DataSetChangeObserver();
        setAdapter(new SimpleTransportModePanelAdapter(getContext()));
    }

    /**
     * Sets an adapter to use for this panel. This can be a custom adapter extending
     * {@link TransportModePanelAdapter}. By setting a new adapter, you will overwrite the default
     * {@link SimpleTransportModePanelAdapter}.
     *
     * @param panelAdapter the adapter to use for this panel.
     */
    public void setAdapter(final TransportModePanelAdapter panelAdapter) {
        if (mPanelAdapter != null && panelAdapter == null) {
            mPanelAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        if (panelAdapter != null) {
            panelAdapter.registerDataSetObserver(mDataSetObserver);
            mViewPager.setAdapter(panelAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        }
        if (panelAdapter != null && panelAdapter.isContentVisible()) {
            mViewPager.setVisibility(VISIBLE);
        } else {
            mViewPager.setVisibility(GONE);
        }
        mPanelAdapter = panelAdapter;
        populateView();
    }

    /**
     * Populates view from adapter.
     */
    private void populateView() {
        if (mPanelAdapter == null) {
            return;
        }
        updateTabs();
    }

    /**
     * Updates the {@link TabView} views.
     */
    void updateTabs() {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            final TabView btn = mPanelAdapter.getTabView(i);
            mTabLayout.getTabAt(i).setIcon(btn.getIcon()).setText(btn.getLabel()).setCustomView(btn);
        }
    }

    /**
     * Gets the {@link TabLayout} associated with this panel.
     * @return the associated {@link TabLayout}.
     */
    public TabLayout getTabView() {
        return mTabLayout;
    }

    /**
     * Gets the {@link ViewPager} associated with this panel.
     * @return the associated {@link ViewPager}.
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * Gets the number of tabs shown in this panel.
     * @return the number of tabs currently registered.
     */
    public int getTabCount() {
        return mTabLayout.getTabCount();
    }

    /**
     * Gets the position of the currently selected tab.
     * @return the position of the selected tab.
     */
    public int getSelected() {
        return mTabLayout.getSelectedTabPosition();
    }

    /**
     * Selects the tab at a given position.
     * @param position the position of the tab that should be selected.
     */
    public void setSelected(final int position) {
        final TabLayout.Tab tab = mTabLayout.getTabAt(position);
        if (tab != null) {
            tab.select();
        }
    }

    /**
     * Gets the selected {@link com.here.android.mpa.routing.RouteOptions.TransportMode}.
     * @return the selected {@link com.here.android.mpa.routing.RouteOptions.TransportMode}.
     */
    public RouteOptions.TransportMode getSelectedTransportMode() {
        return mPanelAdapter.getTransportModes()
                .get(mTabLayout.getSelectedTabPosition());
    }

    /**
     * Sets the {@link com.here.android.mpa.routing.RouteOptions.TransportMode}
     * that should be selected.
     * @param transportMode the {@link com.here.android.mpa.routing.RouteOptions.TransportMode}
     *                     to be selected.
     */
    public void setSelectedTransportMode(final RouteOptions.TransportMode transportMode) {
        final TabLayout.Tab tab = mTabLayout.getTabAt(mPanelAdapter.getTransportModes().indexOf(transportMode));
        if (tab != null) {
            tab.select();
        }
    }

    /**
     * Sets the {@link OnSelectedListener} to get events for tab selection, unselection or re-selection.
     *
     * @param listener
     *         the {@link OnSelectedListener} to set.
     */
    public void setOnSelectedListener(final OnSelectedListener listener) {
        if (listener == null && mOnSelectedListener != null) {
            mTabLayout.removeOnTabSelectedListener(this);
        }
        mOnSelectedListener = listener;
    }

    @Override
    public void onTabSelected(final TabLayout.Tab tab) {
        if (mOnSelectedListener != null) {
            mOnSelectedListener.onSelected(tab.getPosition(), (TabView) tab.getCustomView());
        }
    }

    @Override
    public void onTabUnselected(final TabLayout.Tab tab) {
        if (mOnSelectedListener != null) {
            mOnSelectedListener.onUnselected(tab.getPosition(), (TabView) tab.getCustomView());
        }
    }

    @Override
    public void onTabReselected(final TabLayout.Tab tab) {
        if (mOnSelectedListener != null) {
            mOnSelectedListener.onReselected(tab.getPosition(), (TabView) tab.getCustomView());
        }
    }

    /**
     * An interface for a callback to be invoked when a tab is selected, unselected or re-selected.
     */
    public interface OnSelectedListener {

        /**
         * Called when a tab is selected.
         *
         * @param index
         *         position of selected tab.
         * @param button
         *         The tab that was selected
         */
        void onSelected(int index, TabView button);

        /**
         * Called when a tab exits the selected state.
         *
         * @param index
         *         the position of the selected tab.
         * @param button
         *         the tab that was unselected.
         */
        default void onUnselected(int index, TabView button) {
        }

        /**
         * Called when a tab is re-selected.
         *
         * @param index
         *         position of selected tab.
         * @param button
         *         The tab that was selected.
         */
        default void onReselected(int index, TabView button) {
        }
    }

    /**
     * Data set change observer.
     */
    public class DataSetChangeObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            updateTabs();
        }

        @Override
        public void onInvalidated() {
            updateTabs();
        }
    }
}
