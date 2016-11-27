// VibrationAlarmSchedulerTest.java --- Tests VibrationAlarmScheduler

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
package com.jmstudios.chibe;

import org.junit.Test;
import static org.junit.Assert.*;

import com.jmstudios.chibe.timing.VibrationAlarmScheduler;

import java.util.Calendar;

public class VibrationAlarmSchedulerTest {
    // Test VibrationAlarmScheduler::parseMinutesFromTimeString
    @Test
    public void alarmScheduler_parseMinutesFromString_simple() {
        assertEquals("Parse minutes from simple time",
                     VibrationAlarmScheduler.parseMinutesFromTimeString("13:37"), 37);
    }

    @Test
    public void alarmScheduler_parseMinutesFromString_leadingZero() {
        assertEquals("Parse minutes from time with leading zero",
                     VibrationAlarmScheduler.parseMinutesFromTimeString("13:07"), 7);
    }

    @Test
    public void alarmScheduler_parseMinutesFromString_noLeadingZero() {
        assertEquals("Parse minutes from time with no leading zero",
                     VibrationAlarmScheduler.parseMinutesFromTimeString("13:7"), 7);
    }

    // Test VibrationAlarmScheduler::parseHoursFromTimeString
    @Test
    public void alarmScheduler_parseHoursFromString_simple() {
        assertEquals("Parse hours from simple time",
                     VibrationAlarmScheduler.parseHoursFromTimeString("13:37"), 13);
    }

    @Test
    public void alarmScheduler_parseHoursFromString_leadingZero() {
        assertEquals("Parse hours from time with leading zero",
                     VibrationAlarmScheduler.parseHoursFromTimeString("03:37"), 3);
    }

    @Test
    public void alarmScheduler_parseHoursFromString_noLeadingZero() {
        assertEquals("Parse hours from time without leading zero",
                     VibrationAlarmScheduler.parseHoursFromTimeString("3:37"), 3);
    }

    // Test VibrationAlarmScheduler::getCalendarFromTimeString
    @Test
    public void alarmScheduler_getCalendarFromTimeString_minuteSimple() {
        Calendar today = Calendar.getInstance();
        Calendar fromTimeString = VibrationAlarmScheduler.getCalendarFromTimeString("13:37", today);
        assertEquals(fromTimeString.get(Calendar.MINUTE), 37);
    }

    @Test
    public void alarmScheduler_getCalendarFromTimeString_hourSimple() {
        Calendar today = Calendar.getInstance();
        Calendar fromTimeString = VibrationAlarmScheduler.getCalendarFromTimeString("13:37", today);
        assertEquals(fromTimeString.get(Calendar.HOUR_OF_DAY), 13);
    }

    @Test
    public void alarmScheduler_getCalendarFromTimeString_daySimple() {
        Calendar today = Calendar.getInstance();
        Calendar fromTimeString = VibrationAlarmScheduler.getCalendarFromTimeString("13:37", today);
        assertEquals(fromTimeString.get(Calendar.DAY_OF_YEAR), today.get(Calendar.DAY_OF_YEAR));
    }

    @Test
    public void alarmScheduler_getCalendarFromTimeString_minuteMidnight() {
        Calendar today = Calendar.getInstance();
        Calendar fromTimeString = VibrationAlarmScheduler.getCalendarFromTimeString("00:00", today);
        assertEquals(fromTimeString.get(Calendar.MINUTE), 0);
    }

    @Test
    public void alarmScheduler_getCalendarFromTimeString_hourMidnight() {
        Calendar today = Calendar.getInstance();
        Calendar fromTimeString = VibrationAlarmScheduler.getCalendarFromTimeString("00:00", today);
        assertEquals(fromTimeString.get(Calendar.HOUR_OF_DAY), 0);
    }

    @Test
    public void alarmScheduler_getCalendarFromTimeString_dayMidnight() {
        Calendar today = Calendar.getInstance();
        Calendar fromTimeString = VibrationAlarmScheduler.getCalendarFromTimeString("00:00", today);
        assertEquals(fromTimeString.get(Calendar.DAY_OF_YEAR), today.get(Calendar.DAY_OF_YEAR));
    }

    // Test VibrationAlarmScheduler::getNextAlarmTime
    @Test
    public void alarmScheduler_getNextAlarmTime_duringDay() {
        Calendar day = Calendar.getInstance();
        Calendar time = VibrationAlarmScheduler.getCalendarFromTimeString("13:37", day);
        Calendar nextAlarm = VibrationAlarmScheduler.getNextAlarmTime(time, "22:00", "06:00", 5);
        assertEquals(nextAlarm.get(Calendar.MINUTE), 40);
        assertEquals(nextAlarm.get(Calendar.HOUR_OF_DAY), 13);
        assertEquals(nextAlarm.get(Calendar.DAY_OF_YEAR), day.get(Calendar.DAY_OF_YEAR));
    }

    @Test
    public void alarmScheduler_getNextAlarmTime_duringSleepEarlyMorning() {
        Calendar day = Calendar.getInstance();
        Calendar time = VibrationAlarmScheduler.getCalendarFromTimeString("04:08", day);
        Calendar nextAlarm = VibrationAlarmScheduler.getNextAlarmTime(time, "22:00", "06:00", 5);
        assertEquals(nextAlarm.get(Calendar.MINUTE), 0);
        assertEquals(nextAlarm.get(Calendar.HOUR_OF_DAY), 6);
        assertEquals(nextAlarm.get(Calendar.DAY_OF_YEAR), day.get(Calendar.DAY_OF_YEAR));
    }

