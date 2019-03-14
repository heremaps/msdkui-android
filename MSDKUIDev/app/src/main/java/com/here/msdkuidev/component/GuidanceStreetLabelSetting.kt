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
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import com.here.msdkui.R
import com.here.msdkui.common.ThemeUtil
import com.here.msdkui.guidance.GuidanceStreetLabelData
import com.here.msdkuidev.Setting
import com.here.msdkuidev.SettingItem

class GuidanceStreetLabelSetting : Setting<GuidanceStreetLabel>() {

    class GuidanceStreetLabelSettingItem : SettingItem {

        var guidanceStreetLabelData: GuidanceStreetLabelData? = null
        var defaultView = false
        var redText = false
        var bigSize = false

        constructor() : super()

        constructor(parcel: Parcel) : super(parcel) {
            guidanceStreetLabelData =
                    parcel.readParcelable(GuidanceStreetLabelData::class.java.classLoader)
            defaultView = parcel.readValue(Boolean::class.java.classLoader) as Boolean
            redText = parcel.readValue(Boolean::class.java.classLoader) as Boolean
            bigSize = parcel.readValue(Boolean::class.java.classLoader) as Boolean
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeParcelable(guidanceStreetLabelData, flags)
            parcel.writeValue(defaultView)
            parcel.writeValue(redText)
            parcel.writeValue(bigSize)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<GuidanceStreetLabelSettingItem> {
            override fun createFromParcel(parcel: Parcel): GuidanceStreetLabelSettingItem {
                return GuidanceStreetLabelSettingItem(parcel)
            }

            override fun newArray(size: Int): Array<GuidanceStreetLabelSettingItem?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun getClassName(): Class<GuidanceStreetLabel> {
        return GuidanceStreetLabel::class.java
    }

    override fun getItems(context: Context): LinkedHashMap<String, SettingItem> {
        return linkedMapOf(
            "Default" to GuidanceStreetLabelSettingItem().apply {
                defaultView = true
            },

            "With all properties set -- background colorPositive" to GuidanceStreetLabelSettingItem().apply {
                guidanceStreetLabelData = GuidanceStreetLabelData("Street Label Text",
                    ThemeUtil.getColor(context, R.attr.colorPositive))
            },
            "With all properties set -- background colorForegroundSecondary" to GuidanceStreetLabelSettingItem().apply {
                guidanceStreetLabelData = GuidanceStreetLabelData("Street Label Text",
                    ThemeUtil.getColor(context, R.attr.colorForegroundSecondary))
            },

            "Without background and with red text color" to GuidanceStreetLabelSettingItem().apply {
                guidanceStreetLabelData = GuidanceStreetLabelData("Street Label Text", 0)
                redText = true
            },

            "With 30sp text size" to GuidanceStreetLabelSettingItem().apply {
                guidanceStreetLabelData = GuidanceStreetLabelData("Street Label Text", ThemeUtil.getColor(context, R.attr.colorPositive))
                bigSize = true
            }
        )
    }
}