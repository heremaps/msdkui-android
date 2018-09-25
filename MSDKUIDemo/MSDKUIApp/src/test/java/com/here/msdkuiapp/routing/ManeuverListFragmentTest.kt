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

import MockUtils
import android.content.res.Configuration
import android.view.View
import com.here.msdkuiapp.R
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.android.synthetic.main.maneuver.view.maneuver_route_list
import kotlinx.android.synthetic.main.maneuver.view.route_item
import kotlinx.android.synthetic.main.maneuver.view.route_item_container
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric

/**
 * Tests for [ManeuverListFragment].
 */
class ManeuverListFragmentTest : BaseTest() {

    private lateinit var fragment: ManeuverListFragment

    @Mock
    private lateinit var presenter: ManeuverListPresenter

    @Before
    public override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        fragment = Robolectric.buildFragment(ManeuverListFragment::class.java).create().get()
        fragment.presenter = presenter
    }

    @Test
    fun testPresenterInit() {
        fragment = ManeuverListFragment()
        assertNotNull(fragment.presenter)
    }

    @Test
    fun testOnViewCreated() {
        fragment.onViewCreated(mock(View::class.java), null)
        with(verify(presenter)) {
            onAttach(anySafe(), anySafe())
            updateActionBar(anySafe())
            makeUiDataReady()
        }
    }

    @Test
    fun testUpdateRoute() {
        val mockRoute = MockUtils.mockRoute()
        fragment.updateRoute(mockRoute, false)
        verify(presenter).updateRoute(mockRoute, false)
        addFrag(fragment, ManeuverListFragment.toString())
        assertNotNull(fragment.view.findViewById<View>(R.id.route_item))
    }

    @Test
    fun testOnConfigurationChanged() {
        val mockConfig = mock(Configuration::class.java)
        fragment.onConfigurationChanged(mockConfig)
        verify(presenter).populateConfigChanges(mockConfig.orientation)
    }

    @Test
    fun testUpdateConfigChanges() {
        fragment.updateConfigChanges(true)
        assertEquals(fragment.view.route_item_container.visibility, View.VISIBLE)
        fragment.updateConfigChanges(false)
        assertEquals(fragment.view.route_item_container.visibility, View.GONE)
    }

    @Test
    fun testOnUiDataReady() {
        val mockRoute = MockUtils.mockRoute()
        fragment.onUiDataReady(false, mockRoute)
        assertEquals(fragment.view.route_item.isTrafficEnabled, false)
        assertEquals(fragment.view.route_item.route, mockRoute)

        fragment.view.maneuver_route_list.performClick()
        assertEquals(fragment.view.maneuver_route_list.route, mockRoute)
    }
}
