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

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.routing.RouteOptions;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * An options panel for displaying the tunnel options of {@link RouteOptions RouteOptions}.
 */
public class TunnelOptionsPanelTest extends RobolectricTest {

    private TunnelOptionsPanel mTunnelOptionsPanel;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mTunnelOptionsPanel = new TunnelOptionsPanel(getApplicationContext());
    }

    @Test
    public void testInitUiFunction() {
        final List<OptionItem> items = mTunnelOptionsPanel.getOptionItems();
        assertThat(items.size(), equalTo(1));
        SingleChoiceOptionItem item = (SingleChoiceOptionItem) items.get(0);
        final List<String> labels = item.getLabels();
        assertThat(labels.size(), equalTo(5));
        item.selectLabel(getString(R.string.msdkui_tunnel_cat_c));
        String selectedIds = item.getSelectedItemLabel();
        assertThat(selectedIds, equalTo(getString(R.string.msdkui_tunnel_cat_c)));
    }

    @Test
    public void testSettingRouteOptions() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        mTunnelOptionsPanel.setRouteOptions(builder.withTunnelOptions()
                .build());
        final List<OptionItem> items = mTunnelOptionsPanel.getOptionItems();
        assertThat(items.size(), equalTo(1));
        SingleChoiceOptionItem item = (SingleChoiceOptionItem) items.get(0);
        final List<String> labels = item.getLabels();
        assertThat(labels.size(), equalTo(5));
        String selectedIds = item.getSelectedItemLabel();
        assertThat(selectedIds, equalTo(getString(R.string.msdkui_tunnel_cat_c)));
        mTunnelOptionsPanel.setRouteOptions(builder.withNoTunnelOptions()
                .build());
        selectedIds = item.getSelectedItemLabel();
        assertThat(selectedIds, equalTo(getString(R.string.msdkui_undefined)));
    }

    @Test
    public void testGettingRouteOptionsWithNoTunnel() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        mTunnelOptionsPanel.setRouteOptions(builder.withNoTunnelOptions()
                .build());
        RouteOptions routeOptions = mTunnelOptionsPanel.getRouteOptions();
        final RouteOptions.TunnelCategory truckTunnelCategory = routeOptions.getTruckTunnelCategory();
        assertThat(truckTunnelCategory, equalTo(RouteOptions.TunnelCategory.UNDEFINED));
    }

    @Test
    public void testGettingRouteOptionsWithTunnel() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        mTunnelOptionsPanel.setRouteOptions(builder.withTunnelOptions()
                .build());
        RouteOptions routeOptions = mTunnelOptionsPanel.getRouteOptions();
        final RouteOptions.TunnelCategory truckTunnelCategory = routeOptions.getTruckTunnelCategory();
        assertThat(truckTunnelCategory, equalTo(RouteOptions.TunnelCategory.C));
    }

    @Test
    public void testChangeListener() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        RouteOptions options = builder.withTunnelOptions().build();
        mTunnelOptionsPanel.setRouteOptions(options);
        mTunnelOptionsPanel.setListener(new OptionsPanel.Listener() {
            @Override
            public void onOptionCreated(List<OptionItem> item) {
                mCallbackCalled = false;
            }

            @Override
            public void onOptionChanged(OptionItem item) {
                mCallbackCalled = true;
            }
        });
        // negate one part
        options = builder.withNoTunnelOptions().build();
        mTunnelOptionsPanel.setRouteOptions(options);
        assertThat(mCallbackCalled, is(true));
    }
}
