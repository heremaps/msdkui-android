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
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A view that shows an options panel for displaying the hazardous materials options of {@link RouteOptions RouteOptions}.
 */
public class HazardousMaterialsOptionsPanel extends OptionsPanel implements OptionItem.OnChangedListener {

    private Map<RouteOptions.HazardousGoodType, String> mResourceKey;
    private RouteOptions mRouteOptions;
    private OptionItem mSubItems;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public HazardousMaterialsOptionsPanel(final Context context) {
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
    public HazardousMaterialsOptionsPanel(final Context context, final AttributeSet attrs) {
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
    public HazardousMaterialsOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr) {
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
     * Requires Lollipop (API level 21).
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HazardousMaterialsOptionsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        populateResources();
        createPanel();
    }

    private void populateResources() {
        mResourceKey = new LinkedHashMap<>();
        mResourceKey.put(RouteOptions.HazardousGoodType.EXPLOSIVE, getString(R.string.msdkui_explosive));
        mResourceKey.put(RouteOptions.HazardousGoodType.GAS, getString(R.string.msdkui_gas));
        mResourceKey.put(RouteOptions.HazardousGoodType.FLAMMABLE, getString(R.string.msdkui_flammable));
        mResourceKey.put(RouteOptions.HazardousGoodType.COMBUSTIBLE, getString(R.string.msdkui_combustible));
        mResourceKey.put(RouteOptions.HazardousGoodType.ORGANIC, getString(R.string.msdkui_organic));
        mResourceKey.put(RouteOptions.HazardousGoodType.POISON, getString(R.string.msdkui_poison));
        mResourceKey.put(RouteOptions.HazardousGoodType.RADIOACTIVE, getString(R.string.msdkui_radioactive));
        mResourceKey.put(RouteOptions.HazardousGoodType.CORROSIVE, getString(R.string.msdkui_corrosive));
        mResourceKey.put(RouteOptions.HazardousGoodType.POISONOUS_INHALATION, getString(R.string.msdkui_poisonous));
        mResourceKey.put(RouteOptions.HazardousGoodType.HARMFUL_TO_WATER, getString(R.string.msdkui_harmful_to_water));
        mResourceKey.put(RouteOptions.HazardousGoodType.OTHER, getString(R.string.msdkui_other));
    }

    private void createPanel() {
        mSubItems = new OptionItemBuilders.MultipleChoiceOptionItemBuilder(getContext())
                .setLabels(new ArrayList<String>(mResourceKey.values()))
                .build();
        mSubItems.setListener(this);
        setOptionItems(Collections.singletonList(mSubItems));
    }

    private void select(final EnumSet<RouteOptions.HazardousGoodType> checkedOptions) {
        final List<String> labels = new ArrayList<>();
        for (final RouteOptions.HazardousGoodType option : checkedOptions) {
            labels.add(mResourceKey.get(option));
        }
        ((MultipleChoiceOptionItem) mSubItems).selectLabels(labels);
    }

    /**
     * Gets the underlying {@link RouteOptions RouteOptions}, modified according to UI selection of this panel.
     * @return the modified route options or null if no route options have been set.
     */
    public RouteOptions getRouteOptions() {
        return mRouteOptions;
    }

    /**
     * Sets the {@link RouteOptions RouteOptions} to populate the {@code HazardousMaterialsOptionsPanel}.
     * @param routeOptions the route options to use
     */
    public void setRouteOptions(final RouteOptions routeOptions) {
        mRouteOptions = routeOptions;
        final EnumSet<RouteOptions.HazardousGoodType> materialsOptions = mRouteOptions.getTruckShippedHazardousGoods();
        select(materialsOptions);
    }

    private void populateRouteOptions() {
        if (mRouteOptions == null) {
            return;
        }
        final EnumSet<RouteOptions.HazardousGoodType> hazardousGoodTypes = EnumSet.noneOf(RouteOptions.HazardousGoodType.class);
        final List<String> labelIds = ((MultipleChoiceOptionItem) mSubItems).getSelectedLabels();
        for (final Map.Entry<RouteOptions.HazardousGoodType, String> entry : mResourceKey.entrySet()) {
            if (labelIds.contains(entry.getValue())) {
                hazardousGoodTypes.add(entry.getKey());
            }
        }
        mRouteOptions.setTruckShippedHazardousGoods(hazardousGoodTypes);
    }

    @Override
    public void onChanged(OptionItem item) {
        populateRouteOptions();
        notifyOnOptionChanged(item);
    }
}