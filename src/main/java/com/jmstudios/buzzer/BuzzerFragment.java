// BuzzerFragment.java --- PreferenceFragment for BuzzerActivity

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

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.Preference;
import android.util.Log;
import android.preference.ListPreference;

import com.jmstudios.buzzer.timing.BuzzAlarmReceiver;

import com.jmstudios.buzzer.R;

public class BuzzerFragment extends PreferenceFragment {
    private static final String TAG = "BuzzerFragment";
    private static final boolean DEBUG = true;

    // Required for recreation
    public BuzzerFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        // Add a change listener to the buzz time list preference, to
        // change the summary to the selected value
        ListPreference buzzTime = (ListPreference)
            findPreference("pref_key_buzz_time");
        buzzTime.setOnPreferenceChangeListener
            (new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange
                        (Preference pref,
                         Object newValue) {
                        // The value of the preference has not been
                        // updated yet, so we need to find the entry
                        // corresponding with the new value manually.
                        ListPreference listPref =
                            (ListPreference) pref;
                        CharSequence entry = listPref.getEntries()
                            [listPref.findIndexOfValue
                             (newValue.toString())];
                        pref.setSummary(entry);

                        // Return true to update the value
                        return true;
                    }
                });
        // The first time we need to manually set the summary with the
        // current entry.
        buzzTime.setSummary(buzzTime.getEntry());

        // Set the OnClickListener for the 'Test buzz' preference
        Preference testBuzz = findPreference("pref_key_test_buzz");
        testBuzz.setOnPreferenceClickListener
            (new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick
                        (Preference pref) {
                        buzzOnce();
                        return true;
                    }
                });
    }

    public void buzzOnce() {
        if (DEBUG) Log.i(TAG, "Starting a test buzz");
        BuzzAlarmReceiver.buzz(getActivity());
    }
}
