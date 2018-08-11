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

package com.here.msdkuiapp.guidance

import android.content.Intent
import android.os.Bundle
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseFragmentCoordinator
import com.here.msdkuiapp.base.BasePermissionActivity
import com.here.msdkuiapp.common.Constant.GUIDANCE_IS_SIMULATION_KEY
import com.here.msdkuiapp.common.Constant.ROUTE_KEY
import com.here.msdkuiapp.landing.LandingActivity
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.guidance_status_panel.*

/**
 * Guidance Main/Entry Activity.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceActivity : BasePermissionActivity() {

    internal var guidanceCoordinator: GuidanceCoordinator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar()
        setContentView(R.layout.activity_guidance)

        if (isLocationOk().not()) {
            startLandingActivity()
            return
        }
        if (guidanceCoordinator == null) {
            guidanceCoordinator = GuidanceCoordinator(this, fragmentManager)
        }
        guidanceCoordinator!!.apply {
            routeDeserialize = intent.extras.getByteArray(ROUTE_KEY)
            isSimulation = intent.extras.getBoolean(GUIDANCE_IS_SIMULATION_KEY, false)
            start()
        }
        stop_navigation.setOnClickListener { finishGuidance() }
    }

    override fun onDestroy() {
        super.onDestroy()
        guidanceCoordinator?.destroy()
    }

    override fun onLocationReady() {
    }

    override var coordinator: BaseFragmentCoordinator?
        get() = guidanceCoordinator
        set(value) {
            guidanceCoordinator = value as GuidanceCoordinator
        }

    /**
     * do action bar specific settings.
     */
    private fun setUpActionBar() {
        supportActionBar?.hide()
    }

    /**
     * starts LandingActivity with clear top flag.
     */
    private fun startLandingActivity() {
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
    }

    /**
     * finish guidance activity.
     */
    private fun finishGuidance() {
        guidanceCoordinator?.run {
            if (!onBackPressed()) {
                startLandingActivity()
            }
        } ?: startLandingActivity()
    }

    /**
     * Handles back press.
     */
    override fun onBackPressed() {
        finishGuidance()
    }
}
