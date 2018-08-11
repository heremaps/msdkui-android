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

package com.here.msdkui.routing;

import com.here.android.mpa.routing.RouteWaypoint;

/**
 * A convenience class that wraps {@link RouteWaypoint} and adds additional methods.
 */
public class WaypointEntry {

    private RouteWaypoint mRouteWaypoint;
    private String mName = "";
    private boolean mDraggable = true;
    private boolean mRemovable = true;

    /**
     * Constructs a new instance using the given label.
     *
     * <p>Note that this does not set a default {@link com.here.android.mpa.common.GeoCoordinate}.</p>
     *
     * @param label the text to show in the {@link WaypointList}.
     */
    public WaypointEntry(final String label) {
        mName = label;
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
     * Constructs a new instance using the given {@link RouteWaypoint} and label.
     *
     * @param waypoint {@link RouteWaypoint RouteWaypoint}.
     * @param name the label to show or null if the coordinates should be shown instead.
     */
    public WaypointEntry(final RouteWaypoint waypoint, final String name) {
        mRouteWaypoint = waypoint;
        mName = name;
    }

    /**
     * Gets the label associated with this instance.
     *
     * @return the label associated.
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the {@link RouteWaypoint} that should be associated with this instance.
     *
     * @return an instance of this class.
     */
    public WaypointEntry setName(final String name) {
        mName = name;
        return this;
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
     * Indicates whether this instance is draggable when used as element of a {@link WaypointList}.
     * Default is true.
     *
     * @return true if draggable, false otherwise.
     */
    public boolean isDraggable() {
        return mDraggable;
    }

    /**
     * Sets this instance to be draggable when used as element of a {@link WaypointList}.
     *
     * @param draggable true if item should be draggable, false otherwise.
     * @return an instance of this class.
     */
    public WaypointEntry setDraggable(final boolean draggable) {
        mDraggable = draggable;
        return this;
    }

    /**
     * Indicates whether this instance is removable when used as element of a {@link WaypointList}.
     * Default is true.
     *
     * @return true if it is removable, false otherwise.
     */
    public boolean isRemovable() {
        return mRemovable;
    }

    /**
     * Sets this instance to be removable when used as element of a {@link WaypointList}.
     *
     * @param removable true if item should be removable, false otherwise.
     * @return an instance of this class.
     */
    public WaypointEntry setRemovable(final boolean removable) {
        mRemovable = removable;
        return this;
    }
}
