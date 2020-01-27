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

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import androidx.fragment.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.AbsSavedState;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link GuidanceManeuverView}.
 */
public class GuidanceManeuverViewTest extends RobolectricTest {

    private GuidanceManeuverView mGuidanceManeuverView;
    private final int mIconId = R.drawable.ic_maneuver_icon_0;
    private final long mDistance = 100;
    private final String mInfo1 = "Exit";
    private final String mInfo2 = "Str";

    @Before
    public void setUp() {
        super.setUp();
        mGuidanceManeuverView = new GuidanceManeuverView(getApplicationContext());
    }

    @Test
    public void testUIInit() {

        // since there is no data, only info 1 shows string
        final TextView infoView1 =  mGuidanceManeuverView.findViewById(R.id.infoView1);
        final TextView distanceView =  mGuidanceManeuverView.findViewById(R.id.distanceView);
        final TextView infoView2 =  mGuidanceManeuverView.findViewById(R.id.infoView2);
        final ImageView iconView =  mGuidanceManeuverView.findViewById(R.id.maneuverIconView);
        final ProgressBar busyProgressBar =  mGuidanceManeuverView.findViewById(
                R.id.busyStateProgressBar);
        final TextView defaultView = mGuidanceManeuverView.findViewById(R.id.defaultViewText);

        assertEquals(defaultView.getVisibility(), View.VISIBLE);
        assertThat(defaultView.getText().toString(), is(getString(R.string.msdkui_maneuverpanel_nodata)));

        GuidanceManeuverData data = createData(mIconId, mDistance, mInfo1, mInfo2);
        // create proper data to see if everything is fine.
        mGuidanceManeuverView.setViewState(new GuidanceManeuverView.State(data));
        assertNotNull(mGuidanceManeuverView.getViewState().getData());

        // since no data is null, all view should be visible.
        assertEquals(distanceView.getVisibility(), View.VISIBLE);
        assertEquals(infoView1.getVisibility(), View.VISIBLE);
        assertEquals(infoView2.getVisibility(), View.VISIBLE);
        assertEquals(iconView.getVisibility(), View.VISIBLE);
        // but progress bar should be gone
        assertEquals(busyProgressBar.getVisibility(), View.GONE);

        // verify the data
        assertThat(distanceView.getText(), is(equalTo(mDistance + " m")));
        assertThat(infoView1.getText(), is(equalTo(mInfo1)));
        assertThat(infoView2.getText(), is(equalTo(mInfo2)));
    }

