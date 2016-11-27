// ChibeActivity.java --- Lets the user control the app's settings

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

package com.jmstudios.chibe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.CompoundButton;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;

import com.jmstudios.chibe.timing.VibrationAlarmScheduler;
import com.jmstudios.chibe.state.SettingsModel;

public class ChibeActivity extends AppCompatActivity
    implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "ChibeActivity";
    private static final boolean DEBUG = true;

    private static final String FRAGMENT_TAG_CHIBE
        = "com.jmstudios.chibe.fragment.tag.CHIBE";

    private Context mContext;
    private SettingsModel settingsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settingsModel = new SettingsModel(this);

        // Set the theme before calling super
        if (settingsModel.isDarkTheme())
            setTheme(R.style.AppThemeDark);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chibe_activity);

        mContext = this;

        FragmentManager fragmentManager = getFragmentManager();
        ChibeFragment fragment;

        // If the activity is recreated, we retrieve the existing
        // fragment from the FragmentManager.
        // Otherwise we create a new fragment and attach it.
        if (savedInstanceState != null) {
            if (DEBUG) Log.i(TAG, "onCreate - Recreation" +
                             " - retrieving stored fragment");

            fragment = (ChibeFragment) fragmentManager.findFragmentByTag
                (FRAGMENT_TAG_CHIBE);
        } else {
            if (DEBUG) Log.i(TAG, "onCreate - First creation" +
                             " - creating a new fragment");

            fragment = new ChibeFragment();

            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_TAG_CHIBE)
                .commit();
        }

        // Show intro if the user hasn't seen it yet
        if (!settingsModel.isIntroShown()) {
            startIntro(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // We need to inflate the menu for the app bar, to display the switch
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vibration_activity_menu, menu);

        final MenuItem item = menu.findItem(R.id.vibration_switch);
        SwitchCompat mainSwitch = (SwitchCompat) item.getActionView();
        mainSwitch.setOnCheckedChangeListener
            (new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged
                        (CompoundButton buttonView, boolean isChecked) {
                        settingsModel.setVibrationServiceOn(isChecked);
                    }
                });

        // Initialise the switch to the current vibrate service state.
        mainSwitch.setChecked(settingsModel.isVibrationServiceOn());
        VibrationAlarmScheduler.updateAlarms(this);

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        settingsModel.getSharedPreferences()
            .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        settingsModel.getSharedPreferences()
            .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    public void onSharedPreferenceChanged
        (SharedPreferences sharedPreferences, String key) {
        if (DEBUG) Log.d(TAG, "Received preference change" +
                         "for pref key: " + key);

        switch (key) {
        case SettingsModel.mSleepStartPrefKey:
        case SettingsModel.mSleepEndPrefKey:
        case SettingsModel.mVibrationTimePrefKey:
        case SettingsModel.mVibrationServiceOnPrefKey:
            VibrationAlarmScheduler.updateAlarms(this);
            break;
        }
    }

    // This method is also used by the floating action button, through the
    // android:onClick attribute. This attribute requires a public method with a
    // View as parameter. However, this method simply ignores the View
    // parameter.
    public void startIntro(View v) {
        Intent introIntent = new Intent(this, ChibeIntro.class);
        startActivity(introIntent);

        // Save that the intro has been shown
        settingsModel.setIntroShown(true);
    }
}
