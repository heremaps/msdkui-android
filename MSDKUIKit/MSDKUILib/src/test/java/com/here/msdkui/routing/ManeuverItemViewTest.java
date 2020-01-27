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

import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.routing.Maneuver;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test class for {@link ManeuverItemView} class.
 */
public class ManeuverItemViewTest extends RobolectricTest {

    private ManeuverItemView mManeuverItemView;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mManeuverItemView = new ManeuverItemView(getApplicationContext());
    }

    @Test
    public void testInitWithAnotherConstruction() {
        mManeuverItemView = new ManeuverItemView(getApplicationContext(), null, 0, 0);
        createdViewShouldHaveProperInitialContent();
    }

    @Test
    public void createdViewShouldHaveProperInitialContent() {
        final ImageView icon = mManeuverItemView.findViewById(R.id.maneuver_icon_view);
        assertNotNull(icon);
        // by default, it should be visible
        assertSame("Maneuver icon is not visible by default", View.VISIBLE, icon.getVisibility());

        final TextView addressView = mManeuverItemView.findViewById(R.id.maneuver_address_view);
        assertNotNull(addressView);
        // by default, it should be visible
        assertSame("Address View is not visible by default", View.VISIBLE, addressView.getVisibility());

        final TextView distanceView = mManeuverItemView.findViewById(R.id.maneuver_distance_view);
        assertNotNull(distanceView);
        // by default, it should be visible
        assertSame("Drag Icon is not visible by default", View.VISIBLE, distanceView.getVisibility());

        final TextView instructionView = mManeuverItemView.findViewById(R.id.maneuver_instruction_view);
        assertNotNull(instructionView);
        // by default, it should be visible
        assertSame("Drag Icon is not visible by default", View.VISIBLE, instructionView.getVisibility());

        // there should not be any waypoint entry
        assertNull(mManeuverItemView.getManeuver());
        assertThat(mManeuverItemView.getVisibleSections().size(), equalTo(4));
    }

    @Test
    public void settingManeuverShouldPopulateView() {
        mManeuverItemView.setManeuver(getManeuvers(), 0);
        assertThat(mManeuverItemView.getVisibility(), equalTo(View.VISIBLE));
        assertNotNull(mManeuverItemView.getManeuver());
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingNullManeuverShouldThrowException() {
        mManeuverItemView.setManeuver(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setManeuverIncorrectPositionShouldThrowException() {
        mManeuverItemView.setManeuver(getManeuvers(), -1);
    }

    @Test
    public void testClick() {
        mManeuverItemView.setOnClickListener(v -> mCallbackCalled = true);
        mManeuverItemView.performClick();
        assertThat(mCallbackCalled, is(true));
    }

    @Test
    public void testSectionVisibilityModifications() {
        assertThat(mManeuverItemView.getVisibleSections().size(), equalTo(4));
        assertThat(mManeuverItemView.isSectionVisible(ManeuverItemView.Section.ADDRESS), is(true));
        mManeuverItemView.setSectionVisible(ManeuverItemView.Section.ADDRESS, false);
        assertThat(mManeuverItemView.getVisibleSections().size(), equalTo(3));
        assertThat(mManeuverItemView.isSectionVisible(ManeuverItemView.Section.ADDRESS), is(false));
        mManeuverItemView.setSectionVisible(ManeuverItemView.Section.ADDRESS, true);
        assertThat(mManeuverItemView.isSectionVisible(ManeuverItemView.Section.ADDRESS), is(true));
    }

    @Test
    public void testSettingVisibleSections() {
        EnumSet<ManeuverItemView.Section> set = EnumSet.of(ManeuverItemView.Section.INSTRUCTIONS,
                ManeuverItemView.Section.ADDRESS);
        mManeuverItemView.setVisibleSections(set);
        assertThat(mManeuverItemView.getVisibleSections().size(), equalTo(2));
        assertThat(mManeuverItemView.isSectionVisible(ManeuverItemView.Section.ADDRESS), is(true));
        assertThat(mManeuverItemView.isSectionVisible(ManeuverItemView.Section.ICON), is(false));
    }

    @Test
    public void testCreationWithIconSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visible, "icon")
                .build();

        ManeuverItemView item = new ManeuverItemView(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(ManeuverItemView.Section.ICON));
    }

    @Test
    public void testCreationWithInstructionsSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visible, "instructions")
                .build();

        ManeuverItemView item = new ManeuverItemView(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(ManeuverItemView.Section.INSTRUCTIONS));
    }

    @Test
    public void testCreationWithAddressSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visible, "address")
                .build();

        ManeuverItemView item = new ManeuverItemView(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(ManeuverItemView.Section.ADDRESS));
    }

    @Test
    public void testCreationWithDistanceSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visible, "distance")
                .build();

        ManeuverItemView item = new ManeuverItemView(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(ManeuverItemView.Section.DISTANCE));
    }

    public List<Maneuver> getManeuvers() {
        return new ArrayList<>(Collections.singletonList(MockUtils.mockManeuver()));
    }
}
