/*
 * Copyright (C) 2015 The Team-Radium Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.radium;

import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContentResolver;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.preference.Preference;
import android.preference.PreferenceScreen;

public class RadiumIsotopes extends SettingsPreferenceFragment
{


    @Override
    public void onCreate(Bundle savedInstanceState)
		{
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.radium_isotopes);
        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getContentResolver();
				mContext = getActivity();
    }

}
