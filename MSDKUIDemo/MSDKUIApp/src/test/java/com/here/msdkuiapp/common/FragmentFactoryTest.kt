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

package com.here.msdkuiapp.common

import androidx.fragment.app.Fragment
import com.here.msdkuiapp.common.mapselection.WaypointSelectionFragment
import com.here.msdkuiapp.common.routepreview.RoutePreviewFragment
import com.here.msdkuiapp.guidance.GuidanceManeuverFragment
import com.here.msdkuiapp.routing.OptionPanelFragment
import com.here.msdkuiapp.routing.RouteDescriptionListFragment
import com.here.msdkuiapp.routing.RouteManeuverListFragment
import com.here.msdkuiapp.routing.SubOptionPanelFragment
import junit.framework.Assert.assertNotNull
import org.junit.Test

/**
 * Tests for [FragmentFactory].
 */
class FragmentFactoryTest {

    @Test
    fun testFragmentCreation() {
        assertNotNull(FragmentFactory.create(WaypointSelectionFragment::class.java))
        assertNotNull(FragmentFactory.create(RouteDescriptionListFragment::class.java))
        assertNotNull(FragmentFactory.create(RouteManeuverListFragment::class.java))
        assertNotNull(FragmentFactory.create(OptionPanelFragment::class.java))
        assertNotNull(FragmentFactory.create(SubOptionPanelFragment::class.java))
        assertNotNull(FragmentFactory.create(GuidanceManeuverFragment::class.java))
        assertNotNull(FragmentFactory.create(RoutePreviewFragment::class.java))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidCreation() {
        assertNotNull(FragmentFactory.create(FragmentTest::class.java))
    }

    class FragmentTest : Fragment()
}