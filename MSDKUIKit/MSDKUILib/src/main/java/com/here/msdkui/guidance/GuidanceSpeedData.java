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

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/**
 * A data class holding current speed and applicable speed limit.
 */
public class GuidanceSpeedData implements Parcelable {

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

    private Double mCurrentSpeed;
    private Double mCurrentSpeedLimit;

    /**
     * Constructs a new instance using current speed and color values.
     *
     * @param speed      a current speed value in meter per second.
     * @param speedLimit a current speed limit value in meter per second.
     */
    public GuidanceSpeedData(Double speed, Double speedLimit) {
        mCurrentSpeed = speed;
        mCurrentSpeedLimit = speedLimit;
    }

    protected GuidanceSpeedData(Parcel in) {
        if (in.readByte() == 0) {
            mCurrentSpeed = null;
        } else {
            mCurrentSpeed = in.readDouble();
        }
        if (in.readByte() == 0) {
            mCurrentSpeedLimit = null;
        } else {
            mCurrentSpeedLimit = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mCurrentSpeed == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mCurrentSpeed);
        }
        if (mCurrentSpeedLimit == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mCurrentSpeedLimit);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Gets current speed.
     *
     * @return a speed value in meter per second.
     */
    public Double getCurrentSpeed() {
        return mCurrentSpeed;
    }

    /**
     * Gets current speed limit.
     *
     * @return a speed limit value in meter per second.
     */
    public Double getCurrentSpeedLimit() {
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
     * Checks if internal state is correct.
     *
     * @return true if object holds correct data, false otherwise.
     */
    public boolean isValid() {
        return mCurrentSpeed != null && mCurrentSpeedLimit != null;
    }

    @NonNull
    @Override
    public String toString() {
        return "GuidanceSpeedData(mCurrentSpeed=" + this.mCurrentSpeed +
                ", mCurrentSpeedLimit=" + this.mCurrentSpeedLimit +
                ")";
    }

    @Override
    public int hashCode() {
        long var = Double.doubleToLongBits(this.mCurrentSpeed);
        int var1 = (int) (var ^ var >>> 32) * 31;
        long var2 = Double.doubleToLongBits(this.mCurrentSpeedLimit);
        return var1 + (int) (var2 ^ var2 >>> 32);
    }

    @Override
    public boolean equals(Object var1) {
        if (this != var1) {
            if (var1 instanceof GuidanceSpeedData) {
                GuidanceSpeedData var2 = (GuidanceSpeedData) var1;
                return Double.compare(this.mCurrentSpeed, var2.mCurrentSpeed) == 0 &&
                        Double.compare(this.mCurrentSpeedLimit, var2.mCurrentSpeedLimit) == 0;
            }
            return false;
        } else {
            return true;
        }
    }
}
