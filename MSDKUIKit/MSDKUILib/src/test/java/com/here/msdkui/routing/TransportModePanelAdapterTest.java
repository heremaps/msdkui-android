/*
 * Copyright (C) 2017-2018 HERE Europe B.V.
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
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import com.here.RobolectricTest;
import com.here.android.mpa.routing.RouteOptions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test for {@link TransportModePanelAdapter}.
 */
public class TransportModePanelAdapterTest extends RobolectricTest {

    private Context mContext;
    private List<RouteOptions.TransportMode> mTransportModes;

    @Before
    public void setUp() {
        super.setUp();
        mTransportModes = new ArrayList<>(1);
        mTransportModes.add(RouteOptions.TransportMode.BICYCLE);
    }

    /**
     * Test for adapter having tab view only.
     */
    @Test
    public void testTabViewAdapter() {

        final TabView btn = new TabView(getContextWithTheme());

        TransportModePanelAdapter adapter = new TransportModePanelAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getContent(RouteOptions.TransportMode mode) {
                return null;
            }

            @Override
            public TabView getTabCustomView(RouteOptions.TransportMode mode) {
                return btn;
            }

            @Override
            public boolean isContentVisible() {
                return false;
            }
        };

        adapter.setTransportModes(mTransportModes);
        assertThat(adapter.getTransportModes(), equalTo(mTransportModes));
        assertThat(adapter.getCount(), equalTo(mTransportModes.size()));
        assertNull(adapter.getItem(0));
        assertThat(adapter.getPageTitle(0), equalTo(""));
        assertThat(adapter.getTabView(0), equalTo(btn));
        assertThat(adapter.isContentVisible(), is(false));
    }

    /**
     * Test for adapter having tab view & content.
     */
    @Test
    public void testContentAdapter() {
        final TabView btn = new TabView(getContextWithTheme());
        final Fragment fr = new Fragment();

        TransportModePanelAdapter adapter = new TransportModePanelAdapter(null) {
            @Override
            public Fragment getContent(RouteOptions.TransportMode mode) {
                return fr;
            }

            @Override
            public TabView getTabCustomView(RouteOptions.TransportMode mode) {
                return btn;
            }

            @Override
            public boolean isContentVisible() {
                return true;
            }
        };

        adapter.setTransportModes(mTransportModes);
        assertThat(adapter.getTransportModes(), equalTo(mTransportModes));
        assertThat(adapter.getCount(), equalTo(mTransportModes.size()));
        assertNotNull(adapter.getItem(0));
        assertThat(adapter.getPageTitle(0), equalTo(""));
        assertThat(adapter.getTabView(0), equalTo(btn));
        assertThat(adapter.isContentVisible(), is(true));
    }

    @Test
    public void testGetItemPosition() {
        TransportModePanelAdapter adapter = new TransportModePanelAdapter(getSupportFragmentManager(), mTransportModes) {
            @Override
            public Fragment getContent(RouteOptions.TransportMode mode) {
                return null;
            }

            @Override
            public TabView getTabCustomView(RouteOptions.TransportMode mode) {
                return null;
            }

            @Override
            public boolean isContentVisible() {
                return false;
            }
        };
        assertEquals(PagerAdapter.POSITION_NONE, adapter.getItemPosition(new Object()));
    }
}