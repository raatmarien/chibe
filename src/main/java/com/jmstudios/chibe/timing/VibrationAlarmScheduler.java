// VibrationAlarmScheduler.java --- Determines the next vibration time and
// schedules new vibrate alarms using the Android alarm manager.

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

import android.content.Context;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Calendar;

import com.jmstudios.chibe.timing.VibrationAlarmReceiver;
import com.jmstudios.chibe.state.SettingsModel;

public class VibrationAlarmScheduler {
    private static String TAG = "VibrationAlarmScheduler";
    private static boolean DEBUG = true;

    // Cancels all alarms if the service is off, reschedules the alarm
    // if the service is on.
    public static void updateAlarms(Context context) {
        SettingsModel settingsModel = new SettingsModel(context);
        if (settingsModel.isVibrationServiceOn())
            rescheduleAlarm(context);
        else
            cancelAlarms(context);
    }

    // Removes all old alarms before scheduling a new one
    public static void rescheduleAlarm(Context context) {
        cancelAlarms(context);

        scheduleAlarm(context);
    }

    public static void cancelAlarms(Context context) {
        AlarmManager alarmManager = (AlarmManager)
            context.getSystemService(Context.ALARM_SERVICE);

        // It doesn't matter what time we pass to
        // getVibrationPendingIntent, since the extras aren't
        // compared. See:
        // https://developer.android.com/reference/android/app/AlarmManager.html#cancel(android.app.PendingIntent)
        alarmManager.cancel(getVibrationPendingIntent
                            (context, Calendar.getInstance()));
    }

    // This method checks what android version is used and uses
    // methods accordingly.
    @SuppressLint("NewApi")
    public static void scheduleAlarm(Context context) {
        SettingsModel settingsModel = new SettingsModel(context);

        Calendar alarmTime = getNextAlarmTime
            (Calendar.getInstance(),
             settingsModel.getSleepStart(),
             settingsModel.getSleepEnd(),
             settingsModel.getVibrationTimeInMinutes());

        AlarmManager alarmManager = (AlarmManager)
            context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent vibrationPendingIntent =
            getVibrationPendingIntent(context, alarmTime);

        // In API 19 AlarmManager::set was made inexact and
        // AlarmManager::setExact was introduced, since we always want
        // to vibrate at exactly the right time, we use setExact on API
        // 19+.
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC,
                                  alarmTime.getTimeInMillis(),
                                  vibrationPendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC, alarmTime.getTimeInMillis(),
                             vibrationPendingIntent);
        }
    }

    public static PendingIntent getVibrationPendingIntent
        (Context context, Calendar time) {
        Intent vibrationIntent = new Intent(context, VibrationAlarmReceiver.class);

        // Save in the intent how many times the 'hour repeat pattern'
        // should be repeated, if it should be repeated at all.
        if (time.get(Calendar.MINUTE) == 0) {
            int hour = time.get(Calendar.HOUR);
            // The hour field of the Calendar is 0 for 12:00 and
            // 24:00, but we wan't to repeat 12 times for those.
            int hourRepeatCount = hour == 0 ? 12 : hour;

            vibrationIntent.putExtra
                (VibrationAlarmReceiver.HOUR_REPEAT_COUNT_EXTRA,
                 hour);

            if (DEBUG) Log.i(TAG, String.format
                             ("Added an hour repeat extra of %d to an PendingIntent",
                              hourRepeatCount));
        }

        // We don't need a specific requestcode or any flags.
        int requestCode = 0, flag = 0;
        return PendingIntent.getBroadcast
            (context, requestCode, vibrationIntent, flag);
    }

    // Chibe won't schedules vibrations during sleep (between sleepStart
    // and sleepEnd).
    // Vibration times can be any whole number of minutes, however, if the
    // vibrate time is longer than the time from sleepEnd to sleepStart,
    // it will just be called once every day, at sleepEnd.
    public static Calendar getNextAlarmTime
        (Calendar now, String sleepStart, String sleepEnd,
         int vibrationTimeMinutes) {
        // First we define the last sleepEnd and the next sleepStart
        // and the next sleepEnd.
        Calendar lastSleepStart = getCalendarFromTimeString(sleepStart, now);
        if (lastSleepStart.after(now)) lastSleepStart.add(Calendar.DAY_OF_YEAR, -1);

        Calendar lastSleepEnd = getCalendarFromTimeString(sleepEnd, now);
        if (lastSleepEnd.after(now)) lastSleepEnd.add(Calendar.DAY_OF_YEAR, -1);

        Calendar nextSleepStart = getCalendarFromTimeString(sleepStart, now);
        if (nextSleepStart.before(now)) nextSleepStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar nextSleepEnd = getCalendarFromTimeString(sleepEnd, now);
        if (nextSleepEnd.before(now)) nextSleepEnd.add(Calendar.DAY_OF_YEAR, 1);

        // We start off with the last sleepEnd
        Calendar nextAlarm = (Calendar) lastSleepEnd.clone();

        // Then we add the vibrate time to the nextAlarm Calendar, until
        // nextAlarm is after now. If no time is found after now and
        // before sleepStart, we schedule the next alarm for the next
        // sleepEnd.
        while (!nextAlarm.after(now)) {
            if (nextAlarm.after(nextSleepStart) ||
                // If the last sleepEnd is before the lastSleepStart,
                // that means that we are in the sleep period and we
                // should return the nextSleepStart.
                lastSleepEnd.before(lastSleepStart))
                return nextSleepEnd;

            nextAlarm.add(Calendar.MINUTE, vibrationTimeMinutes);
        }

        return nextAlarm;
    }

    /**
     * @param time     should be a 24-hour formatted string of the form "HH:MM"
     * @param day      should be a Calendar with the day that will be
     * on the returned calendar, the hour, minute, second and
     * millisecond field of this calendar is irrelevant for this
     * function.
     */
    public static Calendar getCalendarFromTimeString
        (String time, Calendar day) {
        Calendar cal = (Calendar) day.clone();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, parseMinutesFromTimeString(time));
        cal.set(Calendar.HOUR_OF_DAY, parseHoursFromTimeString(time));

        return cal;
    }

    // Accepts a 24-hour formatted string of the form "HH:MM"
    public static int parseMinutesFromTimeString(String time) {
        return Integer.parseInt(time.split(":")[1]);
    }

    // Accepts a 24-hour formatted string of the form "HH:MM"
    public static int parseHoursFromTimeString(String time) {
        return Integer.parseInt(time.split(":")[0]);
    }
}
