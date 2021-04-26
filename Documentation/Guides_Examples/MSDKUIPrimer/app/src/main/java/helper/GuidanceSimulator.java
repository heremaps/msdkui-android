/*
 * Copyright (C) 2017-2021 HERE Europe B.V.
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

package helper;

import androidx.annotation.NonNull;
import android.util.Log;

import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.common.PositioningManager.OnPositionChangedListener;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.Route;

import java.lang.ref.WeakReference;

/**
 * Convenience class that does not contain any MSDK UI Kit related code.
 */
public class GuidanceSimulator {

    private static final String LOG_TAG = MapInitializer.class.getName();
    private static GuidanceSimulator guidanceSimulatorInstance;
    private static final int METERS_PER_SECOND = 50;

    private Map map;

    private GuidanceSimulator(){ }

    public static synchronized GuidanceSimulator getInstance(){
        if(guidanceSimulatorInstance == null){
            guidanceSimulatorInstance = new GuidanceSimulator();
        }
        return guidanceSimulatorInstance;
    }

    private final OnPositionChangedListener positionChangedlistener = new OnPositionChangedListener() {
        @Override
        public void onPositionUpdated(PositioningManager.LocationMethod locationMethod,
                                      GeoPosition geoPosition, boolean isMapMatched) {
            // Follow current position
            map.setCenter(geoPosition.getCoordinate(), Map.Animation.LINEAR);
            map.setOrientation((float) geoPosition.getHeading(), Map.Animation.BOW);
        }

        @Override
        public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod,
                                         PositioningManager.LocationStatus locationStatus) {
        }
    };

    public void startGuidanceSimulation(@NonNull Route route, @NonNull Map hereMap) {
        map = hereMap;
        map.addMapObject(new MapRoute(route));
        map.setCenter(route.getStart(), Map.Animation.NONE);
        map.setTilt(72.0f);
        map.setZoomLevel(20);
        map.setLandmarksVisible(true);
        map.setExtrudedBuildingsVisible(true);

        PositioningManager.getInstance().addListener(new WeakReference<>(positionChangedlistener));

        NavigationManager.getInstance().setMap(map);
        NavigationManager.getInstance().setRoute(route);
        NavigationManager.Error error = NavigationManager.getInstance().simulate(route, METERS_PER_SECOND);
        if (error != NavigationManager.Error.NONE) {
            Log.e(LOG_TAG, "Cannot start guidance simulation: " + error.toString());
        }
    }

    public void stopGuidance() {
        PositioningManager.getInstance().removeListener(positionChangedlistener);
        NavigationManager.getInstance().stop();
    }
}
