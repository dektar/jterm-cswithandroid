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

import java.util.ArrayList;
import java.util.Collection;

public class User {
    private String username;
    private String email;
    private int wins;
    private ArrayList<String> currentGames;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        wins = 0;
        currentGames = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public ArrayList<String> getCurrentGames() {
        return currentGames;
    }

    public void setCurrentGames(ArrayList<String> currentGames) {
        this.currentGames = currentGames;
    }
}
