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
import android.widget.Spinner;
import android.widget.TextView;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * An option item with a set of radio button style checkboxes. The user can can select only one option
 * among the displayed options.
 */
public class SingleChoiceOptionItemTest extends RobolectricTest {

    private static final String TITLE = "Test";
    private SingleChoiceOptionItem mSingleChoiceOptionItem;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mSingleChoiceOptionItem = new SingleChoiceOptionItem(getApplicationContext());
    }

    @Test
    public void testInitUi() {
        // since there is no label set, no labels are expected.
        assertThat(mSingleChoiceOptionItem.getLabels(), is(nullValue()));
        assertThat(mSingleChoiceOptionItem.getSelectedItemLabel(), is(nullValue()));
    }

    @Test
    public void testSelection() {
        final List<String> ids = Arrays.asList(getString(R.string.msdkui_fastest), getString(R.string.msdkui_allow_car_pool));
        mSingleChoiceOptionItem.setLabels(TITLE, ids);
        assertThat(mSingleChoiceOptionItem.getTitle(), equalTo(TITLE));
        assertThat(mSingleChoiceOptionItem.getSelectedItemLabel(), equalTo(getString(R.string.msdkui_fastest)));
        mSingleChoiceOptionItem.selectLabel(getString(R.string.msdkui_allow_car_pool));
        assertThat(mSingleChoiceOptionItem.getSelectedItemLabel(), equalTo(getString(R.string.msdkui_allow_car_pool)));
        mSingleChoiceOptionItem.selectIndex(1);
        assertThat(mSingleChoiceOptionItem.getSelectedItemLabel(), equalTo(getString(R.string.msdkui_allow_car_pool)));
    }

    @Test
    public void testLabels() {
        final List<String> ids = Collections.singletonList(getString(R.string.msdkui_fastest));
        mSingleChoiceOptionItem.setLabels(TITLE, ids);
        final List<String> labels = mSingleChoiceOptionItem.getLabels();
        assertThat(labels.size(), equalTo(1));
        assertThat(labels.get(0), equalTo(getApplicationContext().getString(R.string.msdkui_fastest)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullLabelsException() {
        mSingleChoiceOptionItem.setLabels(TITLE, null);
    }

    @Test
    public void testHideTitleWhenNullTitleProvided() {
        final List<String> labels = Collections.singletonList(getString(R.string.msdkui_fastest));
        mSingleChoiceOptionItem.setLabels(null, labels);
        View titleView = mSingleChoiceOptionItem.getRootView().findViewById(R.id.single_item_label);
        assertNotNull(titleView);
        assertEquals(View.GONE, titleView.getVisibility());
    }

    @Test
    public void testCallback() {
        mSingleChoiceOptionItem.setListener(new OptionItem.OnChangedListener() {
            @Override
            public void onChanged(OptionItem item) {
                mCallbackCalled = true;
            }
        });
        final List<String> ids = Arrays.asList(getString(R.string.msdkui_fastest), getString(R.string.msdkui_allow_car_pool));
        mSingleChoiceOptionItem.setLabels(TITLE, ids);
        assertThat(mSingleChoiceOptionItem.getChildCount(), equalTo(1));
        final View view = mSingleChoiceOptionItem.getChildAt(0);
        final TextView labelView = (TextView) view.findViewById(R.id.single_item_label);
        final Spinner selectionView = (Spinner) view.findViewById(R.id.single_item_value);
        assertNotNull(labelView);
        assertNotNull(selectionView);
        selectionView.setSelection(1);
        assertThat(mCallbackCalled, is(true));
    }
}
