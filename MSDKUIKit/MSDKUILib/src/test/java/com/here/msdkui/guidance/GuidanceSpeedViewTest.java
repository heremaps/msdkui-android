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
import androidx.fragment.app.FragmentActivity;
import android.view.AbsSavedState;
import android.view.View;
import android.widget.TextView;

import com.here.RobolectricTest;
import com.here.msdkui.R;
import com.here.msdkui.common.SpeedFormatterUtil;
import com.here.msdkui.common.measurements.UnitSystem;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.android.controller.ActivityController;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Tests for class {@link GuidanceSpeedView}.
 */
public class GuidanceSpeedViewTest extends RobolectricTest {

    private static final double VELOCITY = 60;
    private static final double SPEED_LIMIT = 50;
    private static final int RED_COLOR = 16711680;
    private static final int BLUE_COLOR = 255;

    private GuidanceSpeedView mCurrentGuidanceSpeedView = null;

    @Before
    public void setUp() {
        super.setUp();
        mCurrentGuidanceSpeedView = new GuidanceSpeedView(getApplicationContext());
    }

    @Test
    public void testUiInitWithDifferentConstructor() {
        mCurrentGuidanceSpeedView = new GuidanceSpeedView(getApplicationContext(), null, 0, 0);
        assertThat(mCurrentGuidanceSpeedView.getVisibility(), is(View.VISIBLE));
    }

    @Test
    public void testUiInit() {
        final View container = mCurrentGuidanceSpeedView.findViewById(R.id.guidance_current_speed_container);
        final TextView speedValueView = mCurrentGuidanceSpeedView.findViewById(R.id.guidance_current_speed_value);
        final TextView speedUnitView = mCurrentGuidanceSpeedView.findViewById(R.id.guidance_current_speed_unit);

        assertThat(container.getVisibility(), is(View.VISIBLE));
        assertThat(speedValueView.getText().toString(), is(getString(R.string.msdkui_value_not_available)));
        assertThat(speedUnitView.getText().toString(), is(""));
        GuidanceSpeedData data = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
        mCurrentGuidanceSpeedView.setCurrentSpeedData(data);
        assertThat(speedValueView.getText().toString(),
                is(String.valueOf(SpeedFormatterUtil.format(VELOCITY, UnitSystem.METRIC))));
    }

    @Test
    public void testColorSetting() {
        //set different colors
        mCurrentGuidanceSpeedView.setValueTextColor(0);
        mCurrentGuidanceSpeedView.setUnitTextColor(0);
        mCurrentGuidanceSpeedView.setValueTextColor(RED_COLOR);
        mCurrentGuidanceSpeedView.setUnitTextColor(BLUE_COLOR);

        assertThat(mCurrentGuidanceSpeedView.getValueTextColor(), is(RED_COLOR));
        assertThat(mCurrentGuidanceSpeedView.getUnitTextColor(), is(BLUE_COLOR));

        //set same colors
        mCurrentGuidanceSpeedView.setValueTextColor(RED_COLOR);
        mCurrentGuidanceSpeedView.setUnitTextColor(BLUE_COLOR);

        assertThat(mCurrentGuidanceSpeedView.getValueTextColor(), is(RED_COLOR));
        assertThat(mCurrentGuidanceSpeedView.getUnitTextColor(), is(BLUE_COLOR));
    }

    @Test
    public void testUiWhenDataIsNull() {
        mCurrentGuidanceSpeedView.setCurrentSpeedData(null);
        final View container = mCurrentGuidanceSpeedView.findViewById(R.id.guidance_current_speed_container);
        assertThat(container.getVisibility(), is(View.VISIBLE));
    }

    @Test
    public void testSetterAndGetter() {
        GuidanceSpeedData data = new GuidanceSpeedData(10.0, 10.0);
        mCurrentGuidanceSpeedView.setCurrentSpeedData(data);
        assertThat(mCurrentGuidanceSpeedView.getCurrentSpeedData(), is(data));
    }

    @Test
    public void testSettingNullDataWontCrash() {
        mCurrentGuidanceSpeedView.setCurrentSpeedData(null);
        assertNull(mCurrentGuidanceSpeedView.getCurrentSpeedData());
    }

    @Test
    public void testDataIsNotLostWhileRecreatingActivity() {
        final GuidanceSpeedData data = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
        final ActivityController<FragmentActivity> activityController = getActivityController();

        // when data is not null
        mCurrentGuidanceSpeedView.setCurrentSpeedData(data);
        assertNotNull(mCurrentGuidanceSpeedView.getCurrentSpeedData());
        mCurrentGuidanceSpeedView.setId(1);
        activityController.get().setContentView(mCurrentGuidanceSpeedView);
        activityController.recreate();
        assertNotNull(mCurrentGuidanceSpeedView.getCurrentSpeedData());

        // when data is null
        mCurrentGuidanceSpeedView.setCurrentSpeedData(null);
        assertNull(mCurrentGuidanceSpeedView.getCurrentSpeedData());
        mCurrentGuidanceSpeedView.setId(1);
        activityController.recreate();
        assertNull(mCurrentGuidanceSpeedView.getCurrentSpeedData());
    }

    @Test
    public void testViewDataIsParcelable() {
        final GuidanceSpeedData data = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
        GuidanceSpeedView.SavedState savedState = new GuidanceSpeedView.SavedState(
                AbsSavedState.EMPTY_STATE);
        savedState.setStateToSave(data, RED_COLOR, BLUE_COLOR);
        Parcel parcel = Parcel.obtain();
        savedState.writeToParcel(parcel, savedState.describeContents());
        parcel.setDataPosition(0);

        GuidanceSpeedView.SavedState createdFromParcel = GuidanceSpeedView.SavedState.CREATOR.createFromParcel(
                parcel);
        assertNotNull(createdFromParcel.getSavedSpeedData());
        assertThat(createdFromParcel.getSavedValueTextColor(), is(RED_COLOR));
        assertThat(createdFromParcel.getSavedUnitTextColor(), is(BLUE_COLOR));
    }
}
