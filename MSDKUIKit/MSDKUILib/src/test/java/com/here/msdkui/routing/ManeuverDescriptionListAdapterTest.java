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

import android.view.ViewGroup;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.routing.Maneuver;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Test class for {@link ManeuverDescriptionListAdapter} class.
 */
public class ManeuverDescriptionListAdapterTest extends RobolectricTest {

    private ManeuverDescriptionListAdapter mManeuverDescriptionListAdapter;
    private ManeuverDescriptionListAdapter.ViewHolder mViewHolder;

    @Before
    public void setUp() {
    }

    @Test
    public void itemCount() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        mManeuverDescriptionListAdapter = new ManeuverDescriptionListAdapter(Arrays.asList(maneuver, maneuver));
        assertThat(mManeuverDescriptionListAdapter.getItemCount(), equalTo(2));
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

        mManeuverDescriptionListAdapter = new ManeuverDescriptionListAdapter(Arrays.asList(maneuver, maneuver));
        ManeuverDescriptionListAdapter.ViewHolder viewHolder =
                mManeuverDescriptionListAdapter.onCreateViewHolder(viewGroup, 0);
        assertNotNull(viewHolder);
    }

    private void getFirstViewHolder(final List<Maneuver> maneuvers) {
        mManeuverDescriptionListAdapter = new ManeuverDescriptionListAdapter(maneuvers);
        assertThat(mManeuverDescriptionListAdapter.getItemCount(), equalTo(maneuvers.size()));
        final ManeuverDescriptionItem itemView = new ManeuverDescriptionItem(getApplicationContext());
        mViewHolder = spy(mManeuverDescriptionListAdapter.new ViewHolder(itemView));
        doReturn(0).when(mViewHolder)
                .getAdapterPosition();
        mManeuverDescriptionListAdapter.onBindViewHolder(mViewHolder, 0);
    }
}
