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
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.RoadElement;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteElement;
import com.here.android.mpa.routing.RouteElements;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteTta;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * Test class for {@link RouteDescriptionListAdapter} class.
 */
@PrepareForTest({GeoCoordinate.class, RouteTta.class, RouteElements.class, RouteElement.class, RoadElement.class, RoutePlan.class})
@PowerMockIgnore("com.here.msdkui.routing.SectionBar")
public class RouteDescriptionsListAdapterTest extends RobolectricTest {

    private boolean mIsCallbackCalled;
    private RouteDescriptionListAdapter mRoutesDescriptionsListAdapter;
    private RouteDescriptionListAdapter.ViewHolder mViewHolder;

    @Before
    public void setUp() {
        mIsCallbackCalled = false;
    }

    @Test
    public void itemCount() {
        final Route route = new MockUtils.MockRouteBuilder().getRoute();
        mRoutesDescriptionsListAdapter = new RouteDescriptionListAdapter(Arrays.asList(route, route));
        assertThat(mRoutesDescriptionsListAdapter.getItemCount(), equalTo(2));
    }

    @Test
    public void testUi() {
        final Route route = new MockUtils.MockRouteBuilder().getRoute();
        getFirstViewHolder(new ArrayList<Route>(Arrays.asList(route)));
        assertNotNull(mViewHolder.itemView.findViewById(R.id.desc_type_icon));
        assertNotNull(mViewHolder.itemView.findViewById(R.id.desc_details));
        assertNotNull(mViewHolder.itemView.findViewById(R.id.desc_traffic_warning));
    }

    @Test
    public void testTrafficeEnabled() {
        final Route route = new MockUtils.MockRouteBuilder().getRoute();
        mRoutesDescriptionsListAdapter = new RouteDescriptionListAdapter(Arrays.asList(route, route));
        mRoutesDescriptionsListAdapter.setTrafficEnabled(true);
        assertTrue(mRoutesDescriptionsListAdapter.isTrafficEnabled());
    }

    private void getFirstViewHolder(final List<Route> routes) {
        mRoutesDescriptionsListAdapter = new RouteDescriptionListAdapter(routes);
        assertThat(mRoutesDescriptionsListAdapter.getItemCount(), equalTo(routes.size()));
        final RouteDescriptionItem itemView = new RouteDescriptionItem(getApplicationContext());
        mViewHolder = spy(mRoutesDescriptionsListAdapter.new ViewHolder(itemView));
        doReturn(0).when(mViewHolder)
                .getAdapterPosition();
        mRoutesDescriptionsListAdapter.onBindViewHolder(mViewHolder, 0);
    }
}
