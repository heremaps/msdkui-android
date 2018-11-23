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

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.common.GeoBoundingBox
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.routing.Route
import com.here.msdkuiapp.common.routepreview.RoutePreviewFragment
import com.here.msdkuiapp.map.MapFragmentWrapper
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Tests for [RouteManeuverListFragment].
 */
class RouteManeuverListFragmentTest : BaseTest() {

    @Mock
    private lateinit var mockMapFragment: MapFragmentWrapper

    @Mock
    private lateinit var mockFragmentManager: FragmentManager

    @Mock
    private lateinit var mockFragmentTransaction: FragmentTransaction

    @Mock
    private lateinit var mockRoute: Route

    private lateinit var fragment: RouteManeuverListFragment

    @Before
    override fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(mockFragmentManager.beginTransaction()).thenReturn(mockFragmentTransaction)
        fragment = spy(RouteManeuverListFragment.newInstance()).apply {
            mapFragment = mockMapFragment
            Mockito.`when`(mockRoute.destination).thenReturn(mock(GeoCoordinate::class.java))
            setRoute(mockRoute, false)
        }
    }

    @Test
    fun testCreateView() {
        val inflater = mock(LayoutInflater::class.java)
        val mockView = mock(View::class.java)
        Mockito.`when`(inflater.inflate(Mockito.anyInt(), ArgumentMatchers.any(), Mockito.anyBoolean())).thenReturn(mockView)
        fragment.onCreateView(inflater, mock(ViewGroup::class.java), null)
        verify(inflater).inflate(Mockito.anyInt(), ArgumentMatchers.any(), ArgumentMatchers.anyBoolean())
    }

    @Test
    fun testViewCreated() {
        fragment.run {
            manager = mockFragmentManager
            onViewCreated(mock(View::class.java), null)
        }
        verify(mockMapFragment).start(anySafe())
        val fragmentCapture = argumentCaptor<Fragment>()
        verify(mockFragmentTransaction).replace(ArgumentMatchers.anyInt(), fragmentCapture.capture(), Mockito.anyString())
        assertNotNull(fragmentCapture.value as? RoutePreviewFragment)
    }

    @Test
    fun testRouteRendering() {
        `when`(mockRoute.boundingBox).thenReturn(mock(GeoBoundingBox::class.java))
        fragment.renderRoute(mockRoute)
        verify(mockMapFragment).
                renderAndZoomTo(anySafe(), ArgumentMatchers.eq(true))
    }
}