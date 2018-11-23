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

package com.here.msdkuiapp.about

import android.os.Bundle
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseActivity
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Activity class to show about information.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        appActionBar.run {
            setBack(true, R.drawable.ic_clear_black_24dp)
            setTitle(value = getString(R.string.msdkui_app_about))
            setRightIcon(false)
        }
        app_version.text = com.here.msdkuiapp.BuildConfig.VERSION_NAME
        ui_kit_version.text = com.here.msdkui.BuildConfig.VERSION_NAME
        here_sdk_version.text = com.here.android.mpa.common.Version.getSdkVersion()
    }
}
