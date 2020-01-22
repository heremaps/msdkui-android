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

package com.here.msdkui.guidance;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Test for {@link GuidanceStreetLabelPresenter} class.
 */
public class GuidanceStreetLabelPresenterTest extends RobolectricTest {

    private GuidanceStreetLabelPresenter mGuidanceStreetLabelPresenter;
    private NavigationManager mNavigationManager;

    @Before
    public void setUp() {
        mNavigationManager = mock(NavigationManager.class);
        mGuidanceStreetLabelPresenter = new GuidanceStreetLabelPresenter(getApplicationContext(),
                mNavigationManager, mock(Route.class));
    }

    @Test
    public void testHandleNewInstructionEvent() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        GuidanceStreetLabelListener listener = mock(GuidanceStreetLabelListener.class);
        mGuidanceStreetLabelPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mGuidanceStreetLabelPresenter.handleNewInstructionEvent();
        verify(listener, times(2)).onDataChanged(any());
    }

    @Test
    public void testHandleManeuverEvent() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        GuidanceStreetLabelListener listener = mock(GuidanceStreetLabelListener.class);
        mGuidanceStreetLabelPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mGuidanceStreetLabelPresenter.handleManeuverEvent();
        verify(listener, times(2)).onDataChanged(any());
    }

    @Test
    public void testHandleGpsLost() {
        GuidanceStreetLabelListener listener = mock(GuidanceStreetLabelListener.class);
        mGuidanceStreetLabelPresenter.addListener(listener);
        mGuidanceStreetLabelPresenter.handleGpsLost();
        verify(listener, times(2)).onDataChanged(any());
    }

    @Test
    public void testHandleGpsRestore() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        GuidanceStreetLabelListener listener = mock(GuidanceStreetLabelListener.class);
        mGuidanceStreetLabelPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mGuidanceStreetLabelPresenter.handleGpsRestore();
        verify(listener, times(2)).onDataChanged(any());
    }

    @Test
    public void testRemoveListener() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        GuidanceStreetLabelListener listener = mock(GuidanceStreetLabelListener.class);
        mGuidanceStreetLabelPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mGuidanceStreetLabelPresenter.handleManeuverEvent();
        verify(listener, times(2)).onDataChanged(any());

        mGuidanceStreetLabelPresenter.removeListener(listener);
        mGuidanceStreetLabelPresenter.handleManeuverEvent();
        verifyNoMoreInteractions(listener);
    }
}
