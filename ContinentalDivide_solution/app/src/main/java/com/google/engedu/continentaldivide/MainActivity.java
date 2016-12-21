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

package com.google.engedu.continentaldivide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    ContinentMap continentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout container = (LinearLayout) findViewById(R.id.vertical_layout);
        // Create the map and insert it into the view.
        continentMap = new ContinentMap(this);
        continentMap.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        container.addView(continentMap, 0);

    }

    public boolean onGenerateTerrain(View view) {
        continentMap.generateTerrain(3);
        return true;
    }

    public boolean onFindContinentalDivideDown(View view) {
        continentMap.buildDownContinentalDivide(false);
        return true;
    }

    public boolean onFindContinentalDivideUp(View view) {
        continentMap.buildUpContinentalDivide(false);
        return true;
    }

    public boolean onClearContinentalDivide(View view) {
        continentMap.clearContinentalDivide();
        return true;
    }
}
