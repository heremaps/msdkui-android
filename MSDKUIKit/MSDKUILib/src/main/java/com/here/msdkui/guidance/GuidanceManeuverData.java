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

package com.here.msdkui.guidance;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A data class holding the current guidance instructions to be fed into the {@link GuidanceManeuverView}.
 * This class can be used to hold an icon along with some other details like street name, signpost or exit.
 * {@link GuidanceManeuverPresenter} can be used to get notified on new instances of this class during guidance.
 */
public class GuidanceManeuverData implements Parcelable {

    public static final Creator<GuidanceManeuverData> CREATOR = new Creator<GuidanceManeuverData>() {
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
    private final Long mDistance;
    /**
     * Additional information. For example, a {@link com.here.android.mpa.routing.Signpost Signpost} or null.
     */
    private final String mInfo1;
    /**
     * Additional information. For example, a street name.
     */
    private final String mInfo2;
    /**
     * The next road icon for this maneuver.
     */
    private final Bitmap mNextRoadIcon;

    /**
     * Constructs a new instance using the provided icon, distance and info strings.
     *
     * @param iconId   resource id of the maneuver icon.
     * @param distance distance to next {@link com.here.android.mpa.routing.Maneuver}.
     * @param info1    information for the 1st line of {@link GuidanceManeuverView}. In most cases, it is used to display
     *                 highway exit numbers. In case there is no relevant information, null can be set.
     * @param info2    information for the 2nd line of {@link GuidanceManeuverView}. In most cases, it is used to display
     *                 next maneuver street or the destination.
     */
    public GuidanceManeuverData(int iconId, Long distance, String info1, String info2) {
        this(iconId, distance, info1, info2, null);
    }

    /**
     * Constructs a new instance using the provided icons, distance and info strings.
     *
     * @param iconId       resource id of the maneuver icon.
     * @param distance     distance to next {@link com.here.android.mpa.routing.Maneuver}.
     * @param info1        information for the 1st line of this panel. In most cases, it is used to display
     *                     highway exit numbers. In case there is no relevant information, null can be set.
     * @param info2        information for the 2nd line of this panel. In most cases, it is used to display
     *                     next maneuver street or the destination.
     * @param nextRoadIcon next road icon for this maneuver.
     */
    public GuidanceManeuverData(int iconId, Long distance, String info1, String info2, Bitmap nextRoadIcon) {
        mIconId = iconId;
        mDistance = distance;
        mInfo1 = info1;
        mInfo2 = info2;
        mNextRoadIcon = nextRoadIcon;
    }

    GuidanceManeuverData(Parcel in) {
        mIconId = in.readInt();
        if (in.readByte() == 0) {
            mDistance = null;
        } else {
            mDistance = in.readLong();
        }
        mInfo1 = in.readString();
        mInfo2 = in.readString();
        mNextRoadIcon = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mIconId);
        if (mDistance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mDistance);
        }
        dest.writeString(mInfo1);
        dest.writeString(mInfo2);
        dest.writeParcelable(mNextRoadIcon, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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
    public Long getDistance() {
        return mDistance;
    }

    /**
     * Gets next road icon for this maneuver.
     *
     * @return {@link Bitmap} for next road icon for this maneuver.
     */
    public Bitmap getNextRoadIcon() {
        return mNextRoadIcon;
    }

    @Override
    public String toString() {
        return "GuidanceManeuverData(mIconId=" + this.mIconId +
                ", mDistance=" + this.mDistance +
                ", mInfo1=" + this.mInfo1 +
                ", mInfo2=" + this.mInfo2 +
                ", mNextRoadIcon=" + this.mNextRoadIcon +
                ")";
    }

    @Override
    public int hashCode() {
        return (((this.mIconId * 31 +
                (int) (this.mDistance ^ this.mDistance >>> 32)) * 31 +
                (this.mInfo1 == null ? 0 : this.mInfo1.hashCode())) * 31 +
                (this.mInfo2 == null ? 0 : this.mInfo2.hashCode())) * 31 +
                (this.mNextRoadIcon == null ? 0 : this.mNextRoadIcon.hashCode()) * 31;
    }

    @Override
    public boolean equals(Object obj1) {
        if (obj1 instanceof GuidanceManeuverData) {
            final GuidanceManeuverData obj2 = (GuidanceManeuverData) obj1;
            if (this.mIconId == obj2.mIconId &&
                    areEqual(this.mDistance, obj2.mDistance) &&
                    areEqual(this.mInfo1, obj2.mInfo1) &&
                    areEqual(this.mInfo2, obj2.mInfo2) &&
                    areEqual(this.mNextRoadIcon, obj2.mNextRoadIcon)) {
                return true;
            }
        }
        return false;
    }

    private boolean areEqual(Object first, Object second) {
        return first == null ? second == null : first.equals(second);
    }
}
