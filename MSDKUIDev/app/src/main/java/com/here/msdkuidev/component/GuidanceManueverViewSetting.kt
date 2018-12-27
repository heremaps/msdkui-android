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


class GuidanceManueverViewSetting() : Setting<GuidanceManeuverView>() {

    class GuidanceManueverViewSettingItem : SettingItem {

        // data customization
        var guidanceManeuverData: GuidanceManeuverData? = null
        var onlyView : Boolean? = null

        constructor() : super()

        constructor(parcel: Parcel) : super(parcel) {
            guidanceManeuverData =
                    parcel.readParcelable(GuidanceManeuverData::class.java.classLoader)
            onlyView = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeParcelable(guidanceManeuverData, flags)
            parcel.writeValue(onlyView)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<GuidanceManueverViewSettingItem> {
            override fun createFromParcel(parcel: Parcel): GuidanceManueverViewSettingItem {
                return GuidanceManueverViewSettingItem(parcel)
            }

            override fun newArray(size: Int): Array<GuidanceManueverViewSettingItem?> {
                return arrayOfNulls(size)
            }
        }

    }

    override fun getClassName(): Class<GuidanceManeuverView> {
        return GuidanceManeuverView::class.java
    }

    override fun getItems(context: Context): LinkedHashMap<String, SettingItem> {
        return linkedMapOf(
            "Default" to GuidanceManueverViewSettingItem().apply {
                onlyView = true
            },
            "null" to GuidanceManueverViewSettingItem().apply {
                onlyView = null
                guidanceManeuverData = null
            },
            "With all properties set" to GuidanceManueverViewSettingItem().apply {
                    guidanceManeuverData = GuidanceManeuverData(R.drawable.ic_maneuver_icon_2, 2000, "Info1", "Info2",
                        context.getImage(R.drawable.ic_launcher).toBitmap())
            },
            "Without maneuver icon" to GuidanceManueverViewSettingItem().apply {
                guidanceManeuverData = GuidanceManeuverData(0, 2000, "Info1", "Info2",
                    context.getImage(R.drawable.ic_launcher).toBitmap())
            },
            "Without distance" to GuidanceManueverViewSettingItem().apply {
                guidanceManeuverData = GuidanceManeuverData(0, -1, "Info1", "Info2",
                    context.getImage(R.drawable.ic_launcher).toBitmap())
            }
        )
    }


    private fun Drawable.toBitmap(): Bitmap {
        if (this is BitmapDrawable) {
            return bitmap
        }
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        return bitmap
    }

    private fun Context.getImage(id: Int): Drawable {
        return ContextCompat.getDrawable(this, id)!!
    }
}