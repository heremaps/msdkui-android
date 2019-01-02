package com.here.msdkuidev

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.here.msdkuidev.Constant.COMPONENT
import com.here.msdkuidev.Constant.FIX_VALUE
import com.here.msdkuidev.base.BaseListActivity
import com.here.msdkuidev.component.GuidanceEstimatedArrivalSetting
import com.here.msdkuidev.component.GuidanceManeuverViewSetting
import com.here.msdkuidev.component.GuidanceNextManeuverViewSetting
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addListContent()
    }

    override fun onStart() {
        super.onStart()
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
