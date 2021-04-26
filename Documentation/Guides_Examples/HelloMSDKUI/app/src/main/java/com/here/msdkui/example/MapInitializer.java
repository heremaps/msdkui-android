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

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.here.android.mpa.common.MapSettings;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Convenience class to request the required Android permissions as defined by manifest and
 * to initialize the HERE map - does not contain any MSDK UI Kit related code.
 */
public class MapInitializer {

    private static final String LOG_TAG = MapInitializer.class.getName();
    private static final int PERMISSIONS_REQUEST_CODE = 42;

    private final ResultListener resultListener;
    private final AppCompatActivity activity;

    public interface ResultListener {
        void onMapInitDone(Map map);
    }

    public MapInitializer(@NonNull AppCompatActivity activity, @NonNull ResultListener resultListener) {
        this.activity = activity;
        this.resultListener = resultListener;

        String[] missingPermissions = getPermissionsToRequest();
        if (missingPermissions.length == 0) {
            initializeMap();
        } else {
            ActivityCompat.requestPermissions(activity, missingPermissions, PERMISSIONS_REQUEST_CODE);
        }
    }

    private void initializeMap() {
        String cacheLocation =
                activity.getApplicationContext().getFilesDir().getPath() + File.separator + "example_maps_cache";
        MapSettings.setDiskCacheRootPath(cacheLocation);
        AndroidXMapFragment mapFragment = (AndroidXMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.init(error -> {
            if (error == OnEngineInitListener.Error.NONE) {
                mapFragment.getPositionIndicator().setVisible(true);
                resultListener.onMapInitDone(mapFragment.getMap());
            } else {
                Log.e(LOG_TAG, "Cannot initialize Map Fragment: " + error.getDetails());
            }
        });
    }

    private String[] getPermissionsToRequest() {
        ArrayList<String> permissionList = new ArrayList<>();
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(
                    activity.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (packageInfo.requestedPermissions != null) {
                for (String permission : packageInfo.requestedPermissions) {
                    if (ContextCompat.checkSelfPermission(
                            activity, permission) != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(permission);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Cannot read permissions from manifest: " + e.getMessage());
        }
        return permissionList.toArray(new String[permissionList.size()]);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                allGranted &= result == PackageManager.PERMISSION_GRANTED;
            }

            if (allGranted) {
                initializeMap();
            } else {
                Log.e(LOG_TAG, "Required Android permissions denied by user.");
            }
        }
    }
}
