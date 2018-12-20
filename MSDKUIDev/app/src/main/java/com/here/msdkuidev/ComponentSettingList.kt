package com.here.msdkuidev

import android.content.Intent
import android.view.View
import com.here.msdkuidev.base.BaseListActivity
import kotlinx.android.synthetic.main.activity_main.*

class ComponentSettingList : BaseListActivity() {

    companion object {
        const val ITEM = "Item"
    }

    lateinit var settingMap: Setting<*>

    override fun onStart() {
        super.onStart()
        settingMap = intent.getSerializableExtra(ComponentList.COMPONENT) as Setting<*>
        setUpList(settingMap.getItems().keys.toList(), false, itemClickListener)
    }

    private val itemClickListener = object : LandingScreenAdapter.Listener {
        override fun onItemClicked(view: View) {
            val position = landing_list.getChildLayoutPosition(view)
            val item = settingMap.getItems().values.toList()[position]
           startActivity(Intent(this@ComponentSettingList, settingMap.getClassName()).apply {
               putExtra(ITEM, item)
           })
        }
    }
}
