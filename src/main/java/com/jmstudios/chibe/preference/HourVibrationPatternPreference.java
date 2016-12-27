// HourVibrationPatternPreference.java ---

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
package com.jmstudios.chibe.preference;

import android.content.Context;
import android.util.AttributeSet;

import com.jmstudios.chibe.preference.VibrationPatternPreference;
import com.jmstudios.chibe.state.SettingsModel;

public class HourVibrationPatternPreference
    extends VibrationPatternPreference {
    public HourVibrationPatternPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setCustomVibrationPattern(String pattern) {
        SettingsModel settingsModel = new SettingsModel(mContext);
        settingsModel.setCustomHourVibrationPattern(pattern);
    }

    @Override
    protected String getCustomVibrationPattern() {
        SettingsModel settingsModel = new SettingsModel(mContext);
        return settingsModel.getCustomHourVibrationPattern();
    }

    @Override
    protected String getVibrationPattern() {
        SettingsModel settingsModel = new SettingsModel(mContext);
        return settingsModel.getHourVibrationPattern();
    }
}
