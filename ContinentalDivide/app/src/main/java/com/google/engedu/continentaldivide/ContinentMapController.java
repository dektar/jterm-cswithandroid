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

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class ContinentMapController {
    public static final int MAX_HEIGHT = 255;
    private Integer[] DEFAULT_MAP_HEIGHTS = {
            1, 2, 3, 4, 5,
            2, 3, 4, 5, 6,
            3, 4, 5, 3, 1,
            6, 7, 3, 4, 5,
            5, 1, 2, 3, 4,
    };

    // An inner class to store the information we need for each point in the map
    private class Cell {
        // The height of this spot
        protected int height = 0;

        // Whether this cell flows to the Green Ocean
        protected boolean flowsNW = false;

        // Whether this cell flows to the Blue Ocean
        protected boolean flowsSE = false;

        // Whether this cell has been determined to flow to neither ocean
        protected boolean basin = false;

        // A flag to allow us to track whether the current cell is already being evaluated
        protected boolean processing = false;
    }

    private Cell[] map;
    private int boardSize;
    private Random random = new Random();
    private int maxHeight = 0, minHeight = 0;

    public ContinentMapController() {
        boardSize = (int) (Math.sqrt(DEFAULT_MAP_HEIGHTS.length));
        map = new Cell[boardSize * boardSize];
        for (int i = 0; i < boardSize * boardSize; i++) {
            map[i] = new Cell();
            map[i].height = DEFAULT_MAP_HEIGHTS[i];
        }
        maxHeight = Collections.max(Arrays.asList(DEFAULT_MAP_HEIGHTS));
    }

    private Cell getCell(int x, int y) {
        if (x >=0 && x < boardSize && y >= 0 && y < boardSize)
            return map[x + boardSize * y];
        else
            return null;
    }

    /**
     * Resets the continental divide info for each cell (except the height)
     */
    public void clearContinentalDivide() {
        for (int i = 0; i < boardSize * boardSize; i++) {
            map[i].flowsNW = false;
            map[i].flowsSE = false;
            map[i].basin = false;
            map[i].processing = false;
        }
    }

    /**
     * Labels each cell's continental divide status by starting from the coasts and working uphill.
     * @param oneStep Note that this method takes a oneStep parameter which allows you to build the
     *                solution incrementally. This will be handy in debugging your own solution.
     */
    public void buildUpContinentalDivide(boolean oneStep) {
        if (!oneStep) {
            clearContinentalDivide();
        }
        boolean iterated = false;
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                Cell cell = getCell(x, y);
                if ((x == 0 || y == 0 || x == boardSize - 1 || y == boardSize - 1)) {
                    buildUpContinentalDivideRecursively(
                            x, y, x == 0 || y == 0, x == boardSize - 1 || y == boardSize - 1, -1);
                    if (oneStep) {
                        iterated = true;
                        break;
                    }
                }
            }
            if (iterated && oneStep)
                break;
        }
    }

    /**
     * Helper for the above buildUpContinentalDivide.
     */
    private void buildUpContinentalDivideRecursively(
            int x, int y, boolean flowsNW, boolean flowsSE, int previousHeight) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }

    /**
     * Labels each cell's continental divide status by starting from the peaks and working downhill.
     * @param oneStep  Note that this method takes a oneStep parameter which allows you to build the
     *                 solution incrementally. This will be handy in debugging your own solution.
     */
    public void buildDownContinentalDivide(boolean oneStep) {
        if (!oneStep)
            clearContinentalDivide();
        while (true) {
            int maxUnprocessedX = -1, maxUnprocessedY = -1, foundMaxHeight = -1;
            for (int y = 0; y < boardSize; y++) {
                for (int x = 0; x < boardSize; x++) {
                    Cell cell = getCell(x, y);
                    if (!(cell.flowsNW || cell.flowsSE || cell.basin) && cell.height > foundMaxHeight) {
                        maxUnprocessedX = x;
                        maxUnprocessedY = y;
                        foundMaxHeight = cell.height;
                    }
                }
            }
            if (maxUnprocessedX != -1) {
                buildDownContinentalDivideRecursively(maxUnprocessedX, maxUnprocessedY, foundMaxHeight + 1);
                if (oneStep) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    /**
     * Helper for the above buildUpContinentalDivide.
     */
    private Cell buildDownContinentalDivideRecursively(int x, int y, int previousHeight) {
        Cell workingCell = new Cell();
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        return workingCell;
    }

    /**
     * Method to pseudo-randomly generate other continent maps.
     * This is an extension for this unit's activity.
     */
    public void generateTerrain(int detail) {
        int newBoardSize = (int) (Math.pow(2, detail) + 1);
        if (newBoardSize != boardSize * boardSize) {
            boardSize = newBoardSize;
            map = new Cell[boardSize * boardSize];
            for (int i = 0; i < boardSize * boardSize; i++) {
                map[i] = new Cell();
            }
        }
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }
}
