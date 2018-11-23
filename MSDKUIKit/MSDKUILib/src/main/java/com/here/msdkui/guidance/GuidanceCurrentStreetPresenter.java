/*
 * Copyright (C) 2017-2018 HERE Europe B.V.
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

import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.msdkui.R;
import com.here.msdkui.common.ThemeUtil;
import com.here.msdkui.guidance.base.BaseGuidancePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that creates {@link GuidanceCurrentStreetData} objects on maneuver update and feds them into
 * registered listeners (e.g. {@link GuidanceCurrentStreet}.
 */
public class GuidanceCurrentStreetPresenter extends BaseGuidancePresenter {

    private final Context mContext;

    private final List<GuidanceCurrentStreetListener> mListener = new ArrayList<>();

    /**
     * Constructs a new instance using a {@link NavigationManager} instance and
     * a route to follow during guidance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param navigationManager
     *         a {@link NavigationManager}.
     *
     * @param route
     *         a {@link Route}.
     */
    public GuidanceCurrentStreetPresenter(Context context, NavigationManager navigationManager,
            Route route) {
        super(navigationManager, route);
        mContext = context;
    }

    @Override
    public void handleNewInstructionEvent() {
        handleManeuverEvent();
    }

    @Override
    public void handleManeuverEvent() {
        final Maneuver nextManeuver = getNextManeuver();
        if (nextManeuver != null) {
            final String currentStreetInfo = GuidanceManeuverUtil.getCurrentManeuverStreet(mContext, nextManeuver);
            final GuidanceCurrentStreetData data = new
                    GuidanceCurrentStreetData(currentStreetInfo, ThemeUtil.getColor(mContext, R.attr.colorPositive));
            notifyDataChanged(data);
        }
    }

    protected void handleGpsLost() {
        final GuidanceCurrentStreetData data = new
                GuidanceCurrentStreetData(mContext.getString(R.string.msdkui_waypoint_current_location),
                ThemeUtil.getColor(mContext, R.attr.colorForegroundSecondary));
        notifyDataChanged(data);
    }

    protected void handleGpsRestore() {
        handleManeuverEvent();
    }

    /**
     * Adds a {@link GuidanceCurrentStreetListener}.
     *
     * @param listener
     *         the listener to add to the list of listeners.
     */
    public void addListener(GuidanceCurrentStreetListener listener) {
        if (listener != null && !mListener.contains(listener)) {
            mListener.add(listener);
        }
    }

    /**
     * Remove a {@link GuidanceCurrentStreetListener}.
     *
     * @param listener
     *         the listener to remove from the list of listeners.
     */
    public void removeListener(GuidanceCurrentStreetListener listener) {
        mListener.remove(listener);
    }

    /**
     * Notify on {@link GuidanceCurrentStreetData} changes.
     */
    private void notifyDataChanged(GuidanceCurrentStreetData data) {
        for (final GuidanceCurrentStreetListener listener : mListener) {
            listener.onDataChanged(data);
        }
    }
}
