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

package com.here.msdkuiapp.espresso.impl.views.map.screens

import android.content.pm.ActivityInfo
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIndex
import com.here.msdkuiapp.espresso.impl.utils.ScreenOrientationUtils.Companion.getOrientation
import org.hamcrest.Matcher

/**
 * Map ViewInteractions selectors
 */
object MapView {

    /**
     * @return The [ViewInteraction] for the map view
     */
    val onMapFragmentWrapper: ViewInteraction
        get() = if (getOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            onView(withIndex(withId(R.id.mapfragment_wrapper), 0))
        else
            onView(withId(R.id.mapfragment_wrapper))

    /**
     * @return The [Matcher]<[View]> for the map view
     */
    val onMapFragmentWrapperView: Matcher<View>
        get() = if (getOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            withIndex(withId(R.id.mapfragment_wrapper), 1)
        else
            withId(R.id.mapfragment_wrapper)

}