// VibrationPatternPreference.java --- A subclass of ListPreference that
// allows the user to set a custom vibrate pattern or one of the
// preselected vibrate patterns.

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

import android.content.Context;
import android.util.AttributeSet;
import android.preference.ListPreference;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jmstudios.chibe.state.SettingsModel;

public class VibrationPatternPreference extends ListPreference {
    private Context mContext;
    private String mCustomPattern;
    private EditText customPatternText;

    public VibrationPatternPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onSetInitialValue(boolean restoredPersistedValue,
                                     Object defaultValue) {
        super.onSetInitialValue(restoredPersistedValue, defaultValue);
        updateSummary();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult && getValue().equals("custom")) {
            mCustomPattern = "";
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View customPatternView =
                inflater.inflate(R.layout.custom_pattern_view, null);

            customPatternText = (EditText)
                customPatternView.findViewById(R.id.custom_pattern_text);
            customPatternText.setCursorVisible(true);
            customPatternText.setLongClickable(false);
            customPatternText.setClickable(false);
            customPatternText.setSelected(false);
            customPatternText.setKeyListener(null);

            ((Button) customPatternView.findViewById(R.id.button_short_vibration))
                .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCustomPattern += ".";
                            updateCustomPatternText();
                        }
                    });
            ((Button) customPatternView.findViewById(R.id.button_long_vibration))
                .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCustomPattern += "_";
                            updateCustomPatternText();
                        }
                    });
            ((Button) customPatternView.findViewById(R.id.button_delete_vibration))
                .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!mCustomPattern.isEmpty())
                                mCustomPattern =
                                    mCustomPattern.substring
                                    (0, mCustomPattern.length() - 1);
                            updateCustomPatternText();
                        }
                    });
            
            AlertDialog.Builder builder =
                new AlertDialog.Builder(mContext);

            builder.setCancelable(true);
            builder.setNegativeButton
                (R.string.cancel_dialog,
                 new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         resetValue();
                     }
                 });
            builder.setOnCancelListener
                (new DialogInterface.OnCancelListener() {
                     @Override
                     public void onCancel(DialogInterface dialog) {
                         resetValue();
                     }
                 });

            if (android.os.Build.VERSION.SDK_INT >= 17) {
                builder.setOnDismissListener
                    (new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                updateSummary();
                            }
                        });
            }

            builder.setPositiveButton
                (R.string.ok_dialog,
                 new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         if (mCustomPattern.equals("")) {
                             resetValue();
                             return;
                         } else {
                             SettingsModel settingsModel =
                                 new SettingsModel(mContext);
                             settingsModel.setCustomVibrationPattern(mCustomPattern);
                             updateSummary();
                         }
                     }
                 });

            builder.setView(customPatternView);

            builder.setTitle(R.string.vibration_pattern_preference_title);

            builder.create().show();
        }

        updateSummary();
    }

    private void resetValue() {
        setValue(".._..");
        updateSummary();
    }

    private void updateSummary() { 
        SettingsModel settingsModel = new SettingsModel(mContext);
        if (getValue().equals("custom"))
            setSummary(getEntry() + ": " +
                       getFormattedPattern(settingsModel.getVibrationPattern()));
        else
            setSummary(getEntry());
    }

    private String getFormattedPattern(String pattern) {
        String formattedPattern = "";
        for (int i = 0; i < pattern.length(); i++) {
            formattedPattern += pattern.charAt(i);
            if (pattern.charAt(i) == '_' ||
                (i < pattern.length() - 1 &&
                 pattern.charAt(i+1) == '_'))
                formattedPattern += " ";
        }
        return formattedPattern;
    }

    private void updateCustomPatternText() {
        customPatternText.setText
            (getFormattedPattern(mCustomPattern));
    }
}
