/*
 * Copyright (C) 2017-2020 HERE Europe B.V.
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

package com.here.msdkuiapp.espresso.impl.core

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.*
import android.view.View
import com.here.msdkuiapp.R
import org.hamcrest.Matcher

/**
 * Core ViewInteractions framework selectors
 */
object CoreView {

    /**
     * @return a matcher that matches root [Matcher]<[View]>
     */
    val onRootView: ViewInteraction
        get() = onView(isRoot())

    /**
     * @return The [ViewInteraction] for the landing list view.
     */
    val onLandingScreenList: ViewInteraction
        get() = onView(withId(R.id.landing_list))

    /**
     * @return The [ViewInteraction] route planner app name title text view on action bar
     */
    val onPlannerBarAppNameTitleView: ViewInteraction
        get() = onView(withText(R.string.msdkui_app_name_title))

    /**
     * @return The [ViewInteraction] route planner heading text view on landing screen
     */
    val onLandingScreenRoutePlannerView : ViewInteraction
        get() = onView(withText(R.string.msdkui_app_rp_teaser_title))

    /**
     * @return The [ViewInteraction] navigation driver heading text view on landing screen
     */
    val onLandingScreenDriverNavigationView: ViewInteraction
        get() = onView(withText(R.string.msdkui_app_guidance_teaser_title))

    /**
    * @return The [Matcher]<[View]> Drive navigation icon on landing screen
    */
    val onLandingScreenDriverNavigationViewIcon: Matcher<View>
        get() = withId(R.id.ls_icon)

}