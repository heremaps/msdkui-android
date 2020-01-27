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
import android.content.res.TypedArray;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.here.android.mpa.routing.Maneuver;
import com.here.msdkui.R;
import com.here.msdkui.common.BaseView;
import com.here.msdkui.common.DistanceFormatterUtil;
import com.here.msdkui.common.ThemeUtil;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A view that displays a maneuver based on its visible sections. To make this item visible, please set {@link Maneuver} in
 * {@link ManeuverItemView#setManeuver(List, int)}.
 */
public class ManeuverItemView extends BaseView {

    private final EnumMap<ManeuverItemView.Section, View> mSections = new EnumMap<>(
            ManeuverItemView.Section.class);
    private Maneuver mManeuver;

    /**
     * Constructs a new instance.
     *
     * @param context
     *         the required {@link Context}.
     */
    public ManeuverItemView(final Context context) {
        this(context, null);
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
    public ManeuverItemView(final Context context, final AttributeSet attrs) {
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
    public ManeuverItemView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
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
    public ManeuverItemView(final Context context, final AttributeSet attrs, final int defStyleAttr,
                            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.maneuver_item, this);
        if (getBackground() == null) {
            setBackgroundColor(ThemeUtil.getColor(getContext(), R.attr.colorBackgroundViewLight));
        }
        initSections();
        attrsInit(attrs, defStyleAttr, defStyleRes);
    }

    private void initSections() {
        for (final ManeuverItemView.Section section : ManeuverItemView.Section.values()) {
            switch (section) {
                case ICON:
                    mSections.put(section, (View) findViewById(R.id.maneuver_icon_view));
                    break;
                case ADDRESS:
                    mSections.put(section, (View) findViewById(R.id.maneuver_address_view));
                    break;
                case DISTANCE:
                    mSections.put(section, (View) findViewById(R.id.maneuver_distance_view));
                    break;
                case INSTRUCTIONS:
                    mSections.put(section, (View) findViewById(R.id.maneuver_instruction_view));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Initialization of attributes.
     */
    private void attrsInit(final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        if (attrs == null) {
            return;
        }
        final TypedArray typedArray = this.getContext()
                .obtainStyledAttributes(attrs, R.styleable.ManeuverItemView, defStyleAttr, defStyleRes);
        final int section = typedArray.getInt(R.styleable.ManeuverItemView_visible, 0);
        if (section == 0) {
            return; // There are no options.
        }
        final EnumSet<Section> shouldVisibleSection = EnumSet.noneOf(Section.class);
        if ((section & Section.ADDRESS.getAttrValue()) != 0) {
            shouldVisibleSection.add(Section.ADDRESS);
        }
        if ((section & Section.DISTANCE.getAttrValue()) != 0) {
            shouldVisibleSection.add(Section.DISTANCE);
        }
        if ((section & Section.ICON.getAttrValue()) != 0) {
            shouldVisibleSection.add(Section.ICON);
        }
        if ((section & Section.INSTRUCTIONS.getAttrValue()) != 0) {
            shouldVisibleSection.add(Section.INSTRUCTIONS);
        }
        typedArray.recycle();
        setVisibleSections(shouldVisibleSection);
    }

    /**
     * Sets the visibility of a {@link Section}.
     *
     * @param section
     *         the {@link Section Section} to set the visibility for.
     * @param visible
     *         true if visible, false otherwise.
     */
    public void setSectionVisible(final Section section, final boolean visible) {
        if (visible) {
            mSections.get(section).setVisibility(VISIBLE);
        } else {
            mSections.get(section).setVisibility(GONE);
        }
    }

    /**
     * Returns the visibility for the given {@link Section Section}.
     *
     * @param section
     *         {@link Section Section}.
     *
     * @return true if visible, false otherwise.
     */
    public boolean isSectionVisible(final Section section) {
        return mSections.get(section).getVisibility() == VISIBLE;
    }

    /**
     * Gets a set of {@link Section} elements that are visible.
     *
     * @return all visible sections.
     */
    public Set<Section> getVisibleSections() {
        final EnumSet<Section> visibleSections = EnumSet.noneOf(Section.class);
        for (final Map.Entry<Section, View> entry : mSections.entrySet()) {
            if (entry.getValue()
                    .getVisibility() == VISIBLE) {
                visibleSections.add(entry.getKey());
            }
        }
        return visibleSections;
    }

    /**
     * Sets a collection of {@link Section} elements that should be visible. Sections that are not part of
     * this collection will be gone in the view hierarchy.
     *
     * @param sectionEnumSet
     *         {@link Section Section} enum set.
     */
    public void setVisibleSections(final EnumSet<Section> sectionEnumSet) {
        for (final Map.Entry<Section, View> entry : mSections.entrySet()) {
            if (sectionEnumSet.contains(entry.getKey())) {
                entry.getValue()
                        .setVisibility(VISIBLE);
            } else {
                entry.getValue()
                        .setVisibility(GONE);
            }
        }
    }

    /**
     * Gets the {@link Maneuver} associated with this {@link ManeuverItemView}.
     *
     * @return the {@link Maneuver} belonging to this item.
     */
    public Maneuver getManeuver() {
        return mManeuver;
    }

    /**
     * Sets the {@link Maneuver} to be shown in this item. The maneuver is taken from the provided maneuver list
     * at the specified position.
     *
     * @param maneuvers
     *         a list of {@link Maneuver Maneuver} elements that describe the instructions for a route.
     * @param pos
     *         the index of the maneuver element from the given list that should be shown in this item.
     * @throws IllegalArgumentException if pos is out of range.
     */
    public void setManeuver(final List<Maneuver> maneuvers, final int pos) {
        if (maneuvers == null) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_maneuvres_null));
        }

        if (pos < 0 || pos > maneuvers.size()) {
            throw new IllegalArgumentException(
                    getContext().getString(R.string.msdkui_exception_maneuver_pos_invalid));
        }

        final ManeuverResources maneuverResources = new ManeuverResources(getContext(), maneuvers);
        mManeuver = maneuvers.get(pos);

        final ImageView icon = (ImageView) mSections.get(Section.ICON);
        final int id = maneuverResources.getManeuverIconId(pos);
        if (id == 0) {
            icon.setVisibility(View.GONE);
        } else {
            icon.setImageResource(id);
        }
        icon.setTag(id);

        final TextView instructionView = (TextView) mSections.get(Section.INSTRUCTIONS);
        instructionView.setText(maneuverResources.getManeuverInstruction(pos));

        final TextView addressView = (TextView) mSections.get(Section.ADDRESS);
        addressView.setText(maneuverResources.getRoadToDisplay(pos));

        final TextView distanceView = (TextView) mSections.get(Section.DISTANCE);
        final int distance = maneuverResources.getDistanceFromNext(pos);
        if (distance == 0) {
            distanceView.setVisibility(GONE);
        } else {
            distanceView.setVisibility(VISIBLE);
            distanceView.setText(DistanceFormatterUtil.format(getContext(), distance, mUnitSystem));
        }

        if (getVisibility() == INVISIBLE) {
            setVisibility(View.VISIBLE);
        }
    }

    /**
     * Describes all available sections that the {@link ManeuverItemView} can show.
     */
    public enum Section {
        /**
         * Icon
         */
        ICON(0x01),

        /**
         * Instructions
         */
        INSTRUCTIONS(0x02),

        /**
         * Address
         */
        ADDRESS(0x04),

        /**
         * Distance
         */
        DISTANCE(0x08);

        private int mAttrValue;

        Section(int value) {
            mAttrValue = value;
        }

        public int getAttrValue() {
            return mAttrValue;
        }
    }
}
