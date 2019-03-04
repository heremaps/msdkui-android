/*
 * Copyright (C) 2017-2019 HERE Europe B.V.
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
import android.os.Parcel
import android.os.Parcelable
import com.here.msdkui.guidance.GuidanceNextManeuverData
import com.here.msdkuidev.R
import com.here.msdkuidev.Setting
import com.here.msdkuidev.SettingItem


class GuidanceNextManeuverViewSetting() : Setting<GuidanceNextManeuverView>() {

    class GuidanceNextManeuverViewSettingItem : SettingItem {

        // data customization
        var guidanceNextManeuverData: GuidanceNextManeuverData? = null

        constructor() : super()

        constructor(parcel: Parcel) : super(parcel) {
            guidanceNextManeuverData =
                parcel.readParcelable(GuidanceNextManeuverData::class.java.classLoader)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeParcelable(guidanceNextManeuverData, flags)
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
        val info1 = "Lindenstraße 110"
        val iconId = R.drawable.ic_maneuver_icon_15
        val distance: Long = 2000
        return linkedMapOf(
            "Default view or set null data" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = null
            },
            "With all properties set" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = GuidanceNextManeuverData(iconId, distance, info1)
            },
            "With long street name" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = GuidanceNextManeuverData(iconId, distance, "Long street name but not very long street name.")
            },
            "Without maneuver icon" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = GuidanceNextManeuverData(null, distance, info1)
            },
            "Without street" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = GuidanceNextManeuverData(iconId, distance, null)
            },
            "Without distance" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = GuidanceNextManeuverData(iconId, null, info1)
            },
            "Without street, distance" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = GuidanceNextManeuverData(iconId, null, null)
            }
        )
    }
}