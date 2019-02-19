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
import com.here.msdkuidev.R
import com.here.msdkuidev.Setting
import com.here.msdkuidev.SettingItem

class ManeuverItemViewSetting() : Setting<ManeuverItemView>() {

    class ManeuverItemViewSettingItem : SettingItem {

        var iconId: Int? = null
        var instruction: String? = null
        var address : String? = null
        var distance : String? = null
        var customTheme : Int? = null

        constructor() : super()

        constructor(parcel: Parcel) : super(parcel) {
            iconId = parcel.readValue(Int::class.java.classLoader) as? Int
            instruction = parcel.readValue(Boolean::class.java.classLoader) as? String
            address = parcel.readValue(Boolean::class.java.classLoader) as? String
            distance = parcel.readValue(Boolean::class.java.classLoader) as? String
            customTheme = parcel.readValue(Int::class.java.classLoader) as? Int
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeValue(iconId)
            parcel.writeValue(instruction)
            parcel.writeValue(address)
            parcel.writeValue(distance)
            parcel.writeValue(customTheme)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ManeuverItemViewSettingItem> {
            override fun createFromParcel(parcel: Parcel): ManeuverItemViewSettingItem {
                return ManeuverItemViewSettingItem(parcel)
            }

            override fun newArray(size: Int): Array<ManeuverItemViewSettingItem?> {
                return arrayOfNulls(size)
            }
        }

    }

    override fun getClassName(): Class<ManeuverItemView> {
        return ManeuverItemView::class.java
    }

    override fun getItems(context: Context): LinkedHashMap<String, SettingItem> {
        return linkedMapOf(
            "Without any value and all sections visible" to ManeuverItemViewSettingItem().apply {
                iconId = 0
                instruction = null
                address = null
            },
            "With all values (short instruction)" to ManeuverItemViewSettingItem().apply {
                iconId = R.drawable.ic_maneuver_icon_12
                instruction = "Short instruction!"
                address = "Fuubarstrasse"
                distance = "10 km"
            },
            "With all values (red, green, yellow)" to ManeuverItemViewSettingItem().apply {
                iconId = R.drawable.ic_maneuver_icon_12
                instruction = "Short instruction!"
                address = "Fuubarstrasse"
                distance = "10 km"
                customTheme = R.style.ManeuverItemRedGreenYellow
            },
            "With all values (long instruction)" to ManeuverItemViewSettingItem().apply {
                iconId = R.drawable.ic_maneuver_icon_12
                instruction = "This is a very very very long instruction!"
                address = "Fuubarstrasse"
                distance = "10 km"
            },
            "Only instructions" to ManeuverItemViewSettingItem().apply {
                iconId = 0
                instruction = "Short instruction!"
                address = null
                distance = null
                customTheme = R.style.ManeuverItemVisibleSectionInstructions
            },

            "Only address" to ManeuverItemViewSettingItem().apply {
                iconId = 0
                instruction = null
                address = "Fuubarstrasse"
                distance = null
                customTheme = R.style.ManeuverItemVisibleSectionAddress
            },

            "Without icon" to ManeuverItemViewSettingItem().apply {
                iconId = 0
                instruction = "Short instruction!"
                address = "Fuubarstrasse"
                distance = "10 km"
            },

            "Without instructions" to ManeuverItemViewSettingItem().apply {
                iconId = R.drawable.ic_maneuver_icon_12
                instruction = null
                address = "Fuubarstrasse"
                distance = "10 km"
            },

            "Without address" to ManeuverItemViewSettingItem().apply {
                iconId = R.drawable.ic_maneuver_icon_12
                instruction = "Short instruction!"
                address = null
                distance = "10 km"
            },

            "Without distance" to ManeuverItemViewSettingItem().apply {
                iconId = R.drawable.ic_maneuver_icon_12
                instruction = "Short instruction!"
                address = "Fuubarstrasse"
                distance = null
            },

            "Without address and distance" to ManeuverItemViewSettingItem().apply {
                iconId = R.drawable.ic_maneuver_icon_12
                instruction = "Short instruction!"
                address = null
                distance = null
            },

            "With all values (short instruction, long address)" to ManeuverItemViewSettingItem().apply {
                iconId = R.drawable.ic_maneuver_icon_12
                instruction = "Short instruction!"
                address = "Fuubarstrasse, more data to show here a lot of data."
                distance = "10 km"
            },
            "With all values (long instruction, long address)" to ManeuverItemViewSettingItem().apply {
                iconId = R.drawable.ic_maneuver_icon_12
                instruction = "This is a very very very long instruction!"
                address = "Fuubarstrasse, more data to show here a lot of data."
                distance = "10 km"
            },
            "With all values (long instruction, long address, long distance)" to ManeuverItemViewSettingItem().apply {
                iconId = R.drawable.ic_maneuver_icon_12
                instruction = "This is a very very very long instruction!"
                address = "Fuubarstrasse, more data to show here a lot of data."
                distance = "1200 km"
            }
        )
    }
}