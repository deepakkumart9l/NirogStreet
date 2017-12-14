package com.app.nirogstreet.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.AppLaunchChecker;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.support.design.widget.TextInputLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.UserDetailPaser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.ImageLoader;
import com.app.nirogstreet.uttil.ImageProcess;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.ganfra.materialspinner.MaterialSpinner;

import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;

/**
 * Created by Preeti on 23-08-2017.
 */

public class CreateDrProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    CircleImageView circleImageView;
    CircularProgressBar circularProgressBar;
    RadioGroup genderSpinnerRadioGroup;
    ImageView backImageView;
    UpdateProfileAsyncTask updateProfileAsyncTask;
    TextView registerAs;
    private int REQUEST_CAMERA = 99;

    String title, category, gender;
    EditText editTextDob, editTextemail, editTextName, editTextCity, editTextYearOfExpeicence, editTextWebsite, editTextAbout, editTextContactNumber;
    TextInputLayout textInputLayout;
    File photoFile;
    private static final String[] titleArray = {"Dr/Mr", "Dr/Mrs", "Dr/Miss"};
    private static final String[] genderArray = {"Male", "Female"};
    private static final String[] categoryArray = {"Ayurveda", "Naturopathy"};
    boolean isSkip = false;
    TextView skipTextView;
    boolean isImageClicked = false;

    public static boolean isVisible = true;
    String selectedImagePath = null;
    String authToken, userId, email, mobile, userName;
    TextView saveTv;
    String mCurrentPhotoPath;
    RadioButton maleRadioButton, femaleRadioButton;
    MaterialSpinner spinnerTitle, spinnerGender, spinnerCategory;
    private ArrayAdapter<String> adapterTitle, adapterGender, adapterCategory;
    UserDetailAsyncTask userDetailAsyncTask;
    private int STORAGE_PERMISSION_CODE_VIDEO = 2;

    UserDetailModel userDetailModel;
    private int CAMERA_PERMISSION_CODE = 1;

    private int SELECT_FILE = 999;
    TextView title_side_left;
    SesstionManager sesstionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.create_dr_profile);
        if (getIntent().hasExtra("isSkip")) {
            isSkip = getIntent().getBooleanExtra("isSkip", false);
        }
        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
        }
        title_side_left=(TextView)findViewById(R.id.title_side_left);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(title_side_left,CreateDrProfile.this);
        editTextemail = (EditText) findViewById(R.id.email);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editTextName = (EditText) findViewById(R.id.firstNameEt);
        editTextCity = (EditText) findViewById(R.id.cityEt);
        editTextYearOfExpeicence = (EditText) findViewById(R.id.no_of_year);
        editTextWebsite = (EditText) findViewById(R.id.website_blog);
        editTextAbout = (EditText) findViewById(R.id.about_you);

        editTextContactNumber = (EditText) findViewById(R.id.contact_num);
        maleRadioButton = (RadioButton) findViewById(R.id.male);
        registerAs = (TextView) findViewById(R.id.registerAs);
        genderSpinnerRadioGroup = (RadioGroup) findViewById(R.id.genderSpinner);
        femaleRadioButton = (RadioButton) findViewById(R.id.female);
        skipTextView = (TextView) findViewById(R.id.skip);
        spinnerCategory = (MaterialSpinner) findViewById(R.id.categorySpinner);
        spinnerTitle = (MaterialSpinner) findViewById(R.id.titleLay);

        if (isSkip) {
            skipTextView.setVisibility(View.GONE);
            backImageView.setVisibility(View.GONE);
        } else {
            skipTextView.setVisibility(View.GONE);

        }
        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateDrProfile.this, Dr_Qualifications.class);
                intent.putExtra("isSkip", true);
                startActivity(intent);
            }
        });
        TypeFaceMethods.setRegularTypeFaceForTextView(skipTextView, CreateDrProfile.this);

        TypeFaceMethods.setRegularTypeFaceForTextView(registerAs, CreateDrProfile.this);

        TypeFaceMethods.setRegularTypeFaceRadioButton(maleRadioButton, CreateDrProfile.this);
        TypeFaceMethods.setRegularTypeFaceRadioButton(femaleRadioButton, CreateDrProfile.this);
        TypeFaceMethods.setRegularTypeFaceEditText(editTextemail, CreateDrProfile.this);
        TypeFaceMethods.setRegularTypeFaceEditText(editTextName, CreateDrProfile.this);
        TypeFaceMethods.setRegularTypeFaceEditText(editTextCity, CreateDrProfile.this);

        TypeFaceMethods.setRegularTypeFaceEditText(editTextYearOfExpeicence, CreateDrProfile.this);
        TypeFaceMethods.setRegularTypeFaceEditText(editTextWebsite, CreateDrProfile.this);
        TypeFaceMethods.setRegularTypeFaceEditText(editTextAbout, CreateDrProfile.this);
        TypeFaceMethods.setRegularTypeFaceEditText(editTextContactNumber, CreateDrProfile.this);

        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        circleImageView = (CircleImageView) findViewById(R.id.pro);
        sesstionManager = new SesstionManager(CreateDrProfile.this);

        if (sesstionManager.isUserLoggedIn()) {
            authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
            userId = sesstionManager.getUserDetails().get(SesstionManager.USER_ID);
            email = sesstionManager.getUserDetails().get(SesstionManager.KEY_EMAIL);
            userName = sesstionManager.getUserDetails().get(SesstionManager.KEY_FNAME) + " "+sesstionManager.getUserDetails().get(SesstionManager.KEY_LNAME);
            mobile = sesstionManager.getUserDetails().get(SesstionManager.MOBILE);
            editTextemail.setText(email);
            editTextName.setText(userName);
            editTextContactNumber.setText(mobile);

        }

        editTextemail.setEnabled(false);
        editTextName.setEnabled(false);
        editTextContactNumber.setEnabled(false);
        saveTv = (TextView) findViewById(R.id.saveTv);
        TypeFaceMethods.setRegularTypeFaceForTextView(saveTv, CreateDrProfile.this);
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtill.isNetworkAvailable(CreateDrProfile.this)) {
                    if (validate()) {

                        updateProfileAsyncTask = new UpdateProfileAsyncTask(userName, email, mobile, title, category, editTextCity.getText().toString(), gender, editTextYearOfExpeicence.getText().toString(), editTextDob.getText().toString(), editTextWebsite.getText().toString(), editTextAbout.getText().toString());
                        updateProfileAsyncTask.execute();
                    }

                } else {
                    NetworkUtill.showNoInternetDialog(CreateDrProfile.this);
                }
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();

            }
        });
        textInputLayout = (TextInputLayout) findViewById(R.id.dob_layout);

        editTextDob = (EditText) findViewById(R.id.dob);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextDob.getWindowToken(), 0);
        editTextDob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                return false;
            }
        });
        editTextDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextAbout.getWindowToken(), 0);
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(editTextWebsite.getWindowToken(), 0);
                if (isVisible) {
                    showDateDialog();

                    isVisible = false;
                }
            }
        });
        TypeFaceMethods.setRegularTypeFaceForTextView(editTextDob, CreateDrProfile.this);


        initSpinnerScrollingTitle();
        initSpinnerScrollingCategory();
       // spinnerCategory.setSelection(0);
        if (NetworkUtill.isNetworkAvailable(CreateDrProfile.this)) {
            userDetailAsyncTask = new UserDetailAsyncTask();
            userDetailAsyncTask.execute();
        } else {
            NetworkUtill.showNoInternetDialog(CreateDrProfile.this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.print(requestCode);
        if (requestCode == 1) {
            selectImage();
        }
    }
    private void selectImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateDrProfile.this);


        if (selectedImagePath == null)

        {
            final CharSequence[] items = {"Take Photo", "Choose from Library",
                    "Cancel"};
            builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
                        takePicture();
                        isImageClicked = true;
                    } else if (items[item].equals("Choose from Library")) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, SELECT_FILE);
                        isImageClicked = true;

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();

                    }
                }
            });
        } else {

            final CharSequence[] items = {"Take Photo", "Choose from Library",
                    "Remove", "Cancel"};
            builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
                        takePicture();
                        isImageClicked = true;

                    } else if (items[item].equals("Choose from Library")) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, SELECT_FILE);
                        isImageClicked = true;

                    } else if (items[item].equals("Remove")) {
                        dialog.dismiss();
                        selectedImagePath = null;
                        Glide.with(CreateDrProfile.this)
                                .load(R.drawable.user)
                                .into(circleImageView);
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
        }
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = new ImageProcess(CreateDrProfile.this).getOutputMediaFile("");
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e("ex", "" + ex);
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    public void checkPermissionGeneral() {
        if (ContextCompat.checkSelfPermission(CreateDrProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CreateDrProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE_VIDEO);
        }
    }

    public void showDateDialog() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextAbout.getWindowToken(), 0);
        InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm1.hideSoftInputFromWindow(editTextWebsite.getWindowToken(), 0);
        imm1.hideSoftInputFromWindow(editTextDob.getWindowToken(), 0);
        imm1.hideSoftInputFromWindow(editTextCity.getWindowToken(), 0);
        imm1.hideSoftInputFromWindow(editTextYearOfExpeicence.getWindowToken(), 0);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        TimePickerFragment newFragment = new TimePickerFragment(this);

        newFragment.show(fm, "date");

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        isVisible = true;
        DateFormat format = new java.text.SimpleDateFormat("dd-MM-yyyy");

        String dateStr = (new StringBuilder()

                // Month is 0 based, just add 1

                .append(dayOfMonth).append("-").append(month + 1).append("-")
                .append(year)).toString();
        try {
            Date date1 = format.parse(dateStr);
            if (Methods.isPastDay(date1)) {
                editTextDob.setText(dateStr);

            } else {
                Toast.makeText(CreateDrProfile.this, "Not a valid date", Toast.LENGTH_SHORT).show();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment {


        private DatePickerDialog.OnDateSetListener listener;

        public TimePickerFragment(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public void onDestroyView() {
            CreateDrProfile.isVisible = true;
            super.onDestroyView();

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int year = 1980;
            int month = 1;
            int day = 1;

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), listener, year, month,
                    day);
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = new ImageProcess(CreateDrProfile.this).getOutputMediaFile("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.e("ex", "" + ex);
                    // Error occurred while creating the File
                }
                Uri photoURI = FileProvider.getUriForFile(CreateDrProfile.this,
                        "com.app.nirogstreet.fileprovider",
                        photoFile);
                Log.e("photoURI", "" + photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = Uri.fromFile(photoFile);

                try {
                    ImageProcess obj = new ImageProcess(CreateDrProfile.this);
                    mCurrentPhotoPath = obj.getPath(uri);
                    selectedImagePath = mCurrentPhotoPath;
                    File fff = new File(selectedImagePath);
                    Glide.with(CreateDrProfile.this)
                            .load(fff) // Uri of the picture
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .crossFade()
                            .override(100, 100)
                            .into(circleImageView);
                } catch (Exception e) {

                }
            }
        }
        if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
            try {
                final Uri imageUri = data.getData();

                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                circleImageView.setImageBitmap(selectedImage);
                Uri selectedImagePath1 = getImageUri(CreateDrProfile.this, selectedImage);
                selectedImagePath = getPath(selectedImagePath1, CreateDrProfile.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    private void initSpinnerScrollingCategory() {
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1)
                    category = position + 1 + "";
                else
                    category = "-1";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spiner_item, categoryArray) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                TypeFaceMethods.setRegularTypeFaceForTextView((TextView) v, CreateDrProfile.this);

                return v;
            }


            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                TypeFaceMethods.setRegularTypeFaceForTextView((TextView) v, CreateDrProfile.this);

                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setHint("Select Category");
        spinnerCategory.setPaddingSafe(0, 0, 0, 0);

    }

    private void initSpinnerScrollingTitle() {
        spinnerTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    title = position + 1 + "";
                    if (position==0)
                    {
                        maleRadioButton.setChecked(true);
                    }
                    else {
                        femaleRadioButton.setChecked(true);

                    }
                }
                else
                    title = "-1";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spiner_item, titleArray) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                TypeFaceMethods.setRegularTypeFaceForTextView((TextView) v, CreateDrProfile.this);

                return v;
            }


            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                TypeFaceMethods.setRegularTypeFaceForTextView((TextView) v, CreateDrProfile.this);

                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTitle.setAdapter(adapter);
        spinnerTitle.setHint("Select Title");
        spinnerTitle.setPaddingSafe(0, 0, 0, 0);

    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(CreateDrProfile.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CreateDrProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CreateDrProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
            selectImage();
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
        }
    }

    public class UpdateProfileAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String fname, lname, email, password, mobile, otp, title, category, city, gender, yearOfExperince, dob, website, about;
        CircularProgressBar bar;
        //PlayServiceHelper regId;

        JSONObject jo;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public UpdateProfileAsyncTask(String fname, String email, String mobile, String title, String category, String city, String gender, String yearOfExperince, String dob, String website, String about) {
            this.email = email;
            this.title = title;
            this.yearOfExperince = yearOfExperince;
            this.about = about;
            this.gender = gender;
            this.dob = dob;
            this.website = website;
            this.fname = fname;
            this.city = city;
            this.lname = lname;
            this.mobile = mobile;
            this.category = category;
        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/update-profile";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;

                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
                        .create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("Content-Type", "applicaion/json");
                entityBuilder.addTextBody(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST);
                entityBuilder.addTextBody("User[name]", fname);
                entityBuilder.addTextBody("userID", userId);
                entityBuilder.addTextBody("User[email]", email);
                entityBuilder.addTextBody("User[mobile]", mobile);
                entityBuilder.addTextBody("DoctorProfile[title]", title);
                entityBuilder.addTextBody("DoctorProfile[category]", category);
                entityBuilder.addTextBody("DoctorProfile[city]", city);
                entityBuilder.addTextBody("DoctorProfile[gender]", gender);
                entityBuilder.addTextBody("DoctorProfile[experience]", yearOfExperince);
                entityBuilder.addTextBody("DoctorProfile[description]", about);
                entityBuilder.addTextBody("DoctorProfile[dob]", dob);
                entityBuilder.addTextBody("DoctorProfile[website]", website);
                if (selectedImagePath != null && selectedImagePath.toString().trim().length() > 0) {
                    File file = new File(selectedImagePath);
                    FileBody encFile = new FileBody(file);
                    entityBuilder.addPart("DoctorProfile[imageFile]", encFile);
                }
                httppost.setHeader("Authorization", "Basic " + authToken);

                HttpEntity entity = entityBuilder.build();
                httppost.setEntity(entity);
                response = client.execute(httppost);

                responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
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
                    JSONArray errorArray;
                    JSONObject dataJsonObject;
                    boolean status = false;
                    String auth_token = "", createdOn = "", id = "", email = "", mobile = "", user_type = "", lname = "", fname = "";
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");

                        if (dataJsonObject.has("status") && !dataJsonObject.isNull("status"))

                        {
                            status = dataJsonObject.getBoolean("status");
                            if (!status) {
                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    errorArray = dataJsonObject.getJSONArray("message");
                                    for (int i = 0; i < errorArray.length(); i++) {
                                        String error = errorArray.getJSONObject(i).getString("error");
                                        Toast.makeText(CreateDrProfile.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {

                                if(dataJsonObject.has("userDetails")&&!dataJsonObject.isNull("userDetails"))
                                { UserDetailModel userDetailModel=new UserDetailModel();
                                    ApplicationSingleton.setUserDetailModel(userDetailModel);
                                    JSONObject userJsonObject = dataJsonObject.getJSONObject("userDetails");
                                    if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                                        ApplicationSingleton.getUserDetailModel() .setName(userJsonObject.getString("name")) ;
                                    }
                                    if (userJsonObject.has("experience") && !userJsonObject.isNull("experience")) {
                                        ApplicationSingleton.getUserDetailModel() .setExperience(userJsonObject.getString("experience")); ;

                                    }
                                    if (userJsonObject.has("gender") && !userJsonObject.isNull("gender")) {
                                        ApplicationSingleton.getUserDetailModel() .setGender( userJsonObject.getString("gender")); ;

                                    }
                                    if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                                        ApplicationSingleton.getUserDetailModel() .setEmail( userJsonObject.getString("email")); ;

                                    }
                                    if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                                        ApplicationSingleton.getUserDetailModel() .setMobile(userJsonObject.getString("mobile")); ;

                                    }
                                    if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                                        ApplicationSingleton.getUserDetailModel() .setProfile_pic(userJsonObject.getString("profile_pic")); ;

                                    }
                                    if (userJsonObject.has("category") && !userJsonObject.isNull("category")) {
                                        ApplicationSingleton.getUserDetailModel() .setCategory(userJsonObject.getString("category")); ;

                                    }
                                    if (userJsonObject.has("dob") && !userJsonObject.isNull("dob")) {
                                        ApplicationSingleton.getUserDetailModel() .setDob( userJsonObject.getString("dob")); ;

                                    }
                                    if (userJsonObject.has("website") && !userJsonObject.isNull("website")) {
                                        ApplicationSingleton.getUserDetailModel() .setWebSite(userJsonObject.getString("website")); ;

                                    }
                                    if (userJsonObject.has("aboutus") && !userJsonObject.isNull("aboutus")) {
                                        ApplicationSingleton.getUserDetailModel() .setAbout(userJsonObject.getString("aboutus")); ;

                                    }
                                    if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                                        ApplicationSingleton.getUserDetailModel() .setTitle( userJsonObject.getString("Title")); ;

                                    }
                                    if (userJsonObject.has("city") && !userJsonObject.isNull("city")) {
                                        ApplicationSingleton.getUserDetailModel() .setTitle( userJsonObject.getString("city")); ;

                                    }

                                }
                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    Toast.makeText(CreateDrProfile.this, dataJsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    if (isSkip) {
                                        Intent intent = new Intent(CreateDrProfile.this, MainActivity.class);
                                        intent.putExtra("isSkip", true);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                    else {
                                        ApplicationSingleton.setIsContactInfoUpdated(true);

                                        finish();
                                    }
                                }

                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public class UserDetailAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;


        JSONObject jo;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }


        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/profile";
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

                pairs.add(new BasicNameValuePair("userID", userId));

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                httppost.setHeader("Authorization", "Basic " + authToken);

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
                    JSONArray errorArray;
                    JSONObject dataJsonObject;
                    boolean status = false;
                    String auth_token = "", createdOn = "", id = "", email = "", mobile = "", user_type = "", lname = "", fname = "";
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");

                        if (dataJsonObject.has("status") && !dataJsonObject.isNull("status"))

                        {
                            status = dataJsonObject.getBoolean("status");
                            if (!status) {
                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    errorArray = dataJsonObject.getJSONArray("message");
                                    for (int i = 0; i < errorArray.length(); i++) {
                                        String error = errorArray.getJSONObject(i).getString("error");
                                        //  Toast.makeText(OtpActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                UserDetailModel userDetailModel = UserDetailPaser.userDetailParser(dataJsonObject);
                                if (userDetailModel != null) {
                                    editTextDob.setText(userDetailModel.getDob());
                                    if (userDetailModel.getWebSite() != null)
                                        editTextWebsite.setText(userDetailModel.getWebSite());
                                    if (userDetailModel.getGender() != null) {
                                        RadioButton radioButtonMale = (RadioButton) findViewById(R.id.male);
                                        RadioButton radioButtonFemale = (RadioButton) findViewById(R.id.female);
                                        if (userDetailModel.getGender().equalsIgnoreCase("1")) {
                                            radioButtonMale.setChecked(true);
                                        } else {
                                            radioButtonFemale.setChecked(true);
                                        }


                                    }
                                    if (userDetailModel.getExperience() != null) {
                                        editTextYearOfExpeicence.setText(userDetailModel.getExperience());
                                    }
                                    if (userDetailModel.getCategory() != null) {
                                        if(!userDetailModel.getCategory().equalsIgnoreCase(""))
                                        if (userDetailModel.getCategory().equalsIgnoreCase("1") && !userDetailModel.getCategory().equalsIgnoreCase("")) {
                                            spinnerCategory.setSelection(1);
                                        } else {
                                            spinnerCategory.setSelection(2);

                                        }
                                    }
                                    if (userDetailModel.getAbout() != null) {
                                        editTextAbout.setText(userDetailModel.getAbout());
                                    }
                                    if (userDetailModel.getTitle() != null && !userDetailModel.getTitle().equalsIgnoreCase("")) {
                                        if (userDetailModel.getTitle().equalsIgnoreCase("DR/Mr")) {
                                            spinnerTitle.setSelection(1);
                                        } else if (userDetailModel.getTitle().equalsIgnoreCase("DR/Mrs")) {
                                            spinnerTitle.setSelection(2);

                                        } else {
                                            spinnerTitle.setSelection(3);
                                        }
                                    }
                                    if (userDetailModel.getCity() != null) {
                                        editTextCity.setText(userDetailModel.getCity());
                                    }
                                    if (userDetailModel.getProfile_pic() != null) {
                                        ImageLoader imageLoader = new ImageLoader(CreateDrProfile.this);
                                        imageLoader.DisplayImage(CreateDrProfile.this, userDetailModel.getProfile_pic(), circleImageView, null, 150, 150, R.drawable.user);
                                    }
                                }

                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public boolean validate() {
        if (title.equalsIgnoreCase("-1")) {
            Toast.makeText(CreateDrProfile.this, R.string.seletct_title, Toast.LENGTH_SHORT).show();
            return false;
        }
        int radioButtonID = genderSpinnerRadioGroup.getCheckedRadioButtonId();
        View radioButton = genderSpinnerRadioGroup.findViewById(radioButtonID);
        int idx = genderSpinnerRadioGroup.indexOfChild(radioButton);
        if (idx == -1) {
            Toast.makeText(CreateDrProfile.this, R.string.select_gender, Toast.LENGTH_SHORT).show();

            return false;
        } else {
            gender = idx + 1 + "";
        }
        if (category.equalsIgnoreCase("-1")) {
            Toast.makeText(CreateDrProfile.this, R.string.select_category, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextCity.getText().toString().length() == 0) {
            Toast.makeText(CreateDrProfile.this, R.string.enter_City, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextYearOfExpeicence.getText().toString().length() == 0) {
            Toast.makeText(CreateDrProfile.this, R.string.Experience, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Integer.parseInt(editTextYearOfExpeicence.getText().toString()) > 100) {
            Toast.makeText(CreateDrProfile.this, "Experience cannot be greater than 100", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextDob.getText().toString().length() == 0) {
            Toast.makeText(CreateDrProfile.this, R.string.DOB, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextAbout.getText().toString().length() == 0) {
            Toast.makeText(CreateDrProfile.this, R.string.About_you, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextWebsite.getText().length() != 0) {
            if (!Methods.validWebOrBlog(editTextWebsite.getText().toString())) {
                Toast.makeText(CreateDrProfile.this, R.string.blog, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userDetailAsyncTask != null && !userDetailAsyncTask.isCancelled()) {
            userDetailAsyncTask.cancelAsyncTask();
        }
        if (updateProfileAsyncTask != null && !updateProfileAsyncTask.isCancelled()) {
            updateProfileAsyncTask.cancelAsyncTask();
        }
    }
}
