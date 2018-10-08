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

package com.here.msdkuiapp.utils

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.here.msdkuiapp.R

/**
 * Container for fragment, useful to isolate fragment for testing.
 */
class FragmentTestActivityUtils : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_container)
    }

    fun setFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
                .add(R.id.contentFrame, fragment, "TEST")
                .commit()
    }

    fun replaceFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, fragment).commit()
    }
}