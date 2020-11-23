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

package com.here.msdkuiapp.guidance

import androidx.fragment.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.here.msdkuiapp.R
import com.here.msdkuiapp.about.AboutActivity
import com.here.msdkuiapp.landing.LandingActivity
import com.here.testutils.BaseTest
import com.here.testutils.argumentCaptor
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController
import org.robolectric.util.ReflectionHelpers

/**
 * Tests for [GuidanceActivity].
 */
class GuidanceActivityTest : BaseTest() {

    private lateinit var activityController: ActivityController<GuidanceActivity>

    @Mock
    private lateinit var mockCoordinator: GuidanceCoordinator

    @Before
    override fun setUp() {
        MockitoAnnotations.initMocks(this)
        activityController = Robolectric.buildActivity(GuidanceActivity::class.java, Intent().
                putExtra("GUIDANCE_IS_SIMULATION_KEY", false))
    }

    @Test
    fun testGuidanceCoordinatorNotInitialized() {
        makeIsLocationOkReturnTrue()
        assertNotNull(activityController.create().get().guidanceCoordinator)
    }

    @Test
    fun testUIWhenLocationIsOk() {
        mockGuidanceCoordinator()
        makeIsLocationOkReturnTrue()
        Assert.assertTrue(activityController.get().isLocationOk())
        activityController.create().get()
        verify(mockCoordinator).start()
    }

    @Test
    fun testUIWhenLocationIsNotOk() {
        mockGuidanceCoordinator()
        activityController.create()
        val startedIntent = shadowOf(activityController.get()).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(LandingActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun testDestroy() {
        mockGuidanceCoordinator()
        activityController.create()
        activityController.destroy()
        verify(mockCoordinator).destroy()
    }

    @Test
    fun testDataIsNotLostWhenActivityGetsRecreated() {
        // Create activity with real GuidanceCoordinator instance and save activity's
        // state to bundle.
        var bundle = Bundle()
        var fragMgr = mock(FragmentManager::class.java)
        var localCoordinator = GuidanceCoordinator(applicationContext, fragMgr)
        with(activityController) {
            get().guidanceCoordinator = localCoordinator
            assertNotNull(get().guidanceCoordinator)
        }
        activityController.create()
        localCoordinator.didGuidanceFinished = true
        activityController
                .saveInstanceState(bundle)
                .destroy()

        localCoordinator.didGuidanceFinished = false

        // Bring up a new activity using state saved in bundle.
        activityController = Robolectric.buildActivity(GuidanceActivity::class.java!!)
        makeIsLocationOkReturnTrue()
        activityController.create(bundle)
        assertTrue(activityController.get().guidanceCoordinator!!.didGuidanceFinished)
    }

    @Test
    fun testSetAndGetCoordinator() {
        with(activityController) {
            get().coordinator = mockCoordinator
            assertNotNull(get().coordinator)
        }
    }

    @Test
    fun testBackPressWhenCoordinator() {
        `when`(mockCoordinator.onBackPressed()).thenReturn(false)
        val activity = activityController.get()
        with(activity) {
            guidanceCoordinator = mockCoordinator
            onBackPressed()
        }
        verify(mockCoordinator).onBackPressed()
        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(LandingActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun testBackPressWhenNoCoordinator() {
        `when`(mockCoordinator.onBackPressed()).thenReturn(false)
        val activity = activityController.get()
        activity.onBackPressed()
        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(LandingActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun testDashBoardBottomSheetBehavior() {
        mockGuidanceCoordinator()
        makeIsLocationOkReturnTrue()
        val activity = activityController.create().get()
        val bottomSheetBehavior = BottomSheetBehavior.from(
                activity.findViewById<GuidanceDashBoardView>(R.id.guidance_dashboard_view))
        val grayedScreenFirsPart = activity.findViewById<View>(R.id.grayed_screen_view_first_part)
        val collapsedView = activity.findViewById<View>(R.id.collapsed_view)

        collapsedView.performClick()
        assertEquals(bottomSheetBehavior.state, BottomSheetBehavior.STATE_EXPANDED)

        collapsedView.performClick()
        assertEquals(bottomSheetBehavior.state, BottomSheetBehavior.STATE_COLLAPSED)

        // expand dashboard again, to test is it closeable by clicking on grayed map
        collapsedView.performClick()
        grayedScreenFirsPart.performClick()
        assertEquals(bottomSheetBehavior.state, BottomSheetBehavior.STATE_COLLAPSED)
    }

    @Test
    fun testDashBoardBottomSheetCallback() {
        val mockBottomSheetBehavior = mock(BottomSheetBehavior<View>().javaClass)
        val view = mock(View::class.java)

        mockGuidanceCoordinator()
        makeIsLocationOkReturnTrue()
        val activity = activityController.get()
        activity.bottomSheetBehavior = mockBottomSheetBehavior

        activityController.create()

        val captor = argumentCaptor<BottomSheetBehavior.BottomSheetCallback>()
        verify(mockBottomSheetBehavior).addBottomSheetCallback(captor.capture())

        val grayedScreenFirsPart = activity.findViewById<View>(R.id.grayed_screen_view_first_part)

        captor.value.onStateChanged(view, BottomSheetBehavior.STATE_COLLAPSED)
        assertEquals(grayedScreenFirsPart.visibility, View.GONE)

        captor.value.onStateChanged(view, BottomSheetBehavior.STATE_EXPANDED)
        assertEquals(grayedScreenFirsPart.visibility, View.VISIBLE)

        captor.value.onStateChanged(view, BottomSheetBehavior.STATE_SETTLING)
        captor.value.onSlide(view, 0.5f)
        // nothing to test on these actions
    }

    @Test
    fun testClickItemsOnItemsList() {
        mockGuidanceCoordinator()
        makeIsLocationOkReturnTrue()
        val activity = activityController.create().get()

        val collapsedView = activity.findViewById<View>(R.id.collapsed_view)
        collapsedView.performClick()

        val recyclerView = activity.findViewById<RecyclerView>(R.id.items_list)
        assertNotNull(recyclerView)
        recyclerView.measure(0, 0)
        recyclerView.layout(0, 0, 100, 10000)
        recyclerView.getChildAt(1).performClick()
        assertActivityStarted(activity, AboutActivity::class.java)
    }

    @Test
    fun testStopNavigationButtonClick() {
        mockGuidanceCoordinator()
        makeIsLocationOkReturnTrue()
        val activity = activityController.create().get()
        activity.findViewById<View>(R.id.stop_navigation).performClick()
        assertActivityStarted(activity, LandingActivity::class.java)
    }

    private fun mockGuidanceCoordinator() {
        with(activityController) {
            get().guidanceCoordinator = mockCoordinator
            assertNotNull(get().guidanceCoordinator)
        }
    }

    private fun makeIsLocationOkReturnTrue() {
        val locationManager = activityController.get().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val shadowLocationManager = shadowOf(locationManager)
        shadowLocationManager.setProviderEnabled(LocationManager.GPS_PROVIDER, true)
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 19)
    }
}