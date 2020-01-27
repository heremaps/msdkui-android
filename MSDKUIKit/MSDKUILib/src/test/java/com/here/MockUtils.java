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

package com.here;

import androidx.annotation.NonNull;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.RoadElement;
import com.here.android.mpa.routing.DynamicPenalty;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteElement;
import com.here.android.mpa.routing.RouteElements;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteTta;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.msdkui.routing.WaypointEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Mock utilities class.
 */
public final class MockUtils {

    public static final String ROAD_NAME = "roadName";
    public static final String ROAD_NUMBER = "1";

    private MockUtils() {
    }

    /**
     * mockGeoCoordinate.
     */
    public static GeoCoordinate mockGeoCoordinate(double lat, double lon) {
        GeoCoordinate ret = mock(GeoCoordinate.class);
        when(ret.toString()).thenReturn("(" + lat + ", " + lon + ")");
        return ret;
    }

    /**
     * mockRouteWaypoint.
     */
    public static RouteWaypoint mockRouteWaypoint(GeoCoordinate coordinate) {
        RouteWaypoint ret = mock(RouteWaypoint.class);
        when(ret.getOriginalPosition()).thenReturn(coordinate);
        return ret;
    }

    /**
     * mockWayPointEntry.
     */
    public static WaypointEntry mockWayPointEntry() {
        return new WaypointEntry(MockUtils.mockRouteWaypoint(MockUtils.mockGeoCoordinate(52.530555, 13.379257)));
    }

