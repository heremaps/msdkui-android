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
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.here.android.mpa.routing.Route;
import com.here.msdkui.common.measurements.UnitSystem;

import java.util.List;

/**
 * An adapter class that acts as a bridge between a list of {@link Route} elements and the corresponding
 * view holder items constructed as an instance of {@link RouteDescriptionItem}.
 */
public class RouteDescriptionListAdapter extends RecyclerView.Adapter<RouteDescriptionListAdapter.ViewHolder> {

    private final List<Route> mRouteList;
    private final RouteBarScaler mRouteBarScaler;
    private boolean mTrafficEnabled;
    private UnitSystem mUnitSystem = UnitSystem.METRIC;

    /**
     * Constructs a new instance using a list of {@link Route} elements.
     *
     * @param routeList list of {@link Route} elements.
     */
    public RouteDescriptionListAdapter(final List<Route> routeList) {
        super();
        mRouteList = routeList;
        mRouteBarScaler = new RouteBarScaler();
        registerAdapterDataObserver(new DataObserver());
    }

    /**
     * Sets unit system of this adapter.
     *
     * @param unitSystem
     *         unit system {@link UnitSystem}.
     */
    public void setUnitSystem(UnitSystem unitSystem) {
        mUnitSystem = unitSystem;
        notifyDataSetChanged();
    }

    /**
     * Returns current unit system of this adapter.
     *
     * @return unit system {@link UnitSystem}.
     */
    public UnitSystem getUnitSystem() {
        return mUnitSystem;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(getRowView(parent.getContext()));
    }

    /**
     * Creates a new {@link RouteDescriptionItem} that can be used as view holder.
     *
     * <p>Override this method to customize the use of a custom view holder.</p>
     *
     * @param context the context to use.
     * @return a new instance of {@link RouteDescriptionItem}.
     */
    protected View getRowView(final Context context) {
        RouteDescriptionItem item = new RouteDescriptionItem(context);
        item.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return item;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Route route = mRouteList.get(position);
        if (route != null && holder.itemView instanceof RouteDescriptionItem) {
            ((RouteDescriptionItem) holder.itemView).setUnitSystem(mUnitSystem);
            ((RouteDescriptionItem) holder.itemView).setTrafficEnabled(mTrafficEnabled);
            ((RouteDescriptionItem) holder.itemView).setSectionBarScaling(mRouteBarScaler.getScaling(route));
            ((RouteDescriptionItem) holder.itemView).setRoute(route);
        }
    }

    @Override
    public int getItemCount() {
        return mRouteList.size();
    }

    /**
     * Updates section bar scaling for all routes.
     */
    void updateScaling() {
        mRouteBarScaler.scaleRoutes(mRouteList);
    }

    /**
     * Gets if total time to arrival should be calculated with traffic.
     * @return true if traffic is enabled, false otherwise.
     */
    public boolean isTrafficEnabled() {
        return mTrafficEnabled;
    }

    /**
     * Sets if total time to arrival should be re-calculated including potential traffic delays.
     *
     * <p>Please note, setting this to true will call route tta with traffic. For more details please
     * see {@link com.here.android.mpa.routing.Route#getTtaIncludingTraffic(int)}</p>
     *
     * @param isTraffic true if traffic should be enabled, false otherwise.
     */
    public void setTrafficEnabled(final boolean isTraffic) {
        mTrafficEnabled = isTraffic;
        notifyDataSetChanged();
    }

    /**
     * The view holder for this adapter.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Constructs a new instance.
         *
         * @param view view for new instance.
         */
        public ViewHolder(final View view) {
            super(view);
        }
    }

    /**
     * The data observer for this adapter.
     */
    private class DataObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            updateScaling();
            super.onChanged();
        }

        @Override
        public void onItemRangeChanged(final int positionStart, final int itemCount, final Object payload) {
            updateScaling();
            super.onItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(final int positionStart, final int itemCount) {
            updateScaling();
            super.onItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(final int positionStart, final int itemCount) {
            updateScaling();
            super.onItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(final int fromPosition, final int toPosition, final int itemCount) {
            updateScaling();
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        }
    }
}
