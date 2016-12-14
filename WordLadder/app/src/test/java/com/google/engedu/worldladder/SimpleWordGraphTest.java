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

package com.google.engedu.worldladder;

import com.google.engedu.wordladder.SimpleWordGraph;
import com.google.engedu.wordladder.WordGraph;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleWordGraphTest {

    @Test
    public void testGetNeighbors() {
        SimpleWordGraph graph = new SimpleWordGraph();
        graph.addWord("cat");
        graph.addWord("cow");
        assertEquals(0, graph.getNeighbors("cat").size());

        graph.addWord("cap");
        assertEquals("cat", graph.getNeighbors("cap").get(0));

        graph.addWord("can");
        assertEquals(2, graph.getNeighbors("cat").size());
        assertEquals(2, graph.getNeighbors("can").size());
        assertEquals(2, graph.getNeighbors("cap").size());

        graph.addWord("cut");
        assertEquals(3, graph.getNeighbors("cat").size());
        assertEquals(1, graph.getNeighbors("cut").size());
        assertEquals(2, graph.getNeighbors("can").size());
    }
}
