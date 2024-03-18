package com.tp.recyclertree;

import android.util.Log;



/**
 * @author akshat.tailang on 7/11/17.
 */

public class AppLog {

    private static final String TAG = "TimesPrimeApp";

    private static final boolean SHOW_LOGS = true;

    public static void e(String msg) {
        if (SHOW_LOGS)
            Log.e(TAG, msg);
    }
    public static void e(String tag,String msg) {
        if (SHOW_LOGS)
            Log.e(tag, msg);
    }

    public static void d(String msg) {
        if (SHOW_LOGS)
            Log.d(TAG, msg);
    }

    public static void d(String tag,String msg) {
        if (SHOW_LOGS)
            Log.d(tag, msg);
    }

}
