/**
 *  Copyright 2016 Google Inc. All Rights Reserved.
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

package com.google.engedu.palindromes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Range;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private HashMap<Range, PalindromeGroup> findings = new HashMap<>();
    private PalindromeHelper mPalindromeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPalindromeHelper = new PalindromeHelper();
    }

    public boolean onFindPalindromes(View view) {
        findings.clear();
        EditText editText = (EditText) findViewById(R.id.editText);
        TextView textView = (TextView) findViewById(R.id.textView);
        String text = editText.getText().toString();
        text = text.replaceAll(" ", "");
        text = text.replaceAll("'", "");
        // This is not complete -- more special chars should be removed!
        char[] textAsChars = text.toCharArray();
        if (PalindromeHelper.isPalindrome(textAsChars, 0, text.length())) {
            textView.setText(text + " is already a palindrome!");
        } else {
            PalindromeGroup palindromes = mPalindromeHelper.breakIntoPalindromes(
                    text.toCharArray(), 0, text.length());
            textView.setText(palindromes.toString());
        }
        return true;
    }
}
