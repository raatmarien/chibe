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

public class SettingsModel {
    private static final String TAG = "SettingsModel";
    private static final boolean DEBUG = true;

    public static final String mSleepStartPrefKey = "pref_key_sleep_start";
    public static final String mSleepEndPrefKey = "pref_key_sleep_end";
    public static final String mBuzzTimePrefKey = "pref_key_buzz_time";
    public static final String mBuzzServiceOnPrefKey = "pref_key_buzz_service_on";
    public static final String mIntroShowPrefKey = "pref_key_intro_shown";
    public static final String mBuzzPatternPrefKey = "pref_key_buzz_pattern";
    public static final String mDarkThemePrefKey = "pref_key_dark_theme";

    private SharedPreferences mSharedPrefs;

    public SettingsModel(SharedPreferences sharedPreferences) {
        mSharedPrefs = sharedPreferences;
    }

    // Creates a new SettingsModel using the default SharedPreferences
    // for the given context.
    public SettingsModel(Context context) {
        this(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPrefs;
    }

    public String getSleepStart() {
        return mSharedPrefs.getString(mSleepStartPrefKey, "22:00");
    }

    public String getSleepEnd() {
        return mSharedPrefs.getString(mSleepEndPrefKey, "09:00");
    }

    public int getBuzzTimeInMinutes() {
        return mSharedPrefs.getInt(mBuzzTimePrefKey, 30);
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

    public int getBuzzPatternIndex() {
        return Integer.parseInt
            (mSharedPrefs.getString(mBuzzPatternPrefKey, "0"));
    }

    public boolean isDarkTheme() {
        return mSharedPrefs.getBoolean(mDarkThemePrefKey, false);
    }
}
