package com.here.msdkuidev.component

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.here.msdkuidev.Constant
import com.here.msdkuidev.R
import kotlinx.android.synthetic.main.guidance_maneuver_view.*
import kotlinx.android.synthetic.main.guidance_next_maneuver_view.*

class GuidanceNextManeuverView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val setting = intent.getParcelableExtra(Constant.ITEM) as GuidanceNextManeuverViewSetting.GuidanceNextManeuverViewSettingItem
        val resourceId = if(setting.subTitle == Constant.DEFAULT) R.layout.guidance_next_maneuver_view else
            R.layout.guidance_next_maneuver_view_fix
        setContentView(resourceId)
        setting.defaultView ?: run {
            guidanceNextManeuverView.nextManeuverData = setting.guidanceNextManeuverData
        }
    }
}