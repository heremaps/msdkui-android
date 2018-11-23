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

package com.here.msdkuiapp.landing

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.msdkuiapp.R
import kotlinx.android.synthetic.main.landing_screen_item.view.*

/**
 * Adapter class to provide data to list for landing activity.
 */
class LandingScreenAdapter(private val items: List<RowItem>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    var itemListener: Listener? = null

    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowItem = LayoutInflater.from(context).inflate(R.layout.landing_screen_item, parent, false)
        rowItem.setOnClickListener { itemListener?.onItemClicked(rowItem) }
        return ViewHolder(rowItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            heading.text = items[position].heading
            description.text = items[position].desc
            extDescription.text = items[position].extraDesc
            if (items[position].iconId == 0) {
                icon.visibility = View.INVISIBLE
            } else {
                icon.setImageResource(items[position].iconId)
            }
        }
    }

    /**
     * Listener to be notified when item is clicked.
     */
    interface Listener {

        /**
         * Callback to indicate a row has been clicked.
         */
        fun onItemClicked(view: View)
    }
}

/**
 * ViewHolder to hold data for a row.
 */
class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val heading = view.ls_heading!!
    val description = view.ls_description!!
    val extDescription = view.ls_extra_desc!!
    val icon = view.ls_icon!!
}

/**
 * List row data.
 */
data class RowItem(val heading: String, val desc: String, val extraDesc: String, val iconId: Int)