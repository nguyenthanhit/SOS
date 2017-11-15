package com.coderschool.sosvn.object;

import android.graphics.Bitmap;

public class Report {
    private String severity;
    private String what;
    private Bitmap where;

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public Bitmap getWhere() {
        return where;
    }

    public void setWhere(Bitmap where) {
        this.where = where;
    }
}
