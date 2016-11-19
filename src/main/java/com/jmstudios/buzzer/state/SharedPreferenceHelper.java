// SharedPreferenceHelper.java --- A class to help read and write data
// to the default SharedPreference.

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

public class SharedPreferenceHelper {
    private static final String TAG = "SharedPrefHelper";
    private static final boolean DEBUG = false;

    private SharedPreferences mSharedPrefs;

    public static final String BUZZ_SERVICE_ON_PREF_KEY =
        "pref_key_buzz_service_on";
    public static final String INTRO_SHOWN_PREF_KEY =
        "pref_key_intro_shown";

    public SharedPreferenceHelper(SharedPreferences sharedPrefs) {
        this.mSharedPrefs = sharedPrefs;
    }

    // Creates a new SharedPreferenceHelper using the default
    // SharedPreferences for the given context.
    public SharedPreferenceHelper(Context context) {
        this(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public boolean isBuzzServiceOn() {
        return mSharedPrefs.getBoolean
            (BUZZ_SERVICE_ON_PREF_KEY, false);
    }

    public void setBuzzServiceOn(boolean serviceOn) {
        mSharedPrefs.edit()
            .putBoolean(BUZZ_SERVICE_ON_PREF_KEY, serviceOn).apply();
    }

    public boolean isIntroShown() {
        return mSharedPrefs.getBoolean(INTRO_SHOWN_PREF_KEY, false);
    }

    public void setIntroShown(boolean introShown) {
        mSharedPrefs.edit()
            .putBoolean(INTRO_SHOWN_PREF_KEY, introShown).apply();
    }
}
