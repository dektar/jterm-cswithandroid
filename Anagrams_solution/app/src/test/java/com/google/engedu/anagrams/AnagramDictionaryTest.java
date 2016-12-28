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

package com.google.engedu.anagrams;

import org.junit.Test;

import static org.junit.Assert.*;

import android.text.TextUtils;

import java.io.InputStream;
import java.util.List;


/**
 * Tests for AnagramDictionary
 */

public class AnagramDictionaryTest {
    String[] words = {"act", "cat", "dog", "pot", "pots", "spots", "stop", "stops"};

    @Test
    public void testSortLetters() {
        assertEquals("act", AnagramDictionary.sortLetters("cat"));
        assertEquals("abcde", AnagramDictionary.sortLetters("abcde"));
        assertEquals("abcde", AnagramDictionary.sortLetters("edcba"));
        assertNotEquals("abcde", AnagramDictionary.sortLetters("abcdef"));
    }

    @Test
    public void testIsAnagram() {
        assertTrue(AnagramDictionary.isAnagram("cat", "act"));
        assertTrue(AnagramDictionary.isAnagram("spots", "stops"));
        assertFalse(AnagramDictionary.isAnagram("cat", "dog"));
        assertFalse(AnagramDictionary.isAnagram("cat", ""));
    }

    @Test
    public void testGetAnagrams() {
        AnagramDictionary dict = new AnagramDictionary(words);
        List<String> result = dict.getAnagrams("cat");
        assertEquals(2, result.size());
        assertTrue(result.contains("cat"));
        assertTrue(result.contains("act"));
        assertFalse(result.contains("dog"));
        assertFalse(result.contains("spots"));

        assertTrue(dict.getAnagrams("stops").contains("spots"));
    }

    /**
     * Example:
     * Input                              | Output
     * -----------------------------------| ------
     * isGoodWord("nonstop", "post")      | true
     * isGoodWord("poster", "post")       | false
     * isGoodWord("lamp post", "post")    | false
     * isGoodWord("spots", "post")        | true
     * isGoodWord("apostrophe", "post")   | false
     */
    @Test
    public void testIsGoodWord() {
        AnagramDictionary dict = new AnagramDictionary(words);
        assertTrue(dict.isGoodWord("stops", "pot"));
        assertFalse(dict.isGoodWord("pots", "pot"));
        assertFalse(dict.isGoodWord("abcdefg", "cat"));
    }
}
