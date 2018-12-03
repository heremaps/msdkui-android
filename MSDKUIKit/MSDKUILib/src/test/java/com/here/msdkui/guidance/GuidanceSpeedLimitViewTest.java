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
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for class {@link GuidanceSpeedLimitView}.
 */
public class GuidanceSpeedLimitViewTest extends RobolectricTest {

    private static final int VELOCITY = 60;
    private static final int SPEED_LIMIT = 50;

    private GuidanceSpeedLimitView mSpeedLimitPanel = null;

    @Before
    public void setUp() {
        super.setUp();
        mSpeedLimitPanel = new GuidanceSpeedLimitView(getApplicationContext());
    }

    @Test
    public void testUiInit() {
        final View container = mSpeedLimitPanel.findViewById(R.id.guidance_speed_limit_container);
        assertThat(container.getVisibility(), is(View.GONE));
    }

    @Test
    public void testUi() {
        final View container = mSpeedLimitPanel.findViewById(R.id.guidance_speed_limit_container);
        final TextView speedLimitView = mSpeedLimitPanel.findViewById(R.id.speed_limit);
        GuidanceSpeedData data;

        mSpeedLimitPanel.setCurrentSpeedData(null);
        assertThat(container.getVisibility(), is(View.GONE));

        data = new GuidanceSpeedData(45, 50);
        mSpeedLimitPanel.setCurrentSpeedData(data);
        assertThat(container.getVisibility(), is(View.VISIBLE));
        assertThat(speedLimitView.getText().toString(), is(String.valueOf(50)));

        data = new GuidanceSpeedData(60, 50);
        mSpeedLimitPanel.setCurrentSpeedData(data);
        assertThat(container.getVisibility(), is(View.VISIBLE));
        assertThat(speedLimitView.getText().toString(), is(String.valueOf(50)));

        data = new GuidanceSpeedData(150, 0);
        mSpeedLimitPanel.setCurrentSpeedData(data);
        assertThat(container.getVisibility(), is(View.GONE));

        data = new GuidanceSpeedData(45, 50);
        mSpeedLimitPanel.setCurrentSpeedData(data);
        assertThat(container.getVisibility(), is(View.VISIBLE));
        assertThat(speedLimitView.getText().toString(), is(String.valueOf(50)));
    }

    @Test
    public void testSetterAndGetter() {
        GuidanceSpeedData data = new GuidanceSpeedData(10, 10);
        mSpeedLimitPanel.setCurrentSpeedData(data);
        assertThat(mSpeedLimitPanel.getCurrentSpeedData(), is(data));
    }

    @Test
    public void testSettingNullDataWontCrash() {
        mSpeedLimitPanel.setCurrentSpeedData(null);
        assertThat(mSpeedLimitPanel.getCurrentSpeedData().isValid(), is(false));
    }

    @Test
    public void testDataIsNotLostWhileRecreatingActivity() {
        final GuidanceSpeedData data = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
        final FragmentActivity activity = getFragmentActivity();

        // when data is not null
        mSpeedLimitPanel.setCurrentSpeedData(data);
        assertNotNull(mSpeedLimitPanel.getCurrentSpeedData());
        mSpeedLimitPanel.setId(R.id.vertical_guideline);
        activity.setContentView(mSpeedLimitPanel);
        activity.recreate();
        assertNotNull(mSpeedLimitPanel.getCurrentSpeedData());

        // when data is null
        mSpeedLimitPanel.setCurrentSpeedData(null);
        assertThat(mSpeedLimitPanel.getCurrentSpeedData().isValid(), is(false));
        mSpeedLimitPanel.setId(R.id.vertical_guideline);
        activity.setContentView(mSpeedLimitPanel);
        activity.recreate();
        assertThat(mSpeedLimitPanel.getCurrentSpeedData().isValid(), is(false));
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
