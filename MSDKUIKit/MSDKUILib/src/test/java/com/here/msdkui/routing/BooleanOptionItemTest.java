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

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * An option item with only one checkbox.
 */
public class BooleanOptionItemTest extends RobolectricTest {

    private BooleanOptionItem mBooleanOptionItem;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mBooleanOptionItem = new OptionItemBuilders.BooleanOptionItemBuilder(getApplicationContext()).build();
    }

    @Test
    public void testInitUi() {
        final TextView labelView = (TextView) mBooleanOptionItem.findViewById(R.id.boolean_item_label);
        final Switch valueView = (Switch) mBooleanOptionItem.findViewById(R.id.boolean_item_value);
        assertNotNull(labelView);
        assertThat(labelView.getVisibility(), equalTo(View.VISIBLE));
        assertNotNull(valueView);
        assertThat(valueView.getVisibility(), equalTo(View.VISIBLE));
    }

    @Test
    public void testSelection() {
        // default
        assertThat(mBooleanOptionItem.isChecked(), is(false));
        mBooleanOptionItem.setChecked(true);
        assertThat(mBooleanOptionItem.isChecked(), is(true));
    }

    @Test
    public void testLabel() {
        mBooleanOptionItem.setLabel(getString(R.string.msdkui_violate_truck_options));
        final TextView labelView = (TextView) mBooleanOptionItem.findViewById(R.id.boolean_item_label);
        assertThat(labelView.getText().toString(),
                equalTo(getApplicationContext().getString(R.string.msdkui_violate_truck_options)));
        mBooleanOptionItem.setItemId(R.string.msdkui_violate_truck_options);
        assertThat(mBooleanOptionItem.getItemId(), equalTo(R.string.msdkui_violate_truck_options));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullLabelException() {
        mBooleanOptionItem.setLabel(null);
    }

    @Test
    public void testCallback() {
        assertThat(mBooleanOptionItem.isChecked(), is(false));
        mBooleanOptionItem.setListener(new OptionItem.OnChangedListener() {
            @Override
            public void onChanged(OptionItem item) {
                mCallbackCalled = true;
            }
        });
        mBooleanOptionItem.setChecked(true);
        assertThat(mCallbackCalled, is(true));
    }

}
