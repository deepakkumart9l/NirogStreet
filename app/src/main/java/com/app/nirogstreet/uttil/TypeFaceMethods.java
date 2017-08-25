package com.app.nirogstreet.uttil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Preeti on 18-08-2017.
 */

public class TypeFaceMethods {
    public static void setRegularTypeFaceForTextView(TextView textView, Context context)
    {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/ubuntu.regular.ttf");
        textView.setTypeface(tf);

    }
    public static void setRegularTypeFaceEditText(EditText editText, Context context)
    {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/ubuntu.regular.ttf");
        editText.setTypeface(tf);

    }
    public static void setRegularTypeBoldFaceTextView(TextView textView, Context context)
    {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/ubuntu.bold.ttf");
        textView.setTypeface(tf);

    }
    public static void setRegularTypeFaceRadioButton(RadioButton radioButton, Context context)
    {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/ubuntu.regular.ttf");
        radioButton.setTypeface(tf);

    }
}
