// SettingsModel.java --- Helper class to manage the SharedPreferences
// of Buzzer

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

import android.content.SharedPreferences;
import android.util.Log;

public class SettingsModel
    implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "SettingsModel";
    private static final boolean DEBUG = true;

    private static final String mSleepStartPrefKey = "pref_key_sleep_start";
    private static final String mSleepEndPrefKey = "pref_key_sleep_end";
    private static final String mBuzzTimePrefKey = "pref_key_buzz_time";

    private SharedPreferences mSharedPreferences;

    public SettingsModel(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    public String getSleepStart() {
        return mSharedPreferences.getString(mSleepStartPrefKey, "22:00");
    }

    public String getSleepEnd() {
        return mSharedPreferences.getString(mSleepEndPrefKey, "9:00");
    }

    public int getBuzzTimeInMinutes() {
        return Integer.parseInt
            (mSharedPreferences.getString(mBuzzTimePrefKey, "30"));
    }

    @Override
    public void onSharedPreferenceChanged
        (SharedPreferences sharedPreferences, String key) {

    }
}
