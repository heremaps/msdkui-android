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

package com.here.msdkuiapp.routing

import com.here.testutils.BaseTest
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * Test class for [RoutingInstructionsFragment].
 */
class RoutingInstructionsFragmentTest : BaseTest() {
    lateinit var routingInstructionsFragment: RoutingInstructionsFragment

    @Before
    override fun setUp() {
        super.setUp()
        routingInstructionsFragment = RoutingInstructionsFragment.newInstance()
    }

    @Test
    fun testRoutingInstructionsFragmentCreation() {
        addFrag(routingInstructionsFragment,
                RoutingInstructionsFragment::class.java.name)
        assertNotNull(routingInstructionsFragment)
        assertNotNull(routingInstructionsFragment.view)
    }
}