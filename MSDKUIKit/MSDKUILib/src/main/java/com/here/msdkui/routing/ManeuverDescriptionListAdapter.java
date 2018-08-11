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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.here.android.mpa.routing.Maneuver;

import java.util.List;

/**
 * A {@RecyclerView.Adapter} to bind a view item of the list to a {@link ManeuverDescriptionItem}.
 */
public class ManeuverDescriptionListAdapter extends RecyclerView.Adapter<ManeuverDescriptionListAdapter.ViewHolder> {

    private final List<Maneuver> mManeuverList;

    /**
     * Constructs a new instance using a list of maneuvers.
     */
    public ManeuverDescriptionListAdapter(final List<Maneuver> maneuverList) {
        super();
        mManeuverList = maneuverList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(getRowView(parent.getContext()));
    }

    /**
     * Creates an empty {@link ManeuverDescriptionItem} to be used as view holder.
     * <p>Override this method to set a custom view holder for a row.</p>
     */
    protected View getRowView(final Context context) {
        return new ManeuverDescriptionItem(context);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Maneuver maneuver = mManeuverList.get(position);
        if (maneuver != null && holder.itemView instanceof ManeuverDescriptionItem) {
            ((ManeuverDescriptionItem) holder.itemView).setManeuver(mManeuverList, position);
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
         * Constructs a new {@ViewHolder}.
         */
        public ViewHolder(final View view) {
            super(view);
        }
    }
}
