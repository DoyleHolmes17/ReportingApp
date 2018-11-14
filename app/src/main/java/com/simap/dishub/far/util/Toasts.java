package com.simap.dishub.far.util;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aviroez on 10/02/2015.
 */
public class Toasts {
    private static int textSize = 20;

    public static void showShortToast(Context context, String messsage) {
        Toast toast = Toast.makeText(context, messsage, Toast.LENGTH_SHORT);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(textSize);
        toast.show();
    }

    public static void showShortToast(Context context, int StringId) {
        Toast toast = Toast.makeText(context, StringId, Toast.LENGTH_SHORT);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(textSize);
        toast.show();
    }

    public static void showLongToast(Context context, String messsage) {
        Toast toast= Toast.makeText(context, messsage, Toast.LENGTH_LONG);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(textSize);
        toast.show();
    }

    public static void showLongToast(Context context, int StringId) {
        Toast.makeText(context, StringId, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context context, Toast toast, String messsage) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, messsage, Toast.LENGTH_SHORT);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(textSize);
        toast.show();
    }

    public static void showShortToast(Context context, Toast toast, int StringId) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, StringId, Toast.LENGTH_SHORT);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(textSize);
        toast.show();
    }

    public static void showLongToast(Context context, Toast toast, String messsage) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, messsage, Toast.LENGTH_LONG);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(textSize);
        toast.show();
    }

    public static void showLongToast(Context context, Toast toast, int StringId) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, StringId, Toast.LENGTH_LONG);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(textSize);
        toast.show();
    }
}
