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

import com.here.android.mpa.common.MapEngine
import com.here.android.mpa.common.PositioningManager
import com.here.android.mpa.guidance.NavigationManager
import com.here.msdkuiapp.position.AppPositioningManager

object SingletonHelper {

    /**
     * Single place to get navigation manager throughout the app.
     * This will make testing easy too by mocking it on this place only.
     *
     * Setters & Getters are auto generated.
     */
    internal var navigationManager: NavigationManager? = null
        get() = field ?: if (MapEngine.isInitialized()) NavigationManager.getInstance() else null

    /**
     * Single place to get positioning manager throughout the app.
     * This will make testing easy too by mocking it on this place only.
     *
     * Setters & Getters are auto generated.
     */
    internal var appPositioningManager: AppPositioningManager? = null
        get() = field ?: AppPositioningManager.getInstance()
}