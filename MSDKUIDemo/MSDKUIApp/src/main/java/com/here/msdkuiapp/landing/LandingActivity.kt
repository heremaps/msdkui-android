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

package com.here.msdkuiapp.landing

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.here.android.mpa.common.ApplicationContext
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.MapEngine
import com.here.android.mpa.common.OnEngineInitListener
import com.here.android.mpa.guidance.VoiceCatalog
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseActivity
import com.here.msdkuiapp.guidance.GuidanceRouteSelectionActivity
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import com.here.msdkuiapp.routing.RoutingActivity
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.activity_landing.*

/**
 * Activity class to show options to choose after splash screen.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class LandingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        MapEngine.getInstance().init(ApplicationContext(applicationContext)) { error ->
            if (error == OnEngineInitListener.Error.NONE) downloadVoicePackage(VoiceCatalog.getInstance())
        }
        setUpList()
    }

    private fun setUpList() {
        with(landing_list) {
            layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                LinearLayoutManager(this@LandingActivity, LinearLayoutManager.VERTICAL, false) else
                GridLayoutManager(this@LandingActivity, 2)
            setHasFixedSize(true)
        }

        val list = listOf<RowItem>(
                RowItem(getString(R.string.msdkui_app_rp_teaser_title), getString(R.string.msdkui_app_rp_teaser_description),
                        getString(R.string.msdkui_app_teaser_link), R.drawable.routeplanner_teaser),
                RowItem(getString(R.string.msdkui_app_guidance_teaser_title), getString(R.string.msdkui_app_guidance_teaser_description),
                        getString(R.string.msdkui_app_teaser_link), R.drawable.drivenav_teaser)
        )
        val adapter = LandingScreenAdapter(list, this@LandingActivity)
        adapter.itemListener = itemClickListener
        landing_list.adapter = adapter
    }

    private val itemClickListener = object : LandingScreenAdapter.Listener {
        override fun onItemClicked(view: View) {
            val position = landing_list.getChildLayoutPosition(view)
            when (position) {
                0 -> {
                    val intent = Intent(this@LandingActivity, RoutingActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(this@LandingActivity, GuidanceRouteSelectionActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        appPositioningManager?.run {
            if (isActive) {
                stop()
            }
        }
    }

    /**
     * Downloads english voice package for voice guidance.
     *
     * @param voiceCatalog [VoiceCatalog] to download the voice.
     */
    fun downloadVoicePackage(voiceCatalog: VoiceCatalog) {
        // english language female voice skin id. this id can be get from downloading catalog but we can skip it for now.
        val voiceSkinId = 201L
        if (!voiceCatalog.isLocalVoiceSkin(voiceSkinId)) {
            voiceCatalog.downloadVoice(voiceSkinId) { error ->
                if (error == VoiceCatalog.Error.NONE) {
                    navigationManager!!.voiceGuidanceOptions.voiceSkin = voiceCatalog.getLocalVoiceSkin(voiceSkinId)
                } else {
                    Log.e(LandingActivity::class.java.name, "Error while downloading voice package $error")
                }
            }
        } else {
            navigationManager!!.voiceGuidanceOptions.voiceSkin = voiceCatalog.getLocalVoiceSkin(voiceSkinId)
        }
    }
}
