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

package com.here.msdkuidev

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.View
import com.here.msdkuidev.Constant.COMPONENT
import com.here.msdkuidev.Constant.FIX_VALUE
import com.here.msdkuidev.base.ListFragment
import com.here.msdkuidev.component.*
import kotlinx.android.synthetic.main.main_activity.*
import androidx.viewpager.widget.ViewPager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.appcompat.app.AppCompatActivity
import com.here.msdkuidev.Constant.INDEX
import com.here.msdkuidev.Constant.MATCH_PARENT
import com.here.msdkuidev.Constant.WRAP_CONTENT

class ComponentList : AppCompatActivity(), ListFragment.Listener {

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
        setContentView(R.layout.main_activity)
        addListContent().also {
            list.sortBy { it.getClassName().simpleName }
        }
        setupViewPager(viewpager)
        tabLayout.setupWithViewPager(viewpager)
        setupTabIcons()
    }

    private fun setupTabIcons() {
        tabLayout.getTabAt(0)?.text = WRAP_CONTENT
        tabLayout.getTabAt(1)?.text = MATCH_PARENT
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(
            supportFragmentManager
        )
        val page1 = ListFragment().apply { arguments =  Bundle().apply { putInt(INDEX, 1) }}
        val page2 = ListFragment().apply { arguments =  Bundle().apply { putInt(INDEX, 2) }}
        adapter.addPage(page1)
        adapter.addPage(page2)
        viewPager.adapter = adapter
    }

    override fun getList(index: Int?): List<Pair<String, String>> {
        val list = when(index) {
            1 -> list.filter { setting -> setting.subTitle != FIX_VALUE }
            else -> list.filter { setting -> setting.subTitle == FIX_VALUE }
        }
        return list.asSequence().map { setting -> Pair(setting.getClassName().simpleName, setting.subTitle) }.toList()
    }

    override fun onItemClicked(view: View, position: Int) {
        val index = if (viewpager.currentItem % 2 == 0) position * 2 else position * 2 + 1
        startActivity(Intent(this@ComponentList, ComponentSettingList::class.java).apply {
            putExtra(COMPONENT, list[index])
        })
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList = arrayListOf<Fragment>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addPage(fragment: Fragment) {
            mFragmentList.add(fragment)
        }

        override fun getItemPosition(`object`: Any): Int {
            // Causes adapter to reload all Fragments when
            // notifyDataSetChanged is called
            notifyDataSetChanged()
            return POSITION_NONE
        }
    }
}
