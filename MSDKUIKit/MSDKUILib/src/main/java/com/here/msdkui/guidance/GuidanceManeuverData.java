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
 * A data class holding the current guidance instructions to be fed into the {@link GuidanceManeuverPanel}.
 * This class can be used to hold an icon along with some other details like street name, signpost or exit.
 * {@link GuidanceManeuverPanelPresenter} can be used to get notified on new instances of this class during guidance.
 */
public class GuidanceManeuverData implements Parcelable {

    /**
     * Creator for parcelable.
     */
    public static final Parcelable.Creator<GuidanceManeuverData> CREATOR = new Parcelable.Creator<GuidanceManeuverData>() {
        @Override
        public GuidanceManeuverData createFromParcel(Parcel in) {
            return new GuidanceManeuverData(in);
        }

        @Override
        public GuidanceManeuverData[] newArray(int size) {
            return new GuidanceManeuverData[size];
        }
    };

    /**
     * Icon id.
     */
    private final int mIconId;
    /**
     * Distance to next maneuver.
     */
    private final long mDistance;
    /**
     * Additional information. For example, a {@link com.here.android.mpa.routing.Signpost Signpost} or null.
     */
    private final String mInfo1;
    /**
     * Additional information. For example, a street name.
     */
    private final String mInfo2;

    /**
     * Constructs a new instance using the provided icon, distance and info strings.
     *
     * @param iconId
     *         resource id of the maneuver icon.
     * @param distance
     *         distance to next {@link com.here.android.mpa.routing.Maneuver}.
     * @param info1
     *         information for the 1st line of {@link GuidanceManeuverPanel}. In most cases, it is used to display
     *         highway exit numbers. In case there is no relevant information, null can be set.
     * @param info2
     *         information for the 2nd line of {@link GuidanceManeuverPanel}. In most cases, it is used to display
     *         next maneuver street or the destination.
     */
    public GuidanceManeuverData(int iconId, long distance, String info1, String info2) {
        mIconId = iconId;
        mDistance = distance;
        mInfo1 = info1;
        mInfo2 = info2;
    }

    /* package */ GuidanceManeuverData(Parcel in) {
        mIconId = in.readInt();
        mDistance = in.readLong();
        mInfo1 = in.readString();
        mInfo2 = in.readString();
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
     * Gets information of 1st line of guidance panel. Can be null in case no relevant information was set.
     *
     * @return a string with information for 1st line.
     */
    public String getInfo1() {
        return mInfo1;
    }

    /**
     * Gets information of 2nd line of guidance panel.
     *
     * @return a string with information for 2nd line.
     */
    public String getInfo2() {
        return mInfo2;
    }

    /**
     * Gets the distance to the next maneuver.
     *
     * @return the distance to next maneuver in meters.
     */
    public long getDistance() {
        return mDistance;
    }

    @Override
    public String toString() {
        return "GuidanceManeuverData(mIconId=" + this.mIconId +
                ", mDistance=" + this.mDistance +
                ", mInfo1=" + this.mInfo1 +
                ", mInfo2=" + this.mInfo2 +
                ")";
    }

    @Override
    public int hashCode() {
        return (((this.mIconId * 31 +
                (int) (this.mDistance ^ this.mDistance >>> 32)) * 31 +
                (this.mInfo1 == null ? 0 : this.mInfo1.hashCode())) * 31 +
                (this.mInfo2 == null ? 0 : this.mInfo2.hashCode())) * 31;
    }

    @Override
    public boolean equals(Object obj1) {
        if (obj1 instanceof GuidanceManeuverData) {
            final GuidanceManeuverData obj2 = (GuidanceManeuverData) obj1;
            if (this.mIconId == obj2.mIconId &&
                    this.mDistance == obj2.mDistance &&
                    areEqual(this.mInfo1, obj2.mInfo1) &&
                    areEqual(this.mInfo2, obj2.mInfo2)) {
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
        dest.writeString(mInfo1);
        dest.writeString(mInfo2);
    }
}
