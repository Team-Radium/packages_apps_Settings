/*
 * Copyright (C) 2015 The Team-Radium's RandomStuff
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

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.util.Log;

import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContentResolver;
import android.preference.SwitchPreference;
import com.android.settings.util.AbstractAsyncSuCMDProcessor;
import com.android.settings.util.CMDProcessor;
import com.android.settings.util.Helpers;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomStuff extends SettingsPreferenceFragment implements
	Preference.OnPreferenceChangeListener{

private static final String KEY_UPDATE_SETTINGS = "update_settings";
private static final String KEY_UPDATE_SETTINGS_PACKAGE_NAME = "com.radium.ota";
private static final String SELINUX = "selinux";
private static final String MSIM = "msim";

private SwitchPreference mSelinux;
private SwitchPreference mMsimSwitch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.random_stuff);
        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getContentResolver();
        mContext = getActivity();

	String mMsimCurrentValue=CMDProcessor.runSuCommand("getprop persist.radio.multisim.config").getStdout();
	if(mMsimCurrentValue.equals("")||mMsimCurrentValue.equals(null))
	{
		prefSet.removePreference(mMsimSwitch);
	}

        //SELinux
        mSelinux = (SwitchPreference) findPreference(SELINUX);
        mSelinux.setOnPreferenceChangeListener(this);

        if (CMDProcessor.runSuCommand("getenforce").getStdout().contains("Enforcing")) {
            mSelinux.setChecked(true);
            mSelinux.setSummary(R.string.selinux_enforcing_title);
        } else {
            mSelinux.setChecked(false);
            mSelinux.setSummary(R.string.selinux_permissive_title);
        }

	//MSIM Switch

	mMsimSwitch=(SwitchPreference)findPreference(MSIM);
	mMsimSwitch.setOnPreferenceChangeListener(this);
	if(mMsimCurrentValue.equals("dsds")||mMsimCurrentValue.equals("dsda"))
	{
		mMsimSwitch.setChecked(true);
		mMsimSwitch.setSummary(R.string.msim_enabled_title);
	}
	else
	{
		mMsimSwitch.setChecked(false);
		mMsimSwitch.setSummary(R.string.msim_disabled_title);
	}
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
          if (preference == mSelinux) {
            if (newValue.toString().equals("true")) {
                CMDProcessor.runSuCommand("setenforce 1");
                mSelinux.setSummary(R.string.selinux_enforcing_title);
            } else if (newValue.toString().equals("false")) {
                CMDProcessor.runSuCommand("setenforce 0");
                mSelinux.setSummary(R.string.selinux_permissive_title);
            }
            return true;
          }
	  if (preference == mMsimSwitch)
	  {
		if(newValue.toString().equals("true"))
		{
			CMDProcessor.runSuCommand("setprop persist.radio.multisim.config dsds");
			mMsimSwitch.setSummary(R.string.msim_enabled_title);
		}
		else if(newValue.toString().equals("false"))
		{
			CMDProcessor.runSuCommand("setprop persist.radio.multisim.config none");
			mMsimSwitch.setSummary(R.string.msim_disabled_title);
		}
		return true;
	  }
         return false;
      }
}
