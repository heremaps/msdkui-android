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

package com.here.msdkuiapp.base

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.here.android.mpa.common.MapSettings
import com.here.msdkuiapp.common.AppActionBar
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import java.io.File

/**
 * Activity class that can serve as Base for other activities (who have coordinator) to get custom action bar, back button
 * handling out of box.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
abstract class BaseActivity : AppCompatActivity() {

    lateinit var appActionBar: AppActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapSettings.setIsolatedDiskCacheRootPath(getMapDataPath(), "com.here.msdkuiapp.AppService")
        appActionBar = AppActionBar(this).setUpActionBar()
    }

    override fun onBackPressed() {
        coordinator?.let {
            if (!it.onBackPressed()) {
                finish()
            }
        } ?: finish()
    }

    private fun getMapDataPath(): String {
        return Environment.getExternalStorageDirectory()
                .absolutePath + File.separator + ".msdkui-data"
    }

    /**
     * subclasses can override to give references of their coordinator.
     */
    open var coordinator: BaseFragmentCoordinator? = null
}