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
 * A data class holding current speed and applicable speed limit.
 */
public class GuidanceSpeedData implements Parcelable {

    /**
     * Creator for parcelable.
     */
    public static final Creator<GuidanceSpeedData> CREATOR = new Creator<GuidanceSpeedData>() {
        @Override
        public GuidanceSpeedData createFromParcel(Parcel in) {
            return new GuidanceSpeedData(in);
        }

        @Override
        public GuidanceSpeedData[] newArray(int size) {
            return new GuidanceSpeedData[size];
        }
    };

    private static final int INVALID_VALUE = -1;
    private static final double COMPARISION_PRECISION = .0000001;

    private double mCurrentSpeed;
    private double mCurrentSpeedLimit;

    /**
     * Constructs a new instance.
     */
    public GuidanceSpeedData() {
        this(INVALID_VALUE, INVALID_VALUE);
    }

    /**
     * Constructs a new instance using current speed and color values.
     *
     * @param speed
     *          a current speed value in meter per second.
     * @param speedLimit
     *          a current speed limit value in meter per second.
     */
    public GuidanceSpeedData(double speed, double speedLimit) {
        mCurrentSpeed = speed;
        mCurrentSpeedLimit = speedLimit;
    }

    GuidanceSpeedData(Parcel in) {
        mCurrentSpeed = in.readDouble();
        mCurrentSpeedLimit = in.readDouble();
    }

    /**
     * Gets current speed.
     *
     * @return a speed value in meter per second.
     */
    public double getCurrentSpeed() {
        return mCurrentSpeed;
    }

    /**
     * Gets current speed limit.
     *
     * @return a speed limit value in meter per second.
     */
    public double getCurrentSpeedLimit() {
        return mCurrentSpeedLimit;
    }

    /**
     * Determines if current speed exceeds current speed limit.
     *
     * @return true if driving faster than allowed.
     */
    public boolean isSpeeding() {
        return mCurrentSpeed > mCurrentSpeedLimit && mCurrentSpeedLimit > 0;
    }

    /**
     * Sets internal data to invalid state.
     */
    public void invalidate() {
        mCurrentSpeed = INVALID_VALUE;
        mCurrentSpeedLimit = INVALID_VALUE;
    }

    /**
     * Checks if internal state is correct.
     *
     * @return true if object holds correct data, false otherwise.
     */
    public boolean isValid() {
        return mCurrentSpeed > INVALID_VALUE && mCurrentSpeedLimit > INVALID_VALUE;
    }

    @Override
    public String toString() {
        return "GuidanceSpeedData(mCurrentSpeed=" + this.mCurrentSpeed +
                ", mCurrentSpeedLimit=" + this.mCurrentSpeedLimit +
                ")";
    }

    @Override
    public int hashCode() {
        return (int) (this.mCurrentSpeed * 31) + (int) (this.mCurrentSpeedLimit * 31);
    }

    @Override
    public boolean equals(Object obj1) {
        if (obj1 instanceof GuidanceSpeedData) {
            final GuidanceSpeedData obj2 = (GuidanceSpeedData) obj1;
            return Math.abs(this.mCurrentSpeed - obj2.mCurrentSpeed) < COMPARISION_PRECISION &&
                    Math.abs(this.mCurrentSpeedLimit - obj2.mCurrentSpeedLimit) < COMPARISION_PRECISION;
        }
        return false;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mCurrentSpeed);
        dest.writeDouble(mCurrentSpeedLimit);
    }
}
