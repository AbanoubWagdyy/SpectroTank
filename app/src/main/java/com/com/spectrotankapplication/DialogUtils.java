package com.com.spectrotankapplication;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtils {

    private static ProgressDialog progressDialog;

    public static synchronized void showProgressDialog(Context context, String message) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public static synchronized Boolean isProgressShowing() {
        if (progressDialog == null)
            return false;
        return progressDialog.isShowing();
    }

    public static synchronized void dismissProgressDialog() {
        if (isProgressShowing())
            progressDialog.dismiss();
    }
}