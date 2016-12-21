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

package jterm.katydek.scarne;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String SCORE_DESCRIPTION = "Your score: %d Computer score: %d";
    private static final String YOUR_TURN_DESCRIPTION = " your turn score: %d";
    private static final String COMPUTER_TURN_DESCRIPTION = " computer turn score: %d";

    // The user's overall score state
    private int mUserScore;

    // The user's turn score
    private int mUserTurnScore;

    // The computer's overall score
    private int mCompScore;

    // The computer's turn score
    private int mCompTurnScore;

    private Random mRandom;
    private Handler mHandler;
    private Runnable mRunnable;

    private boolean mIsUsersTurn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.reset_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        findViewById(R.id.hold_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsUsersTurn) {
                    hold();
                }
            }
        });
        findViewById(R.id.roll_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsUsersTurn) {
                    roll();
                }
            }
        });

        mRandom = new Random();
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                // Do the computer's roll.
                roll();

                // See if it is still our turn, and if the score is low enough to keep rolling.
                if (mCompTurnScore < 20 && !mIsUsersTurn) {
                    doComputerTurn();
                } else if (!mIsUsersTurn) {
                    // Go ahead and stop -- it is the computer's turn, but it is about 20 points.
                    hold();
                }
            }
        };
        reset();
    }

    private void reset() {
        mUserScore = 0;
        mUserTurnScore = 0;
        mCompScore = 0;
        mCompTurnScore = 0;
        if (!mIsUsersTurn) {
            switchTurns();
        }
        updateText();
    }

    private void roll() {
        int value = mRandom.nextInt(6);
        updateDiceImage(value);
        doRollLogic(value + 1);
        updateText();
        // Here we could add logic to make one player or the other win!
    }

    /**
     * Saves the turn score into the total score, and switches players.
     */
    private void hold() {
        if (mIsUsersTurn) {
            mUserScore += mUserTurnScore;
            mUserTurnScore = 0;
        } else {
            mCompScore += mCompTurnScore;
            mCompTurnScore = 0;
        }

        // Switch players turns
        switchTurns();
        updateText();
    }

    /**
     * Logic for one roll. If the value is 0, switches players.
     * @param value
     */
    private void doRollLogic(int value) {
        if (value == 1) {
            mUserTurnScore = 0;
            mCompTurnScore = 0;
            hold();
            return;
        }
        if (mIsUsersTurn) {
            mUserTurnScore += value;
        } else {
            mCompTurnScore += value;
        }
    }

    /**
     * Switches whose turn it is and updates the UI.
     */
    private void switchTurns() {
        mIsUsersTurn = !mIsUsersTurn;
        findViewById(R.id.roll_btn).setEnabled(mIsUsersTurn);
        findViewById(R.id.hold_btn).setEnabled(mIsUsersTurn);
        if (!mIsUsersTurn) {
            doComputerTurn();
        }
    }

    private void doComputerTurn() {
        mHandler.postDelayed(mRunnable, 500);
    }

    private void updateText() {
        String text = String.format(SCORE_DESCRIPTION, mUserScore, mCompScore);
        if (mIsUsersTurn) {
            text += String.format(YOUR_TURN_DESCRIPTION, mUserTurnScore);
        } else {
            text += String.format(COMPUTER_TURN_DESCRIPTION, mCompTurnScore);
        }
        ((TextView) findViewById(R.id.score_description)).setText(text);
    }

    private void updateDiceImage(int id) {
        int drawable;
        switch (id) {
            case 0:
                drawable = R.drawable.dice1;
                break;
            case 1:
                drawable = R.drawable.dice2;
                break;
            case 2:
                drawable = R.drawable.dice3;
                break;
            case 3:
                drawable = R.drawable.dice4;
                break;
            case 4:
                drawable = R.drawable.dice5;
                break;
            default:
                drawable = R.drawable.dice6;
        }
        ((ImageView) findViewById(R.id.dice_image)).setImageDrawable(
                getResources().getDrawable(drawable));
    }
}
