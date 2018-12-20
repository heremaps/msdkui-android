package com.here.msdkuidev.component

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.here.msdkuidev.*
import com.here.msdkuidev.Constant.ITEM
import kotlinx.android.synthetic.main.guidance_estimated_arrival.*
import java.util.*
import kotlin.collections.LinkedHashMap

class GuidanceEstimatedArrival : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val setting = intent.getParcelableExtra(ITEM) as GuidanceEstimatedArrivalSetting.GuidanceEstimatedArrivalSettingItem
        val resourceId = if(setting.subTitle == Constant.DEFAULT) R.layout.guidance_estimated_arrival else
            R.layout.guidance_estimated_arrival_fix
        setContentView(resourceId)
        guidanceEstimatedArrivalView.estimatedArrivalData = setting.guidanceEstimatedArrivalViewData
    }
}