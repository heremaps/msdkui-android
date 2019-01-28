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

package com.here;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.here.msdkui.BuildConfig;
import com.here.msdkui.R;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.robolectric.RuntimeEnvironment.application;

/**
 * Base class for Roboelectric tests.
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = TestApplication.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*", "org.powermock.*" })
public abstract class RobolectricTest {
    @Rule
    public PowerMockRule mRule = new PowerMockRule();

    private FragmentActivity mFragmentActivity;

    /**
     * Build an fragment activity.
     */
    public void setUp() {
        mFragmentActivity = Robolectric.buildActivity(FragmentActivity.class)
                .create()
                .start()
                .resume()
                .get();
        mFragmentActivity.setTheme(R.style.MSDKUIDarkTheme);
    }

    /**
     * Gets FragmentManager.
     */
    public android.app.FragmentManager getFragmentManager() {
        return mFragmentActivity.getFragmentManager();
    }

    /**
     * Gets getSupportFragmentManager.
     */
    public FragmentManager getSupportFragmentManager() {
        return mFragmentActivity.getSupportFragmentManager();
    }

    /**
     * Gets Application context.
     */
    public Context getApplicationContext() {
        return application.getApplicationContext();
    }

    /**
     * Gets Activity context.
     */
    public Context getActivityContext() {
        return mFragmentActivity;
    }

    /**
     * Gets context and attach material theme.
     */
    public Context getContextWithTheme() {
        final Context context = RuntimeEnvironment.application.getApplicationContext();
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
     * Gets FragmentActivity.
     */
    public FragmentActivity getFragmentActivity() {
        return mFragmentActivity;
    }

    /**
     * Add fragment to the activity with given tag.
     */
    public void addFragment(final Fragment fragment, final String tag) {
        final FragmentManager fragmentManager = mFragmentActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragment, tag);
        transaction.commit();
    }
}

