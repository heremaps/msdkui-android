package com.here.msdkuidev

import java.io.Serializable

open class SettingItem : Serializable

interface Setting<T> : Serializable {
    fun getClassName() : Class<T>
    fun getItems() : LinkedHashMap<String, SettingItem>
}