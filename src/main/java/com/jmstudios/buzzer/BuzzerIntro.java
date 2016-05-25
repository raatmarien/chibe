// BuzzerIntro.java --- Intro/help activity using the AppIntro
// library: https://github.com/PaoloRotolo/AppIntro

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

package com.jmstudios.buzzer;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import com.jmstudios.buzzer.R;

public class BuzzerIntro extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(AppIntroFragment.newInstance
                 ("Hello", "Halloah",
                  R.drawable.alarm_clock,
                  0xFFB71C1C));
        addSlide(AppIntroFragment.newInstance
                 ("Hello", "Halloah",
                  R.drawable.alarm_clock,
                  0xFF004D40));
        addSlide(AppIntroFragment.newInstance
                 ("Hello", "Halloah",
                  R.drawable.alarm_clock,
                  0xFF3E2723));
        addSlide(AppIntroFragment.newInstance
                 ("Hello", "Halloah",
                  R.drawable.alarm_clock,
                  0xFF263238));
    }

    @Override
    public void onSkipPressed() {
    // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed() {
    // Do something when users tap on Done button.
    }

    public void onSlideChanged() {
    // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
    // Do something when users tap on Next button.
    }

}
