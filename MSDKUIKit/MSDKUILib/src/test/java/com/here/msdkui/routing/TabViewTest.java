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

import android.util.AttributeSet;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Test;
import org.robolectric.Robolectric;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

/**
 * Test class for {@link TabView} class.
 */
public class TabViewTest extends RobolectricTest {

    @Test
    public void testCreationWithIconAndLabel() {
        final String testLabel = "testLabel";
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.icon, "@drawable/ic_drive")
                .addAttribute(R.attr.label, testLabel)
                .build();

        TabView tabView = new TabView(getContextWithTheme(), attributeSet);

        assertNotNull(tabView);
        assertEquals(testLabel, tabView.getLabel());
        assertEquals(
                shadowOf(getApplicationContext().getResources().getDrawable(R.drawable.ic_drive)).getCreatedFromResId(),
                shadowOf(tabView.getIcon()).getCreatedFromResId());
    }

    @Test
    public void testSetLabel() {
        final String testLabel = "testLabel";
        TabView tabView = new TabView(getContextWithTheme());
        tabView.setLabel(testLabel);
        assertEquals(testLabel, tabView.getLabel());
    }
}
