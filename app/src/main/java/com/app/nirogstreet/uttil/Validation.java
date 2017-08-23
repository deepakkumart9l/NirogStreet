package com.app.nirogstreet.uttil;

/**
 * Created by Preeti on 23-08-2017.
 */
public class Validation {
    public static boolean isEmptyString(String text) {

        return !(text != null && !text.isEmpty() && !text.equals("null"));

    }
}
