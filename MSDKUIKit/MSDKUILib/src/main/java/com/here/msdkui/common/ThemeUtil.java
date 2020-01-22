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
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;

import com.here.msdkui.R;

/**
 * A convenience class to access theme attributes.
 */
public final class ThemeUtil {

    private static final int[] DESIGN_THEME = R.styleable.DarkTheme;

    private ThemeUtil() {
    }

    /**
     * Gets a {@link Drawable} from {@link TypedArray} and index of attribute.
     *
     * @param context
     *         the required context.
     *
     * @param typedArray
     *         the array of values.
     * @param index
     *         the index of the attribute.
     *
     * @return a {@link Drawable} or null if a raw TypedValue could not be retrieved.
     * @throws IllegalArgumentException if the attribute at index cannot be resolved.
     */
    public static Drawable getDrawable(Context context, final TypedArray typedArray, final int index) {
        final TypedValue value = typedArray.peekValue(index);
        if (value == null) {
            return null;
        }
        if (value.type == TypedValue.TYPE_REFERENCE || value.type == TypedValue.TYPE_STRING) {
            // if it is reference to some drawable.
            return typedArray.getDrawable(index);
        } else if (value.type == TypedValue.TYPE_INT_COLOR_ARGB8 || value.type == TypedValue.TYPE_INT_COLOR_ARGB4 || value.type ==
                TypedValue.TYPE_INT_COLOR_RGB8 || value.type == TypedValue.TYPE_INT_COLOR_RGB4) {
            // if it is reference to some color.
            return new ColorDrawable(typedArray.getColor(index, 0xFF000000)); // black color
        } else {
            throw new IllegalArgumentException(
                    context.getString(R.string.msdkui_exception_attribute_invalid, index));
        }
    }

    /**
     * Gets a string from {@link TypedArray} and index of attribute.
     *
     * @param typedArray
     *         the array of values.
     * @param index
     *         the index of the attribute.
     *
     * @return a string or null if a raw TypedValue could not be retrieved.
     */
    public static String getString(final TypedArray typedArray, final int index) {
        final TypedValue value = typedArray.peekValue(index);
        if (value == null) {
            return null;
        }
        return typedArray.getString(index);
    }

    /**
     * Returns a color value specified by the given theme attribute.
     *
     * @param context
     *         the required context.
     * @param colorAttribute
     *         a theme attribute such as <code>R.attr.colorBackground</code>.
     *
     * @return a color. In case of errors <code>Color.CYAN</code> is returned.
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

    /**
     * Checks if given {@link TypedValue} is of type Color.
     *
     * @return true if given {@link TypedValue} is of type Color, false otherwise.
     */
    private static boolean isColor(TypedValue value) {
        final int type = value.type;
        return type >= TypedValue.TYPE_FIRST_COLOR_INT && type <= TypedValue.TYPE_LAST_COLOR_INT;
    }

    /**
     * Returns a color value specified by the given theme attribute.
     * If the method couldn't properly resolve the color, returns Color.CYAN.
     *
     * @param colorStyleable
     *         A styleable color attribute,
     *         such as R.styleable.HereTheme_colorBackground.
     *
     * @return a color value specified by the given theme attribute
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
     * Resolves the R.attr.* to R.styleable.*.
     *
     * @param attribute
     *         given R.attr.
     *
     * @return a style for the given attribute.
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

    /**
     * Returns a resource id for the given theme attribute.
     *
     * @param context
     *         the required context.
     *
     * @param themeAttribute
     *         a theme attribute whose value is an arbitrary resource id.
     *
     * @return a resource id.
     */
    public static int getStyleId(Context context, int themeAttribute) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(themeAttribute, value, true);
        return value.resourceId;
    }
}
