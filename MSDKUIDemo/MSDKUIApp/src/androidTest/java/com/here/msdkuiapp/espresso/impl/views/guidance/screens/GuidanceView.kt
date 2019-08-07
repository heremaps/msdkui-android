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

package com.here.msdkuiapp.espresso.impl.views.guidance.screens

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.*
import android.view.View
import com.here.msdkuiapp.R
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

object GuidanceView {

    /**
     * The [ViewInteraction] End navigation on guidance
     */
    val onGuidanceEndNaviBtn: ViewInteraction
        get() = onView(withId(R.id.stop_navigation))

    /**
     * The [Matcher]<[View]> Follow the route on the map title text view on guidance
     */
    val onGuidanceTitle: Matcher<View>
        get() = withText(R.string.msdkui_maneuverpanel_nodata)

    /**
     * The [ViewInteraction] Distance info on guidance maneuver panel
     */
    val onGuidanceManeuverPanelDistanceInfo: ViewInteraction
        get() = onView(withId(R.id.distanceView))

    /**
     * The [ViewInteraction] Location info on guidance maneuver panel
     */
    val onGuidanceManeuverPanelLocationInfo: ViewInteraction
        get() = onView(withId(R.id.infoView2))

    /**
     * The [ViewInteraction] Maneuver icon on guidance maneuver panel
     */
    val onGuidanceManeuverPanelManeuverIcon: ViewInteraction
        get() = onView(withId(R.id.maneuverIconView))

    /**
     * The [ViewInteraction] Next maneuver panel container on guidance
     */
    val onGuidanceNextManeuverPanel: ViewInteraction
        get() = onView(withId(R.id.next_maneuver_panel_container))

    /**
     * The [ViewInteraction] Next maneuver icon on guidance next maneuver panel container
     */
    val onGuidanceNextManeuverIcon: ViewInteraction
        get() = onView(withId(R.id.nextManeuverIconView))

    /**
     * The [ViewInteraction] Next maneuver distance info on guidance next maneuver panel container
     */
    val onGuidanceNextManeuverDistanceInfo: ViewInteraction
        get() = onView(withId(R.id.nextManeuverDistance))

    /**
     * The [ViewInteraction] Next maneuver panel text separation dot
     */
    val onGuidanceNextManeuverDot: ViewInteraction
        get() = onView(allOf(withId(R.id.dot), withParent(withId(R.id.afterNextManeuverContainer))))

    /**
     * The [ViewInteraction] Next maneuver street name info on guidance next maneuver panel container
     */
    val onGuidanceNextManeuverStreetNameInfo: ViewInteraction
        get() = onView(withId(R.id.afterNextManeuverStreetName))

    /**
     * The [ViewInteraction] Current speed view on guidance dashboard
     */
    val onGuidanceDashBoardCurrentSpeed: ViewInteraction
        get() = onView(withId(R.id.guidance_current_speed))

    /**
     * The [ViewInteraction] Current speed value on guidance dashboard
     */
    val onGuidanceDashBoardCurrentSpeedValue: ViewInteraction
        get() = onView(withId(R.id.guidance_current_speed_value))

    /**
     * The [ViewInteraction] Current speed unit on guidance dashboard
     */
    val onGuidanceDashBoardCurrentSpeedUnit: ViewInteraction
        get() = onView(withId(R.id.guidance_current_speed_unit))

    /**
     * The [Matcher]<[View]> current speed limit
     */
    val onGuidanceSpeedLimiter: Matcher<View>
        get() = withId(R.id.speed_limit)

    /**
     * The [ViewInteraction] ETA info on guidance dashboard
     */
    val onGuidanceDashBoardEtaInfo: ViewInteraction
        get() = onView(withId(R.id.eta))

    /**
     * The [ViewInteraction] Duration info on guidance dashboard
     */
    val onGuidanceDashBoardDurationInfo: ViewInteraction
        get() = onView(withId(R.id.duration))

    /**
     * The [ViewInteraction] Distance info on guidance dashboard
     */
    val onGuidanceDashBoardDistanceInfo: ViewInteraction
        get() = onView(withId(R.id.distance))

    /**
     * The [ViewInteraction] Guidance dashboard view
     */
    val onGuidanceDashBoardPullLine: ViewInteraction
        get() = onView(withId(R.id.pull_line))

    /**
     * The [Matcher]<[View]> Dashboard Settings view
     */
    val onGuidanceDashBoardSettings: Matcher<View>
        get() = allOf(withText(R.string.msdkui_app_settings), withParent(withParent(withId(R.id.items_list))))

    /**
     * The [Matcher]<[View]> Dashboard About view
     */
    val onGuidanceDashBoardAbout: Matcher<View>
        get() = allOf(withText(R.string.msdkui_app_about), withParent(withParent(withId(R.id.items_list))))

    /**
     * The [ViewInteraction] Current street name info view
     */
    val onGuidanceCurrentStreetInfo: ViewInteraction
        get() = onView(withId(R.id.guidance_current_street_view))

    /**
     * The [ViewInteraction] Current street name info text view
     */
    val onGuidanceCurrentStreetInfoText: ViewInteraction
        get() = onView(withId(R.id.guidance_current_street_text))
}