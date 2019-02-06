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

package com.here.msdkuidev

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.here.msdkuidev.Constant.COMPONENT
import com.here.msdkuidev.Constant.FIX_VALUE
import com.here.msdkuidev.base.BaseListActivity
import com.here.msdkuidev.component.*
import kotlinx.android.synthetic.main.activity_main.*

class ComponentList : BaseListActivity() {

    val list = arrayListOf<Setting<*>>()

    /**
     * please add your new component here.
     */
    private fun addListContent() {
        list.add(GuidanceEstimatedArrivalSetting()) // default size
        list.add(GuidanceEstimatedArrivalSetting().apply { subTitle = FIX_VALUE }) // fix size
        list.add(GuidanceManeuverViewSetting())
        list.add(GuidanceManeuverViewSetting().apply { subTitle = FIX_VALUE })
        list.add(GuidanceNextManeuverViewSetting())
        list.add(GuidanceNextManeuverViewSetting().apply { subTitle = FIX_VALUE })
        list.add(GuidanceStreetLabelSetting())
        list.add(GuidanceStreetLabelSetting().apply { subTitle = FIX_VALUE })
        list.add(GuidanceSpeedLimitSetting())
        list.add(GuidanceSpeedLimitSetting().apply { subTitle = FIX_VALUE })
        list.add(GuidanceSpeedViewSetting())
        list.add(GuidanceSpeedViewSetting().apply { subTitle = FIX_VALUE })
        list.add(ManeuverItemViewSetting())
        list.add(ManeuverItemViewSetting().apply { subTitle = FIX_VALUE })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addListContent()
        initList()
    }

    fun initList() {
        setUpList(list.map { setting -> Pair(setting.getClassName().simpleName, setting.subTitle) }, itemClickListener)
    }

    private val itemClickListener = object : LandingScreenAdapter.Listener {
        override fun onItemClicked(view: View) {
            val position = landing_list.getChildLayoutPosition(view)
            startActivity(Intent(this@ComponentList, ComponentSettingList::class.java).apply {
                putExtra(COMPONENT, list[position])
            })
        }
    }
}
