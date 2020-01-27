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

package com.here.msdkui.routing;

import android.view.View;
import android.widget.TextView;

import com.here.RobolectricTest;
import com.here.android.mpa.routing.RouteOptions;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

/**
 * Test for {@link TravelTimePanel}.
 */
public class TravelTimePanelTest extends RobolectricTest implements TravelTimePanel.OnTimeChangedListener {

    private TravelTimePanel mTravelTimePanel;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        super.setUp();
        mTravelTimePanel = new TravelTimePanel(getActivityController().get());
    }

    @Test
    public void testInitUIContent() {
        final TextView travelTimeText = mTravelTimePanel.findViewById(R.id.travel_time_text);
        assertNotNull(travelTimeText);
        // by default, it should be visible
        assertSame("Remove icon is not visible by default", View.VISIBLE, travelTimeText.getVisibility());

        final TextView travelTimeDetails = mTravelTimePanel.findViewById(R.id.travel_time_details);
        assertNotNull(travelTimeDetails);
        // by default, it should be visible
        assertSame("Remove icon is not visible by default", View.VISIBLE, travelTimeDetails.getVisibility());
    }

    @Test
    public void testInitContent() {
        assertNotNull(mTravelTimePanel.getTime());
        double expectedTime = (double) mTravelTimePanel.getTime().getTime();
        assertThat(expectedTime, is(closeTo(new Date().getTime(), 5000L)));
        assertNotNull(mTravelTimePanel.getTimeType());
        assertThat(mTravelTimePanel.getTimeType(), equalTo(RouteOptions.TimeType.DEPARTURE));
    }

    @Test
    public void testSettingTime() {
        // test setting time
        mTravelTimePanel.setOnTimeChangedListener(this);
        final Date time = mTravelTimePanel.getTime();
        // setting same time should not invoke time change event
        mTravelTimePanel.setTime(time);
        assertThat(mCallbackCalled, is(false));
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        // now set future date.
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + 1);
        mTravelTimePanel.setTime(calendar.getTime());
        assertThat(mCallbackCalled, is(true));
    }

    @Test
    public void testSettingTimeType() {
        // test setting time type
        mTravelTimePanel.setOnTimeChangedListener(this);
        assertThat(mTravelTimePanel.getTimeType(), equalTo(RouteOptions.TimeType.DEPARTURE));
        mTravelTimePanel.setTimeType(RouteOptions.TimeType.DEPARTURE);
        assertThat(mCallbackCalled, is(false));
        mTravelTimePanel.setTimeType(RouteOptions.TimeType.ARRIVAL);
        assertThat(mCallbackCalled, is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullTimeShouldException() {
        mTravelTimePanel.setTime(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullTimeTypeException() {
        mTravelTimePanel.setTimeType(null);
    }

    @Override
    public void onTimeChanged(final Date date, final RouteOptions.TimeType type) {
        mCallbackCalled = true;
    }
}
