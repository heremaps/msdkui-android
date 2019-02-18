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

package com.here.msdkuiapp.espresso.impl.views.guidance.useractions

import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.TIMEOUT_WAIT_TRIPLE_MILLIS
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.viewIsDisplayed
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForTextChange
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceDashBoardDistanceInfo
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceDashBoardDurationInfo
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceDashBoardEtaInfo
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceEndNaviBtn
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceManueverPanelLocationInfo
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceManeuverPanelDistanceInfo
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceManeuverPanelManueverIcon
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceNextManeuverDistanceInfo
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceNextManeuverIcon
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceNextManeuverPanel
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceNextManeuverStreetNameInfo
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceSpeedLimiter
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceTitle

object GuidanceActions {

    /**
     * Tap on stop navigation button to return to landing screen
     */
    fun tapOnStopNavigationBtn(): GuidanceActions {
        onGuidanceEndNaviBtn.check(matches(isDisplayed())).perform(click())
        return this
    }

    /**
     * Wait for guidance view displayed on action bar
     */
    fun waitForGuidanceViewDisplayed(): GuidanceActions {
        onRootView.perform(waitForCondition(onGuidanceTitle))
        return this
    }

    /**
     * Wait for next maneuver panel to be displayed
     */
    fun waitForGuidanceNextManeuverPanelDisplayed(): GuidanceActions {
        onRootView.perform(waitForCondition(withId(R.id.next_maneuver_panel_container)))
        return this
    }

    /**
     * Wait for guidance speed limit to be displayed
     */
    fun waitForSpeedLimitDisplayed(): GuidanceActions {
        waitForCondition(onGuidanceSpeedLimiter)
        return this
    }

    /**
     * Wait for next maneuver street name to change
     * Triple timeout is used, as it can take up to 3 minutes for maneuver to change
     */
    fun waitForGuidanceNextManeuverChanged(currentStreetName: String): GuidanceActions {
        onRootView.perform(waitForTextChange(withId(R.id.afterNextManeuverStreetName), currentStreetName, TIMEOUT_WAIT_TRIPLE_MILLIS))
        return this
    }

    /**
     * Check manuever panel on guidance
     */
    fun checkGuidanceManeuverPanelInfo(): GuidanceActions {
        onGuidanceManeuverPanelDistanceInfo.check(matches(isDisplayed()))
        onGuidanceManueverPanelLocationInfo.check(matches(isDisplayed()))
        onGuidanceManeuverPanelManueverIcon.check(matches(isDisplayed()))
        if(viewIsDisplayed(onGuidanceNextManeuverPanel)) {
            onGuidanceNextManeuverDistanceInfo.check(matches(isDisplayed()))
            onGuidanceNextManeuverIcon.check(matches(isDisplayed()))
            onGuidanceNextManeuverStreetNameInfo.check(matches(isDisplayed()))
        }
        return this
    }

    /**
     * Check dashboard on guidance
     */
    fun checkGuidanceDashBoardInfo(): GuidanceActions {
        // FIXME:MSDKUI-1816
        // onGuidanceDashBoardCurrentSpeedValue.check(matches(isDisplayed()))
        // onGuidanceDashBoardCurrentSpeedUnit.check(matches(isDisplayed()))
        onGuidanceDashBoardEtaInfo.check(matches(isDisplayed()))
        onGuidanceDashBoardDistanceInfo.check(matches(isDisplayed()))
        onGuidanceDashBoardDurationInfo.check(matches(isDisplayed()))
        onGuidanceEndNaviBtn.check(matches(isDisplayed()))
        return this
    }
}