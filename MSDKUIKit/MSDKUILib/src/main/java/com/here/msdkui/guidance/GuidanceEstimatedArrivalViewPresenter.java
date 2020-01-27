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

import com.here.android.mpa.guidance.NavigationManager;
import com.here.msdkui.guidance.base.BaseGuidancePresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class creates {@link GuidanceEstimatedArrivalViewData} instances during guidance and notifies it's
 * listeners about that.
 */
public class GuidanceEstimatedArrivalViewPresenter extends BaseGuidancePresenter {

    private final List<GuidanceEstimatedArrivalViewListener> mListener = new ArrayList<>();

    /**
     * Constructs a new instance.
     *
     * @param navigationManager
     *         a {@link NavigationManager} to be used for guidance handling.
     */
    public GuidanceEstimatedArrivalViewPresenter(NavigationManager navigationManager) {
        super(navigationManager, null);
    }

    @Override
    protected void handlePositionUpdate() {
        updateEtaData(getEta(), getDestinationDistance(), getTimeToArrival());
    }

    private void updateEtaData(Date date, Long distance, Integer duration) {
        final GuidanceEstimatedArrivalViewData data = new GuidanceEstimatedArrivalViewData(date, distance, duration);
        notifyDataChanged(data);
    }

    /**
     * Notify on {@link GuidanceEstimatedArrivalViewData} changes.
     */
    private void notifyDataChanged(GuidanceEstimatedArrivalViewData data) {
        for (final GuidanceEstimatedArrivalViewListener listener : mListener) {
            listener.onDataChanged(data);
        }
    }

    /**
     * Adds a {@link GuidanceEstimatedArrivalViewListener}.
     *
     * @param listener
     *         the listener to add to the list of listeners.
     */
    public void addListener(GuidanceEstimatedArrivalViewListener listener) {
        if (listener != null && !mListener.contains(listener)) {
            mListener.add(listener);
        }
    }

    /**
     * Remove a {@link GuidanceEstimatedArrivalViewListener}.
     *
     * @param listener
     *         the listener to remove from the list of listeners.
     */
    public void removeListener(GuidanceEstimatedArrivalViewListener listener) {
        mListener.remove(listener);
    }
}
