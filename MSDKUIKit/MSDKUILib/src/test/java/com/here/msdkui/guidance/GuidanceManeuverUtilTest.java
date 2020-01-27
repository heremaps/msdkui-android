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

package com.here.msdkui.guidance;

import android.content.Context;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Signpost;

import org.junit.Test;
import org.mockito.Matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link GuidanceManeuverUtil}.
 */
public class GuidanceManeuverUtilTest extends RobolectricTest {

    @Test
    public void testIsManeuverEquals() {
        final Maneuver maneuver1 = MockUtils.mockManeuver();
        final GeoCoordinate coordinate1 = MockUtils.mockGeoCoordinate(1, 1);
        when(maneuver1.getCoordinate()).thenReturn(coordinate1);
        final Maneuver maneuver2 = MockUtils.mockManeuver();
        final GeoCoordinate coordinate2 = MockUtils.mockGeoCoordinate(2, 2);
        when(maneuver2.getCoordinate()).thenReturn(coordinate2);
        assertFalse(GuidanceManeuverUtil.maneuversEqual(maneuver1, maneuver2));
        assertTrue(GuidanceManeuverUtil.maneuversEqual(maneuver1, maneuver1));
    }

    @Test
    public void testGetIndexOfManeuver() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        final Maneuver maneuver1 = MockUtils.mockManeuver();
        final GeoCoordinate coordinate = MockUtils.mockGeoCoordinate(1, 1);
        final GeoCoordinate coordinate1 = MockUtils.mockGeoCoordinate(2, 2);
        when(maneuver.getCoordinate()).thenReturn(coordinate);
        when(maneuver1.getCoordinate()).thenReturn(coordinate1);
        int index = GuidanceManeuverUtil.getIndexOfManeuver(maneuver,
                new ArrayList<>(Arrays.asList(maneuver, maneuver1)));
        assertThat(index, is(0));
        index = GuidanceManeuverUtil.getIndexOfManeuver(maneuver,
                new ArrayList<>(Collections.singletonList(maneuver1)));
        assertThat(index, is(-1));
    }

    @Test
    public void testNextManeuverStreetName() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        when(maneuver.getNextRoadName()).thenReturn(MockUtils.ROAD_NAME);
        when(maneuver.getNextRoadNumber()).thenReturn(MockUtils.ROAD_NUMBER);
        final Context context = mock(Context.class);
        doAnswer(invocation -> {
            return invocation.getArguments()[1] + " " + invocation.getArguments()[2];
        }).when(context).getString(anyInt(), Matchers.<String>anyVararg());
        String street = GuidanceManeuverUtil.determineNextManeuverStreet(context, maneuver,
                mock(GuidanceManeuverPresenter.class));
        assertThat(street, is(MockUtils.ROAD_NUMBER + " " + MockUtils.ROAD_NAME));
    }

    @Test
    public void testNextManeuverStreetNameWhenSignPost() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        when(maneuver.getNextRoadName()).thenReturn(MockUtils.ROAD_NAME);
        when(maneuver.getNextRoadNumber()).thenReturn(MockUtils.ROAD_NUMBER);
        when(maneuver.getAction()).thenReturn(Maneuver.Action.LEAVE_HIGHWAY);

        Signpost signpost = mock(Signpost.class);
        when(signpost.getExitNumber()).thenReturn("52");
        when(maneuver.getSignpost()).thenReturn(signpost);
        final Context context = mock(Context.class);
        doAnswer(invocation -> {
            return invocation.getArguments()[1] + " " + invocation.getArguments()[2];
        }).when(context).getString(anyInt(), Matchers.<String>anyVararg());
        String street = GuidanceManeuverUtil.determineNextManeuverStreet(context, maneuver,
                mock(GuidanceManeuverPresenter.class));
        assertThat(street, is(MockUtils.ROAD_NUMBER + " " + MockUtils.ROAD_NAME));
    }

    @Test
    public void testSignPostStreet() {
        final String signPostExitText = "exitText";
        final Maneuver maneuver = MockUtils.mockManeuver();
        when(maneuver.getNextRoadName()).thenReturn("");
        when(maneuver.getNextRoadNumber()).thenReturn("");
        when(maneuver.getRoadName()).thenReturn("");
        when(maneuver.getRoadNumber()).thenReturn("");
        Signpost signpost = mock(Signpost.class);
        when(signpost.getExitText()).thenReturn(signPostExitText);
        when(maneuver.getSignpost()).thenReturn(signpost);
        final Context context = mock(Context.class);
        doAnswer(invocation -> {
            return invocation.getArguments()[1] + " " + invocation.getArguments()[2];
        }).when(context).getString(anyInt(), Matchers.<String>anyVararg());
        String street = GuidanceManeuverUtil.determineNextManeuverStreet(context, maneuver,
                mock(GuidanceManeuverPresenter.class));
        assertThat(street, is(signPostExitText));
    }

    @Test
    public void testCurrentManeuverStreetName() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        when(maneuver.getNextRoadName()).thenReturn("");
        when(maneuver.getNextRoadNumber()).thenReturn("");
        final Context context = mock(Context.class);
        doAnswer(invocation -> {
            return invocation.getArguments()[1] + " " + invocation.getArguments()[2];
        }).when(context).getString(anyInt(), Matchers.<String>anyVararg());
        String street = GuidanceManeuverUtil.determineNextManeuverStreet(context, maneuver,
                mock(GuidanceManeuverPresenter.class));
        assertThat(street, is(MockUtils.ROAD_NUMBER + " " + MockUtils.ROAD_NAME));
    }
}
