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

import com.here.android.mpa.routing.Maneuver;
import com.here.msdkui.R;
import com.here.msdkui.common.measurements.UnitSystem;

import java.util.List;

/**
 * A {@link androidx.recyclerview.widget.RecyclerView.Adapter} to bind a view item of the list to a
 * {@link ManeuverItemView}.
 */
public class ManeuverListAdapter extends RecyclerView.Adapter<ManeuverListAdapter.ViewHolder> {

    private final List<Maneuver> mManeuverList;
    private UnitSystem mUnitSystem = UnitSystem.METRIC;

    /**
     * Constructs a new instance using a list of maneuvers.
     *
     * @param maneuverList
     *         a list of maneuvers.
     */
    public ManeuverListAdapter(final List<Maneuver> maneuverList) {
        super();
        mManeuverList = maneuverList;
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
     * Creates an empty {@link ManeuverItemView} to be used as view holder.
     * <p>Override this method to set a custom view holder for a row.</p>
     *
     * @param context
     *         the required context.
     *
     * @return an instance of {@link ManeuverItemView}.
     */
    protected View getRowView(final Context context) {
        View view =  View.inflate(context, R.layout.maneuver_item_list, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Maneuver maneuver = mManeuverList.get(position);
        if (maneuver != null && holder.itemView instanceof ManeuverItemView) {
            ((ManeuverItemView) holder.itemView).setUnitSystem(mUnitSystem);
            ((ManeuverItemView) holder.itemView).setManeuver(mManeuverList, position);
        }
    }

    @Override
    public int getItemCount() {
        return mManeuverList.size();
    }

    /**
     * A view holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Constructs a new {@link androidx.recyclerview.widget.RecyclerView.ViewHolder}.
         *
         * @param view
         *         a view to be hold.
         */
        public ViewHolder(final View view) {
            super(view);
        }
    }
}
