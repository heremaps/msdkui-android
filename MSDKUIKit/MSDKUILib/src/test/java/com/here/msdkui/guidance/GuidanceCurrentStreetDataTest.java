/*
 * Copyright (C) 2017-2018 HERE Europe B.V.
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

package com.here.msdkui.guidance;

import android.os.Parcel;

import com.here.RobolectricTest;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Test for {@link GuidanceCurrentStreetData} class.
 */
public class GuidanceCurrentStreetDataTest extends RobolectricTest {

    final private static String NAME = "StreetName";
    final private static int COLOR = 16777215;

    private GuidanceCurrentStreetData mCurrentStreetData = null;

    @Before
    public void setUp() {
        mCurrentStreetData = new GuidanceCurrentStreetData(NAME, COLOR);
    }

    @Test
    public void testDefaultState() {
        assertEquals(NAME, mCurrentStreetData.getCurrentStreetName());
        assertEquals(COLOR, mCurrentStreetData.getBackgroundColor());
        assertEquals(0, mCurrentStreetData.describeContents());
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        mCurrentStreetData.writeToParcel(parcel, mCurrentStreetData.describeContents());
        parcel.setDataPosition(0);
        GuidanceCurrentStreetData fromParcel = GuidanceCurrentStreetData.CREATOR.createFromParcel(parcel);
        assertEquals(NAME, fromParcel.getCurrentStreetName());
        assertEquals(COLOR, fromParcel.getBackgroundColor());
    }
}
