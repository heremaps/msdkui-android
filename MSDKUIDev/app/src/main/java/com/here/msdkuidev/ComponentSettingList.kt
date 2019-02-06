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
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.here.msdkuidev.Constant.COMPONENT
import com.here.msdkuidev.Constant.ITEM
import com.here.msdkuidev.base.ListFragment

class ComponentSettingList : AppCompatActivity(), ListFragment.Listener {

    private lateinit var settingMap: Setting<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingMap = intent.getSerializableExtra(COMPONENT) as Setting<*>
        setContentView(R.layout.frame_layout)
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, ListFragment()).commit()
        title = settingMap.getClassName().simpleName
    }

    override fun getList(index: Int?): List<Pair<String, String>> {
        return settingMap.getItems(this@ComponentSettingList).keys.map { key-> Pair(key, "") }
    }

    override fun onItemClicked(view: View, position: Int) {
        val item = settingMap.getItems(this@ComponentSettingList).values.toList()[position].apply {
            subTitle = settingMap.subTitle
            title = settingMap.getItems(this@ComponentSettingList).keys.toList()[position]
        }
        startActivity(Intent(this@ComponentSettingList, settingMap.getClassName()).apply {
            putExtra(ITEM, item)
        })
    }
}
