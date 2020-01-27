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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.here.android.mpa.routing.RouteWaypoint;

/**
 * A convenience class that wraps {@link RouteWaypoint} and adds additional methods.
 */
public class WaypointEntry {

    private RouteWaypoint mRouteWaypoint;
    private String mLabel = "";
    private int mResourceIdLabel = 0;
    private boolean mDraggable = true;
    private boolean mRemovable = true;

    /**
     * Constructs a new instance using the given string label.
     *
     * <p>Note that this does not set a default {@link com.here.android.mpa.common.GeoCoordinate}.</p>
     *
     * @param label the text to show in the {@link WaypointList}.
     */
    public WaypointEntry(final String label) {
        mLabel = label;
    }

    /**
     * Constructs a new instance using the given resource id label. For more information about
     * resource id label see {@link #setResourceIdLabel (int)}
     *
     * <p>Note that this does not set a default {@link com.here.android.mpa.common.GeoCoordinate}.</p>
     *
     * @param resourceIdLabel the resource id of a string to show in the {@link WaypointList}.
     */
    public WaypointEntry(@StringRes final int resourceIdLabel) {
        mResourceIdLabel = resourceIdLabel;
    }

    /**
     * Constructs a new instance using the given {@link RouteWaypoint}.
     *
     * @param waypoint the {@link RouteWaypoint} to set. If no label is provided, the coordinates will be shown
     *                 in the {@link WaypointList}.
     */
    public WaypointEntry(final RouteWaypoint waypoint) {
        mRouteWaypoint = waypoint;
    }

    /**
     * Constructs a new instance using the given {@link RouteWaypoint} and string label.
     *
     * @param waypoint {@link RouteWaypoint RouteWaypoint}.
     * @param label the label to show or null if the coordinates should be shown instead.
     */
    public WaypointEntry(final RouteWaypoint waypoint, final String label) {
        mRouteWaypoint = waypoint;
        mLabel = label;
    }

    /**
     * Constructs a new instance using the given {@link RouteWaypoint} and resource id label.
     * For more information about resource id label see {@link #setResourceIdLabel (int)}
     *
     * @param waypoint {@link RouteWaypoint RouteWaypoint}.
     * @param resourceIdLabel the resource id of a string to show or 0 if the coordinates should be shown instead.
     */
    public WaypointEntry(final RouteWaypoint waypoint, @StringRes final int resourceIdLabel) {
        mRouteWaypoint = waypoint;
        mResourceIdLabel = resourceIdLabel;
    }

    /**
     * Gets the string label associated with this instance.
     *
     * @return the label associated or null.
     */
    public @Nullable String getStringLabel() {
        return mLabel;
    }

    /**
     * Sets the string label to this instance.
     *
     * @param label
     *         a string label for this entry.
     *
     * @return an instance of this class.
     */
    public WaypointEntry setStringLabel(final String label) {
        mLabel = label;
        return this;
    }

    /**
     * Gets the resource string id associated with this instance.
     *
     * @return the resource string id associated.
     */
    public @StringRes int getResourceIdLabel() {
        return mResourceIdLabel;
    }

    /**
     * Sets the resource id label to this instance. Use resource id label instead of string
     * label for translatable name. See also {@link #getLabel(Context, String)}.
     *
     * @param stringId
     *         a resource string id for this entry.
     *
     * @return an instance of this class.
     */
    public WaypointEntry setResourceIdLabel(final @StringRes int stringId) {
        mResourceIdLabel = stringId;
        return this;
    }

    /**
     * Gets label to be shown for this instance.
     *
     * @param context the required {@link Context}.
     *
     * @param defaultValue string that will be returned if string label and resource id label
     *                     are invalid.
     *
     * @return first priority have string label (if is not null and not empty)
     * then resource id label (if is valid), otherwise defaultValue param is returned.
     */
    public String getLabel(final @NonNull Context context, final @NonNull String defaultValue) {
        String stringLabel = getStringLabel();
        if (stringLabel != null && !stringLabel.isEmpty()) {
            return stringLabel;
        } else if (getResourceIdLabel() != 0) {
            return context.getString(getResourceIdLabel());
        } else {
            return defaultValue;
        }
    }

    /**
     * Gets the {@link RouteWaypoint} associated with this instance.
     *
     * @return the {@link RouteWaypoint} associated.
     */
    public RouteWaypoint getRouteWaypoint() {
        return mRouteWaypoint;
    }

    /**
     * Sets the {@link RouteWaypoint} for this instance.
     *
     * @param routeWaypoint
     *         a {@link RouteWaypoint} instance.
     *
     * @return an instance of this class.
     */
    public WaypointEntry setRouteWaypoint(final RouteWaypoint routeWaypoint) {
        mRouteWaypoint = routeWaypoint;
        return this;
    }

    /**
     * Indicates whether this instance contains a valid coordinate.
     *
     * @return true if the associated {@link RouteWaypoint} and {@link RouteWaypoint#getOriginalPosition()}
     * is not null, false otherwise.
     */
    public boolean isValid() {
        return getRouteWaypoint() != null && getRouteWaypoint().getOriginalPosition() != null;
    }

    /**
     * Indicates whether this instance is draggable when used as an element of a {@link WaypointList}.
     * Default is true.
     *
     * @return true if draggable, false otherwise.
     */
    public boolean isDraggable() {
        return mDraggable;
    }

    /**
     * Sets this instance to be draggable when used as an element of a {@link WaypointList}.
     *
     * @param draggable true if item should be draggable, false otherwise.
     * @return an instance of this class.
     */
    public WaypointEntry setDraggable(final boolean draggable) {
        mDraggable = draggable;
        return this;
    }

    /**
     * Indicates whether this instance is removable when used as an element of a {@link WaypointList}.
     * Default is true.
     *
     * @return true if it is removable, false otherwise.
     */
    public boolean isRemovable() {
        return mRemovable;
    }

    /**
     * Sets this instance to be removable when used as an element of a {@link WaypointList}.
     *
     * @param removable true if item should be removable, false otherwise.
     * @return an instance of this class.
     */
    public WaypointEntry setRemovable(final boolean removable) {
        mRemovable = removable;
        return this;
    }
}
