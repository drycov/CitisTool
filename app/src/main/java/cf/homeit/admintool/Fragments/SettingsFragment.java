package cf.homeit.admintool.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import cf.homeit.admintool.BuildConfig;
import cf.homeit.admintool.R;

import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences sharedPreferences;
    private int clickCount = 10;

    @SuppressLint("InflateParams")
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings_fragment);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());

//        onSharedPreferenceChanged(sharedPreferences, getString(R.string.ps_sw_vibro));

        Preference myPref = findPreference(getString(R.string.key_send_feedback));
        assert myPref != null;
        myPref.setOnPreferenceClickListener(preference -> {
            sendFeedback(requireActivity());
            return true;
        });
        Preference myEasterEgg = findPreference(getString(R.string.app_version));
        assert myEasterEgg != null;
        myEasterEgg.setSummary(BuildConfig.VERSION_NAME);

        myEasterEgg.setOnPreferenceClickListener(preference -> {
            clickCount--;
            showToast(requireActivity().getApplicationContext(), getString(R.string.creators_easter_egg) + "  " + clickCount);
            if (clickCount <= 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_easter_egg, null))
                        .setPositiveButton("ok", (dialog, id) -> dialog.cancel())
                        .setNegativeButton("cancel", (dialog, id) -> dialog.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


    }

    /**
     * Email client intent to send support mail
     * Appends the necessary device information to email body
     * useful when providing support
     */
    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android " +
                    "\n Device OS version: " + Build.VERSION.RELEASE +
                    "\n App Version: " + body +
                    "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL +
                    "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"drycov@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }
}