    @Test
    public void testUiWithAttributeSet() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.viewMode, "0")
                .build();
        mGuidanceManeuverView = new GuidanceManeuverView(getApplicationContext(), attributeSet);
        testUIInit();
        attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.viewMode, "1")
                .build();
        mGuidanceManeuverView = new GuidanceManeuverView(getApplicationContext(), attributeSet);
        testUIInit();
        attributeSet = Robolectric.buildAttributeSet()
                .build();
        mGuidanceManeuverView = new GuidanceManeuverView(getApplicationContext(), attributeSet);
        testUIInit();
    }

    @Test
    public void testWhenStateIsNull() {
        mGuidanceManeuverView.setViewState(null);
        assertEquals(mGuidanceManeuverView.getVisibility(), View.GONE);
        assertNull(mGuidanceManeuverView.getViewState());
    }

    @Test
    public void testWhenManeuverForRouteRecalculation() {
        mGuidanceManeuverView.setViewState(GuidanceManeuverView.State.UPDATING);
        final TextView distanceView = (TextView) mGuidanceManeuverView.findViewById(R.id.distanceView);
        final TextView infoView1 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView1);
        final TextView infoView2 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView2);
        final ImageView iconView = (ImageView) mGuidanceManeuverView.findViewById(R.id.maneuverIconView);
        final ProgressBar busyProgressBar = (ProgressBar) mGuidanceManeuverView.findViewById(
                R.id.busyStateProgressBar);
        final TextView defaultView = mGuidanceManeuverView.findViewById(R.id.defaultViewText);

        // null data should be hidden.
        assertEquals(distanceView.getVisibility(), View.GONE);
        assertEquals(infoView1.getVisibility(), View.GONE);
        assertEquals(defaultView.getVisibility(), View.VISIBLE);
        assertThat(defaultView.getText().toString(), is(getString(R.string.msdkui_maneuverpanel_updating)));
        assertEquals(infoView2.getVisibility(), View.GONE);
        assertEquals(iconView.getVisibility(), View.INVISIBLE);
        assertEquals(busyProgressBar.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testExtraIconView() {
        final ImageView extraIconView = (ImageView) mGuidanceManeuverView.findViewById(R.id.extraIconView);
        assertEquals(extraIconView.getVisibility(), View.GONE);
        GuidanceManeuverData data = new GuidanceManeuverData(-1, -1L, "", "", Mockito.mock(Bitmap.class));
        mGuidanceManeuverView.setViewState(new GuidanceManeuverView.State(data));
        assertEquals(extraIconView.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testUIWhenSomeDataIsNull() {
        GuidanceManeuverData data = createData(0, null, null, null);
        // create proper data to see if everything is fine.
        mGuidanceManeuverView.setViewState(new GuidanceManeuverView.State(data));

        final TextView distanceView = (TextView) mGuidanceManeuverView.findViewById(R.id.distanceView);
        final TextView infoView1 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView1);
        final TextView infoView2 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView2);
        final ImageView iconView = (ImageView) mGuidanceManeuverView.findViewById(R.id.maneuverIconView);

        // null data should be hidden.
        assertEquals(distanceView.getVisibility(), View.GONE);
        assertEquals(infoView1.getVisibility(), View.GONE);
        assertEquals(infoView2.getVisibility(), View.GONE);
        assertEquals(iconView.getVisibility(), View.GONE);
    }

    @Test
    public void testSettingDataReturnsTheSame() {
        GuidanceManeuverData data = createData(mIconId, mDistance, mInfo1, mInfo2);
        mGuidanceManeuverView.setViewState(new GuidanceManeuverView.State(data));
        assertThat(mGuidanceManeuverView.getManeuverData(), equalTo(data));
    }

    @Test
    public void testHighLightManeuver() {
        mGuidanceManeuverView.highLightManeuver(Color.RED);
        assertThat(((TextView) mGuidanceManeuverView.findViewById(R.id.infoView2)).getCurrentTextColor(),
                is(Color.RED));
    }

    @Test
    public void testDataIsNotLostWhileRecreatingActivity() {
        mGuidanceManeuverView.setViewState(new GuidanceManeuverView.State(createData(mIconId, mDistance, mInfo1, mInfo2)));
        assertNotNull(mGuidanceManeuverView.getManeuverData());
        final ActivityController<FragmentActivity> activityController = getActivityController();
        mGuidanceManeuverView.setId(1);
        activityController.get().setContentView(mGuidanceManeuverView);
        activityController.recreate();
        assertNotNull(mGuidanceManeuverView.getManeuverData());
    }

    @Test
    public void testPanelDataIsParcelable() {
        final GuidanceManeuverData data = createData(mIconId, mDistance, mInfo1, mInfo2);
        GuidanceManeuverView.SavedState savedState = new GuidanceManeuverView.SavedState(AbsSavedState.EMPTY_STATE);
        savedState.setViewState(new GuidanceManeuverView.State(data));
        Parcel parcel = Parcel.obtain();
        savedState.writeToParcel(parcel, savedState.describeContents());
        parcel.setDataPosition(0);

        GuidanceManeuverView.SavedState createdFromParcel = GuidanceManeuverView.SavedState.CREATOR.createFromParcel(
                parcel);
        assertNotNull(createdFromParcel.getViewState());
    }

    @Test
    public void testPanelNoDataIsParcelable() {
        GuidanceManeuverView.SavedState savedState = new GuidanceManeuverView.SavedState(AbsSavedState.EMPTY_STATE);
        savedState.setViewState(GuidanceManeuverView.State.NO_DATA);
        Parcel parcel = Parcel.obtain();
        savedState.writeToParcel(parcel, savedState.describeContents());
        parcel.setDataPosition(0);

        GuidanceManeuverView.SavedState createdFromParcel = GuidanceManeuverView.SavedState.CREATOR.createFromParcel(
                parcel);
        assertNotNull(createdFromParcel.getViewState());
        assertThat(createdFromParcel.getViewState(), equalTo(GuidanceManeuverView.State.NO_DATA));
    }

    @Test
    public void testPanelUpdatingStateIsParcelable() {
        GuidanceManeuverView.SavedState savedState = new GuidanceManeuverView.SavedState(AbsSavedState.EMPTY_STATE);
        savedState.setViewState(GuidanceManeuverView.State.UPDATING);
        Parcel parcel = Parcel.obtain();
        savedState.writeToParcel(parcel, savedState.describeContents());
        parcel.setDataPosition(0);

        GuidanceManeuverView.SavedState createdFromParcel = GuidanceManeuverView.SavedState.CREATOR.createFromParcel(
                parcel);
        assertNotNull(createdFromParcel.getViewState());
        assertThat(createdFromParcel.getViewState(), equalTo(GuidanceManeuverView.State.UPDATING));
    }

    private GuidanceManeuverData createData(int iconId, Long distance, String info1, String info2) {
        return new GuidanceManeuverData(iconId, distance, info1, info2);
    }
}
