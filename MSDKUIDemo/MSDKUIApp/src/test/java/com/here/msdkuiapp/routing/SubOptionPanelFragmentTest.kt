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

package com.here.msdkuiapp.routing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkuiapp.R
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.android.synthetic.main.option_panel.view.optionpanel_container
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric

/**
 * Tests for [SubOptionPanelFragment].
 */
class SubOptionPanelFragmentTest : BaseTest() {

    private lateinit var fragment: SubOptionPanelFragment

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var presenter: SubOptionPanelPresenter

    @Before
    public override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        fragment = SubOptionPanelFragment.newInstance()
        fragment.presenter = presenter
        addFrag(fragment)
    }

    @Test
    fun testPresenterInit() {
        fragment = SubOptionPanelFragment.newInstance()
        assertNotNull(fragment.presenter)
    }

    @Test
    fun testOnCreateView() {
        val inflaterMock = mock(LayoutInflater::class.java)
        val viewGroupMock = mock(ViewGroup::class.java)
        val inflatedView = mock(View::class.java)
        `when`(inflaterMock.inflate(R.layout.option_panel, viewGroupMock, false)).thenReturn(inflatedView)
        fragment.onCreateView(inflaterMock, viewGroupMock, null)
        verify(inflaterMock).inflate(R.layout.option_panel, viewGroupMock, false)
        verify(inflatedView).isClickable = true
    }

    @Test
    fun testSetAndGetRouteOptions() {
        fragment = SubOptionPanelFragment.newInstance()
        val routeOptionsMock = mock(RouteOptions::class.java)
        with(fragment) {
            routeOptions = routeOptionsMock
            assertEquals(routeOptions, routeOptionsMock)
        }
    }

    @Test
    fun testSetAndGetPanels() {
        fragment = SubOptionPanelFragment.newInstance()
        val panelsMock = mock(Panels::class.java)
        with(fragment) {
            type = panelsMock
            assertEquals(type, panelsMock)
        }
    }

    @Test
    fun testOnViewCreated() {
        with(verify(presenter)) {
            onAttach(anySafe(), anySafe())
            updateActionBar(anySafe())
            makeUiDataReady(anySafe())
        }
    }

    @Test
    fun testOnUiDataReady() {
        with(fragment) {
            onUiDataReady(mock(View::class.java))
            assertEquals(view!!.optionpanel_container.childCount, 1)
        }
    }
}