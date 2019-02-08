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

package com.here.msdkui.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.routing.Route;
import com.here.msdkui.guidance.GuidanceManeuverData;
import com.here.msdkui.guidance.GuidanceManeuverListener;
import com.here.msdkui.guidance.GuidanceManeuverPresenter;
import com.here.msdkui.guidance.GuidanceManeuverView;

import helper.GuidanceSimulator;
import helper.MapInitializer;
import helper.RouteCalculator;

/**
 * Shows usage of GuidanceManeuverView and how to fed maneuver data into it while
 * running guidance simulation.
 */
public class GuidanceActivity extends AppCompatActivity {

    private static final String LOG_TAG = GuidanceActivity.class.getName();

    private MapInitializer mapInitializer;
    private Map map;
    private GuidanceManeuverView guidanceManeuverView;
    private GuidanceManeuverPresenter guidanceManeuverPresenter;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);

        mapInitializer = new MapInitializer(this, this::onMapLoaded);
    }

    private void onMapLoaded(Map hereMap) {
        map = hereMap;
        route = RouteCalculator.getInstance().selectedRoute;

        guidanceManeuverView = findViewById(R.id.guidanceManeuverView);
        guidanceManeuverPresenter = new GuidanceManeuverPresenter(this, NavigationManager.getInstance(), route);
        guidanceManeuverPresenter.addListener(new GuidanceManeuverListener() {
            @Override
            public void onDataChanged(@Nullable GuidanceManeuverData guidanceManeuverData) {
                if (guidanceManeuverData != null) {
                    Log.d(LOG_TAG, "onDataChanged: 1st line: " + guidanceManeuverData.getInfo1());
                    Log.d(LOG_TAG, "onDataChanged: 2nd line: " + guidanceManeuverData.getInfo2());
                }
                guidanceManeuverView.setManeuverData(guidanceManeuverData);
            }

            @Override
            public void onDestinationReached() {
                Log.d(LOG_TAG, "onDestinationReached");
                guidanceManeuverView.highLightManeuver(Color.BLUE);
            }
        });

        startGuidanceSimulation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGuidanceSimulation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGuidanceSimulation();
    }

    public void onStopGuidanceButtonClick(View view) {
        stopGuidanceSimulation();
    }

    private void startGuidanceSimulation() {
        if (route == null || guidanceManeuverPresenter == null) {
            return;
        }

        guidanceManeuverPresenter.resume();
        GuidanceSimulator.getInstance().startGuidanceSimulation(route, map);
    }

    private void stopGuidanceSimulation() {
        if (guidanceManeuverPresenter == null) {
            return;
        }

        guidanceManeuverPresenter.pause();
        GuidanceSimulator.getInstance().stopGuidance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapInitializer.onRequestPermissionsResult(requestCode, grantResults);
    }
}
