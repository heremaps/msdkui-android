/*
 * Copyright (C) 2017-2021 HERE Europe B.V.
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

import android.os.Build
import android.view.View
import com.here.testutils.BaseTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.robolectric.util.ReflectionHelpers

/**
 * Tests for [IconButton].
 */
class IconButtonTest : BaseTest() {
    private lateinit var button: IconButton
    private var isClicked: Boolean = false

    @Before
    override fun setUp() {
        super.setUp()
        button = IconButton(applicationContext)
        isClicked = false
    }

    @Test
    fun testDefaultType() {
        assertTrue("Not the expected type .ADD!", button.type == IconButton.Type.ADD)
        assertNotNull("No image is set!", button.drawable)
    }

    @Test
    fun testCustomType() {
        button.type = IconButton.Type.OPTIONS
        assertTrue("Not the expected type .OPTIONS!", button.type == IconButton.Type.OPTIONS)
        assertNotNull("No image is set!", button.drawable);
    }

    @Test
    fun testCallback() {
        button.setOnClickListener { view: View? -> isClicked = true }
        button.performClick()
        assertTrue("The tap is not detected!", isClicked)
    }

    @Test
    fun testUpdateImageAPI19() {
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 19)
        button = IconButton(applicationContext)
        assertNotNull("No image is set!", button.drawable)
    }
}
