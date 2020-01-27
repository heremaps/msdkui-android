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
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.guidance.NavigationManager;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for class {@link GuidanceSpeedPresenter}.
 */
public class GuidanceSpeedPresenterTest extends RobolectricTest {

    private GuidanceSpeedPresenter mCurrentSpeedPresenter;
    private NavigationManager mNavigationManager;
    private PositioningManager mPositioningManager;

    @Before
    public void setUp() {
        mNavigationManager = mock(NavigationManager.class);
        mPositioningManager = mock(PositioningManager.class);
        mCurrentSpeedPresenter = new GuidanceSpeedPresenter(mNavigationManager, mPositioningManager);
    }

    @Test
    public void testHandlePositionUpdate() {
        GuidanceSpeedListener listener = mock(GuidanceSpeedListener.class);
        mCurrentSpeedPresenter.addListener(listener);

        //for incorrect data
        when(mPositioningManager.hasValidPosition()).thenReturn(false);
        mCurrentSpeedPresenter.handlePositionUpdate();
        verify(listener).onDataChanged(any());

        when(mPositioningManager.hasValidPosition()).thenReturn(true);
        GeoPosition geoPos = mock(GeoPosition.class);
        when(geoPos.isValid()).thenReturn(true);
        when(geoPos.getSpeed()).thenReturn(10.0);
        when(mPositioningManager.getPosition()).thenReturn(geoPos);
        mCurrentSpeedPresenter.handlePositionUpdate();
        verify(listener, times(2)).onDataChanged(any());
    }

    @Test
    public void testAddRemoveListener() {
        GuidanceSpeedListener listener = mock(GuidanceSpeedListener.class);
        mCurrentSpeedPresenter.addListener(listener);

        when(mNavigationManager.getAverageSpeed()).thenReturn(10.0);
        mCurrentSpeedPresenter.handlePositionUpdate();
        verify(listener).onDataChanged(any());

        mCurrentSpeedPresenter.removeListener(listener);
        mCurrentSpeedPresenter.handlePositionUpdate();
        verify(listener, times(1)).onDataChanged(any());
    }

    @Test
    public void testHandleSpeedExceeded() {
        GuidanceSpeedListener listener = mock(GuidanceSpeedListener.class);
        mCurrentSpeedPresenter.addListener(listener);
        when(mPositioningManager.hasValidPosition()).thenReturn(false);
        mCurrentSpeedPresenter.handleSpeedExceeded(10);
        verify(listener).onDataChanged(any());
    }

    @Test
    public void testHandleSpeedExceededEnd() {
        GuidanceSpeedListener listener = mock(GuidanceSpeedListener.class);
        mCurrentSpeedPresenter.addListener(listener);
        when(mPositioningManager.hasValidPosition()).thenReturn(false);
        mCurrentSpeedPresenter.handleSpeedExceededEnd(10);
        verify(listener).onDataChanged(any());
    }

    @Test
    public void testResumePauseBehaviour() {
        mCurrentSpeedPresenter.resume();
        verify(mNavigationManager).addSpeedWarningListener(any());
        mCurrentSpeedPresenter.pause();
        verify(mNavigationManager).removeSpeedWarningListener(any());
    }
}
