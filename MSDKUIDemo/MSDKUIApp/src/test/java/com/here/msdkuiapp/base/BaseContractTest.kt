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

package com.here.msdkuiapp.base

import junit.framework.Assert.*

/**
 * Tests for [BaseContract]
 */
class BaseContractTest {

    class BaseContractImpl : BaseContract<BaseContractImpl> {

        var progress = false

        override fun onProgress(visible: Boolean) {
            super.onProgress(visible)
            progress = visible
        }
    }

    public fun testBehaviour() {
        val impl = BaseContractImpl()
        assertNotNull(impl.getCurrentViewContract())
        impl.onProgress(true)
        assertTrue(impl.progress)
        impl.onProgress(false)
        assertFalse(impl.progress)
    }
}