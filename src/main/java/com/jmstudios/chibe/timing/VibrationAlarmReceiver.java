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
import android.os.Build;
import android.util.Log;
import android.os.Vibrator;
import android.app.NotificationManager;
import android.annotation.TargetApi;
import android.telephony.TelephonyManager;

import com.jmstudios.chibe.ForegroundService;
import com.jmstudios.chibe.state.SettingsModel;

public class VibrationAlarmReceiver extends BroadcastReceiver {
    private final static String TAG = "VibrationAlarmReceiver";
    private final static boolean DEBUG = true;

    public final static String HOUR_REPEAT_COUNT_EXTRA
        = "com.jmstudios.chibe.timing.hour_repeat_count";

    private final static int SHORT_BUZZ = 100,
        LONG_BUZZ = 400,
        SHORT_PAUSE = 50,
        LONG_PAUSE = 200,
        PATTERN_PAUSE = 800;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DEBUG) {
            Log.i(TAG, "Vibration alarm received");
            Log.i(TAG, String.format
                  ("Ammount (-1 is no extra): %d | Repeat enabled: %b",
                   intent.getIntExtra(HOUR_REPEAT_COUNT_EXTRA, -1),
                   (new SettingsModel(context)).isHourRepeatEnabled()));
        }

        SettingsModel settingsModel = new SettingsModel(context);

        int ammHourPattern =
            settingsModel.isHourRepeatEnabled() ?
            intent.getIntExtra(HOUR_REPEAT_COUNT_EXTRA, 0) : 0;

        if (shouldVibrate(settingsModel, context)) {
            vibrate(context, 1, ammHourPattern);
        }

        // Schedule new alarm
        VibrationAlarmScheduler.rescheduleAlarm(context);
    }

    private boolean shouldVibrate(SettingsModel settingsModel,
                                  Context context) {
        boolean vibrate = true;

        if ( (!settingsModel.shouldVibrateDuringDnd() && isDndEnabled(context)) ||
             (!settingsModel.shouldVibrateDuringCalls() && isCallActive(context))){
            vibrate = false;
        }

        return vibrate;
    }

    @TargetApi(23)
    private boolean isDndEnabled(Context context) {
        boolean dndIsEnabled = false;
        boolean dndIsSupported = android.os.Build.VERSION.SDK_INT >= 23;

        if(dndIsSupported){
            NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager == null) return false;

            int currentFilter =
                notificationManager.getCurrentInterruptionFilter();

            // All other values indicate an active Dnd filter. See:
            // https://developer.android.com/reference/android/app/NotificationManager.html#INTERRUPTION_FILTER_ALARMS
            dndIsEnabled = currentFilter !=
                NotificationManager.INTERRUPTION_FILTER_ALL &&
                currentFilter !=
                NotificationManager.INTERRUPTION_FILTER_UNKNOWN;

            if (DEBUG) Log.i(TAG, "Dnd is " +
                             (dndIsEnabled ? "enabled" : "disabled"));
            if (DEBUG) Log.i(TAG, String.format
                             ("Filter number is %d.", currentFilter));
        }

        return dndIsEnabled;
    }

    private boolean isCallActive(Context context){
        boolean activeCall = true;
        TelephonyManager telephonyManager = (TelephonyManager)
            context.getSystemService(Context.TELEPHONY_SERVICE);

        if(telephonyManager.getCallState() == telephonyManager.CALL_STATE_IDLE){
            activeCall = false;
        }

        return activeCall;
    }

    // Vibrates the normal pattern `ammNormalPattern` times and the
    // hour pattern `ammHourPattern` times.
    public static void vibrate
        (Context context, int ammNormalPattern, int ammHourPattern) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent (context, ForegroundService.class);
            context.startForegroundService (intent);
        }

        Vibrator vibrator = (Vibrator) context.
            getSystemService(Context.VIBRATOR_SERVICE);

        long[] vibrationPattern = getVibrationPattern
            (context, ammNormalPattern, ammHourPattern);

        int noRepeat = -1;
        vibrator.vibrate(vibrationPattern, noRepeat);
    }

    private static long[] getVibrationPattern
        (Context context, int ammNormalPattern, int ammHourPattern) {
        SettingsModel settingsModel = new SettingsModel(context);

        String normal = settingsModel.getVibrationPattern(),
            hour = settingsModel.getHourVibrationPattern();

        long[] normalPattern = getPatternFromString(normal),
            hourPattern = getPatternFromString(hour);

        long[] vibrationPattern = new long
            [2 * (ammNormalPattern * normal.length()
                  + ammHourPattern * hour.length()
                  // For the pauses between the patterns
                  + ammNormalPattern + ammHourPattern)];

        for (int i = 0; i < ammNormalPattern; i++) {
            int base = 2 * i * normal.length() + 2 * i;
            for (int j = 0; j < 2 * normal.length(); j++)
                vibrationPattern[base + j] = normalPattern[j];

            // Add a pause between the patterns
            vibrationPattern[base + 2 * normal.length() + 0]
                = PATTERN_PAUSE;
            vibrationPattern[base + 2 * normal.length() + 1]= 0;
        }

        for (int i = 0; i < ammHourPattern; i++) {
            int base = 2 * ammNormalPattern * normal.length() +
                2 * ammNormalPattern +
                2 * i * hour.length() + 2 * i;
            for (int j = 0; j < 2 * hour.length(); j++)
                vibrationPattern[base + j] = hourPattern[j];

            // Add a pause between the patterns
            vibrationPattern[base + 2 * hour.length() + 0]
                = PATTERN_PAUSE;
            vibrationPattern[base + 2 * hour.length() + 1] = 0;
        }

        return vibrationPattern;
    }

    private static long[] getPatternFromString(String pattern) {
        long[] vibrationPattern = new long[2 * pattern.length()];

        vibrationPattern[0] = 0;
        vibrationPattern[1] = pattern.charAt(0) == '.' ?
            SHORT_BUZZ : LONG_BUZZ;
        for (int i = 1; i < pattern.length(); i++) {
            vibrationPattern[2 * i] =
                pattern.charAt(i - 1) == '_' || pattern.charAt(i) == '_' ?
                LONG_PAUSE : SHORT_PAUSE;
            vibrationPattern[2 * i + 1] = pattern.charAt(i) == '.' ?
                SHORT_BUZZ : LONG_BUZZ;
        }

        return vibrationPattern;
    }
}
