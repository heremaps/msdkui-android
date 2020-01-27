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

import android.view.ViewGroup;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.routing.Maneuver;
import com.here.msdkui.R;
import com.here.msdkui.common.measurements.UnitSystem;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ManeuverListAdapter} class.
 */
public class ManeuverListAdapterTest extends RobolectricTest {

    private ManeuverListAdapter mManeuverListAdapter;
    private ManeuverListAdapter.ViewHolder mViewHolder;

    @Before
    public void setUp() {
    }

    @Test
    public void itemCount() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        mManeuverListAdapter = new ManeuverListAdapter(Arrays.asList(maneuver, maneuver));
        assertThat(mManeuverListAdapter.getItemCount(), equalTo(2));
    }

    @Test
    public void testUi() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        getFirstViewHolder(new ArrayList<>(Collections.singletonList(maneuver)));
        assertNotNull(mViewHolder.itemView.findViewById(R.id.maneuver_icon_view));
        assertNotNull(mViewHolder.itemView.findViewById(R.id.maneuver_address_view));
        assertNotNull(mViewHolder.itemView.findViewById(R.id.maneuver_distance_view));
        assertNotNull(mViewHolder.itemView.findViewById(R.id.maneuver_instruction_view));
    }

    @Test
    public void testOnCreateViewHolder() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        final ViewGroup viewGroup = mock(ViewGroup.class);

        when(viewGroup.getContext()).thenReturn(getContextWithTheme());

        mManeuverListAdapter = new ManeuverListAdapter(Arrays.asList(maneuver, maneuver));
        ManeuverListAdapter.ViewHolder viewHolder =
                mManeuverListAdapter.onCreateViewHolder(viewGroup, 0);
        assertNotNull(viewHolder);
    }

    @Test
    public void testSetGetUnitSystem() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        mManeuverListAdapter = new ManeuverListAdapter(Arrays.asList(maneuver, maneuver));
        assertEquals(mManeuverListAdapter.getUnitSystem(), UnitSystem.METRIC);
        mManeuverListAdapter.setUnitSystem(UnitSystem.IMPERIAL_UK);
        assertEquals(mManeuverListAdapter.getUnitSystem(), UnitSystem.IMPERIAL_UK);
    }

    private void getFirstViewHolder(final List<Maneuver> maneuvers) {
        mManeuverListAdapter = new ManeuverListAdapter(maneuvers);
        assertThat(mManeuverListAdapter.getItemCount(), equalTo(maneuvers.size()));
        final ManeuverItemView itemView = new ManeuverItemView(getApplicationContext());
        mViewHolder = spy(mManeuverListAdapter.new ViewHolder(itemView));
        doReturn(0).when(mViewHolder)
                .getAdapterPosition();
        mManeuverListAdapter.onBindViewHolder(mViewHolder, 0);
    }
}
