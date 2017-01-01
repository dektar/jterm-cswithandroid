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

package com.google.engedu.continentaldivide;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContinentMapControllerTest {

    @Test
    public void testGetBoardSize() {
        ContinentMapController controller = new ContinentMapController(new Integer[] {1});
        assertEquals(1, controller.getBoardSize());

        controller = new ContinentMapController(ContinentMapController.DEFAULT_MAP_HEIGHTS);
        assertEquals(5, controller.getBoardSize());
    }
}
