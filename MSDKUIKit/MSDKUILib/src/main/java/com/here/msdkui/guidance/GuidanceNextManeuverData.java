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

package com.here.msdkui.guidance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A data class holding guidance instructions for the maneuver after the currently shown maneuver.
 * To be fed into the {@link GuidanceNextManeuverPanel}.
 * This class can be used to hold an icon along with street name and distance.
 * {@link GuidanceNextManeuverPanelPresenter} can be used to get notified on new instances of this class during guidance.
 */
public class GuidanceNextManeuverData implements Parcelable {
    /**
     * Creator for parcelable.
     */
    public static final Parcelable.Creator<GuidanceNextManeuverData> CREATOR = new Parcelable.Creator<GuidanceNextManeuverData>() {
        @Override
        public GuidanceNextManeuverData createFromParcel(Parcel in) {
            return new GuidanceNextManeuverData(in);
        }

        @Override
        public GuidanceNextManeuverData[] newArray(int size) {
            return new GuidanceNextManeuverData[size];
        }
    };

    /**
     * Icon id.
     */
    private final int mIconId;
    /**
     * Distance to the respective maneuver.
     */
    private final long mDistance;
    /**
     * The name of the street after the maneuver.
     */
    private final String mStreetName;

    /**
     * Constructs a new instance using the provided icon and info strings.
     *
     * @param iconId
     *         resource id of the maneuver icon.
     * @param distance
     *         distance to the maneuver displayed on {@link GuidanceNextManeuverPanel}
     * @param streetName
     *         street name after the maneuver displayed on {@link GuidanceNextManeuverPanel}
     */
    public GuidanceNextManeuverData(int iconId, long distance, String streetName) {
        mIconId = iconId;
        mDistance = distance;
        mStreetName = streetName;
    }

    GuidanceNextManeuverData(Parcel in) {
        mIconId = in.readInt();
        mDistance = in.readLong();
        mStreetName = in.readString();
    }

    /**
     * Gets the icon id of the next maneuver.
     *
     * @return an icon resource id.
     */
    public int getIconId() {
        return mIconId;
    }

    /**
     * Gets the distance to the maneuver.
     *
     * @return a distance in meters.
     */
    public long getDistance() {
        return mDistance;
    }

    /**
     * Gets street name after the next maneuver.
     *
     * @return a {@link String} containing street name.
     */
    public String getStreetName() {
        return mStreetName;
    }

    @Override
    public String toString() {
        return "GuidanceNextManeuverData(mIconId=" + this.mIconId +
                ", mDistance=" + this.mDistance +
                ", mStreetName=" + this.mStreetName +
                ")";
    }

    @Override
    public int hashCode() {
        return ((this.mIconId * 31 +
                (int) (this.mDistance ^ this.mDistance >>> 32)) * 31 +
                (this.mStreetName == null ? 0 : this.mStreetName.hashCode())) * 31;
    }

    @Override
    public boolean equals(Object obj1) {
        if (obj1 instanceof GuidanceNextManeuverData) {
            final GuidanceNextManeuverData obj2 = (GuidanceNextManeuverData) obj1;
            if (this.mIconId == obj2.mIconId &&
                    this.mDistance == obj2.mDistance &&
                    areEqual(this.mStreetName, obj2.mStreetName)) {
                return true;
            }
        }
        return false;
    }

    private boolean areEqual(Object first, Object second) {
        return first == null ? second == null : first.equals(second);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mIconId);
        dest.writeLong(mDistance);
        dest.writeString(mStreetName);
    }
}
