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

package com.here.msdkuiapp.routing

import android.os.Bundle
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseActivity
import com.here.msdkuiapp.base.BaseFragmentCoordinator
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Activity class to handle routing.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class RoutingActivity : BaseActivity() {

    internal var routingCoordinator: RoutingCoordinator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routing)
        routingCoordinator = routingCoordinator ?: RoutingCoordinator(this, supportFragmentManager)
        routingCoordinator!!.start()
    }

    override var coordinator: BaseFragmentCoordinator? = null
        get() = routingCoordinator
}



