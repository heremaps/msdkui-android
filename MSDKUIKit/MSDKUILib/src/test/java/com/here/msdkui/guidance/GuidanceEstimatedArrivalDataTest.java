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

import com.here.RobolectricTest;
import com.here.msdkui.common.DateFormatterUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test for {@link GuidanceEstimatedArrivalData} class.
 */
public class GuidanceEstimatedArrivalDataTest extends RobolectricTest {

    private static final Date ETA_DATE = new Date(1535016978000L);
    private static final long DISTANCE = 10000;
    private static final int DURATION = 100;

    private GuidanceEstimatedArrivalData mEstimatedArrivalData = null;

    @Before
    public void setUp() {
        mEstimatedArrivalData = new GuidanceEstimatedArrivalData(ETA_DATE, DISTANCE, DURATION);
    }

    @Test
    public void testProperties() {
        assertThat(mEstimatedArrivalData.getEta(), is(ETA_DATE));
        assertThat(mEstimatedArrivalData.getDistance(), is(DISTANCE));
        assertThat(mEstimatedArrivalData.getDuration(), is(DURATION));
    }

    @Test
    public void testEquality() {
        GuidanceEstimatedArrivalData data = new GuidanceEstimatedArrivalData(ETA_DATE, DISTANCE, DURATION);
        assertThat(mEstimatedArrivalData, is(data));
        GuidanceEstimatedArrivalData data1 = new GuidanceEstimatedArrivalData(ETA_DATE, DISTANCE, 1);
        assertThat(mEstimatedArrivalData, is(not(data1)));
        assertThat(null, is(not(mEstimatedArrivalData)));
        assertThat(mEstimatedArrivalData.hashCode(), is(data.hashCode()));
    }

    @Test
    public void testToString() {
        GuidanceEstimatedArrivalData data = new GuidanceEstimatedArrivalData(ETA_DATE, DISTANCE, DURATION);
        String inputStr = "GuidanceEstimatedArrivalData(mEta=" + DateFormatterUtil.format(ETA_DATE) + ", mDistance=10000, mDuration=100)";
        assertThat(data.toString(), is(inputStr));
    }

    @Test
    public void testDataIsParcelable() {
        GuidanceEstimatedArrivalData data = new GuidanceEstimatedArrivalData(ETA_DATE, DISTANCE, DURATION);
        Parcel parcel = Parcel.obtain();
        data.writeToParcel(parcel, data.describeContents());
        parcel.setDataPosition(0);

        GuidanceEstimatedArrivalData createdFromParcel = GuidanceEstimatedArrivalData.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getEta(), is(ETA_DATE));
        assertThat(createdFromParcel.getDistance(), is(DISTANCE));
        assertThat(createdFromParcel.getDuration(), is(DURATION));
    }
}
