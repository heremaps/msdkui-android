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
 * A data class holding the current street name and a background color to be fed into the {@link GuidanceCurrentStreet}.
 */
public class GuidanceCurrentStreetData implements Parcelable {

    /**
     * Creator for parcelable.
     */
    public static final Creator<GuidanceCurrentStreetData> CREATOR = new Creator<GuidanceCurrentStreetData>() {
        @Override
        public GuidanceCurrentStreetData createFromParcel(Parcel in) {
            return new GuidanceCurrentStreetData(in);
        }

        @Override
        public GuidanceCurrentStreetData[] newArray(int size) {
            return new GuidanceCurrentStreetData[size];
        }
    };

    private final String mCurrentStreet;
    private final int mBackgroundColor;

    /**
     * Constructs a new instance using provided street name and background color.
     *
     * @param currentStreet
     *          name of the street.
     * @param backgroundColor
     *          a color value for a background used by {@link GuidanceCurrentStreet}.
     */
    public GuidanceCurrentStreetData(String currentStreet, int backgroundColor) {
        mCurrentStreet = currentStreet;
        mBackgroundColor = backgroundColor;
    }

    GuidanceCurrentStreetData(Parcel in) {
        mCurrentStreet = in.readString();
        mBackgroundColor = in.readInt();
    }

    /**
     * Gets name of the current street.
     *
     * @return name of the street.
     */
    public String getCurrentStreetName() {
        return mCurrentStreet;
    }

    /**
     * Gets background color to be used by {@link GuidanceCurrentStreet}.
     * @return a color value.
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCurrentStreet);
        dest.writeInt(mBackgroundColor);
    }
}
