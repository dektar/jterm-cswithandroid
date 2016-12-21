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

import java.util.ArrayList;

/**
 * The interface to a WordGraph. This can be implemented in different ways!
 */
public interface WordGraph {
    /**
     * Gets the neighbors of a word, i.e. other words that differ in just one character.
     * @param word
     * @return The list of neighboring words, or null if word is not in the dict.
     */
    ArrayList<String> getNeighbors(String word);

    /**
     * Adds a word to the graph.
     * @param word
     */
    void addWord(String word);

    /**
     * Checks whether a word is in the graph.
     * @param word
     * @return true if the word is in the graph.
     */
    boolean isWord(String word);
}
