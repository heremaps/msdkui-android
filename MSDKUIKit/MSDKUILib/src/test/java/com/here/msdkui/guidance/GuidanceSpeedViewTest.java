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
 * Tests for class {@link GuidanceSpeedView}.
 */
public class GuidanceSpeedViewTest extends RobolectricTest {

    private static final int VELOCITY = 60;
    private static final int SPEED_LIMIT = 50;
    private static final int RED_COLOR = 16711680;
    private static final int BLUE_COLOR = 255;

    private GuidanceSpeedView mCurrentSpeedPanel = null;

    @Before
    public void setUp() {
        super.setUp();
        mCurrentSpeedPanel = new GuidanceSpeedView(getApplicationContext());
    }

    @Test
    public void testUiInit() {
        final View container = mCurrentSpeedPanel.findViewById(R.id.guidance_current_speed_container);
        final TextView speedValueView = mCurrentSpeedPanel.findViewById(R.id.guidance_current_speed_value);
        final TextView speedUnitView = mCurrentSpeedPanel.findViewById(R.id.guidance_current_speed_unit);

        assertThat(container.getVisibility(), is(View.VISIBLE));
        assertThat(speedValueView.getText().toString(), is(getString(R.string.msdkui_value_not_available)));
        assertThat(speedUnitView.getText().toString(),
                is(getApplicationContext().getResources().getString(R.string.msdkui_unit_km_per_h)));
        GuidanceSpeedData data = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
        mCurrentSpeedPanel.setCurrentSpeedData(data);
        assertThat(speedValueView.getText().toString(), is(String.valueOf(VELOCITY)));
    }

    @Test
    public void testColorSetting() {
        //set different colors
        mCurrentSpeedPanel.setValueTextColor(0);
        mCurrentSpeedPanel.setUnitTextColor(0);
        mCurrentSpeedPanel.setValueTextColor(RED_COLOR);
        mCurrentSpeedPanel.setUnitTextColor(BLUE_COLOR);

        assertThat(mCurrentSpeedPanel.getValueTextColor(), is(RED_COLOR));
        assertThat(mCurrentSpeedPanel.getUnitTextColor(), is(BLUE_COLOR));

        //set same colors
        mCurrentSpeedPanel.setValueTextColor(RED_COLOR);
        mCurrentSpeedPanel.setUnitTextColor(BLUE_COLOR);

        assertThat(mCurrentSpeedPanel.getValueTextColor(), is(RED_COLOR));
        assertThat(mCurrentSpeedPanel.getUnitTextColor(), is(BLUE_COLOR));
    }

    @Test
    public void testUiWhenDataIsNull() {
        mCurrentSpeedPanel.setCurrentSpeedData(null);
        final View container = mCurrentSpeedPanel.findViewById(R.id.guidance_current_speed_container);
        assertThat(container.getVisibility(), is(View.VISIBLE));
    }

    @Test
    public void testSetterAndGetter() {
        GuidanceSpeedData data = new GuidanceSpeedData(10, 10);
        mCurrentSpeedPanel.setCurrentSpeedData(data);
        assertThat(mCurrentSpeedPanel.getCurrentSpeedData(), is(data));
    }

    @Test
    public void testSettingNullDataWontCrash() {
        mCurrentSpeedPanel.setCurrentSpeedData(null);
        assertThat(mCurrentSpeedPanel.getCurrentSpeedData().isValid(), is(false));
    }

    @Test
    public void testDataIsNotLostWhileRecreatingActivity() {
        final GuidanceSpeedData data = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
        final FragmentActivity activity = getFragmentActivity();

        // when data is not null
        mCurrentSpeedPanel.setCurrentSpeedData(data);
        assertNotNull(mCurrentSpeedPanel.getCurrentSpeedData());
        mCurrentSpeedPanel.setId(R.id.vertical_guideline);
        activity.setContentView(mCurrentSpeedPanel);
        activity.recreate();
        assertNotNull(mCurrentSpeedPanel.getCurrentSpeedData());

        // when data is null
        mCurrentSpeedPanel.setCurrentSpeedData(null);
        assertThat(mCurrentSpeedPanel.getCurrentSpeedData().isValid(), is(false));
        mCurrentSpeedPanel.setId(R.id.vertical_guideline);
        activity.setContentView(mCurrentSpeedPanel);
        activity.recreate();
        assertThat(mCurrentSpeedPanel.getCurrentSpeedData().isValid(), is(false));
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
