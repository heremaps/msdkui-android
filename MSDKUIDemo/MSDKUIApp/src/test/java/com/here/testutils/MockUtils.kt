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

import com.here.android.mpa.common.GeoBoundingBox
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.RoadElement
import com.here.android.mpa.routing.*
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.*
import java.util.*

object MockUtils {

    fun mockRoute(mockTta: RouteTta = mockTta(),
                  mockManeuvers: List<Maneuver> = Arrays.asList(mockManeuver()),
                  mockRouteElements: RouteElements = mockRouteElements(),
                  mockPlan: RoutePlan = mockRoutePlan(),
                  mockLength: Int = 10000,
                  mockBox: GeoBoundingBox = mockGeoBoundingBox(),
                  mockStart: GeoCoordinate? = mock(GeoCoordinate::class.java)): Route {

        val route: Route = mock<Route>(Route::class.java)
        return route.apply {
            `when`<RouteTta>(getTta(any<Route.TrafficPenaltyMode>(Route.TrafficPenaltyMode::class.java), anyInt())).thenReturn(mockTta)
            `when`<List<Maneuver>>(maneuvers).thenReturn(mockManeuvers)
            `when`<RouteElements>(routeElements).thenReturn(mockRouteElements)
            `when`<RoutePlan>(routePlan).thenReturn(mockPlan)
            `when`<Int>(length).thenReturn(mockLength)
            `when`<GeoBoundingBox>(boundingBox).thenReturn(mockBox)
            `when`<GeoCoordinate>(start).thenReturn(mockStart)
        }
    }

    private fun mockGeoBoundingBox(): GeoBoundingBox {
        return mock(GeoBoundingBox::class.java)
    }

    private fun mockRoutePlan(mode: RouteOptions.TransportMode = RouteOptions.TransportMode.CAR): RoutePlan {
        val options = mock(RouteOptions::class.java)
        `when`<RouteOptions.TransportMode>(options.transportMode).thenReturn(RouteOptions.TransportMode.CAR)
        val plan = mock(RoutePlan::class.java)
        doReturn(options).`when`(plan).routeOptions
        `when`(plan.routeOptions).thenReturn(options)
        return plan
    }

    private fun mockRouteElements(): RouteElements {
        val routeElements = mock(RouteElements::class.java)
        val elements = ArrayList(Arrays.asList<RouteElement>(mockRouteElement()))
        `when`(routeElements.elements).thenReturn(elements)
        return routeElements
    }

    private fun mockRouteElement(): RouteElement {
        val roadElem = mock(RoadElement::class.java)
        `when`(roadElem.geometryLength).thenReturn(100.0)
        `when`(roadElem.roadName).thenReturn("Test Road Name")
        `when`(roadElem.routeName).thenReturn("RouteName")
        val elem = mock(RouteElement::class.java)
        `when`(elem.roadElement).thenReturn(roadElem)
        return elem
    }

    private fun mockTta(isBlocked: Boolean = false, duration: Int = 10000): RouteTta {
        val tta = mock<RouteTta>(RouteTta::class.java)
        `when`<Boolean>(tta.isBlocked).thenReturn(isBlocked)
        `when`<Int>(tta.duration).thenReturn(duration)
        return tta
    }

    private fun mockManeuver(): Maneuver {
        val maneuver = mock(Maneuver::class.java)
        `when`(maneuver.distanceToNextManeuver).thenReturn(200)
        `when`(maneuver.roadName).thenReturn("test road")
        `when`(maneuver.roadNumber).thenReturn("")
        `when`(maneuver.transportMode).thenReturn(RouteOptions.TransportMode.CAR)
        `when`<Maneuver.Action>(maneuver.action).thenReturn(Maneuver.Action.ENTER_HIGHWAY)
        `when`<Maneuver.Turn>(maneuver.turn).thenReturn(Maneuver.Turn.LIGHT_LEFT)
        return maneuver
    }
}