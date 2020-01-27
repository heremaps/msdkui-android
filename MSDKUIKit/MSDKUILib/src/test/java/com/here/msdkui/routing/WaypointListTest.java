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

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.routing.RouteWaypoint;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test class for {@link WaypointList} class.
 */
public class WaypointListTest extends RobolectricTest {

    private WaypointList mWaypointList;

    @Before
    public void setUp() {
        final Context context = RuntimeEnvironment.application.getApplicationContext();
        mWaypointList = new WaypointList(context);
    }

    @Test
    public void initShouldHaveProperDefaultValues() {
        assertThat(mWaypointList.getAdapter().getItemCount(), equalTo(2));
        assertThat(mWaypointList.getEntriesCount(), equalTo(2));
    }

    @Test
    public void testMinWaypointItems() {
        mWaypointList.setMinWaypointItems(3);
        // add one more
        assertThat(mWaypointList.getAdapter().getItemCount(), equalTo(3));
        assertThat(mWaypointList.getEntriesCount(), equalTo(3));
        assertThat(mWaypointList.getMinWaypointItems(), equalTo(3));
    }

    @Test
    public void testMaxWaypointItems() {
        mWaypointList.setMaxWaypointItems(10);
        assertThat(mWaypointList.getMaxWaypointItems(), equalTo(10));
    }

    @Test
    public void testGetRouteWaypoints() {
        List<RouteWaypoint> waypoints = mWaypointList.getRouteWaypoints();
        assertEquals(waypoints.size(), mWaypointList.getEntriesCount());
    }

    @Test
    public void testVisibleWaypointItems() {
        mWaypointList.setVisibleNumberOfWaypointItems(3);
        assertThat(mWaypointList.getVisibleWaypointItems(), equalTo(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinWaypointItemsMustBeGreaterThan1() {
        mWaypointList.setMinWaypointItems(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxWaypointItemsCantBeSmallerThanMinWaypointItems() {
        mWaypointList.setMinWaypointItems(4);
        mWaypointList.setMaxWaypointItems(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVisibleNumberOfWaypointMustBeBetweenMinAndMaxCount() {
        mWaypointList.setMinWaypointItems(3);
        mWaypointList.setMaxWaypointItems(8);
        mWaypointList.setVisibleNumberOfWaypointItems(2);
    }

    @Test
    public void addEntryShouldAddRow() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        mWaypointList.addEntry(entry);
        assertThat(mWaypointList.getAdapter().getItemCount(), equalTo(3));
        assertThat(mWaypointList.getEntriesCount(), equalTo(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullEntryException() {
        mWaypointList.addEntry(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEntryOverMaxCapacityException() {
        final WaypointList waypointList = new WaypointList(getApplicationContext());
        int count = waypointList.getEntriesCount();
        waypointList.setMaxWaypointItems(count);

        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        waypointList.addEntry(entry);
    }

    @Test
    public void insertEntryShouldInsertRow() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        mWaypointList.insertEntry(0, entry);
        assertThat(mWaypointList.getAdapter().getItemCount(), equalTo(3));
        assertThat(mWaypointList.getEntriesCount(), equalTo(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertOnInvalidIndexException() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        mWaypointList.insertEntry(-1, entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertOverMaxCapacityException() {
        final WaypointList waypointList = new WaypointList(getApplicationContext());
        int count = waypointList.getEntriesCount();
        waypointList.setMaxWaypointItems(count);

        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        waypointList.insertEntry(0, entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertNullEntryException() {
        mWaypointList.insertEntry(0, null);
    }

    @Test
    public void insertEmptyEntryShouldAddEmptyRow() {
        mWaypointList.insertEmptyEntry(0);
        assertNotNull(mWaypointList.getEntries().get(0));
        assertEquals("", mWaypointList.getEntries().get(0).getStringLabel());
    }

    @Test
    public void updateEntryShouldUpdateRowData() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        entry.setDraggable(false);
        mWaypointList.updateEntry(0, entry);
        assertThat(mWaypointList.getEntries().get(0).isDraggable(), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEntryInvalidIndexException() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        mWaypointList.updateEntry(-1, entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEntryWithNullException() {
        mWaypointList.updateEntry(0, null);
    }

    @Test
    public void removeEntryShouldRemoveRow() {
        //first add
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        mWaypointList.addEntry(entry);
        assertThat(mWaypointList.getAdapter().getItemCount(), equalTo(3));
        assertThat(mWaypointList.getEntriesCount(), equalTo(3));

        // then remove
        mWaypointList.removeEntry(2);
        assertThat(mWaypointList.getAdapter().getItemCount(), equalTo(2));
        assertThat(mWaypointList.getEntriesCount(), equalTo(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveOnInvalidIndexException() {
        mWaypointList.removeEntry(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveBelowMinCountException() {
        mWaypointList.removeEntry(0);
    }

    @Test
    public void resetListShouldResetTheList() {
        assertThat(mWaypointList.getAdapter().getItemCount(), equalTo(2));
        //first add
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        mWaypointList.addEntry(entry);
        assertThat(mWaypointList.getAdapter().getItemCount(), equalTo(3));
        mWaypointList.reset();
        assertThat(mWaypointList.getAdapter().getItemCount(), equalTo(2));
    }

    @Test
    public void setEntriesShouldCreateRows() {
        List<WaypointEntry> entryList = new ArrayList<>(2);
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        entryList.add(entry);
        entryList.add(entry);
        mWaypointList.setEntries(entryList);
        assertThat(mWaypointList.getAdapter().getItemCount(), equalTo(2));
        assertThat(mWaypointList.getEntriesCount(), equalTo(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingLessThanMinShouldThrowException() {
        List<WaypointEntry> entryList = new ArrayList<>();
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        entryList.add(entry);
        mWaypointList.setEntries(entryList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingMoreThanMaxShouldThrowException() {
        List<WaypointEntry> entryList = new ArrayList<>();
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        for (int i = 0; i < 17; i++) {
            entryList.add(entry);
        }
        mWaypointList.setEntries(entryList);
    }
}
