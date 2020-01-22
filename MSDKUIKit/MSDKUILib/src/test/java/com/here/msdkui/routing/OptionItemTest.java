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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * The base class for all the available option items.
 */
public class OptionItemTest extends RobolectricTest {

    private OptionItemImpl mOptionItem;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mOptionItem = new OptionItemImpl(getApplicationContext());
    }

    public void testInit() {
        // test orientation
        assertThat(mOptionItem.getOrientation(), equalTo(LinearLayout.VERTICAL));
    }

    @Test
    public void testItemType() {
        mOptionItem.setItemType(OptionItem.ItemType.BOOLEAN_OPTION_ITEM);
        assertThat(mOptionItem.getItemType(), equalTo(OptionItem.ItemType.BOOLEAN_OPTION_ITEM));
    }

    @Test
    public void testListener() {
        mOptionItem.setListener(new OptionItem.OnChangedListener() {
            @Override
            public void onChanged(OptionItem item) {
                mCallbackCalled = true;
            }
        });
        mOptionItem.notifyChange();
        assertThat(mCallbackCalled, is(true));
        mCallbackCalled = false;
        mOptionItem.setListener(null);
        mOptionItem.notifyChange();
        assertThat(mCallbackCalled, is(false));
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
