package com.example.whisperloop.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class Validator {
    public static Boolean validate(Context context, String emailInput, String passwordInput) {
        String emailRegularExpression = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (TextUtils.isEmpty((emailInput))) {
            Toast.makeText(context, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty((passwordInput))) {
            Toast.makeText(context, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!emailInput.matches(emailRegularExpression)) {
            Toast.makeText(context, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (passwordInput.length() < 6) {
            Toast.makeText(context, "Password Should Contain At Least 6 Characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
