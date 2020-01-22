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

package com.here.msdkuiapp.guidance

import com.here.msdkuiapp.base.BaseFragmentCoordinator
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import com.here.msdkuiapp.position.AppPositioningManager
import com.here.testutils.BaseTest
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

/**
 * Tests for [GuidanceRouteSelectionActivity].
 */
class GuidanceRouteSelectionActivityTest : BaseTest() {

    private lateinit var activityController: ActivityController<GuidanceRouteSelectionActivity>

    @Mock
    private lateinit var mockCoordinator: GuidanceRouteSelectionCoordinator

    @Before
    override fun setUp() {
        MockitoAnnotations.initMocks(this)
        activityController = Robolectric.buildActivity(GuidanceRouteSelectionActivity::class.java)
    }

    @Test
    fun testUI() {
        val activity = activityController.get()
        with(activity) {
            coordinator = mockCoordinator
            assertNotNull(coordinator)
            activityController.create()
        }
        verify(mockCoordinator).start()

        activity.onLocationReady()
        verify(mockCoordinator).onLocationReady()
    }

    @Test
    fun testOnCreateWithNotInitializedCoordinator() {
        val activity = activityController.get()
        activityController.create()
        assertNotNull(activity.coordinator)
    }

    @Test(expected = ClassCastException::class)
    fun testSettingDifferentTypeOfCoordinator() {
        activityController.get().coordinator = Mockito.mock(BaseFragmentCoordinator::class.java)
    }

    @Test
    fun testBackPressWhenCoordinator() {
        Mockito.`when`(mockCoordinator.onBackPressed()).thenReturn(false)
        SingletonHelper.appPositioningManager = mock(AppPositioningManager::class.java)
        val activity = activityController.get()
        with(activity) {
            coordinator = mockCoordinator
            onBackPressed()
        }
        Mockito.verify(mockCoordinator).onBackPressed()
        Assert.assertTrue(activity.isFinishing)
    }

    @Test
    fun testBackPressWhenNoCoordinator() {
        Mockito.`when`(mockCoordinator.onBackPressed()).thenReturn(false)
        val activity = activityController.get()
        activity.onBackPressed()
        Assert.assertTrue(activity.isFinishing)
    }
}