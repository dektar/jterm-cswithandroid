/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.engedu.palindromes;

import android.util.Range;

import java.util.HashMap;

/**
 * Helper class for finding palindromes
 */
public class PalindromeHelper {

    HashMap<String, PalindromeGroup> mSavedPalindromes = new HashMap<>();

    /**
     * Determines if text from start to end is a palindrome
     * @param text the text to check, all lower-cased and without special chars.
     * @param start inclusive
     * @param end exclusive
     * @return
     */
    public static boolean isPalindrome(char[] text, int start, int end)
            throws IllegalArgumentException {
        if (text.length == 0) {
            return true;
        }
        if (start >= end) {
            throw new IllegalArgumentException();
        }
        int j = end - 1;
        for (int i = start; i < end; i++) {
            if (i >= j) {
                break;
            }
            if (text[i] != text[j--]) {
                return false;
            }
        }
        return true;
    }

    public PalindromeGroup breakIntoPalindromes(char[] text, int start, int end) {
        //return greedyBreakIntoPalindromes(text, start, end);
        //return recursiveBreakIntoPalindromes(text, start, end);
        return dynamicBreakIntoPalindromes(text, start, end);
    }

    /**
     * Greedy implementation: Find the largest possible palindrome and then recurse on the end.
     */
    private PalindromeGroup greedyBreakIntoPalindromes(char[] text, int start, int end) {
        if (start == end - 1) {
            return new PalindromeGroup(text, start, end);
        }
        int next = end;
        while (next > start) {
            if (isPalindrome(text, start, next)) {
                // This is the biggest one, starting at the end! Save it and recursively search.
                PalindromeGroup group = new PalindromeGroup(text, start, next);
                PalindromeGroup remaining = greedyBreakIntoPalindromes(text, next, end);
                group.append(remaining);
                return group;
            }
            next--;
        }
        return null;
    }

    /**
     * Recursive implementation: Looks for all prefixes of the string that are palindromes and
     * recurse on the rest of the string and picks the combination with the smallest number of
     * total palindromes
     */
    private PalindromeGroup recursiveBreakIntoPalindromes(char[] text, int start, int end) {
        if (start == end - 1) {
            return new PalindromeGroup(text, start, end);
        }
        int bestLength = Integer.MAX_VALUE;
        PalindromeGroup bestGroup = null;
        // Go through all the prefixes which are palindromes, and find the best length.
        for (int i = start + 1; i <= end; i++) {
            if (isPalindrome(text, start, i)) {
                PalindromeGroup group = new PalindromeGroup(text, start, i);
                PalindromeGroup remaining = recursiveBreakIntoPalindromes(text, i, end);
                group.append(remaining);
                // If this is the shortest one we've found yet, save it!
                if (group.length() < bestLength) {
                    bestLength = group.length();
                    bestGroup = group;
                }
            }
        }
        return bestGroup;
    }

    /**
     * Dynamic implementation: Recursive implementation with dynamic programming.
     */
    private PalindromeGroup dynamicBreakIntoPalindromes(char[] text, int start, int end) {
        // See if we've saved it dynamically.
        String key = String.copyValueOf(text, start, end - start);
        if (mSavedPalindromes.containsKey(key)) {
            return mSavedPalindromes.get(key);
        }
        int bestLength = Integer.MAX_VALUE;
        PalindromeGroup bestGroup = null;
        // Go through all the prefixes which are palindromes, and find the best length.
        for (int i = start + 1; i <= end; i++) {
            if (isPalindrome(text, start, i)) {
                PalindromeGroup group = new PalindromeGroup(text, start, i);
                PalindromeGroup remaining = dynamicBreakIntoPalindromes(text, i, end);
                group.append(remaining);
                // If this is the shortest one we've found yet, save it!
                if (group.length() < bestLength) {
                    bestLength = group.length();
                    bestGroup = group;
                }
            }
        }
        // Now that we have a best group over this range, save it for later!
        mSavedPalindromes.put(key, bestGroup);
        return bestGroup;
    }
}
