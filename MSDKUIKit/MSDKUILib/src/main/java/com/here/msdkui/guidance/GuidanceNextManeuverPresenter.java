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
import com.here.msdkui.common.DistanceFormatterUtil;
import com.here.msdkui.guidance.base.BaseGuidancePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that creates {@link GuidanceNextManeuverData} during guidance. This data can be fed into
 * {@link GuidanceNextManeuverView} to inform the user about maneuver to take after current one.
 * You must call resume() to start listening for guidance events.
 * {@link GuidanceNextManeuverListener#onDataChanged(GuidanceNextManeuverData)}.
 */
public class GuidanceNextManeuverPresenter extends BaseGuidancePresenter {

    private final Context mContext;
    private final List<GuidanceNextManeuverListener> mListener = new ArrayList<>();

    /**
     * Constructs a new instance.
     *
     * @param context
     *         a {@link Context} to retrieve resources.
     * @param navigationManager
     *         a {@link NavigationManager} to be used for guidance handling.
     * @param route
     *         a route to be used for guidance.
     */
    public GuidanceNextManeuverPresenter(Context context, NavigationManager navigationManager, Route route) {
        super(navigationManager, route);
        mContext = context;
    }

    @Override
    public void handleManeuverEvent() {
        final Maneuver maneuver = getAfterNextManeuver();
        updateManeuverData(maneuver);
    }

    @Override
    protected void handleNewInstructionEvent() {
        handleManeuverEvent();
    }

    /**
     * Creates {@link GuidanceNextManeuverData} and notifies on the changes.
     *
     * @param maneuver
     *         the maneuver to use.
     */
    private void updateManeuverData(Maneuver maneuver) {
        if (maneuver == null) {
            notifyDataChanged(null);
        } else {
            final GuidanceNextManeuverData data = new GuidanceNextManeuverData(getIcon(maneuver),
                    (long) maneuver.getDistanceFromPreviousManeuver(), getStreet(maneuver));
            if (data.getIconId() == 0 || data.getDistance() < 0 ||
                    data.getDistance() > DistanceFormatterUtil.THOUSAND) {
                notifyDataChanged(null);
            } else {
                notifyDataChanged(data);
            }
        }
    }

    /**
     * Gets the icon identifier for {@link Maneuver}.
     *
     * @param maneuver
     *         the {@link Maneuver} to get the icon id from.
     *
     * @return the resource id of the icon.
     */
    protected int getIcon(Maneuver maneuver) {
        final int id = maneuver.getIcon().ordinal();
        return mContext.getResources().getIdentifier("ic_maneuver_icon_" + id, "drawable", mContext.getPackageName());
    }

    /**
     * Gets the street name for {@link Maneuver}.
     */
    private String getStreet(Maneuver maneuver) {
        return GuidanceManeuverUtil.determineNextManeuverStreet(mContext, maneuver, this);
    }

    /**
     * Adds a {@link GuidanceNextManeuverListener}.
     *
     * @param listener
     *         the listener to add to the list of listeners.
     */
    public void addListener(GuidanceNextManeuverListener listener) {
        if (listener != null && !mListener.contains(listener)) {
            mListener.add(listener);
        }
    }

    /**
     * Remove a {@link GuidanceNextManeuverListener}.
     *
     * @param listener
     *         the listener to remove from the list of listeners.
     */
    public void removeListener(GuidanceNextManeuverListener listener) {
        mListener.remove(listener);
    }

    /**
     * Notify on {@link GuidanceNextManeuverData} changes.
     */
    private void notifyDataChanged(GuidanceNextManeuverData data) {
        for (final GuidanceNextManeuverListener listener : mListener) {
            listener.onDataChanged(data);
        }
    }
}
