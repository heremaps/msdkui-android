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

package com.here.msdkuiapp.espresso.tests.guidance.integration

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.SplashActivity
import com.here.msdkuiapp.espresso.impl.annotation.IntegrationUITest
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.core.CoreView.onPlannerBarAppNameTitleView
import com.here.msdkuiapp.espresso.impl.testdata.Constants
import com.here.msdkuiapp.espresso.impl.testdata.Constants.GEO_POINT_1
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.matchers.DriveNavigationMatchers
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions.DriveNavigationActions
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions.DriveNavigationBarActions
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceCurrentStreetInfo
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceDashBoardCurrentSpeedValue
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceDashBoardEtaInfo
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceNextManeuverPanel
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceSpeedLimiter
import com.here.msdkuiapp.espresso.impl.views.guidance.useractions.GuidanceActions
import com.here.msdkuiapp.espresso.impl.views.map.useractions.MapActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerBarActions
import com.here.msdkuiapp.espresso.tests.TestBase
import org.junit.Before
import org.junit.After
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Test
import org.junit.runners.MethodSorters

/**
 * Test for integration of RoutePlanner component with other components.
 */
@Ignore // FIXME: MSDKUI-1350
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GuidanceIntegrationTests: TestBase<SplashActivity>(SplashActivity::class.java) {

    private val destination = GEO_POINT_1

    @Before
    fun prepare() {
        CoreActions().enterDriveNavigation().provideMockLocation(mockLocationData)
        // Check drive navigation view opened and default destination label displayed
        DriveNavigationBarActions.waitForDestinationDisplayed()
        // Check map view displayed and tap anywhere on map view
        MapActions.waitForMapViewEnabled().tap(destination)
        // Tap on tick an actionbar to confirm guidance
        DriveNavigationBarActions.waitForDestinationNotDisplayed()
                .waitForRightImageIconCheck()
        RoutePlannerBarActions.tapOnTickButton()
        // Check that route overview exists
        DriveNavigationBarActions.waitForRouteOverView()
                .waitForGuidanceDescriptionDisplayed()
    }

    @After
    fun tearDown() {
        CoreActions().stopMockLocation(mockLocationData)
    }

    /**
     * MSDKUI-570: Integration tests for Guidance start/stop
     */
    @Test
    @IntegrationUITest
    fun testForGuidanceStartStop_shouldOpenGuidanceView() {
        // Tap on Start navigation to open guidance view
        DriveNavigationActions
                .tapOnStartNavigationBtn()
                .tapOnStopNavigationBtn()
        // Check that returns from guidance to Landing screen
        onPlannerBarAppNameTitleView.check(matches(isDisplayed()))
    }

    /**
     * MSDKUI-866: Integration tests for route calculation in Guidance
     */
    @Test
    @IntegrationUITest
    fun testForRouteCalculationInGuidance_shouldOpenRouteOverview() {
        // Check that route overview information is displayed
        DriveNavigationMatchers.checkRouteOverviewInfoDisplayed()
    }

    /**
     * MSDKUI-1267: Integration tests for showing current speed on maneuver panel
     */
    @Test
    @IntegrationUITest
    fun testForGuidanceManeuverPanel_shouldDisplayCurrentSpeed() {
        // Start navigation simulation
        DriveNavigationActions.startNavigationSimulation()
        // Check that speed value element is displayed on guidance view
        onGuidanceDashBoardCurrentSpeedValue.check(matches(isDisplayed()))
    }

    /**
     * MSDKUI-1288:  Integration test for Guidance/Street label component
     */
    @Test
    @IntegrationUITest
    fun testForGuidanceCurrentStreet_shouldDisplayCurrentStreet() {
        // Start navigation
        DriveNavigationActions.tapOnStartNavigationBtn()
        // Check that current street element is displayed on guidance view for both orientations
        enumValues<Constants.ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions().changeOrientation(it)
            // Check is view displayed
            onGuidanceCurrentStreetInfo.check(matches(isDisplayed()))
        }
    }

    /**
     * MSDKUI-1474 Integration tests for ETA
     */
    @Test
    @IntegrationUITest
    fun testForGuidanceManeuverPanel_shouldDisplayETA() {
        // Start navigation
        DriveNavigationActions.tapOnStartNavigationBtn()
        // Check that ETA view is displayed on both screen orientations
        enumValues<Constants.ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions().changeOrientation(it)
            // Check is view displayed
            onGuidanceDashBoardEtaInfo.check(matches(isDisplayed()))
        }
    }

    /**
     * MSDKUI-1281 Integration tests for next-next maneuver.
     */
    @Test
    @IntegrationUITest
    fun testForNextNextManeuverView_shouldDisplayNextNextManeuver() {
        // Start navigation simulation
        DriveNavigationActions.startNavigationSimulation()
        // Check that next-next maneuver view is displayed on both screen orientations
        enumValues<Constants.ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions().changeOrientation(it)
            // Check is view displayed
            GuidanceActions.waitForGuidanceNextManeuverPanelDisplayed()
            onGuidanceNextManeuverPanel.check(matches(isDisplayed()))
        }
    }

    /**
     * MSDKUI-1271 Integration tests for guidance speed limit.
     */
    @Test
    @IntegrationUITest
    fun testForGuidanceSpeedLimit_shouldDisplaySpeedLimit() {
        // Start navigation simulation
        DriveNavigationActions.startNavigationSimulation()
        // Check that guidance speed limit is dispalyed
        GuidanceActions.waitForSpeedLimitDisplayed()
            CoreActions().changeOrientation(Constants.ScreenOrientation.LANDSCAPE)
        GuidanceActions.waitForSpeedLimitDisplayed()
    }
}