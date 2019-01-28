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

package com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import com.here.msdkuiapp.R
import org.hamcrest.Matcher

/**
 * Drive Navigation & Overview related view selectors
 */
object DriveNavigationView {

    /**
     * @return The [ViewInteraction] Start navigation on route overview
     */
    val onRouteOverviewStartNaviBtn: ViewInteraction
        get() = onView(withId(R.id.go))

    /**
     * @return The [ViewInteraction] See manoeuvres navigation on route overview
     */
    val onRouteOverviewSeeManoeuvresNaviBtn: ViewInteraction
        get() = onView(withId(R.id.see_steps))

    /**
     * @return The [ViewInteraction] route description item on route overview
     */
    val onRouteOverviewDescriptionView: ViewInteraction
        get() = onView(withId(R.id.description))

    /**
     * @return The [Matcher]<[View]> route description item view on route overview
     */
    val onRouteOverviewDescription: Matcher<View>
        get() = withId(R.id.description)

    /**
     * @return The [ViewInteraction] route destination item on route overview
     */
    val onRouteOverviewDestination: ViewInteraction
        get() = onView(withId(R.id.destination))

    /**
     * @return The [ViewInteraction] Start navigation simulation start ok button on route overview
     */
    val onRouteOverviewStartSimulationOkBtn: ViewInteraction
        get() = onView(withText(R.string.msdkui_app_ok))
}