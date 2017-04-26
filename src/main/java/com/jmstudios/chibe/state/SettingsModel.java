// SettingsModel.java --- Helper class to manage the SharedPreferences
// of Chibe

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
package com.jmstudios.chibe.state;

import android.content.SharedPreferences;
import android.content.Context;
import android.preference.PreferenceManager;

public class SettingsModel {
    private static final String TAG = "SettingsModel";
    private static final boolean DEBUG = true;

    public static final String mSleepStartPrefKey = "pref_key_sleep_start";
    public static final String mSleepEndPrefKey = "pref_key_sleep_end";
    public static final String mVibrationTimePrefKey = "pref_key_vibration_time";
    public static final String mVibrationServiceOnPrefKey = "pref_key_vibration_service_on";
    public static final String mIntroShownPrefKey = "pref_key_intro_shown";
    public static final String mVibrationPatternPrefKey = "pref_key_vibration_pattern";
    public static final String mCustomVibrationPatternPrefKey = "pref_key_custom_vibration_pattern";
    public static final String mHourRepeatPrefKey = "pref_key_hour_repeat";
    public static final String mHourVibrationPatternPrefKey = "pref_key_hour_vibration_pattern";
    public static final String mCustomHourVibrationPatternPrefKey = "pref_key_custom_hour_vibration_pattern";
    public static final String mDarkThemePrefKey = "pref_key_dark_theme";
    public static final String mVibrateDuringDndPrefKey = "pref_key_vibrate_during_dnd";
    public static final String mVibrateDuringCallsPrefKey = "pref_key_vibrate_during_calls";

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

    public int getVibrationTimeInMinutes() {
        return mSharedPrefs.getInt(mVibrationTimePrefKey, 30);
    }

    public boolean isVibrationServiceOn() {
        return mSharedPrefs.getBoolean
            (mVibrationServiceOnPrefKey, false);
    }

    public void setVibrationServiceOn(boolean serviceOn) {
        mSharedPrefs.edit()
            .putBoolean(mVibrationServiceOnPrefKey, serviceOn).commit();
    }

    public boolean shouldVibrateDuringDnd() {
        return mSharedPrefs.getBoolean
            (mVibrateDuringDndPrefKey, true);
    }

    public boolean shouldVibrateDuringCalls() {
        return mSharedPrefs.getBoolean
            (mVibrateDuringCallsPrefKey, true);
    }

    public boolean isIntroShown() {
        return mSharedPrefs.getBoolean(mIntroShownPrefKey, false);
    }

    public void setIntroShown(boolean introShown) {
        mSharedPrefs.edit()
            .putBoolean(mIntroShownPrefKey, introShown).commit();
    }

    public String getVibrationPattern() {
        String chosenPattern =
            mSharedPrefs.getString(mVibrationPatternPrefKey, ".._..");
        if (chosenPattern.equals("custom")) {
            return getCustomVibrationPattern();
        } else {
            return chosenPattern;
        }
    }

    public String getCustomVibrationPattern() {
        return mSharedPrefs.
            getString(mCustomVibrationPatternPrefKey, "");
    }

    public void setCustomVibrationPattern(String vibrationPattern) {
        mSharedPrefs.edit()
            .putString(mCustomVibrationPatternPrefKey, vibrationPattern).commit();
    }

    public boolean isHourRepeatEnabled() {
        return mSharedPrefs.getBoolean(mHourRepeatPrefKey, false);
    }

    public String getHourVibrationPattern() {
        String chosenPattern =
            mSharedPrefs.getString(mHourVibrationPatternPrefKey, ".._..");
        if (chosenPattern.equals("custom")) {
            return getCustomHourVibrationPattern();
        } else {
            return chosenPattern;
        }
    }

    public String getCustomHourVibrationPattern() {
        return mSharedPrefs.
            getString(mCustomHourVibrationPatternPrefKey, "");
    }

    public void setCustomHourVibrationPattern(String vibrationPattern) {
        mSharedPrefs.edit()
            .putString(mCustomHourVibrationPatternPrefKey, vibrationPattern).commit();
    }

    public boolean isDarkTheme() {
        return mSharedPrefs.getBoolean(mDarkThemePrefKey, false);
    }
}
