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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * An options panel for displaying the truck options of a {@link RouteOptions RouteOptions}.
 */
public class TruckOptionsPanelTest extends RobolectricTest {

    private TruckOptionsPanel mTruckOptionsPanel;
    private boolean mCallbackCalled = false;

    @Before
    public void setUp() {
        mTruckOptionsPanel = new TruckOptionsPanel(getApplicationContext());
    }

    @Test
    public void testInitUiFunction() {
        final List<OptionItem> items = mTruckOptionsPanel.getOptionItems();
        assertThat(items.size(), equalTo(8));
        BooleanOptionItem booleanOptionItem = (BooleanOptionItem) items.get(items.size() - 1);
        assertThat(booleanOptionItem.getLabel(),
                equalTo(getApplicationContext().getString(R.string.msdkui_violate_truck_options)));
        assertThat(booleanOptionItem.isChecked(), is(false));
        // now panel should have 7 number option item.
        int index = 0;
        for (OptionItem item : items) {
            if (index++ == 6)
                return;
            assertThat(item, instanceOf(NumericOptionItem.class));
        }
        assertThat(items.get(index - 1), instanceOf(SingleChoiceOptionItem.class));
    }

    @Test
    public void testSetRouteOption() {
        RouteOptions options = new MockUtils.MockRouteOptionsBuilder().withTruckOptions()
                .build();
        mTruckOptionsPanel.setRouteOptions(options);
        final List<OptionItem> items = mTruckOptionsPanel.getOptionItems();
        int index = 0;
        for (OptionItem item : items) {
            if (index++ == 0 || index++ == 7)
                return;
            assertThat(item, instanceOf(NumericOptionItem.class));
            final Number value = ((NumericOptionItem) item).getValue();
            assertThat(value, equalTo(3));
        }
    }


    @Test
    public void testGettingRouteOptionsWhenMainItemIsUnselected() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        final RouteOptions mockRouteOptions = builder.withFullHazardousOptions()
                .build();
        mTruckOptionsPanel.setRouteOptions(mockRouteOptions);

        doAnswer(new AnswerGetRouteOptions(RouteOptions.TruckRestrictionsMode.NO_VIOLATIONS)).when(mockRouteOptions)
                .setTruckRestrictionsMode(any(RouteOptions.TruckRestrictionsMode.class));

        final List<OptionItem> items = mTruckOptionsPanel.getOptionItems();
        BooleanOptionItem booleanOptionItem = (BooleanOptionItem) items.get(items.size() - 1);
        booleanOptionItem.setChecked(false);
    }

    @Test
    public void testGettingRouteOptionsWhenMainItemIsSelected() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        final RouteOptions mockRouteOptions = builder.withFullHazardousOptions()
                .build();
        mTruckOptionsPanel.setRouteOptions(mockRouteOptions);

        doAnswer(new AnswerGetRouteOptions(RouteOptions.TruckRestrictionsMode.PENALIZE_VIOLATIONS)).when(mockRouteOptions)
                .setTruckRestrictionsMode(any(RouteOptions.TruckRestrictionsMode.class));

        final List<OptionItem> items = mTruckOptionsPanel.getOptionItems();
        BooleanOptionItem booleanOptionItem = (BooleanOptionItem) items.get(items.size() - 1);
        booleanOptionItem.setChecked(true);
    }

    @Test
    public void testGettingRouteOptionAndVerifyInputs() {
        RouteOptions options = new MockUtils.MockRouteOptionsBuilder().withTruckOptions()
                .build();
        mTruckOptionsPanel.setRouteOptions(options);
        RouteOptions gotOptions = mTruckOptionsPanel.getRouteOptions();
        assertThat(gotOptions.getTruckHeight(), equalTo(3.0f));
        assertThat(gotOptions.getTruckWidth(), equalTo(3.0f));
        assertThat(gotOptions.getTruckLimitedWeight(), equalTo(3.0f));
        assertThat(gotOptions.getTruckWeightPerAxle(), equalTo(3.0f));
        assertThat(gotOptions.getTruckTrailersCount(), equalTo(3));
        assertThat(gotOptions.getTruckType(), equalTo(RouteOptions.TruckType.TRACTOR_TRUCK));
    }

    @Test
    public void testChangeListener() {
        final MockUtils.MockRouteOptionsBuilder builder = new MockUtils.MockRouteOptionsBuilder();
        final RouteOptions options = builder.withTruckOptions().build();
        mTruckOptionsPanel.setRouteOptions(options);
        mTruckOptionsPanel.setListener(new OptionsPanel.Listener() {
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
        when(options.getTruckHeight()).thenReturn(2.0f);
        mTruckOptionsPanel.setRouteOptions(options);
        assertThat(mCallbackCalled, is(true));
    }

    /**
     * Answer implementation
     */
    private static class AnswerGetRouteOptions implements Answer<Void> {

        private final RouteOptions.TruckRestrictionsMode mTruckRestrictionsMode;

        public AnswerGetRouteOptions(RouteOptions.TruckRestrictionsMode mode) {
            mTruckRestrictionsMode = mode;
        }

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            Object[] arguments = invocation.getArguments();
            if (arguments != null && arguments.length > 0 && arguments[0] != null) {
                final RouteOptions.TruckRestrictionsMode restrictionsMode = (RouteOptions.TruckRestrictionsMode) arguments[0];
                if (mTruckRestrictionsMode != null) {
                    assertThat(restrictionsMode, equalTo(mTruckRestrictionsMode));
                }
            }
            return null;
        }
    }
}
