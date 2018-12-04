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

package com.here.msdkuiapp.espresso.tests.guidance.flow

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.R
import com.here.msdkuiapp.SplashActivity
import com.here.msdkuiapp.espresso.impl.annotation.FunctionalUITest
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getTextView
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIdAndText
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.Constants
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ScreenOrientation
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ScreenOrientation.PORTRAIT
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.matchers.DriveNavigationMatchers.checkCurrentStreetViewValueChanged
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.matchers.DriveNavigationMatchers.checkManeuverPanelData
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.matchers.DriveNavigationMatchers.checkManeuverPanelIsDestinationReached
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationBarView.onDriveNavigationBarTitleView
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewSeeManoeuvresNaviBtn
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewStartNaviBtn
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions.DriveNavigationActions
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions.DriveNavigationBarActions
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.utils.ManeuversActions
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceCurrentStreetInfoText
import com.here.msdkuiapp.espresso.impl.views.guidance.useractions.GuidanceActions
import com.here.msdkuiapp.espresso.impl.views.map.useractions.MapActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerBarActions
import com.here.msdkuiapp.espresso.tests.TestBase
import org.junit.*
import org.junit.runners.MethodSorters

/**
 * UI flow tests for guidance
 */
@Ignore // FIXME: MSDKUI-1350
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GuidanceFlowTests: TestBase<SplashActivity>(SplashActivity::class.java)  {

    private val destination = Constants.GEO_POINT_1
    private val destinationForShortSimulation = Constants.GEO_POINT_3

    @After
    fun tearDown() {
        CoreActions().changeOrientation(PORTRAIT).stopMockLocation(mockLocationData)
    }

    /**
     * MSDKUI-573: Select Drive Navigation destination via tap
     */
    @Test
    @FunctionalUITest
    fun testSelectDriveNavigationDestinationViaTap() {
        enumValues<ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions().changeOrientation(it)
            //Enter Drive Navigation
            CoreActions().enterDriveNavigation()
                    .provideMockLocation(mockLocationData)
            // Check drive navigation view opened
            DriveNavigationBarActions.waitForDriveNavigationView()
            // Check drive navigation view title
            onDriveNavigationBarTitleView.check(matches(isDisplayed()))
            // Check until default destination label displayed on drive navigation
            DriveNavigationBarActions.waitForDestinationDisplayed().checkDestinationDefaultLabel()
            // Check map view displayed and tap anywhere on map view
            MapActions.waitForMapViewEnabled()
                    .tap(destination)
            // Wait until destination location selected
            DriveNavigationBarActions.waitForDestinationNotDisplayed()
            // Returns back to Landing screen
            CoreActions().pressBackButton()
        }
    }

    /**
     * MSDKUI-574: Select Drive Navigation destination via long press
     */
    @Test
    @FunctionalUITest
    fun testSelectDriveNavigationDestinationViaLongPress() {
        enumValues<ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions().changeOrientation(it)
            //Enter Drive Navigation
            CoreActions().enterDriveNavigation()
                    .provideMockLocation(mockLocationData)
            // Check drive navigation view opened
            DriveNavigationBarActions.waitForDriveNavigationView()
            // Check drive navigation view title
            onDriveNavigationBarTitleView.check(matches(isDisplayed()))
            // Check until default destination label displayed on drive navigation
            DriveNavigationBarActions.waitForDestinationDisplayed().checkDestinationDefaultLabel()
            // Tap anywhere on map view
            MapActions.waitForMapViewEnabled()
                    .longTap(destination)
            // Wait until destination location selected
            DriveNavigationBarActions.waitForDestinationNotDisplayed()
            // Returns back to Landing screen
            CoreActions().pressBackButton()
        }
    }

    /**
     * MSDKUI-661: Guidance route overview
     */
    @Test
    @FunctionalUITest
    fun testGuidanceRouteOverview() {
        enumValues<ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions().changeOrientation(it)
            //Enter Drive Navigation
            CoreActions().enterDriveNavigation()
                    .provideMockLocation(mockLocationData)
            // Check drive navigation view opened
            DriveNavigationBarActions.waitForDriveNavigationView()
                    .waitForDestinationDisplayed()
            // Tap anywhere on map view
            MapActions.waitForMapViewEnabled()
                    .tap(destination)
            // Tap on tick to confirm the first waypoint selection
            DriveNavigationBarActions.waitForDestinationNotDisplayed()
            val location = RoutePlannerBarActions.tapOnTickButton()
            // Check route description and destination items on guidance route overview
            DriveNavigationBarActions.waitForRouteOverView()
                    .waitForGuidanceDescriptionDisplayed()
                    .checkRouteOverviewDescription()
                    .checkRouteOverviewDestination(location)
            // Check 'See manoeuvres' & 'Start navigation' button displayed
            onRouteOverviewSeeManoeuvresNaviBtn.check(matches(isDisplayed()))
            onRouteOverviewStartNaviBtn.check(matches(isDisplayed()))
            //Wait for position fix
            CoreActions().provideMockLocation(mockLocationData)
            // Returns back to the Drive Navigation
            CoreActions().pressBackButton()
            // Check drive navigation view opened
            DriveNavigationBarActions.waitForDriveNavigationView()
            // Returns back to the Landing Screen
            CoreActions().pressBackButton()
        }
    }

    /**
     * MSDKUI-576: Maneuver panel, switch orientation while navigation simulation is ongoing
     */
    @Test
    @FunctionalUITest
    fun testSwitchOrientationWhileNavigationSimulationIsOngoing() {
        //Enter Drive Navigation
        CoreActions().enterDriveNavigation()
                .provideMockLocation(mockLocationData)
        // Check drive navigation view opened
        DriveNavigationBarActions.waitForDriveNavigationView()
                .waitForDestinationDisplayed()
        // Tap anywhere on map view
        MapActions.waitForMapViewEnabled().tap(destination)
        // Tap on tick to confirm the first waypoint selection
        DriveNavigationBarActions.waitForDestinationNotDisplayed()
        RoutePlannerBarActions.tapOnTickButton()
        // Check guidance route overview view opened
        DriveNavigationBarActions.waitForRouteOverView()
                .waitForGuidanceDescriptionDisplayed()
        // Start navigation simulation
        DriveNavigationActions.startNavigationSimulation()
        enumValues<ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions().changeOrientation(it)
            // Check maneuver panel and dashboard is displayed
            GuidanceActions.checkGuidanceManeuverPanelInfo()
            GuidanceActions.checkGuidanceDashBoardInfo()
        }
    }

    /**
     * MSDKUI-575: Maneuver panel
     */
    @Test
    @FunctionalUITest
    fun testManeuverPanel() {
        enumValues<ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions().changeOrientation(it)
            //Enter Drive Navigation
            CoreActions().enterDriveNavigation()
                    .provideMockLocation(mockLocationData)
            // Check drive navigation view opened
            DriveNavigationBarActions.waitForDriveNavigationView()
                    .waitForDestinationDisplayed()
            // Tap anywhere on map view to define destination
            MapActions.waitForMapViewEnabled().tap(destinationForShortSimulation)
            // Tap on tick to confirm the first waypoint selection
            DriveNavigationBarActions.waitForDestinationNotDisplayed()
            RoutePlannerBarActions.tapOnTickButton()
            // Check guidance route overview view opened
            DriveNavigationBarActions.waitForRouteOverView()
                    .waitForGuidanceDescriptionDisplayed()
            // Click "See maneuvers" to open maneuvers list
            DriveNavigationActions.tapOnSeeManeuversBtn()
            // Save maneuvers information from maneuvers list
            val maneuversDataList = ManeuversActions.getManeuversDataFromManeuversList()
            // Start simulation
            DriveNavigationActions.startNavigationSimulation()
            // First check maneuver panel starting state.
            onRootView.perform(waitForCondition(
                    withIdAndText(R.id.infoView1, R.string.msdkui_maneuverpanel_nodata),
                    // Check interval is very short because maneuver panel is in this state for very short time.
                    checkInterval = 30))
            // During simulation first maneuver is skipped,
            // so let's make sure that there is more than one maneuver.
            val maneuversCount = maneuversDataList.size
            if (maneuversCount > 1) {
                for (index in 1 until maneuversCount) {
                    val maneuverAddress = maneuversDataList[index].address
                    val maneuverIconTag = maneuversDataList[index].iconTag
                    checkManeuverPanelData(maneuverAddress, maneuverIconTag)
                }
                // Check is destination reached (by checking address text color)
                checkManeuverPanelIsDestinationReached(maneuversDataList[maneuversCount - 1].address)
            }
            // Stop navigation and enter navigation again to test maneuver panel on different screen orientation.
            GuidanceActions.tapOnStopNavigationBtn()
        }
    }

    /**
     * MSDKUI-1478: Current street label in Guidance
     */
    @Test
    @FunctionalUITest
    fun testCurrentStreetLabelInGuidanceSimulation() {
        //Enter Drive Navigation
        CoreActions().enterDriveNavigation()
                .provideMockLocation(mockLocationData)
        // Check drive navigation view opened
        DriveNavigationBarActions.waitForDriveNavigationView()
                .waitForDestinationDisplayed()
        // Tap anywhere on map view
        MapActions.waitForMapViewEnabled().tap(destinationForShortSimulation)
        // Tap on tick to confirm the first waypoint selection
        DriveNavigationBarActions.waitForDestinationNotDisplayed()
                .provideMockLocation(mockLocationData)
        RoutePlannerBarActions.tapOnTickButton()
        // Check guidance route overview view opened
        DriveNavigationBarActions.waitForRouteOverView()
                .waitForGuidanceDescriptionDisplayed()
        // Start navigation simulation
        DriveNavigationActions.startNavigationSimulation()
        enumValues<ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions().changeOrientation(it)
            // Check that current street view is displayed and store its current value
            onGuidanceCurrentStreetInfoText.check(matches(isDisplayed()))
            val currentStreetData = getTextView(onGuidanceCurrentStreetInfoText)
            // Check that current street view value changed
            checkCurrentStreetViewValueChanged(currentStreetData)
        }
    }
 }