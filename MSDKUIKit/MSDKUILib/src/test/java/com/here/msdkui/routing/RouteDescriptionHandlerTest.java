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

package com.here.msdkui.routing;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.RoadElement;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteElement;
import com.here.android.mpa.routing.RouteElements;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteTta;
import com.here.msdkui.R;
import com.here.msdkui.common.DateFormatterUtil;
import com.here.msdkui.common.DistanceFormatterUtil;
import com.here.msdkui.common.TimeFormatterUtil;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Gets the different information from {@link Route} to be used in {@link RouteDescriptionList}.
 */
@PrepareForTest({ GeoCoordinate.class, RouteTta.class, RouteElements.class, RouteElement.class, RoadElement.class,
        RoutePlan.class })
public final class RouteDescriptionHandlerTest extends RobolectricTest {

    @Test
    public void testIcon() {
        //test CAR
        RouteDescriptionHandler routeDescriptionHandler = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().getRoute());
        Integer id = routeDescriptionHandler.getIcon();
        assertThat(id, equalTo(R.drawable.ic_drive));

        //test TRUCK
        routeDescriptionHandler = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().setTransportMode(RouteOptions.TransportMode.TRUCK).getRoute());
        id = routeDescriptionHandler.getIcon();
        assertThat(id, equalTo(R.drawable.ic_truck_24));

        //test PEDESTRIAN
        routeDescriptionHandler = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().setTransportMode(RouteOptions.TransportMode.PEDESTRIAN).getRoute());
        id = routeDescriptionHandler.getIcon();
        assertThat(id, equalTo(R.drawable.ic_walk_24));

        //test BICYCLE
        routeDescriptionHandler = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().setTransportMode(RouteOptions.TransportMode.BICYCLE).getRoute());
        id = routeDescriptionHandler.getIcon();
        assertThat(id, equalTo(R.drawable.ic_bike_24));

        //test SCOOTER
        routeDescriptionHandler = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().setTransportMode(RouteOptions.TransportMode.SCOOTER).getRoute());
        id = routeDescriptionHandler.getIcon();
        assertThat(id, equalTo(R.drawable.ic_scooter));
    }

    @Test
    public void testTime() {
        Spannable spannable = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().getRoute()).getTimeToArrive(false);
        assertThat(spannable.toString(),
                equalTo(TimeFormatterUtil.format(getApplicationContext(), 1000 * DateUtils.SECOND_IN_MILLIS)));
    }

    @Test
    public void testTrafficDelayed() {
        //no delay
        RouteDescriptionHandler routeDescriptionHandler = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().getRoute());
        Spannable spannable = routeDescriptionHandler.getTrafficDelayed();
        assertThat(spannable.toString().trim(),
                equalTo(getContextWithTheme().getString(R.string.msdkui_no_traffic_delays)));

        //road is blocked
        routeDescriptionHandler = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().setBlockedRoad().getRoute());
        spannable = routeDescriptionHandler.getTrafficDelayed();
        assertThat(spannable.toString().trim(),
                equalTo(getContextWithTheme().getString(R.string.msdkui_traffic_blocked)));

        //delayed
        int delayInMinutes = 10;
        routeDescriptionHandler = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().setTrafficPenaltyMinutes(10).getRoute());
        spannable = routeDescriptionHandler.getTrafficDelayed();
        assertThat(spannable.toString().trim(),
                equalTo(getContextWithTheme().getString(R.string.msdkui_incl_traffic_delay,
                        TimeFormatterUtil.format(getContextWithTheme(), TimeUnit.MINUTES.toMillis(delayInMinutes)))));
    }

    @Test
    public void testDetails() {
        Spannable spannable = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().getRoute()).getDetails();
        SpannableStringBuilder expectedDetails = new SpannableStringBuilder(
                DistanceFormatterUtil.format(getApplicationContext(), 1000));
        assertThat(spannable.toString(), equalTo(expectedDetails + "   RouteName"));

        RouteDescriptionHandler routeDescriptionHandler = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().setTransportMode(RouteOptions.TransportMode.PUBLIC_TRANSPORT).getRoute());
        Spannable spannable2 = routeDescriptionHandler.getDetails();
        assertThat(spannable2.toString(), equalTo(getContextWithTheme().getString(R.string.msdkui_not_implemented)));
    }

    @Test
    public void testArrivalTime() {
        long currentTimeInMs = new Date().getTime();
        String arrivalTime = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().getRoute()).getArrivalTime(false);
        assertThat(arrivalTime, equalTo(DateFormatterUtil.format(getApplicationContext(),
                new Date(currentTimeInMs + 1000 * DateUtils.SECOND_IN_MILLIS))));

        int penaltyMinutes = 5;
        arrivalTime = new RouteDescriptionHandler(getApplicationContext(),
                new MockUtils.MockRouteBuilder().setTrafficPenaltyMinutes(penaltyMinutes).getRoute()).getArrivalTime(true);//mRouteDescriptionHandler.getArrivalTime(true);
        assertThat(arrivalTime, equalTo(DateFormatterUtil.format(getApplicationContext(),
                new Date(currentTimeInMs + penaltyMinutes * DateUtils.MINUTE_IN_MILLIS))));
    }
}
