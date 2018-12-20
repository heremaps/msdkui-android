package com.here.msdkuidev

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.here.msdkuidev.base.BaseListActivity
import com.here.msdkuidev.component.GuidanceEstimatedArrival
import kotlinx.android.synthetic.main.activity_main.*

class ComponentList : BaseListActivity() {

    companion object {
        const val COMPONENT = "Component"
    }

    val list = arrayListOf<Setting<*>>()

    /**
     * please add your new component here.
     */
    private fun addListContent() {
        list.add(GuidanceEstimatedArrival() as Setting<*>)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addListContent()
    }

    override fun onStart() {
        super.onStart()
        setUpList(list.map { setting -> setting.getClassName().simpleName },true, itemClickListener)
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
