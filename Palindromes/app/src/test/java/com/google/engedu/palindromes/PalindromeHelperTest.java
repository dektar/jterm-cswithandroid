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

import android.text.TextUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

/**
 * Tests for the PalindromeHelper class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TextUtils.class})
public class PalindromeHelperTest {
    private static final String TAG = "PalindromeHelperTest";

    @Test
    public void testIsNotPalindrome() {
        String[] palindromes = {"ab", "abc", "abbac", "wasitacatsisaw", "steponmypets",
                "nolemonnomelons"};
        for (String str : palindromes) {
            char[] text = str.toCharArray();
            assertFalse(PalindromeHelper.isPalindrome(text, 0, text.length));
        }
    }

    @Test
    public void testIsPalindrome() {
        String[] palindromes = {"a", "aba", "abba", "wasitacatisaw", "steponnopets",
                "nolemonnomelon"};
        for (String str : palindromes) {
            char[] text = str.toCharArray();
            assertTrue(PalindromeHelper.isPalindrome(text, 0, text.length));
        }
    }

    @Test
    public void testBreakIntPalindromes() {
        PalindromeHelper helper = new PalindromeHelper();

        char[] text = "a".toCharArray();
        PalindromeGroup group = helper.breakIntoPalindromes(text, 0, text.length);
        assertEquals("a", group.getStrings().get(0));

        text = "ab".toCharArray();
        group = helper.breakIntoPalindromes(text, 0, text.length);
        assertEquals("a", group.getStrings().get(0));
        assertEquals("b", group.getStrings().get(1));

        text = "aba".toCharArray();
        group = helper.breakIntoPalindromes(text, 0, text.length);
        assertEquals("aba", group.getStrings().get(0));

        text = "abba".toCharArray();
        group = helper.breakIntoPalindromes(text, 0, text.length);
        assertEquals("abba", group.getStrings().get(0));

        text = "abbac".toCharArray();
        group = helper.breakIntoPalindromes(text, 0, text.length);
        assertEquals("abba", group.getStrings().get(0));
        assertEquals("c", group.getStrings().get(1));
    }

    @Test
    public void testBreakIntoPalindromesGreedy() {
        PalindromeHelper helper = new PalindromeHelper();

        // This will break for the greedy algorithm
        char[] text = "abbacabb".toCharArray();
        PalindromeGroup group = helper.breakIntoPalindromes(text, 0, text.length);
        assertEquals("a", group.getStrings().get(0));
        assertEquals("bbacabb", group.getStrings().get(1));
    }

    @Test
    public void testBreakIntPalindromesRecursive() {
        PalindromeHelper helper = new PalindromeHelper();

        // This will be slow for the recursive algorithm
        // It took 468ms on my laptop for recursive, 6ms for greedy, and 3ms for dynamic recursive.
        char[] text = "aaaaaaaaaaaaaaaaabcdeeeeee".toCharArray();
        PalindromeGroup group = helper.breakIntoPalindromes(text, 0, text.length);
        assertEquals("aaaaaaaaaaaaaaaaa", group.getStrings().get(0));
        assertEquals("b", group.getStrings().get(1));
        assertEquals("c", group.getStrings().get(2));
        assertEquals("d", group.getStrings().get(3));
        assertEquals("eeeeee", group.getStrings().get(4));
    }
}