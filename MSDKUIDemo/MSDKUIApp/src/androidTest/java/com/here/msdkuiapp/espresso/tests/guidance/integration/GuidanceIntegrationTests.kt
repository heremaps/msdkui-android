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

package com.here.msdkuiapp.espresso.tests.guidance.integration

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.SplashActivity
import com.here.msdkuiapp.espresso.impl.annotation.IntegrationUITest
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.core.CoreView.onPlannerBarAppNameTitleView
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions.DriveNavigationBarActions
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.utils.DestinationData
import com.here.msdkuiapp.espresso.impl.views.map.useractions.MapActions
import com.here.msdkuiapp.espresso.impl.views.map.utils.MapData
import com.here.msdkuiapp.espresso.tests.TestBase
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Test
import org.junit.runners.MethodSorters

/**
 * Test for integration of RoutePlanner component with other components.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GuidanceIntegrationTests: TestBase<SplashActivity>(SplashActivity::class.java) {

    private lateinit var destination: DestinationData

    @Before
    fun prepare() {
        destination = DestinationData(MapData.randMapPoint)
        CoreActions.enterDriveNavigation()
    }

    /**
     * MSDKUI-570: Integration tests for Guidance start/stop
     */
    @Ignore("has to be fixed") // FIXME: MSDKUI-1073
    @Test
    @IntegrationUITest
    fun testForGuidanceStartStop() {
        // Check drive navigation view opened and default destination label displayed
        DriveNavigationBarActions.waitForDestinationDisplayed()
        // Check map view displayed and tap anywhere on map view
        MapActions.waitForMapViewEnabled().tapOnMap(destination)
        // Tap on tick an actionbar to confirm guidance
        DriveNavigationBarActions.waitForDestinationNotDisplayed().tapOnTickButton()
        // Tap on Start navigation to open guidance view
        DriveNavigationBarActions.waitForRouteOverView()
                .tapOnStartNavigationBtn()
                .waitForGuidanceViewDisplayed()
                .tapOnStopNavigationBtn()
        // Check that returns from guidance to Landing screen
        onPlannerBarAppNameTitleView.check(matches(isDisplayed()))
    }
}