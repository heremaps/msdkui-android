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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.content.res.AppCompatResources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.here.android.mpa.common.RoadElement;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteElement;
import com.here.android.mpa.routing.RouteElements;
import com.here.android.mpa.routing.RouteOptions;
import com.here.msdkui.R;
import com.here.msdkui.common.DateFormatterUtil;
import com.here.msdkui.common.DistanceFormatterUtil;
import com.here.msdkui.common.ThemeUtil;
import com.here.msdkui.common.TimeFormatterUtil;
import com.here.msdkui.common.measurements.UnitSystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.here.android.mpa.routing.Route.WHOLE_ROUTE;

/**
 * An utility class facilitating access to various {@link Route} attributes.
 */
public final class RouteUtil {

    private RouteUtil() {}

    /**
     * Gets the longest road segments of the associated {@link Route}.
     *
     * @param route
     *         a {@link Route} instance.
     * @return a string containing the longest road segments or an empty string if the route
     *         contains no {@link RoadElement}.
     */
    public static String getLongestRoadSegments(final Route route) {

        String longestSegment = "";

        final RouteElements routeElements = route.getRouteElements();
        if (routeElements == null) {
            return longestSegment;
        }

        final List<RouteElement> routeElementList = routeElements.getElements();
        final List<RoadElement> roadElementList = new ArrayList<>(routeElementList.size());
        for (final RouteElement routeElement : routeElementList) {
            roadElementList.add(routeElement.getRoadElement());
        }

        double segmentLength = 0;
        int i = 0;
        while (i < roadElementList.size()) {
            final RoadElement road = roadElementList.get(i);

            final String name = getRouteOrRoadName(road);
            double length = road.getGeometryLength();

            while (i < roadElementList.size() - 1 && name.equals(getRouteOrRoadName(roadElementList.get(i + 1)))) {
                length += roadElementList.get(i + 1)
                        .getGeometryLength();
                i++;
            }

            if (length > segmentLength) {
                longestSegment = name;
                segmentLength = length;
            }
            i++;
        }
        return longestSegment;
    }

    /**
     * Gets the traffic delay text of the associated {@link Route}.
     *
     * @param context
     *         the required {@link Context}.
     * @param route
     *         a {@link Route} instance.
     * @return the {@link Spannable} containing the traffic delay.
     */
    public static Spannable getTrafficDelayed(final Context context, final Route route) {
        final SpannableStringBuilder builder = new SpannableStringBuilder(" ");

        if (!isTrafficAvailable(route)) {
            return builder;
        }

        final boolean isBlockedRoad = route.getTtaIncludingTraffic(WHOLE_ROUTE).isBlocked();
        if (isBlockedRoad) {
            appendDrawable(context, builder, R.drawable.ic_warning, R.attr.colorAlert);
            appendText(context, builder, context.getString(R.string.msdkui_traffic_blocked), R.attr.colorAlert);
            return builder;
        }
        final long durationWithTraffic = route.getTtaIncludingTraffic(WHOLE_ROUTE)
                .getDuration();
        final long durationWithoutTraffic = route.getTtaExcludingTraffic(WHOLE_ROUTE)
                .getDuration();
        final long delayInSeconds = durationWithTraffic > durationWithoutTraffic ?
                durationWithTraffic - durationWithoutTraffic :
                0;
        final long delayInMinutes = (long) (delayInSeconds / 60);   // min
        if (delayInMinutes > 0) {
            appendDrawable(context, builder, R.drawable.ic_warning, R.attr.colorAlert);
            appendText(context, builder, context.getString(R.string.msdkui_incl_traffic_delay,
                    TimeFormatterUtil.format(context, TimeUnit.MINUTES.toMillis(delayInMinutes))), R.attr.colorAlert);
            return builder;
        } else {
            appendText(context, builder, context.getString(R.string.msdkui_no_traffic_delays), R.attr.colorForegroundSecondary);
        }
        return builder;
    }

