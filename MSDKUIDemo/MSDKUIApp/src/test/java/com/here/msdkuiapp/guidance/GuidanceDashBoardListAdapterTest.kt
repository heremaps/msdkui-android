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

package com.here.msdkuiapp.guidance

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.here.testutils.BaseTest
import junit.framework.Assert.assertNotNull
import kotlinx.android.synthetic.main.guidance_dashboard_list_item.view.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Tests for [GuidanceDashBoardListAdapter].
 */
class GuidanceDashBoardListAdapterTest : BaseTest() {

    private lateinit var adapter: GuidanceDashBoardListAdapter

    @Before
    override fun setUp() {
        super.setUp()
        val rowItem = GuidanceDashBoardListItem(1, "title")
        val rowItem2 = GuidanceDashBoardListItem(0, "title2")
        adapter = GuidanceDashBoardListAdapter(listOf(rowItem, rowItem2), applicationContext)
    }

    @Test
    fun testItemCount() {
        assertThat(adapter.itemCount, `is`(2))
    }

    @Test
    fun testDataPopulation() {
        val mockView = mock(View::class.java)
        val mockTextView = mock(TextView::class.java)
        val mockImageView = mock(ImageView::class.java)

        `when`(mockView.icon).thenReturn(mockImageView)
        `when`(mockView.title).thenReturn(mockTextView)

        val holder = spy(ViewHolder(mockView))
        doReturn(0).`when`(holder).adapterPosition
        adapter.onBindViewHolder(holder, 0)
        assertNotNull(holder.itemView)
        with(holder) {
            verify(title).text = eq("title")
            verify(icon).setImageResource(eq(1))
        }

        doReturn(1).`when`(holder).adapterPosition
        adapter.onBindViewHolder(holder, 1)
        assertNotNull(holder.itemView)
        with(holder) {
            verify(title).text = eq("title2")
            verify(icon).visibility = eq(View.INVISIBLE)
        }
    }

    @Test
    fun testDataClass() {
        val item = GuidanceDashBoardListItem(0, "other_title")
        assertNotNull(item.component1())
        assertNotNull(item.component2())
        assertNotNull(item.hashCode())
        assertThat(item, `is`(item.copy()))
        assertNotNull(item.toString())
    }
}