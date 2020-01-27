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

import android.content.Context;
import android.text.TextUtils;

import com.here.android.mpa.common.RoadElement;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Maneuver.Icon;
import com.here.android.mpa.routing.Maneuver.Turn;
import com.here.android.mpa.routing.Signpost;
import com.here.msdkui.R;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A resource provider for {@link Maneuver}.
 */
@SuppressWarnings("PMD.GodClass")
public class ManeuverResources {
    private static final int NEXT_NEXT_MANEUVER_THRESHOLD = 750;
    private static final Map<Enum, Integer> RESOURCE_MAP = new ConcurrentHashMap<>();
    private final Context mContext;
    private final List<Maneuver> mManeuverList;

    @SuppressWarnings("PMD.NcssCount") // There is no purpose to break this constuctor into smaller functions.
    /**
     * Constructs a new instance using a list of maneuvers.
     */
    public ManeuverResources(final Context context, final List<Maneuver> maneuverList) {
        mContext = context.getApplicationContext();
        mManeuverList = maneuverList;
        if (RESOURCE_MAP.isEmpty()) {
            // Actions
            RESOURCE_MAP.put(Maneuver.Action.END, R.string.msdkui_maneuver_arrive_at_02y);
            RESOURCE_MAP.put(Maneuver.Action.ENTER_HIGHWAY, R.string.msdkui_maneuver_enter_highway);
            RESOURCE_MAP.put(Maneuver.Action.ENTER_HIGHWAY_FROM_LEFT, R.string.msdkui_maneuver_turn_keep_right);
            RESOURCE_MAP.put(Maneuver.Action.ENTER_HIGHWAY_FROM_RIGHT, R.string.msdkui_maneuver_turn_keep_left);
            RESOURCE_MAP.put(Maneuver.Action.LEAVE_HIGHWAY, R.string.msdkui_maneuver_leave_highway);
            RESOURCE_MAP.put(Maneuver.Action.UTURN, R.string.msdkui_maneuver_uturn);

            // Turns
            RESOURCE_MAP.put(Turn.HEAVY_LEFT, R.string.msdkui_maneuver_turn_sharply_left);
            RESOURCE_MAP.put(Turn.HEAVY_RIGHT, R.string.msdkui_maneuver_turn_sharply_right);
            RESOURCE_MAP.put(Turn.KEEP_LEFT, R.string.msdkui_maneuver_turn_keep_left);
            RESOURCE_MAP.put(Turn.KEEP_MIDDLE, R.string.msdkui_maneuver_turn_keep_middle);
            RESOURCE_MAP.put(Turn.KEEP_RIGHT, R.string.msdkui_maneuver_turn_keep_right);
            RESOURCE_MAP.put(Turn.LIGHT_LEFT, R.string.msdkui_maneuver_turn_slightly_left);
            RESOURCE_MAP.put(Turn.LIGHT_RIGHT, R.string.msdkui_maneuver_turn_slightly_right);
            RESOURCE_MAP.put(Turn.QUITE_LEFT, R.string.msdkui_maneuver_turn_left);
            RESOURCE_MAP.put(Turn.QUITE_RIGHT, R.string.msdkui_maneuver_turn_right);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_1, R.string.msdkui_maneuver_turn_roundabout_exit_1);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_2, R.string.msdkui_maneuver_turn_roundabout_exit_2);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_3, R.string.msdkui_maneuver_turn_roundabout_exit_3);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_4, R.string.msdkui_maneuver_turn_roundabout_exit_4);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_5, R.string.msdkui_maneuver_turn_roundabout_exit_5);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_6, R.string.msdkui_maneuver_turn_roundabout_exit_6);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_7, R.string.msdkui_maneuver_turn_roundabout_exit_7);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_8, R.string.msdkui_maneuver_turn_roundabout_exit_8);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_9, R.string.msdkui_maneuver_turn_roundabout_exit_9);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_10, R.string.msdkui_maneuver_turn_roundabout_exit_10);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_11, R.string.msdkui_maneuver_turn_roundabout_exit_11);
            RESOURCE_MAP.put(Turn.ROUNDABOUT_12, R.string.msdkui_maneuver_turn_roundabout_exit_12);
        }
    }

    /**
     * Gets start icon id.
     *
     * @return the icon id for start, as defined by {@link Maneuver} class.
     */
    public int getStartIconId() {
        return getManeuverIconId(Icon.START);
    }

    /**
     * Gets destination icon id.
     *
     * @return the icon id for end, as defined by {@link Maneuver} class.
     */
    public int getDestinationIconId() {
        return getManeuverIconId(Icon.END);
    }

    /**
     * Gets icon id for a given index of maneuver list.
     *
     * @param index the index of the manuever.
     * @return the icon id of the maneuver found at given list index or 0 if no icon is found.
     */
    public int getManeuverIconId(final int index) {
        final Maneuver maneuver = getManeuverAt(index);
        if (maneuver == null) {
            return 0;
        }
        if (maneuver.getAction() == com.here.android.mpa.routing.Maneuver.Action.FERRY && isCarShuttleTrainManeuver(maneuver)) {
            return R.drawable.ic_maneuver_icon_motorail;
        }
        return getManeuverIconId(maneuver.getIcon());
    }

    /**
     * Gets maneuver instruction at index.
     * <p>Please note that if maneuver is not mapped to any instruction string, then a 'head to
     * orientation' instruction is returned.</p>
     *
     * @param index the index of the manuever.
     * @return the maneuver instruction found at given list index or empty string if no maneuver is found.
     */
    public String getManeuverInstruction(final int index) {
        final Maneuver maneuver = getManeuverAt(index);
        if (maneuver == null) {
            return "";
        }
        String instruction = getInstruction(maneuver);
        if (instruction == null || TextUtils.isEmpty(instruction)) {
            instruction = mContext.getString(R.string.msdkui_maneuver_head_to, getLocalizedOrientation(mContext, maneuver
                    .getMapOrientation()));
        }
        return instruction;
    }

    /**
     * Gets distance from maneuver at index to next {@link Maneuver}.
     *
     * @param index the index of the maneuver to use.
     * @return the distance in meters to next maneuver or 0 if no next maneuver was found.
     */
    public int getDistanceFromNext(final int index) {
        final Maneuver nextManeuver = getManeuverAt(index + 1);
        if (nextManeuver != null) {
            return nextManeuver.getDistanceFromPreviousManeuver();
        }
        return 0;
    }

    /**
     * Gets the road name for a {@link Maneuver} at the given indes.
     *
     * @param index the index of the maneuver to use.
     * @return the road name.
     */
    public String getRoadToDisplay(final int index) {
        final String exitDir = getExitDirections(getManeuverAt(index));
        if (exitDir != null) {
            return mContext.getString(R.string.msdkui_maneuver_exit_directions_towards, getRoadName(index), exitDir);
        }
        return getRoadName(index);
    }

    /**
     * Gets the exit direction for given {@link Maneuver}.
     *
     * @param maneuver the maneuver for which the exit direction will be returned.
     * @return the exit direction or null if no result.
     */
    public String getExitDirections(final Maneuver maneuver) {
        final Signpost signpost = maneuver.getSignpost();
        if (signpost == null) {
            return null;
        }
        final List<Signpost.LocalizedLabel> exitDirections = signpost.getExitDirections();
        if (exitDirections == null) {
            return null;
        }
        String exitDirectionsText = null;
        for (Signpost.LocalizedLabel label:exitDirections) {
            final String text = label.getText();
            if (!TextUtils.isEmpty(text)) {
                exitDirectionsText = exitDirectionsText == null ? text :
                        mContext.getString(R.string.msdkui_maneuver_road_name_divider, exitDirectionsText, text);
            }
        }
        return exitDirectionsText;
    }

    /**
     * Gets road number and name for maneuver specified at given index in the given list.
     */
    private String getRoadName(final int index) {
        final Maneuver currentManeuver = getManeuverAt(index);
        if (currentManeuver == null) {
            return "";
        }
        String roadName = currentManeuver.getRoadName();
        String roadNumber = currentManeuver.getRoadNumber();
        if (isManeuverChangingRoad(currentManeuver) || index == 0) {
            roadName = currentManeuver.getNextRoadName();
            roadNumber = currentManeuver.getNextRoadNumber();
        }
        String formattedRoad = format(roadName, roadNumber);
        if (TextUtils.isEmpty(formattedRoad)) {
            formattedRoad = determineNextManeuverStreet(index);
        }
        return formattedRoad;
    }

    /**
     * Gets Next Maneuver Street name.
     */
    private String determineNextManeuverStreet(final int index) {
        int distance = 0;
        int i = index;
        String nextManeuverStreetValue = null;
        Maneuver afterNextManeuver = getManeuverAt(++i);
        while (distance < NEXT_NEXT_MANEUVER_THRESHOLD && afterNextManeuver != null && nextManeuverStreetValue == null) {
            distance += afterNextManeuver.getDistanceFromPreviousManeuver();
            nextManeuverStreetValue = format(afterNextManeuver.getNextRoadName(), afterNextManeuver.getNextRoadNumber());
            afterNextManeuver = getManeuverAt(++i);
        }

        return nextManeuverStreetValue;
    }

    /**
     * Gets Maneuver at given index in given list of Maneuver.
     */
    private Maneuver getManeuverAt(final int maneuverIndex) {
        if (mManeuverList != null && maneuverIndex >= 0 && maneuverIndex < mManeuverList.size()) {
            return mManeuverList.get(maneuverIndex);
        }
        return null;
    }

    /**
     * Formats road and number in {@code number /road } form.
     */
    private String format(final String roadName, final String roadNumber) {
        if (TextUtils.isEmpty(roadNumber)) {
            return roadName;
        } else if (TextUtils.isEmpty(roadName)) {
            return roadNumber;
        } else {
            return mContext.getString(R.string.msdkui_maneuver_road_name_divider, roadNumber, roadName);
        }
    }

    /**
     * Gets if Maneuver is changing road.
     */

    private boolean isManeuverChangingRoad(final Maneuver maneuver) {
        if (maneuver != null) {
            final Maneuver.Action action = maneuver.getAction();
            return action == Maneuver.Action.JUNCTION || action == Maneuver.Action.ROUNDABOUT;
        }
        return false;
    }

    /**
     * Gets Maneuver icon id.
     */
    private int getManeuverIconId(final Icon icon) {
        if (icon == null || icon == Icon.PASS_STATION) {
            return 0; // No id in this case.
        }

        final String resourceName = "ic_maneuver_icon_" + icon.ordinal();
        return mContext.getResources()
                .getIdentifier(resourceName, "drawable", mContext.getPackageName());
    }

    /**
     * Gets Localized orientation.
     */
    private String getLocalizedOrientation(final Context context, final int angleInDegrees) {
        final int[] ids = {R.string.msdkui_maneuver_orientation_north, R.string.msdkui_maneuver_orientation_north_east, R.string
                .msdkui_maneuver_orientation_east, R.string.msdkui_maneuver_orientation_south_east, R.string
                .msdkui_maneuver_orientation_south, R.string.msdkui_maneuver_orientation_south_west, R.string
                .msdkui_maneuver_orientation_west, R.string.msdkui_maneuver_orientation_north_west};
        final int index = ((angleInDegrees + 45 / 2) % 360) / 45;
        // Normally, this should never happen.
        if (index < 0 || index >= ids.length) {
            return String.valueOf(angleInDegrees);
        }
        return context.getString(ids[index]);
    }

    /**
     * Gets Instruction for given Maneuver.
     */
    private String getInstruction(final Maneuver maneuver) {
        final Maneuver.Action action = maneuver.getAction();
        String ret;
        switch (action) {
            case CHANGE_HIGHWAY:
            case CONTINUE_HIGHWAY:
                ret = getHighwayInstructions(maneuver.getTurn());
                break;
            case FERRY:
                ret = getString(isCarShuttleTrainManeuver(maneuver) ? R.string.msdkui_maneuver_enter_car_shuttle_train : R.string
                        .msdkui_maneuver_enter_ferry);
                break;
            case JUNCTION:
            case ROUNDABOUT:
                ret = getString(RESOURCE_MAP.get(maneuver.getTurn()));
                break;
            default:
                ret = getString(RESOURCE_MAP.get(action));
                break;
        }
        return ret;
    }

    /**
     * Get localized string from the application's package's default string table.
     *
     * @param id resource id for the string.
     * @return the string data associated with the resource or null if id is invalid.
     */
    public String getString(final Integer id) {
        if (id == null || id == 0) {
            return null;
        }
        return mContext.getString(id);
    }

    /**
     * Get the localized "You've arrived" string to indicate that destination was reached.
     *
     * @return the localized "You've arrived" string.
     */
    public String getArrivedAtDestinationInstruction() {
        return mContext.getString(R.string.msdkui_maneuver_end);
    }

    /**
     * Gets Highway instruction for given Maneuver.
     */
    private String getHighwayInstructions(final Turn turn) {
        if (turn == Turn.KEEP_LEFT || turn == Turn.KEEP_MIDDLE || turn == Turn.KEEP_RIGHT) {
            return getString(RESOURCE_MAP.get(turn));
        }

        return mContext.getString(R.string.msdkui_maneuver_continue);
    }

    /**
     * Gets if {@link Maneuver} is {@link com.here.android.mpa.common.RoadElement.Attribute#CAR_SHUTTLE_TRAIN}.
     */
    private boolean isCarShuttleTrainManeuver(final Maneuver maneuver) {
        final List<RoadElement> roadElements = maneuver.getRoadElements();
        return roadElements != null && !roadElements.isEmpty() && roadElements.get(0)
                .getAttributes()
                .contains(RoadElement.Attribute.CAR_SHUTTLE_TRAIN);
    }
}
