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

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;

import com.here.android.mpa.routing.RouteOptions;
import com.here.msdkui.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A view that shows an options panel to select the available tunnel options provided by {@link RouteOptions}.
 */
public class TunnelOptionsPanel extends OptionsPanel implements OptionItem.OnChangedListener {

    private Map<RouteOptions.TunnelCategory, String> mResourceKey;
    private OptionItem mSubOptionItem;
    private RouteOptions mRouteOptions;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public TunnelOptionsPanel(final Context context) {
        this(context, null, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     */
    public TunnelOptionsPanel(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     *
     * @param defStyleAttr
     *         a default style attribute.
     */
    public TunnelOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        populateResources();
        createPanel();
    }

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     *
     * @param attrs
     *         a set of attributes.
     *
     * @param defStyleAttr
     *         a default style attribute.
     *
     * @param defStyleRes
     *         a default style resource.
     *
     * Requires Lollipop (API Level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TunnelOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        populateResources();
        createPanel();
    }

    private void populateResources() {
        mResourceKey = new LinkedHashMap<>();
        mResourceKey.put(RouteOptions.TunnelCategory.B, getString(R.string.msdkui_tunnel_cat_b));
        mResourceKey.put(RouteOptions.TunnelCategory.C, getString(R.string.msdkui_tunnel_cat_c));
        mResourceKey.put(RouteOptions.TunnelCategory.D, getString(R.string.msdkui_tunnel_cat_d));
        mResourceKey.put(RouteOptions.TunnelCategory.E, getString(R.string.msdkui_tunnel_cat_e));
        mResourceKey.put(RouteOptions.TunnelCategory.UNDEFINED, getString(R.string.msdkui_undefined));
    }


    private void createPanel() {
        mSubOptionItem = new OptionItemBuilders.SingleChoiceOptionItemBuilder(getContext()).setLabels(getString(R.string
                .msdkui_tunnels_allowed_title), new ArrayList<String>(mResourceKey.values()))
                .build();
        mSubOptionItem.setListener(this);
        setOptionItems(Collections.singletonList(mSubOptionItem));
    }

    /**
     * Gets the underlying {@link RouteOptions}.
     *
     * @return the {@link RouteOptions} that was set for this panel or null if no options have been set.
     */
    public RouteOptions getRouteOptions() {
        if (mRouteOptions == null) {
            return null;
        }
        populateRouteOptions();
        return mRouteOptions;
    }

    /**
     * Sets the {@link RouteOptions} and populates this panel based on the provided options.
     *
     * @param routeOptions the {@link RouteOptions} to set for this panel.
     * @throws IllegalArgumentException if the routeOptions param is null.
     */
    public void setRouteOptions(final RouteOptions routeOptions) {
        if (routeOptions == null) {
            throw new IllegalArgumentException(getString(R.string.msdkui_exception_route_options_null));
        }
        mRouteOptions = routeOptions;
        select(mRouteOptions.getTruckTunnelCategory());
    }

    /**
     * Populates this panel based on the provided {@link RouteOptions}.
     * Does nothing if no options have been set.
     */
    public void populateRouteOptions() {
        if (mRouteOptions == null) {
            return;
        }
        final String label = ((SingleChoiceOptionItem) mSubOptionItem).getSelectedItemLabel();
        for (final Map.Entry<RouteOptions.TunnelCategory, String> entry : mResourceKey.entrySet()) {
            if (entry.getValue()
                    .equals(label)) {
                mRouteOptions.setTruckTunnelCategory(entry.getKey());
                break;
            }
        }
    }

    private void select(final RouteOptions.TunnelCategory category) {
        for (final Map.Entry<RouteOptions.TunnelCategory, String> entry : mResourceKey.entrySet()) {
            if (category == entry.getKey()) {
                ((SingleChoiceOptionItem) mSubOptionItem).selectLabel(entry.getValue());
                break;
            }
        }
    }

    @Override
    public void onChanged(OptionItem item) {
        populateRouteOptions();
        notifyOnOptionChanged(item);
    }
}
