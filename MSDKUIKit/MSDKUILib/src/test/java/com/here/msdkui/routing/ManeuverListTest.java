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

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.common.RoadElement;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteElement;
import com.here.android.mpa.routing.RouteElements;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteTta;
import com.here.msdkui.common.measurements.UnitSystem;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test class for {@link ManeuverList} class.
 */
@PrepareForTest({ RouteTta.class, RouteElements.class, RouteElement.class, RoadElement.class,
        RoutePlan.class })
public class ManeuverListTest extends RobolectricTest {

    private ManeuverList mManeuverList;

    @Before
    public void setUp() {
        mManeuverList = new ManeuverList(getContextWithTheme());
    }

    @Test
    public void initShouldHaveProperDefaultValues() {
        assertNotNull(mManeuverList.getAdapter());
        assertThat(mManeuverList.getAdapter().getItemCount(), equalTo(0));
        assertNull(mManeuverList.getRoute());
    }

    @Test
    public void setRouteShouldAddManeuverRow() {
        final Route route = new MockUtils.MockRouteBuilder().setListOfManeuver()
                .getRoute();
        mManeuverList.setRoute(route);
        assertThat(mManeuverList.getAdapter().getItemCount(), equalTo(1));
        assertNotNull(mManeuverList.getRoute());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullRouteException() {
        mManeuverList.setRoute(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRouteWithNullManeuverListException() {
        Route route = mock(Route.class);
        when(route.getManeuvers()).thenReturn(null);
        mManeuverList.setRoute(route);
    }

    @Test
    public void testSetGetUnitSystem() {
        assertEquals(mManeuverList.getUnitSystem(), UnitSystem.METRIC);
        assertEquals(((ManeuverListAdapter) mManeuverList.getAdapter()).getUnitSystem(),
                UnitSystem.METRIC);

        mManeuverList.setUnitSystem(UnitSystem.IMPERIAL_UK);
        assertEquals(mManeuverList.getUnitSystem(), UnitSystem.IMPERIAL_UK);
        assertEquals(((ManeuverListAdapter) mManeuverList.getAdapter()).getUnitSystem(),
                UnitSystem.IMPERIAL_UK);
    }
}
