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

import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.RoadElement;
import com.here.android.mpa.routing.RouteElement;
import com.here.android.mpa.routing.RouteElements;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteTta;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;

import java.util.EnumSet;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test class for {@link RouteDescriptionItem} class.
 */
@PrepareForTest({GeoCoordinate.class, RouteTta.class, RouteElements.class, RouteElement.class, RoadElement.class, RoutePlan.class})
@PowerMockIgnore("com.here.msdkui.routing.SectionBar")
public class RouteDescriptionItemTest extends RobolectricTest {

    private static final int TOTAL_SECTION = 6;

    static {

    }

    private RouteDescriptionItem mRouteDescriptionItem;
    private boolean mCallbackCalled;

    @Before
    public void setUp() {
        mRouteDescriptionItem = new RouteDescriptionItem(getApplicationContext());
    }

    @Test
    public void createdViewShouldHaveProperInitialContent() {
        final ImageView icon = (ImageView) mRouteDescriptionItem.findViewById(R.id.desc_type_icon);
        assertNotNull(icon);
        // by default, it should be visible
        assertTrue("Route icon is not visible by default", icon.getVisibility() == View.VISIBLE);

        final TextView timeView = (TextView) mRouteDescriptionItem.findViewById(R.id.desc_time);
        assertNotNull(timeView);
        // by default, it should be visible
        assertTrue("Address View is not visible by default", timeView.getVisibility() == View.VISIBLE);

        final TextView trafficWarningView = (TextView) mRouteDescriptionItem.findViewById(R.id.desc_traffic_warning);
        assertNotNull(trafficWarningView);

        final TextView detailsView = (TextView) mRouteDescriptionItem.findViewById(R.id.desc_details);
        assertNotNull(detailsView);
        // by default, it should be visible
        assertTrue("Drag Icon is not visible by default", detailsView.getVisibility() == View.VISIBLE);

        final SectionBar sectionBar = (SectionBar) mRouteDescriptionItem.findViewById(R.id.desc_bar);
        assertNotNull(sectionBar);

        assertThat(mRouteDescriptionItem.getVisibility(), equalTo(View.INVISIBLE));
        // there should not be any waypoint entry
        assertNull(mRouteDescriptionItem.getRoute());
        assertThat(mRouteDescriptionItem.getVisibleSections().size(), equalTo(TOTAL_SECTION));
    }

    @Test
    public void settingRouteShouldPopulateView() {
        mRouteDescriptionItem.setRoute(new MockUtils.MockRouteBuilder().getRoute());
        assertThat(mRouteDescriptionItem.getVisibility(), equalTo(View.VISIBLE));
        assertNotNull(mRouteDescriptionItem.getRoute());
    }

    @Test
    public void trafficWarningShouldBeVisibleForTrafficAndCar() {
        mRouteDescriptionItem.setTrafficEnabled(true);
        mRouteDescriptionItem.setRoute(new MockUtils.MockRouteBuilder().getRoute());
        assertTrue(mRouteDescriptionItem.isSectionVisible(RouteDescriptionItem.Section.TRAFFIC_WARNING));
    }

    @Test
    public void testIsBikeOrPedestrian() {
        mRouteDescriptionItem.setRoute(new MockUtils.MockRouteBuilder().setTransportMode(RouteOptions.TransportMode.BICYCLE).getRoute());
        assertTrue(mRouteDescriptionItem.isBikeOrPedestrian());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullRouteException() {
        mRouteDescriptionItem.setRoute(null);
    }

    @Test
    public void testClick() {
        mRouteDescriptionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbackCalled = true;
            }
        });
        mRouteDescriptionItem.performClick();
        assertThat(mCallbackCalled, is(true));
    }

    @Test
    public void testSectionVisibilityModifications() {
        assertThat(mRouteDescriptionItem.getVisibleSections().size(), equalTo(TOTAL_SECTION));
        assertThat(mRouteDescriptionItem.isSectionVisible(RouteDescriptionItem.Section.ARRIVAL_TIME), is(true));
        mRouteDescriptionItem.setSectionVisible(RouteDescriptionItem.Section.ARRIVAL_TIME, false);
        assertThat(mRouteDescriptionItem.getVisibleSections().size(), equalTo(TOTAL_SECTION - 1));
        assertThat(mRouteDescriptionItem.isSectionVisible(RouteDescriptionItem.Section.ARRIVAL_TIME), is(false));
    }

    @Test
    public void testSettingVisibleSections() {
        EnumSet set = EnumSet.of(RouteDescriptionItem.Section.DETAILS, RouteDescriptionItem.Section.TIME);
        mRouteDescriptionItem.setVisibleSections(set);
        assertThat(mRouteDescriptionItem.getVisibleSections().size(), equalTo(2));
        assertThat(mRouteDescriptionItem.isSectionVisible(RouteDescriptionItem.Section.DETAILS), is(true));
        assertThat(mRouteDescriptionItem.isSectionVisible(RouteDescriptionItem.Section.TIME), is(true));
        assertThat(mRouteDescriptionItem.isSectionVisible(RouteDescriptionItem.Section.ARRIVAL_TIME), is(false));
    }

    @Test
    public void testSetSectionVisible() {
        mRouteDescriptionItem.setSectionVisible(RouteDescriptionItem.Section.TRAFFIC_WARNING, true);
        assertTrue(mRouteDescriptionItem.isSectionVisible(RouteDescriptionItem.Section.TRAFFIC_WARNING));

        mRouteDescriptionItem.setSectionVisible(RouteDescriptionItem.Section.TRAFFIC_WARNING, false);
        assertFalse(mRouteDescriptionItem.isSectionVisible(RouteDescriptionItem.Section.TRAFFIC_WARNING));
    }

    @Test
    public void testCreationWithTypeSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visibleSection,"0x01")
                .build();

        RouteDescriptionItem item = new RouteDescriptionItem(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(RouteDescriptionItem.Section.TYPE_ICON));
    }

    @Test
    public void testCreationWithTimeSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visibleSection,"0x02")
                .build();

        RouteDescriptionItem item = new RouteDescriptionItem(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(RouteDescriptionItem.Section.TIME));
    }

    @Test
    public void testCreationWithDetailsSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visibleSection,"0x04")
                .build();

        RouteDescriptionItem item = new RouteDescriptionItem(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(RouteDescriptionItem.Section.DETAILS));
    }

    @Test
    public void testCreationWithTrafficWarningSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visibleSection,"0x08")
                .build();

        RouteDescriptionItem item = new RouteDescriptionItem(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(RouteDescriptionItem.Section.TRAFFIC_WARNING));
    }

    @Test
    public void testCreationWithArrivalTimeSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visibleSection,"0x10")
                .build();

        RouteDescriptionItem item = new RouteDescriptionItem(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(RouteDescriptionItem.Section.ARRIVAL_TIME));
    }

    @Test
    public void testCreationWithBarSectionVisible() {
        AttributeSet attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.visibleSection,"0x20")
                .build();

        RouteDescriptionItem item = new RouteDescriptionItem(getContextWithTheme(), attributeSet);

        assertNotNull(item);
        assertTrue(item.isSectionVisible(RouteDescriptionItem.Section.SECTION_BAR));
    }
}
