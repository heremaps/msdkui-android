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
import android.util.AttributeSet;

import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteResult;
import com.here.msdkui.R;
import com.here.msdkui.common.measurements.UnitSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A view that shows a list of {@link RouteDescriptionItem} elements.
 */
public class RouteDescriptionList extends CustomRecyclerView {

    private final List<Route> mRouteList = new ArrayList<>();
    private RouteDescriptionListAdapter mAdapter;
    private SortType mSortType = SortType.TOTAL_TIME;
    private SortOrder mSortOrder = SortOrder.ASCENDING;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public RouteDescriptionList(final Context context) {
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
    public RouteDescriptionList(final Context context, final AttributeSet attrs) {
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
    public RouteDescriptionList(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * Sets the unit system to be used by the list adapter.
     *
     * @param unitSystem
     *         unit system {@link UnitSystem}.
     */
    public void setUnitSystem(UnitSystem unitSystem) {
        mAdapter.setUnitSystem(unitSystem);
    }

    /**
     * Returns the current unit system used by the list adapter.
     *
     * @return unit system {@link UnitSystem}.
     */
    public UnitSystem getUnitSystem() {
        return mAdapter.getUnitSystem();
    }

    private void init(final AttributeSet attrs) {
        attrsInit(attrs);
        mAdapter = new RouteDescriptionListAdapter(mRouteList);
        setAdapter(mAdapter);
    }

    /**
     * Initialization of attributes.
     */
    private void attrsInit(final AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        final TypedArray typedArray = this.getContext().obtainStyledAttributes(attrs, R.styleable.RouteDescriptionList);
        if (typedArray.hasValue(R.styleable.RouteDescriptionList_sortOrder)) {
            final int value = typedArray.getInt(R.styleable.RouteDescriptionList_sortOrder, 0);
            if (value == 0) {
                mSortOrder = SortOrder.ASCENDING;
            } else {
                mSortOrder = SortOrder.DESCENDING;
            }
        }

        if (typedArray.hasValue(R.styleable.RouteDescriptionList_sortType)) {
            final int value = typedArray.getInt(R.styleable.RouteDescriptionList_sortType, 1);
            if (value == 0) {
                mSortType = SortType.DISTANCE;
            } else {
                mSortType = SortType.TOTAL_TIME;
            }
        }

        typedArray.recycle();
    }

    /**
     * Gets the {@link SortType} for this list.
     *
     * @return the {@link SortType} that is used for this list. Default is {@link SortType#TOTAL_TIME TOTAL_TIME}.
     */
    public SortType getSortType() {
        return mSortType;
    }

    /**
     * Sets the {@link SortType} for this list. Default is {@link SortType#TOTAL_TIME TOTAL_TIME}.
     * @param sortType the new {@link SortType} to be used for this list.
     * @throws IllegalArgumentException when sortType is null.
     */
    public void setSortType(final SortType sortType) {
        if (sortType == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_sort_type_null));
        }
        mSortType = sortType;
    }

    /**
     * Gets the {@link SortOrder} for this list.
     *
     * @return the {@link SortOrder} that is used for this list. Default is {@link SortOrder#ASCENDING ASCENDING}.
     */
    public SortOrder getSortOrder() {
        return mSortOrder;
    }

    /**
     * Sets {@link SortOrder} for {@link SortType}. Default is {@link SortOrder#ASCENDING ASCENDING}.
     * @param sortOrder the new {@link SortOrder} to be used for this list.
     * @throws IllegalArgumentException when sortOrder is null.
     */
    public void setSortOrder(final SortOrder sortOrder) {
        if (sortOrder == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_sort_order_null));
        }
        mSortOrder = sortOrder;
    }

    /**
     * Gets a list of {@link Route} elements which was used to create the {@link RouteDescriptionItem} elements of
     * this list.
     *
     * @return a list of {@link Route} elements.
     */
    public List<Route> getRoutes() {
        return mRouteList;
    }

    /**
     * Sets a list of {@link Route} elements to the list. The given list will be used to create the rows of
     * this list.
     *
     * @param routes the list of {@link Route} elements to set.
     * @throws IllegalArgumentException when the list is null.
     */
    public void setRoutes(final List<Route> routes) {
        if (routes == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_routes_null));
        }
        sort(routes);
        mRouteList.clear();
        mRouteList.addAll(routes);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * A convenient method to set a list of {@link RouteResult} elements to this list.
     * The routes included from {@link RouteResult} will be used to create the list items.
     *
     * @param routesResult a list of {@link RouteResult} elements to be used for this list.
     * @throws IllegalArgumentException when the list is null.
     */
    public void setRoutesResult(final List<RouteResult> routesResult) {
        if (routesResult == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_routes_null));
        }
        final List<Route> routes = new ArrayList<>(routesResult.size());
        for (final RouteResult result : routesResult) {
            routes.add(result.getRoute());
        }
        setRoutes(routes);
    }

    /**
     * Indicates whether potential traffic dalays are included in travel time or not.
     * @return true if traffic is enabled, false otherwise.
     */
    public boolean isTrafficEnabled() {
        return mAdapter.isTrafficEnabled();
    }

    /**
     * Sets traffic enabled. If traffic is enabled, the travel time will be re-calculated including potential traffic
     * delays.
     *
     * <p> Note that setting traffic for this list will always have preference over setting traffic for each of the
     * {@link RouteDescriptionItem} elements contained in this list via {@link RouteDescriptionItem#setTrafficEnabled(boolean)}.</p>
     *
     * @param isTraffic true if traffic delays should be included, false otherwise.
     */
    public void setTrafficEnabled(final boolean isTraffic) {
        mAdapter.setTrafficEnabled(isTraffic);
    }

    private void sort(final List<Route> routes) {
        switch (mSortType) {
            case DISTANCE:
                sortByDistance(routes);
                break;
            case TOTAL_TIME:
                sortByTime(routes);
                break;
            default:
                break;
        }
    }

    private void sortByDistance(final List<Route> routes) {
        Collections.sort(routes, (final Route route1, final Route route2) -> {
            if (mSortOrder == SortOrder.DESCENDING) {
                return route2.getLength() - route1.getLength();
            } else {
                return route1.getLength() - route2.getLength();
            }
        });
    }

    private void sortByTime(final List<Route> routes) {
        Collections.sort(routes, (final Route route1, final Route route2) -> {
            final int tta1 = route1.getTtaIncludingTraffic(Route.WHOLE_ROUTE)
                    .getDuration();
            final int tta2 = route2.getTtaIncludingTraffic(Route.WHOLE_ROUTE)
                    .getDuration();
            if (getSortOrder() == SortOrder.DESCENDING) {
                return tta2 - tta1;
            } else {
                return tta1 - tta2;
            }
        });
    }

    /**
     * An enum describing the sort types that are available for this list.
     */
    public enum SortType {

        /**
         * Sort by distance.
         */
        DISTANCE,

        /**
         * Sort by travel time.
         */
        TOTAL_TIME
    }

    /**
     * An enum describing the order types that are available for this list.
     */
    public enum SortOrder {

        /**
         * Sort with ascending order.
         */
        ASCENDING,

        /**
         * Sort with descending order.
         */
        DESCENDING,
    }
}
