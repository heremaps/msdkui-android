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

/**
 * Callbacks notified when action from dashboard is triggered.
 */
interface GuidanceDashBoardViewListener {

    /**
     * Event triggered by clicking on stop navigation button.
     */
    fun onStopNavigationButtonClicked()

    /**
     * Event triggered when DashBoard is being expanded.
     */
    fun onExpanded()

    /**
     * Event triggered when DashBoard is being collapsed.
     */
    fun onCollapsed()

    /**
     * Event triggered by clicking item on list.
     */
    fun onItemClicked(itemIndex: Int)

    /**
     * Event triggered by clicking on part of DashBoard that is visible when it is collapsed.
     */
    fun onCollapsedViewClicked()
}