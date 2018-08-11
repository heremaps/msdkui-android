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
import com.here.msdkuiapp.SplashActivity
import com.here.msdkuiapp.espresso.impl.annotation.FunctionalUITest
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ScreenOrientation
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ScreenOrientation.PORTRAIT
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.matchers.DriveNavigationBarMatchers
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationBarView.onDriveNavigationBarTitleView
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions.DriveNavigationBarActions
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.utils.DestinationData
import com.here.msdkuiapp.espresso.impl.views.map.screens.MapView.onMapFragmentWrapper
import com.here.msdkuiapp.espresso.impl.views.map.useractions.MapActions
import com.here.msdkuiapp.espresso.impl.views.map.utils.MapData
import com.here.msdkuiapp.espresso.tests.TestBase
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.Ignore
import org.junit.runners.MethodSorters

/**
 * UI flow tests for guidance
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GuidanceFlowTests: TestBase<SplashActivity>(SplashActivity::class.java)  {

    private lateinit var destination: DestinationData

    @Before
    fun prepare() {
        destination = DestinationData(MapData.randMapPoint)
        CoreActions.enterDriveNavigation().changeOrientation(PORTRAIT)
    }

    @After
    fun tearDown() {
        CoreActions.changeOrientation(PORTRAIT)
    }

    /**
     * MSDKUI-573: Select Drive Navigation destination via tap
     */
    @Ignore("has to be fixed") // FIXME: MSDKUI-1073
    @Test
    @FunctionalUITest
    fun testSelectDriveNavigationDestinationViaTap() {
        enumValues<ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions.changeOrientation(it)
            // Check drive navigation view opened
            DriveNavigationBarActions.waitForDriveNavigationView()
            // Check drive navigation view title
            onDriveNavigationBarTitleView.check(matches(isDisplayed()))
            // Check map view displayed
            onMapFragmentWrapper.check(matches(isDisplayed()))
            // Check default waypoint items labels text and default transportation mode
            DriveNavigationBarMatchers.checkDestinationDefaultLabel()
            // Tap anywhere on map view
            MapActions.waitForMapViewEnabled().tapOnMap(destination)
            // Tap on tick to confirm the first waypoint selection
            DriveNavigationBarActions.waitForDestinationNotDisplayed()
            // Returns back to Landing screen and selects Drive Navigation
            CoreActions.changeOrientation(PORTRAIT).pressBackButton().enterDriveNavigation()
        }
    }

    /**
     * MSDKUI-574: Select Drive Navigation destination via long press
     */
    @Ignore("has to be fixed") // FIXME: MSDKUI-1073
    @Test
    @FunctionalUITest
    fun testSelectDriveNavigationDestinationViaLongPress() {
        enumValues<ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions.changeOrientation(it)
            // Check drive navigation view opened
            DriveNavigationBarActions.waitForDriveNavigationView()
            // Check drive navigation view title
            onDriveNavigationBarTitleView.check(matches(isDisplayed()))
            // Check map view displayed
            onMapFragmentWrapper.check(matches(isDisplayed()))
            // Check default waypoint items labels text and default transportation mode
            DriveNavigationBarMatchers.checkDestinationDefaultLabel()
            // Tap anywhere on map view
            MapActions.waitForMapViewEnabled().longPressOnMap(destination)
            // Tap on tick to confirm the first waypoint selection
            DriveNavigationBarActions.waitForDestinationNotDisplayed()
            // Returns back to Landing screen and selects Drive Navigation
            CoreActions.pressBackButton().changeOrientation(PORTRAIT).enterDriveNavigation()
        }
    }
}