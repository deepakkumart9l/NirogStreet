package com.app.nirogstreet.activites;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.MultipleSelectedItemModel;
import com.app.nirogstreet.model.UserList;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.ImageProcess;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;

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

/**
 * Created by Preeti on 24-11-2017.
 */
public class UpdateCommunity extends Activity {
    EditText community_nameEditText, add_peopleEditText, noteEditText, descriptionEditText;
    private int SELECT_FILE = 999;
    private int REQUEST_CAMERA = 99;
    File photoFile;
    MaterialSpinner spinnerTitle, spinnerGender, spinnerCategory;

    ArrayList<MultipleSelectedItemModel> multipleSelectedItemModels = new ArrayList<>();
    String group = "";
    TextView create;
    CreateGroupAsyncTask createGroupAsyncTask;
    private int STORAGE_PERMISSION_CODE = 1;
    CircularProgressBar circularProgressBar;
    private static final String[] categoryArray = {"Public", "Private"};

    private static final int RESULT_CODE = 1;
    ImageView imgCover;
    String selectedImagePath, selectedVideoPath;
    SesstionManager sesstionManager;
    String groupId = "";
    TextView addTextView, add_imageTextView;
    String authToken, userId;
    ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sesstionManager = new SesstionManager(UpdateCommunity.this);
        authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
        userId = sesstionManager.getUserDetails().get(SesstionManager.USER_ID);
        setContentView(R.layout.create_conunites);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        imgCover = (ImageView) findViewById(R.id.imgView);
        community_nameEditText = (EditText) findViewById(R.id.community_name);
        add_peopleEditText = (EditText) findViewById(R.id.add_people);
        noteEditText = (EditText) findViewById(R.id.note);
        addTextView = (TextView) findViewById(R.id.add);
        spinnerCategory = (MaterialSpinner) findViewById(R.id.titleLay);
        create = (TextView) findViewById(R.id.create);
        create.setText("Update");
        TypeFaceMethods.setRegularTypeFaceForTextView(create, UpdateCommunity.this);
        descriptionEditText = (EditText) findViewById(R.id.description);
        add_imageTextView = (TextView) findViewById(R.id.add_image);
        add_peopleEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UpdateCommunity.this, Multi_Select_Search.class);
                if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0) {
                    intent.putExtra("list", multipleSelectedItemModels);
                }
                startActivityForResult(intent, RESULT_CODE);


            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtill.isNetworkAvailable(UpdateCommunity.this)) {
                    if (isValidated()) {
                        createGroupAsyncTask = new CreateGroupAsyncTask(userId, authToken);
                        createGroupAsyncTask.execute();
                    } else {
                        NetworkUtill.showNoInternetDialog(UpdateCommunity.this);
                    }
                }
            }
        });

        TypeFaceMethods.setRegularTypeFaceForTextView(add_imageTextView, UpdateCommunity.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(addTextView, UpdateCommunity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(community_nameEditText, UpdateCommunity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(noteEditText, UpdateCommunity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(descriptionEditText, UpdateCommunity.this);
        if (getIntent().hasExtra("groupId")) {
            groupId = getIntent().getStringExtra("groupId");
        }



        TypeFaceMethods.setRegularTypeFaceEditText(add_peopleEditText, UpdateCommunity.this);
        imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        initSpinnerScrollingCategory();
        if (!groupId.equalsIgnoreCase(""))
            if (NetworkUtill.isNetworkAvailable(UpdateCommunity.this)) {
                GetCommunityDetailAsyncTask getCommunityDetailAsyncTask = new GetCommunityDetailAsyncTask(groupId);
                getCommunityDetailAsyncTask.execute();
            } else {
                NetworkUtill.showNoInternetDialog(UpdateCommunity.this);
            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (createGroupAsyncTask != null && !createGroupAsyncTask.isCancelled()) {
            createGroupAsyncTask.cancelAsyncTask();
        }
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(UpdateCommunity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(UpdateCommunity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(UpdateCommunity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
            selectImage();
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
        selectedImagePath = image.getAbsolutePath();
        return image;
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = new ImageProcess(UpdateCommunity.this).getOutputMediaFile("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.e("hello", "hi");
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Log.e("ex", "" + ex);
                    // Error occurred while creating the File
                }
                Uri photoURI = FileProvider.getUriForFile(UpdateCommunity.this,
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

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = new ImageProcess(UpdateCommunity.this).getOutputMediaFile("");
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                Uri contentUri = FileProvider.getUriForFile(UpdateCommunity.this, "com.app.nirogstreet.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

            } else {

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            }
            startActivityForResult(intent, REQUEST_CAMERA);

        }
        // Ensure that there's a camera activity to handle the intent
/*
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null)
        {
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
           */
/* // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "empwin.com.app.empwin.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }*//*

        }
*/
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateCommunity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    // dispatchTakePictureIntent();
                    takePicture();
                } else if (items[item].equals("Choose from Library")) {

                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                    // chooseImage();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_CODE) {
                if (data != null) {
                    String s = data.getStringExtra("friendsCsv");
                    add_peopleEditText.setText(s);
                    System.out.print(s);
                    multipleSelectedItemModels = (ArrayList<MultipleSelectedItemModel>) data.getSerializableExtra("list");
                }
            }

            if (requestCode == SELECT_FILE) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    selectedImagePath = getPath(selectedImageUri, UpdateCommunity.this);

                    File fff = new File(selectedImagePath);
                    Glide.with(UpdateCommunity.this)
                            .load(fff) // Uri of the picture
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .crossFade()
                            .override(100, 100)
                            .into(imgCover);


                       /* imgCover.setImageBitmap(BitmapFactory
                                .decodeFile(selectedImagePath));*/
                       /* imgCover.setImageBitmap(BitmapFactory
                                .decodeFile(selectedImagePath));*/


                }
            }

            if (requestCode == REQUEST_CAMERA) {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                      /*  imgCover.setImageBitmap(BitmapFactory
                                .decodeFile(selectedImagePath));
                        imagecoverback.setImageBitmap(BitmapFactory
                                .decodeFile(selectedImagePath));
                        BitmapDrawable drawable = (BitmapDrawable) imagecoverback.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        Bitmap blurred = blurRenderScript(bitmap, 25);//second parametre is radius
                        imagecoverback.setImageBitmap(blurred);*/
                        Uri uri = Uri.fromFile(photoFile);
                        ImageProcess obj = new ImageProcess(UpdateCommunity.this);
                        selectedImagePath = obj.getPath(uri);
                       /* imgCover.setImageBitmap(BitmapFactory
                                .decodeFile(selectedImagePath));*/
                        File fff = new File(selectedImagePath);
                        Glide.with(UpdateCommunity.this)
                                .load(fff) // Uri of the picture
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .crossFade()
                                .override(100, 100)
                                .into(imgCover);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CreateGroupAsyncTask extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String authToken, userId, feedFrom;
        private String responseBody;
        String locationString;
        String messageText;
        String groupName;
        HttpClient client;
        Context context;
        String noteText;

        public CreateGroupAsyncTask(String userid, String authToken) {
            this.feedFrom = feedFrom;
            this.userId = userid;
            this.authToken = authToken;

        }

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            circularProgressBar.setVisibility(View.VISIBLE);
            try {
                messageText = URLEncoder.encode(descriptionEditText.getText().toString(), "UTF-8");
                groupName = URLEncoder.encode(community_nameEditText.getText().toString(), "UTF-8");
                noteText = URLEncoder.encode(noteEditText.getText().toString(), "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String url = AppUrl.BaseUrl + "group/update";
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

                entityBuilder.addTextBody("Community[privacy]", group + "");
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                entityBuilder.addTextBody("userID", userId);
                entityBuilder.addTextBody("Community[name]", groupName);
                entityBuilder.addTextBody("Community[invite_note]", noteText);
                entityBuilder.addTextBody("Community[description]", messageText);
                entityBuilder.addTextBody("groupID", groupId);
                if (selectedImagePath != null && selectedImagePath.toString().trim().length() > 0) {
                    File file = new File(selectedImagePath);
                    FileBody encFile = new FileBody(file);
                    entityBuilder.addPart("Community[imageFile]", encFile);
                }

                if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0) {
                    for (int i = 0; i < multipleSelectedItemModels.size(); i++) {
                        entityBuilder.addTextBody("CommunityMembers[user_id][]", multipleSelectedItemModels.get(i).getUserId());
                    }
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
                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObject = jo.getJSONObject("response");
                        if (jsonObject.has("groupDetail") && !jsonObject.isNull("groupDetail")) {
                            ApplicationSingleton.setIsGroupUpdated(true);

                            finish();

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class GetCommunityDetailAsyncTask extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String groupId;


        private String responseBody;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public GetCommunityDetailAsyncTask(String groupId) {
            this.groupId = groupId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObject = jo.getJSONObject("response");
                        if (jsonObject.has("groupDetail") && !jsonObject.isNull("groupDetail")) {
                            String name = null, invite_note = null, description = null, banner = null, privacy = null, created_profile = null, createdBy_id = null, createdBy_name = null;
                            JSONObject groupDetailJsonObject = jsonObject.getJSONObject("groupDetail");
                            if (groupDetailJsonObject.has("name") && !groupDetailJsonObject.isNull("name")) {
                                name = groupDetailJsonObject.getString("name");
                                community_nameEditText.setText(name);
                            }
                            if (groupDetailJsonObject.has("invite_note") && !groupDetailJsonObject.isNull("invite_note")) {
                                invite_note = groupDetailJsonObject.getString("invite_note");
                                noteEditText.setText(invite_note);
                            }
                            if (groupDetailJsonObject.has("description") && !groupDetailJsonObject.isNull("description")) {
                                description = groupDetailJsonObject.getString("description");
                                descriptionEditText.setText(description);
                            }
                            if (groupDetailJsonObject.has("banner") && !groupDetailJsonObject.isNull("banner")) {
                                banner = groupDetailJsonObject.getString("banner");
                                Glide.with(UpdateCommunity.this)
                                        .load(banner) // Uri of the picture
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .crossFade()
                                        .override(100, 100)
                                        .into(imgCover);
                            }

                            if (groupDetailJsonObject.has("privacy") && !groupDetailJsonObject.isNull("privacy")) {
                                privacy = groupDetailJsonObject.getString("privacy");
                                if (privacy.equalsIgnoreCase("1")) {
                                    spinnerCategory.setSelection(2);
                                } else {
                                    spinnerCategory.setSelection(1);

                                }
                            }
                            if (name != null && banner != null && !banner.contains("tempimages")) {
                                CommunitiesDetails.setNameAndCoverPic(name, banner);
                            }
                            if (groupDetailJsonObject.has("created_by") && !groupDetailJsonObject.isNull("created_by")) {
                                JSONObject created_ByObject = groupDetailJsonObject.getJSONObject("created_by");
                                if (created_ByObject.has("id") && !created_ByObject.isNull("id")) {
                                    createdBy_id = created_ByObject.getString("id");
                                }
                                if (created_ByObject.has("name") && !created_ByObject.isNull("name")) {
                                    createdBy_name = created_ByObject.getString("name");
                                }
                                if (created_ByObject.has("profile_pic") && !created_ByObject.isNull("profile_pic")) {
                                    created_profile = created_ByObject.getString("profile_pic");
                                }

                            }
                            ArrayList<UserList> userDetailModels = new ArrayList<>();
                            if (groupDetailJsonObject.has("members") && !groupDetailJsonObject.isNull("members")) {
                                JSONArray jsonArray = groupDetailJsonObject.getJSONArray("members");
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String userId = null, userName = null, profile_pic = null;
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        if (object.has("user_id") && !object.isNull("user_id")) {
                                            JSONObject userDetail = object.getJSONObject("user_id");
                                            if (userDetail.has("id") && !userDetail.isNull("id")) {
                                                userId = userDetail.getString("id");

                                            }
                                            if (userDetail.has("name") && !userDetail.isNull("name")) {
                                                userName = userDetail.getString("name");

                                            }
                                            if (userDetail.has("profile_pic") && !userDetail.isNull("profile_pic")) {
                                                profile_pic = userDetail.getString("profile_pic");
                                            }
                                            multipleSelectedItemModels.add(new MultipleSelectedItemModel(userId, userName, "", profile_pic));
                                        }
                                    }

                                    if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0) {
                                        add_peopleEditText.setText(getSelectedNameCsv());
                                    }
                                 /*   LinearLayout llGallery = (LinearLayout) view.findViewById(R.id.llGallery);

                                    if (userDetailModels.size() > 0)
                                        for (int i = 0; i < userDetailModels.size() + 1; i++) {
                                            View view = ((CommunitiesDetails) context).getLayoutInflater().inflate(R.layout.member_item_communities, null, false);
                                            if (i == 4)
                                                break;
                                            TextView nameTv = (TextView) view.findViewById(R.id.adminname);
                                            TypeFaceMethods.setRegularTypeFaceForTextView(nameTv, context);
                                            if (i == 3) {
                                                nameTv.setText("View More");
                                            } else {

                                                nameTv.setText(userDetailModels.get(i).getName());
                                                TypeFaceMethods.setRegularTypeFaceForTextView(nameTv, context);
                                                CircleImageView imageView = (CircleImageView) view.findViewById(R.id.cir);
                                                Glide.with(context)
                                                        .load(userDetailModels.get(i).getProfile_pic()) // Uri of the picture
                                                        .centerCrop()
                                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                        .crossFade()
                                                        .override(100, 100)
                                                        .into(imageView);
                                            }
                                            llGallery.addView(view);
                                        }

                                }*/

                                }
                                if (createdBy_id.equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {
                                    CommunitiesDetails.moreImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                } else {
                                    if (userDetailModels != null && userDetailModels.size() > 0)
                                        for (int i = 0; i < userDetailModels.size(); i++) {
                                            if (userDetailModels.get(i).getId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID))) {

                                                break;
                                            }
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

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String url = AppUrl.BaseUrl + "group/detail";
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
                pairs.add(new BasicNameValuePair("groupID", groupId));
                pairs.add(new BasicNameValuePair("userID",userId));

                httppost.setHeader("Authorization", "Basic " + authToken);
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
            circularProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void initSpinnerScrollingCategory() {
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1)
                    group = position  + "";
                else
                    group = "-1";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spiner_item, categoryArray) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                TypeFaceMethods.setRegularTypeFaceForTextView((TextView) v, UpdateCommunity.this);

                return v;
            }


            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                TypeFaceMethods.setRegularTypeFaceForTextView((TextView) v, UpdateCommunity.this);

                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setHint("Select Category");
        spinnerCategory.setPaddingSafe(0, 0, 0, 0);

    }

    private boolean isValidated() {
        if (community_nameEditText.getText().toString() == null || community_nameEditText.getText().toString().equals("")) {
            Toast.makeText(UpdateCommunity.this, "Please enter Group Name!", Toast.LENGTH_LONG).show();

            return false;
        } else if (group.toString() == null || group.toString().equals("")) {
            Toast.makeText(UpdateCommunity.this, "Please select privacy type", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public String getSelectedNameCsv() {
        String languageCSV = "";
        if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0) {
            for (int i = 0; i < multipleSelectedItemModels.size(); i++) {
                String language = multipleSelectedItemModels.get(i).getUserName();
                if (language != null && !language.trim().isEmpty()
                        && languageCSV != null && !languageCSV.trim().isEmpty())
                    languageCSV = languageCSV + ", ";
                languageCSV = languageCSV + language;

            }
        }
        return languageCSV;
    }

}

