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

package com.here.msdkuiapp.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.os.Bundle
import com.here.msdkuiapp.common.routepreview.RoutePreviewFragment
import com.here.msdkuiapp.guidance.GuidanceWaypointSelectionFragment
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.Assert.assertNotNull
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Tests for [BaseFragmentCoordinator].
 */
class BaseFragmentCoordinatorTest : BaseTest() {

    /**
     * Implementation of BaseFragmentCoordinator to test.
     */
    class BaseFragmentCoordinatorImpl(fragmentManager: FragmentManager) :
            BaseFragmentCoordinator(fragmentManager) {
    }

    @Mock
    private lateinit var mockFragmentManager: FragmentManager

    @Mock
    private lateinit var mockFragment: Fragment

    @Mock
    private lateinit var fragmentTransaction: FragmentTransaction

    private lateinit var baseFragmentCoordinator: BaseFragmentCoordinator

    @Before
    override fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(mockFragmentManager.beginTransaction()).thenReturn(fragmentTransaction)
        `when`(mockFragmentManager.findFragmentById(anyInt())).thenReturn(mockFragment)
        baseFragmentCoordinator = BaseFragmentCoordinatorImpl(mockFragmentManager)
        assertNotNull(baseFragmentCoordinator.fragmentManager)
    }

    @Test
    fun testAddFragmentShouldAddFragment() {
        baseFragmentCoordinator.addFragment(1, mockFragment, true)
        verify(fragmentTransaction).replace(anyInt(), anySafe(), anySafe())
        verify(fragmentTransaction).addToBackStack(anyString())
    }

    @Test
    fun testAddFragmentClassShouldAddFragment() {
        // lets say at same id, some other fragment was there
        val routeSelectionFragment = mock(GuidanceWaypointSelectionFragment::class.java)
        `when`(mockFragmentManager.findFragmentById(anyInt())).thenReturn(routeSelectionFragment)
        baseFragmentCoordinator.addFragment(1, RoutePreviewFragment::class.java, true)
        verify(fragmentTransaction).replace(anyInt(), anySafe(), anySafe())
        verify(fragmentTransaction).addToBackStack(anyString())
    }

    @Test
    fun testAddFragmentClassWithBundle() {
        val bundle = Bundle()
        bundle.putInt("key", 1)
        `when`(mockFragmentManager.findFragmentById(anyInt())).thenReturn(null)
        val fr = baseFragmentCoordinator.addFragment(1, RoutePreviewFragment::class.java, true, bundle)
        assertNotNull(fr.arguments)
        assertThat(fr.arguments!!.getInt("key"), `is`(1))
    }

    @Test
    fun testRemoveFragmentShouldRemoveFragment() {
        baseFragmentCoordinator.removeFragment(1)
        verify(fragmentTransaction).remove(anySafe())
    }

    @Test
    fun testFragmentVisibilitySettings() {
        baseFragmentCoordinator.setVisible(1, true)
        verify(fragmentTransaction).show(anySafe())

        baseFragmentCoordinator.setVisible(1, false)
        verify(fragmentTransaction).hide(anySafe())
    }

    @Test
    fun testGetFragment() {
        assertNotNull(baseFragmentCoordinator.getFragment(1))
    }
}