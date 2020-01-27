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

package com.here.msdkuiapp.guidance

import java.util.ArrayList

/**
 * Class to handle logic for [GuidanceDashBoardView].
 */
class GuidanceDashBoardPresenter : GuidanceDashBoardViewListener {

    private val listenersList = ArrayList<GuidanceDashBoardViewListener>()

    /**
     * Adds a {@link GuidanceDashBoardViewListener}.
     *
     * @param listener
     *         the listener to add to the list of listeners.
     */
    fun addListener(listener: GuidanceDashBoardViewListener) {
        if (!listenersList.contains(listener)) {
            listenersList.add(listener)
        }
    }

    /**
     * Remove a {@link GuidanceDashBoardViewListener}.
     *
     * @param listener
     *         the listener to remove from the list of listeners.
     */
    fun removeListener(listener: GuidanceDashBoardViewListener) {
        listenersList.remove(listener)
    }

    override fun onCollapsed() {
        listenersList.forEach {
            it.onCollapsed()
        }
    }

    override fun onExpanded() {
        listenersList.forEach {
            it.onExpanded()
        }
    }

    override fun onItemClicked(itemIndex: Int) {
        listenersList.forEach {
            it.onItemClicked(itemIndex)
        }
    }

    override fun onStopNavigationButtonClicked() {
        listenersList.forEach {
            it.onStopNavigationButtonClicked()
        }
    }

    override fun onCollapsedViewClicked() {
        listenersList.forEach {
            it.onCollapsedViewClicked()
        }
    }
}