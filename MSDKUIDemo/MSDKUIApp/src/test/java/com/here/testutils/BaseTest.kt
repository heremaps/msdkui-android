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

import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.support.v4.app.FragmentActivity
import com.here.msdkuiapp.BuildConfig
import com.here.msdkuiapp.R
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@Config(constants = BuildConfig::class, sdk = [23])
@RunWith(RobolectricTestRunner::class)
abstract class BaseTest {

    var fragmentActivity: FragmentActivity? = null

    /**
     * Gets FragmentManager.
     */
    val fragmentManager: FragmentManager?
        get() = fragmentActivity?.fragmentManager

    /**
     * Gets getSupportFragmentManager.
     */
    val supportFragmentManager: android.support.v4.app.FragmentManager?
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
     * Add fragment to the activity with given tag.
     */
    public fun addFrag(fragment: Fragment, tag: String = "") {
        fragmentActivity!!.fragmentManager.run {
            beginTransaction().apply {
                add(fragment, tag)
                commit()
            }
        }
    }

    /**
     * Gets fragment with given id.
     */
    fun getFragment(id: Int): Fragment? {
        fragmentManager?.run {
            return findFragmentById(id)
        }
        return null
    }
}