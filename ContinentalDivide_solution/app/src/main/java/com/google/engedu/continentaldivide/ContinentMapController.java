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

import android.support.annotation.VisibleForTesting;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class ContinentMapController {
    private static final int MAX_HEIGHT = 255;

    @VisibleForTesting
    protected static Integer[] DEFAULT_MAP_HEIGHTS = {
            1, 2, 3, 4, 5,
            2, 3, 4, 5, 6,
            3, 4, 5, 3, 1,
            6, 7, 3, 4, 5,
            5, 1, 2, 3, 4,
    };

    @VisibleForTesting
    protected static Integer[] LAKE_MAP_HEIGHTS = {
            1, 2, 3, 4, 5,
            2, 3, 4, 5, 6,
            3, 4, 0, 0, 1,
            6, 7, 3, 1, 5,
            5, 1, 2, 3, 4,
    };

    @VisibleForTesting
    protected static Integer[] MAP_EQUAL_NEIGHBORS_HEIGHTS = {
            50, 50, 50, 50, 60,
            50, 22, 26, 70, 50,
            50, 24, 30, 30, 29,
            50, 28, 28, 29, 22,
            60, 50, 50, 50, 50,
    };

    // An inner class to store the information we need for each point in the map
    public class Cell {
        // The height of this spot
        int height = 0;

        // Whether this cell flows to the Green Ocean
        boolean flowsNW = false;

        // Whether this cell flows to the Blue Ocean
        boolean flowsSE = false;

        // Whether this cell has been determined to flow to neither ocean
        boolean basin = false;

        // A flag to allow us to track whether the current cell is already being evaluated
        boolean processing = false;
    }

    private Cell[] map;
    private int boardSize;
    private Random random = new Random();
    private int maxHeight = 0, minHeight = 0;

    public ContinentMapController() {
        this(MAP_EQUAL_NEIGHBORS_HEIGHTS);
    }

    @VisibleForTesting
    ContinentMapController(Integer[] mapHeights) {
        boardSize = (int) (Math.sqrt(mapHeights.length));
        map = new Cell[boardSize * boardSize];
        for (int i = 0; i < boardSize * boardSize; i++) {
            map[i] = new Cell();
            map[i].height = mapHeights[i];
        }
        maxHeight = Collections.max(Arrays.asList(mapHeights));
        minHeight = Collections.min(Arrays.asList(mapHeights));
    }

    /**
     * Gets the cell at a specific x, y location on the board.
     */
    public Cell getCell(int x, int y) {
        if (x >=0 && x < boardSize && y >= 0 && y < boardSize)
            return map[x + boardSize * y];
        else
            return null;
    }

    /**
     * Returns the size of the board. The board is always a square, so a 4x4 board
     * of 16 squares has size 4.
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Gets the max height of the current board.
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Gets the minimum height of the current board.
     */
    public int getMinHeight() {
        return minHeight;
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
     * This calls buildUpContinentalDivideRecursively on each edge cell that has not yet been
     * processed.
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
     * Helper for the above buildUpContinentalDivide, which builds it up for just one cell.
     * @param flowsNW Whether the neighbor before this cell flows NW
     * @param flowsSE Whether the neighbor before this cell flows SE
     * @param previousHeight The height of the neighbor before this cell
     */
    private void buildUpContinentalDivideRecursively(
            int x, int y, boolean flowsNW, boolean flowsSE, int previousHeight) {
        // EXAMPLE SOLUTION

        // Check we are on the board
        if (x < 0 || x >= boardSize || y < 0 || y >= boardSize) {
            return;
        }

        Cell cell = getCell(x, y);
        // Check we are at least higher than our neighbor
        if (previousHeight > cell.height) {
            // This may be a basin if it doesn't flow anywhere yet.
            cell.basin = !cell.flowsNW && !cell.flowsSE;
            return;
        }

        // Check we are not already working on this cell
        if (cell.processing) {
            return;
        }

        // Now do the recursive work on this cell's neighbors
        cell.processing = true;

        // This cell will flow the same direction as its neighbor / parent, because it is taller
        // than its neighbor / parent, and abutts it.
        cell.flowsNW = cell.flowsNW || flowsNW;
        cell.flowsSE = cell.flowsSE || flowsSE;
        cell.basin = false;
        buildUpContinentalDivideRecursively(x - 1, y, cell.flowsNW, cell.flowsSE, cell.height);
        buildUpContinentalDivideRecursively(x + 1, y, cell.flowsNW, cell.flowsSE, cell.height);
        buildUpContinentalDivideRecursively(x, y - 1, cell.flowsNW, cell.flowsSE, cell.height);
        buildUpContinentalDivideRecursively(x, y + 1, cell.flowsNW, cell.flowsSE, cell.height);

        cell.processing = false;

        // END EXAMPLE SOLUTION
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
     * Helper for the above buildDownContinentalDivide, which builds down the continental divide
     * starting at a single cell. This calculates the cell at the X and Y position given recursively
     * @param previousHeight The height of the previous neighbor who called this function.
     */
    private Cell buildDownContinentalDivideRecursively(int x, int y, int previousHeight) {
        // EXAMPLE SOLUTION
        // If we are off the board.
        if (x < 0 || y < 0 || x >= boardSize || y >= boardSize) {
            Cell workingCell = new Cell();
            if (x < 0 || y < 0) {
                workingCell.flowsNW = true;
            }
            if (x >= boardSize || y >= boardSize) {
                workingCell.flowsSE = true;
            }
            return workingCell;
        }

        Cell cell = getCell(x, y);
        if (cell.processing || cell.height > previousHeight) {
            // Return something blank so as not to mess up the processing results.
            // Also, if the height of the current cell is taller than the previous one, skip this.
            // Instead of returning a "null" cell, a new blank cell will suffice because of OR logic
            return new Cell();
        }
        cell.processing = true;
        Cell north = buildDownContinentalDivideRecursively(x - 1, y, cell.height);
        Cell south = buildDownContinentalDivideRecursively(x + 1, y, cell.height);
        Cell west = buildDownContinentalDivideRecursively(x, y - 1, cell.height);
        Cell east = buildDownContinentalDivideRecursively(x, y + 1, cell.height);
        cell.flowsNW = cell.flowsNW || north.flowsNW || south.flowsNW || east.flowsNW || west.flowsNW;
        cell.flowsSE = cell.flowsSE || north.flowsSE || south.flowsSE || east.flowsSE || west.flowsSE;
        if (!cell.flowsNW && !cell.flowsSE) {
            cell.basin = true;
        } else {
            cell.basin = false;
        }
        cell.processing = false;
        return cell;
        // END EXAMPLE SOLUTION
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
