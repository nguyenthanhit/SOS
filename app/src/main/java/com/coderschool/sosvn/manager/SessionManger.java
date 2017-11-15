package com.coderschool.sosvn.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 8/1/2017.
 */

public class SessionManger {

    private final String MyPREFERENCES = "MyPrefs" ;
    private final String CHECK ="check";
    private boolean check = false;
    private Activity activity;
    SharedPreferences pre;
    SharedPreferences.Editor editor;

    private static SessionManger sessionManger = null;

    private SessionManger(Activity activity){
        this.activity = activity;
    }

    public static SessionManger getInstance(Activity activity) {

        if (sessionManger == null) {
            sessionManger = new SessionManger(activity);
        }
        return sessionManger;
    }

    public void setLogin() {

        pre = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = pre.edit();
        editor.putBoolean(CHECK,true);
        editor.commit();
    }

    public void setLogout() {

        pre = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = pre.edit();
        editor.putBoolean(CHECK,false);
        editor.commit();
    }

    public boolean checkLogining() {
        if (pre != null) {
            pre = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            check = pre.getBoolean(CHECK,false);
            return check;
        }
        return false;
    }
}
