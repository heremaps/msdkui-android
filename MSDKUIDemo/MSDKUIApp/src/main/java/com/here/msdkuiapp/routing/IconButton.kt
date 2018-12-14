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

package com.here.msdkuiapp.routing

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageButton
import android.util.AttributeSet

import com.here.msdkuiapp.R
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Generic button with predefined set of icons.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class IconButton(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageButton(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var type: Type = Type.ADD
        set(value) {
            field = value
            updateImage()
        }

    init {
        val attributesArray: TypedArray = context.theme
                .obtainStyledAttributes(attrs, R.styleable.IconButton, defStyleAttr, 0)

        if (attributesArray.hasValue(R.styleable.IconButton_type)) {
            val value: Int = attributesArray.getInt(R.styleable.IconButton_type, 0)

            if (value >= 0 && value < Type.values().size) {
                type = Type.values()[value]
            }
        }

        attributesArray.recycle()

        // Load the image for the default type
        updateImage()
    }

    /**
     * Updates the foreground image based on the button type.
     */
    private fun updateImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setImageResource(type.value)
        } else {
            // The backgroundTint and tint properties works well with layered images
            setImageDrawable(ContextCompat.getDrawable(context, type.value))
        }
    }

    /**
     * Enum defining type of button.
     */
    enum class Type(val value: Int) {
        /**
         * Add button.
         */
        ADD(R.drawable.ic_add_waypoint),

        /**
         * Collapse button.
         */
        COLLAPSE(R.drawable.ic_collapse),

        /**
         * Expand button.
         */
        EXPAND(R.drawable.ic_expande),

        /**
         * Options button.
         */
        OPTIONS(R.drawable.ic_settings_route_24),

        /**
         * Remove button.
         */
        REMOVE(R.drawable.ic_remove_listitem),

        /**
         * Swap button.
         */
        SWAP(R.drawable.ic_swap_list);

        var imageId: Int = value
        private set
    }
}

