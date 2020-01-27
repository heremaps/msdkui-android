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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.here.android.mpa.routing.RouteOptions;

import java.util.List;

/**
 * An adapter class that can be used for a {@link TransportModePanel}. You can override this class to
 * customize the panel.
 *
 * @see SimpleTransportModePanelAdapter
 */
public abstract class TransportModePanelAdapter extends FragmentStatePagerAdapter {

    private List<RouteOptions.TransportMode> mTransportModes;

    /**
     * Constructs a new instance using a {@link FragmentManager}.
     *
     * <p>Please set the transport modes list by calling {@link #setTransportModes(List)} and update the UI by
     * calling {@link #notifyDataSetChanged()}.</p>
     *
     * @param manager the required {@link FragmentManager}.
     */
    public TransportModePanelAdapter(final FragmentManager manager) {
        super(manager);
    }

    /**
     * Constructs a new instance using a {@link FragmentManager} and a list of
     * {@link com.here.android.mpa.routing.RouteOptions.TransportMode}.
     *
     * @param manager  the required {@link FragmentManager}.
     * @param transportModes the list to use for this adapter.
     */
    public TransportModePanelAdapter(final FragmentManager manager, final List<RouteOptions.TransportMode> transportModes) {
        super(manager);
        mTransportModes = transportModes;
    }

    @Override
    public Fragment getItem(final int position) {
        return getContent(mTransportModes.get(position));
    }

    @Override
    public int getCount() {
        return mTransportModes.size();
    }

    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    @Override
    public CharSequence getPageTitle(final int position) {
        return "";
    }

    /**
     * Gets the custom {@link TabView} at a given position, which represents a
     * {@link com.here.android.mpa.routing.RouteOptions.TransportMode}.
     *
     * This method can be used to update {@link com.google.android.material.tabs.TabLayout.Tab}
     * with the returned custom {@link TabView}.
     *
     * @param position the position to get the {@link TabView} from.
     * @return the custom {@link TabView}.
     */
    public TabView getTabView(final int position) {
        return getCustomTabView(mTransportModes.get(position));
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    /**
     * Gets all elements of {@link com.here.android.mpa.routing.RouteOptions.TransportMode}
     * associated with this adapter.
     *
     * @return the associated transport modes.
     */
    public List<RouteOptions.TransportMode> getTransportModes() {
        return mTransportModes;
    }

    /**
     * Sets a list containing elements of {@link com.here.android.mpa.routing.RouteOptions.TransportMode}
     * that should be associated with this adapter.
     *
     * @param transportModes the list of transport modes.
     */
    public void setTransportModes(final List<RouteOptions.TransportMode> transportModes) {
        mTransportModes = transportModes;
    }

    /**
     * Gets the fragment of {@link TransportModePanel}.
     *
     * <p>If you don't want to display the content and only want to have a {@link TabView} in {@link TransportModePanel},
     * please return null and return false from {@link TransportModePanelAdapter#isContentVisible()} method.</p>
     *
     * @param mode the {@link com.here.android.mpa.routing.RouteOptions.TransportMode}.
     * @return the fragment of the panel hosting the content.
     */
    public abstract Fragment getContent(RouteOptions.TransportMode mode);

    /**
     * Gets the {@link TabView} view for the given
     * {@link com.here.android.mpa.routing.RouteOptions.TransportMode}.
     *
     * @param mode the {@link com.here.android.mpa.routing.RouteOptions.TransportMode}.
     * @return the {@link TabView} that is used to represent the given transport mode.
     * @deprecated Please use
     * {@link #getCustomTabView(com.here.android.mpa.routing.RouteOptions.TransportMode)} instead.
     */
    @Deprecated
    public abstract TabView getTabCustomView(RouteOptions.TransportMode mode);

    /**
     * Gets the {@link TabView} view for the given
     * {@link com.here.android.mpa.routing.RouteOptions.TransportMode}.
     *
     * @param mode the {@link com.here.android.mpa.routing.RouteOptions.TransportMode}.
     * @return the {@link TabView} that is used to represent the given transport mode.
     */
    public abstract TabView getCustomTabView(RouteOptions.TransportMode mode);


    /**
     * Indicates whether the content is visible.
     *
     * This is responsible for showing the fragment hosting the content for a
     * {@link com.here.android.mpa.routing.RouteOptions.TransportMode}
     * in the {@link TransportModePanel}. If the method returns false, the method
     * {@link TransportModePanelAdapter#getItem(int)} will be ignored.
     *
     * @return true if content should be visible, false will hide the content.
     */
    public abstract boolean isContentVisible();
}
