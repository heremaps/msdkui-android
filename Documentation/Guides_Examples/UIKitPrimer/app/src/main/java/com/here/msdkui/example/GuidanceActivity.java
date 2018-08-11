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

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.routing.Route;
import com.here.msdkui.guidance.GuidanceManeuverData;
import com.here.msdkui.guidance.GuidanceManeuverPanel;
import com.here.msdkui.guidance.GuidanceManeuverPanelListener;
import com.here.msdkui.guidance.GuidanceManeuverPanelPresenter;

import helper.GuidanceSimulator;
import helper.MapInitializer;
import helper.RouteCalculator;

/**
 * Shows usage of GuidanceManeuverPanel and how to fed maneuver data into it while
 * running guidance simulation.
 */
public class GuidanceActivity extends AppCompatActivity {

    private static final String LOG_TAG = GuidanceActivity.class.getName();

    private MapInitializer mapInitializer;
    private Map map;
    private GuidanceManeuverPanel guidanceManeuverPanel;
    private GuidanceManeuverPanelPresenter guidanceManeuverPanelPresenter;
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

        guidanceManeuverPanel = findViewById(R.id.guidanceManeuverPanel);
        guidanceManeuverPanelPresenter = new GuidanceManeuverPanelPresenter(this, NavigationManager.getInstance(), route);
        guidanceManeuverPanelPresenter.addListener(new GuidanceManeuverPanelListener() {
            @Override
            public void onDataChanged(GuidanceManeuverData guidanceManeuverData) {
                Log.d(LOG_TAG, "onDataChanged: 1st line: " + guidanceManeuverData.getInfo1());
                Log.d(LOG_TAG, "onDataChanged: 2nd line: " + guidanceManeuverData.getInfo2());
                guidanceManeuverPanel.setManeuverData(guidanceManeuverData);
            }

            @Override
            public void onDestinationReached() {
                Log.d(LOG_TAG, "onDestinationReached");
                guidanceManeuverPanel.highLightManeuver(Color.BLUE);
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
        if (route == null || guidanceManeuverPanelPresenter == null) {
            return;
        }

        guidanceManeuverPanelPresenter.resume();
        GuidanceSimulator.getInstance().startGuidanceSimulation(route, map);
    }

    private void stopGuidanceSimulation() {
        if (guidanceManeuverPanelPresenter == null) {
            return;
        }

        guidanceManeuverPanelPresenter.pause();
        GuidanceSimulator.getInstance().stopGuidance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapInitializer.onRequestPermissionsResult(requestCode, grantResults);
    }
}
