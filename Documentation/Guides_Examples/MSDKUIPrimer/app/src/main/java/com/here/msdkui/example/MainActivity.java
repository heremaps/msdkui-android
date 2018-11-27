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

package com.here.msdkui.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.msdkui.routing.SimpleTransportModePanelAdapter;
import com.here.msdkui.routing.TabView;
import com.here.msdkui.routing.TransportModePanel;
import com.here.msdkui.routing.WaypointEntry;
import com.here.msdkui.routing.WaypointList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import helper.MapInitializer;
import helper.RouteCalculator;

/**
 * Shows example usage of WaypointList and TransportModePanel. A Map is added to
 * show how the calulcated route is affected by interacting with these components.
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    private MapInitializer mapInitializer;
    private WaypointList waypointList;
    private TransportModePanel transportModePanel;
    private Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapInitializer = new MapInitializer(this, this::onMapLoaded);
    }

    private void onMapLoaded(Map hereMap) {
        Toast.makeText(this, "HERE MSDK UI Kit version: " +
                com.here.msdkui.BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show();

        waypointList = findViewById(R.id.waypointList);
        transportModePanel = findViewById(R.id.transportModePanel);
        map = hereMap;

        prepareWaypointList();
        prepareTransportModePanel();
        calculateRoutes();
    }

    private void prepareWaypointList() {
        waypointList.setListener(new WaypointList.Listener() {
            @Override
            public void onEntryClicked(int index, WaypointEntry waypointEntry) {
                Log.d(LOG_TAG, "WaypointList: onEntryClicked");
                map.setZoomLevel(14);
                map.setCenter(waypointEntry.getRouteWaypoint().getOriginalPosition(), Map.Animation.BOW);
            }

            @Override
            public void onEntryAdded(int index, WaypointEntry waypointEntry) {
                Log.d(LOG_TAG, "WaypointList: onEntryAdded");
            }

            @Override
            public void onEntryUpdated(int index, WaypointEntry waypointEntry) {
                Log.d(LOG_TAG, "WaypointList: onEntryUpdated");
                calculateRoutes();
            }

            @Override
            public void onEntryRemoved(int index, WaypointEntry waypointEntry) {
                Log.d(LOG_TAG, "WaypointList: onEntryRemoved");
                calculateRoutes();
            }

            @Override
            public void onEntryDragged(int fromIndex, int toIndex) {
                Log.d(LOG_TAG, "WaypointList: onEntryDragged");
                calculateRoutes();
            }
        });

        List<WaypointEntry> waypointEntries = new ArrayList<>();
        waypointEntries.add(new WaypointEntry(new RouteWaypoint(new GeoCoordinate(52.53852,13.42506))));
        waypointEntries.add(new WaypointEntry(new RouteWaypoint(new GeoCoordinate(52.33852,13.22506))));
        waypointEntries.add(new WaypointEntry(new RouteWaypoint(new GeoCoordinate(52.43852,13.12506))));
        waypointEntries.add(new WaypointEntry(new RouteWaypoint(new GeoCoordinate(52.37085,13.27242))));

        waypointList.setEntries(waypointEntries);
    }

    private void prepareTransportModePanel() {
        transportModePanel.setAdapter(
                new SimpleTransportModePanelAdapter(this, Arrays.asList(
                        RouteOptions.TransportMode.BICYCLE,
                        RouteOptions.TransportMode.PEDESTRIAN,
                        RouteOptions.TransportMode.TRUCK,
                        RouteOptions.TransportMode.CAR)));
        transportModePanel.setSelectedTransportMode(RouteOptions.TransportMode.CAR);
        transportModePanel.setOnSelectedListener(new TransportModePanel.OnSelectedListener() {
            @Override
            public void onSelected(int index, TabView tabView) {
                Log.d(LOG_TAG, "TransportModePanel: onSelected");
                calculateRoutes();
            }

            @Override
            public void onUnselected(int index, TabView tabView) {
                Log.d(LOG_TAG, "TransportModePanel: onUnselected");
            }

            @Override
            public void onReselected(int index, TabView tabView) {
                Log.d(LOG_TAG, "TransportModePanel: onReselected");
            }
        });
    }

    private void calculateRoutes() {
        RouteOptions routeOptions = new RouteOptions();
        routeOptions.setRouteCount(5);
        routeOptions.setTransportMode(transportModePanel.getSelectedTransportMode());

        List<RouteWaypoint> waypoints = waypointList.getRouteWaypoints();

        RouteCalculator.getInstance().calculateRoute(waypoints, routeOptions, routeResultList -> {
            // for demo purposes we show only the first route
            RouteCalculator.getInstance().showRoute(map, routeResultList.get(0));
        });
    }

    public void onRouteDetailsButtonClick(View view) {
        startActivity(new Intent(this, RouteDetailsActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapInitializer.onRequestPermissionsResult(requestCode, grantResults);
    }
}
