package com.here.msdkuidev.component

import android.os.Parcel
import android.os.Parcelable
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewData
import com.here.msdkuidev.Constant
import com.here.msdkuidev.R
import com.here.msdkuidev.Setting
import com.here.msdkuidev.SettingItem
import java.util.*

class GuidanceEstimatedArrivalSetting : Setting<GuidanceEstimatedArrival>() {

    class GuidanceEstimatedArrivalSettingItem : SettingItem {

        // data customization
        var guidanceEstimatedArrivalViewData: GuidanceEstimatedArrivalViewData? = null

        // ui customization
        var durationTheme : Int ? = null
        var arrivalTheme: Int ? = null

        constructor() : super()

        constructor(parcel: Parcel) : super(parcel) {
            guidanceEstimatedArrivalViewData =
                    parcel.readParcelable(GuidanceEstimatedArrivalViewData::class.java.classLoader)

            durationTheme = parcel.readValue(Int::class.java.classLoader) as? Int
            arrivalTheme = parcel.readValue(Int::class.java.classLoader) as? Int
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeParcelable(guidanceEstimatedArrivalViewData, flags)
            parcel.writeValue(durationTheme)
            parcel.writeValue(arrivalTheme)
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

    override fun getItems(): LinkedHashMap<String, SettingItem> {
        return linkedMapOf(
            "Default" to GuidanceEstimatedArrivalSettingItem(),
            "With all properties set" to GuidanceEstimatedArrivalSettingItem().apply {
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 2000, 2000)
            },
            "Without distance of arrival" to GuidanceEstimatedArrivalSettingItem().apply {
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 0, 2000)
            },
            "With red & big arrival time" to GuidanceEstimatedArrivalSettingItem().apply {
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 0, 2000)
                arrivalTheme = R.style.GuidanceEstimatedArrivalTimeTheme

            },
            "With duration red and bigger" to GuidanceEstimatedArrivalSettingItem().apply {
                guidanceEstimatedArrivalViewData = GuidanceEstimatedArrivalViewData(Date(), 2000, 2000)
                durationTheme = R.style.GuidanceEstimatedArrivalDurationTheme
            }
        )
    }

}