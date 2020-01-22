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

import android.graphics.Bitmap;

import com.here.MockUtils;
import com.here.RobolectricTest;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.Signpost;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link GuidanceManeuverPresenter}.
 */
public class GuidanceManeuverPresenterTest extends RobolectricTest {

    private GuidanceManeuverPresenter mGuidanceManeuverPresenter;
    private NavigationManager mNavigationManager;

    @Before
    public void setUp() {
        mNavigationManager = mock(NavigationManager.class);
        mGuidanceManeuverPresenter = new GuidanceManeuverPresenter(getApplicationContext(), mNavigationManager,
                mock(Route.class));
    }

    @Test
    public void testHandlePositionUpdate() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        GuidanceManeuverListener listener = mock(GuidanceManeuverListener.class);
        mGuidanceManeuverPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mGuidanceManeuverPresenter.handlePositionUpdate();
        verify(listener).onDataChanged(any());
    }

    @Test
    public void testHandleManeuverEvent() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        GuidanceManeuverListener listener = mock(GuidanceManeuverListener.class);
        mGuidanceManeuverPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mGuidanceManeuverPresenter.handleManeuverEvent();
        verify(listener).onDataChanged(any());
    }

    @Test
    public void testHandleManeuverEventForDestination() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        when(maneuver.getAction()).thenReturn(Maneuver.Action.END);
        GuidanceManeuverListener listener = mock(GuidanceManeuverListener.class);
        mGuidanceManeuverPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mGuidanceManeuverPresenter.handleManeuverEvent();
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
        GuidanceManeuverListener listener = mock(GuidanceManeuverListener.class);
        mGuidanceManeuverPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mGuidanceManeuverPresenter.handleManeuverEvent();
        verify(listener).onDataChanged(any());
    }

    @Test
    public void testWithRoadIcon() {
        final Maneuver maneuver = MockUtils.mockManeuver();
        Image mockImage = mock(Image.class);
        when(mockImage.getHeight()).thenReturn(10L);
        when(mockImage.getWidth()).thenReturn(10L);
        Bitmap mockBitmap = mock(Bitmap.class);
        when(mockImage.getBitmap(anyInt(), anyInt())).thenReturn(mockBitmap);
        when(maneuver.getNextRoadImage()).thenReturn(mockImage);
        GuidanceManeuverListener listener = mock(GuidanceManeuverListener.class);
        mGuidanceManeuverPresenter.addListener(listener);
        when(mNavigationManager.getNextManeuver()).thenReturn(maneuver);
        when(mNavigationManager.getNextManeuverDistance()).thenReturn(200L);
        mGuidanceManeuverPresenter.handleManeuverEvent();
        ArgumentCaptor<GuidanceManeuverData> dataArgumentCaptor = ArgumentCaptor.forClass(GuidanceManeuverData.class);
        verify(listener).onDataChanged(dataArgumentCaptor.capture());
        assertThat(dataArgumentCaptor.getValue().getNextRoadIcon(), is(mockBitmap));
    }

    @Test
    public void testAddRemoveListener() {
        final GuidanceManeuverListener listener = mock(GuidanceManeuverListener.class);
        mGuidanceManeuverPresenter.addListener(listener);
        mGuidanceManeuverPresenter.removeListener(listener);
        mGuidanceManeuverPresenter.handleManeuverEvent();
        verify(listener, never()).onDataChanged(any());
    }
}
