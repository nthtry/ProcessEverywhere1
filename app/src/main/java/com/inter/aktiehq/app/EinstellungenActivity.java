package com.inter.aktiehq.app;

/**
 * Created by jonas on 30.08.2016.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class EinstellungenActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noninspection deprecation
        addPreferencesFromResource(R.xml.preferences);

        //iBeacon
        Preference iBeaconPref = findPreference(getString(R.string.preference_iBeaconmodus_key));
        iBeaconPref.setOnPreferenceChangeListener(this);

        // onPreferenceChange sofort aufrufen mit der in SharedPreferences gespeicherten Aktienliste
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean iBeaconSelected = sharedPrefs.getBoolean(iBeaconPref.getKey(), true);
        onPreferenceChange(iBeaconPref, iBeaconSelected);

        //Eddystone
        Preference eddystonePref = findPreference(getString(R.string.preference_eddystonemodus_key));
        iBeaconPref.setOnPreferenceChangeListener(this);

        // onPreferenceChange sofort aufrufen mit der in SharedPreferences gespeicherten Aktienliste
        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean eddystoneSelected = sharedPrefs.getBoolean(eddystonePref.getKey(), true);
        onPreferenceChange(eddystonePref, eddystoneSelected);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        preference.setSummary(value.toString());

        return true;
    }
}
