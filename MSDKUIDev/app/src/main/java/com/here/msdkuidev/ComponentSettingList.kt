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
