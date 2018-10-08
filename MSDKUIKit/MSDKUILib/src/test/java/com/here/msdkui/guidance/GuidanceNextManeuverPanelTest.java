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
import android.view.View;
import android.widget.TextView;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link GuidanceNextManeuverPanel}.
 */
public class GuidanceNextManeuverPanelTest extends RobolectricTest {

    private GuidanceNextManeuverPanel mGuidanceNextManeuverPanel;
    private final int mIconId = R.drawable.ic_maneuver_icon_0;
    private final long mDistance = 100;
    private final String mStreetName = "SomeStreet";

    @Before
    public void setUp() {
        super.setUp();
        mGuidanceNextManeuverPanel = new GuidanceNextManeuverPanel(getApplicationContext());
    }

    @Test
    public void testUIInit() {

        final TextView distanceView = mGuidanceNextManeuverPanel.findViewById(R.id.nextManeuverDistance);
        final TextView streetName = mGuidanceNextManeuverPanel.findViewById(R.id.afterNextManeuverStreetName);
        final View containerView = mGuidanceNextManeuverPanel.findViewById(R.id.afterNextManeuverContainer);

        assertEquals(View.GONE, containerView.getVisibility());

        GuidanceNextManeuverData data = createData(mIconId, mDistance, mStreetName);
        // create proper data to see if everything is fine.
        mGuidanceNextManeuverPanel.setNextManeuverData(data);

        // since distance and icon are correct, panel should be visible.
        assertEquals(containerView.getVisibility(), View.VISIBLE);

        // verify the data
        assertThat(distanceView.getText(), is(equalTo(mDistance + " m")));
        assertThat(streetName.getText(), is(equalTo(mStreetName)));
    }

    @Test
    public void testSettingDataReturnsTheSame() {
        GuidanceNextManeuverData data = createData(mIconId, mDistance, mStreetName);
        mGuidanceNextManeuverPanel.setNextManeuverData(data);
        assertThat(mGuidanceNextManeuverPanel.getNextManeuverData(), equalTo(data));
    }

    @Test
    public void settingDataNullShouldNotThrowException() {
        mGuidanceNextManeuverPanel.setNextManeuverData(null);
        assertNull(mGuidanceNextManeuverPanel.getNextManeuverData());
    }

    @Test
    public void testDataIsNotLostWhileRecreatingActivity() {
        mGuidanceNextManeuverPanel.setNextManeuverData(createData(mIconId, mDistance, mStreetName));
        assertNotNull(mGuidanceNextManeuverPanel.getNextManeuverData());
        final FragmentActivity activity = getFragmentActivity();
        mGuidanceNextManeuverPanel.setId(R.id.vertical_guideline);
        activity.setContentView(mGuidanceNextManeuverPanel);
        activity.recreate();
        assertNotNull(mGuidanceNextManeuverPanel.getNextManeuverData());
    }

    @Test
    public void testPanelDataIsParcelable() {

        final GuidanceNextManeuverData data = createData(mIconId, mDistance, mStreetName);
        GuidanceNextManeuverPanel.SavedState savedState = new GuidanceNextManeuverPanel.SavedState(AbsSavedState.EMPTY_STATE);
        savedState.setStateToSave(data);
        Parcel parcel = Parcel.obtain();
        savedState.writeToParcel(parcel, savedState.describeContents());
        parcel.setDataPosition(0);

        GuidanceNextManeuverPanel.SavedState createdFromParcel = GuidanceNextManeuverPanel.SavedState.CREATOR.createFromParcel(
                parcel);
        assertNotNull(createdFromParcel.getSavedState());
    }

    private GuidanceNextManeuverData createData(int iconId, long distance, String streetName) {
        return new GuidanceNextManeuverData(iconId, distance, streetName);
    }
}
