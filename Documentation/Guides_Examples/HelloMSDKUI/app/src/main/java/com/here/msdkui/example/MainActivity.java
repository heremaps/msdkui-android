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

package com.here.msdkui.example;

import android.os.Bundle;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.msdkui.routing.WaypointEntry;
import com.here.msdkui.routing.WaypointList;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Shows a WaypointList where each waypoint has a custom name. Usually
 * this can be used to show reverse geocoded addresses. Here we show the
 * traditional "Hello World" message instead.
 */
public class MainActivity extends AppCompatActivity {

    private MapInitializer mapInitializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapInitializer = new MapInitializer(this, this::onMapLoaded);
    }

    private void onMapLoaded(Map hereMap) {
        Toast.makeText(this, "HERE MSDK UI Kit version: " +
                com.here.msdkui.BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show();

        List<WaypointEntry> waypointEntries = new ArrayList<>();
        waypointEntries.add(new WaypointEntry(new RouteWaypoint(
                new GeoCoordinate(52.53852, 13.42506)),
                "Hello"));
        waypointEntries.add(new WaypointEntry(new RouteWaypoint(
                new GeoCoordinate(52.53853, 13.42507)),
                "HERE Mobile SDK UI Kit!"));

        WaypointList waypointList = findViewById(R.id.waypointList);
        waypointList.setEntries(waypointEntries);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapInitializer.onRequestPermissionsResult(requestCode, grantResults);
    }
}
