package com.here.msdkuidev.component

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.here.msdkuidev.ComponentSettingList
import com.here.msdkuidev.R
import com.here.msdkuidev.Setting
import com.here.msdkuidev.SettingItem
import java.util.*
import kotlin.collections.LinkedHashMap

class GuidanceEstimatedArrival : AppCompatActivity(),
    Setting<GuidanceEstimatedArrival> {

    override fun getClassName(): Class<GuidanceEstimatedArrival> {
        return GuidanceEstimatedArrival::class.java
    }

    override fun getItems(): LinkedHashMap<String, SettingItem> {
        return linkedMapOf(
            "Default" to GuidanceEstimatedArrivalSettingItem(),
            "With all properties set" to GuidanceEstimatedArrivalSettingItem(),
            "Without time of arrival" to GuidanceEstimatedArrivalSettingItem()
        // and more
        )
    }

    class GuidanceEstimatedArrivalSettingItem : SettingItem() {
        var estimatedTimeOfArrival: Date? = null
        var primaryInfoTextColor: Int? = null
        var secondaryInfoTextColor: Int? = null
        // more settings
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guidance_estimated_arrival)
        val setting = intent.getSerializableExtra(ComponentSettingList.ITEM)
        //apply setting to view.

    }
}