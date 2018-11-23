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
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import com.here.msdkuiapp.R
import org.hamcrest.Matcher

object GuidanceView {

    /**
     * @return The [ViewInteraction] End navigation on guidance
     */
    val onGuidanceEndNaviBtn: ViewInteraction
        get() = onView(withId(R.id.stop_navigation))

    /**
     * @return The [Matcher]<[View]> Follow the route on the map title text view on guidance
     */
    val onGuidanceTitle: Matcher<View>
        get() = withText(R.string.msdkui_maneuverpanel_nodata)

    /**
     * @return The [ViewInteraction] Distance info on guidance maneuver panel
     */
    val onGuidanceManeuverPanelDistanceInfo: ViewInteraction
        get() = onView(withId(R.id.distanceView))

    /**
     * @return The [ViewInteraction] Location info on guidance maneuver panel
     */
    val onGuidanceManueverPanelLocationInfo: ViewInteraction
        get() = onView(withId(R.id.infoView2))

    /**
     * @return The [ViewInteraction] Manuever icon on guidance maneuver panel
     */
    val onGuidanceManeuverPanelManueverIcon: ViewInteraction
        get() = onView(withId(R.id.maneuverIconView))

    /**
     * @return The [ViewInteraction] Next maneuver panel container on guidance
     */
    val onGuidanceNextManeuverPanel: ViewInteraction
        get() = onView(withId(R.id.next_maneuver_panel_container))

    /**
     * @return The [ViewInteraction] Next maneuver icon on guidance next maneuver panel container
     */
    val onGuidanceNextManeuverIcon: ViewInteraction
        get() = onView(withId(R.id.nextManeuverIconView))

    /**
     * @return The [ViewInteraction] Next maneuver distance info on guidance next maneuver panel container
     */
    val onGuidanceNextManeuverDistanceInfo: ViewInteraction
        get() = onView(withId(R.id.nextManeuverDistance))

    /**
     * @return The [ViewInteraction] Next maneuver street name info on guidance next maneuver panel container
     */
    val onGuidanceNextManeuverStreetNameInfo: ViewInteraction
        get() = onView(withId(R.id.afterNextManeuverStreetName))

    /**
     * @return The [ViewInteraction] Current speed value on guidance dashboard
     */
    val onGuidanceDashBoardCurrentSpeedValue: ViewInteraction
        get() = onView(withId(R.id.guidance_current_speed_value))

    /**
     * @return The [ViewInteraction] Current speed unit on guidance dashboard
     */
    val onGuidanceDashBoardCurrentSpeedUnit: ViewInteraction
        get() = onView(withId(R.id.guidance_current_speed_unit))

    /**
     * @return The [ViewInteraction] ETA info on guidance dashboard
     */
    val onGuidanceDashBoardEtaInfo: ViewInteraction
        get() = onView(withId(R.id.eta))

    /**
     * @return The [ViewInteraction] Duration info on guidance dashboard
     */
    val onGuidanceDashBoardDurationInfo: ViewInteraction
        get() = onView(withId(R.id.duration))

    /**
     * @return The [ViewInteraction] Distance info on guidance dashboard
     */
    val onGuidanceDashBoardDistanceInfo: ViewInteraction
        get() = onView(withId(R.id.distance))
}