    @Test
    public void alarmScheduler_getNextAlarmTime_duringSleepLateNight() {
        Calendar day = Calendar.getInstance();
        Calendar time = VibrationAlarmScheduler.getCalendarFromTimeString("23:09", day);
        Calendar nextAlarm = VibrationAlarmScheduler.getNextAlarmTime(time, "22:00", "06:00", 5);
        assertEquals(nextAlarm.get(Calendar.MINUTE), 0);
        assertEquals(nextAlarm.get(Calendar.HOUR_OF_DAY), 6);

        Calendar expectedDay = ((Calendar) day.clone());
        expectedDay.add(Calendar.DAY_OF_YEAR, 1);
        assertEquals(nextAlarm.get(Calendar.DAY_OF_YEAR), expectedDay.get(Calendar.DAY_OF_YEAR));
    }

    @Test
    public void alarmScheduler_getNextAlarmTime_longVibrationTime_duringDay() {
        Calendar day = Calendar.getInstance();
        Calendar time = VibrationAlarmScheduler.getCalendarFromTimeString("12:19", day);
        Calendar nextAlarm = VibrationAlarmScheduler.getNextAlarmTime(time, "22:00", "06:00", 120);
        assertEquals(nextAlarm.get(Calendar.MINUTE), 0);
        assertEquals(nextAlarm.get(Calendar.HOUR_OF_DAY), 14);
        assertEquals(nextAlarm.get(Calendar.DAY_OF_YEAR), day.get(Calendar.DAY_OF_YEAR));
    }

    @Test
    public void alarmScheduler_getNextAlarmTime_weirdVibrationTime_duringDay() {
        Calendar day = Calendar.getInstance();
        Calendar time = VibrationAlarmScheduler.getCalendarFromTimeString("11:39", day);
        Calendar nextAlarm = VibrationAlarmScheduler.getNextAlarmTime(time, "22:00", "06:00", 93);
        assertEquals(nextAlarm.get(Calendar.MINUTE), 12);
        assertEquals(nextAlarm.get(Calendar.HOUR_OF_DAY), 12);
        assertEquals(nextAlarm.get(Calendar.DAY_OF_YEAR), day.get(Calendar.DAY_OF_YEAR));
    }

    @Test
    public void alarmScheduler_getNextAlarmTime_noSleep_duringDay() {
        Calendar day = Calendar.getInstance();
        Calendar time = VibrationAlarmScheduler.getCalendarFromTimeString("13:37", day);
        Calendar nextAlarm = VibrationAlarmScheduler.getNextAlarmTime(time, "00:00", "00:00", 5);
        assertEquals(nextAlarm.get(Calendar.MINUTE), 40);
        assertEquals(nextAlarm.get(Calendar.HOUR_OF_DAY), 13);
        assertEquals(nextAlarm.get(Calendar.DAY_OF_YEAR), day.get(Calendar.DAY_OF_YEAR));
    }

    @Test
    public void alarmScheduler_getNextAlarmTime_noSleep_atSleepBreak() {
        Calendar day = Calendar.getInstance();
        Calendar time = VibrationAlarmScheduler.getCalendarFromTimeString("13:37", day);
        Calendar nextAlarm = VibrationAlarmScheduler.getNextAlarmTime(time, "13:37", "13:37", 5);
        assertEquals(nextAlarm.get(Calendar.MINUTE), 42);
        assertEquals(nextAlarm.get(Calendar.HOUR_OF_DAY), 13);
        assertEquals(nextAlarm.get(Calendar.DAY_OF_YEAR), day.get(Calendar.DAY_OF_YEAR));
    }

    @Test
    public void alarmScheduler_getNextAlarmTime_sleepDuringDay_inMorning() {
        Calendar day = Calendar.getInstance();
        Calendar time = VibrationAlarmScheduler.getCalendarFromTimeString("03:37", day);
        Calendar nextAlarm = VibrationAlarmScheduler.getNextAlarmTime(time, "09:00", "17:00", 5);
        assertEquals(nextAlarm.get(Calendar.MINUTE), 40);
        assertEquals(nextAlarm.get(Calendar.HOUR_OF_DAY), 3);
        assertEquals(nextAlarm.get(Calendar.DAY_OF_YEAR), day.get(Calendar.DAY_OF_YEAR));
    }

    @Test
    public void alarmScheduler_getNextAlarmTime_longSleep() {
        Calendar day = Calendar.getInstance();
        Calendar time = VibrationAlarmScheduler.getCalendarFromTimeString("13:37", day);
        Calendar nextAlarm = VibrationAlarmScheduler.getNextAlarmTime(time, "00:01", "00:00", 5);
        assertEquals(nextAlarm.get(Calendar.MINUTE), 0);
        assertEquals(nextAlarm.get(Calendar.HOUR_OF_DAY), 0);

        Calendar expectedDay = ((Calendar) day.clone());
        expectedDay.add(Calendar.DAY_OF_YEAR, 1);
        assertEquals(nextAlarm.get(Calendar.DAY_OF_YEAR), expectedDay.get(Calendar.DAY_OF_YEAR));
    }
}
