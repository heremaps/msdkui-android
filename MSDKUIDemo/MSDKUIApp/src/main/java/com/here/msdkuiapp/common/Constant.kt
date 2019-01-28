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

package com.here.msdkuiapp.common

/**
 * Class to have all kind of constants shared across different modules.
 */
object Constant {

    /**
     * Key value of intent extra boolean that determines whether simulation or normal guidance should be started.
     */
    const val GUIDANCE_IS_SIMULATION_KEY: String = "guidanceIsSimulation"

    /**
     * Key value of intent extra long that determines simulation speed
     */
    const val GUIDANCE_SIMULATION_SPEED: String = "guidanceSimulationSpeed"

    /**
     * Key value of intent extra boolean that determines whether guidance reached its destination.
     */
    const val GUIDANCE_DID_FINISHED: String = "guidanceDidFinished"

    /**
     * Key value of intent extra boolean for route.
     */
    const val ROUTE_KEY: String = "routeKey"

    /**
     * Key value of intent extra integer that determines visibility of a view.
     */
    const val GO_VISIBILITY = "goVisibility"

}