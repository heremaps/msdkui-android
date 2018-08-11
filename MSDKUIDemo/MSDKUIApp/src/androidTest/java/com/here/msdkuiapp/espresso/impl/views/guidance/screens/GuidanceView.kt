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

package com.here.msdkuiapp.espresso.impl.views.guidance.screens

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import com.here.msdkuiapp.R
import org.hamcrest.Matcher

object GuidanceView {

    /**
     * @return The [ViewInteraction] End navigation on guidance
     */
    val onGuidanceEndNaviBtn: ViewInteraction
        get() = onView(ViewMatchers.withId(R.id.stop_navigation))

    /**
     * @return The [Matcher]<[View]> Follow the route on the map title text view on guidance
     */
    val onGuidanceTitle: Matcher<View>
        get() = withText(R.string.msdkui_maneuverpanel_nodata)
}