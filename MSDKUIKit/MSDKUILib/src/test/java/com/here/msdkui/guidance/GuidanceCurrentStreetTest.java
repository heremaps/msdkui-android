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
import static junit.framework.Assert.assertNotNull;

/**
 * Test for {@link GuidanceCurrentStreet} class.
 */
public class GuidanceCurrentStreetTest extends RobolectricTest {

    final private static String STREET_NAME = "StreetName";
    final private static int COLOR = 16777215;

    private GuidanceCurrentStreet mGuidanceCurrentStreet = null;

    @Before
    public void setUp() {
        mGuidanceCurrentStreet = new GuidanceCurrentStreet(getApplicationContext());
    }

    @Test
    public void testSetCurrentStreetData() {
        final GuidanceCurrentStreetData currentStreetData = new GuidanceCurrentStreetData(STREET_NAME, COLOR);
        mGuidanceCurrentStreet.setCurrentStreetData(currentStreetData);
        assertEquals(currentStreetData.getCurrentStreetName(), mGuidanceCurrentStreet.getGuidanceCurrentStreetData().getCurrentStreetName());
        assertEquals(currentStreetData.getBackgroundColor(), mGuidanceCurrentStreet.getGuidanceCurrentStreetData().getBackgroundColor());
    }

    @Test
    public void SavedState_testWriteAndCreateFromParcel() {
        Parcel parcel = Parcel.obtain();
        final GuidanceCurrentStreet.SavedState savedState = new GuidanceCurrentStreet.SavedState(parcel);

        assertNotNull(savedState);

        GuidanceCurrentStreetData streetData = new GuidanceCurrentStreetData(STREET_NAME, COLOR);
        savedState.setStateToSave(streetData);

        assertEquals(streetData, savedState.getSavedState());

        savedState.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        GuidanceCurrentStreet.SavedState recreatedState = new GuidanceCurrentStreet.SavedState(parcel);
        assertEquals(STREET_NAME, recreatedState.getSavedState().getCurrentStreetName());
        assertEquals(COLOR, recreatedState.getSavedState().getBackgroundColor());
    }
}
