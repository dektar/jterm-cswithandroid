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
package com.jterm.katie.fractals_solution;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int mDepth = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FractalView fractalView = (FractalView) findViewById(R.id.fractal_view);
        final TextView depthText = (TextView) findViewById(R.id.depth);
        depthText.setText(String.valueOf(mDepth));

        Button redrawBtn = (Button) findViewById(R.id.redraw);

        redrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFromDepth(fractalView, depthText);
            }
        });

        Button decreaseBtn = (Button) findViewById(R.id.decrease_depth);
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDepth > 0) {
                    mDepth--;
                }
                depthText.setText(String.valueOf(mDepth));
            }
        });

        Button increaseBtn = (Button) findViewById(R.id.increase_depth);
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                depthText.setText(String.valueOf(++mDepth));
            }
        });

        resetFromDepth(fractalView, depthText);
    }

    /**
     * Resets the Mandelbrot drawing from the updated depth string.
     */
    private void resetFromDepth(FractalView fractalView, TextView depthText) {
        String depthString = depthText.getText().toString();
        int depth = Integer.parseInt(depthString);
        fractalView.reset(depth);
    }
}
