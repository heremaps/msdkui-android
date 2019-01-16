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

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.support.v4.app.FragmentActivity;
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
        final TextView infoView1 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView1);
        final TextView distanceView = (TextView) mGuidanceManeuverView.findViewById(R.id.distanceView);
        final TextView infoView2 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView2);
        final ImageView iconView = (ImageView) mGuidanceManeuverView.findViewById(R.id.maneuverIconView);
        final ProgressBar busyProgressBar = (ProgressBar) mGuidanceManeuverView.findViewById(
                R.id.busyStateProgressBar);

        assertEquals(infoView1.getVisibility(), View.VISIBLE);
        assertThat(infoView1.getText().toString(), is(getString(R.string.msdkui_maneuverpanel_nodata)));

        GuidanceManeuverData data = createData(mIconId, mDistance, mInfo1, mInfo2);
        // create proper data to see if everything is fine.
        mGuidanceManeuverView.setManeuverData(data);

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
                .addAttribute(R.attr.viewMode, "2")
                .build();
        mGuidanceManeuverView = new GuidanceManeuverView(getApplicationContext(), attributeSet);
        testUIInit();
    }

    @Test
    public void testWhenManeuverIsNull() {
        mGuidanceManeuverView.setManeuverData(null);
        final TextView distanceView = (TextView) mGuidanceManeuverView.findViewById(R.id.distanceView);
        final TextView infoView1 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView1);
        final TextView infoView2 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView2);
        final ImageView iconView = (ImageView) mGuidanceManeuverView.findViewById(R.id.maneuverIconView);
        final ProgressBar busyProgressBar = (ProgressBar) mGuidanceManeuverView.findViewById(
                R.id.busyStateProgressBar);

        // null data should be hidden.
        assertEquals(distanceView.getVisibility(), View.INVISIBLE);
        assertEquals(infoView1.getVisibility(), View.GONE);
        assertEquals(infoView2.getVisibility(), View.GONE);
        assertEquals(iconView.getVisibility(), View.INVISIBLE);
        assertEquals(busyProgressBar.getVisibility(), View.GONE);
    }

    @Test
    public void testWhenManeuverForRouteRecalculation() {
        mGuidanceManeuverView.setManeuverData(new GuidanceManeuverData(-1, -1,
                getString(R.string.msdkui_maneuverpanel_updating), ""));
        final TextView distanceView = (TextView) mGuidanceManeuverView.findViewById(R.id.distanceView);
        final TextView infoView1 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView1);
        final TextView infoView2 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView2);
        final ImageView iconView = (ImageView) mGuidanceManeuverView.findViewById(R.id.maneuverIconView);
        final ProgressBar busyProgressBar = (ProgressBar) mGuidanceManeuverView.findViewById(
                R.id.busyStateProgressBar);

        // null data should be hidden.
        assertEquals(distanceView.getVisibility(), View.INVISIBLE);
        assertEquals(infoView1.getVisibility(), View.VISIBLE);
        assertThat(infoView1.getText().toString(), is(getString(R.string.msdkui_maneuverpanel_updating)));
        assertEquals(infoView2.getVisibility(), View.INVISIBLE);
        assertEquals(iconView.getVisibility(), View.INVISIBLE);
        assertEquals(busyProgressBar.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testExtraIconView() {
        final ImageView extraIconView = (ImageView) mGuidanceManeuverView.findViewById(R.id.extraIconView);
        assertEquals(extraIconView.getVisibility(), View.INVISIBLE);
        GuidanceManeuverData data = new GuidanceManeuverData(-1, -1, "", "", Mockito.mock(Bitmap.class));
        mGuidanceManeuverView.setManeuverData(data);
        assertEquals(extraIconView.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testUIWhenSomeDataIsNull() {

        GuidanceManeuverData data = createData(0, -1, null, null);
        // create proper data to see if everything is fine.
        mGuidanceManeuverView.setManeuverData(data);

        final TextView distanceView = (TextView) mGuidanceManeuverView.findViewById(R.id.distanceView);
        final TextView infoView1 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView1);
        final TextView infoView2 = (TextView) mGuidanceManeuverView.findViewById(R.id.infoView2);
        final ImageView iconView = (ImageView) mGuidanceManeuverView.findViewById(R.id.maneuverIconView);

        // null data should be hidden.
        assertEquals(distanceView.getVisibility(), View.INVISIBLE);
        assertEquals(infoView1.getVisibility(), View.GONE);
        assertEquals(infoView2.getVisibility(), View.GONE);
        assertEquals(iconView.getVisibility(), View.GONE);
    }

    @Test
    public void testSettingDataReturnsTheSame() {
        GuidanceManeuverData data = createData(mIconId, mDistance, mInfo1, mInfo2);
        mGuidanceManeuverView.setManeuverData(data);
        assertThat(mGuidanceManeuverView.getManeuverData(), equalTo(data));
    }

    @Test
    public void settingDataNullShouldNotThrowException() {
        mGuidanceManeuverView.setManeuverData(null);
        assertNull(mGuidanceManeuverView.getManeuverData());
    }

    @Test
    public void testHighLightManeuver() {
        mGuidanceManeuverView.highLightManeuver(Color.RED);
        assertThat(((TextView) mGuidanceManeuverView.findViewById(R.id.infoView2)).getCurrentTextColor(),
                is(Color.RED));

    }

    @Test
    public void testDataIsNotLostWhileRecreatingActivity() {
        mGuidanceManeuverView.setManeuverData(createData(mIconId, mDistance, mInfo1, mInfo2));
        assertNotNull(mGuidanceManeuverView.getManeuverData());
        final FragmentActivity activity = getFragmentActivity();
        mGuidanceManeuverView.setId(R.id.vertical_guideline);
        activity.setContentView(mGuidanceManeuverView);
        activity.recreate();
        assertNotNull(mGuidanceManeuverView.getManeuverData());
    }

    @Test
    public void testPanelDataIsParcelable() {

        final GuidanceManeuverData data = createData(mIconId, mDistance, mInfo1, mInfo2);
        GuidanceManeuverView.SavedState savedState = new GuidanceManeuverView.SavedState(AbsSavedState.EMPTY_STATE);
        savedState.setManeuverData(data);
        Parcel parcel = Parcel.obtain();
        savedState.writeToParcel(parcel, savedState.describeContents());
        parcel.setDataPosition(0);

        GuidanceManeuverView.SavedState createdFromParcel = GuidanceManeuverView.SavedState.CREATOR.createFromParcel(
                parcel);
        assertNotNull(createdFromParcel.getManeuverData());
    }

    private GuidanceManeuverData createData(int iconId, long distance, String info1, String info2) {
        return new GuidanceManeuverData(iconId, distance, info1, info2);
    }
}
