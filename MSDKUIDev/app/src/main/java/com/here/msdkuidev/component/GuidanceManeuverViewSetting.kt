/*
 * Copyright (C) 2017-2018 HERE Europe B.V.
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

package com.here.msdkuidev.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import com.here.msdkui.guidance.GuidanceManeuverData
import com.here.msdkuidev.R
import com.here.msdkuidev.Setting
import com.here.msdkuidev.SettingItem

class GuidanceManeuverViewSetting() : Setting<GuidanceManeuverView>() {

    class GuidanceManeuverViewSettingItem : SettingItem {

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

        companion object CREATOR : Parcelable.Creator<GuidanceManeuverViewSettingItem> {
            override fun createFromParcel(parcel: Parcel): GuidanceManeuverViewSettingItem {
                return GuidanceManeuverViewSettingItem(parcel)
            }

            override fun newArray(size: Int): Array<GuidanceManeuverViewSettingItem?> {
                return arrayOfNulls(size)
            }
        }

    }

    override fun getClassName(): Class<GuidanceManeuverView> {
        return GuidanceManeuverView::class.java
    }

    override fun getItems(context: Context): LinkedHashMap<String, SettingItem> {
        return linkedMapOf(
            "Default" to GuidanceManeuverViewSettingItem().apply {
                onlyView = true
            },
            "null" to GuidanceManeuverViewSettingItem().apply {
                onlyView = null
                guidanceManeuverData = null
            },
            "With all properties set" to GuidanceManeuverViewSettingItem().apply {
                    guidanceManeuverData = GuidanceManeuverData(R.drawable.ic_maneuver_icon_2, 2000, "Info1", "Info2",
                        context.getImage(R.drawable.ic_launcher).toBitmap())
            },
            "Without maneuver icon" to GuidanceManeuverViewSettingItem().apply {
                guidanceManeuverData = GuidanceManeuverData(0, 2000, "Info1", "Info2",
                    context.getImage(R.drawable.ic_launcher).toBitmap())
            },
            "Without distance" to GuidanceManeuverViewSettingItem().apply {
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