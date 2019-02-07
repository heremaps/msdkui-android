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

package com.here.msdkuidev

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.DialogTitle
import com.here.msdkuidev.Constant.DEFAULT
import java.io.Serializable

abstract class SettingItem(var title: String ="", var subTitle: String = DEFAULT) : Parcelable {

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()!!
        subTitle = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(subTitle)
    }
}

abstract class Setting<T> : Serializable {
    var subTitle: String = DEFAULT
    abstract fun getClassName() : Class<T>
    abstract fun getItems(context: Context) : LinkedHashMap<String, SettingItem>
}