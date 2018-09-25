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

package com.here.msdkuiapp.guidance

import com.here.android.mpa.common.PositioningManager
import com.here.android.mpa.guidance.NavigationManager
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.msdkuiapp.guidance.SingletonHelper.positioningManager
import junit.framework.Assert.assertNotNull
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Tests for [SingletonHelper].
 */
class SingletonHelperTest {

    @Test
    fun testNavigationManagerAndPositionManagerNotNull() {
        navigationManager = mock(NavigationManager::class.java)
        assertNotNull(SingletonHelper.navigationManager)

        positioningManager = mock(PositioningManager::class.java)
        assertNotNull(SingletonHelper.navigationManager)
    }
}