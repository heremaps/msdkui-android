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

package com.here.msdkuidev.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.here.msdkui.common.ThemeUtil
import com.here.msdkuidev.Constant
import com.here.msdkuidev.R
import kotlinx.android.synthetic.main.guidance_maneuver_view_hori.*

class GuidanceManeuverView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val setting =
            intent.getParcelableExtra(Constant.ITEM) as GuidanceManeuverViewSetting.GuidanceManeuverViewSettingItem
        setting.customTheme?.run {
            setTheme(this)
        }
        title = setting.title.toLowerCase()
        val resourceId = if (setting.subTitle == Constant.DEFAULT && setting.direction == 0) {
            R.layout.guidance_maneuver_view_hori
        } else if (setting.subTitle == Constant.DEFAULT && setting.direction == 1) {
            R.layout.guidance_maneuver_view_ver
        } else if (setting.subTitle == Constant.FIX_VALUE && setting.direction == 0) {
            R.layout.guidance_maneuver_view_fix_hori
        } else {
            R.layout.guidance_maneuver_view_fix_ver
        }

        setContentView(resourceId)
        setting.defaultView ?: run {
            guidanceManeuverView.viewState = setting.state
        }
        if (setting.noDistance == true) {
            guidanceManeuverView.findViewById<TextView>(R.id.distanceView).visibility = View.GONE
        }

        if(setting.highlight  == true) {
            guidanceManeuverView.highLightManeuver(
                ThemeUtil.getColor(this, com.here.msdkui.R.attr.colorAccentLight))
        }
    }
}