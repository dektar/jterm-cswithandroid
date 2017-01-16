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

import java.lang.annotation.Retention;
import java.util.ArrayList;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Represents a tic tac toe game
 */
public class Game {
    @Retention(SOURCE)
    @IntDef({FIRST_PLAYER, SECOND_PLAYER})
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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
