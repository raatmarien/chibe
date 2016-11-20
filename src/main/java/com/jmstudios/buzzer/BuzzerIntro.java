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
import android.content.res.Resources;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import com.jmstudios.buzzer.R;

public class BuzzerIntro extends AppIntro {
    private Resources resources;

    @Override
    public void init(Bundle savedInstanceState) {
        resources = this.getResources();

        addSlide(AppIntroFragment.newInstance
                 (resources.getString(R.string.intro_slide_1_title),
                  resources.getString(R.string.intro_slide_1_text),
                  R.drawable.alarm_clock,
                  0xFF3f51b5));
        addSlide(AppIntroFragment.newInstance
                 (resources.getString(R.string.intro_slide_2_title),
                  resources.getString(R.string.intro_slide_2_text),
                  R.drawable.intro_slide_2,
                  0xFFf44336));
        addSlide(AppIntroFragment.newInstance
                 (resources.getString(R.string.intro_slide_3_title),
                  resources.getString(R.string.intro_slide_3_text),
                  R.drawable.intro_slide_3,
                  0xFF263238));
        addSlide(AppIntroFragment.newInstance
                 (resources.getString(R.string.intro_slide_4_title),
                  resources.getString(R.string.intro_slide_4_text),
                  R.drawable.intro_slide_4,
                  0xFF9c27b0));
        addSlide(AppIntroFragment.newInstance
                 (resources.getString(R.string.intro_slide_5_title),
                  resources.getString(R.string.intro_slide_5_text),
                  R.drawable.intro_slide_5,
                  0xFFffb300));
    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
        finish();
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

}
