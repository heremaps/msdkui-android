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

import com.here.RobolectricTest;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test for {@link SimpleTransportModePanelAdapter}.
 */
public class SimpleTransportModePanelAdapterTest extends RobolectricTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    /**
     * Test proper initialization.
     */
    @Test
    public void testInitialization() {
        SimpleTransportModePanelAdapter adapter = new SimpleTransportModePanelAdapter(getContextWithTheme());
        assertThat(adapter.getCount(), equalTo(5));
        assertNull(adapter.getItem(0));
        assertNotNull(adapter.getTabView(0)
                .getIcon());
        assertThat(adapter.isContentVisible(), is(false));
    }
}