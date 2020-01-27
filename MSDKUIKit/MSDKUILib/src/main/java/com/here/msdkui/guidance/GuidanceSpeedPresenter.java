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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.common.RoadElement;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.msdkui.guidance.base.BaseGuidancePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates {@link GuidanceSpeedData} instances during guidance and notifies it's
 * listeners about that.
 */
public class GuidanceSpeedPresenter extends BaseGuidancePresenter {

    @NonNull
    private final PositioningManager mPositioningManager;
    private final List<GuidanceSpeedListener> mListener = new ArrayList<>();
    private float mPreviousSpeedLimit = -1.0f;

    /**
     * Constructs a new instance using instances of {@link NavigationManager} and {@link PositioningManager}.
     *
     * @param navigationManager
     *         a {@link NavigationManager}.
     * @param positioningManager
     *         a {@link PositioningManager}.
     */
    public GuidanceSpeedPresenter(@NonNull NavigationManager navigationManager,
            @NonNull PositioningManager positioningManager) {
        super(navigationManager, null);
        mPositioningManager = positioningManager;
    }

    @Override
    public void resume() {
        super.resume();
        enableSpeedWarnings();
    }

    @Override
    public void pause() {
        super.pause();
        disableSpeedWarnings();
    }

    @Override
    protected void handlePositionUpdate() {
        final RoadElement roadElement = mPositioningManager.getRoadElement();
        updateCurrentSpeedData(roadElement == null ? mPreviousSpeedLimit : roadElement.getSpeedLimit());
    }

    @Override
    protected void handleSpeedExceeded(float speedLimit) {
        mPreviousSpeedLimit = speedLimit;
        updateCurrentSpeedData(speedLimit);
    }

    @Override
    protected void handleSpeedExceededEnd(float speedLimit) {
        mPreviousSpeedLimit = speedLimit;
        updateCurrentSpeedData(speedLimit);
    }

    private void updateCurrentSpeedData(float speedLimit) {
        final GeoPosition geoPosition = mPositioningManager.hasValidPosition() ?
                mPositioningManager.getPosition() : null;
        final double speed = geoPosition != null && geoPosition.isValid() && geoPosition.getSpeed() != GeoPosition.UNKNOWN ?
                geoPosition.getSpeed() : -1;
        if (speed >= 0) {
            final GuidanceSpeedData data = new GuidanceSpeedData(speed, (double) speedLimit);
            notifyDataChanged(data);
        } else {
            notifyDataChanged(null);
        }
    }

    /**
     * Notify on {@link GuidanceSpeedData} changes.
     */
    private void notifyDataChanged(@Nullable GuidanceSpeedData data) {
        for (final GuidanceSpeedListener listener : mListener) {
            listener.onDataChanged(data);
        }
    }

    /**
     * Adds a {@link GuidanceSpeedListener}.
     *
     * @param listener
     *         the listener to add to the list of listeners.
     */
    public void addListener(GuidanceSpeedListener listener) {
        if (listener != null && !mListener.contains(listener)) {
            mListener.add(listener);
        }
    }

    /**
     * Remove a {@link GuidanceSpeedListener}.
     *
     * @param listener
     *         the listener to remove from the list of listeners.
     */
    public void removeListener(GuidanceSpeedListener listener) {
        mListener.remove(listener);
    }
}
