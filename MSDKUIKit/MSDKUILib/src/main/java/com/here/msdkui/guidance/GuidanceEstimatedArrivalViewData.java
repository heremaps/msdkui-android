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

import com.here.msdkui.common.DateFormatterUtil;

import java.util.Date;

/**
 * A data class holding information related to Estimated Arrival Time (ETA). It consists of ETA date,
 * distance to destination and remaining travel time.
 * Use {@link GuidanceEstimatedArrivalViewPresenter} to get notified on new instances of this class during guidance.
 */
public class GuidanceEstimatedArrivalViewData implements Parcelable {
    /**
     * Creator for parcelable.
     */
    public static final Parcelable.Creator<GuidanceEstimatedArrivalViewData> CREATOR = new Parcelable.Creator<GuidanceEstimatedArrivalViewData>() {
        @Override
        public GuidanceEstimatedArrivalViewData createFromParcel(Parcel in) {
            return new GuidanceEstimatedArrivalViewData(in);
        }

        @Override
        public GuidanceEstimatedArrivalViewData[] newArray(int size) {
            return new GuidanceEstimatedArrivalViewData[size];
        }
    };

    /**
     * ETA date.
     */
    private final Date mEta;
    /**
     * Distance to destination.
     */
    private final long mDistance;
    /**
     * Remaining duration of the travel.
     */
    private final int mDuration;

    /**
     * Constructs a new instance using the provided icon and info strings.
     *
     * @param etaDate
     *         estimated time of arrival.
     * @param distance
     *         distance to the destination.
     * @param duration
     *         remaining duration of the travel.
     */
    public GuidanceEstimatedArrivalViewData(Date etaDate, long distance, int duration) {
        mEta = new Date(etaDate.getTime());
        mDistance = distance;
        mDuration = duration;
    }

    GuidanceEstimatedArrivalViewData(Parcel in) {
        mEta = (Date) in.readSerializable();
        mDistance = in.readLong();
        mDuration = in.readInt();
    }

    /**
     * Gets the estimated time of arrival at the destination.
     *
     * @return estimated time of arrival.
     */
    public Date getEta() {
        return new Date(mEta.getTime());
    }

    /**
     * Gets the distance to the destination.
     *
     * @return a distance in meters.
     */
    public long getDistance() {
        return mDistance;
    }

    /**
     * Gets the remaining duration of the travel.
     *
     * @return remaining travel time in milliseconds.
     */
    public int getDuration() {
        return mDuration;
    }

    @Override
    public String toString() {
        return "GuidanceEstimatedArrivalViewData(mEta=" + DateFormatterUtil.format(this.mEta) +
                ", mDistance=" + this.mDistance +
                ", mDuration=" + this.mDuration +
                ")";
    }

    @Override
    public int hashCode() {
        return ((this.mDuration * 31 +
                (int) (this.mDistance ^ this.mDistance >>> 32)) * 31 +
                (this.mEta == null ? 0 : this.mEta.hashCode())) * 31;
    }

    @Override
    public boolean equals(Object obj1) {
        if (obj1 instanceof GuidanceEstimatedArrivalViewData) {
            final GuidanceEstimatedArrivalViewData obj2 = (GuidanceEstimatedArrivalViewData) obj1;
            if (areEqual(this.mEta, obj2.mEta) &&
                    this.mDistance == obj2.mDistance &&
                    this.mDuration == obj2.mDuration) {
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
        dest.writeSerializable(mEta);
        dest.writeLong(mDistance);
        dest.writeInt(mDuration);
    }
}
