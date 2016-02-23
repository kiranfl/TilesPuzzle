package com.games.tilespuzzle.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @author kiran on 2/5/2016.
 */
public class Utils {
    private static ProgressDialog mProgressDialog;

    public static ProgressDialog showProgressBar(Context context, String msg) {
        hideProgressBar();

        try {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setProgressStyle(android.R.style.Widget_DeviceDefault_ProgressBar_Small_Title);
                mProgressDialog.setMessage(msg);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mProgressDialog;
    }

    public static ProgressDialog showProgressBar(Context context, String msg, boolean flag) {
        hideProgressBar();

        try {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
                mProgressDialog.setMessage(msg);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(flag);
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mProgressDialog;
    }

    public static void hideProgressBar() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mProgressDialog = null;
    }
}
