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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;
    private Random mRandom;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
        mRandom = new Random();
    }

    public void add(String s) {
        if (s.length() == 0) {
            isWord = true;
            return;
        }
        String firstChar = s.substring(0, 1);
        String remaining = s.substring(1);
        if (children.containsKey(firstChar)) {
            children.get(firstChar).add(remaining);
        } else {
            TrieNode child = new TrieNode();
            children.put(firstChar, child);
            child.add(remaining);
        }
    }

    public boolean isWord(String s) {
        // Are we at the base node?
        if (s.length() == 0) {
            return isWord;
        }
        // Do we have children that start with this character?
        String firstChar = s.substring(0, 1);
        if (children.containsKey(firstChar)) {
            // If so, do they think it is a word?
            String remaining = s.substring(1);
            return children.get(firstChar).isWord(remaining);
        }
        return false;
    }

    // TODO need strictly longer than prefix
    public String getAnyWordStartingWith(String s) {
        // Base case: No prefix at all -- s is null.
        if (s == null) {
            if (children.size() > 0) {
                // Pick any next character and return that word
                String nextChar = pickRandomChildChar();
                String nextSuffix = children.get(nextChar).getAnyWordStartingWith(null);
                return nextChar + nextSuffix;
            } else {
                return "";
            }
        }
        if (s.length() == 0) {
            if (children.size() == 0 && isWord) {
                // We are a leaf node and a word!
                return "";
            }
            if (children.size() > (children.containsKey("") ? 1 : 0)) {
                // Pick any next character and return that word
                String nextChar = pickRandomChildChar();
                String nextSuffix = children.get(nextChar).getAnyWordStartingWith(null);
                return nextChar + nextSuffix;
            }
            // Otherwise we are a leaf node -- we have no children. Return null; we couldn't make
            // a valid word.
            return null;
        }
        String firstChar = s.substring(0, 1);
        if (children.containsKey(firstChar)) {
            String remaining = s.substring(1);
            String nextSuffix = children.get(firstChar).getAnyWordStartingWith(remaining);
            if (nextSuffix == null) {
                return null;
            }
            return firstChar + nextSuffix;
        }
        return null;
    }

    // Pick a random child character from all the children.
    private String pickRandomChildChar() {
        int index = mRandom.nextInt(children.size());
        int reached = 0;
        // Assume that the children's order doesn't change after creation so that this is truly
        // random.
        for (String s : children.keySet()) {
            if (index == reached) {
                return s;
            }
            reached++;
        }
        return null;
    }

    // Pick a random child character which is not itself a word, if possible, from all the children.
    private String pickRandomGoodChildChar() {
        List<String> goodKeys = new ArrayList<>();
        List<String> otherKeys = new ArrayList<>();
        for (String s : children.keySet()) {
            if (children.get(s).isWord) {
                otherKeys.add(s);
            } else {
                goodKeys.add(s);
            }
        }
        if (goodKeys.size() > 0) {
            return goodKeys.get(mRandom.nextInt(goodKeys.size()));
        } else {
            return otherKeys.get(mRandom.nextInt(otherKeys.size()));
        }
    }

    /**
     * This method should consider all the children of the current prefix and attempt to randomly
     * pick one that is not a complete word. Only if all the children of the current prefix are
     * words should it randomly select one of those. This is not an optimum computer player but
     * should make the game quite a bit more challenging.
     */
    public String getGoodWordStartingWith(String s) {
        if (s.length() == 0) {
            if (children.size() == 0) {
                // If there are no children, it depends on if we are a word.
                return isWord ? "" : null;
            }
            // Pick a child that is not a complete word, if possible.
            String childChar = pickRandomGoodChildChar();
            return childChar + children.get(childChar).getGoodWordStartingWith(s);
        }
        String firstChar = s.substring(0, 1);
        if (children.containsKey(firstChar)) {
            String result = children.get(firstChar).getGoodWordStartingWith(s.substring(1));
            if (result == null) {
                return null;
            }
            return firstChar + result;
        }
        return null;
    }
}
