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

package com.here;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ApplicationProvider;

import com.here.msdkui.R;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

/**
 * Base class for Roboelectric tests.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, application = TestApplication.class)
public abstract class RobolectricTest {

    private ActivityController<FragmentActivity> mFragmentActivityController;

    /**
     * Build an fragment activity.
     */
    public void setUp() {
        mFragmentActivityController = Robolectric.buildActivity(FragmentActivity.class).create();
        FragmentActivity fragmentActivity = mFragmentActivityController
                .start()
                .resume()
                .get();
        fragmentActivity.setTheme(R.style.MSDKUIDarkTheme);
    }

    /**
     * Gets getSupportFragmentManager.
     */

    public FragmentManager getSupportFragmentManager() {
        return mFragmentActivityController.get().getSupportFragmentManager();
    }

    /**
     * Gets Application context.
     */
    public Context getApplicationContext() {
        return ApplicationProvider.getApplicationContext();
    }

    /**
     * Gets context and attach material theme.
     */
    public Context getContextWithTheme() {
        final Context context = ApplicationProvider.getApplicationContext();
        context.setTheme(R.style.MSDKUIDarkTheme);
        return context;
    }

    /**
     * Get string utility method
     */
    public String getString(int id) {
        return getApplicationContext().getString(id);
    }

    /**
     * Gets FragmentActivity ActivityController.
     */
    public ActivityController<FragmentActivity> getActivityController() {
        return mFragmentActivityController;
    }
}

