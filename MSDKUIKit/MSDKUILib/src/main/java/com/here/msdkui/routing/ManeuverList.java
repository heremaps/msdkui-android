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
import android.util.AttributeSet;

import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.msdkui.R;
import com.here.msdkui.common.ThemeUtil;
import com.here.msdkui.common.measurements.UnitSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom {@code RecyclerView} that displays a list of maneuvers.
 */
public class ManeuverList extends CustomRecyclerView {

    private final List<Maneuver> mManeuverList = new ArrayList<>();
    private ManeuverListAdapter mAdapter;
    private Route mRoute;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public ManeuverList(final Context context) {
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
    public ManeuverList(final Context context, final AttributeSet attrs) {
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
    public ManeuverList(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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

    private void init() {
        mAdapter = new ManeuverListAdapter(mManeuverList);
        setAdapter(mAdapter);
        if (getBackground() == null) {
            setBackgroundColor(ThemeUtil.getColor(getContext(), R.attr.colorBackgroundViewLight));
        }
    }

    /**
     * Gets the {@link Route} associated with this list.
     *
     * @return the {@link Route} object.
     */
    public Route getRoute() {
        return mRoute;
    }

    /**
     * Sets a new {@link Route} to be associated with this list.
     *
     * @param route the new {@link Route} to use, replacing the old one.
     * @throws IllegalArgumentException if route is null or if no maneuvers have been set for the route.
     */
    public void setRoute(final Route route) {
        if (route == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_exception_route_null));
        }
        mRoute = route;
        final List<Maneuver> maneuvers = mRoute.getManeuvers();
        if (maneuvers == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.msdkui_no_maneuver_found));
        }
        mManeuverList.clear();
        mManeuverList.addAll(maneuvers);
        mAdapter.notifyDataSetChanged();
    }
}
