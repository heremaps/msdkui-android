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

package com.here.msdkui.common;

import android.content.Context;

import com.here.RobolectricTest;
import com.here.msdkui.common.measurements.UnitSystem;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Tests for {@link BaseView}.
 */
public class BaseViewTest extends RobolectricTest {

    @Test
    public void testSetGetUnitSystem() {
        ConcreteBaseView concreteBaseView = new ConcreteBaseView(getApplicationContext());
        assertEquals(concreteBaseView.getUnitSystem(), UnitSystem.METRIC);
        concreteBaseView.setUnitSystem(UnitSystem.IMPERIAL_UK);
        assertEquals(concreteBaseView.getUnitSystem(), UnitSystem.IMPERIAL_UK);
        concreteBaseView.setSaveEnabled(true);
        assertTrue(concreteBaseView.mSaveStateEnabled);
    }

    /**
     * Concrete class of {@link BaseView} to test it.
     */
    static class ConcreteBaseView extends BaseView {
        /**
         * Required constructor.
         */
        public ConcreteBaseView(Context context) {
            super(context);
        }
    }
}
