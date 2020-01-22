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

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.robolectric.Shadows.shadowOf;


/**
 * Test class for {@link WaypointItem} class.
 */
public class WaypointItemTest extends RobolectricTest implements WaypointItem.Listener {

    private WaypointItem mWaypointItem;
    private boolean mIsRemoveCallbackCalled;

    @Before
    public void setUp() {
        mWaypointItem = new WaypointItem(getApplicationContext());
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
                .addAttribute(R.attr.draggingEnabled, "true")
                .addAttribute(R.attr.removingEnabledIcon, "@drawable/ic_remove_listitem")
                .addAttribute(R.attr.draggingEnabledIcon, "@drawable/ic_drag_listitem")
                .build();

        WaypointItem waypointItem = new WaypointItem(getContextWithTheme(), attributeSet);

        assertNotNull(waypointItem);
        assertTrue(waypointItem.isDragEnabled());
        assertTrue(waypointItem.isRemoveEnabled());
        assertEquals(
                shadowOf(getApplicationContext().getResources().getDrawable(R.drawable.ic_remove_listitem)).getCreatedFromResId(),
                shadowOf(waypointItem.getRemoveDrawable()).getCreatedFromResId());
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
}