    /**
     * Gets the arrival time of the associated {@link Route} with or without traffic.
     *
     * @param context
     *         the required {@link Context}.
     * @param route
     *         a {@link Route} instance.
     * @param isTraffic
     *         true if traffic situation should be included, false otherwise.
     * @return a string containing the arrival time.
     */
    public static String getArrivalTime(final Context context, final Route route, final boolean isTraffic) {
        final long tta = getTta(route, isTraffic);
        final Date setArrivalOrDepartureDate = new Date();
        final Date estimatedArrival;
        final RouteOptions.TimeType type = route.getRoutePlan().getRouteOptions().getTime(setArrivalOrDepartureDate);
        if (type == RouteOptions.TimeType.ARRIVAL) {
            estimatedArrival = new Date(setArrivalOrDepartureDate.getTime() - tta);
        } else {
            estimatedArrival = new Date(setArrivalOrDepartureDate.getTime() + tta);
        }
        return DateFormatterUtil.format(context, estimatedArrival);
    }

    /**
     * Gets the total length of the associated {@link Route} as formatted string.
     *
     * @param context
     *         the required {@link Context}.
     * @param route
     *         a {@link Route} instance.
     * @param unitSystem
     *         unit system {@link UnitSystem}.
     *
     * @return the formatted route length including units.
     */
    public static String getRouteLength(final Context context, final Route route,
                                        final UnitSystem unitSystem) {
        final int length = route.getLength();
        return DistanceFormatterUtil.format(context, length, unitSystem);
    }

    /**
     * Gets different details of the associated {@link Route} like via and others.
     *
     * @param context
     *         the required {@link Context}.
     * @param route
     *         a {@link Route} instance.
     * @param unitSystem
     *         unit system {@link UnitSystem}.
     *
     * @return a {@link Spannable} containing the details.
     */
    public static Spannable getDetails(final Context context, final Route route,
                                       final UnitSystem unitSystem) {
        final RouteOptions.TransportMode transportMode = route.getRoutePlan()
                .getRouteOptions()
                .getTransportMode();
        final SpannableStringBuilder builder =
                new SpannableStringBuilder(getRouteLength(context, route, unitSystem));
        if (transportMode == RouteOptions.TransportMode.PUBLIC_TRANSPORT) {
            return new SpannableStringBuilder(); // empty
        } else {
            addVia(context, route, builder);
        }
        return builder;
    }

    /**
     * Gets the estimated remaining time to arrive at the destination with or without traffic.
     *
     * @param context
     *         the required {@link Context}.
     * @param route
     *         a {@link Route} instance.
     * @param isTraffic
     *         true if traffic situation should be included, false otherwise.
     * @return the {@link Spannable} containing the remaining time to arrive in the format:
     * days-hours-minutes.
     */
    public static Spannable getTimeToArrive(final Context context, final Route route, final boolean isTraffic) {
        return SpannableString.valueOf(TimeFormatterUtil.format(context, getTta(route, isTraffic)));
    }

    /**
     * Gets the {@link com.here.android.mpa.routing.RouteOptions.TransportMode} icon of the associated {@link Route}.
     *
     * @param route
     *         a {@link Route} instance.
     * @return an icon indicating the transport mode.
     */
    public static Integer getIcon(final Route route) {
        final RouteOptions.TransportMode transportMode = route.getRoutePlan()
                .getRouteOptions()
                .getTransportMode();
        Integer id = 0;
        switch (transportMode) {
            case CAR:
                id = R.drawable.ic_drive;
                break;
            case TRUCK:
                id = R.drawable.ic_truck_24;
                break;
            case PEDESTRIAN:
                id = R.drawable.ic_walk_24;
                break;
            case BICYCLE:
                id = R.drawable.ic_bike_24;
                break;
            case SCOOTER:
                id = R.drawable.ic_scooter;
                break;
            default:
                break;
        }
        return id;
    }

