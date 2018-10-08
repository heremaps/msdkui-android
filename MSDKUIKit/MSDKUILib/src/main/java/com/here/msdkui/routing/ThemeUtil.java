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

package com.here.msdkui.routing;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;

import com.here.msdkui.R;

/**
 * This class contains utility methods that allow easier usage of theme attributes in code.
 *
 * @see com.here.msdkui.common.ThemeUtil
 * @deprecated This class will be removed in version 1.4.0
 */
public final class ThemeUtil {

    private static final int[] DESIGN_THEME = R.styleable.DarkTheme;

    private ThemeUtil() {
    }

    /**
     * Gets drawable from {@link TypedArray} and attr.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param array
     *         an array of attributes values.
     *
     * @param attr
     *         an identifier of the attribute.
     *
     * @return a requested {@link Drawable}.
     */
    public static Drawable getDrawable(Context context, final TypedArray array, final int attr) {
        final TypedValue value = array.peekValue(attr);
        if (value == null) {
            return null;
        }
        if (value.type == TypedValue.TYPE_REFERENCE || value.type == TypedValue.TYPE_STRING) {
            // if it is reference to some drawable.
            return array.getDrawable(attr);
        } else if (value.type == TypedValue.TYPE_INT_COLOR_ARGB8 || value.type == TypedValue.TYPE_INT_COLOR_ARGB4 || value.type ==
                TypedValue.TYPE_INT_COLOR_RGB8 || value.type == TypedValue.TYPE_INT_COLOR_RGB4) {
            // if it is reference to some color.
            return new ColorDrawable(array.getColor(attr, 0xFF000000)); // black color
        } else {
            throw new IllegalArgumentException(
                    context.getString(R.string.msdkui_exception_attribute_invalid, attr));
        }
    }

    /**
     * Gets String from {@link TypedArray} and attr.
     *
     * @param array
     *         an array of attributes values.
     *
     * @param attr
     *         an identifier of the attribute.
     *
     * @return a requested {@link String}.
     */
    public static String getString(final TypedArray array, final int attr) {
        final TypedValue value = array.peekValue(attr);
        if (value == null) {
            return null;
        }
        return array.getString(attr);
    }

    /**
     * Returns a color value specified by the given theme attribute.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param colorAttribute
     *         A theme attribute such as R.attr.colorBackground.
     *
     * @return required color or Color.CYAN if couldn't resolve it properly.
     */
    @ColorInt
    public static int getColor(Context context, int colorAttribute) {
        final TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttribute, outValue, true);
        if (isColor(outValue)) {
            return outValue.data;
        } else if (outValue.type == TypedValue.TYPE_REFERENCE || outValue.type == TypedValue.TYPE_STRING) {
            try {
                final ColorStateList colors = ContextCompat.getColorStateList(context, outValue.resourceId);
                return colors.getDefaultColor();
            } catch (Resources.NotFoundException ex) {
                Log.d(ThemeUtil.class.getName(), ex.getMessage());
            }
        } else if (outValue.type == TypedValue.TYPE_NULL) {
            return getColorFromStyleable(context, styleableForAttr(colorAttribute));
        }
        // default to cyan in case of errors
        return Color.CYAN;
    }

    private static boolean isColor(TypedValue value) {
        final int type = value.type;
        return type >= TypedValue.TYPE_FIRST_COLOR_INT && type <= TypedValue.TYPE_LAST_COLOR_INT;
    }

    /**
     * Returns a color value specified by the given theme attribute.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param colorStyleable
     *         A styleable color attribute,
     *         such as R.styleable.HereTheme_colorBackground.
     *
     * @return required color or Color.CYAN if couldn't resolve it properly.
     */
    private static int getColorFromStyleable(Context context, int colorStyleable) {
        if (colorStyleable <= 0) {
            return 0;
        }
        final TypedArray array = context.obtainStyledAttributes(DESIGN_THEME);
        final int color = array.getColor(colorStyleable, 0);
        array.recycle();
        return color;
    }

    /**
     * Resolves the R.attr.* -> R.styleable.*. Do not use this code in production; this
     * is only intended for design time data usage.
     */
    private static int styleableForAttr(int attribute) {
        final int[] themeStyleable = DESIGN_THEME;
        for (int i = 0; i < themeStyleable.length; ++i) {
            if (attribute == themeStyleable[i]) {
                return i;
            }
        }
        return -1;
    }
}
