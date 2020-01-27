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

import java.util.List;

/**
 * A container of Builders that allows to create option items.
 */
public final class OptionItemBuilders {

    /**
     * Builder for {@link BooleanOptionItem}.
     */
    public static class BooleanOptionItemBuilder {

        private final BooleanOptionItem mItem;

        /**
         * Constructs a new instance.
         * @param context an activity or application context.
         */
        public BooleanOptionItemBuilder(final Context context) {
            mItem = new BooleanOptionItem(context);
        }

        /**
         * Set a label for this item.
         * @param label the label to set.
         * @return an instance of this class.
         */
        public BooleanOptionItemBuilder setLabel(String label) {
            mItem.setLabel(label);
            return this;
        }

        /**
         * Set an id for this item.
         * @param id the id to set.
         * @return an instance of this class.
         */
        public BooleanOptionItemBuilder setItemId(int id) {
            mItem.setItemId(id);
            return this;
        }

        /**
         * Build the item.
         * @return the item that was built.
         */
        public BooleanOptionItem build() {
            return mItem;
        }
    }

    /**
     * Builder for {@link SingleChoiceOptionItem}.
     */
    public static class SingleChoiceOptionItemBuilder {

        private final SingleChoiceOptionItem mItem;

        /**
         * Constructs a new instance.
         * @param context an activity or application context.
         */
        public SingleChoiceOptionItemBuilder(final Context context) {
            mItem = new SingleChoiceOptionItem(context);
        }

        /**
         * Set the labels for this item.
         * @param title the title for this item.
         * @param labels the labels to set.
         * @return an instance of this class.
         */
        public SingleChoiceOptionItemBuilder setLabels(String title, List<String> labels) {
            mItem.setLabels(title, labels);
            return this;
        }

        /**
         * Set an id for this item.
         * @param id the id to set.
         * @return an instance of this class.
         */
        public SingleChoiceOptionItemBuilder setItemId(int id) {
            mItem.setItemId(id);
            return this;
        }

        /**
         * Build the item.
         * @return the item that was built.
         */
        public SingleChoiceOptionItem build() {
            return mItem;
        }
    }

    /**
     * Builder for {@link MultipleChoiceOptionItem}.
     */
    public static class MultipleChoiceOptionItemBuilder {

        private final MultipleChoiceOptionItem mItem;

        /**
         * Constructs a new instance.
         * @param context an activity or application context.
         */
        public MultipleChoiceOptionItemBuilder(final Context context) {
            mItem = new MultipleChoiceOptionItem(context);
        }

        /**
         * Set the labels for this item.
         * @param labels the labels to set.
         * @return an instance of this class.
         */
        public MultipleChoiceOptionItemBuilder setLabels(List<String> labels) {
            mItem.setLabels(labels);
            return this;
        }

        /**
         * Set an id for this item.
         * @param id the id to set.
         * @return an instance of this class.
         */
        public MultipleChoiceOptionItemBuilder setItemId(int id) {
            mItem.setItemId(id);
            return this;
        }

        /**
         * Build the item.
         * @return the item that was built.
         */
        public MultipleChoiceOptionItem build() {
            return mItem;
        }
    }


     /**
     * Builder for {@link NumericOptionItem}.
     */
    public static class NumericOptionItemBuilder {

        private final NumericOptionItem mItem;

        /**
         * Constructs a new instance.
         * @param context an activity or application context.
         */
        public NumericOptionItemBuilder(final Context context) {
            mItem = new NumericOptionItem(context);
        }

        /**
         * Set a label for this item.
         * @param label the label to set.
         * @return an instance of this class.
         */
        public NumericOptionItemBuilder setLabel(String label) {
            mItem.setLabel(label);
            return this;
        }

        /**
         * Set an id for this item.
         * @param id the id to set.
         * @return an instance of this class.
         */
        public NumericOptionItemBuilder setItemId(int id) {
            mItem.setItemId(id);
            return this;
        }

        /**
         * Set input type for this item.
         * @param inputType the input type to use.
         * @return an instance of this class.
         */
        public NumericOptionItemBuilder setInputType(int inputType) {
            mItem.setInputType(inputType);
            return this;
        }

        /**
         * Build the item.
         * @return the item that was built.
         */
        public NumericOptionItem build() {
            return mItem;
        }
    }
}
