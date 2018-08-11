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

import android.view.View;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.PowerMockito.doReturn;


/**
 * Test class for {@link WaypointListAdapter} class.
 */
@PowerMockIgnore({ "com.here.msdkui.routing.DraggableImageView" })
@PrepareForTest({RouteWaypoint.class, GeoCoordinate.class})
public class WaypointListAdapterTest extends RobolectricTest implements WaypointList.Listener {

    private boolean mIsCallbackCalled;
    private WaypointListAdapter mWaypointListAdapter;
    private WaypointListAdapter.WaypointsListViewHolder mWaypointsListViewHolder;
    private ArrayList<WaypointListAdapter.WaypointsListViewHolder> mWaypointsListViewHoldersList = new ArrayList<>();

    @Before
    public void setUp() {
        mIsCallbackCalled = false;
    }

    @Test
    public void itemCount() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        mWaypointListAdapter = new WaypointListAdapter(Arrays.asList(entry, entry, entry));
        assertThat(mWaypointListAdapter.getItemCount(), equalTo(3));
    }

    @Test
    public void clickEntryViewShouldCallCallback() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        entry.setName("Test");
        getFirstViewHolder(new ArrayList<WaypointEntry>(Arrays.asList(entry)));
        assertThat(mWaypointsListViewHolder.getEntryView().getText().toString(), equalTo("Test"));
        mWaypointsListViewHolder.getEntryView()
                .performClick();
        assertThat(mIsCallbackCalled, is(true));
    }

    @Test
    public void removeButtonShouldRemoveRowAndCallCallback() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        getFirstViewHolder(new ArrayList<WaypointEntry>(Arrays.asList(entry)));
        // by default row should have removable view visible.
        assertThat(mWaypointsListViewHolder.getRemovableView().getVisibility(), equalTo(View.VISIBLE));
        mWaypointsListViewHolder.getRemovableView()
                .performClick();
        assertThat(mIsCallbackCalled, is(true));
        assertThat(mWaypointListAdapter.getItemCount(), equalTo(0));
    }

    @Test
    public void removeButtonShouldNotRemoveRowWhenNotVisible() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        entry.setRemovable(false);
        getFirstViewHolder(new ArrayList<WaypointEntry>(Arrays.asList(entry)));
        // by default row should have removable view visible.
        assertThat(mWaypointsListViewHolder.getRemovableView().getVisibility(), equalTo(View.GONE));
    }

    @Test
    public void dragButtonShouldBeVisibleWhenSet() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        entry.setDraggable(false);
        getFirstViewHolder(new ArrayList<WaypointEntry>(Arrays.asList(entry)));
        // by default row should have removable view visible.
        assertThat(mWaypointsListViewHolder.getDraggableView().getVisibility(), equalTo(View.GONE));
    }

    @Test
    public void addedEntriesShouldHaveCorrectFromToPrefixes() {
        final WaypointEntry entry = new WaypointEntry("Test");
        prepareViewHoldersList(new ArrayList<WaypointEntry>(Arrays.asList(entry, entry, entry)));
        WaypointListAdapter.WaypointsListViewHolder holder;

        holder = getViewHolderFromList(0);
        assertThat(holder.getEntryView().getText().toString(),
                equalTo(holder.getEntryView().getContext().getString(R.string.msdkui_rp_from, "Test")));
        assertThat(holder.getRemovableView().getVisibility(), equalTo(View.VISIBLE));

        holder = getViewHolderFromList(1);
        assertThat(holder.getEntryView().getText().toString(), equalTo("Test"));
        assertThat(holder.getRemovableView().getVisibility(), equalTo(View.VISIBLE));

        holder = getViewHolderFromList(2);
        assertThat(holder.getEntryView().getText().toString(),
                equalTo(holder.getEntryView().getContext().getString(R.string.msdkui_rp_to, "Test")));
        assertThat(holder.getRemovableView().getVisibility(), equalTo(View.VISIBLE));
    }

    @Test
    public void validEntryShouldNotHaveFromToPrefix() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        entry.setName("Test");
        getFirstViewHolder(new ArrayList<WaypointEntry>(Arrays.asList(entry)));
        assertThat(mWaypointsListViewHolder.getEntryView().getText().toString(), equalTo("Test"));
    }

    private void getFirstViewHolder(final List<WaypointEntry> entries) {
        mWaypointListAdapter = new WaypointListAdapter(entries);
        assertThat(mWaypointListAdapter.getItemCount(), equalTo(entries.size()));
        mWaypointListAdapter.setListener(this);
        final WaypointItem itemView = new WaypointItem(RuntimeEnvironment.application.getApplicationContext());
        mWaypointsListViewHolder = spy(mWaypointListAdapter.new WaypointsListViewHolder(itemView));
        doReturn(0).when(mWaypointsListViewHolder)
                .getAdapterPosition();
        mWaypointListAdapter.onBindViewHolder(mWaypointsListViewHolder, 0);
    }

    private void prepareViewHoldersList(final List<WaypointEntry> entries) {
        mWaypointListAdapter = new WaypointListAdapter(entries);
        assertThat(mWaypointListAdapter.getItemCount(), equalTo(entries.size()));
        mWaypointListAdapter.setListener(this);
        mWaypointsListViewHoldersList.clear();
        for (int i = 0; i < entries.size(); i++) {
            final WaypointItem itemView = new WaypointItem(RuntimeEnvironment.application.getApplicationContext());
            WaypointListAdapter.WaypointsListViewHolder holder = spy(mWaypointListAdapter.new WaypointsListViewHolder(itemView));
            doReturn(i).when(holder)
                    .getAdapterPosition();
            mWaypointListAdapter.onBindViewHolder(holder, i);
            mWaypointsListViewHoldersList.add(holder);
        }
    }

    private WaypointListAdapter.WaypointsListViewHolder getViewHolderFromList(int index) {
        if (index < 0 || index >= mWaypointsListViewHoldersList.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds!");
        }
        return mWaypointsListViewHoldersList.get(index);
    }

    @Override
    public void onEntryClicked(final int index, final WaypointEntry current) {
        mIsCallbackCalled = true;
    }

    @Override
    public void onEntryAdded(final int index, final WaypointEntry entry) {

    }

    @Override
    public void onEntryUpdated(final int index, final WaypointEntry entry) {

    }

    @Override
    public void onEntryRemoved(final int index, final WaypointEntry entry) {
        mIsCallbackCalled = true;
    }

    @Override
    public void onEntryDragged(final int fromIdx, final int toIdx) {
        mIsCallbackCalled = true;
    }
}
