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

package com.here.msdkuiapp.common

import android.content.Context
import com.here.android.mpa.common.GeoBoundingBox
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.Image
import com.here.android.mpa.common.MapEngine
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.mapping.MapContainer
import com.here.android.mpa.mapping.MapMarker
import com.here.android.mpa.mapping.MapRoute
import com.here.android.mpa.mapping.MapView
import com.here.android.mpa.routing.CoreRouter
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteOptions
import com.here.android.mpa.routing.RoutePlan
import com.here.android.mpa.routing.RouteWaypoint
import com.here.android.mpa.search.ReverseGeocodeRequest2

/**
 * Provides HERE SDK objects by creating them.
 */
class Provider {

    /**
     * Provides [RouteWaypoint].
     *
     * @param cord [GeoCoordinate] to create [RouteWaypoint].
     * @return created [RouteWaypoint].
     */
    fun providesRouteWaypoint(cord: GeoCoordinate): RouteWaypoint {
        return RouteWaypoint(cord)
    }

    /**
     * Provides [CoreRouter].
     * @return created [CoreRouter].
     */
    fun providesCoreRouter(): CoreRouter {
        return CoreRouter()
    }

    /**
     * Provides [RoutePlan].
     * @return created [RoutePlan].
     */
    fun provideRoutePlan(): RoutePlan {
        return RoutePlan()
    }

    /**
     * Provides [RouteOptions].
     * @return created [RouteOptions].
     */
    fun providesRouteOptions(): RouteOptions {
        return RouteOptions()
    }

    /**
     * Provides [ReverseGeocodeRequest2].
     * @return created [ReverseGeocodeRequest2]
     */
    fun providesReverseGeocodeRequest(cord: GeoCoordinate): ReverseGeocodeRequest2 {
        val ret = ReverseGeocodeRequest2(cord)
       // ret.locale = Locale.getDefault()
        return ret
    }

    /**
     * Provides [MapContainer].
     * @return created [MapContainer]
     */
    fun providesMapContainer(): MapContainer {
        return MapContainer()
    }

    /**
     * Provides [MapRoute].
     * @return created [MapRoute]
     */
    fun providesMapRoutes(route: Route): MapRoute {
        return MapRoute(route)
    }

    /**
     * Provides [GeoBoundingBox].
     * @return created [GeoBoundingBox]
     */
    fun providesGeoBoundingBox(topLeft: GeoCoordinate, bottomRight: GeoCoordinate): GeoBoundingBox {
        return GeoBoundingBox(topLeft, bottomRight)
    }

    /**
     * Provides [GeoCoordinate].
     * @return created [GeoCoordinate]
     */
    fun providesGeoCoordinate(lat: Double, long: Double): GeoCoordinate {
        return GeoCoordinate(lat, long)
    }

    /**
     * Provides [MapMarker].
     * @return created [MapMarker]
     */
    fun providesMapMarker(): MapMarker {
        return MapMarker()
    }

    /**
     * Provides [MapContainer].
     * @return created [MapContainer]
     */
    fun providesImage(): Image {
        return Image()
    }

    /**
     * Provides [DynamicPenalty].
     * @return created [DynamicPenalty].
     */
    fun providesDynamicPenalty(): DynamicPenalty = DynamicPenalty()

    /**
     * Provides [MapView].
     * @return created [MapView].
     */
    fun provideMapView(context: Context): MapView {
        return MapView(context)
    }

    /**
     * Provides [MapEngine].
     * @return created [MapEngine].
     */
    fun provideMapEngine(): MapEngine {
        return MapEngine.getInstance()
    }

    /**
     * Provides [Map].
     * @return created [Map].
     */
    fun provideMap(): Map {
        return Map()
    }
}