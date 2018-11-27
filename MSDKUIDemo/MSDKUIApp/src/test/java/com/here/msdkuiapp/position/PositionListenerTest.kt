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

package com.here.msdkuiapp.position

import com.here.android.mpa.common.GeoPosition
import com.here.android.mpa.common.PositioningManager
import com.here.msdkuiapp.guidance.SingletonHelper
import com.here.testutils.anySafe
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Tests for [PositionListener].
 */
class PositionListenerTest {

    private lateinit var positionListener: PositionListener

    @Mock
    private lateinit var listenerMock : PositionListener.Listener

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockPositioningManager: PositioningManager

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockGeoPosition: GeoPosition

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        positionListener = PositionListener()
        SingletonHelper.positioningManager = mockPositioningManager
    }

    @Test
    fun testSetAndGetListener() {
        `when`(mockPositioningManager.hasValidPosition()).thenReturn(true)
        positionListener.listener = listenerMock
        Assert.assertEquals(positionListener.listener, listenerMock)
        verify(listenerMock).onPositionAvailable()

        `when`(mockPositioningManager.hasValidPosition()).thenReturn(false)
        positionListener.listener = listenerMock
        verify(mockPositioningManager).addListener(anySafe())
    }

    @Test
    fun testOnPositionUpdated() {
        `when`(mockPositioningManager.hasValidPosition()).thenReturn(false)

        `when`(mockGeoPosition.isValid).thenReturn(false)
        positionListener.listener = listenerMock
        positionListener.onPositionUpdated(PositioningManager.LocationMethod.GPS, mockGeoPosition, false)

        `when`(mockGeoPosition.isValid).thenReturn(true)
        positionListener.onPositionUpdated(PositioningManager.LocationMethod.GPS, mockGeoPosition, false)

        `when`(mockGeoPosition.isValid).thenReturn(true)
        `when`(mockPositioningManager.hasValidPosition()).thenReturn(true)
        positionListener.onPositionUpdated(PositioningManager.LocationMethod.GPS, mockGeoPosition, false)

        verify(listenerMock).onPositionAvailable()
        verify(mockPositioningManager).removeListener(anySafe())
    }
}