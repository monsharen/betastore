package com.lionsinvests.betastore;

import android.content.Context;
import android.widget.Toast;

class ToastUtil {

    static void showToast(Context context, String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
