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

package com.here.msdkui.guidance;

/**
 * A listener that notifies when there is a change in {@link GuidanceNextManeuverData}.
 */
public interface GuidanceNextManeuverListener {

    /**
     * Called when there is change in {@link GuidanceNextManeuverData}.
     *
     * @param data
     *         {@link GuidanceNextManeuverData}.
     */
    void onDataChanged(GuidanceNextManeuverData data);
}
