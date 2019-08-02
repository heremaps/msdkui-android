/*
 * Copyright (C) 2017-2019 HERE Europe B.V.
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

package com.here.msdkuiapp.map

import MockUtils.mockRoute
import android.content.Context
import android.graphics.PointF
import androidx.fragment.app.FragmentActivity
import android.view.LayoutInflater
import com.here.android.mpa.common.GeoBoundingBox
import com.here.android.mpa.common.Image
import com.here.android.mpa.common.MapEngine
import com.here.android.mpa.common.OnEngineInitListener
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.mapping.MapContainer
import com.here.android.mpa.mapping.MapGesture
import com.here.android.mpa.mapping.MapMarker
import com.here.android.mpa.mapping.MapRoute
import com.here.android.mpa.mapping.MapView
import com.here.android.mpa.mapping.OnMapRenderListener
import com.here.android.mpa.mapping.PositionIndicator
import com.here.msdkuiapp.common.Provider
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import com.here.testutils.argumentCaptor
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyFloat
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.logging.Handler

/**
 * Tests for [MapFragmentWrapper].
 */
class MapFragmentWrapperTest {

    private lateinit var fragment: MapFragmentWrapper

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockProvider: Provider

    @Mock
    private lateinit var mockMapView: MapView

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockMap: Map

