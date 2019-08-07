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
import com.here.msdkuidev.Constant
import com.here.msdkuidev.R
import kotlinx.android.synthetic.main.guidance_next_maneuver_view.*

class GuidanceNextManeuverView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val setting =
            intent.getParcelableExtra(Constant.ITEM) as GuidanceNextManeuverViewSetting.GuidanceNextManeuverViewSettingItem
        title = setting.title.toLowerCase()
        val resourceId = if (setting.subTitle == Constant.DEFAULT) R.layout.guidance_next_maneuver_view else
            R.layout.guidance_next_maneuver_view_fix
        setContentView(resourceId)
        guidanceNextManeuverView.nextManeuverData = setting.guidanceNextManeuverData
    }
}