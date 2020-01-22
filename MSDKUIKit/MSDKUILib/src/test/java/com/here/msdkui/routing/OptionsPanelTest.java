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
import android.widget.LinearLayout;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * The base class for all the panels containing set of options.
 */
public class OptionsPanelTest extends RobolectricTest {

    private OptionPanelImpl mOptionPanel;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mOptionPanel = new OptionPanelImpl(getApplicationContext());
    }

    public void testInit() {
        // test orientation
        assertThat(mOptionPanel.getOrientation(), equalTo(LinearLayout.VERTICAL));
    }

    @Test
    public void testItemType() {
        final OptionItem booleanOptionItem = new OptionItemBuilders.BooleanOptionItemBuilder(getApplicationContext()).setLabel(getString(R
                .string.msdkui_optimal)).build();
        mOptionPanel.setOptionItems(Collections.singletonList(booleanOptionItem));
        assertThat(mOptionPanel.getOptionItems().get(0), equalTo(booleanOptionItem));

        OptionItem optionItem = new OptionItemImpl(getContextWithTheme());
        mOptionPanel.setOptionItems(optionItem, Collections.singletonList(booleanOptionItem));
        assertThat(mOptionPanel.getOptionItems().get(0), equalTo(optionItem));
    }

    @Test
    public void testCreationListener() {
        mOptionPanel.setListener(new OptionsPanel.Listener() {
            @Override
            public void onOptionCreated(List<OptionItem> item) {
                mCallbackCalled = true;
            }

            @Override
            public void onOptionChanged(OptionItem item) {
                mCallbackCalled = false;
            }
        });
        final OptionItem booleanOptionItem = new OptionItemBuilders.BooleanOptionItemBuilder(getApplicationContext()).setLabel(getString(R
                .string.msdkui_optimal)).build();
        mOptionPanel.setOptionItems(Collections.singletonList(booleanOptionItem));
        assertThat(mCallbackCalled, is(true));
    }

    /**
     * Implementation of abstract class {@link OptionsPanel}.
     */
    public static class OptionPanelImpl extends OptionsPanel {

        public OptionPanelImpl(final Context context) {
            super(context);
        }
    }

    /**
     * Implementation of abstract class {@link OptionItem}
     */
    public static class OptionItemImpl extends OptionItem {
        public OptionItemImpl(final Context context) {
            super(context);
        }
    }
}
