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
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.Signpost;
import com.here.msdkui.R;
import com.here.msdkui.guidance.base.BaseGuidancePresenter;

import java.util.List;

/**
 * A convenience class to get {@link Maneuver} related information for guidance.
 */
public final class GuidanceManeuverUtil {

    private static final int NEXT_NEXT_MANEUVER_THRESHOLD = 750;

    private GuidanceManeuverUtil() {
    }

    /**
     * Gets the index of a maneuver with a list of maneuvers. Maneuvers are compared based on location and the action.
     * @param maneuver the maneuver to find.
     * @param maneuvers the list of maneuvers.
     * @return the index of the given maneuver in the given maneuver list or -1 if the maneuver is not part of the list.
     */
    public static int getIndexOfManeuver(Maneuver maneuver, List<Maneuver> maneuvers) {
        for (int x = 0; x < maneuvers.size(); x++) {
            final Maneuver tmpManeuver = maneuvers.get(x);
            if (maneuversEqual(maneuver, tmpManeuver)) {
                return x;
            }
        }
        return -1;
    }

    /**
     * Checks if two {@link Maneuver}s are equals.
     * @param maneuverA - a maneuver.
     * @param maneuverB - a maneuver.
     * @return true if maneuvers are equal, else false.
     */
    public static boolean maneuversEqual(Maneuver maneuverA, Maneuver maneuverB) {
        return maneuverA == null ?
                maneuverB == null :
                maneuverB != null &&
                        maneuverA.getCoordinate().equals(maneuverB.getCoordinate()) &&
                        maneuverA.getAction() == maneuverB.getAction();
    }

    /**
     * Gets street name of the current {@link Maneuver}.
     * @param context - a context.
     * @param maneuver - the maneuver to use.
     * @return a string containing the street of current maneuver.
     */
    public static String getCurrentManeuverStreet(@NonNull Context context, @NonNull Maneuver maneuver) {
        return combineRoadNumberAndName(context, maneuver, maneuver.getRoadNumber(), maneuver.getRoadName());
    }

    /**
     * Gets next {@link Maneuver} street as string representation.
     * @param context - a context.
     * @param maneuver - the maneuver to use.
     * @param baseGuidancePresenter - the presenter to use.
     * @return a string containing the street of next maneuver or null if maneuver is null.
     */
    public static String determineNextManeuverStreet(Context context, Maneuver maneuver,
            BaseGuidancePresenter baseGuidancePresenter) {
        String nextManeuverStreetValue = null;
        if (maneuver != null) {
            nextManeuverStreetValue = getNextStreet(context, maneuver);
            if (TextUtils.isEmpty(nextManeuverStreetValue)) {
                nextManeuverStreetValue = getNextToNext(context, maneuver, baseGuidancePresenter);
            }
            if (TextUtils.isEmpty(nextManeuverStreetValue)) {
                final String roadNumber = maneuver.getRoadNumber();
                final String roadName = maneuver.getRoadName();
                nextManeuverStreetValue = combineRoadNumberAndName(context, maneuver, roadNumber,
                        roadName);
            }
            if (TextUtils.isEmpty(nextManeuverStreetValue)) {
                nextManeuverStreetValue = getSignPostExitText(maneuver);
            }
        }

        return nextManeuverStreetValue;
    }

    private static String getNextToNext(Context context, Maneuver maneuver,
            BaseGuidancePresenter baseGuidancePresenter) {
        String nextToNextStreetValue = null;
        int distance = 0;
        Maneuver afterNextManeuver = getNextManeuver(baseGuidancePresenter, maneuver);
        while (distance < NEXT_NEXT_MANEUVER_THRESHOLD &&
                afterNextManeuver != null && nextToNextStreetValue == null) {
            distance += afterNextManeuver.getDistanceFromPreviousManeuver();
            nextToNextStreetValue = combineRoadNumberAndName(context, afterNextManeuver,
                    afterNextManeuver.getNextRoadNumber(),
                    afterNextManeuver.getNextRoadName());
            afterNextManeuver = getNextManeuver(baseGuidancePresenter, afterNextManeuver);
        }
        return nextToNextStreetValue;
    }

