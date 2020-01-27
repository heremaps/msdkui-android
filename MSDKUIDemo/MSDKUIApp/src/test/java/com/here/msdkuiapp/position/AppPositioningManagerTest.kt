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

package com.here.msdkuiapp.position

import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.GeoPosition
import com.here.android.mpa.common.PositioningManager
import com.here.testutils.anySafe
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class AppPositioningManagerTest {

    private lateinit var positioningManagerInstance: AppPositioningManager

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var sdkPositioningManagerMock: PositioningManager

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        positioningManagerInstance = AppPositioningManager.getInstance()
        assertNull(positioningManagerInstance.sdkPositioningManager)
        positioningManagerInstance.sdkPositioningManager = sdkPositioningManagerMock
    }

    @After
    fun tearDown() {
        positioningManagerInstance.sdkPositioningManager = null
    }

    @Test
    fun testInstance() {
        assertNotNull(positioningManagerInstance)
        assertThat(AppPositioningManager.getInstance(), `is`(AppPositioningManager.getInstance()))
    }

    @Test
    fun testValidPosition() {
        positioningManagerInstance.customLocation = mock(GeoCoordinate::class.java)
        assertTrue(positioningManagerInstance.isValidPosition)
    }

    @Test
    fun testIsActive() {
        `when`(sdkPositioningManagerMock.isActive).thenReturn(true)
         assertTrue(positioningManagerInstance.isActive)
        positioningManagerInstance.sdkPositioningManager = null
        assertFalse(positioningManagerInstance.isActive)
    }

    @Test
    fun testListeners() {
        val mockListener = mock(AppPositioningManager.Listener::class.java)

        // check if current location is null
        `when`(sdkPositioningManagerMock.hasValidPosition()).thenReturn(false)
        positioningManagerInstance.initPositioning(mockListener)
        verify(sdkPositioningManagerMock).addListener(anySafe())

        `when`(sdkPositioningManagerMock.hasValidPosition()).thenReturn(true)
        positioningManagerInstance.initPositioning(mockListener)
        verify(mockListener).onPositionAvailable()

        positioningManagerInstance.customLocation = mock(GeoCoordinate::class.java)
        positioningManagerInstance.initPositioning(mockListener)
        verify(mockListener, atLeastOnce()).onPositionAvailable()

        assertNotNull(positioningManagerInstance.listener)
    }

    @Test
    fun testInitPositioning() {
        val mockListener = mock(AppPositioningManager.Listener::class.java)
        `when`(sdkPositioningManagerMock.isActive).thenReturn(false)
        positioningManagerInstance.initPositioning(mockListener)
        verify(sdkPositioningManagerMock).start(anySafe())
    }

    @Test
    fun testStop() {
        positioningManagerInstance.customLocation = mock(GeoCoordinate::class.java)
        positioningManagerInstance.stop()
        verify(sdkPositioningManagerMock).stop()
        assertNull(positioningManagerInstance.customLocation)
    }

    @Test
    fun onPositionUpdated() {
        `when`(sdkPositioningManagerMock.hasValidPosition()).thenReturn(true)
        val mockGeoPosition = mock(GeoPosition::class.java, RETURNS_DEEP_STUBS)
        `when`(mockGeoPosition.isValid).thenReturn(true)
        positioningManagerInstance.onPositionUpdated(null, mockGeoPosition, false)
        verify(sdkPositioningManagerMock).removeListener(anySafe())
    }
}