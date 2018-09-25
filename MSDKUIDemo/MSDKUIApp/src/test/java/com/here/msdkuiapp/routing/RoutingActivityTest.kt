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

package com.here.msdkuiapp.routing

import com.here.msdkuiapp.base.BaseFragmentCoordinator
import com.here.testutils.BaseTest
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

/**
 * Tests for [RoutingActivity].
 */
class RoutingActivityTest : BaseTest() {

    private lateinit var activityController: ActivityController<RoutingActivity>

    @Before
    override fun setUp() {
        activityController = Robolectric.buildActivity(RoutingActivity::class.java)
    }

    @Test
    fun testOnCreate() {
        val activity = activityController.create().start().get()
        assertNotNull(activity.coordinator as BaseFragmentCoordinator)
    }

    @Test
    fun testOnCreateCheckRouteCoordinatorStart() {
        val mockRoutingCoordinator = mock(RoutingCoordinator::class.java, RETURNS_DEEP_STUBS)
        val activity = activityController.get()
        activity.routingCoordinator = mockRoutingCoordinator
        activityController.create().start()
        verify(mockRoutingCoordinator).start()
    }
}