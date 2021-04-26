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

package com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import com.here.msdkuiapp.R
import org.hamcrest.Matcher

/**
 * Drive Navigation & overview specific actionbar related ViewInteractions selectors
 */
object DriveNavigationBarView {

    /**
     * @return The [ViewInteraction] drive navigation title text view
     */
    val onDriveNavigationBarTitleView: ViewInteraction
        get() = onView(withText(R.string.msdkui_app_guidance_teaser_title))

    /**
     * @return The [Matcher]<[View]> driver navigation title text view on guidance
     */
    val onDriveNavigationBarTitle: Matcher<View>
        get() = withText(R.string.msdkui_app_guidance_teaser_title)

    /**
     * @return The [Matcher]<[View]> Tap the map location title text view on drive navigation
     */
    val onDriveNavigationBarLocationTitle: Matcher<View>
        get() = withText(R.string.msdkui_app_guidance_waypoint_subtitle)

    /**
     * @return The [ViewInteraction] Choose destination label on drive navigation
     */
    val onRouteOverviewBarTitle: Matcher<View>
        get() = withText(R.string.msdkui_app_route_preview_title)

    /**
     * @return The [ViewInteraction] Choose destination label on drive navigation
     */
    val onDriveNavigationBarDestinationLabel: ViewInteraction
        get() = onView(withId(R.id.selected_waypoint_label))

}