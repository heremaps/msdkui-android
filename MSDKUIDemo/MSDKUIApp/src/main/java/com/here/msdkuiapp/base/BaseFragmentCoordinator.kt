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

package com.here.msdkuiapp.base

import android.app.Fragment
import android.app.FragmentManager
import com.here.msdkuiapp.common.FragmentFactory.create

/**
 * Collections of fragments utility methods to manage different fragments along with back button handling.
 */
open class BaseFragmentCoordinator(val fragmentManager: FragmentManager) {

    /**
     * Adds fragment to given id.
     *
     * @param id id of view.
     * @param fragment Fragment to be added.
     * @param backStack true if fragment needs to be added in backstack.
     */
    fun addFragment(id: Int, fragment: Fragment, backStack: Boolean) {
        val transaction = fragmentManager.beginTransaction()
        with(transaction) {
            replace(id, fragment, fragment::class.java.name)
            if (backStack) addToBackStack(fragment::class.java.name)
            commitAllowingStateLoss()
        }
    }

    /**
     * Adds fragment to given id.
     *
     * if the same fragments already exist with given id, it will return the same.
     * if there is another fragment with given id, this will replace this fragment with new one.
     *
     * @param id id of view.
     * @param fragmentClass fragment class that needs to be created.
     * @param backStack true if fragment needs to be added in backstack.
     *
     * @return created fragment.
     *
     * @see addFragment
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Fragment> addFragment(id: Int, fragmentClass: Class<T>, backStack: Boolean = false): T {
        var fragment: Fragment? = null
        getFragment(id)?.run {
            // on this id, there is already some fragment
            if (!this::class.java.isAssignableFrom(fragmentClass)) {
                // and these fragment are not same.
                if (userVisibleHint) userVisibleHint = false
                fragment = create(fragmentClass)
                // add the new one
                addFragment(id, fragment!!, backStack)
            } else {
                fragment = this
            }
        } ?: run {
            fragment = create(fragmentClass)
            //  there is fragment on given id.
            addFragment(id, fragment!!, backStack)
        }
        return fragment!! as T
    }

    /**
     * Removes fragment from the given view id.
     *
     * @param id id of view.
     */
    fun removeFragment(id: Int) {
        fragmentManager.let {
            val transaction = it.beginTransaction()
            val fr = getFragment(id)
            fr?.let {
                if (it.userVisibleHint) it.userVisibleHint = false
                transaction.remove(fr)
            }
            transaction.commitAllowingStateLoss()
        }
    }

    /**
     * Sets visibility of fragment.
     *
     * @param id id of view containing fragment
     * @param visibility true if fragment is visible, false otherwise
     */
    fun setVisible(id: Int, visibility: Boolean) {
        fragmentManager.let {
            val fragment = getFragment(id) ?: return
            val transaction = it.beginTransaction()
            if (visibility) {
                transaction.show(fragment)
                if (!fragment.userVisibleHint) fragment.userVisibleHint = true
            } else {
                transaction.hide(fragment)
                if (fragment.userVisibleHint) fragment.userVisibleHint = false
            }
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * Gets the fragment with given id.
     *
     * @param id id of view containing fragment
     */
    fun getFragment(id: Int): Fragment? {
        fragmentManager.let {
            return it.findFragmentById(id)
        }
    }

    /**
     * Handles back button press.
     */
    open fun onBackPressed(): Boolean {
        fragmentManager.let {
            if (it.backStackEntryCount > 0) {
                it.popBackStackImmediate()
                return true
            }
        }
        return false
    }
}