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
import com.here.testutils.BaseTest
import com.here.testutils.anySafe
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Tests for [ManeuverRouteDescriptionFragment].
 */
class ManeuverRouteDescriptionFragmentTest : BaseTest() {

    private lateinit var fragment: ManeuverRouteDescriptionFragment

    @Mock
    private lateinit var presenter: ManeuverRouteDescriptionPresenter

    @Before
    public override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
        fragment = ManeuverRouteDescriptionFragment.newInstance()
        fragment.presenter = presenter
    }

    @Test
    fun testPanelCreationAndOnViewCreated() {
        addFrag(fragment, ManeuverRouteDescriptionFragment::class.java.name)
        assertNotNull(fragment)
        assertNotNull(fragment.view)
        with(verify(presenter)) {
            onAttach(anySafe(), anySafe())
            makeUiDataReady()
        }
    }

    @Test
    fun testUpdateRoute() {
        val mockRoute = MockUtils.mockRoute()
        fragment.updateRoute(mockRoute)
        verify(presenter).updateRoute(mockRoute)
    }
}
