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

package com.here.msdkuiapp.espresso.impl.views.route.screens

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import com.here.msdkuiapp.R
import org.hamcrest.Matcher

/**
 * Route overview specific actionbar related ViewInteractions selectors
 */
object RouteBarView {

    /**
     * @return The [ViewInteraction] route back button on route overview
     */
    val onRouteBarBackImageIcon: ViewInteraction
        get() = onView(withId(R.id.ac_back_button))

    /**
     * @return The [Matcher]<[View]> route overview title text view on action bar
     */
    val onRouteBarTitle: Matcher<View>
        get() = withText(R.string.msdkui_app_route_preview_title)

}