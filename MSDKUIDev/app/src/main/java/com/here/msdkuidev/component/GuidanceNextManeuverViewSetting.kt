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
        var defaultView: Boolean? = null
        var withNoDistance : Boolean? = null
        var customTheme : Int ? = null

        constructor() : super()

        constructor(parcel: Parcel) : super(parcel) {
            guidanceNextManeuverData =
                    parcel.readParcelable(GuidanceNextManeuverData::class.java.classLoader)
            defaultView = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
            withNoDistance = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
            customTheme = parcel.readValue(Int::class.java.classLoader) as? Int
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeParcelable(guidanceNextManeuverData, flags)
            parcel.writeValue(defaultView)
            parcel.writeValue(withNoDistance)
            parcel.writeValue(customTheme)
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
            "Default view" to GuidanceNextManeuverViewSettingItem().apply {
                defaultView = true
            },
            "Set null data" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = null
            },
            "With all properties set" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = GuidanceNextManeuverData(R.drawable.ic_maneuver_icon_2, 5000, "Info1")
            },

            "Without maneuver icon" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = GuidanceNextManeuverData(0, 2000, "Info1")
                customTheme = R.style.GuidanceNextManeuverViewWithoutIcon
            },
            "Without distance" to GuidanceNextManeuverViewSettingItem().apply {
                guidanceNextManeuverData = GuidanceNextManeuverData(R.drawable.ic_maneuver_icon_2, 0, "Info1")
                withNoDistance = true
            }
        )
    }
}