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

package com.here.testutils

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.here.msdkuiapp.BuildConfig
import com.here.msdkuiapp.R
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@Config(constants = BuildConfig::class, sdk = [23])
@RunWith(RobolectricTestRunner::class)
abstract class BaseTest {

    var fragmentActivity: FragmentActivity? = null

    /**
     * Gets getSupportFragmentManager.
     */
    val supportFragmentManager: FragmentManager?
        get() = fragmentActivity?.supportFragmentManager

    /**
     * Gets Application context.
     */
    val applicationContext: Context
        get() = RuntimeEnvironment.application.applicationContext

    /**
     * Gets Activity context.
     */
    val activityContext: Context?
        get() = fragmentActivity

    internal open fun setUp() {
        fragmentActivity = Robolectric.buildActivity(FragmentActivity::class.java)
                .create()
                .start()
                .resume()
                .get().apply { setTheme(R.style.MSDKUIDarkTheme) }
    }

    /**
     * Get string utility method
     */
    fun getString(id: Int): String {
        return applicationContext.getString(id)
    }

    /**
     * Add guidanceSpeedLimitFragment to the activity with given tag.
     */
    public fun addFrag(fragment: Fragment, tag: String = ""): Fragment {
        fragmentActivity!!.supportFragmentManager.run {
            beginTransaction().apply {
                add(fragment, tag)
                commit()
            }
        }
        return fragment
    }

    /**
     * Gets guidanceSpeedLimitFragment with given id.
     */
    fun getFragment(id: Int): Fragment? {
        supportFragmentManager?.run {
            return findFragmentById(id)
        }
        return null
    }

    /**
     * Helper function for checking started activity.
     *
     * @param activity currently running activity.
     * @param clazz class of activity that should start.
     */
    fun assertActivityStarted(activity: Activity, clazz: Class<out Activity>) {
        val shadowActivity = Shadows.shadowOf(activity)
        val startedIntent = shadowActivity.nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        MatcherAssert.assertThat(shadowIntent.intentClass.canonicalName, CoreMatchers.`is`(clazz.name))
    }
}