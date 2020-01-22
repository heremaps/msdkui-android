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
import android.os.Parcel;

import com.here.RobolectricTest;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests for {@link GuidanceManeuverData}.
 */
public class GuidanceManeuverDataTest extends RobolectricTest {

    private static final int ICON_ID = 1;
    private static final Long DISTANCE = 1L;
    private static final String INFO1 = "Info1";
    private static final String INFO2 = "Info2";
    private static final Bitmap NEXT_ROAD_ICON = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
    private GuidanceManeuverData mManeuverData;
    private GuidanceManeuverData mManeuverDataWithNextRoadIcon;

    @Before
    public void setUp() {
        mManeuverData = new GuidanceManeuverData(ICON_ID, DISTANCE, INFO1, INFO2);
        mManeuverDataWithNextRoadIcon = new GuidanceManeuverData(ICON_ID, DISTANCE, INFO1, INFO2, NEXT_ROAD_ICON);
    }

    @Test
    public void testProperties() {
        assertThat(mManeuverData.getIconId(), is(1));
        assertThat(mManeuverData.getDistance(), is(1L));
        assertThat(mManeuverData.getInfo1(), is(INFO1));
        assertThat(mManeuverData.getInfo2(), is(INFO2));
        assertNull(mManeuverData.getNextRoadIcon());
        assertThat(mManeuverDataWithNextRoadIcon.getNextRoadIcon(), is(NEXT_ROAD_ICON));
    }

    @Test
    public void testEquality() {
        GuidanceManeuverData data = new GuidanceManeuverData(ICON_ID, DISTANCE, INFO1, INFO2);
        assertThat(mManeuverData, is(data));
        GuidanceManeuverData data1 = new GuidanceManeuverData(0, DISTANCE, INFO1, INFO2);
        assertThat(mManeuverData, is(not(data1)));
        assertThat(null, is(not(mManeuverData)));
        assertThat(mManeuverData.hashCode(), is(data.hashCode()));
        GuidanceManeuverData data2 = new GuidanceManeuverData(ICON_ID, DISTANCE, INFO1, INFO2, NEXT_ROAD_ICON);
        assertThat(data2, is(mManeuverDataWithNextRoadIcon));
        assertThat(mManeuverDataWithNextRoadIcon.hashCode(), is(data2.hashCode()));
    }

    @Test
    public void testToString() {
        GuidanceManeuverData data = new GuidanceManeuverData(ICON_ID, DISTANCE, INFO1, INFO2);
        String inputStr = "GuidanceManeuverData(mIconId=1, mDistance=1, mInfo1=Info1, mInfo2=Info2, mNextRoadIcon=null)";
        assertThat(data.toString(), is(inputStr));
    }

    @Test
    public void testDataIsParcelable() {
        GuidanceManeuverData data = new GuidanceManeuverData(ICON_ID, DISTANCE, INFO1, INFO2);
        Parcel parcel = Parcel.obtain();
        data.writeToParcel(parcel, data.describeContents());
        parcel.setDataPosition(0);

        GuidanceManeuverData createdFromParcel = GuidanceManeuverData.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getInfo1(), is(INFO1));
        assertThat(createdFromParcel.getInfo2(), is(INFO2));
        assertThat(createdFromParcel.getIconId(), is(ICON_ID));
        assertThat(createdFromParcel.getDistance(), is(DISTANCE));
    }
}
