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

package com.here.msdkuiapp.common

import android.content.Intent
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import com.here.msdkuiapp.about.AboutActivity
import com.here.msdkuiapp.setResourceWithTag
import com.here.testutils.BaseTest
import com.here.testutils.argumentCaptor
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Test for [AppActionBarTest].
 */
class AppActionBarTest : BaseTest() {

    private lateinit var appActionBar: AppActionBar

    @Mock
    private lateinit var activity: AppCompatActivity

    @Mock
    private lateinit var mockActionBar: ActionBar

    @Before
    override fun setUp() {
        MockitoAnnotations.initMocks(this)
        super.setUp()
        `when`(activity.supportActionBar).thenReturn(mockActionBar)
        appActionBar = AppActionBar(activity)
        `when`(activity.getString(ArgumentMatchers.anyInt())).thenReturn("")
    }

    @Test
    fun testInit() {
        val inflater = mock(LayoutInflater::class.java)
        val mockView = mock(View::class.java)
        val mockToolBar = mock(Toolbar::class.java)
        `when`(mockView.parent).thenReturn(mockToolBar)
        `when`(inflater.inflate(anyInt(), any())).thenReturn(mockView)
        `when`(activity.layoutInflater).thenReturn(inflater)

        appActionBar.setUpActionBar()
        verify(mockActionBar).elevation = anyFloat()
        verify(mockActionBar).setDisplayShowHomeEnabled(anyBoolean())
        verify(mockActionBar).setDisplayShowTitleEnabled(anyBoolean())
        verify(mockActionBar).setDisplayShowCustomEnabled(anyBoolean())
    }

    @Test
    fun testBack() {
        val mockView = mock(View::class.java)
        val mockImageView = mock(ImageView::class.java)
        `when`(mockView.findViewById<View>(anyInt())).thenReturn(mockImageView)
        `when`(mockActionBar.customView).thenReturn(mockView)
        appActionBar.setBack()
        verify(mockImageView).visibility = eq(View.GONE)
        verify(mockImageView).setResourceWithTag(anyInt())
        val captor = argumentCaptor<View.OnClickListener>()
        verify(mockImageView).setOnClickListener(captor.capture())
        captor.value.onClick(null)
        verify(activity).onBackPressed()
    }

    @Test
    fun testBackClickListener() {
        val mockView = mock(View::class.java)
        val mockImageView = mock(ImageView::class.java)
        `when`(mockView.findViewById<View>(anyInt())).thenReturn(mockImageView)
        `when`(mockActionBar.customView).thenReturn(mockView)
        var clicked = false
        appActionBar.setBack(clickListener = {
            clicked = true
        })
        val captor = argumentCaptor<View.OnClickListener>()
        verify(mockImageView).setOnClickListener(captor.capture())
        captor.value.onClick(null)
        assertTrue(clicked)
    }

    @Test
    fun testTitle() {
        val mockView = mock(View::class.java)
        val mockTextView = mock(TextView::class.java)
        `when`(mockView.findViewById<View>(anyInt())).thenReturn(mockTextView)
        `when`(mockActionBar.customView).thenReturn(mockView)
        appActionBar.setTitle()
        verify(mockTextView).visibility = eq(View.VISIBLE)
        verify(mockTextView).text = eq("")
        assertNotNull(appActionBar.titleView)
    }

    @Test
    fun testRightIcon() {
        val mockView = mock(View::class.java)
        val mockImageView = mock(ImageView::class.java)
        `when`(mockView.findViewById<View>(anyInt())).thenReturn(mockImageView)
        `when`(mockActionBar.customView).thenReturn(mockView)
        appActionBar.setRightIcon()
        verify(mockImageView).visibility = eq(View.VISIBLE)
        verify(mockImageView).setResourceWithTag(anyInt())
    }

    @Test
    fun testRightIconClickedDefaultAction() {
        val mockView = mock(View::class.java)
        val mockImageView = mock(ImageView::class.java)
        `when`(mockView.findViewById<View>(anyInt())).thenReturn(mockImageView)
        `when`(mockActionBar.customView).thenReturn(mockView)
        appActionBar.setRightIcon()
        val captor = argumentCaptor<View.OnClickListener>()
        verify(mockImageView).setOnClickListener(captor.capture())
        captor.value.onClick(null)
        verify(activity).startActivity(Intent(activity, AboutActivity::class.java))
    }

    @Test
    fun testRightIconClicked() {
        val mockView = mock(View::class.java)
        val mockImageView = mock(ImageView::class.java)
        `when`(mockView.findViewById<View>(anyInt())).thenReturn(mockImageView)
        `when`(mockActionBar.customView).thenReturn(mockView)
        var clicked = false
        appActionBar.setRightIcon(clickListener = {
            clicked = true
        })
        val captor = argumentCaptor<View.OnClickListener>()
        verify(mockImageView).setOnClickListener(captor.capture())
        captor.value.onClick(null)
        assertTrue(clicked)
    }

}
