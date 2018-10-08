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

package com.here.msdkuiapp.common

import android.app.Activity
import android.os.Build
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.robolectric.util.ReflectionHelpers

/**
 * Tests for [PermissionsUtils]
 */
class PermissionUtilsTest {

    @Mock
    private lateinit var mockContext: Activity

    @Mock
    private lateinit var mockPositioningStateListener: PermissionsUtils.PositioningStateListener

    @Mock
    private lateinit var mockStorageStateListener: PermissionsUtils.StorageStateListener

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testLocationPermission() {
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 19)
        PermissionsUtils.checkLocationPermission(mockContext, mockPositioningStateListener)
        verify(mockPositioningStateListener).onLocationPermissionOK()

        Mockito.`when`(mockContext.checkPermission(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt())).thenReturn(0)
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 23)
        PermissionsUtils.checkLocationPermission(mockContext, mockPositioningStateListener)
        verify(mockPositioningStateListener, times(2)).onLocationPermissionOK()
    }

    @Test
    fun testStoragePermission() {
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 19)
        PermissionsUtils.checkStoragePermission(mockContext, mockStorageStateListener)
        verify(mockStorageStateListener).onStoragePermissionOK()
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 23)
        Mockito.`when`(mockContext.checkPermission(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt())).thenReturn(1)
        PermissionsUtils.checkStoragePermission(mockContext, mockStorageStateListener)
        Mockito.`when`(mockContext.checkPermission(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt())).thenReturn(0)
        PermissionsUtils.checkStoragePermission(mockContext, mockStorageStateListener)
        verify(mockStorageStateListener, atLeastOnce()).onStoragePermissionOK()
    }

    @Test
    fun testOnRequestPermissionsResult() {
        PermissionsUtils.onRequestPermissionsResult(PermissionRequestCode.STORAGE_REQ_CODE, intArrayOf(),
                mockStorageStateListener)
        verify(mockStorageStateListener).onStoragePermissionFailed()

        PermissionsUtils.onRequestPermissionsResult(PermissionRequestCode.STORAGE_REQ_CODE, intArrayOf(0),
                mockStorageStateListener)
        verify(mockStorageStateListener).onStoragePermissionOK()

        PermissionsUtils.onRequestPermissionsResult(PermissionRequestCode.POSITIONING_REQ_CODE, intArrayOf(),
                mockPositioningStateListener)
        verify(mockPositioningStateListener).onLocationPermissionFailed()

        PermissionsUtils.onRequestPermissionsResult(PermissionRequestCode.POSITIONING_REQ_CODE, intArrayOf(0),
                mockPositioningStateListener)
        verify(mockPositioningStateListener).onLocationPermissionOK()
    }
}