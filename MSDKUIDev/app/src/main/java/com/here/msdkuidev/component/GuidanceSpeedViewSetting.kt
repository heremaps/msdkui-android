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
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import com.here.msdkui.common.measurements.UnitSystem
import com.here.msdkui.guidance.GuidanceSpeedData
import com.here.msdkuidev.R
import com.here.msdkuidev.Setting
import com.here.msdkuidev.SettingItem

class GuidanceSpeedViewSetting : Setting<GuidanceSpeedView>() {

    class GuidanceSpeedViewSettingItem : SettingItem {

        var guidanceSpeedData: GuidanceSpeedData? = null
        var customBackground: Int? = null
        var defaultView = false
        var color: Int? = null
        var unit : UnitSystem = UnitSystem.METRIC

        constructor() : super()

        constructor(parcel: Parcel) : super(parcel) {
            guidanceSpeedData = parcel.readParcelable(GuidanceSpeedData::class.java.classLoader)
            customBackground = parcel.readValue(Int::class.java.classLoader) as? Int
            defaultView = parcel.readByte() != 0.toByte()
            color = parcel.readValue(Int::class.java.classLoader) as? Int
            unit = parcel.readSerializable() as UnitSystem
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeParcelable(guidanceSpeedData, flags)
            parcel.writeValue(customBackground)
            parcel.writeByte(if (defaultView) 1 else 0)
            parcel.writeValue(color)
            parcel.writeSerializable(unit)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<GuidanceSpeedViewSettingItem> {
            override fun createFromParcel(parcel: Parcel): GuidanceSpeedViewSettingItem {
                return GuidanceSpeedViewSettingItem(parcel)
            }

            override fun newArray(size: Int): Array<GuidanceSpeedViewSettingItem?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun getClassName(): Class<GuidanceSpeedView> {
        return GuidanceSpeedView::class.java
    }

    override fun getItems(context: Context): LinkedHashMap<String, SettingItem> {
        return linkedMapOf(
            "Default" to GuidanceSpeedViewSettingItem().apply {
                defaultView = true
            },
            "null" to GuidanceSpeedViewSettingItem(),
            "With speed > 0" to GuidanceSpeedViewSettingItem().apply {
                guidanceSpeedData = GuidanceSpeedData(13.0, 8.0)
            },
            "With speed value=0" to GuidanceSpeedViewSettingItem().apply {
                guidanceSpeedData = GuidanceSpeedData(0.0, 20.0)
            },
            "With negative speed value" to GuidanceSpeedViewSettingItem().apply {
                guidanceSpeedData = GuidanceSpeedData(-1.0, 12.0)
            },
            "km/h, red" to GuidanceSpeedViewSettingItem().apply {
                guidanceSpeedData = GuidanceSpeedData(13.0, 8.0)
                color = Color.RED
            },
            "mph, blue" to GuidanceSpeedViewSettingItem().apply {
                guidanceSpeedData = GuidanceSpeedData(13.0, 8.0)
                unit = UnitSystem.IMPERIAL_UK
                color = Color.BLUE
            },
            "With custom background" to GuidanceSpeedViewSettingItem().apply {
                guidanceSpeedData = GuidanceSpeedData(13.0, 8.0)
                customBackground = R.drawable.current_speed_view_bg
            }
        )
    }
}