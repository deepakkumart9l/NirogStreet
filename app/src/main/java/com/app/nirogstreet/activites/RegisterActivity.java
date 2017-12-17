package com.app.nirogstreet.activites;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Preeti on 17-08-2017.
 */

public class RegisterActivity extends AppCompatActivity {
    SendOtpAsyncTask sendOtpAsyncTask;
    private int STORAGE_PERMISSION_CODE_VIDEO = 2;
    private int CONTACT_PERMISSION_CODE = 1;
    String email = null;
    boolean clickEnamble=true;
    String fname = null, lname = null;
    EditText firstNameEt, phoneEt, emailEt, setPass;
    String phoneNumber = null;
    LinearLayout signIn;
    TextView registerHeader;
    LinearLayout registerAs;
    Button sentTv;
    private int passwordNotVisible=1;

    CircularProgressBar circularProgressBar;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.print(requestCode);
        if (requestCode == 1) {

            getInfo();
        }
    }

    private void getInfo() {
        AccountManager am = AccountManager.get(this);
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;

        Account[] accounts = am.getAccounts();
        for (Account ac : accounts) {
            String acname = ac.name;
            String actype = ac.type;
            // Take your time to look at all available accounts
            System.out.println("Accounts : " + acname + ", " + actype);
            if (gmailPattern.matcher(ac.name).matches()) {
                email = ac.name;
                break;
            }
            System.out.print(email);
        }
        try {
            Cursor c = getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
            int count = c.getCount();
            String[] columnNames = c.getColumnNames();
            boolean b = c.moveToFirst();
            int position = c.getPosition();
            if (count == 1 && position == 0) {
                for (int j = 0; j < columnNames.length; j++) {
                    String columnName = columnNames[j];
                    if (columnName.equalsIgnoreCase("sort_key")) {
                        String columnValue = c.getString(c.getColumnIndex(columnName));
                        if (columnValue.contains(" ")) {
                            String[] val = columnValue.split(" ");
                            fname = val[0];
                            lname = val[1];
                        } else {
                            fname = columnValue;
                        }
                    }
                    //Use the values
                    //     System.out.print(columnValue);
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            System.out.print(mPhoneNumber);
            if (!mPhoneNumber.equalsIgnoreCase("")) {
                if (mPhoneNumber.length() > 10)

                {
                    if (mPhoneNumber.length() == 12) {
                        phoneNumber = mPhoneNumber.substring(2);
                    }
                    if (mPhoneNumber.length() == 13) {
                        phoneNumber = mPhoneNumber.substring(3);

                    }
                } else {
                    phoneNumber = mPhoneNumber;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (email != null) {
            emailEt.setText(email);
        }
        if (fname != null) {
            firstNameEt.setText(fname);
        }

        if (phoneNumber != null) {
            phoneEt.setText(phoneNumber);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.new_register);


        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        registerHeader = (TextView) findViewById(R.id.title_side);
        sentTv = (Button) findViewById(R.id.sentTv1);
        signIn = (LinearLayout) findViewById(R.id.signIn);
        firstNameEt = (EditText) findViewById(R.id.firstNameEt);
        phoneEt = (EditText) findViewById(R.id.phoneEt);
        emailEt = (EditText) findViewById(R.id.emailEt);
        setPass = (EditText) findViewById(R.id.passEt);
        setPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText paswword = (EditText) findViewById(R.id.passEt);
                if (passwordNotVisible == 1) {
                    paswword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                } else {

                    paswword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                }


                paswword.setSelection(paswword.length());

            }
        });
        if (email != null) {
            emailEt.setText(email);
        }
        if (fname != null) {
            firstNameEt.setText(fname);
        }

        if (phoneNumber != null) {
            phoneEt.setText(phoneNumber);
        }
        //  checkPermissionGeneral();
        try {
            checkPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        sentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtill.isNetworkAvailable(RegisterActivity.this)) {
                    if (validation()) {
                        if (checkWriteExternalPermission()) {
                            if(clickEnamble) {
                                clickEnamble=false;
                                sendOtpAsyncTask = new SendOtpAsyncTask(phoneEt.getText().toString(), setPass.getText().toString(), firstNameEt.getText().toString(), emailEt.getText().toString());
                                sendOtpAsyncTask.execute();
                            }
                        } else {
                            checkPermissionGeneral();
                        }
                    }
                } else {
                    NetworkUtill.showNoInternetDialog(RegisterActivity.this);
                }
            }
        });
    }

    private boolean checkWriteExternalPermission() {

        String permission = "android.permission.READ_SMS";
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void checkPermissionGeneral() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            requestPermissions(new String[]{Manifest.permission.READ_SMS,
                    Manifest.permission.READ_SMS}, STORAGE_PERMISSION_CODE_VIDEO);
        }
    }

    public class SendOtpAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String mobile, email, fname, lname, password;
        CircularProgressBar bar;

        JSONObject jo;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public SendOtpAsyncTask(String mobile, String password, String fname, String email) {
            this.email = email;
            this.fname = fname;
            this.password = password;
            this.mobile = mobile;
            this.mobile = mobile;
        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/send-otp";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;


                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                pairs.add(new BasicNameValuePair("User[mobile]", mobile));
                pairs.add(new BasicNameValuePair("device_token", refreshedToken));
                pairs.add(new BasicNameValuePair("type", "android"));
                pairs.add(new BasicNameValuePair("User[fname]", fname));
                pairs.add(new BasicNameValuePair("User[lname]", lname));
                pairs.add(new BasicNameValuePair("User[email]", email));
                pairs.add(new BasicNameValuePair("User[password]", password));
                pairs.add(new BasicNameValuePair("is_registration", "1"));
                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);
                responseBody = EntityUtils.toString(response.getEntity());
                jo = new JSONObject(responseBody);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            circularProgressBar.setVisibility(View.GONE);
            try {

                if (jo != null) {
                    JSONObject dataJsonObject;

                    boolean status = false;
                    String otp = "";
                    JSONArray errorArray;
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");
                        clickEnamble=true;

                        if (dataJsonObject.has("status") && !dataJsonObject.isNull("status")) {
                            status = dataJsonObject.getBoolean("status");

                            if (status) {

                                dataJsonObject = jo.getJSONObject("data");
                                if (dataJsonObject.has("OTP") && !dataJsonObject.isNull("OTP")) {
                                    otp = dataJsonObject.getString("OTP");
                                }
                                Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                                intent.putExtra("fname", firstNameEt.getText().toString());
                                intent.putExtra("otp", otp);
                                intent.putExtra("email", emailEt.getText().toString().trim());
                                intent.putExtra("phone", phoneEt.getText().toString());
                                intent.putExtra("password", setPass.getText().toString());
                                startActivity(intent);
                            } else {
                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    errorArray = dataJsonObject.getJSONArray("message");
                                    for (int i = 0; i < errorArray.length(); i++) {
                                        String error = errorArray.getJSONObject(i).getString("error");
                                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this,R.string.wrong,Toast.LENGTH_LONG).show();
            }


        }
    }

    protected boolean validation() {
        if (firstNameEt.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phoneEt.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Methods.isValidPhoneNumber(phoneEt.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (emailEt.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Methods.isValidEmailAddress(emailEt.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "Enter valid email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (setPass.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        } /*else if (!Methods.isValidPassword(setPass.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sendOtpAsyncTask != null && !sendOtpAsyncTask.isCancelled()) {
            sendOtpAsyncTask.cancelAsyncTask();
        }
    }

    public ArrayList<HashMap<String, Object>> getContacts() {

        ArrayList<HashMap<String, Object>> contacts = new ArrayList<HashMap<String, Object>>();
        final String[] projection = new String[]{ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DELETED};

        @SuppressWarnings("deprecation")
        final Cursor rawContacts = managedQuery(ContactsContract.RawContacts.CONTENT_URI, projection, null, null, null);

        final int contactIdColumnIndex = rawContacts.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID);
        final int deletedColumnIndex = rawContacts.getColumnIndex(ContactsContract.RawContacts.DELETED);

        if (rawContacts.moveToFirst()) {
            while (!rawContacts.isAfterLast()) {
                final int contactId = rawContacts.getInt(contactIdColumnIndex);
                final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);

                if (!deleted) {
                    HashMap<String, Object> contactInfo = new HashMap<String, Object>() {
                        {
                            put("contactId", "");
                            put("name", "");
                            put("email", "");
                            put("address", "");
                            put("photo", "");
                            put("phone", "");
                        }
                    };
                    contactInfo.put("contactId", "" + contactId);
                    contactInfo.put("name", getName(contactId));
                    contactInfo.put("email", getEmail(contactId));
                    contactInfo.put("photo", getPhoto(contactId) != null ? getPhoto(contactId) : "");
                    contactInfo.put("address", getAddress(contactId));
                    contactInfo.put("phone", getPhoneNumber(contactId));
                    contactInfo.put("isChecked", "false");
                    contacts.add(contactInfo);
                }
                rawContacts.moveToNext();
            }
        }

        rawContacts.close();

        return contacts;
    }

    private String getName(int contactId) {
        String name = "";
        final String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

        final Cursor contact = managedQuery(ContactsContract.Contacts.CONTENT_URI, projection, ContactsContract.Contacts._ID + "=?", new String[]{String.valueOf(contactId)}, null);

        if (contact.moveToFirst()) {
            name = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.close();
        }
        contact.close();
        return name;

    }

    private String getEmail(int contactId) {
        String emailStr = "";
        final String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.DATA, // use
                // Email.ADDRESS
                // for API-Level
                // 11+
                ContactsContract.CommonDataKinds.Email.TYPE};

        final Cursor email = managedQuery(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

            while (!email.isAfterLast()) {
                emailStr = emailStr + email.getString(contactEmailColumnIndex) + ";";
                email.moveToNext();
            }
        }
        email.close();
        return emailStr;

    }

    private Bitmap getPhoto(int contactId) {
        Bitmap photo = null;
        final String[] projection = new String[]{ContactsContract.Contacts.PHOTO_ID};

        final Cursor contact = managedQuery(ContactsContract.Contacts.CONTENT_URI, projection, ContactsContract.Contacts._ID + "=?", new String[]{String.valueOf(contactId)}, null);

        if (contact.moveToFirst()) {
            final String photoId = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
            if (photoId != null) {
                photo = getBitmap(photoId);
            } else {
                photo = null;
            }
        }
        contact.close();

        return photo;
    }

    private Bitmap getBitmap(String photoId) {
        final Cursor photo = managedQuery(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, ContactsContract.Data._ID + "=?", new String[]{photoId}, null);

        final Bitmap photoBitmap;
        if (photo.moveToFirst()) {
            byte[] photoBlob = photo.getBlob(photo.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
            photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.length);
        } else {
            photoBitmap = null;
        }
        photo.close();
        return photoBitmap;
    }

    private String getAddress(int contactId) {
        String postalData = "";
        String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] addrWhereParams = new String[]{String.valueOf(contactId), ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};

        Cursor addrCur = managedQuery(ContactsContract.Data.CONTENT_URI, null, addrWhere, addrWhereParams, null);

        if (addrCur.moveToFirst()) {
            postalData = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
        }
        addrCur.close();
        return postalData;
    }

    private String getPhoneNumber(int contactId) {

        String phoneNumber = "";
        final String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE,};
        final Cursor phone = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);

            while (!phone.isAfterLast()) {
                phoneNumber = phoneNumber + phone.getString(contactNumberColumnIndex) + ";";
                phone.moveToNext();
            }

        }
        phone.close();
        return phoneNumber;
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
            getInfo();
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE}, CONTACT_PERMISSION_CODE);
        }
    }

}
