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

import com.here.RobolectricTest;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link GuidanceNextManeuverData}.
 */
public class GuidanceNextManeuverDataTest extends RobolectricTest {

    private static final int ICON_ID = 1;
    private static final long DISTANCE = 1L;
    private static final String STREET_NAME = "SomeStreet";
    private GuidanceNextManeuverData mManeuverData;

    @Before
    public void setUp() {
        mManeuverData = new GuidanceNextManeuverData(ICON_ID, DISTANCE, STREET_NAME);
    }

    @Test
    public void testProperties() {
        assertThat(mManeuverData.getIconId(), is(1));
        assertThat(mManeuverData.getDistance(), is(1L));
        assertThat(mManeuverData.getStreetName(), is(STREET_NAME));
    }

    @Test
    public void testEquality() {
        GuidanceNextManeuverData data = new GuidanceNextManeuverData(ICON_ID, DISTANCE, STREET_NAME);
        assertThat(mManeuverData, is(data));
        GuidanceNextManeuverData data1 = new GuidanceNextManeuverData(0, DISTANCE, STREET_NAME);
        assertThat(mManeuverData, is(not(data1)));
        assertThat(null, is(not(mManeuverData)));
        assertThat(mManeuverData.hashCode(), is(data.hashCode()));
    }

    @Test
    public void testToString() {
        GuidanceNextManeuverData data = new GuidanceNextManeuverData(ICON_ID, DISTANCE, STREET_NAME);
        String inputStr = "GuidanceNextManeuverData(mIconId=1, mDistance=1, mStreetName=SomeStreet)";
        assertThat(data.toString(), is(inputStr));
    }

    @Test
    public void testDataIsParcelable() {
        GuidanceNextManeuverData data = new GuidanceNextManeuverData(ICON_ID, DISTANCE, STREET_NAME);
        Parcel parcel = Parcel.obtain();
        data.writeToParcel(parcel, data.describeContents());
        parcel.setDataPosition(0);

        GuidanceNextManeuverData createdFromParcel = GuidanceNextManeuverData.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getStreetName(), is(STREET_NAME));
        assertThat(createdFromParcel.getIconId(), is(ICON_ID));
        assertThat(createdFromParcel.getDistance(), is(DISTANCE));
    }

    @Test
    public void testNullDataIsParcelable() {
        GuidanceNextManeuverData data = new GuidanceNextManeuverData(null, null, null);
        Parcel parcel = Parcel.obtain();
        data.writeToParcel(parcel, data.describeContents());
        parcel.setDataPosition(0);

        GuidanceNextManeuverData createdFromParcel = GuidanceNextManeuverData.CREATOR.createFromParcel(parcel);
        assertNull(createdFromParcel.getStreetName());
        assertNull(createdFromParcel.getIconId());
        assertNull(createdFromParcel.getDistance());
    }
}
