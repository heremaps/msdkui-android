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

import com.here.testutils.BaseTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.robolectric.Robolectric

/**
 * Tests for [RetainFragment].
 */
class RetainFragmentTest : BaseTest() {

    class RetainFragmentImpl : RetainFragment() {
        var value: Int? = null
    }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun testRetainStatus() {
        val fr = RetainFragmentImpl()
        fr.value = 1
        addFrag(fr)
        fragmentActivity!!.recreate()
        assertThat(fr.value, `is`(1))
    }
}