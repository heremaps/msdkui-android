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

import com.here.RobolectricTest;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.RouteTta;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link GuidanceEstimatedArrivalViewPresenter}.
 */
public class GuidanceEstimatedArrivalViewPresenterTest extends RobolectricTest {

    private GuidanceEstimatedArrivalViewPresenter mEstimatedArrivalPresenter;
    private NavigationManager mNavigationManager;

    @Before
    public void setUp() {
        mNavigationManager = mock(NavigationManager.class);
        mEstimatedArrivalPresenter = new GuidanceEstimatedArrivalViewPresenter(mNavigationManager);
    }

    @Test
    public void testHandlePositionUpdate() {
        final GuidanceEstimatedArrivalViewListener listener = mock(GuidanceEstimatedArrivalViewListener.class);
        mEstimatedArrivalPresenter.addListener(listener);

        prepareNavigationManagerForCorrectHandlePositionUpdate();
        mEstimatedArrivalPresenter.handlePositionUpdate();

        verify(listener).onDataChanged(any());
    }

    @Test
    public void testAddRemoveListener() {
        final GuidanceEstimatedArrivalViewListener listener = mock(GuidanceEstimatedArrivalViewListener.class);
        mEstimatedArrivalPresenter.addListener(listener);

        mEstimatedArrivalPresenter.removeListener(listener);

        prepareNavigationManagerForCorrectHandlePositionUpdate();
        mEstimatedArrivalPresenter.handlePositionUpdate();

        verify(listener, never()).onDataChanged(any());
    }

    private void prepareNavigationManagerForCorrectHandlePositionUpdate() {
        RouteTta mockRouteTta = mock(RouteTta.class);
        Date mockDate = mock(Date.class);
        when(mNavigationManager.getEta(anyBoolean(), any())).thenReturn(mockDate);
        when(mNavigationManager.getDestinationDistance()).thenReturn(200L);
        when(mNavigationManager.getTta(any(), anyBoolean())).thenReturn(mockRouteTta);
        when(mockRouteTta.getDuration()).thenReturn(100);
    }
}
