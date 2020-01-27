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

package com.here.msdkui.guidance;

import android.os.Parcel;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import android.view.AbsSavedState;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.android.controller.ActivityController;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link GuidanceNextManeuverView}.
 */
public class GuidanceNextManeuverViewTest extends RobolectricTest {

    private GuidanceNextManeuverView mGuidanceNextManeuverView;
    private final int mIconId = R.drawable.ic_maneuver_icon_0;
    private final long mDistance = 100;
    private final String mStreetName = "SomeStreet";

    @Before
    public void setUp() {
        super.setUp();
        mGuidanceNextManeuverView = new GuidanceNextManeuverView(getApplicationContext());
    }

    @Test
    public void testUIInit() {

        final TextView distanceView = mGuidanceNextManeuverView.findViewById(R.id.nextManeuverDistance);
        final TextView streetName = mGuidanceNextManeuverView.findViewById(R.id.afterNextManeuverStreetName);

        assertEquals(View.GONE, mGuidanceNextManeuverView.getVisibility());

        GuidanceNextManeuverData data = createData(mIconId, mDistance, mStreetName);
        // create proper data to see if everything is fine.
        mGuidanceNextManeuverView.setNextManeuverData(data);

        // since distance and icon are correct, panel should be visible.
        assertEquals(mGuidanceNextManeuverView.getVisibility(), View.VISIBLE);

        // verify the data
        assertThat(distanceView.getText(), is(equalTo(mDistance + " m")));
        assertThat(streetName.getText(), is(equalTo(mStreetName)));
    }

    @Test
    public void testUIInitWithDifferentConstructor() {

        mGuidanceNextManeuverView = new GuidanceNextManeuverView(getApplicationContext(), null, 0, 0);
        final TextView distanceView = mGuidanceNextManeuverView.findViewById(R.id.nextManeuverDistance);
        final TextView streetName = mGuidanceNextManeuverView.findViewById(R.id.afterNextManeuverStreetName);

        assertEquals(View.GONE, mGuidanceNextManeuverView.getVisibility());

        GuidanceNextManeuverData data = createData(mIconId, mDistance, mStreetName);
        // create proper data to see if everything is fine.
        mGuidanceNextManeuverView.setNextManeuverData(data);

        // since distance and icon are correct, panel should be visible.
        assertEquals(mGuidanceNextManeuverView.getVisibility(), View.VISIBLE);

        // verify the data
        assertThat(distanceView.getText(), is(equalTo(mDistance + " m")));
        assertThat(streetName.getText(), is(equalTo(mStreetName)));
    }

    @Test
    public void testUIInitWithNullForFieldVisibility() {
        final TextView distanceView = mGuidanceNextManeuverView.findViewById(R.id.nextManeuverDistance);
        final TextView streetName = mGuidanceNextManeuverView.findViewById(R.id.afterNextManeuverStreetName);
        final TextView dotView = mGuidanceNextManeuverView.findViewById(R.id.dot);

        assertEquals(View.GONE, mGuidanceNextManeuverView.getVisibility());

        GuidanceNextManeuverData data = createData(null, null, null);
        // create proper data to see if everything is fine.
        mGuidanceNextManeuverView.setNextManeuverData(data);

        // since distance and icon are correct, panel should be visible.
        assertEquals(mGuidanceNextManeuverView.getVisibility(), View.VISIBLE);

        // verify the data
        assertEquals(View.GONE, distanceView.getVisibility());
        assertEquals(View.GONE, streetName.getVisibility());
        assertEquals(View.GONE, dotView.getVisibility());
    }

    @Test
    public void testIconEndMargin() {
        mGuidanceNextManeuverView.setIconEndMargin(10);
        final ImageView iconView = mGuidanceNextManeuverView.findViewById(R.id.nextManeuverIconView);
        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
                iconView.getLayoutParams();
        assertThat(layoutParams.getMarginEnd(), equalTo(10));
    }

    @Test
    public void testSettingDataReturnsTheSame() {
        GuidanceNextManeuverData data = createData(mIconId, mDistance, mStreetName);
        mGuidanceNextManeuverView.setNextManeuverData(data);
        assertThat(mGuidanceNextManeuverView.getNextManeuverData(), equalTo(data));
    }

    @Test
    public void settingDataNullShouldNotThrowException() {
        mGuidanceNextManeuverView.setNextManeuverData(null);
        assertNull(mGuidanceNextManeuverView.getNextManeuverData());
    }

    @Test
    public void testDataIsNotLostWhileRecreatingActivity() {
        mGuidanceNextManeuverView.setNextManeuverData(createData(mIconId, mDistance, mStreetName));
        assertNotNull(mGuidanceNextManeuverView.getNextManeuverData());
        final ActivityController<FragmentActivity> activityController = getActivityController();
        mGuidanceNextManeuverView.setId(1);
        activityController.get().setContentView(mGuidanceNextManeuverView);
        activityController.recreate();
        assertNotNull(mGuidanceNextManeuverView.getNextManeuverData());
    }

    @Test
    public void testPanelDataIsParcelable() {
        final GuidanceNextManeuverData data = createData(mIconId, mDistance, mStreetName);
        GuidanceNextManeuverView.SavedState savedState = new GuidanceNextManeuverView.SavedState(AbsSavedState.EMPTY_STATE);
        savedState.setStateToSave(data);
        Parcel parcel = Parcel.obtain();
        savedState.writeToParcel(parcel, savedState.describeContents());
        parcel.setDataPosition(0);

        GuidanceNextManeuverView.SavedState createdFromParcel = GuidanceNextManeuverView.SavedState.CREATOR.createFromParcel(
                parcel);
        assertNotNull(createdFromParcel.getSavedState());
    }


    private GuidanceNextManeuverData createData(Integer iconId, Long distance, String streetName) {
        return new GuidanceNextManeuverData(iconId, distance, streetName);
    }
}
