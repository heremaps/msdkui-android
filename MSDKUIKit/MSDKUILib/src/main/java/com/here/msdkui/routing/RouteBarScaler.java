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

import android.text.format.DateUtils;

import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteOptions;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.here.android.mpa.routing.Route.WHOLE_ROUTE;

/**
 * A class to calculate scale factors to scale the width of a {@link SectionBar} of a {@link RouteDescriptionItem}.
 * The scale factor is in the range [0, 1] and is proportional compared to the longest route which has a factor of 1.
 */
public final class RouteBarScaler {

    private final Map<Route, Float> mRouteBarScale = new ConcurrentHashMap<>();

    /**
     * Calculates the scale factors for the provided routes. The scale factors are relative to the longest
     * duration of a route and can be used to scale the length of the {@link SectionBar}.
     * @param routes the routes that should be compared to find the relative scale factor for each route.
     * @return a map providing a scale factor for each route.
     */
    public Map<Route, Float> scaleRoutes(final Collection<Route> routes) {
        long maxDuration = 0;
        for (final Route route : routes) {
            if (route.getRoutePlan()
                    .getRouteOptions()
                    .getTransportMode() != RouteOptions.TransportMode.PEDESTRIAN) {
                final long duration = getDurationInMilliSeconds(route);
                if (maxDuration < duration) {
                    maxDuration = duration;
                }
            }
        }
        for (final Route route : routes) {
            final long duration = getDurationInMilliSeconds(route);
            if (maxDuration == 0) {
                maxDuration = duration;
            }
            mRouteBarScale.put(route, (float) duration / maxDuration);
        }
        return mRouteBarScale;
    }

    private long getDurationInMilliSeconds(final Route route) {
        return route.getTtaIncludingTraffic(WHOLE_ROUTE)
                .getDuration() * DateUtils.SECOND_IN_MILLIS;
    }

    /**
     * Gets the scale factor for the given route. Should be called after the relative scale
     * factors for a collection of routes have been calculated.
     *
     * @param input the route
     * @return the scale factor for the provided route or 1, if the scale factor was
     *         not calculated before.
     */
    public float getScaling(final Route input) {
        return mRouteBarScale.containsKey(input) ? mRouteBarScale.get(input) : 1.0f;
    }
}
