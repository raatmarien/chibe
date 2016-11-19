// BuzzAlarmReceiver.java --- Receives alarms from the Android alarm
// manager to buzz when needed

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
package com.jmstudios.buzzer.timing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.Vibrator;

import com.jmstudios.buzzer.timing.BuzzAlarmScheduler;
import com.jmstudios.buzzer.state.SettingsModel;

public class BuzzAlarmReceiver extends BroadcastReceiver {
    private final static String TAG = "BuzzAlarmReceiver";
    private final static boolean DEBUG = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DEBUG) Log.i(TAG, "Buzz alarm received");

        buzz(context);

        // Schedule new alarm
        BuzzAlarmScheduler.rescheduleAlarm(context);
    }

    public static void buzz(Context context) {
        Vibrator vibrator = (Vibrator) context.
            getSystemService(Context.VIBRATOR_SERVICE);

        long[] vibrationPattern;

        SettingsModel settingsModel = new SettingsModel(context);
        switch (settingsModel.getBuzzPatternIndex()) {
        default:
        case 0: long[] temp0 = {0, 100, 50, 100, 200, 400, 200, 100, 50, 100};
            vibrationPattern = temp0; break;
        case 1: long[] temp1 = {0, 400, 200, 100, 50, 100, 200, 400};
            vibrationPattern = temp1; break;
        case 2: long[] temp2 = {0, 200, 400, 200};
            vibrationPattern = temp2; break;
        case 3: long[] temp3 = {0, 100, 50, 100, 50, 100, 50, 100};
            vibrationPattern = temp3; break;
        case 4: long[] temp4 = {0, 200};
            vibrationPattern = temp4; break;
        case 5: long[] temp5 = {0, 100, 50, 100};
            vibrationPattern = temp5; break;
        }
        int noRepeat = -1;
        vibrator.vibrate(vibrationPattern, noRepeat);
    }
}
