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

package com.here.msdkuiapp.espresso.impl.views.route.utils

import com.here.msdkuiapp.espresso.impl.testdata.Constants.ROUTE_RESULT_1
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType

data class RouteData(
        var transportType: TransporType = TransporType.TYPE_CAR,
        var duration: String? = null,
        var details: String? = null,
        var arrival: String? = null,
        var traffic: String? = null,
        var routeItem: Int = ROUTE_RESULT_1)