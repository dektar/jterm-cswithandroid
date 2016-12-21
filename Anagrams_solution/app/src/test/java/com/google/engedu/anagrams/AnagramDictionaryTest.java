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


/**
 * Tests for AnagramDictionary
 */

public class AnagramDictionaryTest {
    @Test
    public void testSortLetters() {
        assertEquals(AnagramDictionary.sortLetters("cat"), "act");
        assertEquals(AnagramDictionary.sortLetters("abcde"), "abcde");
        assertEquals(AnagramDictionary.sortLetters("edcba"), "abcde");
        assertNotEquals(AnagramDictionary.sortLetters("abcdef"), "abcde");
    }

    @Test
    public void testIsAnagram() {
        assertTrue(AnagramDictionary.isAnagram("cat", "act"));
        assertTrue(AnagramDictionary.isAnagram("spots", "stops"));
        assertFalse(AnagramDictionary.isAnagram("cat", "dog"));
        assertFalse(AnagramDictionary.isAnagram("cat", ""));
    }

    /**
     * Example:
     * Input                      | Output
     * ---------------------------| ------
     * isGoodWord("nonstop")      | true
     * isGoodWord("poster")       | false
     * isGoodWord("lamp post")    | false
     * isGoodWord("spots")        | true
     * isGoodWord("apostrophe")   | false
     */
    @Test
    public void testIsGoodWord() {
       // This may need to be an androidTest.
    }
}
