package com.here.msdkuiapp.routing

import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.idling.CountingIdlingResource
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