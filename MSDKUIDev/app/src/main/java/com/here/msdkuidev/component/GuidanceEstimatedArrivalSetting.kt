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
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewData
import com.here.msdkuidev.R
import com.here.msdkuidev.Setting
import com.here.msdkuidev.SettingItem
import java.util.*

class GuidanceEstimatedArrivalSetting : Setting<GuidanceEstimatedArrival>() {

    class GuidanceEstimatedArrivalSettingItem : SettingItem {

        // data customization
        var guidanceEstimatedArrivalViewData: GuidanceEstimatedArrivalViewData? = null

        // ui customization
        var customTheme : Int ? = null
        var withoutTimeOfArrival: Boolean ? = null
        var withoutDuration: Boolean ? = null
        var withoutDistance: Boolean ? = null

        constructor() : super()

        constructor(parcel: Parcel) : super(parcel) {
            guidanceEstimatedArrivalViewData =
                    parcel.readParcelable(GuidanceEstimatedArrivalViewData::class.java.classLoader)

            customTheme = parcel.readValue(Int::class.java.classLoader) as? Int
            withoutTimeOfArrival =  parcel.readValue(Boolean::class.java.classLoader) as? Boolean
            withoutDuration =  parcel.readValue(Boolean::class.java.classLoader) as? Boolean
            withoutDistance =  parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeParcelable(guidanceEstimatedArrivalViewData, flags)
            parcel.writeValue(customTheme)
            parcel.writeValue(withoutTimeOfArrival)
            parcel.writeValue(withoutDuration)
            parcel.writeValue(withoutDistance)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<GuidanceEstimatedArrivalSettingItem> {
            override fun createFromParcel(parcel: Parcel): GuidanceEstimatedArrivalSettingItem {
                return GuidanceEstimatedArrivalSettingItem(parcel)
            }

            override fun newArray(size: Int): Array<GuidanceEstimatedArrivalSettingItem?> {
                return arrayOfNulls(size)
            }
        }

    }

    override fun getClassName(): Class<GuidanceEstimatedArrival> {
        return GuidanceEstimatedArrival::class.java
    }

    override fun getItems(context: Context): LinkedHashMap<String, SettingItem> {
        return linkedMapOf(

            "Without properties" to GuidanceEstimatedArrivalSettingItem(),

            "With all properties" to GuidanceEstimatedArrivalSettingItem().apply {
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 2000, 2000)
            },

            "Without time of arrival" to GuidanceEstimatedArrivalSettingItem().apply { //bug
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 2000, 2000)
                withoutTimeOfArrival = true
            },

            "Without duration" to GuidanceEstimatedArrivalSettingItem().apply { //bug
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 2000, 2000)
                withoutDuration = true
            },

            "Without distance" to GuidanceEstimatedArrivalSettingItem().apply { //bug
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 2000, 2000)
                withoutDistance = true
            },

            "Left aligned, red, green" to GuidanceEstimatedArrivalSettingItem().apply { //bug
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 2000, 2000)
                customTheme = R.style.GuidanceEstimatedArrivalLeftAlignedRedGreen
            },

            "Right aligned, red, green" to GuidanceEstimatedArrivalSettingItem().apply { //bug
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 2000, 2000)
                customTheme = R.style.GuidanceEstimatedArrivalRightAlignedRedGreen
            },

            "With red & big arrival time" to GuidanceEstimatedArrivalSettingItem().apply {
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 0, 2000)
                customTheme = R.style.GuidanceEstimatedArrivalTimeTheme

            },
            "With red and big duration" to GuidanceEstimatedArrivalSettingItem().apply {
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 2000, 2000)
                customTheme = R.style.GuidanceEstimatedArrivalDurationTheme
            }
        )
    }

}