    /**
     * mockManeuver
     */
    public static Maneuver mockManeuver() {
        Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getIcon()).thenReturn(Maneuver.Icon.HEAVY_LEFT);
        when(maneuver.getDistanceToNextManeuver()).thenReturn(200);
        when(maneuver.getDistanceFromPreviousManeuver()).thenReturn(200);
        when(maneuver.getRoadName()).thenReturn(ROAD_NAME);
        when(maneuver.getRoadNumber()).thenReturn(ROAD_NUMBER);
        when(maneuver.getTransportMode()).thenReturn(RouteOptions.TransportMode.CAR);
        when(maneuver.getAction()).thenReturn(Maneuver.Action.ENTER_HIGHWAY);
        when(maneuver.getTurn()).thenReturn(Maneuver.Turn.LIGHT_LEFT);
        return maneuver;
    }

    /**
     * mockRouteElement
     */
    private static RouteElement mockRouteElement() {
        RoadElement roadElem = mock(RoadElement.class);
        when(roadElem.getGeometryLength()).thenReturn(100.0);
        when(roadElem.getRoadName()).thenReturn("Test Road Name");
        when(roadElem.getRouteName()).thenReturn("RouteName");

        RouteElement elem = mock(RouteElement.class);
        when(elem.getRoadElement()).thenReturn(roadElem);
        return elem;
    }

    public static RouteResult mockRouteResult() {
        RouteResult result = mock(RouteResult.class);
        Route route = new MockRouteBuilder().getRoute();
        when(result.getRoute()).thenReturn(route);
        return result;
    }

    public static DynamicPenalty getDynamicPenality(final Route.TrafficPenaltyMode mode) {
        final DynamicPenalty dynamicPenalty = mock(DynamicPenalty.class);
        when(dynamicPenalty.getTrafficPenaltyMode()).thenReturn(mode);
        return dynamicPenalty;
    }

    /**
     * Mock route builder to build route based on given options.
     */
    public static class MockRouteOptionsBuilder {
        private RouteOptions mRouteOptions;

        public MockRouteOptionsBuilder() {

        }

        public RouteOptions build() {
            if (mRouteOptions == null) {
                mRouteOptions = mock(RouteOptions.class);
                when(mRouteOptions.getTransportMode()).thenReturn(RouteOptions.TransportMode.CAR);
            }
            return mRouteOptions;
        }

        public MockRouteOptionsBuilder withTransportMode(@NonNull RouteOptions.TransportMode transportMode) {
            mRouteOptions = mock(RouteOptions.class);
            when(mRouteOptions.getTransportMode()).thenReturn(transportMode);
            return this;
        }

        public MockRouteOptionsBuilder withFullDriveOptions() {
            mRouteOptions = mock(RouteOptions.class);
            when(mRouteOptions.getTransportMode()).thenReturn(RouteOptions.TransportMode.CAR);
            when(mRouteOptions.getTime(new Date())).thenReturn(RouteOptions.TimeType.DEPARTURE);
            when(mRouteOptions.areCarShuttleTrainsAllowed()).thenReturn(true);
            when(mRouteOptions.isCarpoolAllowed()).thenReturn(true);
            when(mRouteOptions.areDirtRoadsAllowed()).thenReturn(true);
            when(mRouteOptions.areFerriesAllowed()).thenReturn(true);
            when(mRouteOptions.areHighwaysAllowed()).thenReturn(true);
            when(mRouteOptions.areParksAllowed()).thenReturn(true);
            when(mRouteOptions.areTollRoadsAllowed()).thenReturn(true);
            when(mRouteOptions.areTunnelsAllowed()).thenReturn(true);
            return this;
        }

        public MockRouteOptionsBuilder withFewDriveOptions() {
            mRouteOptions = mock(RouteOptions.class);
            when(mRouteOptions.areCarShuttleTrainsAllowed()).thenReturn(true);
            when(mRouteOptions.areDirtRoadsAllowed()).thenReturn(true);
            return this;
        }

        public MockRouteOptionsBuilder withFullHazardousOptions() {
            mRouteOptions = mock(RouteOptions.class);
            final EnumSet<RouteOptions.HazardousGoodType> hazardousGoodTypes = EnumSet.allOf(
                    RouteOptions.HazardousGoodType.class);
            when(mRouteOptions.getTruckShippedHazardousGoods()).thenReturn(hazardousGoodTypes);
            return this;
        }

        public MockRouteOptionsBuilder withFewHazardousOptions() {
            mRouteOptions = mock(RouteOptions.class);
            final EnumSet<RouteOptions.HazardousGoodType> hazardousGoodTypes = EnumSet.of(
                    RouteOptions.HazardousGoodType.COMBUSTIBLE);
            when(mRouteOptions.getTruckShippedHazardousGoods()).thenReturn(hazardousGoodTypes);
            return this;
        }

        public MockRouteOptionsBuilder withTunnelOptions() {
            mRouteOptions = mock(RouteOptions.class);
            when(mRouteOptions.getTruckTunnelCategory()).thenReturn(RouteOptions.TunnelCategory.C);
            return this;
        }

        public MockRouteOptionsBuilder withNoTunnelOptions() {
            mRouteOptions = mock(RouteOptions.class);
            when(mRouteOptions.getTruckTunnelCategory()).thenReturn(RouteOptions.TunnelCategory.UNDEFINED);
            return this;
        }

        public MockRouteOptionsBuilder withTruckOptions() {
            mRouteOptions = mock(RouteOptions.class);
            when(mRouteOptions.getTruckHeight()).thenReturn(3.0f);
            when(mRouteOptions.getTruckWidth()).thenReturn(3.0f);
            when(mRouteOptions.getTruckLimitedWeight()).thenReturn(3.0f);
            when(mRouteOptions.getTruckWeightPerAxle()).thenReturn(3.0f);
            when(mRouteOptions.getTruckTrailersCount()).thenReturn(3);
            when(mRouteOptions.getTruckType()).thenReturn(RouteOptions.TruckType.TRACTOR_TRUCK);
            return this;
        }

    }

    /**
     * Mock route builder to build route based on given options.
     */
    public static class MockRouteBuilder {

        private final Route mRoute;
        private RouteTta mRouteTta1;
        private RouteTta mRouteTta2 = null;
        private RouteElements mRouteElements;
        private RoutePlan mPlan;
        private RouteOptions mOptions;

        public MockRouteBuilder() {
            mRoute = mock(Route.class);

            mRouteTta1 = mock(RouteTta.class);
            when(mRouteTta1.isBlocked()).thenReturn(false);
            when(mRouteTta1.getDuration()).thenReturn(1000);
            when(mRoute.getTtaIncludingTraffic(anyInt())).thenReturn(mRouteTta1);
            when(mRoute.getTtaExcludingTraffic(anyInt())).thenReturn(mRouteTta1);

            mRouteElements = mock(RouteElements.class);
            List<RouteElement> elements = new ArrayList<>(Collections.singletonList(mockRouteElement()));
            when(mRouteElements.getElements()).thenReturn(elements);
            when(mRoute.getRouteElements()).thenReturn(mRouteElements);

            mPlan = mock(RoutePlan.class);
            mOptions = new MockUtils.MockRouteOptionsBuilder().build();
            when(mPlan.getRouteOptions()).thenReturn(mOptions);
            when(mRoute.getRoutePlan()).thenReturn(mPlan);

            setListOfManeuver();
            when(mRoute.getLength()).thenReturn(1000);
        }

        public MockRouteBuilder setBlockedRoad() {
            when(mRouteTta1.isBlocked()).thenReturn(true);
            return this;
        }

        public MockRouteBuilder setTransportMode(RouteOptions.TransportMode transportMode) {
            mOptions = new MockRouteOptionsBuilder().withTransportMode(transportMode).build();
            when(mPlan.getRouteOptions()).thenReturn(mOptions);
            return this;
        }

        public MockRouteBuilder setArrivalTimeOption() {
            when(mOptions.getTime(any(Date.class))).thenReturn(RouteOptions.TimeType.ARRIVAL);
            return this;
        }

        public MockRouteBuilder setTrafficPenaltyMinutes(int minutes) {
            int delayInSeconds = minutes * 60;
            when(mRouteTta1.getDuration()).thenReturn(delayInSeconds);
            when(mRoute.getTtaIncludingTraffic(anyInt())).thenReturn(mRouteTta1);
            if (mRouteTta2 == null) {
                mRouteTta2 = mock(RouteTta.class);
            }
            when(mRouteTta2.getDuration()).thenReturn(0);
            when(mRoute.getTtaExcludingTraffic(anyInt())).thenReturn(mRouteTta2);

            return this;
        }

        public MockRouteBuilder setListOfManeuver() {
            Maneuver maneuver = mockManeuver();
            when(mRoute.getManeuvers()).thenReturn(new ArrayList<>(Collections.singletonList(maneuver)));
            return this;
        }

        public Route getRoute() {
            return mRoute;
        }
    }

}
