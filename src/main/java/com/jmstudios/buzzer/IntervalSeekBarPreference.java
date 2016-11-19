// IntervalSeekbarPreference.java --- A preference to let the user
// choose from a set of predetermined intervals using a Seekbar.

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

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jmstudios.buzzer.R;

public class IntervalSeekBarPreference extends Preference {
    private static final String TAG = "IntervalSeekBarPreference";
    private static final boolean DEBUG = false;
    public static final int DEFAULT_VALUE = 30;

    public SeekBar mIntervalSeekBar;
    private TextView mProgressView;
    private int mProgress;
    private View mView;
    private Context mContext;

    public IntervalSeekBarPreference(Context context,
                                     AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        setLayoutResource(R.layout.preference_interval_seekbar);
    }

    public void setProgress(int progress) {
        mProgress = progress;
        persistInt(mProgress);
        if (mIntervalSeekBar != null)
            mIntervalSeekBar.setProgress(mProgress);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
                                     Object defaultValue) {
        if (restorePersistedValue) {
            mProgress = getPersistedInt(DEFAULT_VALUE);
        } else {
            mProgress = (Integer) defaultValue;
            persistInt(mProgress);
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mView = view;

        mProgressView = (TextView) view.findViewById(R.id.progress_text);

        mIntervalSeekBar = (SeekBar) view.findViewById(R.id.interval_seekbar);
        mIntervalSeekBar.setMax(5);

        mIntervalSeekBar.setOnSeekBarChangeListener
            (new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged
                        (SeekBar seekBar, int progress, boolean fromUser) {
                        mProgress = getInterval(progress);
                        persistInt(mProgress);

                        mProgressView.setText(getIntervalText());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });
        mIntervalSeekBar.setProgress(getProgress(mProgress));

        mProgressView.setText(getIntervalText());
    }

    public int getInterval(int progress) {
        switch (progress) {
        case 0: return 1;
        case 1: return 5;
        case 2: return 10;
        case 3: return 15;
        default:
        case 4: return 30;
        case 5: return 60;
        }
    }

    public int getProgress(int interval) {
        switch (interval) {
        case 1: return 0;
        case 5: return 1;
        case 10: return 2;
        case 15: return 3;
        default:
        case 30: return 4;
        case 60: return 5;
        }
    }

    public String getIntervalText() {
        switch (mProgress) {
        case 1: return mContext.getResources()
                .getString(R.string.buzz_time_entries_0);
        case 5: return mContext.getResources()
                .getString(R.string.buzz_time_entries_1);
        case 10: return mContext.getResources()
                .getString(R.string.buzz_time_entries_2);
        case 15: return mContext.getResources()
                .getString(R.string.buzz_time_entries_3);
        default:
        case 30: return mContext.getResources()
                .getString(R.string.buzz_time_entries_4);
        case 60: return mContext.getResources()
                .getString(R.string.buzz_time_entries_5);
        }
    }
}
