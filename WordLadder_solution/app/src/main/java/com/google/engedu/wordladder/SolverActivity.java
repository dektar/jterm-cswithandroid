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
package com.google.engedu.wordladder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.engedu.worldladder.R;

import java.util.ArrayList;
import java.util.List;

public class SolverActivity extends AppCompatActivity {
    public static final String TAG = "SolverActivity";
    public static final String EXTRA_WORDS = "wordsExtra";
    private static final String KEY_WORDS = "keyWords";
    private static final String KEY_USER_WORDS = "keyUserWords";
    private static final String KEY_STATUS = "keyStatus";

    private String[] mWords;
    private List<EditText> mEditTexts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_solver);

        ArrayList<String> editTextStrings = null;
        if (savedInstanceState == null) {
            mWords = getIntent().getStringArrayExtra(EXTRA_WORDS);
            if (mWords == null || mWords.length == 0) {
                // Finish early! There are no words.
                finish();
            }
        } else {
            editTextStrings = savedInstanceState.getStringArrayList(KEY_USER_WORDS);
            mWords = savedInstanceState.getStringArray(KEY_WORDS);
            ((TextView) findViewById(R.id.status)).setText(savedInstanceState.getString(KEY_STATUS));
        }
        LinearLayout editTextHolder = (LinearLayout) findViewById(R.id.input_holder);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        // Create EditTexts to fill the rest of the spaces
        for (int i = 1; i < mWords.length - 1; i++) {
            EditText child = (EditText) layoutInflater.inflate(R.layout.solver_edit_text, null);
            editTextHolder.addView(child);
            if (editTextStrings != null) {
                child.setText(editTextStrings.get(i - 1));
            }
            mEditTexts.add(child);
        }
        ((TextView) findViewById(R.id.startTextView)).setText(mWords[0]);
        ((TextView) findViewById(R.id.endTextView)).setText(mWords[mWords.length - 1]);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArray(KEY_WORDS, mWords);
        ArrayList<String> editTextStrings = new ArrayList<>();
        for (EditText editText : mEditTexts) {
            editTextStrings.add(editText.getText().toString());
        }
        outState.putStringArrayList(KEY_USER_WORDS, editTextStrings);
        outState.putString(KEY_STATUS, ((TextView) findViewById(R.id.status)).getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void onSolve(View view) {
        boolean hasError = false;
        for (int i = 1; i < mWords.length - 1; i++) {
            EditText next = mEditTexts.get(i - 1);
            if (!TextUtils.equals(next.getText().toString().toLowerCase(), mWords[i])) {
                // Not successful! Highlight that this was wrong
                next.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                next.setText(mWords[i]);
                hasError = true;
            } else {
                next.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }
        }
        ((TextView) findViewById(R.id.status)).setText(hasError ? "Not quite..." : "Great work!");
    }
}
