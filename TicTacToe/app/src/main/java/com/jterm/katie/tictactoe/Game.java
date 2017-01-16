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

package com.jterm.katie.tictactoe;

import android.support.annotation.IntDef;
import android.text.TextUtils;

import org.w3c.dom.Text;

import java.lang.annotation.Retention;
import java.util.ArrayList;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Represents a tic tac toe game
 */
public class Game {
    @Retention(SOURCE)
    @IntDef({NO_PLAYER, FIRST_PLAYER, SECOND_PLAYER})
    public @interface PlayerId{}
    public static final int NO_PLAYER = 0;
    public static final int FIRST_PLAYER = 1;
    public static final int SECOND_PLAYER = 2;

    //private int[][] board;
    private ArrayList<ArrayList<Integer>> board;
    private String username_p1;
    private String username_p2;
    private @Game.PlayerId int result;
    private @Game.PlayerId int turn;

    public Game() {
    }

    public Game(String player) {
        username_p1 = player;

        // TODO: Initialize the board.
        //board = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    }

    public ArrayList<ArrayList<Integer>> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<ArrayList<Integer>> board) {
        this.board = board;
    }

    public String getUsername_p1() {
        return username_p1;
    }

    public void setUsername_p1(String username_p1) {
        this.username_p1 = username_p1;
    }

    public String getUsername_p2() {
        return username_p2;
    }

    public void setUsername_p2(String username_p2) {
        this.username_p2 = username_p2;
    }

    public @PlayerId int getResult() {
        return result;
    }

    public void setResult(@PlayerId int result) {
        this.result = result;
    }

    public @PlayerId int getTurn() {
        return turn;
    }

    public void setTurn(@PlayerId int turn) {
        this.turn = turn;
    }

    public boolean isUserTurn(String username) {
        if (turn == NO_PLAYER) {
            return false;
        }
        if (turn == FIRST_PLAYER) {
            return TextUtils.equals(username, username_p1);
        }
        if (turn == SECOND_PLAYER) {
            return TextUtils.equals(username, username_p2);
        }
        return false;
    }

    public void doTurn(int x, int y, String username) {
        @Game.PlayerId int playerId = getUserNumber(username);
        board.get(x).set(y, playerId);
        if (gameIsOver(x, y, playerId)) {
            setTurn(Game.NO_PLAYER);
        } else {
            setTurn(playerId == Game.FIRST_PLAYER ? Game.SECOND_PLAYER : Game.FIRST_PLAYER);
        }
    }

    private @PlayerId int getUserNumber(String username) {
        if (TextUtils.equals(username, username_p1)) {
            return FIRST_PLAYER;
        }
        if (TextUtils.equals(username, username_p2)) {
            return SECOND_PLAYER;
        }
        return NO_PLAYER;
    }

    private boolean gameIsOver(int x, int y, @PlayerId int playerToCheck) {
        // Check this row
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += board.get(i).get(y) == playerToCheck ? 1 : 0;
        }
        if (sum == 3) {
            setResult(playerToCheck);
            return true;
        }

        // Check this column
        sum = 0;
        for (int j = 0; j < 3; j++) {
            sum += board.get(x).get(j) == playerToCheck ? 1 : 0;
        }
        if (sum == 3) {
            setResult(playerToCheck);
            return true;
        }

        // Check diagonals if necessary
        if (x == 0 && y == 0 || x == 1 && y == 1 || x == 2 && y == 2) {
            if (board.get(0).get(0) == playerToCheck &&
                    board.get(1).get(1) == playerToCheck &&
                    board.get(2).get(2) == playerToCheck) {
                setResult(playerToCheck);
                return true;
            }
        }
        if (x == 2 && y == 0 || x == 1 && y == 1 || x == 0 && y == 2) {
            if (board.get(2).get(0) == playerToCheck &&
                    board.get(1).get(1) == playerToCheck &&
                    board.get(0).get(2) == playerToCheck) {
                setResult(playerToCheck);
                return true;
            }
        }

        // Finally, is it full?
        boolean hasSpacesLeft = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.get(i).get(j) == NO_PLAYER) {
                    hasSpacesLeft = true;
                    break;
                }
            }
        }
        if (hasSpacesLeft) {
            return false;
        } else {
            // No spots left, the game is over.
            setResult(Game.NO_PLAYER);
            return true;
        }
    }
}
