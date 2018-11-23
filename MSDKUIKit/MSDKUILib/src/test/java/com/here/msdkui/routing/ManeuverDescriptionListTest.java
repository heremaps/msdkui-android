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

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test class for {@link ManeuverDescriptionList} class.
 */
@PrepareForTest({ RouteTta.class, RouteElements.class, RouteElement.class, RoadElement.class,
        RoutePlan.class })
public class ManeuverDescriptionListTest extends RobolectricTest {

    private ManeuverDescriptionList mManeuverDescriptionList;

    @Before
    public void setUp() {
        mManeuverDescriptionList = new ManeuverDescriptionList(getContextWithTheme());
    }

    @Test
    public void initShouldHaveProperDefaultValues() {
        assertNotNull(mManeuverDescriptionList.getAdapter());
        assertThat(mManeuverDescriptionList.getAdapter().getItemCount(), equalTo(0));
        assertNull(mManeuverDescriptionList.getRoute());
    }

    @Test
    public void setRouteShouldAddManeuverRow() {
        final Route route = new MockUtils.MockRouteBuilder().setListOfManeuver()
                .getRoute();
        mManeuverDescriptionList.setRoute(route);
        assertThat(mManeuverDescriptionList.getAdapter().getItemCount(), equalTo(1));
        assertNotNull(mManeuverDescriptionList.getRoute());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullRouteException() {
        mManeuverDescriptionList.setRoute(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRouteWithNullManeuverListException() {
        Route route = mock(Route.class);
        when(route.getManeuvers()).thenReturn(null);
        mManeuverDescriptionList.setRoute(route);
    }
}
