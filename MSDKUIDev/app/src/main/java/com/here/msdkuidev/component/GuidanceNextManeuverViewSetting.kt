package com.here.msdkuidev.component

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import com.here.msdkui.guidance.GuidanceManeuverData
import com.here.msdkuidev.R
import com.here.msdkuidev.Setting
import com.here.msdkuidev.SettingItem
import kotlin.collections.LinkedHashMap
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import com.here.msdkui.guidance.GuidanceNextManeuverData


class GuidanceNextManeuverViewSetting() : Setting<GuidanceNextManeuverView>() {

    class GuidanceNextManeuverViewSettingItem : SettingItem {

        // data customization
        var guidanceNextManeuverData: GuidanceNextManeuverData? = null
        var defaultView : Boolean? = null

        constructor() : super()

        constructor(parcel: Parcel) : super(parcel) {
            guidanceNextManeuverData =
                    parcel.readParcelable(GuidanceNextManeuverData::class.java.classLoader)
            defaultView = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeParcelable(guidanceNextManeuverData, flags)
            parcel.writeValue(defaultView)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<GuidanceNextManeuverViewSettingItem> {
            override fun createFromParcel(parcel: Parcel): GuidanceNextManeuverViewSettingItem {
                return GuidanceNextManeuverViewSettingItem(parcel)
            }

            override fun newArray(size: Int): Array<GuidanceNextManeuverViewSettingItem?> {
                return arrayOfNulls(size)
            }
        }

    }

    override fun getClassName(): Class<GuidanceNextManeuverView> {
        return GuidanceNextManeuverView::class.java
    }

    override fun getItems(context: Context): LinkedHashMap<String, SettingItem> {
        return linkedMapOf(
            "Default" to GuidanceNextManeuverViewSettingItem().apply {
                defaultView = true
            },
            "null" to GuidanceNextManeuverViewSettingItem().apply {
                defaultView = null
                guidanceNextManeuverData = null
            },
            "With all properties set" to GuidanceNextManeuverViewSettingItem().apply {
                    guidanceNextManeuverData = GuidanceNextManeuverData(R.drawable.ic_maneuver_icon_2, 5000, "Info1")
            },
            "Without maneuver icon" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = GuidanceNextManeuverData(0, 2000, "Info1")
            },
            "Without distance" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = GuidanceNextManeuverData(R.drawable.ic_maneuver_icon_2, 0, "Info1")
            }
        )
    }
}