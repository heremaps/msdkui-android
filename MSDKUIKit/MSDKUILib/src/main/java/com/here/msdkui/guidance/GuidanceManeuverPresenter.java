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
import android.graphics.Bitmap;

import com.here.android.mpa.common.Image;
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
 * {@link GuidanceManeuverView} to inform the user about the next maneuvers to take.
 * You must call resume() to start listening for guidance events.
 * {@link GuidanceManeuverListener#onDataChanged(GuidanceManeuverData)}.
 */
public class GuidanceManeuverPresenter extends BaseGuidancePresenter {

    private static final int DESTINATION_THRESHOLD_DISTANCE = 50;
    private final Context mContext;
    private final List<GuidanceManeuverListener> mListener = new ArrayList<>();

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
    public GuidanceManeuverPresenter(Context context, NavigationManager navigationManager, Route route) {
        super(navigationManager, route);
        mContext = context;
    }

    @Override
    public void handlePositionUpdate() {
        handleManeuverEvent();
    }

    @Override
    public void handleManeuverEvent() {
        final Maneuver maneuver = getNextManeuver();
        if (maneuver != null) {
            if (maneuver.getAction() == Maneuver.Action.END) {
                updateDestinationManeuverData(maneuver);
            } else {
                updateManeuverData(maneuver);
            }
        }
    }

    @Override
    protected void handleRerouteBegin() {
        updateManeuverData(null);
    }

    /**
     * Creates {@link GuidanceManeuverData} and notifies on the changes.
     *
     * @param maneuver
     *         the maneuver to use.
     */
    private void updateDestinationManeuverData(Maneuver maneuver) {

        final long distance = getDestinationDistance();
        if (distance < DESTINATION_THRESHOLD_DISTANCE) {  // Less than 10 meters.
            notifyDestinationReached();
        }
        String street = getStreet(maneuver);
        if (street == null) {
            street = mContext.getString(R.string.msdkui_value_not_available);
        }
        notifyDataChanged(new GuidanceManeuverData(getIcon(maneuver),
                distance, getManeuverSignpost(maneuver), street,
                getNextRoadIcon(maneuver)));
    }

    /**
     * Creates {@link GuidanceManeuverData} and notifies on the changes.
     *
     * @param maneuver
     *         the maneuver to use.
     */
    private void updateManeuverData(Maneuver maneuver) {
        if (maneuver == null) {
            notifyDataChanged(null);
        } else {
            notifyDataChanged(new GuidanceManeuverData(getIcon(maneuver),
                    getNextManeuverDistance(), getManeuverSignpost(maneuver), getStreet(maneuver),
                    getNextRoadIcon(maneuver)));
        }
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
     *
     * @param maneuver
     *         the {@link Maneuver} to get street name from.
     *
     * @return the street name.
     */
    private String getStreet(Maneuver maneuver) {
        return GuidanceManeuverUtil.determineNextManeuverStreet(mContext, maneuver, this);
    }

    /**
     * Gets next road icon for given {@link Maneuver}.
     *
     * @param maneuver
     *         the {@link Maneuver} to get next road icon from.
     *
     * @return {@link Bitmap} for next road icon.
     */
    private Bitmap getNextRoadIcon(Maneuver maneuver) {
        final Image roadImage = maneuver.getNextRoadImage();
        if (roadImage != null && roadImage.getHeight() > 0) {
            return getScaledBitmap(roadImage);
        }
        return null;
    }

    /**
     * Gets scaled bitmap from source image.
     *
     * @param source
     *         input {@link Image} for scaled bitmap.
     *
     * @return scaled bitmap from input {@link Image}.
     */
    private Bitmap getScaledBitmap(Image source) {
        final long originalWidth = source.getWidth();
        final long originalHeight = source.getHeight();

        final int maxHeight = mContext.getResources().getDimensionPixelSize(R.dimen.next_road_image_max_height);

        int width = mContext.getResources().getDimensionPixelSize(R.dimen.next_road_image_max_width);
        int height = (int) ((width * originalHeight) / originalWidth);

        if (height > maxHeight) {
            height = maxHeight;
            width = (int) ((height * originalWidth) / originalHeight);
        }
        return source.getBitmap(width, height);
    }

    /**
     * Adds a {@link GuidanceManeuverListener}.
     *
     * @param listener
     *         the listener to add to the list of listeners.
     */
    public void addListener(GuidanceManeuverListener listener) {
        if (listener != null && !mListener.contains(listener)) {
            mListener.add(listener);
        }
    }

    /**
     * Remove a {@link GuidanceManeuverListener}.
     *
     * @param listener
     *         the listener to remove from the list of listeners.
     */
    public void removeListener(GuidanceManeuverListener listener) {
        mListener.remove(listener);
    }

    /**
     * Notify on {@link GuidanceManeuverData} changes.
     */
    private void notifyDataChanged(GuidanceManeuverData data) {
        for (final GuidanceManeuverListener listener : mListener) {
            listener.onDataChanged(data);
        }
    }

    /**
     * Notify that destination was reached.
     */
    private void notifyDestinationReached() {
        for (final GuidanceManeuverListener listener : mListener) {
            listener.onDestinationReached();
        }
    }
}