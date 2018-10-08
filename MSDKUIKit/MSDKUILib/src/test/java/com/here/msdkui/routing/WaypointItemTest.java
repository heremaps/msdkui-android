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

import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;


/**
 * Test class for {@link WaypointItem} class.
 */
@PowerMockIgnore({ "com.here.msdkui.routing.DraggableImageView" })
@PrepareForTest({RouteWaypoint.class, GeoCoordinate.class})
public class WaypointItemTest extends RobolectricTest implements WaypointItem.Listener {

    private WaypointItem mWaypointItem;
    private boolean mIsDragCallbackCalled;
    private boolean mIsRemoveCallbackCalled;

    @Before
    public void setUp() {
        mWaypointItem = new WaypointItem(getApplicationContext());
        mIsDragCallbackCalled = false;
        mIsRemoveCallbackCalled = false;
    }

    @Test
    public void createdViewShouldHaveProperInitialContent() {
        final ImageView removeIcon = mWaypointItem.findViewById(R.id.remove_icon);
        assertNotNull(removeIcon);
        // by default, it should be visible
        assertSame("Remove icon is not visible by default", View.VISIBLE, removeIcon.getVisibility());

        final TextView label = mWaypointItem.findViewById(R.id.waypoint_label);
        assertNotNull(label);
        // by default, it should be visible
        assertSame("ItemRow label is not visible by default", View.VISIBLE, label.getVisibility());

        final DraggableImageView dragIcon = mWaypointItem.findViewById(R.id.drag_icon);
        assertNotNull(dragIcon);
        // by default, it should be visible
        assertSame("Drag Icon is not visible by default", View.VISIBLE, dragIcon.getVisibility());

        // default draggable and removal are enabled
        assertTrue("Default draggable is not enabled", mWaypointItem.isDragEnabled());
        assertTrue("Default removable is not enabled", mWaypointItem.isRemoveEnabled());

        // there should not be any waypoint entry
        assertNull(mWaypointItem.getWaypointEntry());
    }

    @Test
    public void settingEntryShouldPopulateView() {
        final WaypointEntry entry = MockUtils.mockWayPointEntry();
        mWaypointItem.setWaypointEntry(entry);
        assertNotNull(mWaypointItem.getWaypointEntry());
    }

    @Test
    public void settingDraggableShouldMakeItemDraggable() {

        mWaypointItem.setDragEnabled(false);
        assertFalse("Setting draggable false returns draggable true", mWaypointItem.isDragEnabled());
        mWaypointItem.setDragEnabled(true);
        assertTrue("Setting draggable true returns draggable false", mWaypointItem.isDragEnabled());
        final ImageView dragIcon = mWaypointItem.findViewById(R.id.drag_icon);

        mWaypointItem.setListener(this);

        final long downTime = SystemClock.uptimeMillis();
        final long eventTime = SystemClock.uptimeMillis() + 100;
        MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, 0.0f, 0.0f, 0);
        // Dispatch touch event to view
        dragIcon.dispatchTouchEvent(motionEvent);

        // we should get drag event
        assertTrue("Drag callback not called when dragging", mIsDragCallbackCalled);
    }

    @Test
    public void settingRemovableShouldMakeItemRemovable() {

        mWaypointItem.setRemoveEnabled(false);
        assertFalse("Setting removable false returns removable true", mWaypointItem.isRemoveEnabled());
        mWaypointItem.setRemoveEnabled(true);
        assertTrue("Setting removable true returns removable false", mWaypointItem.isRemoveEnabled());
        final ImageView removeIcon = mWaypointItem.findViewById(R.id.remove_icon);

        mWaypointItem.setListener(this);

        removeIcon.performClick();

        // we should get remove event
        assertTrue("Drag callback not called when dragging", mIsRemoveCallbackCalled);
    }

    @Test
    public void testSetRemoveDrawable() {
        Drawable drawable = mock(Drawable.class);
        mWaypointItem.setRemoveDrawable(drawable);
        assertEquals(drawable, mWaypointItem.getRemoveDrawable());
    }

    @Test
    public void testSetDragDrawable() {
        Drawable drawable = mock(Drawable.class);
        mWaypointItem.setDragDrawable(drawable);
        assertEquals(drawable, mWaypointItem.getDragDrawable());
    }

    @Test
    public void testCreationWithAttributes() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.removingEnabled, "true")
                .addAttribute(R.attr.draggingEnabled, " true")
                .addAttribute(R.attr.removingEnabledIcon, "@drawable/ic_remove_listitem")
                .addAttribute(R.attr.draggingEnabledIcon, "@drawable/ic_drag_listitem")
                .build();

        WaypointItem waypointItem = new WaypointItem(getContextWithTheme(), attributeSet);

        assertNotNull(waypointItem);
        assertTrue(waypointItem.isDragEnabled());
        assertTrue(waypointItem.isRemoveEnabled());
        assertEquals(getContextWithTheme().getResources().getDrawable(R.drawable.ic_remove_listitem), waypointItem.getRemoveDrawable());
    }

    /**
     * A callback indicating the user removed this item from list.
     *
     * @param entry -  {@link WaypointEntry WaypointEntry} removed.
     */
    @Override
    public void onRemoveClicked(WaypointEntry entry) {
        mIsRemoveCallbackCalled = true;
    }

    /**
     * A callback indicating the user has started a drag on {@link WaypointEntry WaypointEntry}.
     *
     * @param entry - current {@link WaypointEntry WaypointEntry}.
     */
    @Override
    public void onDragStarted(WaypointEntry entry) {
        mIsDragCallbackCalled = true;
    }
}
