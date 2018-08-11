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

import android.content.Context;
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
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test class for {@link ManeuverDescriptionItem} class.
 */
public class ManeuverDescriptionItemTest extends RobolectricTest {

    private ManeuverDescriptionItem mManeuverDescriptionItem;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mManeuverDescriptionItem = new ManeuverDescriptionItem(getApplicationContext());
    }

    @Test
    public void createdViewShouldHaveProperInitialContent() {
        final ImageView icon = (ImageView) mManeuverDescriptionItem.findViewById(R.id.maneuver_icon_view);
        assertNotNull(icon);
        // by default, it should be visible
        assertTrue("Maneuver icon is not visible by default", icon.getVisibility() == View.VISIBLE);

        final TextView addressView = (TextView) mManeuverDescriptionItem.findViewById(R.id.maneuver_address_view);
        assertNotNull(addressView);
        // by default, it should be visible
        assertTrue("Address View is not visible by default", addressView.getVisibility() == View.VISIBLE);

        final TextView distanceView = (TextView) mManeuverDescriptionItem.findViewById(R.id.maneuver_distance_view);
        assertNotNull(distanceView);
        // by default, it should be visible
        assertTrue("Drag Icon is not visible by default", distanceView.getVisibility() == View.VISIBLE);

        final TextView instructionView = (TextView) mManeuverDescriptionItem.findViewById(R.id.maneuver_instruction_view);
        assertNotNull(instructionView);
        // by default, it should be visible
        assertTrue("Drag Icon is not visible by default", instructionView.getVisibility() == View.VISIBLE);

        // there should not be any waypoint entry
        assertNull(mManeuverDescriptionItem.getManeuver());
        assertThat(mManeuverDescriptionItem.getVisibleSections().size(), equalTo(4));
    }

    @Test
    public void settingManeuverShouldPopulateView() {
        mManeuverDescriptionItem.setManeuver(getManeuvers(), 0);
        assertThat(mManeuverDescriptionItem.getVisibility(), equalTo(View.VISIBLE));
        assertNotNull(mManeuverDescriptionItem.getManeuver());
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingNullManeuverShouldThrowException() {
        mManeuverDescriptionItem.setManeuver(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setManeuverIncorrectPositionShouldThrowException() {
        mManeuverDescriptionItem.setManeuver(getManeuvers(), -1);
    }

    @Test
    public void testClick() {
        mManeuverDescriptionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbackCalled = true;
            }
        });
        mManeuverDescriptionItem.performClick();
        assertThat(mCallbackCalled, is(true));
    }

    @Test
    public void testSectionVisibilityModifications() {
        assertThat(mManeuverDescriptionItem.getVisibleSections().size(), equalTo(4));
        assertThat(mManeuverDescriptionItem.isSectionVisible(ManeuverDescriptionItem.Section.ADDRESS), is(true));
        mManeuverDescriptionItem.setSectionVisible(ManeuverDescriptionItem.Section.ADDRESS, false);
        assertThat(mManeuverDescriptionItem.getVisibleSections().size(), equalTo(3));
        assertThat(mManeuverDescriptionItem.isSectionVisible(ManeuverDescriptionItem.Section.ADDRESS), is(false));
        mManeuverDescriptionItem.setSectionVisible(ManeuverDescriptionItem.Section.ADDRESS, true);
        assertThat(mManeuverDescriptionItem.isSectionVisible(ManeuverDescriptionItem.Section.ADDRESS), is(true));
    }

    @Test
    public void testSettingVisibleSections() {
        EnumSet set = EnumSet.of(ManeuverDescriptionItem.Section.INSTRUCTIONS, ManeuverDescriptionItem.Section.ADDRESS);
        mManeuverDescriptionItem.setVisibleSections(set);
        assertThat(mManeuverDescriptionItem.getVisibleSections().size(), equalTo(2));
        assertThat(mManeuverDescriptionItem.isSectionVisible(ManeuverDescriptionItem.Section.ADDRESS), is(true));
        assertThat(mManeuverDescriptionItem.isSectionVisible(ManeuverDescriptionItem.Section.ICON), is(false));
    }

    @Test
    public void testCreationWithIconSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visible,"0x01")
                .build();

        ManeuverDescriptionItem item = new ManeuverDescriptionItem(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(ManeuverDescriptionItem.Section.ICON));
    }

    @Test
    public void testCreationWithInstructionsSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visible,"0x02")
                .build();

        ManeuverDescriptionItem item = new ManeuverDescriptionItem(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(ManeuverDescriptionItem.Section.INSTRUCTIONS));
    }

    @Test
    public void testCreationWithAddressSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visible,"0x04")
                .build();

        ManeuverDescriptionItem item = new ManeuverDescriptionItem(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(ManeuverDescriptionItem.Section.ADDRESS));
    }

    @Test
    public void testCreationWithDistanceSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visible,"0x08")
                .build();

        ManeuverDescriptionItem item = new ManeuverDescriptionItem(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(ManeuverDescriptionItem.Section.DISTANCE));
    }

    public List<Maneuver> getManeuvers() {
        return new ArrayList<Maneuver>(Arrays.asList(MockUtils.mockManeuver()));
    }
}
