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
import android.support.design.widget.BottomSheetBehavior
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.here.msdkuiapp.R
import com.here.msdkuiapp.about.AboutActivity
import com.here.msdkuiapp.base.BaseFragmentCoordinator
import com.here.msdkuiapp.base.BasePermissionActivity
import com.here.msdkuiapp.common.Constant.GUIDANCE_IS_SIMULATION_KEY
import com.here.msdkuiapp.landing.LandingActivity
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.activity_guidance.*

/**
 * Guidance Main/Entry Activity.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceActivity : BasePermissionActivity() {

    internal var guidanceCoordinator: GuidanceCoordinator? = null

    internal var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar()
        setContentView(R.layout.activity_guidance)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (isLocationOk().not()) {
            startLandingActivity()
            return
        }
        if (guidanceCoordinator == null) {
            guidanceCoordinator = GuidanceCoordinator(this, fragmentManager)
        }
        guidanceCoordinator!!.apply {
            isSimulation = intent.getBooleanExtra(GUIDANCE_IS_SIMULATION_KEY, false)
            start()
        }

        setUpDashBoard()
    }

    private val dashboardListener = object : GuidanceDashBoardViewListener {

        override fun onCollapsed() {
            grayed_screen_view_first_part.visibility = View.GONE
            grayed_screen_view_second_part?.visibility = View.GONE
        }

        override fun onExpanded() {
            grayed_screen_view_first_part.visibility = View.VISIBLE
            grayed_screen_view_second_part?.visibility = View.VISIBLE
        }

        override fun onItemClicked(itemIndex: Int) {
            if (itemIndex == 1) {
                startAboutActivity()
            }
        }

        override fun onStopNavigationButtonClicked() {
            finishGuidance()
        }

        override fun onCollapsedViewClicked() {
            if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            } else if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
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
     * Do action bar specific settings.
     */
    private fun setUpActionBar() {
        supportActionBar?.hide()
    }

    /**
     * Set up Guidance Dashboard.
     */
    private fun setUpDashBoard() {
        if (bottomSheetBehavior == null) bottomSheetBehavior = BottomSheetBehavior.from(guidance_dashboard_view)
        bottomSheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> guidance_dashboard_view.presenter.onCollapsed()
                    BottomSheetBehavior.STATE_EXPANDED -> guidance_dashboard_view.presenter.onExpanded()
                    else -> Log.d(GuidanceActivity::class.java.name,
                            "GuidanceDashBoard not handled state change (state value:$newState)")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        guidance_dashboard_view.presenter.addListener(dashboardListener)

        val hideDashBoard = View.OnClickListener {
            if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        grayed_screen_view_first_part.setOnClickListener(hideDashBoard)
        grayed_screen_view_second_part?.setOnClickListener(hideDashBoard)
    }

    /**
     * Starts LandingActivity.
     */
    private fun startLandingActivity() {
        startActivity(Intent(this, LandingActivity::class.java))
    }

    /**
     * Starts about activity.
     */
    private fun startAboutActivity() {
        startActivity(Intent(this, AboutActivity::class.java))
    }

    /**
     * Finish guidance activity.
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
