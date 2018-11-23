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

import com.here.RobolectricTest;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.guidance.NavigationManager;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Tests for class {@link GuidanceSpeedPresenter}.
 */
@PrepareForTest({ NavigationManager.class, PositioningManager.class, GeoPosition.class })
public class GuidanceSpeedPresenterTest extends RobolectricTest {

    private GuidanceSpeedPresenter mCurrentSpeedPanelPresenter;
    private NavigationManager mNavigationManager;
    private PositioningManager mPositioningManager;

    @Before
    public void setUp() {
        mNavigationManager = mock(NavigationManager.class);
        mPositioningManager = mock(PositioningManager.class);
        mCurrentSpeedPanelPresenter = new GuidanceSpeedPresenter(mNavigationManager, mPositioningManager);
    }

    @Test
    public void testHandlePositionUpdate() {
        GuidanceSpeedListener listener = mock(GuidanceSpeedListener.class);
        mCurrentSpeedPanelPresenter.addListener(listener);

        //for incorrect data
        when(mPositioningManager.hasValidPosition()).thenReturn(false);
        mCurrentSpeedPanelPresenter.handlePositionUpdate();
        verify(listener).onDataChanged(any());

        when(mPositioningManager.hasValidPosition()).thenReturn(true);
        GeoPosition geoPos = mock(GeoPosition.class);
        when(geoPos.isValid()).thenReturn(true);
        when(geoPos.getSpeed()).thenReturn(10.0);
        when(mPositioningManager.getPosition()).thenReturn(geoPos);
        mCurrentSpeedPanelPresenter.handlePositionUpdate();
        verify(listener, times(2)).onDataChanged(any());
    }

    @Test
    public void testAddRemoveListener() {
        GuidanceSpeedListener listener = mock(GuidanceSpeedListener.class);
        mCurrentSpeedPanelPresenter.addListener(listener);

        when(mNavigationManager.getAverageSpeed()).thenReturn(10.0);
        mCurrentSpeedPanelPresenter.handlePositionUpdate();
        verify(listener).onDataChanged(any());

        mCurrentSpeedPanelPresenter.removeListener(listener);
        mCurrentSpeedPanelPresenter.handlePositionUpdate();
        verify(listener, times(1)).onDataChanged(any());
    }

    @Test
    public void testHandleSpeedExceeded() {
        GuidanceSpeedListener listener = mock(GuidanceSpeedListener.class);
        mCurrentSpeedPanelPresenter.addListener(listener);
        when(mPositioningManager.hasValidPosition()).thenReturn(false);
        mCurrentSpeedPanelPresenter.handleSpeedExceeded(10);
        verify(listener).onDataChanged(any());
    }

    @Test
    public void testHandleSpeedExceededEnd() {
        GuidanceSpeedListener listener = mock(GuidanceSpeedListener.class);
        mCurrentSpeedPanelPresenter.addListener(listener);
        when(mPositioningManager.hasValidPosition()).thenReturn(false);
        mCurrentSpeedPanelPresenter.handleSpeedExceededEnd(10);
        verify(listener).onDataChanged(any());
    }

    @Test
    public void testResumePauseBehaviour() {
        mCurrentSpeedPanelPresenter.resume();
        verify(mNavigationManager).addSpeedWarningListener(any());
        mCurrentSpeedPanelPresenter.pause();
        verify(mNavigationManager).removeSpeedWarningListener(any());
    }
}
