package com.app.nirogstreet.uttil;

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
}
