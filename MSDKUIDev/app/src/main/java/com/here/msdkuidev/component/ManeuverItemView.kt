/*
 * Copyright (C) 2017-2020 HERE Europe B.V.
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
import android.widget.ImageView
import android.widget.TextView
import com.here.msdkui.routing.ManeuverItemView
import com.here.msdkuidev.Constant
import com.here.msdkuidev.R
import kotlinx.android.synthetic.main.maneuver_item_view.*

class ManeuverItemView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val setting = intent.getParcelableExtra<ManeuverItemViewSetting.ManeuverItemViewSettingItem>(Constant.ITEM)!!
        setting.customTheme?.run {
            setTheme(this)
        }
        title = setting.title.toLowerCase()
        val resourceId = if (setting.subTitle == Constant.DEFAULT) R.layout.maneuver_item_view else
            R.layout.maneuver_item_view_fix
        setContentView(resourceId)
        // All code below emulates behavior of ManeuverItemView setManeuver() function.
        // If this function change, then it is very important to update this code.

        with(maneuverItemView) {
            setting.iconId?.run {
                if (this == 0) {
                    setSectionVisible(ManeuverItemView.Section.ICON, false)
                } else {
                    setSectionVisible(ManeuverItemView.Section.ICON, true)
                    findViewById<ImageView>(R.id.maneuver_icon_view).setImageResource(this)
                }
            }
            setting.instruction?.run {
                maneuverItemView.setSectionVisible(ManeuverItemView.Section.INSTRUCTIONS, true)
                findViewById<TextView>(R.id.maneuver_instruction_view).text = this
            } ?: setSectionVisible(ManeuverItemView.Section.INSTRUCTIONS, false)

            setting.address?.run {
                setSectionVisible(ManeuverItemView.Section.ADDRESS, true)
                findViewById<TextView>(R.id.maneuver_address_view).text = this
            } ?: setSectionVisible(ManeuverItemView.Section.ADDRESS, false)

            setting.distance?.run {
                setSectionVisible(ManeuverItemView.Section.DISTANCE, true)
                findViewById<TextView>(R.id.maneuver_distance_view).text = this
            } ?: setSectionVisible(ManeuverItemView.Section.DISTANCE, false)
        }
    }
}