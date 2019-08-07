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
import android.view.Gravity
import android.view.View.TEXT_ALIGNMENT_TEXT_END
import android.view.View.TEXT_ALIGNMENT_TEXT_START
import android.widget.TextView
import com.here.msdkuidev.Constant
import com.here.msdkuidev.R
import kotlinx.android.synthetic.main.guidance_current_speed.*

class GuidanceSpeedView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val setting = intent.getParcelableExtra(Constant.ITEM) as GuidanceSpeedViewSetting.GuidanceSpeedViewSettingItem
        val resourceId = if (setting.subTitle == Constant.DEFAULT) R.layout.guidance_current_speed else
            R.layout.guidance_current_speed_fix
        title = setting.title.toLowerCase()
        setContentView(resourceId)
        if (!setting.defaultView) {
            setting.unit.run {
                guidanceSpeedView.unitSystem = this
            }
            guidanceSpeedView.setCurrentSpeedData(setting.guidanceSpeedData)
            setting.customBackground?.run {
                guidanceSpeedView.setBackgroundResource(this)
            }
            setting.color?.run {
                guidanceSpeedView.unitTextColor = this
                guidanceSpeedView.valueTextColor = this
            }
            setting.alignment?.run {
                guidanceSpeedView.findViewById<TextView> (R.id.guidance_current_speed_value).textAlignment = if( this == 0)
                    TEXT_ALIGNMENT_TEXT_START else TEXT_ALIGNMENT_TEXT_END
            }
        }
    }
}