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
import androidx.fragment.app.Fragment;
import android.view.View;

import com.here.RobolectricTest;
import com.here.android.mpa.routing.RouteOptions;

import org.junit.Before;
import org.junit.Test;

import static com.here.android.mpa.routing.RouteOptions.TransportMode.BICYCLE;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Test for {@link TransportModePanel}.
 */
public class TransportModePanelTest extends RobolectricTest implements TransportModePanel.OnSelectedListener {

    private TransportModePanel mTransportModePanel;
    private Context mContext;
    private int mSelectedIndex = -1;   // tab selection index
    private int mUnselectedIndex = -1; // tab un-selection index
    private int mReselectedIndex = -1; // tab re-selection index

    @Before
    public void setUp() {
        mContext = getContextWithTheme();
        mTransportModePanel = new TransportModePanel(mContext);
    }

    /**
     * Test proper initialization.
     * <p>Initially there should be no tab & no page content.</p>
     */
    @Test
    public void testInitialization() {
        assertNotNull(mTransportModePanel.getTabView());
        // by default there is 4 tabs
        assertThat(mTransportModePanel.getTabCount(), equalTo(5));
        assertNotNull(mTransportModePanel.getViewPager());
        assertThat(mTransportModePanel.getViewPager().getChildCount(), equalTo(0));
    }

    /**
     * Test default panel content.
     * <p>Default panel contains 5 tab, each tab is having icon and there is no content.</p>
     */
    @Test
    public void testDefaultContent() {
        mTransportModePanel.setAdapter(new SimpleTransportModePanelAdapter(mContext));
        assertThat(mTransportModePanel.getTabCount(), equalTo(5));
        assertNotNull(mTransportModePanel.getTabView()
                .getTabAt(0)
                .getIcon());
        assertThat(mTransportModePanel.getViewPager().getVisibility(), equalTo(View.GONE));
    }

    /**
     * Test tab selection of default panel.
     * <ul><li>Selecting tab directly should call proper callback.</li>
     * <li>Selecting tab via {@link TransportModePanel#setSelected(int)} should call proper callback &
     * {@link TransportModePanel#getSelected()} should return proper value.</li>
     * <li>Selecting tab via {@link TransportModePanel#setSelectedTransportMode(RouteOptions.TransportMode)} should call proper callback &
     * {@link TransportModePanel#getSelectedTransportMode()} should return proper value.</li>
     * </ul>
     */
    @Test
    public void testTabSelection() {
        mTransportModePanel.setAdapter(new SimpleTransportModePanelAdapter(mContext));
        mTransportModePanel.setOnSelectedListener(this);    // test selection
        mTransportModePanel.getTabView()
                .getTabAt(1)
                .select();

        assertThat(mSelectedIndex, equalTo(1)); // selection is ok.

        mTransportModePanel.setSelected(2);
        assertThat(mSelectedIndex, equalTo(2));
        assertThat(mUnselectedIndex, equalTo(1));
        assertThat(mTransportModePanel.getSelected(), equalTo(2)); // selection is ok.

        mTransportModePanel.setSelected(2);
        assertThat(mReselectedIndex, equalTo(2));

        // test transport mode selection
        mTransportModePanel.setSelectedTransportMode(BICYCLE);
        assertThat(mTransportModePanel.getSelectedTransportMode(), equalTo(BICYCLE));
        assertThat(mSelectedIndex, is(not(equalTo(-1))));
    }

    /**
     * Test custom panel content.
     * <p>If content provided in custom panel, they should display properly & selecting tab should switch to
     * respective content page.</p>
     */
    @Test
    public void testTabCustomContent() {
        mTransportModePanel.setAdapter(new ShadowAdapter(mContext));
        // now since we are passing content data too. view pager should be visible
        assertThat(mTransportModePanel.getViewPager().getVisibility(), equalTo(View.VISIBLE));
        assertThat(mTransportModePanel.getTabCount(), equalTo(5));
        mTransportModePanel.setSelected(2);
        assertThat(mTransportModePanel.getViewPager().getCurrentItem(), equalTo(2));
    }

    /**
     * Test adding, deleting of tabs in {@code TransportModePanel}.
     * <ul><li>Addition of tab should add tab in panel and UI should be refreshed.</li>
     * <li>Deletion of tab should delete tab in panel and UI should be refreshed.</li></ul>
     */
    @Test
    public void testModificationOfTab() {
        SimpleTransportModePanelAdapter adapter = new SimpleTransportModePanelAdapter(mContext);
        mTransportModePanel.setAdapter(adapter);

        assertThat(mTransportModePanel.getTabCount(), equalTo(5));

        //delete one
        adapter.getTransportModes()
                .remove(BICYCLE);
        adapter.notifyDataSetChanged();
        assertThat(mTransportModePanel.getTabCount(), equalTo(4));

        // add again
        adapter.getTransportModes()
                .add(BICYCLE);
        adapter.notifyDataSetChanged();
        assertThat(mTransportModePanel.getTabCount(), equalTo(5));

        // remove all
        adapter.getTransportModes()
                .clear();
        adapter.notifyDataSetChanged();
        assertThat(mTransportModePanel.getTabCount(), equalTo(0));
    }

    @Override
    public void onSelected(int index, TabView button) {
        mSelectedIndex = index;
    }

    @Override
    public void onUnselected(int index, TabView button) {
        mUnselectedIndex = index;
    }

    @Override
    public void onReselected(int index, TabView button) {
        mReselectedIndex = index;
    }

    /**
     * Shadow for fragment.
     */
    public static class ShadowFragment extends Fragment {

    }

    /**
     * Shadow of custom adapter.
     */
    public static class ShadowAdapter extends SimpleTransportModePanelAdapter {

        ShadowAdapter(final Context context) {
            super(context);
        }

        @Override
        public Fragment getContent(RouteOptions.TransportMode mode) {
            return new Fragment();
        }

        @Override
        public boolean isContentVisible() {
            return true;
        }
    }
}
