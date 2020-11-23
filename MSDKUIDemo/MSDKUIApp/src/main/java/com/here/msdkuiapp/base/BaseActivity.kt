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

package com.here.msdkuiapp.base

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.here.android.mpa.common.DiskCacheUtility
import com.here.android.mpa.common.DiskCacheUtility.MigrationResult
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
    private val TAG = BaseActivity::class.java.name

    lateinit var appActionBar: AppActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val migrationResult = DiskCacheUtility.migrate(getMapDataPath(), getMapDataPathV2())
        if (migrationResult == MigrationResult.SUCCESS ||
            migrationResult == MigrationResult.FAILED ||
            migrationResult == MigrationResult.MISSING_OLD_CACHE) {
            // SUCCESS, FAILED and MISSING_OLD_CACHE are migration results that can occur during
            // first app launch after update. In this case all map data located in old path will be
            // removed because they are no longer used. From now app will use new path for map data
            // and it will be either successfully migrated data or completely new downloaded data.
            // In both cases next app launches migration result will be ALREADY_EXISTS and no
            // additional actions are needed.
            try {
                deleteRecursive(File(getMapDataPath()))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete old map data at: ${getMapDataPath()}", e)
            }
        }

        try {
            MapSettings.setDiskCacheRootPath(getMapDataPathV2())
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Failed to set disk cache root path", e)
        }

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

    private fun getMapDataPathV2(): String {
        return Environment.getExternalStorageDirectory()
                .absolutePath + File.separator + ".msdkui-data-v2"
    }

    @Throws(Exception::class)
    private fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) {
            for (child in fileOrDirectory.listFiles()) {
                try {
                    deleteRecursive(child)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to delete: ${child.absolutePath}", e)
                }
            }
        }
        fileOrDirectory.delete()
    }

    /**
     * subclasses can override to give references of their coordinator.
     */
    open var coordinator: BaseFragmentCoordinator? = null
}