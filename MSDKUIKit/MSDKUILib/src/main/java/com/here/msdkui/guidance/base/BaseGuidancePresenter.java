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

package com.here.msdkui.guidance.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteTta;
import com.here.android.mpa.routing.RoutingError;

import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * Base class for guidance presenters.
 * <p>The class interacts with {@link NavigationManager} and {@link PositioningManager} to support
 * easier handling of guidance events.</p>
 */
public class BaseGuidancePresenter {

    private final NavigationManager mNavigationManager;

    /**
     * Listener for {@link com.here.android.mpa.guidance.NavigationManager.ManeuverEventListener}.
     */
    private final NavigationManager.ManeuverEventListener mManeuverEventListener =
            new NavigationManager.ManeuverEventListener() {
                @Override
                public void onManeuverEvent() {
                    handleManeuverEvent();
                }
            };
    /**
     * Listener for {@link com.here.android.mpa.guidance.NavigationManager.NewInstructionEventListener}.
     */
    private final NavigationManager.NewInstructionEventListener mNewInstructionEventListener =
            new NavigationManager.NewInstructionEventListener() {
                @Override
                public void onNewInstructionEvent() {
                    handleNewInstructionEvent();
                }
            };
    /**
     * Listener for {@link com.here.android.mpa.guidance.NavigationManager.PositionListener}.
     */
    private final NavigationManager.PositionListener mPositionListener =
            new NavigationManager.PositionListener() {
                @Override
                public void onPositionUpdated(final GeoPosition loc) {
                    handlePositionUpdate();
                }
            };

    /**
     * Listener for {@link com.here.android.mpa.guidance.NavigationManager.GpsSignalListener}.
     */
    private final NavigationManager.GpsSignalListener mGpsSignalListener =
            new NavigationManager.GpsSignalListener() {
                @Override public void onGpsLost() {
                    handleGpsLost();
                }

                @Override public void onGpsRestored() {
                    handleGpsRestore();
                }
            };

    /**
     * Listener for {@link com.here.android.mpa.guidance.NavigationManager.RerouteListener}.
     */
    private final NavigationManager.RerouteListener mRerouteListener =
            new NavigationManager.RerouteListener() {
                @Override public void onRerouteBegin() {
                    handleRerouteBegin();
                }

                @Override public void onRerouteEnd(RouteResult routeResult, RoutingError error) {
                    if (error == RoutingError.NONE) {
                        handleRerouteEnd(routeResult);
                    } else {
                        handleRerouteFailed(error);
                    }
                }
            };

    private NavigationManager.SpeedWarningListener mSpeedWarningListener;

    private Route mRoute;

    /**
     * Constructs a new instance using a {@link NavigationManager} instance and
     * a route to follow during guidance.
     *
     * @param navigationManager
     *         a {@link NavigationManager}.
     * @param route
     *         a route to be used for guidance.
     */
    protected BaseGuidancePresenter(@NonNull NavigationManager navigationManager, Route route) {
        mNavigationManager = navigationManager;
        mRoute = route;
    }

    /**
     * Resumes presenter to start listening to navigation events.
     */
    public void resume() {
        mNavigationManager.addManeuverEventListener(new WeakReference<>(mManeuverEventListener));
        mNavigationManager.addNewInstructionEventListener(new WeakReference<>(mNewInstructionEventListener));
        mNavigationManager.addPositionListener(new WeakReference<>(mPositionListener));
        mNavigationManager.addGpsSignalListener(new WeakReference<>(mGpsSignalListener));
        mNavigationManager.addRerouteListener(new WeakReference<>(mRerouteListener));
    }

    /**
     * Pauses presenter to stop listening to navigation events.
     */
    public void pause() {
        mNavigationManager.removeManeuverEventListener(mManeuverEventListener);
        mNavigationManager.removeNewInstructionEventListener(mNewInstructionEventListener);
        mNavigationManager.removePositionListener(mPositionListener);
        mNavigationManager.removeGpsSignalListener(mGpsSignalListener);
        mNavigationManager.removeRerouteListener(mRerouteListener);
    }

