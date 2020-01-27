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

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import java.io.Closeable

/**
 * [CountingIdlingResource] for Route calculation
 * Use it with [kotlin.io.use] for resource to be unregistered
 * or call [close] explicitly
 */
object RoutingIdlingResourceWrapper : Closeable {

    private const val resourceName = "RoutingIdlingResourceWrapper"
    private var idlingResource = CountingIdlingResource(resourceName)

    /**
     * Same as [CountingIdlingResource.increment]
     */
    fun increment() {
        idlingResource.increment()
    }

    /**
     * Same as [CountingIdlingResource.decrement]
     */
    fun decrement() {
        if (!idlingResource.isIdleNow) {
            idlingResource.decrement()
        }
    }

    /**
     * Register idling resource at the beginning of test
     */
    fun register() {
        idlingResource = CountingIdlingResource(resourceName)
        IdlingRegistry.getInstance().register(idlingResource)
    }

    /**
     * Unregister idling resource
     */
    override fun close() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}