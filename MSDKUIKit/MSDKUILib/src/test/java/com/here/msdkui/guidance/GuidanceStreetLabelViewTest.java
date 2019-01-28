/*
 * Copyright (C) 2017-2019 HERE Europe B.V.
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
 * Test for {@link GuidanceStreetLabelView} class.
 */
public class GuidanceStreetLabelViewTest extends RobolectricTest {

    private static final String STREET_NAME = "StreetName";
    private static final int COLOR = 16777215;

    private GuidanceStreetLabelView mGuidanceStreetLabelView = null;

    @Before
    public void setUp() {
        super.setUp();
        mGuidanceStreetLabelView = new GuidanceStreetLabelView(getApplicationContext());
    }

    @Test
    public void testSetCurrentStreetData() {
        final GuidanceStreetLabelData currentStreetData = new GuidanceStreetLabelData(STREET_NAME, COLOR);
        mGuidanceStreetLabelView.setCurrentStreetData(currentStreetData);
        assertEquals(currentStreetData.getCurrentStreetName(),
                mGuidanceStreetLabelView.getGuidanceCurrentStreetData().getCurrentStreetName());
        assertEquals(currentStreetData.getBackgroundColor(),
                mGuidanceStreetLabelView.getGuidanceCurrentStreetData().getBackgroundColor());
    }

    @Test
    public void testDataIsNotLostWhileRecreatingActivity() {
        mGuidanceStreetLabelView.setCurrentStreetData(createData(STREET_NAME, COLOR));
        assertNotNull(mGuidanceStreetLabelView.getGuidanceCurrentStreetData());
        final FragmentActivity activity = getFragmentActivity();
        mGuidanceStreetLabelView.setId(R.id.vertical_guideline);
        activity.setContentView(mGuidanceStreetLabelView);
        activity.recreate();
        assertNotNull(mGuidanceStreetLabelView.getGuidanceCurrentStreetData());
    }

    @Test
    public void testPanelDataIsParcelable() {
        final GuidanceStreetLabelData data = createData(STREET_NAME, COLOR);
        GuidanceStreetLabelView.SavedState savedState = new GuidanceStreetLabelView.SavedState(AbsSavedState.EMPTY_STATE);
        savedState.setStateToSave(data);
        Parcel parcel = Parcel.obtain();
        savedState.writeToParcel(parcel, savedState.describeContents());
        parcel.setDataPosition(0);

        GuidanceStreetLabelView.SavedState createdFromParcel = GuidanceStreetLabelView.SavedState.CREATOR.createFromParcel(
                parcel);
        assertNotNull(createdFromParcel.getSavedState());
    }

    private GuidanceStreetLabelData createData(String currentStreet, int bgColor) {
        return new GuidanceStreetLabelData(currentStreet, bgColor);
    }
}
