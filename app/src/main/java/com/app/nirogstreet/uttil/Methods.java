package com.app.nirogstreet.uttil;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;

import com.app.nirogstreet.activites.SearchLocationCity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Preeti on 23-08-2017.
 */

public class Methods {
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean validWebOrBlog(String str) {
        if (URLUtil.isValidUrl(str))
            return true;
        return false;


    }
    public static boolean isPastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return (date.before(calendar.getTime())) ? true : false;
    }

    public static void showSoftKeyboard(View view, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
    public static boolean isValidPhoneNumber(String phone) {
        if (phone.length() < 10 || phone.length() > 15) {
            return false;
        }
        return true;
    }

    public static boolean isValidPassword(String s) {
        if (s.length() < 5)
            return false;
        return true;
    }
    public static void hideKeyboardOfView(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(EditText autoCompleteTextView, SearchLocationCity searchLocationCity) {
    }
}
