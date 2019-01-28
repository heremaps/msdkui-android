/*
 * Copyright (C) 2017-2019 HERE Europe B.V.
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

package com.here.msdkuiapp.espresso.impl.views.drivenavigation.matchers

import android.support.test.espresso.assertion.ViewAssertions.matches
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationBarView.onDriveNavigationBarDestinationLabel
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationBarView.onDriveNavigationBarLocationTitle

/**
 * Drive Navigation specific actionbar matches
 */
object DriveNavigationBarMatchers {

    /**
     * Check default destination location label text view on drive navigation
     */
    fun checkDestinationDefaultLabel(): DriveNavigationBarMatchers {
        onDriveNavigationBarDestinationLabel.check(matches(onDriveNavigationBarLocationTitle))
        return this
    }
}