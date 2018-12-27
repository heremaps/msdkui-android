package com.here.msdkuidev

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.here.msdkuidev.Constant.DEFAULT
import java.io.Serializable

abstract class SettingItem(var subTitle: String = DEFAULT) : Parcelable {

    constructor(parcel: Parcel) : this() {
        subTitle = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(subTitle)
    }
}

abstract class Setting<T> : Serializable {
     var subTitle: String = DEFAULT
    abstract fun getClassName() : Class<T>
    abstract fun getItems(context: Context) : LinkedHashMap<String, SettingItem>
}