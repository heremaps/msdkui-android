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

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.here.msdkuiapp.common.PermissionRequestCode
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import org.mockito.ArgumentMatchers

/**
 * Tests for [LocationServiceUtils].
 */
class LocationServiceUtilsTest {

    @Mock
    private lateinit var locationServiceStateListenerMock: LocationServiceUtils.LocationServiceStateListener

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockContext: Context

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockActivityContext: Activity

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockLocationManager: LocationManager

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockClient: SettingsClient

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockTask: Task<LocationSettingsResponse>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testCheckLocationServicesWithProviderEnabled() {
        `when`(mockActivityContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(mockLocationManager)
        `when`(mockLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true)
        val client = LocationServices.getSettingsClient(mockContext)
        LocationServiceUtils.checkLocationServices(mockActivityContext, client, locationServiceStateListenerMock)
        verify(locationServiceStateListenerMock).onServiceOk()
    }

    @Test
    fun testCheckLocationServicesOnCompleteWithoutException() {
        testCheckLocationServicesBeginMock(false)
        LocationServiceUtils.checkLocationServices(mockActivityContext, mockClient, locationServiceStateListenerMock)
        val captor = argumentCaptor<OnCompleteListener<LocationSettingsResponse>>()
        verify(mockTask).addOnCompleteListener(captor.capture())
        captor.value.onComplete(mockTask)
        verify(locationServiceStateListenerMock).onServiceOk()
    }

    @Test
    fun testCheckLocationServicesOnCompleteWithExceptionHandling() {
        testCheckLocationServicesBeginMock(false)
        LocationServiceUtils.checkLocationServices(mockActivityContext, mockClient, locationServiceStateListenerMock)
        val captor = argumentCaptor<OnCompleteListener<LocationSettingsResponse>>()
        verify(mockTask).addOnCompleteListener(captor.capture())
        val apiException = mock(ResolvableApiException::class.java)
        `when`(mockTask.getResult(ApiException::class.java)).thenThrow(apiException)
        `when`(apiException.statusCode).thenReturn(LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
        captor.value.onComplete(mockTask)
        verify(apiException).startResolutionForResult(anySafe(), ArgumentMatchers.anyInt())
    }

    @Test
    fun testOnActivityResult() {
        LocationServiceUtils.onActivityResult(-1,
                Activity.RESULT_CANCELED, locationServiceStateListenerMock)

        LocationServiceUtils.onActivityResult(PermissionRequestCode.LOCATION_SERVICE_REQ_CODE,
                Activity.RESULT_CANCELED, null)
        LocationServiceUtils.onActivityResult(PermissionRequestCode.LOCATION_SERVICE_REQ_CODE,
                1, null)

        LocationServiceUtils.onActivityResult(PermissionRequestCode.LOCATION_SERVICE_REQ_CODE,
                Activity.RESULT_CANCELED, locationServiceStateListenerMock)
        verify(locationServiceStateListenerMock).onServiceFailed()
        LocationServiceUtils.onActivityResult(PermissionRequestCode.LOCATION_SERVICE_REQ_CODE,
                1, locationServiceStateListenerMock)
        verify(locationServiceStateListenerMock).onServiceOk()
    }

    private fun testCheckLocationServicesBeginMock(taskIsComplete: Boolean) {
        `when`(mockActivityContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(mockLocationManager)
        `when`(mockLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false)
        `when`(mockClient.checkLocationSettings(anySafe())).thenReturn(mockTask)
        `when`(mockTask.isComplete).thenReturn(taskIsComplete)
    }
}