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

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * An options panel for displaying the drive options of {@link RouteOptions RouteOptions}.
 */
public class RouteOptionsPanelTest extends RobolectricTest {

    private RouteOptionsPanel mRouteOptionsPanel;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mRouteOptionsPanel = new RouteOptionsPanel(getApplicationContext());
    }

    @Test
    public void testInitUiFunction() {
        final List<OptionItem> items = mRouteOptionsPanel.getOptionItems();
        assertThat(items.size(), equalTo(1));
        MultipleChoiceOptionItem item = (MultipleChoiceOptionItem) items.get(0);
        final List<String> labels = item.getLabels();
        assertThat(labels.size(), equalTo(8));
        List<String> selectedIds = item.getSelectedLabels();
        assertThat(selectedIds.size(), equalTo(0));
        assertThat(labels, hasItem(getString(R.string.msdkui_allow_car_pool)));
        item.selectLabels(Collections.singletonList(getString(R.string.msdkui_allow_car_pool)));
        selectedIds = item.getSelectedLabels();
        assertThat(selectedIds.size(), equalTo(1));
    }

    @Test
    public void testSettingRouteOptions() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        mRouteOptionsPanel.setRouteOptions(builder.withFullDriveOptions()
                .build());
        final List<OptionItem> items = mRouteOptionsPanel.getOptionItems();
        assertThat(items.size(), equalTo(1));
        MultipleChoiceOptionItem item = (MultipleChoiceOptionItem) items.get(0);
        final List<String> labels = item.getLabels();
        assertThat(labels.size(), equalTo(8));
        List<String> selectedIds = item.getSelectedLabels();
        assertThat(selectedIds.size(), equalTo(8));

        mRouteOptionsPanel.setRouteOptions(builder.withFewDriveOptions()
                .build());
        selectedIds = item.getSelectedLabels();
        assertThat(selectedIds.size(), equalTo(2));
    }

    @Test
    public void testGettingRouteOptions() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        mRouteOptionsPanel.setRouteOptions(builder.withFullDriveOptions()
                .build());
        RouteOptions routeOptions = mRouteOptionsPanel.getRouteOptions();
        assertThat(routeOptions.areCarShuttleTrainsAllowed(), is(true));
        assertThat(routeOptions.isCarpoolAllowed(), is(true));
        assertThat(routeOptions.areDirtRoadsAllowed(), is(true));
        assertThat(routeOptions.areFerriesAllowed(), is(true));
        assertThat(routeOptions.areHighwaysAllowed(), is(true));
        assertThat(routeOptions.areParksAllowed(), is(true));
        assertThat(routeOptions.areTollRoadsAllowed(), is(true));
        assertThat(routeOptions.areTunnelsAllowed(), is(true));
    }

    @Test
    public void testGettingRouteOptions2() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        mRouteOptionsPanel.setRouteOptions(builder.withFewDriveOptions()
                .build());
        RouteOptions routeOptions = mRouteOptionsPanel.getRouteOptions();
        assertThat(routeOptions.areCarShuttleTrainsAllowed(), is(true));
        assertThat(routeOptions.isCarpoolAllowed(), is(false));
        assertThat(routeOptions.areDirtRoadsAllowed(), is(true));
        assertThat(routeOptions.areFerriesAllowed(), is(false));
        assertThat(routeOptions.areHighwaysAllowed(), is(false));
        assertThat(routeOptions.areParksAllowed(), is(false));
        assertThat(routeOptions.areTollRoadsAllowed(), is(false));
        assertThat(routeOptions.areTunnelsAllowed(), is(false));
    }

    @Test
    public void testChangeListener() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        final RouteOptions options = builder.withFewDriveOptions().build();
        mRouteOptionsPanel.setRouteOptions(options);
        mRouteOptionsPanel.setListener(new OptionsPanel.Listener() {
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
        when(options.areCarShuttleTrainsAllowed()).thenReturn(false);
        mRouteOptionsPanel.setRouteOptions(options);
        assertThat(mCallbackCalled, is(true));
    }
}
