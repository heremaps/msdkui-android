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

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.routing.Route;
import com.here.msdkui.R;
import com.here.msdkui.common.measurements.UnitSystem;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Test class for {@link RouteDescriptionListAdapter} class.
 */
public class RouteDescriptionsListAdapterTest extends RobolectricTest {

    private RouteDescriptionListAdapter mRoutesDescriptionsListAdapter;
    private RouteDescriptionListAdapter.ViewHolder mViewHolder;

    @Before
    public void setUp() {
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
        getFirstViewHolder(new ArrayList<>(Collections.singletonList(route)));
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

    @Test
    public void testSetGetUnitSystem() {
        final Route route = new MockUtils.MockRouteBuilder().getRoute();
        mRoutesDescriptionsListAdapter = new RouteDescriptionListAdapter(Arrays.asList(route, route));
        assertEquals(mRoutesDescriptionsListAdapter.getUnitSystem(), UnitSystem.METRIC);
        mRoutesDescriptionsListAdapter.setUnitSystem(UnitSystem.IMPERIAL_UK);
        assertEquals(mRoutesDescriptionsListAdapter.getUnitSystem(), UnitSystem.IMPERIAL_UK);
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
