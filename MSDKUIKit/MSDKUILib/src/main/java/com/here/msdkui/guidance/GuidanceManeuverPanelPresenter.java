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
import com.here.android.mpa.routing.Signpost;
import com.here.msdkui.R;
import com.here.msdkui.guidance.base.BaseGuidancePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that creates {@link GuidanceManeuverData} during guidance. This data can be fed into
 * {@link GuidanceManeuverPanel} to inform the user about the next maneuvers to take.
 * You must call resume() to start listening for guidance events.
 * {@link GuidanceManeuverPanelListener#onDataChanged(GuidanceManeuverData)}.
 */
public class GuidanceManeuverPanelPresenter extends BaseGuidancePresenter {

    private final Context mContext;
    private final List<GuidanceManeuverPanelListener> mListener = new ArrayList<>();

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
    public GuidanceManeuverPanelPresenter(Context context, NavigationManager navigationManager, Route route) {
        super(navigationManager, route);
        mContext = context;
    }

    @Override
    public void handleManeuverEvent() {
        final Maneuver maneuver = getNextManeuver();
        if (maneuver == null) {
            return;
        }
        updateManeuverData(maneuver);
        if (maneuver.getAction() == Maneuver.Action.END) {
            notifyDestinationReached();
        }
    }

    @Override
    protected void handleNewInstructionEvent() {
        handleManeuverEvent();
    }

    /**
     * Creates {@link GuidanceManeuverData} and notifies on the changes.
     *
     * @param maneuver
     *         the maneuver to use.
     */
    private void updateManeuverData(Maneuver maneuver) {
        final GuidanceManeuverData data = new GuidanceManeuverData(getIcon(maneuver),
                getNextManeuverDistance(), getManeuverSignpost(maneuver), getStreet(maneuver));
        notifyDataChanged(data);
    }

    /**
     * Gets a {@link Signpost} as string representation.
     *
     * @param maneuver
     *         the maneuver to use.
     *
     * @return the signpost as string or null if no signpost was found.
     */
    private String getManeuverSignpost(Maneuver maneuver) {
        final Signpost signpost = maneuver.getSignpost();
        if (signpost != null && !signpost.getExitNumber().isEmpty()) {
            return mContext.getResources().getString(R.string.msdkui_maneuver_exit, signpost.getExitNumber());
        }
        return null;
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
     * Adds a {@link GuidanceManeuverPanelListener}.
     *
     * @param listener
     *         the listener to add to the list of listeners.
     */
    public void addListener(GuidanceManeuverPanelListener listener) {
        if (listener != null && !mListener.contains(listener)) {
            mListener.add(listener);
        }
    }

    /**
     * Remove a {@link GuidanceManeuverPanelListener}.
     *
     * @param listener
     *         the listener to remove from the list of listeners.
     */
    public void removeListener(GuidanceManeuverPanelListener listener) {
        mListener.remove(listener);
    }

    /**
     * Notify on {@link GuidanceManeuverData} changes.
     */
    private void notifyDataChanged(GuidanceManeuverData data) {
        for (final GuidanceManeuverPanelListener listener : mListener) {
            listener.onDataChanged(data);
        }
    }

    /**
     * Notify that destination was reached.
     */
    private void notifyDestinationReached() {
        for (final GuidanceManeuverPanelListener listener : mListener) {
            listener.onDestinationReached();
        }
    }
}