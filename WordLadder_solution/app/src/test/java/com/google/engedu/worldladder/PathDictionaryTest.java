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

package com.google.engedu.worldladder;

import com.google.engedu.wordladder.PathDictionary;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class PathDictionaryTest {
    private String[] wordsArray = {"can", "cap", "cat", "dig", "dot", "dog", "fire", "gain", "gait",
            "wait", "ware", "wart", "wire"};

    @Test
    public void testIsWord() {
        ArrayList<String> words = new ArrayList<>(Arrays.asList(wordsArray));
        PathDictionary dict = new PathDictionary(words);
        assertTrue(dict.isWord("cat"));
        assertFalse(dict.isWord("tac"));
    }

    @Test
    public void testGetPath() {
        ArrayList<String> words = new ArrayList<>(Arrays.asList(wordsArray));
        PathDictionary dict = new PathDictionary(words);

        // Null means no path.
        assertNull(dict.findPath(null, "cat"));

        // Items of different length have no path.
        assertNull(dict.findPath("cat", "gain"));

        // An item is not a neighbor of itself.
        assertNull(dict.findPath("cat", "cat"));

        // Path of length 2 should contain exactly ["can", "cat"]
        List<String> result = dict.findPath("can", "cat");
        assertEquals(2, result.size());
        assertEquals("can", result.get(0));
        assertEquals("cat", result.get(1));

        // This should be ["dig", "dot", "dog"]
        result = dict.findPath("dig", "dot");
        assertEquals("dig", result.get(0));
        assertEquals("dog", result.get(1));
        assertEquals("dot", result.get(2));

        result = dict.findPath("gain", "fire");
        assertEquals(7, result.size());
    }
}