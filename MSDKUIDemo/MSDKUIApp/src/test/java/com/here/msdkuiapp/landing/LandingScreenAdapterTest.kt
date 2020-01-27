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

package com.here.msdkuiapp.landing

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.here.testutils.BaseTest
import junit.framework.Assert.assertNotNull
import kotlinx.android.synthetic.main.landing_screen_item.view.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Tests for [LandingScreenAdapter].
 */
class LandingScreenAdapterTest : BaseTest() {

    private lateinit var adapter: LandingScreenAdapter

    @Before
    override fun setUp() {
        super.setUp()
        val rowItem = RowItem("heading", "desc", "extraDesc", 1)
        adapter = LandingScreenAdapter(listOf(rowItem), applicationContext)
    }

    @Test
    fun testItemCount() {
        assertThat(adapter.itemCount, `is`(1))
    }

    @Test
    fun testDataPopulation() {
        val mockView = mock(View::class.java)
        val mockTextView = mock(TextView::class.java)
        val mockImageView = mock(ImageView::class.java)

        `when`(mockView.ls_description).thenReturn(mockTextView)
        `when`(mockView.ls_extra_desc).thenReturn(mockTextView)
        `when`(mockView.ls_heading).thenReturn(mockTextView)
        `when`(mockView.ls_icon).thenReturn(mockImageView)
        val holder = spy(ViewHolder(mockView))
        doReturn(0).`when`(holder).adapterPosition
        adapter.onBindViewHolder(holder, 0)
        assertNotNull(holder.itemView)
        with(holder) {
            verify(heading).text = eq("heading")
            verify(description).text = eq("desc")
            verify(extDescription).text = eq("extraDesc")
            verify(icon).setImageResource(eq(1))
        }
    }

    @Test
    fun testDataClass() {
        val item = RowItem("heading", "des", "extrades", 0)
        assertNotNull(item.component1())
        assertNotNull(item.component2())
        assertNotNull(item.component3())
        assertNotNull(item.component4())
        assertNotNull(item.hashCode())
        assertThat(item, `is`(item.copy()))
        assertNotNull(item.toString())
    }
}