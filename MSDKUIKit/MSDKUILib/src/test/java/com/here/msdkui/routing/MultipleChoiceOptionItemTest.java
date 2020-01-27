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
import android.widget.CheckBox;
import android.widget.TextView;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * An option item with a set of checkboxes. The user can can select more than one option among the displayed options
 */
public class MultipleChoiceOptionItemTest extends RobolectricTest {

    private MultipleChoiceOptionItem mMultipleChoiceOptionItem;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mMultipleChoiceOptionItem = new OptionItemBuilders.MultipleChoiceOptionItemBuilder(getApplicationContext()).build();
    }

    @Test
    public void testInitUi() {
        // since there is no label set, no labels are expected.
        assertThat(mMultipleChoiceOptionItem.getLabels(), is(empty()));
        assertThat(mMultipleChoiceOptionItem.getSelectedLabels(), is(empty()));
    }

    @Test
    public void testSelection() {
        final List<String> ids = Collections.singletonList(getString(R.string.msdkui_fastest));
        mMultipleChoiceOptionItem.setLabels(ids);
        assertThat(mMultipleChoiceOptionItem.getSelectedLabels(), is(empty()));
        mMultipleChoiceOptionItem.selectLabels(ids);
        assertThat(mMultipleChoiceOptionItem.getSelectedLabels().size(), equalTo(1));
        assertTrue(mMultipleChoiceOptionItem.isItemSelected(ids.get(0)));
    }

    @Test
    public void testLabels() {
        final List<String> ids = Collections.singletonList(getString(R.string.msdkui_fastest));
        mMultipleChoiceOptionItem.setLabels(ids);
        final List<String> labels = mMultipleChoiceOptionItem.getLabels();
        assertThat(labels.size(), equalTo(1));
        assertThat(labels.get(0), equalTo(getApplicationContext().getString(R.string.msdkui_fastest)));
    }

    @Test
    public void testCallback() {
        mMultipleChoiceOptionItem.setListener(new OptionItem.OnChangedListener() {
            @Override
            public void onChanged(OptionItem item) {
                mCallbackCalled = true;
            }
        });
        final List<String> ids = Collections.singletonList(getString(R.string.msdkui_fastest));
        mMultipleChoiceOptionItem.setLabels(ids);
        assertThat(mMultipleChoiceOptionItem.getChildCount(), equalTo(1));
        final View view = mMultipleChoiceOptionItem.getChildAt(0);
        final TextView labelView = (TextView) view.findViewById(R.id.multiple_item_label);
        final CheckBox selectionView = (CheckBox) view.findViewById(R.id.multiple_item_value);
        assertNotNull(labelView);
        assertNotNull(selectionView);
        selectionView.setChecked(true);
        assertThat(mCallbackCalled, is(true));
    }
}
