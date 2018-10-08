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

package com.here.msdkuiapp.guidance

import com.here.testutils.BaseTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Tests for [GuidanceDashBoardPresenter].
 */
class GuidanceDashBoardPresenterTest : BaseTest() {

    private lateinit var presenter: GuidanceDashBoardPresenter

    @Before
    override fun setUp() {
        super.setUp()
        presenter = GuidanceDashBoardPresenter()
    }

    @Test
    fun testAddRemoveListener() {
        val listener = mock(GuidanceDashBoardViewListener::class.java)
        presenter.addListener(listener)
        callAllPresenterActionFunctions(10)
        verify(listener).onCollapsed()
        verify(listener).onCollapsedViewClicked()
        verify(listener).onExpanded()
        verify(listener).onStopNavigationButtonClicked()
        verify(listener).onItemClicked(10)

        presenter.removeListener(listener)
        callAllPresenterActionFunctions(10)
        verifyNoMoreInteractions(listener)
    }

    private fun callAllPresenterActionFunctions(itemClickedIndex: Int) {
        presenter.onCollapsed()
        presenter.onCollapsedViewClicked()
        presenter.onExpanded()
        presenter.onStopNavigationButtonClicked()
        presenter.onItemClicked(itemClickedIndex)
    }
}