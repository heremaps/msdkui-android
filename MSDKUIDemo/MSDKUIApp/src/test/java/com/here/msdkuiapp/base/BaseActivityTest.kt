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

package com.here.msdkuiapp.base

import android.app.FragmentManager
import com.here.testutils.BaseTest
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.Robolectric


/**
 * Tests for [BaseActivity].
 */
class BaseActivityTest : BaseTest() {

    class BaseActivityImpl : BaseActivity() {}
    class BaseCoordinatorImpl(fragmentManager: FragmentManager) : BaseFragmentCoordinator(fragmentManager) {}

    private lateinit var activity: BaseActivityImpl

    @Before
    public override fun setUp() {
        activity = Robolectric.buildActivity(BaseActivityImpl::class.java).create().start().visible().get()
    }

    @Test
    public fun testAppActionBar() {
        assertNotNull(activity.appActionBar)
        // since there is no coordinator so coordinator should be null
        assertNull(activity.coordinator)
    }

    @Test
    public fun testBackBehaviourWhenNoCoodinator() {
        // check when coordinator is null
        activity.onBackPressed()
        assertTrue(activity.isFinishing)
    }


    @Test
    public fun testBackBehaviourWhenCoodinator() {
        val coordinatorImpl = mock(BaseCoordinatorImpl::class.java)
        // check when coordinator is not null
        activity.coordinator = coordinatorImpl
        activity.onBackPressed()
        verify(coordinatorImpl).onBackPressed()
    }

}