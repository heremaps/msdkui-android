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

package com.here.msdkui.routing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.shadows.ShadowDialog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * An option item to set a numeric value.
 */
public class NumericOptionItemTest extends RobolectricTest {

    private NumericOptionItem mNumericOptionItem;

    @Before
    public void setUp() {
        mNumericOptionItem = new NumericOptionItem(getContextWithTheme());
    }

    @Test
    public void testInitUi() {
        final TextView labelView = mNumericOptionItem.findViewById(R.id.numeric_item_label);
        final TextView valueView = mNumericOptionItem.findViewById(R.id.numeric_item_value);
        assertNotNull(labelView);
        assertThat(labelView.getVisibility(), equalTo(View.VISIBLE));
        assertNotNull(valueView);
        assertThat(valueView.getVisibility(), equalTo(View.VISIBLE));
    }

    @Test
    public void testSetValue() {
        Number value = 5;

        final TextView valueView = mNumericOptionItem.findViewById(R.id.numeric_item_value);
        assertNotNull(valueView);

        mNumericOptionItem.setValue(value);
        assertEquals(String.valueOf(value), valueView.getText().toString());

        mNumericOptionItem.setValue(null);
        assertEquals(getApplicationContext().getString(R.string.msdkui_undefined), valueView.getText().toString());

        mNumericOptionItem.setValue(0.0f / 0.0f);
        assertEquals(getApplicationContext().getString(R.string.msdkui_undefined), valueView.getText().toString());
    }

    @Test
    public void testLabel() {
        mNumericOptionItem.setLabel(getString(R.string.msdkui_violate_truck_options));
        final TextView labelView = mNumericOptionItem.findViewById(R.id.numeric_item_label);
        assertThat(labelView.getText().toString(),
                equalTo(getApplicationContext().getString(R.string.msdkui_violate_truck_options)));
        assertThat(mNumericOptionItem.getLabel(),
                equalTo(getApplicationContext().getString(R.string.msdkui_violate_truck_options)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullLabelException() {
        mNumericOptionItem.setLabel(null);
    }

    @Test
    public void testValueEditDialogPositive() {
        int newValue = 10;
        mNumericOptionItem.setLabel(getString(R.string.msdkui_violate_truck_options));
        final TextView valueView = mNumericOptionItem.findViewById(R.id.numeric_item_value);

        assertNotNull(valueView);
        valueView.performClick();

        AlertDialog alertDialog = (AlertDialog) ShadowDialog.getLatestDialog();
        assertTrue(alertDialog.isShowing());

        EditText valueEdit = alertDialog.findViewById(R.id.numeric_item_value_text);
        valueEdit.setText(String.valueOf(newValue));
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        assertEquals(newValue, mNumericOptionItem.getValue().intValue());
    }

    @Test
    public void testValueEditDialogNegative() {
        mNumericOptionItem.setLabel(getString(R.string.msdkui_violate_truck_options));
        final TextView valueView = mNumericOptionItem.findViewById(R.id.numeric_item_value);

        assertNotNull(valueView);
        valueView.performClick();

        AlertDialog alertDialog = (AlertDialog) ShadowDialog.getLatestDialog();
        assertTrue(alertDialog.isShowing());

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        assertFalse(alertDialog.isShowing());
    }
}

