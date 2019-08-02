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

package com.here.msdkuiapp

import android.app.Activity
import androidx.fragment.app.Fragment
import android.content.Context
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.here.msdkuiapp.base.BaseActivity
import com.here.msdkuiapp.base.BaseFragmentCoordinator
import com.here.msdkuiapp.base.BasePermissionActivity
import com.here.msdkuiapp.common.AppActionBar

/**
 *  Gets support action bar.
 */
public val Activity.supportActionBar: ActionBar?
    get() = (this as? AppCompatActivity)?.supportActionBar

/**
 * Gets back button in action bar for given activity.
 */
public val Activity.acBackButton: ImageView?
    get() = supportActionBar?.customView?.findViewById(R.id.ac_back_button) as? ImageView

/**
 * Gets right button in action bar for given activity.
 */
public val Activity.acRightIcon: ImageView?
    get() = supportActionBar?.customView?.findViewById(R.id.ac_right_icon) as? ImageView

/**
 * Gets title in action bar for given activity.
 */
public val Activity.acCustomTitle: TextView?
    get() = (supportActionBar?.customView?.findViewById(R.id.ac_title) as? TextView)?.apply { hideAccessibilityExtraInfo() }

/**
 * Gets [AppActionBar] for given activity.
 */
public val Activity.appActionBar: AppActionBar?
    get() = (this as? BaseActivity)?.appActionBar

/**
 * Gets application context from context.
 */
public val Context.msdkuiApplication: MSDKUIApplication
    get() = (applicationContext as MSDKUIApplication)

/**
 * Indicates location services and permission are ready to use.
 */
public val Context.isLocationOk: Boolean
    get() = (this as? BasePermissionActivity)?.isLocationOk() ?: false

/**
 * Sets visibility of view with context reference.
 *
 * @param id id of given view.
 * @param visible true if view needs to be visible, false otherwise.
 */
public fun Context.setIdVisible(id: Int, visible: Boolean) {
    (this as? Activity)?.findViewById<View>(id)?.visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * Gets [BaseActivity] from fragment instance.
 */
public val Fragment.baseActivity: BaseActivity?
    get() = (activity as? BaseActivity)

/**
 * Gets fragment coordinator which is managing the current fragment.
 */
public val Fragment.coordinator: BaseFragmentCoordinator?
    get() = baseActivity?.coordinator

/**
 * Shows or hides progress bar for activity.
 *
 * @param visible true if progress bar need to be shown, false otherwise.
 */
public fun Activity.showProgressBar(visible: Boolean) {
    findViewById<View>(R.id.determinateBar).visibility =
            if (visible) View.VISIBLE else View.GONE
}

/**
 * Sets image resource with given id and also sets id to image tag.
 *
 * @param id id of resource that need to be set in [ImageView].
 */
public fun ImageView.setResourceWithTag(id: Int) {
    setImageResource(id)
    tag = id
}

/**
 * Removes extra text while speaking from view.
 */
public fun View.hideAccessibilityExtraInfo() {
    ViewCompat.setAccessibilityDelegate(this, object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info.addAction(AccessibilityNodeInfoCompat.ACTION_FOCUS)
        }
    })
}

/**
 * Null safety kotlin extension. This can be used to check if the object is null &
 * if null, the given block will be called.
 *
 * @param block function block to be called when object is null.
 */
fun Any?.isNull(block: () -> Unit) {
    if (this == null) {
        block()
    }
}

/**
 * Sets visibility to a group of view.
 */
fun List<View>.setVisibility(visibility: Int) {
    forEach { it.visibility = visibility }
}