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

package com.here.msdkuiapp.espresso.impl.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage.RESUMED
import android.view.View
import android.view.ViewGroup
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ScreenOrientation
import org.hamcrest.Matcher

/**
 * Espresso ViewAction for the changing of the screen orientation
 */
class ScreenOrientationUtils(private val orientation: Int) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return isRoot()
    }

    override fun getDescription(): String {
        return String.format("change value to: - [%s]", orientation)
    }

    override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadUntilIdle()
        val activity = getActivity(view.context)
        activity?.run {
            requestedOrientation = orientation
        }?: run {
            getActivity(view)!!.requestedOrientation = orientation
        }
        val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED)
        if (resumedActivities.isEmpty()) {
            throw RuntimeException("Could not change value")
        }
    }

    companion object {
        /**
         * Set screen orientation value of the current activity
         * @return value [ViewAction]
         */
        fun setOrientation(orientation: ScreenOrientation): ViewAction {
            return ScreenOrientationUtils(orientation.value)
        }

        /**
         * Get current screen orientation value of the activity
         * @return value [Int]
         */
        fun getOrientation(): Int? {
            return InstrumentationRegistry.getTargetContext().resources.configuration.orientation
        }
    }

    private fun getActivity(context: Context): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    private fun getActivity(view: View): Activity? {
        var activity: Activity? = null
        val v = view as ViewGroup
        val c = v.childCount
        var i = 0
        while (i < c && activity == null) {
            activity = getActivity(v.getChildAt(i).context)
            ++i
        }
        return activity
    }
}