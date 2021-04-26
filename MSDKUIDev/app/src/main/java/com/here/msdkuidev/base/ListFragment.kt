/*
 * Copyright (C) 2017-2021 HERE Europe B.V.
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

package com.here.msdkuidev.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.here.msdkuidev.R
import kotlinx.android.synthetic.main.list.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.LinearLayout.VERTICAL
import com.here.msdkuidev.Constant.INDEX

open class ListFragment : Fragment() {

    private lateinit var listener: Listener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = activity as Listener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement ListFragment.Listener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(landing_list) {
            layoutManager = LinearLayoutManager(
                activity,
                VERTICAL,
                false
            )
            setHasFixedSize(true)
            val dividerItemDecoration = DividerItemDecoration(activity, VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
        val adapter = LandingScreenAdapter(listener.getList(arguments?.getInt(INDEX)), activity!!)
        adapter.itemListener = listener
        landing_list.adapter = adapter
    }

    /**
     * ViewHolder to hold data for a row.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text1 = view.findViewById<TextView>(android.R.id.text1)!!
        val text2 = view.findViewById<TextView>(android.R.id.text2)!!
    }

    class LandingScreenAdapter(private val items: List<Pair<String, String>>,
                               private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

        var itemListener: Listener? = null

        override fun getItemCount(): Int {
            return items.size
        }

        // Inflates the item views
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowItem = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(rowItem)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder) {
                itemView.setOnClickListener { itemListener?.onItemClicked(itemView, position) }
                text1.text = items[position].first
                with(text2) {
                    if(items[position].second.isEmpty()) {
                        visibility = View.GONE
                    } else {
                        visibility = View.VISIBLE
                        text = items[position].second
                    }
                }
            }
        }
    }

    /**
     * Listener to be notified when item is clicked.
     */
    interface Listener {

        fun getList(index: Int? = 0) : List<Pair<String, String>>

        /**
         * Callback to indicate a row has been clicked.
         */
        fun onItemClicked(view: View, position: Int) {}
    }
}