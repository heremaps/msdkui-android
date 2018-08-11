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

package com.here.msdkui.guidance;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.Signpost;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Tests for {@link GuidanceManeuverPanelPresenter}.
 */
@PrepareForTest({ NavigationManager.class, Signpost.class })
public class GuidanceManeuverPanelPresenterTest extends RobolectricTest {

    private GuidanceManeuverPanelPresenter mManeuverPanelPresenter;
    private NavigationManager mNavigationManager;

    @Before
    public void setUp() {
        mNavigationManager = mock(NavigationManager.class);
        mManeuverPanelPresenter = new GuidanceManeuverPanelPresenter(getApplicationContext(), mNavigationManager,
                mock(Route.class));
    }

    @Test
    public void testHandleManeuverEvent() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        GuidanceManeuverPanelListener listener = mock(GuidanceManeuverPanelListener.class);
        mManeuverPanelPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mManeuverPanelPresenter.handleManeuverEvent();
        verify(listener).onDataChanged(any());
    }

    @Test
    public void testHandleManeuverEventForDestination() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        when(maneuver.getAction()).thenReturn(Maneuver.Action.END);
        GuidanceManeuverPanelListener listener = mock(GuidanceManeuverPanelListener.class);
        mManeuverPanelPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mManeuverPanelPresenter.handleManeuverEvent();
        verify(listener).onDataChanged(any());
        verify(listener).onDestinationReached();
    }

    @Test
    public void testWithSignPost() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        Signpost signpost = mock(Signpost.class);
        when(signpost.getExitText()).thenReturn("52");
        when(signpost.getExitNumber()).thenReturn("52");
        when(maneuver.getSignpost()).thenReturn(signpost);
        GuidanceManeuverPanelListener listener = mock(GuidanceManeuverPanelListener.class);
        mManeuverPanelPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mManeuverPanelPresenter.handleManeuverEvent();
        verify(listener).onDataChanged(any());

    }

    @Test
    public void testAddRemoveListener() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        GuidanceManeuverPanelListener listener = mock(GuidanceManeuverPanelListener.class);
        mManeuverPanelPresenter.addListener(listener);
        mManeuverPanelPresenter.removeListener(listener);
        mManeuverPanelPresenter.handleManeuverEvent();
        verify(listener, never()).onDataChanged(any());
    }
}
