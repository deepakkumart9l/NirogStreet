package com.app.nirogstreet.uttil;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.Dr_Profile;
import com.app.nirogstreet.activites.MyActivities;
import com.app.nirogstreet.activites.SearchLocationCity;
import com.app.nirogstreet.activites.Student_Profile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Preeti on 23-08-2017.
 */

public class Methods {

    static ProgressBar pb;
    static Dialog dialog;
    static TextView cur_val;

    public static String getName(String title, String name) {
        String str = "";
        if (title != null) {
            if (title.equalsIgnoreCase("1")) {
                str = "Dr. " + name;
            } else {
                str = name;
            }
        } else {
            str = name;
        }
        return str;
    }

    public static void profileUser(String userType, Context context, String userId) {
        if(userType!=null) {
            SesstionManager sesstionManager = new SesstionManager(context);
            if (userType.equalsIgnoreCase(AppUrl.DOCTOR_ROLE)) {
                Intent intent = new Intent(context, Dr_Profile.class);
                if (!userId.equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                    intent.putExtra("UserId", userId);

                }
                context.startActivity(intent);
            } else if (userType.equalsIgnoreCase(AppUrl.STUDENT_ROLE)) {
                Intent intent = new Intent(context, Student_Profile.class);
                if (!userId.equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                    intent.putExtra("UserId", userId);

                }
                context.startActivity(intent);
            }
        }
    }
public static void openUserActivities(Context context,String userId,String name,String profile_pic,String title,String userType)
{
    Intent intent=new Intent(context, MyActivities.class);
    intent.putExtra("userId",userId);
    intent.putExtra("name",name);
    intent.putExtra("profile_pic",profile_pic);
    intent.putExtra("title",title);
    intent.putExtra("userType",userType);
    context.startActivity(intent);
}
    public static boolean validCellPhone(String number) {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    public static boolean checkStringContainsValidUrl(String str) {
        String strarr[] = str.split(" ");
        for (int i = 0; i < strarr.length; i++) {
            System.out.print(strarr[i]);
            if (Patterns.WEB_URL.matcher(strarr[i]).matches()) {
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static void downloadFile(String dwnload_file_path, Activity activity, String extention, String name) {

        new downloadDocument(dwnload_file_path, name, extention, activity).execute();
    }

    public static void showProgress(String file_path, Activity activity) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");

        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setVisibility(View.GONE);
        text.setText("Downloading file from ... " + file_path);
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");
        dialog.show();

        pb = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(activity.getResources().getDrawable(R.drawable.green_progress));
    }

    public static class downloadDocument extends AsyncTask<Void, Void, Void> {
        String dwnload_file_path;
        String name, extention;
        Activity activity;

        public downloadDocument(String dwnload_file_path, String name, String extention, Activity activity) {
            this.dwnload_file_path = dwnload_file_path;
            this.name = name;
            this.activity = activity;
            this.extention = extention;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(dwnload_file_path);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int fileLength = connection.getContentLength();

                    input = connection.getInputStream();
                    output = new FileOutputStream("/sdcard/" + name + "." + extention);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        // allow canceling with back button
                        if (isCancelled()) {
                            input.close();
                            return null;
                        }
                        total += count;
                        // publishing the progress....
                        if (fileLength > 0) // only if total length is known
                            //  publishProgress((int) (total * 100 / fileLength));
                            output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }

                    if (connection != null)
                        connection.disconnect();
                }

            } catch (final Exception e) {
                showError("Error : Please check your internet connection " + e, activity);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            openDocument(activity, extention, name);
            dialog.cancel();
        }
    }

    public static boolean validWebOrBlog(String str) {
        if (URLUtil.isValidUrl(str))
            return true;
        return false;


    }

    public static boolean validWebUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches();

    }

    private static boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }

    public static String getUrl(String s) {
        try {
            int i = 0;

            SpannableString spannableString = new SpannableString(s);
            Matcher urlMatcher = Patterns.WEB_URL.matcher(s);
            while (urlMatcher.find()) {
                String url = urlMatcher.group(i);
                int start = urlMatcher.start(i);
                int end = urlMatcher.end(i++);
                if (isValidUrl(url)) {
                    return url;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void hyperlinkSet(TextView textView, String s, Context context,int is_pin,SpannableString spannableString) {
        try {
            int i = 0;
            boolean isValidTrue=false;

            Matcher urlMatcher = Patterns.WEB_URL.matcher(s);
            while (urlMatcher.find()) {
                String url = urlMatcher.group(i);
                int start = urlMatcher.start(i);
                int end = urlMatcher.end(i++);
                if (isValidUrl(url)) {
                    isValidTrue=true;
                    if (url.startsWith("http") || url.startsWith("www.") || url.startsWith("Http")||url.startsWith("Www.")) {
                        if(is_pin==1) {
                            SesstionManager sesstionManager=new SesstionManager(context);
                            String q             = Base64.encodeToString(sesstionManager.getUserDetails().get(SesstionManager.USER_ID).getBytes(), Base64.NO_WRAP);

                            spannableString.setSpan(new GoToURLSpan(url+"?userId="+q, context), start, end, 0);
                        }else {
                            spannableString.setSpan(new GoToURLSpan(url, context), start, end, 0);

                        }
                    }
                }else {

                }

            }
            if(!isValidTrue)
            {
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            //textView.setText(spannableString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hyperlink(TextView textView, String s, Context context,int is_pin) {
        try {
            int i = 0;
            SpannableString spannableString = new SpannableString(s);
            Matcher urlMatcher = Patterns.WEB_URL.matcher(s);
            while (urlMatcher.find()) {
                String url = urlMatcher.group(i);
                int start = urlMatcher.start(i);
                int end = urlMatcher.end(i++);
                if (isValidUrl(url)) {
                    if (url.startsWith("http") || url.startsWith("www.") || url.startsWith("Http"))
                        spannableString.setSpan(new GoToURLSpan(url, context), start, end, 0);
                }

            }

            textView.setText(spannableString);
            if(is_pin!=1)
            textView.setMovementMethod(new LinkMovementMethod());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openDocument(Context context, String typeOfDoc, String fileName) {

        File file = new File(Environment.getExternalStorageDirectory(),
                fileName + "." + typeOfDoc);
        Uri path = Uri.fromFile(file);
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, "application/" + typeOfDoc);
        try {
            context.startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Downloaded", Toast.LENGTH_LONG).show();
        }
    }

    static void showError(final String err, final Context context) {

        Toast.makeText(context, err, Toast.LENGTH_LONG).show();
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
        if (phone.length() < 10 || phone.length() > 10) {
            return false;
        }
        return true;
    }

    public static boolean isValidPassword(String s) {
        if (s.length() < 6)
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
