package com.angrycoding.carlauncher.webview;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.angrycoding.carlauncher.SensorSettings;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PreferencesInterface {

    SharedPreferences preferences;
    Context context;

    public PreferencesInterface(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @JavascriptInterface
    public void setSensorType(String area, String type) {

        Intent sensorSettings = new Intent(context, SensorSettings.class);
//        sensorSettings

        sensorSettings.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
        );


        sensorSettings.putExtra("area", area);
        sensorSettings.putExtra("type", type);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, sensorSettings, PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

//        this.context.startActivity(sensorSettings);
    }

    @JavascriptInterface
    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    @JavascriptInterface
    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor preferences = this.preferences.edit();
        preferences.putBoolean(key, value);
        preferences.commit();
    }


    @JavascriptInterface
    public void runApplication(String pkgName) {
        Intent LaunchIntent = this.context.getPackageManager().getLaunchIntentForPackage(pkgName);
        this.context.startActivity( LaunchIntent );

    }


    @JavascriptInterface
    public String getApplications() {
        final PackageManager pm = this.context.getPackageManager();
        //get a list of installed apps.
//        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);


        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities( mainIntent, 0);

        String out = "[";

        out += "{\"label\": \"BLABLA\", \"name\": \"com.angrycoding.carlauncher\", \"icon\": \"icon:///com.angrycoding.carlauncher\"},";

        for (int c = 0; c < pkgAppsList.size(); c++) try {
            String pkgName = pkgAppsList.get(c).activityInfo.packageName;
            String pkgIcon = "icon:///" + pkgName;
            String label = pkgAppsList.get(c).activityInfo.loadLabel(pm).toString();
//            String pkgIcon = encodeIcon(pm.getApplicationIcon(pkgName));
            out += "{\"label\": \"" + label + "\", \"name\": \"" + pkgName + "\", \"icon\": \"" + pkgIcon + "\"},";
        }

        catch (Exception e) {

        }




        out = out.substring(0, out.length() - 1);
        out += "]";

        Log.v("V", out);
        return out;


    }

}
