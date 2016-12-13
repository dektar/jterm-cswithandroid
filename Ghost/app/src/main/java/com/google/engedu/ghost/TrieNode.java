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

import java.util.HashMap;
import java.util.Iterator;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        if (s.length() == 0) {
            TrieNode child = new TrieNode();
            children.put("", child);
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
            return children.containsKey(s);
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
                String nextChar = children.keySet().iterator().next(); // TODO: Use a random.
                String nextSuffix = children.get(nextChar).getAnyWordStartingWith(null);
                return nextChar + nextSuffix;
            } else {
                return "";
            }
        }
        // If s has no length next but is not null, we need to return a word that is at least
        // one character longer.
        if (s.length() == 0) {
            if (children.size() > (children.containsKey("") ? 1 : 0)) {
                // Pick any next character and return that word, as long as the character is not
                // also the empty string.
                Iterator<String> iter = children.keySet().iterator(); // TODO use a random
                String nextChar = iter.next();
                if (nextChar.equals("")) {
                    nextChar = iter.next();
                }
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

    public String getGoodWordStartingWith(String s) {
        // TODO
        return getAnyWordStartingWith(s);
    }
}
