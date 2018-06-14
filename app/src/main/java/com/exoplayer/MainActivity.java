/*
 * Copyright 2016 The Android Open Source Project
 * Copyright 2017 Rúben Sousa
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

package com.exoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this,SimpleVideoStream.class);
        Bundle extras = new Bundle();
        extras.putString("mediaTitle","Jurassic Park");
        extras.putString("mediaUrl","https://www2497.playercdn.net/185/0/rdRbiDjPgI_xFADS_wLpfA/1528997667/180609/U29MgJE2FRiyHeM.mp4");
        intent.putExtras(extras);
        startActivity(intent);
    }

}
