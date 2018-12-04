package com.here.msdkuiapp.guidance

import com.here.android.mpa.guidance.NavigationManager
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewData
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewPresenter
import com.here.msdkui.guidance.GuidanceEstimatedArrivalView
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.testutils.BaseTest
import junit.framework.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.util.*

/**
 * Tests for [GuidanceEstimatedArrivalViewFragment].
 */
class GuidanceEstimatedArrivalViewFragmentTest :BaseTest() {

    lateinit var guidanceEstimatedArrivalViewFragment: GuidanceEstimatedArrivalViewFragment

    @Before
    fun setup() {
        super.setUp()
        navigationManager = mock(NavigationManager::class.java)
        guidanceEstimatedArrivalViewFragment = GuidanceEstimatedArrivalViewFragment.newInstance()
    }

    @Test
    fun testPanelCreation() {
        addFrag(guidanceEstimatedArrivalViewFragment,
                GuidanceEstimatedArrivalViewFragment::class.java.name)
        assertNotNull(guidanceEstimatedArrivalViewFragment)
        assertNotNull(guidanceEstimatedArrivalViewFragment.view)
    }

    @Test
    fun testOnPause() {
        guidanceEstimatedArrivalViewFragment.viewPresenter = mock(GuidanceEstimatedArrivalViewPresenter::class.java)
        guidanceEstimatedArrivalViewFragment.onPause()
        verify(guidanceEstimatedArrivalViewFragment.viewPresenter!!).pause()
    }

    @Test
    fun testOnDataChangedCallback() {
        // calling onManeuverData will update panel
        addFrag(guidanceEstimatedArrivalViewFragment, GuidanceNextManeuverFragment::class.java.name)
        val data = mock(GuidanceEstimatedArrivalViewData::class.java)
        val date = mock(Date::class.java)
        `when`(data.eta).thenReturn(date)
        guidanceEstimatedArrivalViewFragment.onDataChanged(data)
        Assert.assertNotNull((guidanceEstimatedArrivalViewFragment.view as GuidanceEstimatedArrivalView).estimatedArrivalData)
    }
}