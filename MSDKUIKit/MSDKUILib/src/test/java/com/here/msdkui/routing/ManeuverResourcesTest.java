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

import com.here.RobolectricTest;
import com.here.android.mpa.common.RoadElement;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Maneuver.Action;
import com.here.android.mpa.routing.Maneuver.Turn;
import com.here.android.mpa.routing.Signpost;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ManeuverResources} class.
 */
public class ManeuverResourcesTest extends RobolectricTest {

    private static final String DIRECTION = "left";

    private ManeuverResources mManeuverResources;

    @Before
    public void setUp() {
    }

    @Test
    public void testJunctionTurnHeavyLeft() {
        junctionManeuverTurnTest(Turn.HEAVY_LEFT, R.string.msdkui_maneuver_turn_sharply_left);
    }

    @Test
    public void testJunctionTurnHeavyRight() {
        junctionManeuverTurnTest(Turn.HEAVY_RIGHT, R.string.msdkui_maneuver_turn_sharply_right);
    }

    @Test
    public void testJunctionTurnKeepLeft() {
        junctionManeuverTurnTest(Turn.KEEP_LEFT, R.string.msdkui_maneuver_turn_keep_left);
    }

    @Test
    public void testJunctionTurnKeepMiddle() {
        junctionManeuverTurnTest(Turn.KEEP_MIDDLE, R.string.msdkui_maneuver_turn_keep_middle);
    }

    @Test
    public void testJunctionTurnKeepRight() {
        junctionManeuverTurnTest(Turn.KEEP_RIGHT, R.string.msdkui_maneuver_turn_keep_right);
    }

    @Test
    public void testJunctionTurnLightLeft() {
        junctionManeuverTurnTest(Turn.LIGHT_LEFT, R.string.msdkui_maneuver_turn_slightly_left);
    }

    @Test
    public void testJunctionTurnLightRight() {
        junctionManeuverTurnTest(Turn.LIGHT_RIGHT, R.string.msdkui_maneuver_turn_slightly_right);
    }

    @Test
    public void testJunctionTurnQuiteLeft() {
        junctionManeuverTurnTest(Turn.QUITE_LEFT, R.string.msdkui_maneuver_turn_left);
    }

    @Test
    public void testJunctionTurnQuiteRight() {
        junctionManeuverTurnTest(Turn.QUITE_RIGHT, R.string.msdkui_maneuver_turn_right);
    }

