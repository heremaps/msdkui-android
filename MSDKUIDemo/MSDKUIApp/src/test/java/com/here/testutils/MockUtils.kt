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
import com.here.android.mpa.routing.RouteTta
import com.here.android.mpa.routing.RouteOptions
import com.here.android.mpa.routing.RoutePlan
import com.here.android.mpa.routing.RouteElement
import com.here.android.mpa.routing.RouteElements
import com.here.android.mpa.routing.DynamicPenalty

/**
 * Mock utilities class.
 */
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

    fun getDynamicPenality(mode: Route.TrafficPenaltyMode): DynamicPenalty {
        return mock(DynamicPenalty::class.java).apply {
            `when`(trafficPenaltyMode).thenReturn(mode)
        }
    }

    /**
     * Mock route builder to build route based on given options.
     */
    class MockRouteOptionsBuilder {
        private var mRouteOptions: RouteOptions? = null

        fun build(): RouteOptions {
            if (mRouteOptions == null) {
                mRouteOptions = mock(RouteOptions::class.java).apply {
                    `when`(transportMode).thenReturn(RouteOptions.TransportMode.CAR)
                }
            }
            return mRouteOptions!!
        }

        fun withTransportMode(transportModeRet: RouteOptions.TransportMode): MockRouteOptionsBuilder {
            mRouteOptions = mock(RouteOptions::class.java).apply {
                `when`(transportMode).thenReturn(transportModeRet)
            }
            return this
        }

        fun withFullDriveOptions(): MockRouteOptionsBuilder {
            mRouteOptions = mock(RouteOptions::class.java).apply {
                `when`(transportMode).thenReturn(RouteOptions.TransportMode.CAR)
                `when`(getTime(Date())).thenReturn(RouteOptions.TimeType.DEPARTURE)
                `when`(areCarShuttleTrainsAllowed()).thenReturn(true)
                `when`(isCarpoolAllowed).thenReturn(true)
                `when`(areDirtRoadsAllowed()).thenReturn(true)
                `when`(areFerriesAllowed()).thenReturn(true)
                `when`(areHighwaysAllowed()).thenReturn(true)
                `when`(areParksAllowed()).thenReturn(true)
                `when`(areTollRoadsAllowed()).thenReturn(true)
                `when`(areTunnelsAllowed()).thenReturn(true)
            }
            return this
        }

        fun withFewDriveOptions(): MockRouteOptionsBuilder {
            mRouteOptions = mock(RouteOptions::class.java).apply {
                `when`(areCarShuttleTrainsAllowed()).thenReturn(true)
                `when`(areDirtRoadsAllowed()).thenReturn(true)
            }
            return this
        }

        fun withFullHazardousOptions(): MockRouteOptionsBuilder {
            val hazardousGoodTypes = EnumSet.allOf(RouteOptions.HazardousGoodType::class.java)
            mRouteOptions = mock(RouteOptions::class.java).apply {
                `when`(truckShippedHazardousGoods).thenReturn(hazardousGoodTypes)
            }
            return this
        }

        fun withFewHazardousOptions(): MockRouteOptionsBuilder {
            val hazardousGoodTypes = EnumSet.of(RouteOptions.HazardousGoodType.COMBUSTIBLE)
            mRouteOptions = mock(RouteOptions::class.java).apply {
                `when`(truckShippedHazardousGoods).thenReturn(hazardousGoodTypes)
            }
            return this
        }

        fun withTunnelOptions(): MockRouteOptionsBuilder {
            mRouteOptions = mock(RouteOptions::class.java).apply {
                `when`(truckTunnelCategory).thenReturn(RouteOptions.TunnelCategory.C)
            }
            return this
        }

        fun withNoTunnelOptions(): MockRouteOptionsBuilder {
            mRouteOptions = mock(RouteOptions::class.java).apply {
                `when`(truckTunnelCategory).thenReturn(RouteOptions.TunnelCategory.UNDEFINED)
            }
            return this
        }

        fun withTruckOptions(): MockRouteOptionsBuilder {
            mRouteOptions = mock(RouteOptions::class.java).apply {
                `when`(truckHeight).thenReturn(3.0f)
                `when`(truckWidth).thenReturn(3.0f)
                `when`(truckLimitedWeight).thenReturn(3.0f)
                `when`(truckWeightPerAxle).thenReturn(3.0f)
                `when`(truckTrailersCount).thenReturn(3)
                `when`(truckType).thenReturn(RouteOptions.TruckType.TRACTOR_TRUCK)
            }
            return this
        }

    }

    /**
     * Mock route builder to build route based on given options.
     */
    class MockRouteBuilder {

        val route: Route
        private val mRouteTta1: RouteTta
        private var mRouteTta2: RouteTta? = null
        private val mRouteElements: RouteElements
        private val mPlan: RoutePlan
        private var mOptions: RouteOptions? = null

        init {
            route = mock(Route::class.java)

            mRouteTta1 = mock(RouteTta::class.java).apply {
                `when`(isBlocked).thenReturn(false)
                `when`(duration).thenReturn(1000)
            }
            `when`(route.getTta(any(Route.TrafficPenaltyMode::class.java), anyInt())).thenReturn(mRouteTta1)

            val elementsList = ArrayList(Collections.singletonList(mockRouteElement()))
            mRouteElements = mock(RouteElements::class.java).apply {
                `when`(elements).thenReturn(elementsList)
            }
            `when`(route.routeElements).thenReturn(mRouteElements)

            mOptions = MockUtils.MockRouteOptionsBuilder().build()
            mPlan = mock(RoutePlan::class.java).apply {
                `when`(routeOptions).thenReturn(mOptions)
            }
            `when`(route.routePlan).thenReturn(mPlan)

            setListOfManeuver()
            `when`(route.length).thenReturn(1000)
        }

        fun setBlockedRoad(): MockRouteBuilder {
            `when`(mRouteTta1.isBlocked).thenReturn(true)
            return this
        }

        fun setTransportMode(transportMode: RouteOptions.TransportMode): MockRouteBuilder {
            mOptions = MockRouteOptionsBuilder().withTransportMode(transportMode).build()
            `when`(mPlan.routeOptions).thenReturn(mOptions)
            return this
        }

        fun setTrafficPenaltyMinutes(minutes: Int): MockRouteBuilder {
            val delayInSeconds = minutes * 60
            `when`(mRouteTta1.duration).thenReturn(delayInSeconds)
            `when`(route.getTta(eq(Route.TrafficPenaltyMode.OPTIMAL), anyInt())).thenReturn(mRouteTta1)
            if (mRouteTta2 == null) {
                mRouteTta2 = mock(RouteTta::class.java)
            }
            `when`(mRouteTta2!!.duration).thenReturn(0)
            `when`(route.getTta(eq(Route.TrafficPenaltyMode.DISABLED), anyInt())).thenReturn(mRouteTta2)

            return this
        }

        fun setListOfManeuver(): MockRouteBuilder {
            val maneuver = mockManeuver()
            `when`(route.maneuvers).thenReturn(ArrayList(Collections.singletonList(maneuver)))
            return this
        }
    }

}