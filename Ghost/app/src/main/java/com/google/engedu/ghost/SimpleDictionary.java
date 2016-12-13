/* Copyright 2016 Google Inc. All Rights Reserved.
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

package com.google.engedu.ghost;

import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private static final String TAG = "SampleDictionary";
    private ArrayList<String> words;
    private Random mRandom;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
        mRandom = new Random();
    }

    @VisibleForTesting
    public SimpleDictionary(ArrayList<String> words, long randomSeed) {
        this.words = words;
        mRandom = new Random(randomSeed);
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if (prefix.equals("")) {
            return words.get(mRandom.nextInt(words.size()));
        } else {
            return findByPrefix(prefix);
        }
    }

    private String findByPrefix(String prefix) {
        int index = binarySearchForPrefix(0, words.size(), prefix);
        if (index == -1) {
            return null;
        }
        return words.get(index);
    }

    /**
     * Does a binary search through a list for the index of a word beginning with a certain prefix.
     * @param startIndex The index to start the search, inclusive.
     * @param endIndex The index to end the search, exclusive.
     * @param prefix The prefix to search for
     * @return The index of a word that starts with this prefix, or -1 if none is found.
     */
    private int binarySearchForPrefix(int startIndex, int endIndex, String prefix) {
        // Base cases: there are no items left to search.
        if (startIndex == endIndex) {
            return -1;
        }
        if (startIndex == endIndex - 1) {
            return words.get(startIndex).startsWith(prefix) ? startIndex : -1;
        }
        int mid = (startIndex + endIndex) / 2;
        // If it starts with the prefix, but is not the same as the prefix
        if (words.get(mid).startsWith(prefix) && words.get(mid).length() > prefix.length()) {
            return mid;
        }
        int comparison = words.get(mid).compareTo(prefix);
        if (comparison < 0) {
            // The word at mid is too early in the dict. Recursively search the second half.
            return binarySearchForPrefix(mid + 1, endIndex, prefix);
        } else {
            // The word at mid is too late in the dict. Recursively search the first half.
            return binarySearchForPrefix(startIndex, mid, prefix);
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        // TODO
        String selected = getAnyWordStartingWith(prefix);
        return selected;
    }
}
