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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContinentMapControllerTest {

    @Test
    public void testGetBoardSize() {
        ContinentMapController controller = new ContinentMapController(new Integer[] {1});
        assertEquals(1, controller.getBoardSize());

        controller = new ContinentMapController(ContinentMapController.DEFAULT_MAP_HEIGHTS);
        assertEquals(5, controller.getBoardSize());
    }

    @Test
    public void testBuildUpContinentalDivideSimple() {
        ContinentMapController controller = new ContinentMapController(new Integer[]{1});
        controller.buildUpContinentalDivide(false);
        assertTrue(controller.getCell(0, 0).flowsSE);
        assertTrue(controller.getCell(0, 0).flowsNW);
        assertFalse(controller.getCell(0, 0).processing);
    }

    @Test
    public void testBuildUpContinentalDivideSmall() {
        // Build the contintental divide just from cell (0, 0). This will determine what flows into
        // cell (0, 0), and thus into the NW / Green ocean.
        ContinentMapController controller = new ContinentMapController(new Integer[] {
                1, 2, 3,
                2, 3, 2,
                3, 2, 1
        });
        controller.buildUpContinentalDivide(false);

        // Only flow NW
        assertTrue(controller.getCell(0, 0).flowsNW);
        assertFalse(controller.getCell(0, 0).flowsSE);
        assertTrue(controller.getCell(0, 1).flowsNW);
        assertFalse(controller.getCell(0, 1).flowsSE);
        assertTrue(controller.getCell(1, 0).flowsNW);
        assertFalse(controller.getCell(1, 0).flowsSE);

        // Only flow SE
        assertTrue(controller.getCell(2, 2).flowsSE);
        assertFalse(controller.getCell(2, 2).flowsNW);
        assertTrue(controller.getCell(2, 1).flowsSE);
        assertFalse(controller.getCell(2, 1).flowsNW);
        assertTrue(controller.getCell(1, 2).flowsSE);
        assertFalse(controller.getCell(1, 2).flowsNW);

        // Flow both ways
        assertTrue(controller.getCell(0, 2).flowsNW);
        assertTrue(controller.getCell(0, 2).flowsSE);
        assertTrue(controller.getCell(1, 1).flowsNW);
        assertTrue(controller.getCell(1, 1).flowsSE);
        assertTrue(controller.getCell(2, 0).flowsNW);
        assertTrue(controller.getCell(2, 0).flowsSE);
    }

    @Test
    public void testBuildUpContinentalDivideBasin() {
        ContinentMapController controller = new ContinentMapController(new Integer[] {
                1, 2, 3,
                2, 0, 2,
                3, 2, 1
        });
        controller.buildUpContinentalDivide(false);

        assertTrue(controller.getCell(1, 1).basin);
        assertFalse(controller.getCell(1, 1).flowsNW);
        assertFalse(controller.getCell(1, 1).flowsSE);

        // Nothing else is a basin
        assertFalse(controller.getCell(0, 0).basin);
        assertFalse(controller.getCell(0, 1).basin);
        assertFalse(controller.getCell(1, 0).basin);
        assertFalse(controller.getCell(0, 2).basin);
        assertFalse(controller.getCell(1, 2).basin);
        assertFalse(controller.getCell(2, 2).basin);
        assertFalse(controller.getCell(2, 1).basin);
        assertFalse(controller.getCell(2, 0).basin);
    }

    @Test
    public void testBuildUpContinentalDivideEqualNeighborsBasin() {
        ContinentMapController controller = new ContinentMapController(
                ContinentMapController.MAP_EQUAL_NEIGHBORS_HEIGHTS);
        controller.buildUpContinentalDivide(false);
        assertFalse(controller.getCell(2, 2).basin);
        assertTrue(controller.getCell(2, 2).flowsSE);
        assertFalse(controller.getCell(2, 2).flowsNW);
    }

    @Test
    public void testBuildDownContinentalDivideSimple() {
        ContinentMapController controller = new ContinentMapController(new Integer[]{1});
        controller.buildDownContinentalDivide(false);
        assertTrue(controller.getCell(0, 0).flowsSE);
        assertTrue(controller.getCell(0, 0).flowsNW);
        assertFalse(controller.getCell(0, 0).processing);
    }

    @Test
    public void testBuildDownContinentalDivideSmall() {
        // Build the contintental divide just from cell (0, 0). This will determine what flows into
        // cell (0, 0), and thus into the NW / Green ocean.
        ContinentMapController controller = new ContinentMapController(new Integer[] {
                1, 2, 3,
                2, 3, 2,
                3, 2, 1
        });
        controller.buildDownContinentalDivide(false);

        // Only flow NW
        assertTrue(controller.getCell(0, 0).flowsNW);
        assertFalse(controller.getCell(0, 0).flowsSE);
        assertTrue(controller.getCell(0, 1).flowsNW);
        assertFalse(controller.getCell(0, 1).flowsSE);
        assertTrue(controller.getCell(1, 0).flowsNW);
        assertFalse(controller.getCell(1, 0).flowsSE);

        // Only flow SE
        assertTrue(controller.getCell(2, 2).flowsSE);
        assertFalse(controller.getCell(2, 2).flowsNW);
        assertTrue(controller.getCell(2, 1).flowsSE);
        assertFalse(controller.getCell(2, 1).flowsNW);
        assertTrue(controller.getCell(1, 2).flowsSE);
        assertFalse(controller.getCell(1, 2).flowsNW);

        // Flow both ways
        assertTrue(controller.getCell(0, 2).flowsNW);
        assertTrue(controller.getCell(0, 2).flowsSE);
        assertTrue(controller.getCell(1, 1).flowsNW);
        assertTrue(controller.getCell(1, 1).flowsSE);
        assertTrue(controller.getCell(2, 0).flowsNW);
        assertTrue(controller.getCell(2, 0).flowsSE);
    }

    @Test
    public void testBuildDownContinentalDivideBasin() {
        ContinentMapController controller = new ContinentMapController(new Integer[] {
                1, 2, 3,
                2, 0, 2,
                3, 2, 1
        });
        controller.buildDownContinentalDivide(false);

        assertTrue(controller.getCell(1, 1).basin);
        assertFalse(controller.getCell(1, 1).flowsNW);
        assertFalse(controller.getCell(1, 1).flowsSE);

        // Nothing else is a basin
        assertFalse(controller.getCell(0, 0).basin);
        assertFalse(controller.getCell(0, 1).basin);
        assertFalse(controller.getCell(1, 0).basin);
        assertFalse(controller.getCell(0, 2).basin);
        assertFalse(controller.getCell(1, 2).basin);
        assertFalse(controller.getCell(2, 2).basin);
        assertFalse(controller.getCell(2, 1).basin);
        assertFalse(controller.getCell(2, 0).basin);
    }

    @Test
    public void testBuildDownContinentalDivideEqualNeighborsBasin() {
        ContinentMapController controller = new ContinentMapController(
                ContinentMapController.MAP_EQUAL_NEIGHBORS_HEIGHTS);
        controller.buildDownContinentalDivide(false);

        // TODO: Get this working.
        assertFalse(controller.getCell(2, 2).basin);
        assertTrue(controller.getCell(2, 2).flowsSE);
        assertFalse(controller.getCell(2, 2).flowsNW);
    }
}
