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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * An options panel for displaying the hazardous materials options of {@link RouteOptions RouteOptions}.
 */
public class HazardousMaterialsOptionsPanelTest extends RobolectricTest {

    private HazardousMaterialsOptionsPanel mHazardousMaterialsOptionsPanel;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mHazardousMaterialsOptionsPanel = new HazardousMaterialsOptionsPanel(getApplicationContext());
    }

    @Test
    public void testInitUiFunction() {
        final List<OptionItem> items = mHazardousMaterialsOptionsPanel.getOptionItems();
        assertThat(items.size(), equalTo(1));

        MultipleChoiceOptionItem item = (MultipleChoiceOptionItem) items.get(0);
        final List<String> labels = item.getLabels();
        assertThat(labels.size(), equalTo(11));
        List<String> selectedIds = item.getSelectedLabels();
        assertThat(selectedIds.size(), equalTo(0));
        assertThat(labels, hasItem(getApplicationContext().getString(R.string.msdkui_combustible)));
        item.selectLabels(Collections.singletonList(getString(R.string.msdkui_combustible)));
        selectedIds = item.getSelectedLabels();
        assertThat(selectedIds.size(), equalTo(1));
    }

    @Test
    public void testSettingRouteOptions() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        mHazardousMaterialsOptionsPanel.setRouteOptions(builder.withFullHazardousOptions()
                .build());
        final List<OptionItem> items = mHazardousMaterialsOptionsPanel.getOptionItems();
        assertThat(items.size(), equalTo(1));
        MultipleChoiceOptionItem item = (MultipleChoiceOptionItem) items.get(0);
        final List<String> labels = item.getLabels();
        assertThat(labels.size(), equalTo(11));
        List<String> selectedIds = item.getSelectedLabels();
        assertThat(selectedIds.size(), equalTo(11));

        mHazardousMaterialsOptionsPanel.setRouteOptions(builder.withFewHazardousOptions()
                .build());
        selectedIds = item.getSelectedLabels();
        assertThat(selectedIds.size(), equalTo(1));
    }

    @Test
    public void testGettingRouteOptionsWhenSetFewHazardous() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        mHazardousMaterialsOptionsPanel.setRouteOptions(builder.withFewHazardousOptions()
                .build());
        RouteOptions routeOptions = mHazardousMaterialsOptionsPanel.getRouteOptions();
        final EnumSet<RouteOptions.HazardousGoodType> goodTypes = routeOptions.getTruckShippedHazardousGoods();
        assertThat(goodTypes.size(), equalTo(1));
        assertThat(goodTypes, hasItem(RouteOptions.HazardousGoodType.COMBUSTIBLE));
    }

    @Test
    public void testGettingRouteOptionsWhenSetAllHazardous() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        mHazardousMaterialsOptionsPanel.setRouteOptions(builder.withFullHazardousOptions()
                .build());
        RouteOptions routeOptions = mHazardousMaterialsOptionsPanel.getRouteOptions();
        final EnumSet<RouteOptions.HazardousGoodType> goodTypes = routeOptions.getTruckShippedHazardousGoods();
        assertThat(goodTypes.size(), equalTo(11));
    }

    @Test
    public void testGettingRouteOptionsWhenMainItemIsUnselected() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        final RouteOptions mockRouteOptions = builder.withFullHazardousOptions()
                .build();
        mHazardousMaterialsOptionsPanel.setRouteOptions(mockRouteOptions);

        doAnswer(new NoGoodTypesAnswer()).when(mockRouteOptions)
                .setTruckShippedHazardousGoods(any(EnumSet.class));

        mHazardousMaterialsOptionsPanel.getOptionItems();
    }

    @Test
    public void testChangeListener() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        final RouteOptions options = builder.withFewHazardousOptions().build();
        mHazardousMaterialsOptionsPanel.setRouteOptions(options);
        mHazardousMaterialsOptionsPanel.setListener(new OptionsPanel.Listener() {
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
        when(options.getTruckShippedHazardousGoods()).thenReturn(EnumSet.noneOf(RouteOptions.HazardousGoodType.class));
        mHazardousMaterialsOptionsPanel.setRouteOptions(options);
        assertThat(mCallbackCalled, is(true));
    }

    /**
     * Answer implementation
     */
    private static class NoGoodTypesAnswer implements Answer<Void> {
        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            Object[] arguments = invocation.getArguments();
            if (arguments != null && arguments.length > 0 && arguments[0] != null) {
                final EnumSet<RouteOptions.HazardousGoodType> goodTypes = (EnumSet<RouteOptions.HazardousGoodType>) arguments[0];
                assertThat(goodTypes.size(), equalTo(0));
            }
            return null;
        }
    }
}