// BuzzerActivity.java --- Lets the user control the app's settings

// Copyright (C) 2016 Marien Raat <marienraat@riseup.net>

// Author: Marien Raat <marienraat@riseup.net>

// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 3
// of the License, or (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>.

package com.jmstudios.buzzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.Intent;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;

import com.jmstudios.buzzer.R;

import com.jmstudios.buzzer.BuzzerFragment;
import com.jmstudios.buzzer.BuzzerIntro;

public class BuzzerActivity extends AppCompatActivity {
    private static final String TAG = "BuzzerActivity";
    private static final boolean DEBUG = true;

    private static final String FRAGMENT_TAG_BUZZER
        = "com.jmstudios.buzzer.fragment.tag.BUZZER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buzzer_activity);

        FragmentManager fragmentManager = getFragmentManager();
        BuzzerFragment fragment;

        // If the activity is recreated, we retrieve the existing
        // fragment from the FragmentManager.
        // Otherwise we create a new fragment and attach it.
        if (savedInstanceState != null) {
            if (DEBUG) Log.i(TAG, "onCreate - Recreation" +
                             " - retrieving stored fragment");

            fragment = (BuzzerFragment) fragmentManager.findFragmentByTag
                (FRAGMENT_TAG_BUZZER);
        } else {
            if (DEBUG) Log.i(TAG, "onCreate - First creation" +
                             " - creating a new fragment");

            fragment = new BuzzerFragment();

            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_TAG_BUZZER)
                .commit();
        }

        // Show intro if the user hasn't seen it yet
        boolean introShown = PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean("pref_key_intro_shown", false);

        if (!introShown) {
            startIntro(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // We need to inflate the menu for the app bar, to display the switch
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.buzzer_activity_menu, menu);

        return true;
    }

    // This method is also used by the floating action button, through the
    // android:onClick attribute. This attribute requires a public method with a
    // View as parameter. However, this method simply ignores the View
    // parameter.
    public void startIntro(View v) {
        Intent introIntent = new Intent(this, BuzzerIntro.class);
        startActivity(introIntent);

        // Save that the intro has been shown
        SharedPreferences.Editor spEditor =
            PreferenceManager.getDefaultSharedPreferences(this).edit();
        spEditor.putBoolean("pref_key_intro_shown", true);
        spEditor.apply();
    }
}
