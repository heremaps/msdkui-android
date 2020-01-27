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

package com.here.msdkuiapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.here.msdkuiapp.base.BaseActivity
import com.here.msdkuiapp.common.PermissionsUtils
import com.here.msdkuiapp.landing.LandingActivity
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Main activity is the entry point for different components like routing, searching and etc of this application.
 * Since only routing is supported/implemented for now, this activity is starting Routing directly.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class SplashActivity : BaseActivity(), PermissionsUtils.StorageStateListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            start()
        } else {
            PermissionsUtils.checkStoragePermission(this, this)
        }
    }

    private fun start() {
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
        // finish this activity now, this activity is not needed anymore because there is no other component have
        // this activity as entry point.
        finish()
    }

    override fun onStoragePermissionOK() {
        start()
    }

    override fun onStoragePermissionFailed() {
        Toast.makeText(this, getString(R.string.msdkui_app_storage_permission_not_granted), Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionsUtils.onRequestPermissionsResult(requestCode, grantResults, this)
    }
}
