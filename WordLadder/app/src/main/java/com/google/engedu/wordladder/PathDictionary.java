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

package com.google.engedu.wordladder;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class PathDictionary {
    private static final int MAX_WORD_LENGTH = 4;
    private static final int MAX_SEARCH_DEPTH = 6;

    // Write a class implementing WordGraph
    private WordGraph graph;

    public PathDictionary(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        Log.i("Word ladder", "Loading dict");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() > MAX_WORD_LENGTH) {
                continue;
            }
        }
    }

    @VisibleForTesting
    public PathDictionary(List<String> dictWords) {
        for (String word : dictWords) {
            graph.addWord(word);
        }
    }

    public boolean isWord(String word) {
        return graph.isWord(word.toLowerCase());
    }

    /**
     * Finds the set of words connecting two words, or returns null if none is possible.
     * @param start The string to start the word ladder
     * @param end The string to end the word ladder
     * @return An ordered list of the connecting words between start and end
     */
    public List<String> findPath(String start, String end) {
        //
        // Your code here
        //
        return null;
    }
}
