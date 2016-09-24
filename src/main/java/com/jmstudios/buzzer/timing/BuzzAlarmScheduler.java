// BuzzAlarmScheduler.java --- Determines the next buzz time and
// schedules new buzz alarms using the Android alarm manager.

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

import android.content.Context;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;

import java.util.Calendar;
import java.util.TimeZone;

import com.jmstudios.buzzer.timing.BuzzAlarmReceiver;
import com.jmstudios.buzzer.SettingsModel;

public class BuzzAlarmScheduler {
    private static String TAG = "BuzzAlarmScheduler";
    private static boolean DEBUG = true;

    // Removes all old alarms before scheduling a new one
    public static void rescheduleAlarm(Context context) {
        cancelAlarms(context);

        scheduleAlarm(context);
    }

    public static void cancelAlarms(Context context) {
        AlarmManager alarmManager = (AlarmManager)
            context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getBuzzPendingIntent(context));
    }

    // This method checks what android version is used and uses
    // methods accordingly.
    @SuppressLint("NewApi")
    public static void scheduleAlarm(Context context) {
        SettingsModel settingsModel = new SettingsModel
            (PreferenceManager.getDefaultSharedPreferences(context));

        Calendar alarmTime = getNextAlarmTime
            (Calendar.getInstance(),
             settingsModel.getSleepStart(),
             settingsModel.getSleepEnd(),
             settingsModel.getBuzzTimeInMinutes());

        // The AlarmManager accepts the time in UTC
        alarmTime.setTimeZone(TimeZone.getTimeZone("UTC"));

        AlarmManager alarmManager = (AlarmManager)
            context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent buzzPendingIntent = getBuzzPendingIntent(context);

        // In API 19 AlarmManager::set was made inexact and
        // AlarmManager::setExact was introduced, since we always want
        // to buzz at exactly the right time, we use setExact on API
        // 19+.
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC,
                                  alarmTime.getTimeInMillis(),
                                  buzzPendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC, alarmTime.getTimeInMillis(),
                             buzzPendingIntent);
        }
    }

    public static PendingIntent getBuzzPendingIntent(Context context) {
        Intent buzzIntent = new Intent(context, BuzzAlarmReceiver.class);

        // We don't need a specific requestcode or any flags.
        int requestCode = 0, flag = 0;
        return PendingIntent.getBroadcast
            (context, requestCode, buzzIntent, flag);
    }

    // Buzzer won't schedules buzzes during sleep (between sleepStart
    // and sleepEnd).
    // Buzz times can be any whole number of minutes, however, if the
    // buzz time is longer than the time from sleepEnd to sleepStart,
    // it will just be called once every day, at sleepEnd.
    public static Calendar getNextAlarmTime
        (Calendar now, String sleepStart, String sleepEnd,
         int buzzTimeMinutes) {
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

        // Then we add the buzz time to the nextAlarm Calendar, until
        // nextAlarm is after now. If no time is found after now and
        // before sleepStart, we schedule the next alarm for the next
        // sleepEnd.
        while (!nextAlarm.after(now)) {
            if (nextAlarm.after(nextSleepStart) ||
                // If the last sleepEnd is before the lastSleepStart,
                // that means that we are in the sleep period and we
                // should retun the nextSleepStart.
                lastSleepEnd.before(lastSleepStart))
                return nextSleepEnd;

            nextAlarm.add(Calendar.MINUTE, buzzTimeMinutes);
        }

        return nextAlarm;
    }

    /* time     should be a 24-hour formatted string of the form "HH:MM"
     * day      should be a Calendar with the day that will be
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
