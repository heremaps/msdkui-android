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

import android.content.ComponentName;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.RoadElement;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteElement;
import com.here.android.mpa.routing.RouteElements;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteTta;
import com.here.msdkui.R;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test class for {@link RouteDescriptionList} class.
 */
@PrepareForTest({GeoCoordinate.class, RouteTta.class, RouteElements.class, RouteElement.class, RoadElement.class, RoutePlan.class})
public class RouteDescriptionsListTest extends RobolectricTest {

    private RouteDescriptionList mRoutesDescriptionList;

    @Before
    public void setUp() {
        mRoutesDescriptionList = new RouteDescriptionList(getContextWithTheme());
    }

    @Test
    public void testConstructionWithAttributes() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.sortOrder, "0")
                .addAttribute(R.attr.sortType, "1")
                .build();

        RouteDescriptionList routeDescriptionList = new RouteDescriptionList(getContextWithTheme(), attributeSet);
        assertEquals(RouteDescriptionList.SortOrder.ASCENDING, routeDescriptionList.getSortOrder());
        assertEquals(RouteDescriptionList.SortType.TOTAL_TIME, routeDescriptionList.getSortType());
    }

    @Test
    public void initShouldHaveProperDefaultValues() {
        assertNotNull(mRoutesDescriptionList.getAdapter());
        assertThat(mRoutesDescriptionList.getAdapter().getItemCount(), equalTo(0));
        assertNotNull(mRoutesDescriptionList.getRoutes());     // route should be empty
        assertThat(mRoutesDescriptionList.getRoutes().size(), equalTo(0));
    }

    @Test
    public void setRouteShouldAddRoutesRow() {
        final Route route1 = new MockUtils.MockRouteBuilder().getRoute();
        final Route route2 = new MockUtils.MockRouteBuilder().getRoute();
        mRoutesDescriptionList.setRoutes(new ArrayList<Route>(Arrays.asList(route1, route2)));
        assertThat(mRoutesDescriptionList.getAdapter().getItemCount(), equalTo(2));
        assertNotNull(mRoutesDescriptionList.getRoutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullRoutesException() {
        mRoutesDescriptionList.setRoutes(null);
    }

    @Test
    public void setRouteResultShouldAddRoutesRow() {
        final RouteResult route = MockUtils.mockRouteResult();
        mRoutesDescriptionList.setRoutesResult(new ArrayList<RouteResult>(Arrays.asList(route)));
        assertThat(mRoutesDescriptionList.getAdapter().getItemCount(), equalTo(1));
        assertNotNull(mRoutesDescriptionList.getRoutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullRoutesResultException() {
        mRoutesDescriptionList.setRoutesResult(null);
    }

    @Test
    public void testSetGetSortType() {
        mRoutesDescriptionList.setSortType(RouteDescriptionList.SortType.DISTANCE);
        assertEquals(RouteDescriptionList.SortType.DISTANCE, mRoutesDescriptionList.getSortType());
    }

    @Test
    public void testRoutesSorting() {
        int length_1 = 10;
        int length_2 = 15;
        int tta_1 = 1;
        int tta_2 = 3;
        final Route route_1 = new MockUtils.MockRouteBuilder().getRoute();
        final Route route_2 = new MockUtils.MockRouteBuilder().getRoute();
        final RouteTta routeTta_1 = mock(RouteTta.class);
        final RouteTta routeTta_2 = mock(RouteTta.class);

        when(routeTta_1.getDuration()).thenReturn(tta_1);
        when(routeTta_2.getDuration()).thenReturn(tta_2);
        when(route_1.getLength()).thenReturn(length_1);
        when(route_1.getTta(Route.TrafficPenaltyMode.OPTIMAL, Route.WHOLE_ROUTE)).thenReturn(routeTta_1);
        when(route_2.getLength()).thenReturn(length_2);
        when(route_2.getTta(Route.TrafficPenaltyMode.OPTIMAL, Route.WHOLE_ROUTE)).thenReturn(routeTta_2);

        // ascending by distance
        mRoutesDescriptionList.setSortType(RouteDescriptionList.SortType.DISTANCE);
        mRoutesDescriptionList.setSortOrder(RouteDescriptionList.SortOrder.ASCENDING);
        mRoutesDescriptionList.setRoutes(new ArrayList<Route>(Arrays.asList(route_1, route_2)));
        assertEquals(route_1, mRoutesDescriptionList.getRoutes().get(0));

        // descending by distance
        mRoutesDescriptionList.setSortType(RouteDescriptionList.SortType.DISTANCE);
        mRoutesDescriptionList.setSortOrder(RouteDescriptionList.SortOrder.DESCENDING);
        mRoutesDescriptionList.setRoutes(new ArrayList<Route>(Arrays.asList(route_1, route_2)));
        assertEquals(route_1, mRoutesDescriptionList.getRoutes().get(1));

        // ascending by tta
        mRoutesDescriptionList.setSortType(RouteDescriptionList.SortType.TOTAL_TIME);
        mRoutesDescriptionList.setSortOrder(RouteDescriptionList.SortOrder.ASCENDING);
        mRoutesDescriptionList.setRoutes(new ArrayList<Route>(Arrays.asList(route_1, route_2)));
        assertEquals(route_1, mRoutesDescriptionList.getRoutes().get(0));

        // descending by tta
        mRoutesDescriptionList.setSortType(RouteDescriptionList.SortType.TOTAL_TIME);
        mRoutesDescriptionList.setSortOrder(RouteDescriptionList.SortOrder.DESCENDING);
        mRoutesDescriptionList.setRoutes(new ArrayList<Route>(Arrays.asList(route_1, route_2)));
        assertEquals(route_1, mRoutesDescriptionList.getRoutes().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setSetNullSortTypeException() {
        mRoutesDescriptionList.setSortType(null);
    }

    @Test
    public void testSetGetSortOrder() {
        mRoutesDescriptionList.setSortOrder(RouteDescriptionList.SortOrder.DESCENDING);
        assertEquals(RouteDescriptionList.SortOrder.DESCENDING, mRoutesDescriptionList.getSortOrder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullSortOrderException() {
        mRoutesDescriptionList.setSortOrder(null);
    }

    @Test
    public void testSetGetTrafficEnabled() {
        mRoutesDescriptionList.setTrafficEnabled(true);
        assertTrue(mRoutesDescriptionList.isTrafficEnabled());
    }
}
