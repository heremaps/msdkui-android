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
import android.support.v4.app.FragmentActivity;
import android.view.AbsSavedState;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Test for {@link GuidanceCurrentStreet} class.
 */
public class GuidanceCurrentStreetTest extends RobolectricTest {

    private static final String STREET_NAME = "StreetName";
    private static final int COLOR = 16777215;

    private GuidanceCurrentStreet mGuidanceCurrentStreet = null;

    @Before
    public void setUp() {
        super.setUp();
        mGuidanceCurrentStreet = new GuidanceCurrentStreet(getApplicationContext());
    }

    @Test
    public void testSetCurrentStreetData() {
        final GuidanceCurrentStreetData currentStreetData = new GuidanceCurrentStreetData(STREET_NAME, COLOR);
        mGuidanceCurrentStreet.setCurrentStreetData(currentStreetData);
        assertEquals(currentStreetData.getCurrentStreetName(),
                mGuidanceCurrentStreet.getGuidanceCurrentStreetData().getCurrentStreetName());
        assertEquals(currentStreetData.getBackgroundColor(),
                mGuidanceCurrentStreet.getGuidanceCurrentStreetData().getBackgroundColor());
    }

    @Test
    public void testDataIsNotLostWhileRecreatingActivity() {
        mGuidanceCurrentStreet.setCurrentStreetData(createData(STREET_NAME, COLOR));
        assertNotNull(mGuidanceCurrentStreet.getGuidanceCurrentStreetData());
        final FragmentActivity activity = getFragmentActivity();
        mGuidanceCurrentStreet.setId(R.id.vertical_guideline);
        activity.setContentView(mGuidanceCurrentStreet);
        activity.recreate();
        assertNotNull(mGuidanceCurrentStreet.getGuidanceCurrentStreetData());
    }

    @Test
    public void testPanelDataIsParcelable() {
        final GuidanceCurrentStreetData data = createData(STREET_NAME, COLOR);
        GuidanceCurrentStreet.SavedState savedState = new GuidanceCurrentStreet.SavedState(AbsSavedState.EMPTY_STATE);
        savedState.setStateToSave(data);
        Parcel parcel = Parcel.obtain();
        savedState.writeToParcel(parcel, savedState.describeContents());
        parcel.setDataPosition(0);

        GuidanceCurrentStreet.SavedState createdFromParcel = GuidanceCurrentStreet.SavedState.CREATOR.createFromParcel(
                parcel);
        assertNotNull(createdFromParcel.getSavedState());
    }

    private GuidanceCurrentStreetData createData(String currentStreet, int bgColor) {
        return new GuidanceCurrentStreetData(currentStreet, bgColor);
    }
}