    @Mock
    private lateinit var mockActivity: FragmentActivity

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        fragment = spy(MapFragmentWrapper()).apply {
            injectedProvider = mockProvider
        }
        `when`(fragment.activity).thenReturn(mockActivity)
        `when`(fragment.view).thenReturn(mockMapView)
    }

    @Test
    fun testCreation() {
        `when`(mockProvider.provideMapView(anySafe())).thenReturn(mockMapView)
        val layoutInflater = mock(LayoutInflater::class.java).apply {
            `when`(context).thenReturn(mock(Context::class.java))
        }
        val view = fragment.onCreateView(layoutInflater, null, null)
        assertThat(view!!.id, `is`(mockMapView.id))
    }

    @Test
    fun testDestroyView() {
        fragment.onDestroyView()
        verify(mockMapView).map = null
    }

    @Test
    fun onPause() {
        fragment.onPause()
        verify(mockMapView).onPause()
    }

    @Test
    fun onResume() {
        fragment.onResume()
        verify(mockMapView).onResume()
    }

    @Test
    fun testStartWithSuccess() {

        val mockMapEngine = mock(MapEngine::class.java)

        `when`(mockProvider.provideMapEngine()).thenReturn(mockMapEngine)
        `when`(mockProvider.provideMap()).thenReturn(mockMap)
        `when`(mockProvider.providesMapMarker()).thenReturn(mock(MapMarker::class.java))
        `when`(mockProvider.providesImage()).thenReturn(mock(Image::class.java))
        `when`(mockActivity.applicationContext).thenReturn(mock(Context::class.java))

        fragment.start{}
        val initCapture = argumentCaptor<OnEngineInitListener>()
        verify(mockMapEngine).init(anySafe(), initCapture.capture())
        // send success
        initCapture.value.onEngineInitializationCompleted(OnEngineInitListener.Error.NONE)
        assertNotNull(fragment.map)
        verify(mockMapView).map

        // test start with map
        `when`(mockMapView.map).thenReturn(mockMap)
        fragment.start {  }
        assertNotNull(fragment.map)
    }

    @Test
    fun testStartWithFail() {

        val mockMapEngine = mock(MapEngine::class.java)

        `when`(mockProvider.provideMapEngine()).thenReturn(mockMapEngine)
        `when`(mockProvider.provideMap()).thenReturn(mockMap)
        `when`(mockActivity.applicationContext).thenReturn(mock(Context::class.java))

        fragment.start{}
        val initCapture = argumentCaptor<OnEngineInitListener>()
        verify(mockMapEngine).init(anySafe(), initCapture.capture())
        val mockError = mock(OnEngineInitListener.Error.BUSY::class.java)
        `when`(mockError.details).thenReturn("")
        initCapture.value.onEngineInitializationCompleted(mockError)
        assertNull(fragment.map)
    }

    @Test
    fun testShowPositionIndicator() {
        val mockPositionIndicator = mock(PositionIndicator::class.java)
        fragment.map = mockMap
        `when`(mockMapView.positionIndicator).thenReturn(mockPositionIndicator)
        fragment.showPositionIndicator(true)
        verify(mockPositionIndicator).isVisible = eq(true)
        fragment.showPositionIndicator(false)
        verify(mockPositionIndicator).isVisible = eq(false)
    }

    @Test
    fun testRenderRoute() {
        val mapContainer = mock(MapContainer::class.java)
        fragment.map = mockMap
        `when`(mockProvider.providesImage()).thenReturn(mock(Image::class.java))
        `when`(mockProvider.providesMapContainer()).thenReturn(mapContainer)
        // init the map container calling with start
        fragment.start{}
        // see if container is added to map
        verify(mockMap).addMapObject(any())

        val mockMapRoute = mock(MapRoute::class.java)
        `when`(mockProvider.providesMapRoutes(anySafe())).thenReturn(mockMapRoute)
         fragment.renderRoute(mockRoute(), false)
         verify(mapContainer).addMapObject(anySafe())

        fragment.renderRoute(mockRoute(), true)
        verify(mapContainer, atLeastOnce()).addMapObject(anySafe())
    }

    @Test
    fun testRenderAndZoomTo() {
        val mapContainer = mock(MapContainer::class.java)
        fragment.map = mockMap
        `when`(mockProvider.providesImage()).thenReturn(mock(Image::class.java))
        `when`(mockProvider.providesMapContainer()).thenReturn(mapContainer)
        // init the map container calling with start
        fragment.start{}
        // see if container is added to map
        verify(mockMap).addMapObject(any())
        val mockMapRoute = mock(MapRoute::class.java)
        `when`(mockProvider.providesMapRoutes(anySafe())).thenReturn(mockMapRoute)

        fragment.renderAndZoomTo(mockRoute())
        verify(mapContainer, atLeastOnce()).addMapObject(anySafe())
    }


    @Test
    fun testClearMap() {
        // add marker first
        val mapContainer = mock(MapContainer::class.java)
        fragment.map = mockMap
        `when`(mockProvider.providesImage()).thenReturn(mock(Image::class.java))
        `when`(mockProvider.providesMapContainer()).thenReturn(mapContainer)
        // init the map container calling with start
        fragment.start{}
        // see if container is added to map
        verify(mockMap).addMapObject(any())

        val mockMapRoute = mock(MapRoute::class.java)
        `when`(mockProvider.providesMapRoutes(anySafe())).thenReturn(mockMapRoute)
        fragment.renderRoute(mockRoute(), false)
        verify(mapContainer).addMapObject(anySafe())
        verify(mockMap, atLeastOnce()).addMapObject(anySafe())

        //clear now
        fragment.clearMap()
        verify(mockMap).removeMapObject(anySafe())
        verify(mapContainer).removeMapObject(anySafe())
    }

    @Test
    fun testBackBtnBehaviour() {
        fragment.onBackPressed()
        verify(fragment).clearMap()
    }

    @Test
    fun testGestureBehaviour() {
        fragment.map = mockMap
        val mockGesture = mock(MapGesture::class.java)
        `when`(mockMapView.mapGesture).thenReturn(mockGesture)
        val mockListener = mock(MapFragmentWrapper.Listener::class.java)
        fragment.onTouch(true, mockListener)
        val captor = argumentCaptor<MapGesture.OnGestureListener>()
        verify(mockGesture).addOnGestureListener(captor.capture(), anyInt(), ArgumentMatchers.anyBoolean())
        captor.value.onLongPressEvent(mock(PointF::class.java))
        captor.value.onTapEvent(mock(PointF::class.java))
        fragment.onTouch(false)
        verify(mockGesture).removeOnGestureListener(anySafe())
    }

    @Test
    fun testTrafficBehaviour() {
        assertFalse(fragment.traffic)
        fragment.map = mockMap
        fragment.traffic
        verify(mockMap).isTrafficInfoVisible
        fragment.traffic = true
        verify(mockMap).isTrafficInfoVisible = eq(true)
    }

    @Test
    fun testMapRenderListener() {
        `when`(mockMap.width).thenReturn(200)
        `when`(mockMap.height).thenReturn(200)
        fragment.map = mockMap
        fragment.start{}
        fragment.renderAndZoomTo(mockRoute())
        val captor = argumentCaptor<OnMapRenderListener>()
        verify(mockMapView).addOnMapRenderListener(captor.capture())
        captor.value.onSizeChanged(200,200)
        fragment.zoomToBoundingBoxWithMargins(mock(GeoBoundingBox::class.java))
        verify(mockMap).zoomTo(anySafe(), anyInt(), anyInt(), anySafe(), anyFloat())
    }


}