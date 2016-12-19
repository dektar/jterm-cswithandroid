/**
 *  Copyright 2016 Google Inc. All Rights Reserved.
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

package com.google.engedu.palindromes;

import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import java.util.ArrayList;


public class PalindromeGroup {
    private ArrayList<String> strings = new ArrayList<>();

    /**
     * Constructor that builds a one-element group with a single palindrome from a specified segment
     * of the text.
     * @param text
     * @param start inclusive
     * @param end exclusive
     */
    public PalindromeGroup(char[] text, int start, int end) {
        strings.add(new String(text, start, end - start));
    }

    /**
     * Appends the palindromes from another PalindromeGroup to this group.
     */
    public void append(PalindromeGroup other) {
        if (other != null) {
            strings.addAll(other.strings);
        }
    }

    /**
     * The number of palindromes in this group.
     */
    public int length() {
        return strings.size();
    }

    /**
     * Represent this group as a string of palindromes separated by spaces.
     */
    @Override
    public String toString() {
        return TextUtils.join(" ", strings);
    }

    @VisibleForTesting
    ArrayList<String> getStrings() {
        return strings;
    }
}