    /**
     * Gets a list of {@link SectionModel} elements of the associated {@link Route}.
     *
     * @param context
     *         the required {@link Context}.
     * @param route
     *         a {@link Route} instance.
     * @return a list containing the {@link SectionModel} elements.
     */
    public static List<SectionModel> getSectionBar(final Context context, final Route route) {
        final List<SectionModel> sectionModels = new ArrayList<>();
        final RouteOptions.TransportMode transportMode = route.getRoutePlan()
                .getRouteOptions()
                .getTransportMode();
        if (transportMode != RouteOptions.TransportMode.PUBLIC_TRANSPORT) {
            final LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(context,
                    R.drawable.section_bar_layer_rounded)
                    .mutate();
            final GradientDrawable fg = (GradientDrawable) drawable.findDrawableByLayerId(R.id.layer_foreground);
            fg.setColor(ContextCompat.getColor(context, R.color.color_route));
            final SectionModel sectionModel = new SectionModel();
            sectionModel.setDrawable(drawable);
            sectionModels.add(sectionModel);
        }
        return sectionModels;
    }

    private static void addVia(final Context context, final Route route, final SpannableStringBuilder builder) {
        builder.append("  ");
        appendDrawable(context, builder, R.drawable.ic_small_vertical_divider, -1);
        builder.append(" ");
        builder.append(getLongestRoadSegments(route));
    }

    private static void appendDrawable(final Context context, final SpannableStringBuilder builder, final int drawable, int tint) {
        final Drawable divider = AppCompatResources.getDrawable(context, drawable)
                .mutate();
        final int color = (tint == -1) ? R.attr.colorForegroundSecondaryLight : tint;
        divider.setColorFilter(ThemeUtil.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        divider.setBounds(0, 0, divider.getIntrinsicWidth(), divider.getIntrinsicHeight());
        final ImageSpan span = new ImageSpan(divider, ImageSpan.ALIGN_BOTTOM);
        builder.setSpan(span, builder.length() - 1, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static void appendText(final Context context, final SpannableStringBuilder builder, final String text, final int attr) {
        builder.append(" ");
        builder.append(text);
        builder.setSpan(new ForegroundColorSpan(ThemeUtil.getColor(context, attr)),
                builder.length() - text.length(), builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static String getRouteOrRoadName(final RoadElement road) {
        return TextUtils.isEmpty(road.getRouteName()) ? road.getRoadName() : road.getRouteName();
    }

    private static long getTta(final Route route, final boolean isTraffic) {
        if (isTraffic && isTrafficAvailable(route)) {
            return route.getTtaIncludingTraffic(WHOLE_ROUTE)
                    .getDuration() * DateUtils.SECOND_IN_MILLIS;
        }
        return route.getTtaExcludingTraffic(WHOLE_ROUTE)
                .getDuration() * DateUtils.SECOND_IN_MILLIS;
    }

    /**
     * Traffic information is not available when departure time is more than 5 minutes in the past or more
     * than 30 minutes in the future - or when RouteOptions.TimeType.ARRIVAL is set as time.
     *
     * @param route
     *         a {@link Route} instance.
     *
     * @return true if traffic information is available for this route.
     */
    private static boolean isTrafficAvailable(final Route route) {
        // gets arrival or departure date.
        final Date departureDate = new Date();
        final RouteOptions.TimeType type = route.getRoutePlan().getRouteOptions().getTime(departureDate);

        if (type == RouteOptions.TimeType.ARRIVAL) {
            return false; // traffic is not supported for arrival.
        }

        final long pastThresholdSeconds = -5 * 60 * 1000; // -5 minutes
        final long futureThresholdSeconds = 30 * 60 * 1000; // +30 minutes
        final long timeInterval = departureDate.getTime() - new Date().getTime();
        if (timeInterval < pastThresholdSeconds || timeInterval > futureThresholdSeconds) {
            return false;
        }

        return true;
    }
}
