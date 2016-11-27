// ChibeFragment.java --- PreferenceFragment for ChibeActivity

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

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.Preference;
import android.util.Log;

import com.jmstudios.chibe.timing.VibrationAlarmReceiver;

public class ChibeFragment extends PreferenceFragment {
    private static final String TAG = "ChibeFragment";
    private static final boolean DEBUG = true;

    // Required for recreation
    public ChibeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        // Set the OnClickListener for the 'Test vibrate' preference
        Preference testVibration = findPreference("pref_key_test_vibration");
        testVibration.setOnPreferenceClickListener
            (new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick
                        (Preference pref) {
                        vibrateOnce();
                        return true;
                    }
                });

        Preference darkTheme = findPreference("pref_key_dark_theme");
        // To change the theme, the activity has to be recreated
        darkTheme.setOnPreferenceChangeListener
            (new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange
                        (Preference pref, Object newValue) {
                        getActivity().recreate();
                        return true;
                    }
                });
    }

    public void vibrateOnce() {
        if (DEBUG) Log.i(TAG, "Starting a test vibrate");
        VibrationAlarmReceiver.vibrate(getActivity());
    }
}
