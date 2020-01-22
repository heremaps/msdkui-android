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
 * Tests for class {@link GuidanceSpeedLimitView}.
 */
public class GuidanceSpeedLimitViewTest extends RobolectricTest {

    private static final double VELOCITY = 60;
    private static final double SPEED_LIMIT = 50;

    private GuidanceSpeedLimitView mGuidanceSpeedLimitView = null;

    @Before
    public void setUp() {
        super.setUp();
        mGuidanceSpeedLimitView = new GuidanceSpeedLimitView(getApplicationContext());
    }

    @Test
    public void testUiInit() {
        assertThat(mGuidanceSpeedLimitView.getVisibility(), is(View.GONE));
    }

    @Test
    public void testUiInitWithDifferentConstructor() {
        mGuidanceSpeedLimitView = new GuidanceSpeedLimitView(getApplicationContext(), null, 0, 0);
        assertThat(mGuidanceSpeedLimitView.getVisibility(), is(View.GONE));
    }

    @Test
    public void testUi() {
        final TextView speedLimitView = mGuidanceSpeedLimitView.findViewById(R.id.speed_limit);
        GuidanceSpeedData data;

        mGuidanceSpeedLimitView.setCurrentSpeedData(null);
        assertThat(mGuidanceSpeedLimitView.getVisibility(), is(View.GONE));

        data = new GuidanceSpeedData(45.0, SPEED_LIMIT);
        mGuidanceSpeedLimitView.setCurrentSpeedData(data);
        assertThat(mGuidanceSpeedLimitView.getVisibility(), is(View.VISIBLE));
        assertThat(speedLimitView.getText().toString(),
                is(String.valueOf(SpeedFormatterUtil.format(50, UnitSystem.METRIC))));

        data = new GuidanceSpeedData(60.0, SPEED_LIMIT);
        mGuidanceSpeedLimitView.setCurrentSpeedData(data);
        assertThat(mGuidanceSpeedLimitView.getVisibility(), is(View.VISIBLE));
        assertThat(speedLimitView.getText().toString(),
                is(String.valueOf(SpeedFormatterUtil.format(50, UnitSystem.METRIC))));

        data = new GuidanceSpeedData(150.0, 0.0);
        mGuidanceSpeedLimitView.setCurrentSpeedData(data);
        assertThat(mGuidanceSpeedLimitView.getVisibility(), is(View.GONE));

        data = new GuidanceSpeedData(45.0, SPEED_LIMIT);
        mGuidanceSpeedLimitView.setCurrentSpeedData(data);
        assertThat(mGuidanceSpeedLimitView.getVisibility(), is(View.VISIBLE));
        assertThat(speedLimitView.getText().toString(),
                is(String.valueOf(SpeedFormatterUtil.format(50, UnitSystem.METRIC))));
    }

    @Test
    public void testSetterAndGetter() {
        GuidanceSpeedData data = new GuidanceSpeedData(10.0, 10.0);
        mGuidanceSpeedLimitView.setCurrentSpeedData(data);
        assertThat(mGuidanceSpeedLimitView.getCurrentSpeedData(), is(data));
    }

    @Test
    public void testDataIsNotLostWhileRecreatingActivity() {
        final GuidanceSpeedData data = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
        final ActivityController<FragmentActivity> activityController = getActivityController();

        // when data is not null
        mGuidanceSpeedLimitView.setCurrentSpeedData(data);
        assertNotNull(mGuidanceSpeedLimitView.getCurrentSpeedData());
        mGuidanceSpeedLimitView.setId(1);
        activityController.get().setContentView(mGuidanceSpeedLimitView);
        activityController.recreate();
        assertNotNull(mGuidanceSpeedLimitView.getCurrentSpeedData());

        // when data is null
        mGuidanceSpeedLimitView.setCurrentSpeedData(null);
        assertNull(mGuidanceSpeedLimitView.getCurrentSpeedData());
        mGuidanceSpeedLimitView.setId(1);
        activityController.recreate();
        assertNull(mGuidanceSpeedLimitView.getCurrentSpeedData());
    }

    @Test
    public void testViewDataIsParcelable() {
        final GuidanceSpeedData data = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
        GuidanceSpeedLimitView.SavedState savedState = new GuidanceSpeedLimitView.SavedState(AbsSavedState.EMPTY_STATE);
        savedState.setStateToSave(data);
        Parcel parcel = Parcel.obtain();
        savedState.writeToParcel(parcel, savedState.describeContents());
        parcel.setDataPosition(0);

        GuidanceSpeedLimitView.SavedState createdFromParcel = GuidanceSpeedLimitView.SavedState.CREATOR.createFromParcel(
                parcel);
        assertNotNull(createdFromParcel.getSavedState());
    }
}
