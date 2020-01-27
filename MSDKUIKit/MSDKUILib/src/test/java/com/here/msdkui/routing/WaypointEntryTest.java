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

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Test class for {@link WaypointEntry} class.
 */
public class WaypointEntryTest extends RobolectricTest {

    private RouteWaypoint mRouteWaypoint;

    @Before
    public void setUp() {
        mRouteWaypoint = MockUtils.mockRouteWaypoint(MockUtils.mockGeoCoordinate(52.530555, 13.379257));
    }

    @Test
    public void createWaypointEntryWithoutName() {
        final WaypointEntry entry = new WaypointEntry(mRouteWaypoint);
        assertNotNull("Can not create entry with Route Waypoint", entry);
        assertNotNull("Can not get Route Waypoint from entry", entry.getRouteWaypoint());
        assertEquals("Gets wrong Route Waypoint", mRouteWaypoint, entry.getRouteWaypoint());
        assertEquals("Passing only Route Waypoint does return some name", entry.getStringLabel(), "");
        assertTrue("Default removal is not true", entry.isRemovable());
        assertTrue("Default swappable is not true", entry.isRemovable());
    }

    @Test
    public void createWaypointEntryWithName() {
        final String name = "HERE";
        final WaypointEntry entry = new WaypointEntry(mRouteWaypoint, name);
        assertNotNull("Can not create entry with Route Waypoint", entry);
        assertNotNull("Can not get Route Waypoint from entry", entry.getRouteWaypoint());
        assertEquals("Gets wrong Route Waypoint", mRouteWaypoint, entry.getRouteWaypoint());
        assertEquals("Entry doesnt return name", name, entry.getStringLabel());
        assertTrue("Default removal is not true", entry.isRemovable());
        assertTrue("Default swappable is not true", entry.isDraggable());
    }

    @Test
    public void modifyWaypointEntry() {
        final String name = "HERE";
        final String changedName = "Changed";
        final WaypointEntry entry = new WaypointEntry(mRouteWaypoint, name);
        entry.setStringLabel(changedName);
        assertTrue("Default removal is not true", entry.isRemovable());
        assertTrue("Default swappable is not true", entry.isDraggable());
        entry.setDraggable(false);
        entry.setRemovable(false);
        assertNotNull("Can not create entry with Route Waypoint", entry);
        assertNotNull("Can not get Route Waypoint from entry", entry.getRouteWaypoint());
        assertEquals("Gets wrong Route Waypoint", mRouteWaypoint, entry.getRouteWaypoint());
        assertEquals("Entry doesnt return name", changedName, entry.getStringLabel());
        assertFalse("Default removal is not true", entry.isRemovable());
        assertFalse("Default swappable is not true", entry.isDraggable());
    }

    @Test
    public void testGetNameToShow() {
        final String name = "HERE";
        WaypointEntry entry = new WaypointEntry(mRouteWaypoint, R.string.msdkui_waypoint_select_location);
        assertEquals(getString(R.string.msdkui_waypoint_select_location),
                entry.getLabel(getApplicationContext(), name));

        entry.setStringLabel(null);
        entry.setResourceIdLabel(0);
        assertEquals(name, entry.getLabel(getApplicationContext(), name));

        entry.setStringLabel("");
        assertEquals(name, entry.getLabel(getApplicationContext(), name));

        entry.setStringLabel(name);
        assertEquals(name, entry.getLabel(getApplicationContext(), name));
    }

    @Test
    public void testSetNameStringRes() {
        final WaypointEntry entry = new WaypointEntry(mRouteWaypoint);
        entry.setResourceIdLabel(R.string.msdkui_waypoint_select_location);
        assertEquals(R.string.msdkui_waypoint_select_location, entry.getResourceIdLabel());
    }

    @Test
    public void testSetRouteWaypoint() {
        final String name = "HERE";
        final WaypointEntry entry = new WaypointEntry(name);
        entry.setRouteWaypoint(mRouteWaypoint);
        assertEquals(mRouteWaypoint, entry.getRouteWaypoint());
    }
}