    private static String getNextStreet(Context context, Maneuver maneuver) {
        final String nextRoadNumber = maneuver.getNextRoadNumber();
        final String nextRoadName = maneuver.getNextRoadName();
        return combineRoadNumberAndName(context, maneuver, nextRoadNumber, nextRoadName);
    }

    /**
     * Gets {@link Signpost} exit text.
     */
    private static String getSignPostExitText(Maneuver maneuver) {
        final Signpost post = maneuver.getSignpost();
        if (post != null && post.getExitText() != null && post.getExitText().length() != 0) {
            return post.getExitText();
        }
        return null;
    }

    /**
     * Gets the next {@link Maneuver}, if next maneuver is {@code null} it try to get the next one.
     */
    private static Maneuver getNextManeuver(BaseGuidancePresenter baseGuidancePresenter, Maneuver lastManeuver) {
        Maneuver nextManeuver = (lastManeuver == null) ? baseGuidancePresenter.getNextManeuver() : lastManeuver;
        if (nextManeuver == null) {
            final Route route = baseGuidancePresenter.getRoute();
            if (route != null) {
                nextManeuver = getFollowingManeuver(route, lastManeuver);
            }
        }
        return nextManeuver;
    }

    /**
     * Gets the following {@link Maneuver} of given lastManeuver.
     *
     * @param route
     *         {@link Route} to get all maneuvers.
     * @param lastManeuver
     *         input {@link Maneuver} to get its following maneuver.
     */
    private static Maneuver getFollowingManeuver(Route route, Maneuver lastManeuver) {
        final List<Maneuver> maneuvers = route.getManeuvers();
        if (maneuvers != null && !maneuvers.isEmpty()) {
            final int index = getIndexOfManeuver(lastManeuver, maneuvers);
            if (index >= 0 && index < maneuvers.size() - 1) {
                return maneuvers.get(index + 1);
            }
        }
        return null;
    }

    /**
     * Combines Road name and Road Number with a "/" if needed.
     *
     * @return null if empty string is returned.
     */
    private static String combineRoadNumberAndName(Context context, Maneuver maneuver, String roadNumber,
            String roadName) {

        String tempRoadName = roadName;

        // When leaving highway, prefer to show exit signpost text instead of road name.
        if (maneuver.getAction() == Maneuver.Action.LEAVE_HIGHWAY) {
            tempRoadName = getStringFromSignpost(maneuver.getSignpost(), tempRoadName);
        }

        // In order to prevent empty road string use roadNumber if roadName isn't available.
        final String roadText = TextUtils.isEmpty(tempRoadName) ? roadNumber : tempRoadName;

        // Skip formatting if road name contains road number.
        if (!TextUtils.isEmpty(roadNumber) && tempRoadName.contains(roadNumber)) {
            return roadText;
        }

        // Format string if needed.
        if (!TextUtils.isEmpty(tempRoadName) && !TextUtils.isEmpty(roadNumber)) {
            return context.getString(R.string.msdkui_maneuver_road_name_divider, roadNumber, tempRoadName);
        }

        return roadText;
    }

    /**
     * Gets string from signpost. Returns default value if there is no data available.
     */
    private static String getStringFromSignpost(Signpost signpost, String defaultValue) {
        if (signpost != null) {
            final List<Signpost.LocalizedLabel> directions = signpost.getExitDirections();
            if (directions != null && !directions.isEmpty() && !TextUtils.isEmpty(directions.get(0).getText())) {
                return directions.get(0).getText();
            }
            if (!TextUtils.isEmpty(signpost.getExitText())) {
                return signpost.getExitText();
            }
        }
        return defaultValue;
    }
}
