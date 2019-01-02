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

package com.here.msdkuidev

import android.content.Intent
import android.view.View
import com.here.msdkuidev.Constant.COMPONENT
import com.here.msdkuidev.Constant.ITEM
import com.here.msdkuidev.base.BaseListActivity
import kotlinx.android.synthetic.main.activity_main.*

class ComponentSettingList : BaseListActivity() {

    lateinit var settingMap: Setting<*>

    override fun onStart() {
        super.onStart()
        settingMap = intent.getSerializableExtra(COMPONENT) as Setting<*>
        setUpList(settingMap.getItems(this@ComponentSettingList).keys.map { key-> Pair(key, "") }, itemClickListener)
    }

    private val itemClickListener = object : LandingScreenAdapter.Listener {
        override fun onItemClicked(view: View) {
            val position = landing_list.getChildLayoutPosition(view)
            val item = settingMap.getItems(this@ComponentSettingList).values.toList()[position]
            item.subTitle = settingMap.subTitle
           startActivity(Intent(this@ComponentSettingList, settingMap.getClassName()).apply {
               putExtra(ITEM, item)
           })
        }
    }
}
