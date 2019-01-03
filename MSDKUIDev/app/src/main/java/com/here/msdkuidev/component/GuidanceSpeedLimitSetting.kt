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
import com.here.msdkui.guidance.GuidanceSpeedData
import com.here.msdkuidev.R
import com.here.msdkuidev.Setting
import com.here.msdkuidev.SettingItem

class GuidanceSpeedLimitSetting : Setting<GuidanceSpeedLimit>() {

    class GuidanceSpeedLimitSettingItem : SettingItem {

        var guidanceSpeedData: GuidanceSpeedData? = null
        var customBackground: Int? = null
        var defaultView = false

        constructor() : super()

        constructor(parcel: Parcel) : super(parcel) {
            guidanceSpeedData =
                    parcel.readParcelable(GuidanceSpeedData::class.java.classLoader)
            customBackground = parcel.readValue(Int::class.java.classLoader) as? Int
            defaultView = parcel.readValue(Boolean::class.java.classLoader) as Boolean
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeParcelable(guidanceSpeedData, flags)
            parcel.writeValue(customBackground)
            parcel.writeValue(defaultView)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<GuidanceSpeedLimitSettingItem> {
            override fun createFromParcel(parcel: Parcel): GuidanceSpeedLimitSettingItem {
                return GuidanceSpeedLimitSettingItem(parcel)
            }

            override fun newArray(size: Int): Array<GuidanceSpeedLimitSettingItem?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun getClassName(): Class<GuidanceSpeedLimit> {
        return GuidanceSpeedLimit::class.java
    }

    override fun getItems(context: Context): LinkedHashMap<String, SettingItem> {
        return linkedMapOf(
            "Default" to GuidanceSpeedLimitSettingItem().apply {
                defaultView = true
            },
            "null" to GuidanceSpeedLimitSettingItem(),
            "With speed limit > 0" to GuidanceSpeedLimitSettingItem().apply {
                guidanceSpeedData = GuidanceSpeedData(13.89, 8.33)
            },
            "With speed limit value=0" to GuidanceSpeedLimitSettingItem().apply {
                guidanceSpeedData = GuidanceSpeedData(13.89, 0.0)
            },
            "With negative speed limit value" to GuidanceSpeedLimitSettingItem().apply {
                guidanceSpeedData = GuidanceSpeedData(13.89, -1.0)
            },
            "With custom background" to GuidanceSpeedLimitSettingItem().apply {
                guidanceSpeedData = GuidanceSpeedData(13.89, 8.33)
                customBackground = R.drawable.speed_limit_background
            }
        )
    }
}