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

package com.here.msdkuiapp.guidance

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.here.msdkuiapp.R
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.guidance_dashboard.view.*

/**
 * A view that is container type. Contains Guidance Speed, Guidance Estimated Arrival, stop navigation button.
 * View is expandable, when expanded it contains list of clickable items.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceDashBoardView : LinearLayout {

    internal var presenter: GuidanceDashBoardPresenter = GuidanceDashBoardPresenter()

    /**
     * Constructs a new instance.
     */
    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    /**
     * Constructs a new instance.
     * Requires Lollipop (API level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    /**
     * Init the View UI.
     *
     * @param context
     * activity or application context.
     */
    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.guidance_dashboard, this)
        populate()
    }

    /**
     * Populate the UI with list items, add on click listener for stop navigation button.
     */
    private fun populate() {
        stop_navigation.setOnClickListener {
            presenter.onStopNavigationButtonClicked()
        }

        collapsed_view.setOnClickListener {
            presenter.onCollapsedViewClicked()
        }

        with(items_list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        val list = listOf<GuidanceDashBoardListItem>(
                GuidanceDashBoardListItem(R.drawable.ic_settings_route_24,
                        context.getString(R.string.msdkui_app_settings)),
                GuidanceDashBoardListItem(R.drawable.ic_info_outline_black_24dp,
                        context.getString(R.string.msdkui_app_about))
        )
        val adapter = GuidanceDashBoardListAdapter(list, context)
        adapter.itemListener = itemClickListener
        items_list.adapter = adapter
    }

    private val itemClickListener = object : GuidanceDashBoardListAdapter.Listener {
        override fun onItemClicked(view: View) {
            val position = items_list.getChildLayoutPosition(view)
            presenter.onItemClicked(position)
        }
    }

}