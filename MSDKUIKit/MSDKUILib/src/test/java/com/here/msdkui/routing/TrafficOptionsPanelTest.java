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
import com.here.android.mpa.routing.DynamicPenalty;
import com.here.android.mpa.routing.Route;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * An options panel for displaying the traffic options of a dynamic penalty.
 */
public class TrafficOptionsPanelTest extends RobolectricTest {

    private TrafficOptionsPanel mTrafficOptionsPanel;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mTrafficOptionsPanel = new TrafficOptionsPanel(getApplicationContext());
    }

    @Test
    public void testInitUiFunction() {
        final List<OptionItem> items = mTrafficOptionsPanel.getOptionItems();
        assertThat(items.size(), equalTo(1));

        SingleChoiceOptionItem item = (SingleChoiceOptionItem) items.get(0);
        final List<String> labels = item.getLabels();
        assertThat(labels.size(), equalTo(3));
        item.selectLabel(getString(R.string.msdkui_avoid_long_term_closures));
        String selectedIds = item.getSelectedItemLabel();
        assertThat(selectedIds, equalTo(getString(R.string.msdkui_avoid_long_term_closures)));
    }

    @Test
    public void testSettingDynamicPenalty() {

        DynamicPenalty dynamicPenalty = MockUtils.getDynamicPenality(Route.TrafficPenaltyMode.AVOID_LONG_TERM_CLOSURES);
        mTrafficOptionsPanel.setDynamicPenalty(dynamicPenalty);

        final List<OptionItem> items = mTrafficOptionsPanel.getOptionItems();
        assertThat(items.size(), equalTo(1));
        SingleChoiceOptionItem item = (SingleChoiceOptionItem) items.get(0);
        final List<String> labels = item.getLabels();
        assertThat(labels.size(), equalTo(3));

        String selectedId = item.getSelectedItemLabel();
        assertThat(selectedId, equalTo(getString(R.string.msdkui_avoid_long_term_closures)));

        dynamicPenalty = MockUtils.getDynamicPenality(Route.TrafficPenaltyMode.DISABLED);
        mTrafficOptionsPanel.setDynamicPenalty(dynamicPenalty);
        selectedId = item.getSelectedItemLabel();
        assertThat(selectedId, equalTo(getString(R.string.msdkui_disabled)));
    }

    @Test
    public void testGettingDynamicPenalty() {
        DynamicPenalty dynamicPenalty = MockUtils.getDynamicPenality(Route.TrafficPenaltyMode.AVOID_LONG_TERM_CLOSURES);
        mTrafficOptionsPanel.setDynamicPenalty(dynamicPenalty);
        DynamicPenalty gotPenalty = mTrafficOptionsPanel.getDynamicPenalty();
        assertThat(gotPenalty.getTrafficPenaltyMode(), equalTo(Route.TrafficPenaltyMode.AVOID_LONG_TERM_CLOSURES));
    }

    @Test
    public void testGettingDisableDynamicPenalty() {
        DynamicPenalty dynamicPenalty = MockUtils.getDynamicPenality(Route.TrafficPenaltyMode.DISABLED);
        mTrafficOptionsPanel.setDynamicPenalty(dynamicPenalty);
        DynamicPenalty gotPenalty = mTrafficOptionsPanel.getDynamicPenalty();
        assertThat(gotPenalty.getTrafficPenaltyMode(), equalTo(Route.TrafficPenaltyMode.DISABLED));
    }

    @Test
    public void testChangeListener() {
        DynamicPenalty dynamicPenalty = MockUtils.getDynamicPenality(Route.TrafficPenaltyMode.DISABLED);
        mTrafficOptionsPanel.setDynamicPenalty(dynamicPenalty);
        mTrafficOptionsPanel.setListener(new OptionsPanel.Listener() {
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
        dynamicPenalty = MockUtils.getDynamicPenality(Route.TrafficPenaltyMode.AVOID_LONG_TERM_CLOSURES);
        mTrafficOptionsPanel.setDynamicPenalty(dynamicPenalty);
        assertThat(mCallbackCalled, is(true));
    }
}
