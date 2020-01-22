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

package helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.Router;
import com.here.android.mpa.routing.RoutingError;

import java.util.ArrayList;
import java.util.List;

/**
 * Convenience class that does not contain any MSDK UI Kit related code.
 */
public class RouteCalculator {

    private static final String LOG_TAG = RouteCalculator.class.getName();
    private static final float ZOOM_TO_ORIENTATION = 1;
    private static RouteCalculator routeCalculatorInstance;

    private final CoreRouter coreRouter;
    private MapRoute currentMapRoute;
    public List<RouteResult> lastCalculatedRouteResults = new ArrayList<>();
    public Route selectedRoute;

    public interface RouteListener {
        void onRoutesCalculated(@NonNull List<RouteResult> routeResultList);
    }

    private RouteCalculator(){
        coreRouter = new CoreRouter();
    }

    public static synchronized RouteCalculator getInstance(){
        if(routeCalculatorInstance == null){
            routeCalculatorInstance = new RouteCalculator();
        }
        return routeCalculatorInstance;
    }

    public void calculateRoute(@NonNull List<RouteWaypoint> waypoints,
                               @NonNull RouteOptions routeOptions,
                               @NonNull RouteListener routeListener) {
        RoutePlan routePlan = new RoutePlan();
        routePlan.setRouteOptions(routeOptions);
        for (RouteWaypoint waypoint : waypoints) {
            routePlan.addWaypoint(waypoint);
        }

        coreRouter.calculateRoute(routePlan, new Router.Listener<List<RouteResult>, RoutingError>() {
            @Override
            public void onProgress(int i) {
            }

            @Override
            public void onCalculateRouteFinished(List<RouteResult> routeResults, RoutingError routingError) {
                if (routingError != RoutingError.NONE ) {
                    Log.e(LOG_TAG, "Routing Error: "+routingError.toString());
                } else {
                    lastCalculatedRouteResults = routeResults;
                    routeListener.onRoutesCalculated(lastCalculatedRouteResults);
                    Log.d(LOG_TAG, "Calculated routes: "+ lastCalculatedRouteResults.size());
                }
            }
        });
    }

    public void showRoute(@NonNull Map map, @Nullable RouteResult routeResult) {
        if (routeResult == null) {
            Log.d(LOG_TAG, "Route is null, nothing to show.");
            return;
        }

        if (currentMapRoute != null) {
            map.removeMapObject(currentMapRoute);
        }

        selectedRoute = routeResult.getRoute();
        currentMapRoute = new MapRoute(selectedRoute);
        map.addMapObject(currentMapRoute);
        map.zoomTo(selectedRoute.getBoundingBox(), Map.Animation.BOW, ZOOM_TO_ORIENTATION);
    }
}
