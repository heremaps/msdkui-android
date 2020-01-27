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
import com.here.msdkui.common.DateFormatterUtil;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.android.controller.ActivityController;

import java.util.Date;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link GuidanceEstimatedArrivalView}.
 */
public class GuidanceEstimatedArrivalViewTest extends RobolectricTest {
    private static final Date ETA_DATE = new Date(1535016978000L);
    private static final long DISTANCE = 10320;
    private static final int DURATION = 123;

    private GuidanceEstimatedArrivalView mEstimatedArrivalView;

    @Before
    public void setUp() {
        super.setUp();
        mEstimatedArrivalView = new GuidanceEstimatedArrivalView(getApplicationContext());
    }

    @Test
    public void testUIInit() {
        final TextView etaView = mEstimatedArrivalView.findViewById(R.id.eta);
        final TextView distanceView = mEstimatedArrivalView.findViewById(R.id.distance);
        final TextView durationView = mEstimatedArrivalView.findViewById(R.id.duration);
        final TextView dotView = mEstimatedArrivalView.findViewById(R.id.dot);

        assertThat(etaView.getText().toString(), is(getString(R.string.msdkui_value_not_available)));
        assertThat(distanceView.getText().toString(), is(getString(R.string.msdkui_value_not_available)));
        assertThat(durationView.getText().toString(), is(getString(R.string.msdkui_value_not_available)));
        assertThat(dotView.getText().toString(), is(getString(R.string.msdkui_bullet)));

        GuidanceEstimatedArrivalViewData data = new GuidanceEstimatedArrivalViewData(ETA_DATE, DISTANCE, DURATION);
        // create proper data to see if everything is fine.
        mEstimatedArrivalView.setEstimatedArrivalData(data);

        // since no data is null, all view should be visible.
        assertEquals(etaView.getVisibility(), View.VISIBLE);
        assertEquals(distanceView.getVisibility(), View.VISIBLE);
        assertEquals(durationView.getVisibility(), View.VISIBLE);
        assertEquals(dotView.getVisibility(), View.VISIBLE);

        // verify the data
        assertThat(etaView.getText(), is(equalTo(DateFormatterUtil.format(getApplicationContext(), ETA_DATE))));
        assertThat(distanceView.getText(), is(equalTo("10 km")));
        assertThat(durationView.getText(), is("2 min"));
    }

    @Test
    public void setNullSetting() {
        mEstimatedArrivalView.setEstimatedArrivalData(null);
        assertThat(mEstimatedArrivalView.getVisibility(), is(View.GONE));
    }

    @Test
    public void testUIWhenDataIsInvalid() {
        final TextView eta = mEstimatedArrivalView.findViewById(R.id.eta);
        final TextView distance = mEstimatedArrivalView.findViewById(R.id.distance);
        final TextView duration = mEstimatedArrivalView.findViewById(R.id.duration);

        GuidanceEstimatedArrivalViewData data = new GuidanceEstimatedArrivalViewData(null, null, null);
        mEstimatedArrivalView.setEstimatedArrivalData(data);

        assertEquals(eta.getVisibility(), View.VISIBLE);
        assertEquals(distance.getVisibility(), View.VISIBLE);
        assertEquals(duration.getVisibility(), View.VISIBLE);

        assertThat(eta.getText().toString(), is(getString(R.string.msdkui_value_not_available)));
        assertThat(distance.getText().toString(), is(getString(R.string.msdkui_value_not_available)));
        assertThat(duration.getText().toString(), is(getString(R.string.msdkui_value_not_available)));
    }

    @Test
    public void testSettingDataReturnsTheSame() {
        final GuidanceEstimatedArrivalViewData data = new GuidanceEstimatedArrivalViewData(ETA_DATE, DISTANCE, DURATION);
        mEstimatedArrivalView.setEstimatedArrivalData(data);
        assertThat(mEstimatedArrivalView.getEstimatedArrivalData(), equalTo(data));
    }

    @Test
    public void testDataIsNotLostWhileRecreatingActivity() {
        final GuidanceEstimatedArrivalViewData data = new GuidanceEstimatedArrivalViewData(ETA_DATE, DISTANCE, DURATION);
        final ActivityController<FragmentActivity> activityController = getActivityController();

        // when data is not null
        mEstimatedArrivalView.setEstimatedArrivalData(data);
        assertNotNull(mEstimatedArrivalView.getEstimatedArrivalData());
        mEstimatedArrivalView.setId(1);
        activityController.get().setContentView(mEstimatedArrivalView);
        activityController.recreate();
        assertNotNull(mEstimatedArrivalView.getEstimatedArrivalData());
    }

    @Test
    public void testViewDataIsParcelable() {
        final GuidanceEstimatedArrivalViewData data = new GuidanceEstimatedArrivalViewData(ETA_DATE, DISTANCE, DURATION);
        GuidanceEstimatedArrivalView.SavedState savedState = new GuidanceEstimatedArrivalView.SavedState(
                AbsSavedState.EMPTY_STATE);
        savedState.setStateToSave(data);
        Parcel parcel = Parcel.obtain();
        savedState.writeToParcel(parcel, savedState.describeContents());
        parcel.setDataPosition(0);

        GuidanceEstimatedArrivalView.SavedState createdFromParcel = GuidanceEstimatedArrivalView.SavedState.CREATOR.createFromParcel(
                parcel);
        assertNotNull(createdFromParcel.getSavedState());
    }
}
