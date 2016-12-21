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

package com.google.engedu.wordladder;

import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SimpleWordGraph implements WordGraph {
    HashMap<String, ArrayList<String>> graph = new HashMap<>();

    public SimpleWordGraph() {

    }

    @Override
    public ArrayList<String> getNeighbors(String word) {
        if (graph.containsKey(word)) {
            return graph.get(word);
        }
        return null;
    }

    @Override
    public void addWord(String word) {
        ArrayList<String> neighbors = new ArrayList<>();
        // This is O(N^2), not very efficient!
        for (String next : graph.keySet()) {
            if (isNeighbor(word, next)) {
                neighbors.add(next);
                graph.get(next).add(word);
            }
        }
        graph.put(word, neighbors);
    }

    @Override
    public boolean isWord(String word) {
        return graph.containsKey(word);
    }

    /**
     * Compares two words to see if they are neighbors, i.e. if they are equal in all but one
     * character.
     * @param word1
     * @param word2
     * @return
     */
    @VisibleForTesting
    boolean isNeighbor(String word1, String word2) {
        if (word1 == null || word2 == null || word1.length() != word2.length()) {
            // The words need to be the same length.
            return false;
        }
        int diffCount = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (!word1.substring(i, i + 1).equals(word2.substring(i, i + 1))) {
                diffCount++;
                if (diffCount > 1) {
                    // Stop early when we've found more than one difference.
                    return false;
                }
            }
        }
        return diffCount == 1;
    }
}
