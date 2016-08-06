package com.example.listview;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author lynn
 *
 */
public class ConnectivityCheck {

    public static boolean isNetworkReachable(Context context) {
        ConnectivityManager mManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = mManager.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.getState() == NetworkInfo.State.CONNECTED);
    }

    /**
     * @return
     * very cheesy, splash up a big cant do it screen instead of doing something useful
     * like waiting a bit, testing connectivity, and then trying again
     */
    public static boolean isNetworkReachableAlertUserIfNot(Context context) {
        boolean isReachable = isNetworkReachable(context);
        if (!isReachable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.alert_title);
            builder.setMessage(R.string.alert_message);
            builder.setPositiveButton(R.string.alert_ok, null);
            builder.create().show();
        }
        return isReachable;
    }

    public static boolean isWifiReachable(Context context) {
        ConnectivityManager mManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = mManager.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.getType() == ConnectivityManager.TYPE_WIFI);
    }
}
