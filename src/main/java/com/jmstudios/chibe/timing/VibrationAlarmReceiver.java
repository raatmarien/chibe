// VibrationAlarmReceiver.java --- Receives alarms from the Android alarm
// manager to vibrate when needed

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
package com.jmstudios.chibe.timing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.Vibrator;

import com.jmstudios.chibe.state.SettingsModel;

public class VibrationAlarmReceiver extends BroadcastReceiver {
    private final static String TAG = "VibrationAlarmReceiver";
    private final static boolean DEBUG = true;

    public final static String HOUR_REPEAT_COUNT_EXTRA
        = "com.jmstudios.chibe.timing.hour_repeat_count";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DEBUG) Log.i(TAG, "Vibration alarm received");

        vibrate(context);

        // Schedule new alarm
        VibrationAlarmScheduler.rescheduleAlarm(context);
    }

    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.
            getSystemService(Context.VIBRATOR_SERVICE);

        SettingsModel settingsModel = new SettingsModel(context);

        String p = settingsModel.getVibrationPattern();
        long[] vibrationPattern = new long[2 * p.length()];

        vibrationPattern[0] = 0;
        vibrationPattern[1] = p.charAt(0) == '.' ? 100 : 400;
        for (int i = 1; i < p.length(); i++) {
            vibrationPattern[2 * i] =
                p.charAt(i - 1) == '_' || p.charAt(i) == '_' ?
                200 : 50;
            vibrationPattern[2 * i + 1] = p.charAt(i) == '.' ?
                100 : 400;
        }

        int noRepeat = -1;
        vibrator.vibrate(vibrationPattern, noRepeat);
    }
}
