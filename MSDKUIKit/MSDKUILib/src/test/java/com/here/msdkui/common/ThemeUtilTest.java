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

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.here.RobolectricTest;
import com.here.msdkui.R;

import org.junit.Test;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ThemeUtil}.
 */
public class ThemeUtilTest extends RobolectricTest {

    @Test
    public void testGetDrawableWhenTypeValueIsNull() {
        TypedArray typedArray = mock(TypedArray.class);
        when(typedArray.peekValue(anyInt())).thenReturn(null);
        assertNull(ThemeUtil.getDrawable(getApplicationContext(), typedArray, anyInt()));
    }

    @Test
    public void testGetDrawableWhenTypeIsReference() {
        TypedArray typedArray = mock(TypedArray.class);
        TypedValue typedValue = spy(new TypedValue());
        typedValue.type = TypedValue.TYPE_REFERENCE;
        when(typedArray.peekValue(anyInt())).thenReturn(typedValue);
        ThemeUtil.getDrawable(getApplicationContext(), typedArray, anyInt());
        verify(typedArray).getDrawable(anyInt());
    }

    @Test
    public void testGetDrawableWhenTypeIsColor() {
        TypedArray typedArray = mock(TypedArray.class);
        TypedValue typedValue = spy(new TypedValue());
        typedValue.type = TypedValue.TYPE_INT_COLOR_ARGB8;
        when(typedArray.peekValue(anyInt())).thenReturn(typedValue);
        Drawable drawable = ThemeUtil.getDrawable(getApplicationContext(), typedArray, anyInt());
        assertTrue(drawable instanceof ColorDrawable);
        assertThat(((ColorDrawable) drawable).getColor(), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDrawableWhenTypeIsSomethingElse() {
        TypedArray typedArray = mock(TypedArray.class);
        TypedValue typedValue = spy(new TypedValue());
        typedValue.type = TypedValue.TYPE_FLOAT;
        when(typedArray.peekValue(anyInt())).thenReturn(typedValue);
        ThemeUtil.getDrawable(getApplicationContext(), typedArray, anyInt());
    }

    @Test
    public void testGetString() {
        final String valueStr = "value";
        TypedArray typedArray = mock(TypedArray.class);
        TypedValue typedValue = spy(new TypedValue());
        when(typedArray.peekValue(anyInt())).thenReturn(typedValue);
        when(typedArray.getString(anyInt())).thenReturn(valueStr);
        String ret = ThemeUtil.getString(typedArray, anyInt());
        assertThat(ret, is(valueStr));
    }

    @Test
    public void testGetStringWhenTypeValueIsNull() {
        final String valueStr = "value";
        TypedArray typedArray = mock(TypedArray.class);
        when(typedArray.getString(anyInt())).thenReturn(valueStr);
        String ret = ThemeUtil.getString(typedArray, anyInt());
        assertNull(ret);
    }

    @Test
    public void testGetColor() {
        int color = ThemeUtil.getColor(getApplicationContext(), R.attr.colorAccentLight);
        assertNotSame(0, color);
        color = ThemeUtil.getColor(getApplicationContext(), R.attr.actionBarDivider);
        assertNotSame(Color.CYAN, color);  // in case of error
    }

    @Test
    public void testStyleId() {
        int style = ThemeUtil.getStyleId(getContextWithTheme(), R.attr.guidanceManeuverPanelSecondaryText);
        assertNotSame(0, style);
    }
}
