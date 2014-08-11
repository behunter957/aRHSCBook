package com.rhsquashclub.arhscbook;

import java.util.List;

import com.rhsquashclub.arhscbook.model.RHSCPreferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Button;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }
    
    @Override
    public boolean isValidFragment(String fragmentName) {
    	return true;
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
//            PreferenceManager.setDefaultValues(getActivity(),
//                    R.xml.advanced_preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.settings);
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                String key) {
                if (key.equals("serverHostname")) {
                    Preference connectionPref = findPreference(key);
                    // Set summary to be the user-description for the selected value
                    RHSCPreferences.get().setServerName(sharedPreferences.getString("serverHostname", ""));
                }
                if (key.equals("userid")) {
                    Preference connectionPref = findPreference(key);
                    // Set summary to be the user-description for the selected value
                    RHSCPreferences.get().setServerName(sharedPreferences.getString("userid", ""));
                }
                if (key.equals("password")) {
                    Preference connectionPref = findPreference(key);
                    // Set summary to be the user-description for the selected value
                    RHSCPreferences.get().setServerName(sharedPreferences.getString("password", ""));
                }
                // TODO try again to log in
            }
        
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
//            PreferenceManager.setDefaultValues(getActivity(),
//                    R.xml.advanced_preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
