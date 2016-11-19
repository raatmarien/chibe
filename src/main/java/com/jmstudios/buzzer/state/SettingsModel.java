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
package com.jmstudios.buzzer.state;

import android.content.SharedPreferences;
import android.content.Context;
import android.preference.PreferenceManager;

import android.util.Log;

public class SettingsModel
    implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "SettingsModel";
    private static final boolean DEBUG = true;

    private static final String mSleepStartPrefKey = "pref_key_sleep_start";
    private static final String mSleepEndPrefKey = "pref_key_sleep_end";
    private static final String mBuzzTimePrefKey = "pref_key_buzz_time";
    public static final String mBuzzServiceOnPrefKey = "pref_key_buzz_service_on";
    public static final String mIntroShowPrefKey = "pref_key_intro_shown";


    private SharedPreferences mSharedPrefs;

    public SettingsModel(SharedPreferences sharedPreferences) {
        mSharedPrefs = sharedPreferences;
    }

    // Creates a new SettingsModel using the default SharedPreferences
    // for the given context.
    public SettingsModel(Context context) {
        this(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public void openSettingsChangeListener() {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        if (DEBUG) Log.d(TAG, "Opened Settings change listener");
    }

    public void closeSettingsChangeListener() {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

        if (DEBUG) Log.d(TAG, "Closed Settings change listener");
    }

    public String getSleepStart() {
        return mSharedPrefs.getString(mSleepStartPrefKey, "22:00");
    }

    public String getSleepEnd() {
        return mSharedPrefs.getString(mSleepEndPrefKey, "9:00");
    }

    public int getBuzzTimeInMinutes() {
        return Integer.parseInt
            (mSharedPrefs.getString(mBuzzTimePrefKey, "30"));
    }

    public boolean isBuzzServiceOn() {
        return mSharedPrefs.getBoolean
            (mBuzzServiceOnPrefKey, false);
    }

    public void setBuzzServiceOn(boolean serviceOn) {
        mSharedPrefs.edit()
            .putBoolean(mBuzzServiceOnPrefKey, serviceOn).commit();
    }

    public boolean isIntroShown() {
        return mSharedPrefs.getBoolean(mIntroShowPrefKey, false);
    }

    public void setIntroShown(boolean introShown) {
        mSharedPrefs.edit()
            .putBoolean(mIntroShowPrefKey, introShown).commit();
    }

    @Override
    public void onSharedPreferenceChanged
        (SharedPreferences sharedPreferences, String key) {

    }
}
