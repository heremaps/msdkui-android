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
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteOptions;
import com.here.msdkui.R;
import com.here.msdkui.common.BaseView;
import com.here.msdkui.common.ThemeUtil;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

/**
 * A view that shows a row containing details about a {@link Route}. This view can be used as a list item for
 * a {@link RouteDescriptionList}.
 */
public class RouteDescriptionItem extends BaseView {

    private final EnumMap<RouteDescriptionItem.Section, View> mSections = new EnumMap<>(
            RouteDescriptionItem.Section.class);
    private Route mRoute;
    private boolean mTrafficEnabled;
    private float mSectionBarScaling = 1.0f;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public RouteDescriptionItem(final Context context) {
        this(context, null);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     */
    public RouteDescriptionItem(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     *
     * @param defStyleAttr
     *         a default style attribute.
     */
    public RouteDescriptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     *
     * @param defStyleAttr
     *         a default style attribute.
     *
     * @param defStyleRes
     *         a default style resource.
     *
     * Requires Lollipop (API level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RouteDescriptionItem(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.route_description_item, this);
        initSections();
        // Hide this item until route is set.
        setVisibility(View.INVISIBLE);
        attrsInit(attrs, defStyleAttr, defStyleRes);
    }

    private void initSections() {
        for (final RouteDescriptionItem.Section section : RouteDescriptionItem.Section.values()) {
            switch (section) {
                case TYPE_ICON:
                    mSections.put(section, (View) findViewById(R.id.desc_type_icon));
                    break;
                case TIME:
                    mSections.put(section, (View) findViewById(R.id.desc_time));
                    break;
                case DETAILS:
                    mSections.put(section, (View) findViewById(R.id.desc_details));
                    break;
                case TRAFFIC_WARNING:
                    mSections.put(section, (View) findViewById(R.id.desc_traffic_warning));
                    break;
                case ARRIVAL_TIME:
                    mSections.put(section, (View) findViewById(R.id.desc_arrival));
                    break;
                case SECTION_BAR:
                    mSections.put(section, (View) findViewById(R.id.desc_bar));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Initialization of attributes.
     */
    private void attrsInit(final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        if (attrs == null) {
            return;
        }
        final TypedArray typedArray = this.getContext()
                .obtainStyledAttributes(attrs, R.styleable.RouteDescriptionItem, defStyleAttr, defStyleRes);
        if (typedArray.hasValue(R.styleable.RouteDescriptionItem_visibleSection)) {
            final EnumSet<Section> shouldVisibleSection = EnumSet.noneOf(Section.class);
            final int section = typedArray.getInt(R.styleable.RouteDescriptionItem_visibleSection, 0);
            if ((section & Section.TYPE_ICON.getAttrValue()) != 0) {
                shouldVisibleSection.add(Section.TYPE_ICON);
            }
            if ((section & Section.TIME.getAttrValue()) != 0) {
                shouldVisibleSection.add(Section.TIME);
            }
            if ((section & Section.DETAILS.getAttrValue()) != 0) {
                shouldVisibleSection.add(Section.DETAILS);
            }
            if ((section & Section.TRAFFIC_WARNING.getAttrValue()) != 0) {
                shouldVisibleSection.add(Section.TRAFFIC_WARNING);
            }
            if ((section & Section.ARRIVAL_TIME.getAttrValue()) != 0) {
                shouldVisibleSection.add(Section.ARRIVAL_TIME);
            }
            if ((section & Section.SECTION_BAR.getAttrValue()) != 0) {
                shouldVisibleSection.add(Section.SECTION_BAR);
            }
            typedArray.recycle();
            setVisibleSections(shouldVisibleSection);
        }
    }

    /**
     * Sets the visibility of a {@link Section}.
     *
     * @param section
     *         the {@link Section} to set the visibility for.
     * @param visible
     *         true if visible, false otherwise.
     */
    public void setSectionVisible(final Section section, final boolean visible) {
        if (visible) {
            mSections.get(section).setVisibility(VISIBLE);
        } else {
            mSections.get(section).setVisibility(GONE);
        }
    }

    /**
     * Returns the visibility of the given {@link Section}.
     *
     * @param section
     *         {@link Section}.
     *
     * @return true if visible, false otherwise.
     */
    public boolean isSectionVisible(final Section section) {
        return mSections.get(section).getVisibility() == VISIBLE;
    }

    /**
     * Gets a collection of all visible {@link Section} elements.
     *
     * @return all visible sections.
     */
    public EnumSet<Section> getVisibleSections() {
        final EnumSet<Section> visibleSections = EnumSet.noneOf(Section.class);
        for (final Map.Entry<Section, View> entry : mSections.entrySet()) {
            if (entry.getValue().getVisibility() == VISIBLE) {
                visibleSections.add(entry.getKey());
            }
        }
        return visibleSections;
    }

    /**
     * Sets a collection of {@link Section} elements that should be visible. A {@link Section} that is not
     * part of the list will be gone in the view hierarchy.
     *
     * @param sectionEnumSet
     *         the {@link Section} collection that should be visible.
     */
    public void setVisibleSections(final EnumSet<Section> sectionEnumSet) {
        for (final Map.Entry<Section, View> entry : mSections.entrySet()) {
            if (sectionEnumSet.contains(entry.getKey())) {
                entry.getValue().setVisibility(VISIBLE);
            } else {
                entry.getValue().setVisibility(GONE);
            }
        }
    }

    /**
     * Indicates whether traffic is enabled or not. This can influence the travel time.
     * @return true if traffic is enabled, false otherwise.
     */
    public boolean isTrafficEnabled() {
        return mTrafficEnabled;
    }

    /**
     * Sets whether traffic is enabled or not. This can influence the travel time.
     * @param isTraffic true if traffic should be enabled, false otherwise.
     */
    public void setTrafficEnabled(final boolean isTraffic) {
        mTrafficEnabled = isTraffic;
    }

    /**
     * Gets the {@link Route} associated with this item.
     *
     * @return the {@link Route}.
     */
    public Route getRoute() {
        return mRoute;
    }

    /**
     * Sets the new {@link Route} to be associated with this item.
     *
     * @param route
     *         the new {@link Route}.
     */
    public void setRoute(final Route route) {
        if (route == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_route_null));
        }
        mRoute = route;

        final ImageView icon = (ImageView) mSections.get(Section.TYPE_ICON);
        final Integer id = RouteUtil.getIcon(mRoute);
        if (id == 0) {
            icon.setVisibility(GONE);
        } else {
            icon.setImageResource(id);
            icon.setColorFilter(ThemeUtil.getColor(getContext(), R.attr.colorForeground), PorterDuff.Mode.SRC_ATOP);
        }

        final Spannable totalTime = RouteUtil.getTimeToArrive(getContext(), mRoute, isTrafficEnabled());
        ((TextView) mSections.get(Section.TIME)).setText(totalTime);
        ((TextView) mSections.get(Section.TIME)).setContentDescription(
                getContext().getString(R.string.msdkui_duration) + " " + totalTime);

        if (isTrafficEnabled() && !isBikeOrPedestrian()) {
            ((TextView) mSections.get(Section.TRAFFIC_WARNING)).setVisibility(View.VISIBLE);
            final Spannable delayText = RouteUtil.getTrafficDelayed(getContext(), mRoute);
            ((TextView) mSections.get(Section.TRAFFIC_WARNING)).setText(delayText);
            ((TextView) mSections.get(Section.TRAFFIC_WARNING)).setContentDescription(delayText.toString().replace(
                    getContext().getString(R.string.msdkui_incl),
                    getContext().getString(R.string.msdkui_including)));

        } else {
            ((TextView) mSections.get(Section.TRAFFIC_WARNING)).setVisibility(GONE);
        }

        ((TextView) mSections.get(Section.DETAILS)).setText(
                RouteUtil.getDetails(getContext(), mRoute, mUnitSystem));
        ((SectionBar) mSections.get(Section.SECTION_BAR))
                .bind(RouteUtil.getSectionBar(getContext(), mRoute), mSectionBarScaling);

        final String arrivalTime = RouteUtil.getArrivalTime(getContext(), mRoute, isTrafficEnabled());
        ((TextView) mSections.get(Section.ARRIVAL_TIME)).setText(arrivalTime);
        ((TextView) mSections.get(Section.ARRIVAL_TIME)).setContentDescription(
                getContext().getString(R.string.msdkui_arrive_at) +
                        arrivalTime);
        setVisibility(VISIBLE);
    }

    /**
     * Gets the scaling factor of {@link SectionBar}.
     * @return the scaling factor. Default is 1.
     */
    public float getSectionBarScaling() {
        return mSectionBarScaling;
    }

    /**
     * Sets the scaling factor for {@link SectionBar}. Scaling sets the width of section bar relative to the
     * total width of {@code RouteDescriptionItem}.
     * @param sectionBarScaling the scaling factor.
     */
    public void setSectionBarScaling(final float sectionBarScaling) {
        if (Float.compare(mSectionBarScaling, sectionBarScaling) != 0 && mRoute != null) {
            ((SectionBar) mSections.get(Section.SECTION_BAR))
                    .bind(RouteUtil.getSectionBar(getContext(), mRoute), mSectionBarScaling);
        }
        mSectionBarScaling = sectionBarScaling;
    }

    /**
     * Indicates whether the {@link com.here.android.mpa.routing.RouteOptions.TransportMode} is bike or pedestrian.
     * @return true if bike or pedestrian, false otherwise.
     */
    public boolean isBikeOrPedestrian() {
        final RouteOptions.TransportMode transportMode = mRoute.getRoutePlan().getRouteOptions().getTransportMode();
        return transportMode == RouteOptions.TransportMode.BICYCLE || transportMode == RouteOptions.TransportMode.PEDESTRIAN;
    }

    /**
     * An enum describing the sections of this item.
     */
    public enum Section {

        /**
         * Icon section.
         */
        TYPE_ICON(0x01),

        /**
         * Time section.
         */
        TIME(0x02),

        /**
         * Details section.
         */
        DETAILS(0x04),

        /**
         * Traffic warning section.
         */
        TRAFFIC_WARNING(0x08),

        /**
         * Arrival time.
         */
        ARRIVAL_TIME(0x10),

        /**
         * Section bar.
         */
        SECTION_BAR(0x20);

        private int mAttrValue;

        Section(int value) {
            mAttrValue = value;
        }

        public int getAttrValue() {
            return mAttrValue;
        }
    }
}
