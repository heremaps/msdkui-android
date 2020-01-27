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

import android.graphics.drawable.Drawable;

import com.here.RobolectricTest;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test class for {@link SectionModel} class
 */
public class SectionModelTest extends RobolectricTest {

    private static final int RED_COLOR = 16711680;

    private SectionModel mSectionModel;

    @Before
    public void setUp() {
        mSectionModel = new SectionModel();
    }

    @Test
    public void testSetGetBounds() {
        double lower = 1.0;
        double upper = 2.0;
        SectionModel.Bounds bounds = new SectionModel.Bounds(lower, upper);
        mSectionModel.setBounds(bounds);
        assertEquals(lower, mSectionModel.getBounds().getLower());
        assertEquals(upper, mSectionModel.getBounds().getUpper());
    }

    @Test
    public void testSetGetColor() {
        mSectionModel.setColor(RED_COLOR);
        assertEquals(RED_COLOR, mSectionModel.getColor());
    }

    @Test
    public void testSetGetDrawable() {
        Drawable drawable = mock(Drawable.class);
        mSectionModel.setDrawable(drawable);
        assertEquals(drawable, mSectionModel.getDrawable());
    }
}
