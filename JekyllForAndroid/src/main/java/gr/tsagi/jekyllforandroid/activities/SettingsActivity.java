package gr.tsagi.jekyllforandroid.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import gr.tsagi.jekyllforandroid.R;

/**
 * Created by tsagi on 9/10/13.
 */
public class SettingsActivity extends PreferenceFragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.preferences);

    }


}