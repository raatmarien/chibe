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
import android.preference.SwitchPreference;
import android.preference.PreferenceCategory;
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

        addAPISpecificPreferences();

        // Set the OnClickListener for the 'Test normal vibration' preference
        Preference normalTestVibration =
            findPreference("pref_key_test_normal_vibration");
        normalTestVibration.setOnPreferenceClickListener
            (new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick
                        (Preference pref) {
                        VibrationAlarmReceiver.vibrate(getActivity(), 1, 0);
                        return true;
                    }
                });

        // Set the OnClickListener for the 'Test hour vibration' preference
        Preference hourTestVibration = findPreference("pref_key_test_hour_vibration");
        hourTestVibration.setOnPreferenceClickListener
            (new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick
                        (Preference pref) {
                        VibrationAlarmReceiver.vibrate(getActivity(), 0, 1);
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

    private void addAPISpecificPreferences() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // Do not disturb mode was added in API 23
            addVibrateDuringDndPreference();
        }
    }

    private void addVibrateDuringDndPreference() {
        PreferenceCategory timingCategory =
            (PreferenceCategory) findPreference("pref_key_category_timing");
        timingCategory.addPreference(getVibrateDuringDndPreference());
    }

    private SwitchPreference getVibrateDuringDndPreference() {
        SwitchPreference vibrateDuringDnd = new SwitchPreference(getActivity());
        vibrateDuringDnd.setKey("pref_key_vibrate_during_dnd");
        vibrateDuringDnd
            .setTitle(getActivity().getString
                      (R.string.vibrate_during_dnd_preference_title));
        vibrateDuringDnd
            .setSummary(getActivity().getString
                      (R.string.vibrate_during_dnd_preference_summary));
        vibrateDuringDnd.setDefaultValue(true);
        return vibrateDuringDnd;
    }
}
