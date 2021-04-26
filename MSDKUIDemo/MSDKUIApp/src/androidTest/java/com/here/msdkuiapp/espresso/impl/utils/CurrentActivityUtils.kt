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
import androidx.test.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage

/**
 * Utility for acquiring current activity
 */
object CurrentActivityUtils {

    /**
     * @return [Activity] current activity
     */
    val currentActivity: Activity
        get() {
            // The array is just to wrap the Activity and be able to access it from the Runnable.
            val resumedActivity = arrayOfNulls<Activity>(1)
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance()
                        .getActivitiesInStage(Stage.RESUMED)
                if (resumedActivities.iterator().hasNext()) {
                    resumedActivity[0] = resumedActivities.iterator().next() as Activity
                } else {
                    throw IllegalStateException("No Activity in stage RESUMED")
                }
            }

            // Ugly but necessary since resumedActivity[0] needs to be declared in the outer scope
            // and assigned in the runnable.
            return resumedActivity[0]!!
        }
}