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

import android.content.Context
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Tests for [BasePresenter]
 */
class BasePresenterTest {

    class BasePresenterImpl : BasePresenter<Any>() {

        internal var pause = false
        internal var resume = false

        override fun pause() {
            pause = true
        }

        override fun resume() {
            resume = true
        }
    }

    lateinit var presenter: BasePresenter<Any>

    @Mock
    lateinit var contractImpl: BaseContract<Any>

    @Before
    public fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = BasePresenterImpl()
    }

    @Test
    public fun testInit() {
        val context = mock(Context::class.java)
        presenter.onAttach(context, contractImpl)
        assertNotNull(presenter.mView)
        assertNotNull(presenter.context)
        presenter.contract
        verify(contractImpl).getCurrentViewContract()
        presenter.pause()
        assertTrue((presenter as BasePresenterImpl).pause)
        presenter.resume()
        assertTrue((presenter as BasePresenterImpl).resume)
    }
}