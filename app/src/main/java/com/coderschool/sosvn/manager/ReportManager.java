package com.coderschool.sosvn.manager;

import android.graphics.Bitmap;

import com.coderschool.sosvn.object.Report;

public class ReportManager {

    private Report report;
    private static final ReportManager ourInstance = new ReportManager();

    public static ReportManager getInstance() {
        return ourInstance;
    }

    private ReportManager() {
        if(report == null)
            report = new Report();
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
    public void setReport(String severity, String what, Bitmap where) {
        ourInstance.report.setSeverity(severity);
        ourInstance.report.setWhat(what);
        ourInstance.report.setWhere(where);
    }
}
