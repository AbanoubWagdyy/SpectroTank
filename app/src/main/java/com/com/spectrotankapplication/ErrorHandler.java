package com.com.spectrotankapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorHandler implements
        Thread.UncaughtExceptionHandler {

    private static Activity mActivity;

    private ErrorHandler(Activity activity) {
        mPackageName = getPackageName(activity);
    }

    public static ErrorHandler getINSTANCE(Activity activity) {
        if (mErrorHandler == null) {
            mErrorHandler = new ErrorHandler(activity);
        }
        return mErrorHandler;
    }

    private static String getPackageName(Context pContext) {
        String packageName = "";
        try {
            ActivityManager activityManager = (ActivityManager) pContext
                    .getSystemService(Context.ACTIVITY_SERVICE);

            if (Build.VERSION.SDK_INT > 20) {
                packageName = activityManager.getRunningAppProcesses().get(0).processName;
            }
            packageName = packageName
                    .substring(0, packageName.length() > 22 ? 22 : packageName.length());
        } catch (Exception ex) {
        }
        if (packageName.isEmpty()) {
            packageName = pContext.getPackageName();
        }
        return packageName;
    }


    public static void toCatch(Activity activity) {
        ErrorHandler.mActivity = activity;
        Thread.setDefaultUncaughtExceptionHandler(getINSTANCE(activity));
    }


    public static void logError(String message) {
        if (message.isEmpty()) {
            return;
        }
        logError(new Throwable(message.trim()));
    }


    public static void logError(Throwable exception) {
        try {
            logCrash(exception);
        } catch (Exception e) {
            Log.e(mPackageName, e.getMessage());
        }
    }

    public static String getErrorMessage() {
        return mErrorMessage;
    }

    public static void setErrorMessage(String errMsg) {
        mErrorMessage = errMsg;
    }

    public static ApplicationErrorReport.CrashInfo crashInfo() {
        return mCrashInfo;
    }

    private static String getAppLabel(Context pContext) {
        PackageManager lPackageManager = pContext.getPackageManager();
        ApplicationInfo lApplicationInfo = null;
        try {
            lApplicationInfo = lPackageManager
                    .getApplicationInfo(pContext.getApplicationInfo().packageName, 0);

        } catch (final PackageManager.NameNotFoundException e) {
        }
        return (String) (lApplicationInfo != null ? lPackageManager
                .getApplicationLabel(lApplicationInfo) : "Unknown");
    }

    @NonNull
    private static String errorMsg(Throwable exception, String exceptError) {

        if (!exceptError.contains("error")) {

            mReportBuilder.append(reportError(exception));
        }

        if (!exceptError.contains("callstack")) {

            mReportBuilder.append(reportCallStack(exception));
        }

        if (!exceptError.contains("deviceinfo")) {

            mReportBuilder.append(reportDeviceInfo());
        }

        if (!exceptError.contains("firmware")) {

            mReportBuilder.append(reportFirmware());
        }

        return mReportBuilder.toString();
    }


    private static String reportError(Throwable exception) {
        mCrashInfo = new ApplicationErrorReport.CrashInfo(exception);
        if (mCrashInfo.exceptionMessage == null) {
            mErrorMessage = "<unknown error>";
        } else {

            mErrorMessage = mCrashInfo.exceptionMessage
                    .replace(": " + mCrashInfo.exceptionClassName, "");
        }

        String throwFile = mCrashInfo.throwFileName == null ? "<unknown file>"
                : mCrashInfo.throwFileName;

        return "\n************ " + mCrashInfo.exceptionClassName + " ************\n"
                + mErrorMessage + LINE_SEPARATOR
                + "\n File: " + throwFile
                + "\n Method: " + mCrashInfo.throwMethodName + "()"
                + "\n Line No.: " + Integer.toString(mCrashInfo.throwLineNumber)
                + LINE_SEPARATOR;
        //          + "Class: " + crashInfo.throwClassName + LINE_SEPARATOR
    }


    private static String reportCallStack(Throwable exception) {

        StringWriter stackTrace = new StringWriter();

        exception.printStackTrace(new PrintWriter(stackTrace));

        String callStack = stackTrace.toString();

        String errMsg = exception.toString();

        return "\n************ CALLSTACK ************\n"
                + callStack.replace(errMsg, "")
                + LINE_SEPARATOR;
    }


    private static String reportDeviceInfo() {

        return "\n************ DEVICE INFORMATION ***********\n"
                + "Brand: "
                + Build.BRAND
                + LINE_SEPARATOR
                + "Device: "
                + Build.DEVICE
                + LINE_SEPARATOR
                + "Model: "
                + Build.MODEL
                + LINE_SEPARATOR
                + "Id: "
                + Build.ID
                + LINE_SEPARATOR
                + "Product: "
                + Build.PRODUCT
                + LINE_SEPARATOR;
    }


    private static String reportFirmware() {

        return "\n************ FIRMWARE ************\n"
                + "SDK: "
                + Build.VERSION.SDK_INT
                + LINE_SEPARATOR
                + "Release: "
                + Build.VERSION.RELEASE
                + LINE_SEPARATOR
                + "Incremental: "
                + Build.VERSION.INCREMENTAL
                + LINE_SEPARATOR;
    }


    // Empty the report as it is begin re-populated.
    private static void reportEmptied() {

        // No need to empty
        if (mReportBuilder.length() == 0) {

            return;
        }

        mReportBuilder.setLength(0);

        mReportBuilder.trimToSize();
    }


    @Override
    public void uncaughtException(Thread thread, Throwable exception) {

        // Don't re-enter -- avoid infinite loops if crash-reporting crashes.
        if (mCrashing) return;

        mCrashing = true;

        catchException(thread, exception);

        defaultExceptionHandler(thread, exception);
    }


    public String catchException(Thread thread, Throwable exception) {

        String errorMsg = "";
        try {
            errorMsg = logCrash(exception);
        } catch (Exception ex) {
            Log.e(mPackageName, ex.getMessage());
        }
        return errorMsg;
    }

    public static void defaultExceptionHandler(Thread thread, Throwable exception) {
        try {
            if (mOldHandler != null) {
                mOldHandler.uncaughtException(thread, exception);
            }
        } catch (Exception ex) {
            Log.e(mPackageName, ex.getMessage());
        }
    }


    public static String logCrash(Throwable exception) {
        String errorMessage = errorMsg(exception, "deviceinfo firmware");
        Toast.makeText(mActivity, errorMessage, Toast.LENGTH_LONG).show();
        return errorMessage;
    }

    private static volatile boolean mCrashing = false;

    private static final StringBuilder mReportBuilder = new StringBuilder();

    private static final String LINE_SEPARATOR = "\n";

    private static final Thread.UncaughtExceptionHandler mOldHandler = Thread
            .getDefaultUncaughtExceptionHandler();

    private static ErrorHandler mErrorHandler;

    private static String mPackageName;

    private static ApplicationErrorReport.CrashInfo mCrashInfo;

    private static String mErrorMessage = "";
}
