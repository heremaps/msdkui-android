/*
 * Copyright (C) 2017-2021 HERE Europe B.V.
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

package com.here.msdkuiapp.guidance

import android.os.Bundle
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseFragmentCoordinator
import com.here.msdkuiapp.base.BasePermissionActivity
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Activity class for Route Selection for Guidance.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceRouteSelectionActivity : BasePermissionActivity() {

    private var guidanceRouteSelectionCoordinator: GuidanceRouteSelectionCoordinator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guidance_map_selection)
        if (guidanceRouteSelectionCoordinator == null) {
            guidanceRouteSelectionCoordinator = GuidanceRouteSelectionCoordinator(this, supportFragmentManager)
        }
        guidanceRouteSelectionCoordinator!!.start()
    }

    override fun onLocationReady() {
        guidanceRouteSelectionCoordinator?.onLocationReady()
    }

    override var coordinator: BaseFragmentCoordinator?
        get() = guidanceRouteSelectionCoordinator
        set(value) {
            guidanceRouteSelectionCoordinator = value as GuidanceRouteSelectionCoordinator
        }
}
