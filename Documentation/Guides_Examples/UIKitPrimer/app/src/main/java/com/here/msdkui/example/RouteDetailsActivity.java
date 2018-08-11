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
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.here.android.mpa.routing.Route;
import com.here.msdkui.routing.CustomRecyclerView;
import com.here.msdkui.routing.ManeuverDescriptionItem;
import com.here.msdkui.routing.ManeuverDescriptionList;
import com.here.msdkui.routing.RouteDescriptionItem;
import com.here.msdkui.routing.RouteDescriptionList;

import java.util.ArrayList;

import helper.RouteCalculator;

/**
 * Shows a RouteDescriptionList where a user can select a route and sees the belonging
 * maneuvers in a ManeuverDescriptionList.
 */
public class RouteDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = RouteDetailsActivity.class.getName();

    private RouteDescriptionList routeDescriptionList;
    private ManeuverDescriptionList maneuverDescriptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        routeDescriptionList = findViewById(R.id.routeDescriptionList);
        maneuverDescriptionList = findViewById(R.id.maneuverDescriptionList);

        if (RouteCalculator.getInstance().lastCalculatedRouteResults.size() == 0) {
            Log.d(LOG_TAG, "No valid routes yet.");
            routeDescriptionList.setRoutes(new ArrayList<>());
            return;
        }

        routeDescriptionList.setBackgroundColor(Color.argb(255, 238, 238, 238));
        routeDescriptionList.setRoutesResult(RouteCalculator.getInstance().lastCalculatedRouteResults);
        routeDescriptionList.setOnItemClickedListener(new CustomRecyclerView.OnItemClickedListener() {
            @Override
            public void onItemClicked(int index, View view) {
                RouteDescriptionItem routeDescriptionItem = (RouteDescriptionItem) view;
                Route selectedRoute = routeDescriptionItem.getRoute();
                maneuverDescriptionList.setRoute(selectedRoute);

                RouteCalculator.getInstance().selectedRoute = selectedRoute;
                Log.d(LOG_TAG, "Selected route: " + selectedRoute.toString());
            }

            @Override
            public void onItemLongClicked(int i, View view) {
            }
        });

        maneuverDescriptionList.setRoute(RouteCalculator.getInstance().selectedRoute);
        maneuverDescriptionList.setOnItemClickedListener(new CustomRecyclerView.OnItemClickedListener() {
            @Override
            public void onItemClicked(int index, View view) {
                ManeuverDescriptionItem maneuverDescriptionItem = (ManeuverDescriptionItem) view;
                Log.d(LOG_TAG, "Selected maneuver: " + maneuverDescriptionItem.getManeuver().toString());
            }

            @Override
            public void onItemLongClicked(int i, View view) {
            }
        });
    }

    public void onStartGuidanceButtonClick(View view) {
        startActivity(new Intent(this, GuidanceActivity.class));
    }
}
