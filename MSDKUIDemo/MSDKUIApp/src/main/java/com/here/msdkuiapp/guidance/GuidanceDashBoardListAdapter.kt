/*
 * Copyright (C) 2017-2020 HERE Europe B.V.
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

package com.here.msdkuiapp.guidance

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.msdkuiapp.R
import kotlinx.android.synthetic.main.guidance_dashboard_list_item.view.*

/**
 * Adapter class to provide data to list of clickable items on guidance dashboard.
 */
class GuidanceDashBoardListAdapter(private val items: List<GuidanceDashBoardListItem>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    var itemListener: Listener? = null

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowItem = LayoutInflater.from(context).inflate(R.layout.guidance_dashboard_list_item, parent, false)
        rowItem.setOnClickListener({ itemListener?.onItemClicked(rowItem) })
        return ViewHolder(rowItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            if (items[position].iconId == 0) {
                icon.visibility = View.INVISIBLE
            } else {
                icon.setImageResource(items[position].iconId)
            }
            title.text = items[position].title
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
    val icon = view.icon!!
    val title = view.title!!
}

/**
 * Data for item on list.
 */
data class GuidanceDashBoardListItem(val iconId: Int, val title: String)