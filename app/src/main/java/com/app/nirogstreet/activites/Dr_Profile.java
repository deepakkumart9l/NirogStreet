package com.app.nirogstreet.activites;

import android.Manifest;
import android.app.Activity;
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
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.fragments.About_Fragment;
import com.app.nirogstreet.model.ClinicDetailModel;
import com.app.nirogstreet.model.Image;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.UserDetailPaser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.Event_For_Firebase;
import com.app.nirogstreet.uttil.ImageLoader;
import com.app.nirogstreet.uttil.ImageProcess;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Preeti on 22-08-2017.
 */

public class Dr_Profile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    LogoutAsyncTask logoutAsyncTask;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.5f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.5f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    final Uri imageUri = Uri.parse("http://i.imgur.com/VIlcLfg.jpg");
    String mCurrentPhotoPath;
    RelativeLayout experince_rel;
    private int STORAGE_PERMISSION_CODE_VIDEO = 2;
    private int CAMERA_PERMISSION_CODE = 1;
    private boolean mIsTheTitleVisible = false;
    private int REQUEST_CAMERA = 99;

    boolean isImageClicked = false;
    File photoFile;
    private int SELECT_FILE = 999;

    ImageView edtImage, webSite_icon;
    private boolean mIsTheTitleContainerVisible = true;
    ImageView backimg;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private ImageView coverImage;
    private FrameLayout framelayoutTitle;
    RelativeLayout phonelay, emaillay, birth_rel;
    ImageView logout;
    ImageView imgPublic_icon;
    private RelativeLayout linearlayoutTitle;
    TextView profile_complete_txt;
    private TextView textviewTitle;
    TextView refer_pointTV;
    TextView nameTv, placeTv, emailTv, phoneTv, WebTv, yearOfBirthTv, yearOfExperienceTv, QualificationTv, aboutHeading, aboutDetail, QualificationSectionTv, SpecializationSectionHeadingTv, sepcilizationDetailTv, consultationFeesHeading, allTaxes, fee, RegistrationSectionHeadingTv, ExperienceSectionTv, clinicAddressHeading, AwardSectionTv, MemberShipSectionTv;
    CircularProgressBar circularProgressBar;
    TextView specilizationTv;
    ImageView backImageView, editInfo, SpecilizationsevicesEdit, QualificationSectionEdit, RegistrationSectionEdit, ExperinceEdit, MemberShipEdit, AwardEdit, clinicAddressEdit;
    String authToken, userId, email, mobile, userName, UserId = "";
    private SesstionManager sesstionManager;
    UserDetailAsyncTask userDetailAsyncTask;
    TextView SpecilizationsevicesTextView, sevicesTextView, sevicesCsvTextView, spcilizationCsv;
    CircleImageView circleImageView;
    LinearLayout qualifictionLinearLayout, regisrtaionLay, experinceLay, clinicLay, awardLay, memberLay;
    UserDetailModel userDetailModel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    SeekBar seekBar;
    private String selectedImagePath = null;

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(logout, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);

                mIsTheTitleVisible = true;
                backimg.setVisibility(View.VISIBLE);
                logOutHideGone();

                if (nameTv.getText().toString().length() != 0) {
                    textviewTitle.setText(nameTv.getText().toString());
                }
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(logout, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);

                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;

            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                if (nameTv.getText().toString().length() != 0) {
                    textviewTitle.setText(nameTv.getText().toString());
                }
                backimg.setVisibility(View.VISIBLE);
                logOutHideGone();
                startAlphaAnimation(logout, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);

                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    private void logOutHideGone() {
        if (!UserId.equalsIgnoreCase("")) {
            logout.setVisibility(View.GONE);
        } else {
            logout.setVisibility(View.VISIBLE);

        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_detail);
        Event_For_Firebase.getEventCount(Dr_Profile.this, "Feed_Profile_UserProfile_Visit");
        findViews();

        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);
        try {
            setSupportActionBar(toolbar);
            startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            phonelay = (RelativeLayout) findViewById(R.id.phonelay);
            emaillay = (RelativeLayout) findViewById(R.id.emaillay);
            refer_pointTV = (TextView) findViewById(R.id.refer_point);
            backimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
            collapsingToolbarLayout.setTitle("demo");
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.black));
            collapsingToolbarLayout.setTitle("My App Title");
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            edtImage = (ImageView) findViewById(R.id.edtImage);
            experince_rel = (RelativeLayout) findViewById(R.id.experince_rel);
            imgPublic_icon = (ImageView) findViewById(R.id.imgPublic_icon);
            birth_rel = (RelativeLayout) findViewById(R.id.birth_rel);
            webSite_icon = (ImageView) findViewById(R.id.webSite_icon);
            editInfo = (ImageView) findViewById(R.id.editInfo);
            circleImageView = (CircleImageView) findViewById(R.id.pro);
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!UserId.equalsIgnoreCase("")) {

                    } else {
                        Intent intent = new Intent(Dr_Profile.this, CreateDrProfile.class);
                        intent.putExtra("userModel", (Serializable) userDetailModel);
                        startActivity(intent);
                    }
                }
            });
            specilizationTv = (TextView) findViewById(R.id.specilizationTv);
            sevicesTextView = (TextView) findViewById(R.id.sevices);
            spcilizationCsv = (TextView) findViewById(R.id.spcilizationCsv);
            sevicesCsvTextView = (TextView) findViewById(R.id.sevicesCsv);
            SpecilizationsevicesTextView = (TextView) findViewById(R.id.Specilizationsevices);
            SpecilizationsevicesEdit = (ImageView) findViewById(R.id.SpecilizationsevicesEdit);

            MemberShipEdit = (ImageView) findViewById(R.id.MemberShipEdit);
            awardLay = (LinearLayout) findViewById(R.id.awardLay);
            AwardEdit = (ImageView) findViewById(R.id.AwardEdit);
            clinicAddressEdit = (ImageView) findViewById(R.id.clinicAddressEdit);
            AwardSectionTv = (TextView) findViewById(R.id.AwardSectionTv);
            MemberShipSectionTv = (TextView) findViewById(R.id.MemberShipSectionTv);
            backImageView = (ImageView) findViewById(R.id.back);
            memberLay = (LinearLayout) findViewById(R.id.memberLay);
            backImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ExperinceEdit = (ImageView) findViewById(R.id.ExperinceEdit);
            RegistrationSectionEdit = (ImageView) findViewById(R.id.RegistrationSectionEdit);
            QualificationSectionEdit = (ImageView) findViewById(R.id.QualificationSectionEdit);
            experinceLay = (LinearLayout) findViewById(R.id.experinceLay);
            if (getIntent().hasExtra("UserId")) {
                UserId = getIntent().getStringExtra("UserId");
            }
            if (!UserId.equalsIgnoreCase("")) {
                if (!UserId.equalsIgnoreCase(userId)) {
                    edtImage.setVisibility(View.GONE);
                    editInfo.setVisibility(View.GONE);
                    clinicAddressEdit.setVisibility(View.GONE);
                    ExperinceEdit.setVisibility(View.GONE);
                    RegistrationSectionEdit.setVisibility(View.GONE);
                    QualificationSectionEdit.setVisibility(View.GONE);
                    AwardEdit.setVisibility(View.GONE);
                    MemberShipEdit.setVisibility(View.GONE);
                    SpecilizationsevicesEdit.setVisibility(View.GONE);
                }
            }
            if (!UserId.equalsIgnoreCase("")) {
                emaillay.setVisibility(View.GONE);
                phonelay.setVisibility(View.GONE);
            } else {
                emaillay.setVisibility(View.VISIBLE);
                phonelay.setVisibility(View.VISIBLE);
            }
            qualifictionLinearLayout = (LinearLayout) findViewById(R.id.QualificatonLinearLayout);
            regisrtaionLay = (LinearLayout) findViewById(R.id.regisrtaionLay);
            clinicLay = (LinearLayout) findViewById(R.id.clinicLay);
            clinicAddressHeading = (TextView) findViewById(R.id.clinicAddressHeading);
            fee = (TextView) findViewById(R.id.fee);
            nameTv = (TextView) findViewById(R.id.nameTv);
            ExperienceSectionTv = (TextView) findViewById(R.id.ExperienceSectionTv);
            placeTv = (TextView) findViewById(R.id.placeTv);
            emailTv = (TextView) findViewById(R.id.emailTv);
            phoneTv = (TextView) findViewById(R.id.phoneTv);
            WebTv = (TextView) findViewById(R.id.WebTv);
            RegistrationSectionHeadingTv = (TextView) findViewById(R.id.RegistrationSectionHeadingTv);
            aboutHeading = (TextView) findViewById(R.id.about);
            aboutDetail = (TextView) findViewById(R.id.about_detail);
            aboutDetail.setLineSpacing(1, 1);
            yearOfBirthTv = (TextView) findViewById(R.id.yearOfBirthTv);
            yearOfExperienceTv = (TextView) findViewById(R.id.yearOfExperienceTv);
            QualificationSectionTv = (TextView) findViewById(R.id.QualificationSectionTv);
            SpecializationSectionHeadingTv = (TextView) findViewById(R.id.SpecializationSectionHeadingTv);
            QualificationTv = (TextView) findViewById(R.id.QualificationTv);
            sepcilizationDetailTv = (TextView) findViewById(R.id.sepcilizationDetailTv);
            consultationFeesHeading = (TextView) findViewById(R.id.consultaionFees);
            allTaxes = (TextView) findViewById(R.id.allTaxes);
            circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
            sesstionManager = new SesstionManager(Dr_Profile.this);

            if (sesstionManager.isUserLoggedIn() && UserId.equalsIgnoreCase("")) {
                authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
                userId = sesstionManager.getUserDetails().get(SesstionManager.USER_ID);
                email = sesstionManager.getUserDetails().get(SesstionManager.KEY_EMAIL);
                userName = sesstionManager.getUserDetails().get(SesstionManager.KEY_FNAME) + " " + sesstionManager.getUserDetails().get(SesstionManager.KEY_LNAME);
                mobile = sesstionManager.getUserDetails().get(SesstionManager.MOBILE);
                emailTv.setText(email);
                phoneTv.setText(mobile);
                if (userId.equalsIgnoreCase(AppUrl.NIROGSTREET_DESK_ID))
                    nameTv.setText(userName);
                else
                    nameTv.setText(Methods.getName(sesstionManager.getUserDetails().get(SesstionManager.TITLE), userName));


            }
            if (NetworkUtill.isNetworkAvailable(Dr_Profile.this)) {
                userDetailAsyncTask = new UserDetailAsyncTask();
                userDetailAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                NetworkUtill.showNoInternetDialog(Dr_Profile.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findViews() {
        appbar = (AppBarLayout) findViewById(R.id.appbar_layout);
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        coverImage = (ImageView) findViewById(R.id.imageview_placeholder);
        framelayoutTitle = (FrameLayout) findViewById(R.id.framelayout_title);
        linearlayoutTitle = (RelativeLayout) findViewById(R.id.linearlayout_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textviewTitle = (TextView) findViewById(R.id.textview_title);
        backimg = (ImageView) findViewById(R.id.backimg);
        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setMoreMenu(0);
                /*Intent intent = new Intent(Dr_Profile.this, Setting_Activity.class);
                startActivity(intent);*/
            }
        });
        profile_complete_txt = (TextView) findViewById(R.id.profile_complete_txt);
        seekBar = (SeekBar) findViewById(R.id.seekBar_luminosite);
        seekBar.setClickable(false);

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(Dr_Profile.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(Dr_Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(Dr_Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
            selectImage();
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
        }
    }

    private void selectImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dr_Profile.this);


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
                        Glide.with(Dr_Profile.this)
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
        photoFile = new ImageProcess(Dr_Profile.this).getOutputMediaFile("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.e("ex", "" + ex);
                    // Error occurred while creating the File
                }
                Uri photoURI = FileProvider.getUriForFile(Dr_Profile.this,
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = new ImageProcess(Dr_Profile.this).getOutputMediaFile("");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = Uri.fromFile(photoFile);

                try {
                    ImageProcess obj = new ImageProcess(Dr_Profile.this);
                    mCurrentPhotoPath = obj.getPath(uri);
                    selectedImagePath = mCurrentPhotoPath;
                    File fff = new File(selectedImagePath);
                    Glide.with(Dr_Profile.this)
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
                Uri selectedImagePath1 = getImageUri(Dr_Profile.this, selectedImage);
                selectedImagePath = getPath(selectedImagePath1, Dr_Profile.this);
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
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
                if (!UserId.equalsIgnoreCase("")) {
                    pairs.add(new BasicNameValuePair("userID", UserId));

                } else
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
                                        Toast.makeText(Dr_Profile.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                userDetailModel = UserDetailPaser.userDetailParser(dataJsonObject);

                                ApplicationSingleton.setUserDetailModel(userDetailModel);
                                updateContactInfo();
                                updateQualification();
                                updateRegistrationAndDocument();
                                updateExperience();
                                updateClinicInfo();
                                updateAwards();
                                updateSpecilizationAndService();
                                updateMemberShip();
                                SpecilizationsevicesEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Dr_Profile.this, SpecilizationAndService.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                editInfo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Event_For_Firebase.getEventCount(Dr_Profile.this, "Feed_Profile_UserProfile_PersonalDetails_Click");
                                        Intent intent = new Intent(Dr_Profile.this, CreateDrProfile.class);
                                        intent.putExtra("userModel", (Serializable) userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                QualificationSectionEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Event_For_Firebase.getEventCount(Dr_Profile.this, "Feed_Profile_UserProfile_Qualification_Click");
                                        Intent intent = new Intent(Dr_Profile.this, Dr_Qualifications.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                RegistrationSectionEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Dr_Profile.this, RegistrationAndDocuments.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                ExperinceEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Event_For_Firebase.getEventCount(Dr_Profile.this, "Feed_Profile_UserProfile_Experiance_Click");
                                        Intent intent = new Intent(Dr_Profile.this, Experience.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                MemberShipEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Event_For_Firebase.getEventCount(Dr_Profile.this, "Feed_Profile_UserProfile_Membership_Click");
                                        Intent intent = new Intent(Dr_Profile.this, MemberShip.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                AwardEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Event_For_Firebase.getEventCount(Dr_Profile.this, "Feed_Profile_UserProfile_Award_Click");
                                        Intent intent = new Intent(Dr_Profile.this, Award.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                clinicAddressEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Dr_Profile.this, AllClinicListing.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });

                            }

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (ApplicationSingleton.getUserDetailModel() != null) {
            userDetailModel = ApplicationSingleton.getUserDetailModel();
        }
        if (ApplicationSingleton.isContactInfoUpdated()) {
            updateContactInfo();
        }
        if (ApplicationSingleton.isClinicUpdated()) {
            updateClinicInfo();
            ApplicationSingleton.setIsClinicUpdated(false);
        }
        if (ApplicationSingleton.isMemberShipUpdated()) {
            updateMemberShip();
            ApplicationSingleton.setIsMemberShipUpdated(false);
        }
        if (ApplicationSingleton.isAwardUpdated()) {
            updateAwards();
            ApplicationSingleton.setIsAwardUpdated(false);
        }
        if (ApplicationSingleton.isExperinceUpdated()) {
            updateExperience();
            ApplicationSingleton.setIsExperinceUpdated(false);
        }
        if (ApplicationSingleton.isServicesAndSpecializationUpdated()) {

            updateSpecilizationAndService();
            ApplicationSingleton.setServicesAndSpecializationUpdated(false);
        }
        if (ApplicationSingleton.isQualificationUpdated()) {
            updateQualification();
            ApplicationSingleton.setIsQualificationUpdated(false);
        }
        if (ApplicationSingleton.isRegistrationUpdated()) {
            updateRegistrationAndDocument();
            ApplicationSingleton.setRegistrationUpdated(false);
        }
    }

    private void updateSpecilizationAndService() {
        try {
            if (userDetailModel != null) {
                if (userDetailModel.getSpecializationModels().size() == 0 && userDetailModel.getServicesModels().size() == 0) {
                    SpecilizationsevicesEdit.setImageDrawable(getResources().getDrawable(R.drawable.add));

                } else {
                    SpecilizationsevicesEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));
                    SpecilizationsevicesTextView.setText("Services & Specialization");

                }
                if (userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() != 0) {
                    spcilizationCsv.setText(getSelectedNameCsv());
                    specilizationTv.setVisibility(View.VISIBLE);
                    spcilizationCsv.setVisibility(View.VISIBLE);
                } else {
                    spcilizationCsv.setVisibility(View.GONE);
                    specilizationTv.setVisibility(View.GONE);
                }
                if (userDetailModel.getServicesModels() != null && userDetailModel.getServicesModels().size() != 0) {
                    sevicesCsvTextView.setText(getSelectedServicesCsv());
                    QualificationTv.setText(getSelectedNameCsv());
                    imgPublic_icon.setVisibility(View.VISIBLE);
                    sevicesCsvTextView.setVisibility(View.VISIBLE);
                    sevicesTextView.setVisibility(View.VISIBLE);

                } else {
                    sevicesCsvTextView.setVisibility(View.GONE);
                    sevicesTextView.setVisibility(View.GONE);
                }

            }
            if (!UserId.equalsIgnoreCase("")) {
                SpecilizationsevicesTextView.setText("Services & Specialization");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMoreMenu(int i) {
        PopupMenu popup = new PopupMenu(Dr_Profile.this, logout);

        popup.getMenuInflater().inflate(R.menu.logout, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        if (NetworkUtill.isNetworkAvailable(Dr_Profile.this)) {
                            logoutAsyncTask = new LogoutAsyncTask("");
                            logoutAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            NetworkUtill.showNoInternetDialog(Dr_Profile.this);
                        }
                        break;
                    case R.id.refer:
                        Intent intent = new Intent(Dr_Profile.this, ReferalActivity.class);
                        startActivity(intent);
                }
                return false;
            }
        });
        popup.show();
    }

    private void updateExperience() {

        if (userDetailModel != null) {
            if (userDetailModel.getProfile_complete() != 1) {
                seekBar.setProgress(userDetailModel.getProfile_complete());
                profile_complete_txt.setText(userDetailModel.getProfile_complete() + "%");
            }

            if (userDetailModel.getExperinceModels() == null || userDetailModel.getExperinceModels().size() == 0) {
                ExperienceSectionTv.setText("Add a Experience");
                ExperinceEdit.setImageDrawable(getResources().getDrawable(R.drawable.add));
                experinceLay.removeAllViews();

            } else {
                experinceLay.removeAllViews();

                ExperienceSectionTv.setText("Experience");
                ExperinceEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < userDetailModel.getExperinceModels().size(); i++) {
                    try {
                        View v = LayoutInflater.from(this).inflate(R.layout.profile_qualification_item, null);

                        TextView textView = (TextView) v.findViewById(R.id.clgName);
                        TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                        TextView year = (TextView) v.findViewById(R.id.year_of_passing);

                        if (userDetailModel.getExperinceModels().get(i).getEnd_time() == null) {
                            year.setText(userDetailModel.getExperinceModels().get(i).getStart_time() + " - Currently Working");

                        } else
                            year.setText(userDetailModel.getExperinceModels().get(i).getStart_time() + " - " + userDetailModel.getExperinceModels().get(i).getEnd_time());
                        degreename.setVisibility(View.GONE);
                        textView.setText(userDetailModel.getExperinceModels().get(i).getOrganizationName());
                        v.setLayoutParams(params);

                        experinceLay.addView(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }
        if (!UserId.equalsIgnoreCase("")) {
            ExperienceSectionTv.setText("Experience");

        }
    }

    private void updateMemberShip() {

        if (userDetailModel != null) {
            if (userDetailModel.getProfile_complete() != 1) {
                seekBar.setProgress(userDetailModel.getProfile_complete());
                profile_complete_txt.setText(userDetailModel.getProfile_complete() + "%");
            }

            if (userDetailModel.getMemberShipModels() == null || userDetailModel.getMemberShipModels().size() == 0) {
                MemberShipSectionTv.setText("Add a Membership");
                memberLay.removeAllViews();

                MemberShipEdit.setImageResource(R.drawable.add);
            } else {
                memberLay.removeAllViews();
                MemberShipSectionTv.setText("Membership");
                MemberShipEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < userDetailModel.getMemberShipModels().size(); i++) {
                    try {
                        View v = LayoutInflater.from(this).inflate(R.layout.profile_qualification_item, null);

                        TextView textView = (TextView) v.findViewById(R.id.clgName);
                        TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                        TextView year = (TextView) v.findViewById(R.id.year_of_passing);
                        degreename.setVisibility(View.GONE);
                        year.setVisibility(View.GONE);
                        textView.setText(userDetailModel.getMemberShipModels().get(i).getMembership());
                        v.setLayoutParams(params);

                        memberLay.addView(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }
        if (!UserId.equalsIgnoreCase("")) {
            MemberShipSectionTv.setText("Membership");

        }

    }

    private void updateQualification() {

        if (userDetailModel != null) {
            if (userDetailModel.getProfile_complete() != 1) {
                seekBar.setProgress(userDetailModel.getProfile_complete());
                profile_complete_txt.setText(userDetailModel.getProfile_complete() + "%");
            }

            if (userDetailModel.getQualificationModels() == null || userDetailModel.getQualificationModels().size() == 0) {
                qualifictionLinearLayout.removeAllViews();

                QualificationSectionTv.setText("Add a Qualification");
                QualificationSectionEdit.setImageResource(R.drawable.add);

            } else {
                qualifictionLinearLayout.removeAllViews();
                QualificationSectionTv.setText("Qualification");
                QualificationSectionEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < userDetailModel.getQualificationModels().size(); i++) {
                    try {
                        View v = LayoutInflater.from(this).inflate(R.layout.profile_qualification_item, null);

                        TextView textView = (TextView) v.findViewById(R.id.clgName);
                        TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                        TextView year = (TextView) v.findViewById(R.id.year_of_passing);

                        year.setText(userDetailModel.getQualificationModels().get(i).getPassingYear());
                        degreename.setText(userDetailModel.getQualificationModels().get(i).getClgName());
                        textView.setText(userDetailModel.getQualificationModels().get(i).getDegreeName());
                        v.setLayoutParams(params);

                        qualifictionLinearLayout.addView(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }
        if (!UserId.equalsIgnoreCase("")) {
            QualificationSectionTv.setText("Qualification");

        }
    }

    private void updateAwards() {

        if (userDetailModel != null) {
            if (userDetailModel.getProfile_complete() != 1) {
                seekBar.setProgress(userDetailModel.getProfile_complete());
                profile_complete_txt.setText(userDetailModel.getProfile_complete() + "%");
            }

            if (userDetailModel.getAwardsModels() == null || userDetailModel.getAwardsModels().size() == 0) {
                AwardSectionTv.setText("Add a Award");
                AwardEdit.setImageResource(R.drawable.add);
                awardLay.removeAllViews();


            } else {
                awardLay.removeAllViews();
                AwardSectionTv.setText("Award");
                AwardEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < userDetailModel.getAwardsModels().size(); i++) {
                    try {
                        View v = LayoutInflater.from(this).inflate(R.layout.profile_qualification_item, null);

                        TextView textView = (TextView) v.findViewById(R.id.clgName);
                        TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                        TextView year = (TextView) v.findViewById(R.id.year_of_passing);

                        year.setText(userDetailModel.getAwardsModels().get(i).getYear());
                        degreename.setVisibility(View.GONE);
                        textView.setText(userDetailModel.getAwardsModels().get(i).getAwardName());
                        v.setLayoutParams(params);

                        awardLay.addView(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }
        if (!UserId.equalsIgnoreCase("")) {
            AwardSectionTv.setText("Award");

        }

    }


    private void updateContactInfo() {
        if (userDetailModel != null) {
            if (!UserId.equalsIgnoreCase("")) {
                seekBar.setVisibility(View.GONE);
                profile_complete_txt.setVisibility(View.GONE);
            }
            if (userDetailModel.getProfile_complete() != 1) {
                seekBar.setProgress(userDetailModel.getProfile_complete());
                profile_complete_txt.setText(userDetailModel.getProfile_complete() + "%");
            }
            if (userDetailModel.getExperience() != null && !userDetailModel.getExperience().equalsIgnoreCase("")) {
                yearOfExperienceTv.setText(userDetailModel.getExperience() + " " + "years experience");
                experince_rel.setVisibility(View.VISIBLE);
            } else {
                experince_rel.setVisibility(View.GONE);
            }
            if (userDetailModel.getName() != null && !userDetailModel.getName().equalsIgnoreCase("")) {
                if (userDetailModel.getUserId() != null && userDetailModel.getUserId().equalsIgnoreCase(AppUrl.NIROGSTREET_DESK_ID)) {
                    nameTv.setText(userDetailModel.getName());
                } else {
                    nameTv.setText(Methods.getName(userDetailModel.getTitle(), userDetailModel.getName()).trim());

                }
            }
            if (userDetailModel.getEmail() != null && !userDetailModel.getEmail().equalsIgnoreCase("")) {
                emailTv.setText(userDetailModel.getEmail());
            }
            if (userDetailModel.getCity() != null && !userDetailModel.getCity().equalsIgnoreCase("")) {
                placeTv.setText(userDetailModel.getCity());
            }
            if (userDetailModel.getWebSite() != null && !userDetailModel.getWebSite().equalsIgnoreCase("")) {
                WebTv.setText(userDetailModel.getWebSite());
                webSite_icon.setVisibility(View.VISIBLE);
            } else {
                WebTv.setVisibility(View.GONE);
                webSite_icon.setVisibility(View.GONE);
            }
            if (UserId.equalsIgnoreCase("")) {
                //  refer_pointTV.setText("Refer Points - "+userDetailModel.getReferPoints());
            }
            if (UserId.equalsIgnoreCase("")) {

                if (userDetailModel.getDob() != null && !userDetailModel.getDob().equalsIgnoreCase("")) {
                    yearOfBirthTv.setText(userDetailModel.getDob());
                    birth_rel.setVisibility(View.VISIBLE);
                } else {
                    birth_rel.setVisibility(View.GONE);

                }
            } else {
                birth_rel.setVisibility(View.GONE);
                yearOfBirthTv.setVisibility(View.GONE);
            }
            if (userDetailModel.getAbout() != null && !userDetailModel.getAbout().equalsIgnoreCase("")) {
                aboutDetail.setText(userDetailModel.getAbout());
                aboutHeading.setVisibility(View.VISIBLE);
                aboutDetail.setVisibility(View.VISIBLE);
            } else {
                aboutHeading.setVisibility(View.GONE);
                aboutDetail.setVisibility(View.GONE);
            }
            if (userDetailModel != null && userDetailModel.getSpecializationModels() != null && userDetailModel.getServicesModels().size() > 0) {
                QualificationTv.setText(getSelectedNameCsv());
                imgPublic_icon.setVisibility(View.VISIBLE);


            } else {
                imgPublic_icon.setVisibility(View.GONE);
            }


            if (userDetailModel.getProfile_pic() != null && !userDetailModel.getProfile_pic().equalsIgnoreCase("")) {
                Picasso.with(Dr_Profile.this)
                        .load(userDetailModel.getProfile_pic())
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(circleImageView);


            }
        }
    }

    private void updateClinicInfo() {
        if (userDetailModel.getProfile_complete() != 1) {
            seekBar.setProgress(userDetailModel.getProfile_complete());
            profile_complete_txt.setText(userDetailModel.getProfile_complete() + "%");
        }

        if (userDetailModel.getClinicDetailModels() == null || userDetailModel.getClinicDetailModels().size() == 0) {
            clinicAddressHeading.setText("Add a Clinic");
            clinicAddressEdit.setImageResource(R.drawable.add);
            clinicLay.removeAllViews();


        } else {
            clinicLay.removeAllViews();
            clinicAddressHeading.setText("Clinical Address");
            clinicAddressEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < userDetailModel.getClinicDetailModels().size(); i++) {
                try {
                    View v = LayoutInflater.from(this).inflate(R.layout.clinic_profile_layout, null);
                    TextView consultaionFeesTv = (TextView) v.findViewById(R.id.consultaionFees);
                    TextView feeTv = (TextView) v.findViewById(R.id.fee);

                    TextView allTaxesTv = (TextView) v.findViewById(R.id.allTaxes);
                    TextView serviceTv = (TextView) v.findViewById(R.id.service);
                    serviceTv.setText(getSelectedClinicService(userDetailModel.getClinicDetailModels().get(i)));
                    serviceTv.setText(getSelectedClinicService(userDetailModel.getClinicDetailModels().get(i)));
                    TextView textView = (TextView) v.findViewById(R.id.clgName);
                    TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                    TextView year = (TextView) v.findViewById(R.id.year_of_passing);

                    if (userDetailModel.getClinicDetailModels().get(i).getPincode() != null && !userDetailModel.getClinicDetailModels().get(i).getPincode().equalsIgnoreCase("null")) {
                        year.setText("Pin Code :" + userDetailModel.getClinicDetailModels().get(i).getPincode());
                    }
                    degreename.setText(userDetailModel.getClinicDetailModels().get(i).getAddress() + " " + userDetailModel.getClinicDetailModels().get(i).getCity());
                    textView.setText(userDetailModel.getClinicDetailModels().get(i).getName());
                    feeTv.setText(userDetailModel.getClinicDetailModels().get(i).getConsultation_fee());
                    v.setLayoutParams(params);

                    clinicLay.addView(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                fee.setText(userDetailModel.getClinicDetailModels().get(0).getConsultation_fee());

            }
        }
        if (!UserId.equalsIgnoreCase("")) {
            clinicAddressHeading.setText("Clinic");

        }
    }

    public String getSelectedClinicService(ClinicDetailModel clinicDetailModel) {
        String languageCSV = "";

        if (clinicDetailModel != null && clinicDetailModel.getServicesModels() != null && clinicDetailModel.getServicesModels().size() > 0) {
            for (int i = 0; i < clinicDetailModel.getServicesModels().size(); i++) {
                String language = clinicDetailModel.getServicesModels().get(i).getSpecializationName();
                if (language != null && !language.trim().isEmpty()
                        && languageCSV != null && !languageCSV.trim().isEmpty())
                    languageCSV = languageCSV + ", ";
                languageCSV = languageCSV + language;

            }
        }
        return languageCSV;

    }

    public String getSelectedServicesCsv() {
        String languageCSV = "";

        if (userDetailModel != null && userDetailModel.getServicesModels() != null && userDetailModel.getServicesModels().size() > 0) {
            for (int i = 0; i < userDetailModel.getServicesModels().size(); i++) {
                String language = userDetailModel.getServicesModels().get(i).getSpecializationName();
                if (language != null && !language.trim().isEmpty()
                        && languageCSV != null && !languageCSV.trim().isEmpty())
                    languageCSV = languageCSV + ", ";
                languageCSV = languageCSV + language;

            }
        }
        return languageCSV;
    }

    public String getSelectedNameCsv() {
        String languageCSV = "";

        if (userDetailModel != null && userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() > 0) {
            for (int i = 0; i < userDetailModel.getSpecializationModels().size(); i++) {
                String language = userDetailModel.getSpecializationModels().get(i).getSpecializationName();
                if (language != null && !language.trim().isEmpty()
                        && languageCSV != null && !languageCSV.trim().isEmpty())
                    languageCSV = languageCSV + ", ";
                languageCSV = languageCSV + language;

            }
        }
        return languageCSV;
    }

    public void updateRegistrationAndDocument() {


        if (userDetailModel != null) {
            if (userDetailModel.getProfile_complete() != 1) {
                seekBar.setProgress(userDetailModel.getProfile_complete());
                profile_complete_txt.setText(userDetailModel.getProfile_complete() + "%");
            }

            if (userDetailModel.getRegistrationAndDocumenModels() == null || userDetailModel.getRegistrationAndDocumenModels().size() == 0) {
                RegistrationSectionHeadingTv.setText("Add a Registration");
                RegistrationSectionEdit.setImageResource(R.drawable.add);
                regisrtaionLay.removeAllViews();

            } else {
                regisrtaionLay.removeAllViews();
                RegistrationSectionHeadingTv.setText("Registration");
                RegistrationSectionEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < userDetailModel.getRegistrationAndDocumenModels().size(); i++) {
                    try {
                        View v = LayoutInflater.from(this).inflate(R.layout.profile_qualification_item, null);

                        TextView textView = (TextView) v.findViewById(R.id.clgName);
                        TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                        TextView year = (TextView) v.findViewById(R.id.year_of_passing);

                        year.setText(userDetailModel.getRegistrationAndDocumenModels().get(i).getCouncil_year());
                        degreename.setText(userDetailModel.getRegistrationAndDocumenModels().get(i).getCouncil_name());
                        textView.setText(userDetailModel.getRegistrationAndDocumenModels().get(i).getCouncil_registration_number());
                        v.setLayoutParams(params);

                        regisrtaionLay.addView(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }
        if (!UserId.equalsIgnoreCase("")) {
            RegistrationSectionHeadingTv.setText("Registration ");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userDetailAsyncTask != null && !userDetailAsyncTask.isCancelled()) {
            userDetailAsyncTask.cancelAsyncTask();
        }
    }

    public class LogoutAsyncTask extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String feedId;


        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public LogoutAsyncTask(String feedId) {
            this.feedId = feedId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (jo != null) {
                    if (jo.has("status") && !jo.isNull("status")) {
                        boolean status = jo.getBoolean("status");
                        if (status) {
                            sesstionManager.logoutUser();
                            sesstionManager.languageLogOut();
                            Intent intent = new Intent(Dr_Profile.this, LoginActivity.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.AppBaseUrl + "user/logout";
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
                httppost.setHeader("authorization", "Nirogstreet " + sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);

                responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                jo = new JSONObject(responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