    public void junctionManeuverTurnTest(final Turn maneuverTurn, final int expectedString) {
        final Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getAction()).thenReturn(Action.JUNCTION);
        when(maneuver.getTurn()).thenReturn(maneuverTurn);
        mManeuverResources = new ManeuverResources(getApplicationContext(), new ArrayList<>(Collections.singletonList(maneuver)));
        final String maneuverInstruction = mManeuverResources.getManeuverInstruction(0);
        assertThat(maneuverInstruction, equalTo(getStringFromContext(expectedString)));
    }

    @Test
    public void testManeuverActionChangeHighway() {
        maneuverAction(Action.CHANGE_HIGHWAY, getStringFromContext(R.string.msdkui_maneuver_continue));
    }

    @Test
    public void testManeuverActionChangeHighwayKeepLeft() {
        maneuverActionTurn(Action.CHANGE_HIGHWAY, Turn.KEEP_LEFT, getStringFromContext(R.string.msdkui_maneuver_turn_keep_left));
    }

    @Test
    public void testManeuverActionContinueHighway() {
        maneuverAction(Action.CONTINUE_HIGHWAY, getStringFromContext(R.string.msdkui_maneuver_continue));
    }

    @Test
    public void testManeuverActionContinueHighwayKeepRight() {
        maneuverActionTurn(Action.CONTINUE_HIGHWAY, Turn.KEEP_RIGHT, getStringFromContext(R.string.msdkui_maneuver_turn_keep_right));
    }

    @Test
    public void testManeuverActionEnd() {
        maneuverAction(Action.END, getStringFromContext(R.string.msdkui_maneuver_arrive_at_02y));
    }

    @Test
    public void testManeuverEnterHighway() {
        maneuverAction(Action.ENTER_HIGHWAY, getStringFromContext(R.string.msdkui_maneuver_enter_highway));
    }

    @Test
    public void testManeuverEnterHighwayFromLeft() {
        maneuverAction(Action.ENTER_HIGHWAY_FROM_LEFT, getStringFromContext(R.string.msdkui_maneuver_turn_keep_right));
    }

    @Test
    public void testManeuverEnterHighwayFromRight() {
        maneuverAction(Action.ENTER_HIGHWAY_FROM_RIGHT, getStringFromContext(R.string.msdkui_maneuver_turn_keep_left));
    }

    @Test
    public void testManeuverLeaveHighway() {
        maneuverAction(Action.LEAVE_HIGHWAY, getStringFromContext(R.string.msdkui_maneuver_leave_highway));
    }

    @Test
    public void testManeuverActionFerryForFerries() {
        maneuverAction(Action.FERRY, getStringFromContext(R.string.msdkui_maneuver_enter_ferry));
    }

    @Test
    public void testManeuverActionFerryForCarShuttleTrains() {
        Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getAction()).thenReturn(Action.FERRY);
        when(maneuver.getIcon()).thenReturn(com.here.android.mpa.routing.Maneuver.Icon.FERRY);
        RoadElement carShuttleRoadElement = mock(RoadElement.class);
        when(carShuttleRoadElement.getAttributes()).thenReturn(EnumSet.of(RoadElement.Attribute.CAR_SHUTTLE_TRAIN, RoadElement.Attribute
                .FERRY));
        when(maneuver.getRoadElements()).thenReturn(Collections.singletonList(carShuttleRoadElement));
        mManeuverResources = new ManeuverResources(getApplicationContext(), new ArrayList<>(Collections.singletonList(maneuver)));
        assertThat(mManeuverResources.getManeuverInstruction(0),
                equalTo(getStringFromContext(R.string.msdkui_maneuver_enter_car_shuttle_train)));
    }

    @Test
    public void testManeuverActionUturn() {
        maneuverAction(Action.UTURN, getStringFromContext(R.string.msdkui_maneuver_uturn));
    }

    public void maneuverAction(Action maneuverAction, String expectedString) {
        Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getAction()).thenReturn(maneuverAction);
        mManeuverResources = new ManeuverResources(getApplicationContext(), new ArrayList<>(Collections.singletonList(maneuver)));
        String maneuverInstruction = mManeuverResources.getManeuverInstruction(0);
        assertThat(maneuverInstruction, equalTo(expectedString));
    }

    public void maneuverActionTurn(Action maneuverAction, Turn maneuverTurn, String expectedString) {
        Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getAction()).thenReturn(maneuverAction);
        when(maneuver.getTurn()).thenReturn(maneuverTurn);
        mManeuverResources = new ManeuverResources(getApplicationContext(), new ArrayList<>(Collections.singletonList(maneuver)));
        final String maneuverInstruction = mManeuverResources.getManeuverInstruction(0);
        assertThat(maneuverInstruction, equalTo(expectedString));
    }

    private String getStringFromContext(int stringId) {
        return getApplicationContext().getResources()
                .getString(stringId);
    }

    @Test
    public void testRoundAbout1() {
        roundAboutActionTest(Turn.ROUNDABOUT_1, R.string.msdkui_maneuver_turn_roundabout_exit_1);
    }

    @Test
    public void testRoundAbout2() {
        roundAboutActionTest(Turn.ROUNDABOUT_2, R.string.msdkui_maneuver_turn_roundabout_exit_2);
    }

    @Test
    public void testRoundAbout3() {
        roundAboutActionTest(Turn.ROUNDABOUT_3, R.string.msdkui_maneuver_turn_roundabout_exit_3);
    }

    @Test
    public void testRoundAbout4() {
        roundAboutActionTest(Turn.ROUNDABOUT_4, R.string.msdkui_maneuver_turn_roundabout_exit_4);
    }

    @Test
    public void testRoundAbout5() {
        roundAboutActionTest(Turn.ROUNDABOUT_5, R.string.msdkui_maneuver_turn_roundabout_exit_5);
    }

    @Test
    public void testRoundAbout6() {
        roundAboutActionTest(Turn.ROUNDABOUT_6, R.string.msdkui_maneuver_turn_roundabout_exit_6);
    }

    @Test
    public void testRoundAbout7() {
        roundAboutActionTest(Turn.ROUNDABOUT_7, R.string.msdkui_maneuver_turn_roundabout_exit_7);
    }

    @Test
    public void testRoundAbout8() {
        roundAboutActionTest(Turn.ROUNDABOUT_8, R.string.msdkui_maneuver_turn_roundabout_exit_8);
    }

    @Test
    public void testRoundAbout9() {
        roundAboutActionTest(Turn.ROUNDABOUT_9, R.string.msdkui_maneuver_turn_roundabout_exit_9);
    }

    @Test
    public void testRoundAbout10() {
        roundAboutActionTest(Turn.ROUNDABOUT_10, R.string.msdkui_maneuver_turn_roundabout_exit_10);
    }

    @Test
    public void testRoundAbout11() {
        roundAboutActionTest(Turn.ROUNDABOUT_11, R.string.msdkui_maneuver_turn_roundabout_exit_11);
    }

    @Test
    public void testRoundAbout12() {
        roundAboutActionTest(Turn.ROUNDABOUT_12, R.string.msdkui_maneuver_turn_roundabout_exit_12);
    }

    private void roundAboutActionTest(Turn maneuverTurn, int expectedString) {
        Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getAction()).thenReturn(Action.ROUNDABOUT);
        when(maneuver.getTurn()).thenReturn(maneuverTurn);
        mManeuverResources = new ManeuverResources(getApplicationContext(), new ArrayList<>(Collections.singletonList(maneuver)));
        final String maneuverInstruction = mManeuverResources.getManeuverInstruction(0);
        assertThat(maneuverInstruction, equalTo(getStringFromContext(expectedString)));
    }

    @Test
    public void testNullManeuver() {
        mManeuverResources = new ManeuverResources(getApplicationContext(), null);
        String maneuverInstruction = mManeuverResources.getManeuverInstruction(-1);
        assertThat(maneuverInstruction, is(isEmptyString()));
        maneuverInstruction = mManeuverResources.getManeuverInstruction(0);
        assertThat(maneuverInstruction, is(isEmptyString()));
    }

    @Test
    public void testUndefinedManeuverAction() {
        Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getAction()).thenReturn(Action.UNDEFINED);
        fallthroughManeuverAction(maneuver);
    }

    @Test
    public void testNoActionManeuverAction() {
        Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getAction()).thenReturn(Action.NO_ACTION);
        fallthroughManeuverAction(maneuver);
    }

    @Test
    public void testUndefinedTurnManeuverAction() {
        Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getAction()).thenReturn(Action.JUNCTION);
        when(maneuver.getTurn()).thenReturn(Turn.UNDEFINED);
        fallthroughManeuverAction(maneuver);
    }

    @Test
    public void testUndefinedRoundaboutManeuverAction() {
        Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getAction()).thenReturn(Action.ROUNDABOUT);
        when(maneuver.getTurn()).thenReturn(Turn.UNDEFINED);
        fallthroughManeuverAction(maneuver);
    }

    private void fallthroughManeuverAction(Maneuver maneuver) {
        when(maneuver.getMapOrientation()).thenReturn(17);
        mManeuverResources = new ManeuverResources(getApplicationContext(), new ArrayList<>(Collections.singletonList(maneuver)));
        String maneuverInstruction = mManeuverResources.getManeuverInstruction(0);
        assertThat(maneuverInstruction,
                equalTo(getApplicationContext().getString(R.string.msdkui_maneuver_head_to,
                        getApplicationContext().getString(R.string.msdkui_maneuver_orientation_north))
                ));
    }

    @Test
    public void testGetManeuverIconIdReturnsCarShuttleTrainIconForCarShuttleFerryManeuvers() {
        Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getAction()).thenReturn(Action.FERRY);
        when(maneuver.getIcon()).thenReturn(com.here.android.mpa.routing.Maneuver.Icon.FERRY);
        RoadElement carShuttleRoadElement = mock(RoadElement.class);
        when(carShuttleRoadElement.getAttributes()).thenReturn(EnumSet.of(RoadElement.Attribute.CAR_SHUTTLE_TRAIN, RoadElement.Attribute
                .FERRY));
        when(maneuver.getRoadElements()).thenReturn(Collections.singletonList(carShuttleRoadElement));
        mManeuverResources = new ManeuverResources(getApplicationContext(), new ArrayList<>(Collections.singletonList(maneuver)));
        assertThat(mManeuverResources.getManeuverIconId(0), equalTo(R.drawable.ic_maneuver_icon_motorail));
    }

    @Test
    public void testGetManeuverIconIdReturnsFerryIconForBoatFerryManeuvers() {
        Maneuver maneuver = mock(Maneuver.class);
        when(maneuver.getAction()).thenReturn(Action.FERRY);
        when(maneuver.getIcon()).thenReturn(com.here.android.mpa.routing.Maneuver.Icon.FERRY);
        RoadElement carShuttleRoadElement = mock(RoadElement.class);
        when(carShuttleRoadElement.getAttributes()).thenReturn(EnumSet.of(RoadElement.Attribute.FERRY));
        when(maneuver.getRoadElements()).thenReturn(Collections.singletonList(carShuttleRoadElement));
        mManeuverResources = new ManeuverResources(getApplicationContext(), new ArrayList<>(Collections.singletonList(maneuver)));
        assertThat(mManeuverResources.getManeuverIconId(0), equalTo(R.drawable.ic_maneuver_icon_45));
    }

    @Test
    public void testGetArrivedAtDestinationInstructionReturnsArrivalString() {
        mManeuverResources = new ManeuverResources(getApplicationContext(), null);
        String arrivedAtDestinationInstruction = mManeuverResources.getArrivedAtDestinationInstruction();
        assertThat(arrivedAtDestinationInstruction, equalToIgnoringCase(getStringFromContext(R.string.msdkui_maneuver_end)));
    }

    @Test
    public void testGetExitDirections() {
        Maneuver maneuver = mock(Maneuver.class);
        Signpost signpost = mock(Signpost.class);

        List<Signpost.LocalizedLabel> list = getDirectionsList();
        when(signpost.getExitDirections()).thenReturn(list);
        when(maneuver.getSignpost()).thenReturn(signpost);

        mManeuverResources = new ManeuverResources(getApplicationContext(), null);
        assertEquals(DIRECTION, mManeuverResources.getExitDirections(maneuver));
    }

    private static List<Signpost.LocalizedLabel> getDirectionsList() {
        List<Signpost.LocalizedLabel> directions = new ArrayList<>();
        Signpost.LocalizedLabel direction = mock(Signpost.LocalizedLabel.class);
        when(direction.getText()).thenReturn(DIRECTION);
        directions.add(direction);
        return directions;
    }
}
