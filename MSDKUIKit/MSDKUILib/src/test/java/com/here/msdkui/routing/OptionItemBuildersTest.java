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

import android.text.InputType;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * The helper class to make option items.
 */
public final class OptionItemBuildersTest extends RobolectricTest {

    private List<String> mIds;

    @Before
    public void setUp() {
        mIds = new ArrayList<>();
        mIds.add(getString(R.string.msdkui_allow_toll_roads));
    }

    @Test
    public void testBooleanSpecs() {
        final OptionItem booleanOptionItem = new OptionItemBuilders.BooleanOptionItemBuilder(getApplicationContext()).setLabel(mIds.get(0))
                .build();
        assertNotNull(booleanOptionItem);
    }

    @Test
    public void testSingleChoiceItemSpec() {
        final OptionItem singleChoiceItemSpec = new OptionItemBuilders.SingleChoiceOptionItemBuilder(getApplicationContext())
                .setLabels("", mIds).build();
        assertNotNull(singleChoiceItemSpec);
    }

    @Test
    public void testMultipleChoiceItemSpec() {
        int itemId = 5;
        final OptionItem multipleChoiceItemSpec = new OptionItemBuilders.MultipleChoiceOptionItemBuilder(getApplicationContext())
                .setLabels(mIds)
                .setItemId(itemId)
                .build();
        assertNotNull(multipleChoiceItemSpec);
        assertEquals(mIds.size(), ((MultipleChoiceOptionItem) multipleChoiceItemSpec).getLabels().size());
        assertEquals(itemId, multipleChoiceItemSpec.getItemId());
    }

    @Test
    public void testNumberItemSpec() {
        final OptionItem numberItemSpec = new OptionItemBuilders.NumericOptionItemBuilder(getApplicationContext())
                .setLabel(mIds.get(0))
                .setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
                .build();
        assertNotNull(numberItemSpec);
    }

}
