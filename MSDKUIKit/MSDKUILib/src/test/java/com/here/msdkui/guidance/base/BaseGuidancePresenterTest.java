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

package com.here.msdkui.guidance.base;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteTta;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import java.lang.ref.WeakReference;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link BaseGuidancePresenter}.
 */
public class BaseGuidancePresenterTest extends RobolectricTest {

    private NavigationManager mNavigationManager;
    private BaseGuidancePresenter mBaseGuidancePresenter;

    @Captor
    private ArgumentCaptor<WeakReference<NavigationManager.ManeuverEventListener>> mManeuverEventCaptor;

    @Captor
    private ArgumentCaptor<WeakReference<NavigationManager.NewInstructionEventListener>> mNewInstructionCaptor;

    @Captor
    private ArgumentCaptor<WeakReference<NavigationManager.PositionListener>> mPositionListenerCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mNavigationManager = mock(NavigationManager.class);
        mBaseGuidancePresenter = new BaseGuidancePresenter(mNavigationManager, mock(Route.class));
    }

    @Test
    public void testManeuverEvent() {
        mBaseGuidancePresenter.resume();
        verify(mNavigationManager).addManeuverEventListener(mManeuverEventCaptor.capture());
        assertNotNull(mManeuverEventCaptor.getValue().get());
        mBaseGuidancePresenter.pause();
        ArgumentCaptor<NavigationManager.ManeuverEventListener> captor = ArgumentCaptor.forClass(
                NavigationManager.ManeuverEventListener.class);
        verify(mNavigationManager).removeManeuverEventListener(captor.capture());
        assertNotNull(captor.getValue());
    }

    @Test
    public void testNewInstructionEvent() {
        mBaseGuidancePresenter.resume();
        verify(mNavigationManager).addNewInstructionEventListener(mNewInstructionCaptor.capture());
        assertNotNull(mNewInstructionCaptor.getValue().get());
        mBaseGuidancePresenter.pause();
        ArgumentCaptor<NavigationManager.NewInstructionEventListener> captor = ArgumentCaptor.forClass(
                NavigationManager.NewInstructionEventListener.class);
        verify(mNavigationManager).removeNewInstructionEventListener(captor.capture());
        assertNotNull(captor.getValue());
    }

    @Test
    public void testPositionListenerEvent() {
        mBaseGuidancePresenter.resume();
        verify(mNavigationManager).addPositionListener(mPositionListenerCaptor.capture());
        assertNotNull(mPositionListenerCaptor.getValue().get());
        mBaseGuidancePresenter.pause();
        ArgumentCaptor<NavigationManager.PositionListener> captor = ArgumentCaptor.forClass(
                NavigationManager.PositionListener.class);
        verify(mNavigationManager).removePositionListener(captor.capture());
        assertNotNull(captor.getValue());
    }

    @Test
    public void testGetNextManeuver() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        assertNotNull(mBaseGuidancePresenter.getNextManeuver());
    }

    @Test
    public void testGetNextManeuverDistance() {
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(1L);
        assertThat(mBaseGuidancePresenter.getNextManeuverDistance(), is(1L));
    }

    @Test
    public void testRoute() {
        assertNotNull(mBaseGuidancePresenter.getRoute());
        mBaseGuidancePresenter.setRoute(null);
        assertNull(mBaseGuidancePresenter.getRoute());
    }

    @Test
    public void testGetEta() {
        mBaseGuidancePresenter.getEta();
        verify(mNavigationManager).getEta(false, Route.TrafficPenaltyMode.OPTIMAL);

        when(mNavigationManager.getEta(false, Route.TrafficPenaltyMode.OPTIMAL))
                .thenReturn(NavigationManager.INVALID_ETA_DATE);
        assertNotNull(mBaseGuidancePresenter.getEta());
    }

    @Test
    public void testGetDestinationDistance() {
        mBaseGuidancePresenter.getDestinationDistance();
        verify(mNavigationManager).getDestinationDistance();
    }

    @Test
    public void testGetTimeToArrival() {
        RouteTta mockRouteTta = mock(RouteTta.class);

        when(mNavigationManager.getTta(Route.TrafficPenaltyMode.OPTIMAL, false)).thenReturn(null);
        assertNull(mBaseGuidancePresenter.getTimeToArrival());


        when(mNavigationManager.getTta(Route.TrafficPenaltyMode.OPTIMAL, false)).thenReturn(mockRouteTta);
        mBaseGuidancePresenter.getTimeToArrival();
        verify(mockRouteTta).getDuration();
    }
}