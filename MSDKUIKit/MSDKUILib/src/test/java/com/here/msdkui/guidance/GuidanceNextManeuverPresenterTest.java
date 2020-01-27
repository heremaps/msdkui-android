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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for  {@link GuidanceNextManeuverPresenter}.
 */
public class GuidanceNextManeuverPresenterTest extends RobolectricTest {

    private GuidanceNextManeuverPresenter mNextManeuverPresenter;
    private NavigationManager mNavigationManager;

    @Before
    public void setUp() {
        mNavigationManager = mock(NavigationManager.class);
        mNextManeuverPresenter = new GuidanceNextManeuverPresenter(getApplicationContext(), mNavigationManager,
                mock(Route.class));
    }

    @Test
    public void testHandleManeuverEvent() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        GuidanceNextManeuverListener listener = mock(GuidanceNextManeuverListener.class);
        mNextManeuverPresenter.addListener(listener);
        when(mNavigationManager.getAfterNextManeuver()).thenReturn(maneuver);
        mNextManeuverPresenter.handleManeuverEvent();
        verify(listener).onDataChanged(any());
    }

    @Test
    public void testAddRemoveListener() {
        GuidanceNextManeuverListener listener = mock(GuidanceNextManeuverListener.class);
        mNextManeuverPresenter.addListener(listener);
        mNextManeuverPresenter.removeListener(listener);
        mNextManeuverPresenter.handleManeuverEvent();
        verify(listener, never()).onDataChanged(any());
    }
}
