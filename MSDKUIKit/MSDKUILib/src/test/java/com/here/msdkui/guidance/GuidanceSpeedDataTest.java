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
 * Tests for class {@link GuidanceSpeedData}.
 */
public class GuidanceSpeedDataTest extends RobolectricTest {

    private static final double VELOCITY = 60;
    private static final double SPEED_LIMIT = 70;

    private GuidanceSpeedData mCurrentSpeedData = null;

    @Before
    public void setUp() {
        mCurrentSpeedData = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
    }

    @Test
    public void testProperties() {
        assertThat(mCurrentSpeedData.getCurrentSpeed(), is(VELOCITY));
        assertThat(mCurrentSpeedData.getCurrentSpeedLimit(), is(SPEED_LIMIT));
    }

    @Test
    public void testIfSpeeding() {
        assertThat(mCurrentSpeedData.isSpeeding(), is(not(true)));
        double lowLimit = 40;
        GuidanceSpeedData data1 = new GuidanceSpeedData(VELOCITY, lowLimit);
        assertThat(data1.isSpeeding(), is(true));
    }

    @Test
    public void testEquality() {
        GuidanceSpeedData data1 = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
        assertThat(data1, is(mCurrentSpeedData));
        final double speed = 70;
        GuidanceSpeedData data2 = new GuidanceSpeedData(speed, SPEED_LIMIT);
        assertThat(mCurrentSpeedData, is(not(data2)));
        assertThat(null, is(not(mCurrentSpeedData)));
        assertThat(mCurrentSpeedData.hashCode(), is(data1.hashCode()));
    }

    @Test
    public void testToString() {
        GuidanceSpeedData data = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
        String expectedResult = "GuidanceSpeedData(mCurrentSpeed=60.0, mCurrentSpeedLimit=70.0)";
        assertThat(data.toString(), is(expectedResult));
    }

    @Test
    public void testDataIsParcelable() {
        GuidanceSpeedData data = new GuidanceSpeedData(VELOCITY, SPEED_LIMIT);
        Parcel parcel = Parcel.obtain();
        data.writeToParcel(parcel, data.describeContents());
        parcel.setDataPosition(0);

        GuidanceSpeedData createdFromParcel = GuidanceSpeedData.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getCurrentSpeed(), is(VELOCITY));
        assertThat(createdFromParcel.getCurrentSpeedLimit(), is(SPEED_LIMIT));

        // test for null data
        parcel = Parcel.obtain();
        data = new GuidanceSpeedData(null, null);
        data.writeToParcel(parcel, data.describeContents());
        parcel.setDataPosition(0);

        createdFromParcel = GuidanceSpeedData.CREATOR.createFromParcel(parcel);
        assertNull(createdFromParcel.getCurrentSpeed());
        assertNull(createdFromParcel.getCurrentSpeedLimit());

    }
}
