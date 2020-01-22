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

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteOptions;
import com.here.msdkui.R;
import com.here.msdkui.common.DateFormatterUtil;
import com.here.msdkui.common.DistanceFormatterUtil;
import com.here.msdkui.common.TimeFormatterUtil;
import com.here.msdkui.common.measurements.UnitSystem;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Gets the different information from {@link Route} to be used in {@link RouteDescriptionList}.
 */
public final class RouteUtilTest extends RobolectricTest {

    @Test
    public void testIcon() {
        //test CAR
        Route route = new MockUtils.MockRouteBuilder().getRoute();
        Integer id = RouteUtil.getIcon(route);
        assertThat(id, equalTo(R.drawable.ic_drive));

        //test TRUCK
        route = new MockUtils.MockRouteBuilder().setTransportMode(RouteOptions.TransportMode.TRUCK).getRoute();
        id = RouteUtil.getIcon(route);
        assertThat(id, equalTo(R.drawable.ic_truck_24));

        //test PEDESTRIAN
        route = new MockUtils.MockRouteBuilder().setTransportMode(RouteOptions.TransportMode.PEDESTRIAN).getRoute();
        id = RouteUtil.getIcon(route);
        assertThat(id, equalTo(R.drawable.ic_walk_24));

        //test BICYCLE
        route = new MockUtils.MockRouteBuilder().setTransportMode(RouteOptions.TransportMode.BICYCLE).getRoute();
        id = RouteUtil.getIcon(route);
        assertThat(id, equalTo(R.drawable.ic_bike_24));

        //test SCOOTER
        route = new MockUtils.MockRouteBuilder().setTransportMode(RouteOptions.TransportMode.SCOOTER).getRoute();
        id = RouteUtil.getIcon(route);
        assertThat(id, equalTo(R.drawable.ic_scooter));
    }

    @Test
    public void testTime() {
        Spannable spannable = RouteUtil.getTimeToArrive(getApplicationContext(),
                new MockUtils.MockRouteBuilder().getRoute(), false);
        assertThat(spannable.toString(),
                equalTo(TimeFormatterUtil.format(getApplicationContext(), 1000 * DateUtils.SECOND_IN_MILLIS)));
    }

    @Test
    public void testTrafficDelayed() {
        //no delay
        Context context = getApplicationContext();
        Route route = new MockUtils.MockRouteBuilder().getRoute();
        Spannable spannable = RouteUtil.getTrafficDelayed(context, route);
        assertThat(spannable.toString().trim(),
                equalTo(getContextWithTheme().getString(R.string.msdkui_no_traffic_delays)));

        //road is blocked
        route = new MockUtils.MockRouteBuilder().setBlockedRoad().getRoute();
        spannable = RouteUtil.getTrafficDelayed(context, route);
        assertThat(spannable.toString().trim(),
                equalTo(getContextWithTheme().getString(R.string.msdkui_traffic_blocked)));

        //delayed
        int delayInMinutes = 10;
        route = new MockUtils.MockRouteBuilder().setTrafficPenaltyMinutes(10).getRoute();
        spannable = RouteUtil.getTrafficDelayed(context, route);
        assertThat(spannable.toString().trim(),
                equalTo(getContextWithTheme().getString(R.string.msdkui_incl_traffic_delay,
                        TimeFormatterUtil.format(getContextWithTheme(), TimeUnit.MINUTES.toMillis(delayInMinutes)))));

        //arrival time is defined
        route = new MockUtils.MockRouteBuilder().setArrivalTimeOption().getRoute();
        spannable = RouteUtil.getTrafficDelayed(context, route);
        assertTrue(spannable.toString().trim().isEmpty());
    }

    @Test
    public void testDetails() {
        Context context = getApplicationContext();
        Route route = new MockUtils.MockRouteBuilder().getRoute();
        Spannable spannable = RouteUtil.getDetails(context, route, UnitSystem.METRIC);
        SpannableStringBuilder expectedDetails = new SpannableStringBuilder(
                DistanceFormatterUtil.format(getApplicationContext(), 1000, UnitSystem.METRIC));
        assertThat(spannable.toString(), equalTo(expectedDetails + "   RouteName"));

        route = new MockUtils.MockRouteBuilder().setTransportMode(RouteOptions.TransportMode.PUBLIC_TRANSPORT).getRoute();
        Spannable spannable2 = RouteUtil.getDetails(context, route, UnitSystem.METRIC);
        assertTrue(spannable2.toString().isEmpty());
    }

    @Test
    public void testArrivalTime() {
        Context context = getApplicationContext();
        Route route = new MockUtils.MockRouteBuilder().getRoute();
        long currentTimeInMs = new Date().getTime();
        String arrivalTime = RouteUtil.getArrivalTime(context, route, false);
        assertThat(arrivalTime, equalTo(DateFormatterUtil.format(getApplicationContext(),
                new Date(currentTimeInMs + 1000 * DateUtils.SECOND_IN_MILLIS))));

        int penaltyMinutes = 5;
        route = new MockUtils.MockRouteBuilder().setTrafficPenaltyMinutes(penaltyMinutes).getRoute();
        arrivalTime = RouteUtil.getArrivalTime(context, route, true);
        assertThat(arrivalTime, equalTo(DateFormatterUtil.format(getApplicationContext(),
                new Date(currentTimeInMs + penaltyMinutes * DateUtils.MINUTE_IN_MILLIS))));
    }
}
