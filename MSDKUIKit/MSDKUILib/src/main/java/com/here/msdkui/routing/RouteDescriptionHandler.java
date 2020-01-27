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
 * A convenience class to access {@link Route} attributes.
 * @deprecated Please use {@link RouteUtil} instead.
 */
public final class RouteDescriptionHandler {

    private final Route mRoute;
    private final Context mContext;

    /**
     * Constructs a new instance using context and the route of interest.
     * @param context a context to access resources.
     * @param route the route from where attributes should be retrieved.
     */
    public RouteDescriptionHandler(final Context context, final Route route) {
        mRoute = route;
        mContext = context;
    }

    /**
     * Gets the longest road segments of the associated {@link Route}.
     * @return a string containing the longest road segments or an empty string if the route
     *         contains no {@link RoadElement}.
     */
    public String getLongestRoadSegments() {

        String longestSegment = "";

        final RouteElements routeElements = mRoute.getRouteElements();
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
     * @return the {@link Spannable} containing the traffic delay.
     */
    public Spannable getTrafficDelayed() {
        final SpannableStringBuilder builder = new SpannableStringBuilder(" ");

        // get arrival or departure date.
        final Date departureDate = new Date();
        final RouteOptions.TimeType type = mRoute.getRoutePlan().getRouteOptions().getTime(departureDate);

        if (type == RouteOptions.TimeType.ARRIVAL) {
            return builder; // traffic is not supported for arrival.
        }

        // So, we need thresholds beyond which we consider the traffic data is not available in case of departure
        final long pastThresholdSeconds = -5 * 60 * 1000; // -5 minutes
        final long futureThresholdSeconds = 30 * 60 * 1000; // +30 minutes
        final long timeInterval = departureDate.getTime() - new Date().getTime();
        if (timeInterval < pastThresholdSeconds || timeInterval > futureThresholdSeconds) {
            return builder;
        }

        final boolean isBlockedRoad = mRoute.getTtaIncludingTraffic(WHOLE_ROUTE).isBlocked();
        if (isBlockedRoad) {
            appendDrawable(builder, R.drawable.ic_warning, R.attr.colorAlert);
            appendText(builder, mContext.getString(R.string.msdkui_traffic_blocked), R.attr.colorAlert);
            return builder;
        }
        final long durationWithTraffic = mRoute.getTtaIncludingTraffic(WHOLE_ROUTE)
                .getDuration();
        final long durationWithoutTraffic = mRoute.getTtaExcludingTraffic(WHOLE_ROUTE)
                .getDuration();
        final long delayInSeconds = durationWithTraffic > durationWithoutTraffic ?
                durationWithTraffic - durationWithoutTraffic :
                0;
        final long delayInMinutes = (long) (delayInSeconds / 60);   // min
        if (delayInMinutes > 0) {
            appendDrawable(builder, R.drawable.ic_warning, R.attr.colorAlert);
            appendText(builder, mContext.getString(R.string.msdkui_incl_traffic_delay,
                    TimeFormatterUtil.format(mContext, TimeUnit.MINUTES.toMillis(delayInMinutes))), R.attr.colorAlert);
            return builder;
        } else {
            appendText(builder, mContext.getString(R.string.msdkui_no_traffic_delays), R.attr.colorForegroundSecondary);
        }
        return builder;
    }

    /**
     * Gets the arrival time of the associated {@link Route} with or without traffic.
     *
     * @param isTraffic
     *         true if traffic situation should be included, false otherwise.
     * @return a string containing the arrival time.
     */
    public String getArrivalTime(final boolean isTraffic) {
        final long tta = getTta(isTraffic);
        final Date setArrivalOrDepartureDate = new Date();
        final Date estimatedArrival;
        final RouteOptions.TimeType type = mRoute.getRoutePlan().getRouteOptions().getTime(setArrivalOrDepartureDate);
        if (type == RouteOptions.TimeType.ARRIVAL) {
            estimatedArrival = new Date(setArrivalOrDepartureDate.getTime() - tta);
        } else {
            estimatedArrival = new Date(setArrivalOrDepartureDate.getTime() + tta);
        }
        return DateFormatterUtil.format(mContext, estimatedArrival);
    }

    /**
     * Gets the total length of the associated {@link Route} as formatted string.
     *
     * @param unitSystem
     *         unit system {@link UnitSystem}.
     *
     * @return the formatted route length including units.
     */
    public String getRouteLength(UnitSystem unitSystem) {
        final int length = mRoute.getLength();
        return DistanceFormatterUtil.format(mContext, length, unitSystem);
    }

    /**
     * Gets different details of the associated {@link Route} like via and others.
     *
     * @param unitSystem
     *         unit system {@link UnitSystem}.
     *
     * @return a {@link Spannable} containing the details.
     */
    public Spannable getDetails(UnitSystem unitSystem) {
        final RouteOptions.TransportMode transportMode = mRoute.getRoutePlan()
                .getRouteOptions()
                .getTransportMode();
        final SpannableStringBuilder builder = new SpannableStringBuilder(getRouteLength(unitSystem));
        if (transportMode == RouteOptions.TransportMode.PUBLIC_TRANSPORT) {
            return new SpannableStringBuilder(); // empty
        } else {
            addVia(builder);
        }
        return builder;
    }

    private void addVia(final SpannableStringBuilder builder) {
        builder.append("  ");
        appendDrawable(builder, R.drawable.ic_small_vertical_divider, -1);
        builder.append(" ");
        builder.append(getLongestRoadSegments());
    }

    private void appendDrawable(final SpannableStringBuilder builder, final int drawable, int tint) {
        final Drawable divider = AppCompatResources.getDrawable(mContext, drawable)
                .mutate();
        final int color = (tint == -1) ? R.attr.colorForegroundSecondaryLight : tint;
        divider.setColorFilter(ThemeUtil.getColor(mContext, color), PorterDuff.Mode.SRC_ATOP);
        divider.setBounds(0, 0, divider.getIntrinsicWidth(), divider.getIntrinsicHeight());
        final ImageSpan span = new ImageSpan(divider, ImageSpan.ALIGN_BOTTOM);
        builder.setSpan(span, builder.length() - 1, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void appendText(final SpannableStringBuilder builder, final String text, final int attr) {
        builder.append(" ");
        builder.append(text);
        builder.setSpan(new ForegroundColorSpan(ThemeUtil.getColor(mContext, attr)), builder.length() - text.length(),
                builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * Gets the estimated remaining time to arrive at the destination with or without traffic.
     *
     * @param isTraffic
     *         true if traffic situation should be included, false otherwise.
     * @return the {@link Spannable} containing the remaining time to arrive in the format:
     * days-hours-minutes.
     */
    public Spannable getTimeToArrive(final boolean isTraffic) {
        return SpannableString.valueOf(TimeFormatterUtil.format(mContext, getTta(isTraffic)));
    }

    /**
     * Gets the {@link com.here.android.mpa.routing.RouteOptions.TransportMode} icon of the associated {@link Route}.
     * @return an icon indicating the transport mode.
     */
    public Integer getIcon() {
        final RouteOptions.TransportMode transportMode = mRoute.getRoutePlan()
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

    private String getRouteOrRoadName(final RoadElement road) {
        return TextUtils.isEmpty(road.getRouteName()) ? road.getRoadName() : road.getRouteName();
    }

    private long getTta(final boolean isTraffic) {
        if (isTraffic) {
            return mRoute.getTtaIncludingTraffic(WHOLE_ROUTE)
                    .getDuration() * DateUtils.SECOND_IN_MILLIS;
        }
        return mRoute.getTtaExcludingTraffic(WHOLE_ROUTE)
                .getDuration() * DateUtils.SECOND_IN_MILLIS;
    }

    /**
     * Gets a list of {@link SectionModel} elements of the associated {@link Route}.
     * @return a list containing the {@link SectionModel} elements.
     */
    public List<SectionModel> getSectionBar() {
        final List<SectionModel> sectionModels = new ArrayList<>();
        final RouteOptions.TransportMode transportMode = mRoute.getRoutePlan()
                .getRouteOptions()
                .getTransportMode();
        if (transportMode != RouteOptions.TransportMode.PUBLIC_TRANSPORT) {
            final LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(mContext,
                    R.drawable.section_bar_layer_rounded)
                    .mutate();
            final GradientDrawable fg = (GradientDrawable) drawable.findDrawableByLayerId(R.id.layer_foreground);
            fg.setColor(ContextCompat.getColor(mContext, R.color.color_route));
            final SectionModel sectionModel = new SectionModel();
            sectionModel.setDrawable(drawable);
            sectionModels.add(sectionModel);
        }
        return sectionModels;
    }
}
