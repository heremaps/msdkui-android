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

package com.here.msdkuiapp.common

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.here.msdkuiapp.*
import com.here.msdkuiapp.about.AboutActivity

/**
 * Action bar customized for MSDKUI.
 */
class AppActionBar(val activity: Activity) {

    /**
     * Set up the default action bar.
     */
    fun setUpActionBar(): AppActionBar {
        activity.supportActionBar?.run {
            elevation = 0f
            setDisplayShowHomeEnabled(false)
            setDisplayShowTitleEnabled(false)
            setDisplayShowCustomEnabled(true)
            customView = activity.layoutInflater.inflate(R.layout.home_actionbar,
                    activity.findViewById(android.R.id.content), false)
            (customView?.parent as? Toolbar)?.setContentInsetsAbsolute(0, 0)
        }
        setBack()
        setTitle()
        setRightIcon()
        return this
    }

    /**
     * Sets action bar back with given options.
     *
     * @param visible if back button should be visible.
     * @param id id of drawable for back button.
     * @param clickListener listener block for back button.
     */
    fun setBack(visible: Boolean = false,
                id: Int = R.drawable.ic_arrow_back_black_24dp,
                clickListener: (() -> Unit)? = null) {
        val backBtn = activity.supportActionBar?.customView?.findViewById(R.id.ac_back_button) as? ImageView
        backBtn?.run {
            visibility = if (visible) View.VISIBLE else View.GONE
            setResourceWithTag(id)
            contentDescription = activity.getString(R.string.msdkui_app_back)
            setOnClickListener {
                clickListener?.run {
                    invoke()
                } ?: activity.onBackPressed()
            }
        }
    }

    /**
     * Sets action bar title with given options.
     *
     * @param visible if back button should be visible.
     * @param value text for title.
     * @param accessibilityShowExtra  false if you want to hear extra text for accessibility, true otherwise.
     */
    fun setTitle(visible: Boolean = true, value: String = activity.getString(R.string.msdkui_app_name_title),
                 accessibilityShowExtra: Boolean = false) {
        val title = activity.supportActionBar?.customView?.findViewById(R.id.ac_title) as? TextView
        title?.run {
            visibility = if (visible) View.VISIBLE else View.GONE
            text = value
            if (!accessibilityShowExtra) hideAccessibilityExtraInfo()
        }
    }

    /**
     * Sets right icon with given options.
     *
     * @param visible if back button should be visible.
     * @param id id of drawable for back button.
     * @param clickListener listener block for back button.
     */
    fun setRightIcon(visible: Boolean = true,
                     id: Int = R.drawable.ic_info_outline_black_24dp,
                     accessibleValue: String = activity.getString(R.string.msdkui_app_info),
                     clickListener: (() -> Unit)? = null) {
        val rightIcon = activity.supportActionBar?.customView?.findViewById(R.id.ac_right_icon) as? ImageView
        rightIcon?.run {
            visibility = if (visible) View.VISIBLE else View.GONE
            if (!visible) return
            contentDescription = accessibleValue
            setResourceWithTag(id)
            setOnClickListener {
                clickListener?.run {
                    invoke()
                } ?: showAppInfo()
            }
        }
    }

    /**
     * Shows application information.
     */
    private fun showAppInfo() {
        activity.startActivity(Intent(activity, AboutActivity::class.java))
    }
}