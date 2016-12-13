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

        import android.content.res.AssetManager;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.text.Editable;
        import android.text.TextUtils;
        import android.text.TextWatcher;
        import android.text.method.KeyListener;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.io.InputStream;
        import java.util.Random;


public class GhostActivityEditText extends AppCompatActivity {
    private static final String TAG = "GhostActivity";

    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String KEY_USER_TURN = "keyUserTurn";
    private static final String KEY_CURRENT_WORD = "keyCurrentWord";
    private static final String KEY_SAVED_STATUS = "keySavedStatus";

    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String currentWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        if (savedInstanceState == null) {
            onStart(null);
        } else {
            userTurn = savedInstanceState.getBoolean(KEY_USER_TURN);
            currentWord = savedInstanceState.getString(KEY_CURRENT_WORD);
            String status = savedInstanceState.getString(KEY_SAVED_STATUS);
            ((EditText) findViewById(R.id.ghostText)).setText(currentWord);
            ((TextView) findViewById(R.id.gameStatus)).setText(status);
        }
        final EditText ghostText = ((EditText) findViewById(R.id.ghostText));
        ghostText.addTextChangedListener(new TextWatcher() {
            private String textBeforeChange;
            private boolean userInitiated = true;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {
                textBeforeChange = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ghostText.setSelection(editable.length());
                if (currentWord.length() == 0 && editable.length() == 0) {
                    // We are resetting the game, so we don't need to do any saving.
                    return;
                }
                if (!userInitiated) {
                    // The text watcher can get in a loop if we make changes to the editable here
                    // unless we check whether the user initiated the change or not.
                    userInitiated = true;
                    return;
                }
                if (userTurn && (editable.length() == 0 ||
                        !editable.subSequence(0, editable.length() - 1).toString()
                                .equals(textBeforeChange))) {
                    resetText(editable);
                } else if (userTurn) {
                    char last = editable.charAt(editable.length() - 1);
                    if ('A' <= last && last <= 'z') {
                        currentWord += (last + "").toLowerCase();
                        computerTurn();
                    } else {
                        resetText(editable);
                    }
                }
            }

            private void resetText(Editable editable) {
                userInitiated = false;
                editable.replace(0, editable.length(), textBeforeChange);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_USER_TURN, userTurn);
        outState.putString(KEY_CURRENT_WORD, currentWord);
        outState.putString(KEY_SAVED_STATUS,
                ((TextView) findViewById(R.id.gameStatus)).getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        currentWord = "";
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText(currentWord);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    /**
     * Handler for the "Challenge" button.
     * @param unused
     */
    public void challenge(View unused) {
        doChallenge(/* from user */ true);
    }

    /**
     * Challenges the current word. Returns true if the challenge was successful, false otherwise.
     * @param fromUser
     * @return
     */
    private boolean doChallenge(boolean fromUser) {
        TextView status = (TextView) findViewById(R.id.gameStatus);
        if (dictionary.isWord(currentWord)) {
            if (!fromUser) {
                // It is a word! The user loses.
                status.setText(String.format("%s is a word. The computer wins!", currentWord));
                return true;
            } else {
                // The computer loses, it has formed a word.
                status.setText(String.format("%s is a word. You win!", currentWord));
                return true;
            }
        } else if (TextUtils.isEmpty(dictionary.getAnyWordStartingWith(currentWord))) {
            if (!fromUser) {
                // This is not a valid word prefix. The user loses.
                status.setText(String.format("%s is an invalid prefix. The computer wins!",
                        currentWord));
                return true;
            } else {
                status.setText(String.format("%s is an invalid prefix. You win!", currentWord));
                return true;
            }
        }
        if (fromUser) {
            // We've challenged and failed. The user loses.
            status.setText(String.format("%s is a valid prefix and not a word. The computer wins!",
                    currentWord));
        }
        return false;
    }

    private void computerTurn() {
        TextView status = (TextView) findViewById(R.id.gameStatus);

        // Checks if the user's currentWord is a full word, or if it is an invalid prefix.
        boolean challengeSuccessful = doChallenge(/* from the computer */ false);
        if (challengeSuccessful) {
            return;
        }

        userTurn = false;

        // Do computer turn stuff then make it the user's turn again
        status.setText(COMPUTER_TURN);
        String next = dictionary.getGoodWordStartingWith(currentWord);
        currentWord += next.charAt(currentWord.length());
        // We can now update the text field with the computer's word.
        ((TextView) findViewById(R.id.ghostText)).setText(currentWord);

        // Keep playing...
        userTurn = true;
        status.setText(USER_TURN);
    }
}
