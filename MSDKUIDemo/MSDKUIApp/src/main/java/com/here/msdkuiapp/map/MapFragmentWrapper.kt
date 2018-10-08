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

package com.here.msdkuiapp.map

import android.graphics.PointF
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.here.android.mpa.common.GeoBoundingBox
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.OnEngineInitListener
import com.here.android.mpa.common.ViewObject
import com.here.android.mpa.mapping.*
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.routing.Route
import com.here.msdkuiapp.R
import com.here.msdkuiapp.common.Provider
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlin.math.max
import kotlin.math.min

/**
 * Wrapper over [MapFragment] to deal with extra functionality over Map.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class MapFragmentWrapper() : MapFragment() {

    private val state = State()
    private val provider = Provider()

    companion object {
        private val TAG = MapFragmentWrapper::class.java.name
        /**
         * Marker anchor point, expressed as factor to be able to support markers in all resolutions.
         * Factor is calculated (x is coordinate in asset that will be anchored to map) by: x / asset width.
         * The same for y (y / asset height). If different types of markers (eg. pointing point
         * at the top of asset) will be added to map then every type of marker must have its own factors.
         * These values must be adjusted if marker assets change.
         */
        const val MARKER_ANCHOR_POINT_FACTOR_X: Float = 0.49f
        const val MARKER_ANCHOR_POINT_FACTOR_Y: Float = 0.96f
    }

    init {
        retainInstance = true
    }

    private val gestureListener = object : MapGesture.OnGestureListener {
        override fun onLongPressRelease() {

        }

        override fun onRotateEvent(p0: Float): Boolean {
            return false
        }

        override fun onMultiFingerManipulationStart() {
        }

        override fun onPinchLocked() {
        }

        override fun onPinchZoomEvent(p0: Float, p1: PointF?): Boolean {
            return false
        }

        override fun onPanStart() {
        }

        override fun onMultiFingerManipulationEnd() {
        }

        override fun onDoubleTapEvent(p0: PointF?): Boolean {
            return false
        }

        override fun onPanEnd() {
        }

        override fun onTiltEvent(p0: Float): Boolean {
            return false
        }

        override fun onMapObjectsSelected(p0: MutableList<ViewObject>?): Boolean {
            return false
        }

        override fun onRotateLocked() {
        }

        override fun onLongPressEvent(point: PointF?): Boolean {
            handleEvent(point)
            return true
        }

        override fun onTwoFingerTapEvent(p0: PointF?): Boolean {
            return false
        }

        override fun onTapEvent(point: PointF?): Boolean {
            handleEvent(point)
            return true;
        }

        private fun handleEvent(point: PointF?) {
            val geo = map.pixelToGeo(point)
            state.mapMarker?.coordinate = geo
            state.mapContainer?.addMapObject(state.mapMarker)
            fragmentHandler?.post(Runnable { listener?.onPointSelectedOnMap(geo) })
        }

        val fragmentHandler: Handler?
            get() = activity?.let { Handler(it.mainLooper) }
    }

    /**
     * Starts Map engine.
     *
     * @param listener block method to be called after map engine initialization.
     */
    fun start(listener: (() -> Unit)?) {
        init { err ->
            if (err == OnEngineInitListener.Error.NONE) {
                Log.v(TAG, "Success")
                view?.contentDescription = null
                if (state.mapContainer == null) {
                    state.mapContainer = provider.providesMapContainer()
                    map.addMapObject(state.mapContainer)
                }
                if (state.mapMarker == null) {
                    state.mapMarker = getMapMarker(R.drawable.ic_add_marker)
                }
                changeGesture()
                listener?.invoke()
            } else {
                Log.e(TAG, err.name + ": " + err.details)
                Toast.makeText(activity, err.details, Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Shows or hide position indicator with accuracy indicator.
     *
     * @param isShow true if position indicator needs to be shown, false otherwise.
     */
    fun showPositionIndicator(isShow: Boolean = true) {
        map ?: return
        with(positionIndicator) {
            isVisible = isShow
            isAccuracyIndicatorVisible = isShow
        }
    }

    /**
     * Renders Route on Map.
     *
     * @param route Route to be added on map.
     * @param withStartFlag true if start marker should be added.
     */
    fun renderRoute(route: Route, withStartFlag: Boolean = true) {
        clearMap()
        state.mapRoute = provider.providesMapRoutes(route)
        state.routeBoundingBox = route.boundingBox
        map.addMapObject(state.mapRoute)
        if (withStartFlag) {
            state.mapContainer?.addMapObject(getMapMarker(R.drawable.ic_route_start, route.start))
        }
        state.mapContainer?.addMapObject(getMapMarker(R.drawable.ic_route_end, route.destination))
    }

    /**
     * Zooms the map to given bounding box.
     * @param box [GeoBoundingBox]
     */
    fun zoomToBoundingBox(box: GeoBoundingBox) {
        map.zoomTo(box, Map.Animation.LINEAR, Map.MOVE_PRESERVE_ORIENTATION)
        map.setCenter(box.center, Map.Animation.LINEAR)
    }

    /**
     * Zoom the map based on custom [GeoBoundingBox].
     *
     * @param withCustomBox if true, this will calculate a double height bounding box to zoom.
     */
    fun zoomToRoute(withCustomBox: Boolean = false) {
        val route = state.mapRoute?.route ?: return
        if (state.routeBoundingBox!! == route.boundingBox && withCustomBox) {
            val start = route.start
            val destination = route.destination
            val minLong = min(start.longitude, destination.longitude)
            val minLat = min(start.latitude, destination.latitude)
            val maxLong = max(start.longitude, destination.longitude)
            val maxLat = max(start.latitude, destination.latitude)
            val topLeft = provider.providesGeoCoordinate(maxLat, minLong)
            val bottomRight = provider.providesGeoCoordinate(minLat, maxLong)
            val pixelResult = map.projectToPixel(bottomRight)
            if (pixelResult != null && pixelResult.error == Map.PixelResult.Error.NONE) {
                val result = pixelResult.result
                pixelResult.result?.run {
                    // maneuver is half screen, bounding box height should be double from current to show  route
                    val newY = y * 2
                    val newRightCord = map.pixelToGeo(PointF(result.x, newY))
                    state.routeBoundingBox = provider.providesGeoBoundingBox(topLeft, newRightCord)
                }
            }
        }
        val box = if (withCustomBox) state.routeBoundingBox else route.boundingBox
        map.zoomTo(box, Map.Animation.LINEAR, Map.MOVE_PRESERVE_ORIENTATION)
    }

    private fun getMapMarker(imageId: Int, cord: GeoCoordinate? = null): MapMarker {
        val marker = provider.providesMapMarker()
        val image = provider.providesImage()
        image.setImageResource(imageId)
        marker.icon = image
        cord?.let {
            marker.coordinate = it
        }
        marker.anchorPoint = PointF(
                image.width.toFloat() * MARKER_ANCHOR_POINT_FACTOR_X,
                image.height.toFloat() * MARKER_ANCHOR_POINT_FACTOR_Y)
        return marker
    }

    /**
     * Clears the map object.
     */
    fun clearMap(): Boolean {
        var ret = false
        state.mapRoute?.let {
            map.removeMapObject(it)
            ret = true
        }
        state.mapContainer?.let {
            it.removeAllMapObjects()
            ret = true
        }
        return ret
    }

    /**
     * Handles the back button pressed.
     */
    fun onBackPressed(): Boolean {
        return clearMap()
    }

    var traffic: Boolean
        get() = map.isTrafficInfoVisible
        set(value) {
            map.isTrafficInfoVisible = value
        }

    /**
     * Enable/disable touch on map.
     *
     * @param enable if true enable the touch, otherwise disable the touch.
     * @param listener [Listener] for listen event of touch.
     */
    fun onTouch(enable: Boolean, listener: Listener? = null) {
        state.gestureEnable = enable
        this.listener = listener
        changeGesture()
    }

    private fun changeGesture() {
        if (state.gestureEnable) {
            mapGesture?.addOnGestureListener(gestureListener, Int.MAX_VALUE, true)
            // clearMap()
        } else {
            state.mapContainer?.removeMapObject(state.mapMarker)
            mapGesture?.removeOnGestureListener(gestureListener)
        }
    }


    private var listener: Listener? = null

    /**
     * Listener for Map events.
     */
    interface Listener {

        /**
         * Callback to indicate tap or double tap on map.
         */
        fun onPointSelectedOnMap(cord: GeoCoordinate)
    }

    private class State {
        var mapMarker: MapMarker? = null
        var mapRoute: MapRoute? = null
        var routeBoundingBox: GeoBoundingBox? = null
        var mapContainer: MapContainer? = null
        var gestureEnable = false
    }
}