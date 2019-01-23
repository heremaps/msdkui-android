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
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.here.android.mpa.common.ApplicationContext
import com.here.android.mpa.common.GeoBoundingBox
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.MapEngine
import com.here.android.mpa.common.OnEngineInitListener
import com.here.android.mpa.mapping.*
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.mapping.OnMapRenderListener
import com.here.android.mpa.routing.Route
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.RetainFragment
import com.here.msdkuiapp.common.Provider
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Wrapper over [MapFragment] to deal with extra functionality over Map.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class MapFragmentWrapper : RetainFragment() {

    private val state = State()

    internal var injectedProvider : Provider? = null

    // private field to be used without null check in this file.
    private val provider : Provider
        get() = injectedProvider ?: Provider()

    private val mapView
            get() = (view as? MapView)

    internal var map: Map? = null

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

        /**
        * Percentage value of marker width / height that margin will be increased while zooming
        * to route using {@link #zoomToBoundingBoxWithMargins(GeoBoundingBox)} function.
        */
        const val ADDITIONAL_MARGIN_IN_PERCENTAGE_OF_MARKER = 0.25f
    }

    private val gestureListener = object : MapGesture.OnGestureListener.OnGestureListenerAdapter() {
        override fun onLongPressEvent(point: PointF?): Boolean {
            handleEvent(point)
            return true
        }

        override fun onTapEvent(point: PointF?): Boolean {
            handleEvent(point)
            return true
        }

        private fun handleEvent(point: PointF?) {
            val geo = map?.pixelToGeo(point)
            state.mapMarker?.coordinate = geo
            state.mapContainer?.addMapObject(state.mapMarker)
            fragmentHandler?.post {
                geo?.run {
                    listener?.onPointSelectedOnMap(geo)
                }
            }
        }

        val fragmentHandler: Handler?
            get() = activity?.let { Handler(it.mainLooper) }
    }

    private val onMapRenderListener = object : OnMapRenderListener {
        override fun onPreDraw() {}

        override fun onPostDraw(invalidated: Boolean, renderTime: Long) {}

        override fun onSizeChanged(width: Int, height: Int) {
            if (state.zoomToRouteAfterMapViewOnSizeChangedPending) {
                state.zoomToRouteAfterMapViewOnSizeChangedPending = !zoomToBoundingBoxWithMargins(state.routeBoundingBox)
            }
        }

        override fun onGraphicsDetached() {}

        override fun onRenderBufferCreated() {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return provider.provideMapView(inflater.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onDestroyView() {
        mapView?.map = null
        super.onDestroyView()

    }
    override fun onPause() {
        super.onPause()
        mapView?.onPause()
        if(MapEngine.isInitialized()) {
            MapEngine.getInstance().onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
        if(MapEngine.isInitialized()) {
            MapEngine.getInstance().onResume()
        }
    }

    /**
     * Starts Map engine.
     *
     * @param listener block method to be called after map engine initialization.
     */
    fun start(listener: (() -> Unit)?) {
        map?.run {
            populateUI(listener)
            return
        }
        provider.provideMapEngine().init(ApplicationContext(activity!!.applicationContext)) { err ->
            if (err == OnEngineInitListener.Error.NONE) {
                populateUI(listener)
            } else {
                Log.e(TAG, err.name + ": " + err.details)
                Toast.makeText(activity, err.details, Toast.LENGTH_LONG)?.show()
            }
        }
    }

    private fun populateUI(listener: (() -> Unit)?) {
        map ?: run {
            map = provider.provideMap()
        }
        mapView?.map ?: run {
            mapView?.map = map
            mapView?.addOnMapRenderListener(onMapRenderListener)
        }
        Log.v(TAG, "Success")
        view?.contentDescription = null
        if (state.mapContainer == null) {
            state.mapContainer = provider.providesMapContainer()
            map?.addMapObject(state.mapContainer)
        }
        if (state.mapMarker == null) {
            state.mapMarker = getMapMarker(R.drawable.ic_add_marker)
        }
        prepareZoomMarginsAccordingToMarkers()
        changeGesture()
        listener?.invoke()
    }

    /**
     * Shows or hide position indicator with accuracy indicator.
     *
     * @param isShow true if position indicator needs to be shown, false otherwise.
     */
    fun showPositionIndicator(isShow: Boolean = true) {
        map ?: return
        with(mapView!!.positionIndicator) {
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
        map?.run {
            clearMap()
            state.mapRoute = provider.providesMapRoutes(route)
            state.routeBoundingBox = route.boundingBox
            addMapObject(state.mapRoute)
            if (withStartFlag) {
                state.mapContainer?.addMapObject(getMapMarker(R.drawable.ic_route_start, route.start))
            }
            state.mapContainer?.addMapObject(getMapMarker(R.drawable.ic_route_end, route.destination))
        }
    }

    /**
     * Zooms the map to given bounding box (additionally it adds margins, to be sure that markers
     * are fully visible).
     * @param box [GeoBoundingBox]
     * @return true if zoomTo function has been executed, otherwise false
     */
    internal fun zoomToBoundingBoxWithMargins(box: GeoBoundingBox?): Boolean {
        box != null ?: map?.run {
            val newWidth = width - 2 * maxOf(state.zoomLeftMargin, state.zoomRightMargin)
            val newHeight = height - 2 * maxOf(state.zoomTopMargin, state.zoomBottomMargin)
            print(newWidth)
            print(newHeight)
            if (newWidth > 0 && newHeight > 0) {
                zoomTo(box,
                        newWidth,
                        newHeight,
                        Map.Animation.LINEAR,
                        Map.MOVE_PRESERVE_ORIENTATION)
                return true
            }
        }
        return false
    }

    /**
     * Renders Route and zoom to this Route after map size changed
     * {@link OnMapRenderListener#onSizeChanged(int, int)}.
     *
     * @param route Route to be added and zoomed on map.
     * @param withStartFlag true if start marker should be added.
     */
    fun renderAndZoomTo(route: Route, withStartFlag: Boolean = true) {
        renderRoute(route, withStartFlag)
        state.zoomToRouteAfterMapViewOnSizeChangedPending = true
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
            map?.removeMapObject(it)
            ret = true
        }
        state.mapContainer?.let {
            it.removeAllMapObjects()
            ret = true
        }
        return ret
    }

    /*
     * This application uses two markers:
     *    route start marker - ic_route_start
     *    route end marker - ic_route_end
     * They have identical size (that's why the same anchor point position is used for both
     * of them: MARKER_ANCHOR_POINT_FACTOR_X, MARKER_ANCHOR_POINT_FACTOR_Y) so margins calculated
     * for one marker will be proper for both markers.
     */
    private fun prepareZoomMarginsAccordingToMarkers() {
        val image = provider.providesImage()
        image.setImageResource(R.drawable.ic_route_start)

        state.zoomLeftMargin = (image.width.toFloat() * ADDITIONAL_MARGIN_IN_PERCENTAGE_OF_MARKER +
                image.width.toFloat() * MARKER_ANCHOR_POINT_FACTOR_X).toInt()

        state.zoomRightMargin = (image.width.toFloat() * ADDITIONAL_MARGIN_IN_PERCENTAGE_OF_MARKER +
                image.width.toFloat() * (1.0 - MARKER_ANCHOR_POINT_FACTOR_X)).toInt()

        state.zoomTopMargin = (image.height.toFloat() * ADDITIONAL_MARGIN_IN_PERCENTAGE_OF_MARKER +
                image.height.toFloat() * MARKER_ANCHOR_POINT_FACTOR_Y).toInt()

        state.zoomBottomMargin = (image.height.toFloat() * ADDITIONAL_MARGIN_IN_PERCENTAGE_OF_MARKER +
                image.height.toFloat() * (1.0 - MARKER_ANCHOR_POINT_FACTOR_Y)).toInt()
    }

    /**
     * Handles the back button pressed.
     */
    fun onBackPressed(): Boolean {
        return clearMap()
    }

    var traffic: Boolean
        get() = map?.run {
            isTrafficInfoVisible
        } ?: false
        set(value) {
            map?.isTrafficInfoVisible = value
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
            mapView?.mapGesture?.addOnGestureListener(gestureListener, Int.MAX_VALUE, true)
        } else {
            state.mapContainer?.removeMapObject(state.mapMarker)
            mapView?.mapGesture?.removeOnGestureListener(gestureListener)
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
        var zoomLeftMargin = 0
        var zoomRightMargin = 0
        var zoomTopMargin = 0
        var zoomBottomMargin = 0
        var zoomToRouteAfterMapViewOnSizeChangedPending: Boolean = false
    }
}