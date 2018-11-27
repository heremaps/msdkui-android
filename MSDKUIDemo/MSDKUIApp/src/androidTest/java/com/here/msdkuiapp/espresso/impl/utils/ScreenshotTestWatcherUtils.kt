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

import android.graphics.Bitmap
import android.os.Build
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.runner.screenshot.Screenshot
import com.here.msdkuiapp.espresso.impl.utils.CurrentActivityUtils
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Utility class to capture device screenshot while any test is failed.
 *
 * To use this class, define below rule on top of class <code>
 *       @Rule
 *       @JvmField
 *       public val sreenshotTestWatcher = TestRule(ScreenshotTestWatcherUtils::class.java)  </code>
</p>
 */
class ScreenshotTestWatcherUtils : TestWatcher() {

    override fun failed(e: Throwable, description: Description) {
        val bitmap: Bitmap = if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            getInstrumentation().uiAutomation.takeScreenshot()
        } else {
            // only in-app view-elements are visible.
            Screenshot.capture(CurrentActivityUtils.currentActivity).bitmap
        }

        // Save to external storage '/storage/emulated/0/Android/data/[package name app]/cache/screenshots/'.
        val folder = File(getTargetContext().externalCacheDir!!.absolutePath + "/screenshots/")
        if (!folder.exists()) {
            folder.mkdirs()
        }

        storeBitmap(bitmap, folder.getPath() + "/" + getFileName(description))
    }

    private fun getFileName(description: Description): String {
        val className = description.className
        val methodName = description.methodName
        val dateTime = Calendar.getInstance().time.toString()
        return "$className-$methodName-$dateTime.png"
    }

    private fun storeBitmap(bitmap: Bitmap, path: String) {
        BufferedOutputStream(FileOutputStream(path)).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}