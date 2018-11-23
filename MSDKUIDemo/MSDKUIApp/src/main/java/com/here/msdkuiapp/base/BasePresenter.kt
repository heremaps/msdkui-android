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

/**
 * Base presenter to be implemented by all guidance presenter.
 */
abstract class BasePresenter<T> {

    /**
     * View Contract to interact with presenter.
     */
    internal var mView: BaseContract<T>? = null

    /**
     * Gets context
     */
    var context: Context? = null

    /**
     * Gets associated contract
     */
    val contract: T?
        get() = mView?.getCurrentViewContract()

    /**
     * Should be called to attached view contracts with presenter.
     *
     * @param context [Context]
     * @param view View contract
     * @return true if presenter is in valid state, false otherwise
     */
    open fun onAttach(context: Context, view: BaseContract<T>): Boolean {
        mView = view
        this.context = context
        return isStateValid()
    }

    /**
     * Gets string resources
     *
     * @param id Id of string resources
     */
    fun getString(id: Int): String {
        return context!!.getString(id)
    }

    /**
     * Checks if presenter is in valid state.
     */
    open fun isStateValid(): Boolean {
        return true
    }

    /**
     * Pause the presenter. Implementation can be provided by subclasses, if needed.
     */
    open fun pause() {}

    /**
     * Resume the presenter.Implementation can be provided by subclasses, if needed.
     */
    open fun resume() {}
}