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

import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    //private ArrayList<String> wordList = new ArrayList<>();  // ArrayList implementation
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (!TextUtils.isEmpty(word)) {
                //wordList.add(word);  // ArrayList implementation
                addWordToDict(word);
            }
        }
    }

    @VisibleForTesting
    public AnagramDictionary(String[] words) {
        for (int i = 0; i < words.length; i++) {
            addWordToDict(words[i]);
        }
    }

    private void addWordToDict(String word) {
        wordSet.add(word);
        String letters = sortLetters(word);
        if (lettersToWord.containsKey(letters)) {
            lettersToWord.get(letters).add(word);
        } else {
            ArrayList<String> newList = new ArrayList<String>();
            newList.add(word);
            lettersToWord.put(letters, newList);
        }
    }

    /**
     * Asserts that the given word is in the dictionary and isn't formed by adding a letter to the
     * start or end of the base word.
     */
    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word)) {
            return false;
        }
        if (word.contains(base)) {
            return false;
        }
        return true;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        /*
        // ArrayList brute force method
        ArrayList<String> result = new ArrayList<String>();
        for (String word : wordList) {
            if (isAnagram(targetWord, word)) {
                result.add(word);
            }
        }
        */

        if (targetWord == null || targetWord.equals("")) {
            return null;
        }
        String letters = sortLetters(targetWord);
        if (lettersToWord.containsKey(letters)) {
            return lettersToWord.get(letters);
        }
        return null;
    }

    @VisibleForTesting
    static boolean isAnagram(String first, String second) {
        // Check empty
        if (first == null || first.equals("") || second == null || second.equals("")) {
            return false;
        }
        // Check length
        if (first.length() != second.length()) {
            return false;
        }
        return sortLetters(first).equals(sortLetters(second));
    }

    @VisibleForTesting
    static String sortLetters(String input) {
        char[] chars = input.toCharArray();
        Arrays.sort(chars);
        String result = "";
        for (char c : chars) {
            result += c;
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char c = 'a'; c <= 'z'; c++) {
            ArrayList<String> next = getAnagrams(word + c);
            if (next != null) {
                result.addAll(next);
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        Object[] keys = lettersToWord.keySet().toArray();
        String next = (String) keys[random.nextInt(keys.length)];
        while (lettersToWord.get(next).size() < MIN_NUM_ANAGRAMS) {
            next = (String) keys[random.nextInt(keys.length)];
        }
        ArrayList<String> words = lettersToWord.get(next);
        return words.get(random.nextInt(words.size()));
    }
}
