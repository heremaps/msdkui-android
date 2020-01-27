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

import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.msdkui.R;
import com.here.msdkui.common.ThemeUtil;
import com.here.msdkui.guidance.base.BaseGuidancePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that creates {@link GuidanceStreetLabelData} objects on maneuver update and feds them into
 * registered listeners (e.g. {@link GuidanceStreetLabelView}.
 */
public class GuidanceStreetLabelPresenter extends BaseGuidancePresenter {

    private final Context mContext;

    private final List<GuidanceStreetLabelListener> mListener = new ArrayList<>();

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
    public GuidanceStreetLabelPresenter(Context context, NavigationManager navigationManager,
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
            final GuidanceStreetLabelData data = new
                    GuidanceStreetLabelData(currentStreetInfo, ThemeUtil.getColor(mContext, R.attr.colorPositive));
            notifyDataChanged(data);
        }
    }

    protected void handleGpsLost() {
        final GuidanceStreetLabelData data = new
                GuidanceStreetLabelData(mContext.getString(R.string.msdkui_waypoint_current_location),
                ThemeUtil.getColor(mContext, R.attr.colorForegroundSecondary));
        notifyDataChanged(data);
    }

    protected void handleGpsRestore() {
        handleManeuverEvent();
    }

    /**
     * Adds a {@link GuidanceStreetLabelListener}.
     *
     * @param listener
     *         the listener to add to the list of listeners.
     */
    public void addListener(GuidanceStreetLabelListener listener) {
        if (listener != null && !mListener.contains(listener)) {
            mListener.add(listener);
            listener.onDataChanged(new GuidanceStreetLabelData(mContext.getString(R.string.msdkui_userposition_search),
                    ThemeUtil.getColor(mContext, R.attr.colorForegroundSecondary)));
        }
    }

    /**
     * Remove a {@link GuidanceStreetLabelListener}.
     *
     * @param listener
     *         the listener to remove from the list of listeners.
     */
    public void removeListener(GuidanceStreetLabelListener listener) {
        mListener.remove(listener);
    }

    /**
     * Notify on {@link GuidanceStreetLabelData} changes.
     */
    private void notifyDataChanged(GuidanceStreetLabelData data) {
        for (final GuidanceStreetLabelListener listener : mListener) {
            listener.onDataChanged(data);
        }
    }
}