    /**
     * Enable listening to speed limit warnings.
     */
    protected final void enableSpeedWarnings() {
        if (mSpeedWarningListener == null) {
            mSpeedWarningListener = new NavigationManager.SpeedWarningListener() {
                @Override
                public void onSpeedExceeded(String roadName, float speedLimit) {
                    super.onSpeedExceeded(roadName, speedLimit);
                    handleSpeedExceeded(speedLimit);
                }

                @Override
                public void onSpeedExceededEnd(String roadName, float speedLimit) {
                    super.onSpeedExceededEnd(roadName, speedLimit);
                    handleSpeedExceededEnd(speedLimit);
                }
            };
        }
        mNavigationManager.addSpeedWarningListener(new WeakReference<>(mSpeedWarningListener));
    }

    /**
     * Called to de-register speed warnings listener.
     */
    protected final void disableSpeedWarnings() {
        if (mSpeedWarningListener != null) {
            mNavigationManager.removeSpeedWarningListener(mSpeedWarningListener);
        }
    }

    /**
     * Called after resuming the presenter. Subclasses may override for custom needs.
     */
    protected void handleManeuverEvent() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies on new instruction events generated by {@link NavigationManager}.
     * Subclasses may override the method if they are interested in the events forwarded by
     * {@link com.here.android.mpa.guidance.NavigationManager.NewInstructionEventListener}.
     */
    protected void handleNewInstructionEvent() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when GPS signal is lost.
     */
    protected void handleGpsLost() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when GPS signal is restored.
     */
    protected void handleGpsRestore() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when rerouting begins.
     */
    protected void handleRerouteBegin() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when rerouting ends with success.
     *
     * @param routeResult {@link RouteResult} object containing {@link Route} as result of route
     *                                       calculation.
     */
    protected void handleRerouteEnd(RouteResult routeResult) {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when rerouting ends with failure.
     *
     * @param error rerouting error.
     */
    protected void handleRerouteFailed(RoutingError error) {
        // Let the sub class override it, if interested.
    }

    /*
     * Notifies when position updates.
     * The Position change notification is generated by {@link NavigationManager}.
     */
    protected void handlePositionUpdate() {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when driving over speed limit.
     *
     * @param speedLimit
     *         current speed limit.
     */
    protected void handleSpeedExceeded(float speedLimit) {
        // Let the sub class override it, if interested.
    }

    /**
     * Notifies when no longer driving over speed limit.
     *
     * @param speedLimit
     *         current speed limit.
     */
    protected void handleSpeedExceededEnd(float speedLimit) {
        // Let the sub class override it, if interested.
    }

    /**
     * Gets the next {@link Maneuver Maneuver}.
     *
     * @return next {@link Maneuver Maneuver}.
     */
    public Maneuver getNextManeuver() {
        return mNavigationManager.getNextManeuver();
    }

    /**
     * Gets the {@link Maneuver Maneuver} after next {@link Maneuver Maneuver}.
     *
     * @return after-next {@link Maneuver Maneuver}.
     */
    public Maneuver getAfterNextManeuver() {
        return mNavigationManager.getAfterNextManeuver();
    }

    /**
     * Gets the distance to the next {@link Maneuver Maneuver}.
     *
     * @return distance to next {@link Maneuver Maneuver}.
     */
    public long getNextManeuverDistance() {
        return mNavigationManager.getNextManeuverDistance();
    }

    /**
     * Gets the route that was set to be used for guidance.
     *
     * @return a route.
     */
    public Route getRoute() {
        return mRoute;
    }

    /**
     * Sets a new route that should be used for guidance.
     *
     * @param route
     *         a route.
     */
    public void setRoute(Route route) {
        this.mRoute = route;
    }

    /**
     * Gets estimated arrival date.
     *
     * @return a {@link Date} of arrival at the destination.
     */
    public @Nullable Date getEta() {
        return mNavigationManager.getEta(false, Route.TrafficPenaltyMode.OPTIMAL);
    }

    /**
     * Gets distance to the destination.
     *
     * @return distance in meters.
     */
    public long getDestinationDistance() {
        return mNavigationManager.getDestinationDistance();
    }

    /**
     * Gets time to arrive at the destination.
     *
     * @return time to arrive in seconds or -1 if time couldn't be retrieved.
     */
    public Integer getTimeToArrival() {
        final RouteTta routeTta = mNavigationManager.getTta(Route.TrafficPenaltyMode.OPTIMAL, false);
        if (routeTta != null) {
            return routeTta.getDuration();
        }
        return null;
    }
}
