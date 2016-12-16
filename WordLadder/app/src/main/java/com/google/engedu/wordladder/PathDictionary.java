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
    private WordGraph graph = new SimpleWordGraph();

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
            graph.addWord(word);
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
     * @param start
     * @param end
     * @return
     */
    public List<String> findPath(String start, String end) {
        if (start == null || end == null || start.length() != end.length() || start.equals(end)) {
            return null;
        }
        // Here we will hold all the paths ending in the nodes we wish to search next.
        // We could store just a Queue<String> if we only wanted to know if a path exists, but
        // we need to store the whole List<String> to remember the path as we walk down it.
        Queue<List<String>> queue = new ArrayDeque<>();
        List<String> initial = new ArrayList<>();
        initial.add(start);

        // Breadth first search can be implemented with a "while" loop, which continues to search
        // until the queue is empty. Initialize the queue with the start string.

        queue.add(initial);
        while (!queue.isEmpty()) {
            List<String> current = queue.remove();
            // The final element in the next list is the most recently added graph point.
            // Find its neighbors and add them to the list, then enqueue the newly created lists,
            // to continue the search.
            List<String> neighbors = graph.getNeighbors(current.get(current.size() - 1));
            for (String neighbor : neighbors) {
                if (current.contains(neighbor)) {
                    // No repeats.
                    continue;
                }
                List<String> next = new ArrayList<>(current);
                next.add(neighbor);
                // If the neighbor is equal to the end, we are done! Return early.
                if (neighbor.equals(end)) {
                    return next;
                }
                if (next.size() > MAX_SEARCH_DEPTH) {
                    continue;
                }
                queue.add(next);
            }
        }
        // We didn't find anything.
        return null;
    }
}
