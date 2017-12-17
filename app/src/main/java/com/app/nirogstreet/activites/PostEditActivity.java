package com.app.nirogstreet.activites;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.PostDetailAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.helpers.Constants;
import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.model.Image;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.parser.FeedParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.GridSpacingItemDecoration;
import com.app.nirogstreet.uttil.ImageProcess;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.MyScrollView;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.PathUtil;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
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
import cz.msebera.android.httpclient.entity.mime.content.ContentBody;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Preeti on 06-12-2017.
 */

public class PostEditActivity extends Activity implements HashTagHelper.OnHashTagClickListener {
    PostDetailAsyncTask postDetailAsyncTask;
    int position;
    String feedId = "", authToken = "", userId = "";
    String selectedImagePath = null;
    String selectedVideoPath = null;
    ArrayList<String> strings = new ArrayList<>();
    RecyclerView recyclerView;
    RelativeLayout linkLay, imagelay;
    ArrayList<SpecializationModel> servicesMultipleSelectedModels = new ArrayList<>();
    String groupId = "";
    String mCurrentPhotoPath;
    private int STORAGE_PERMISSION_CODE_DOCUMENT = 3;
    CircleImageView circleImageView;
    File photoFile;
    private TextView mEditTextView;
    CheckBox enableCheckBox;
    private int REQUEST_CAMERA = 99;
    int REQUEST_CODE = 4;
    boolean isposting = false;
    LinkPostAsynctask linkPostAsynctask;
    SesstionManager sesstionManager;
    int REQUEST_CODE_DOC = 5;
    private HashTagHelper mTextHashTagHelper;
    CircularProgressBar circularProgressBar;
    private static final int RESULT_CODE = 8;

    private int CAMERA_PERMISSION_CODE = 1;
    private int SELECT_FILE = 999;
    LinearLayoutManager linearLayoutManager;
    String linkDescription = "", linktitle = "", linkImage = "", linkUrl = "";
    ImageView linkImageView;
    PostAsyncTask postAsyncTask;
    String docpath;
    ImageView imageViewSelected;
    private AskQuestionForumImagesAdapter askQuestionForumImagesAdapter;
    private HashTagHelper mEditTextHashTagHelper;
    TextView dr_nameTextView, publicTextView;
    ImageView backImageView, cancelImageView;
    MyScrollView myScrollView;
    TextView textViewpost;
    TextView descriptionTextView, titleTextView;
    EditText title_QuestionEditText, editTextMessage, refernceEditText;
    CheckBox checkBox;
    private boolean albumupdate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.post_edit);
        if (getIntent().hasExtra("feedId")) {
            feedId = getIntent().getStringExtra("feedId");
        }
        if(getIntent().hasExtra("position"))
        {
            position=getIntent().getIntExtra("position",-1);
        }
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        if (NetworkUtill.isNetworkAvailable(PostEditActivity.this)) {
            postDetailAsyncTask = new PostDetailAsyncTask(feedId, userId, authToken);
            postDetailAsyncTask.execute();
        } else {
            NetworkUtill.showNoInternetDialog(PostEditActivity.this);
        }
        myScrollView=(MyScrollView)findViewById(R.id.scrol);
        myScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myScrollView.setScrolling(true);

                return false;
            }
        });

        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        editTextMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.editTextMessage) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);

                    myScrollView.setScrolling(false);
                    return false;

                    // v.getParent().requestDisallowInterceptTouchEvent(false);

                }
                return false;
            }
        });

        backImageView = (ImageView) findViewById(R.id.back);
        cancelImageView = (ImageView) findViewById(R.id.cancel);
        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkLay.setVisibility(View.GONE);
                imagelay.setVisibility(View.VISIBLE);
            }
        });
        sesstionManager = new SesstionManager(PostEditActivity.this);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkBox = (CheckBox) findViewById(R.id.Enable);
        descriptionTextView = (TextView) findViewById(R.id.description);
        titleTextView = (TextView) findViewById(R.id.title);
        linkImageView = (ImageView) findViewById(R.id.linkImage);
        textViewpost = (TextView) findViewById(R.id.post);
        if (getIntent().hasExtra("groupId")) {
            groupId = getIntent().getStringExtra("groupId");
        }
        linkLay = (RelativeLayout) findViewById(R.id.linkLay);
        imagelay = (RelativeLayout) findViewById(R.id.imagelay);
        imagelay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myScrollView.setScrolling(true);
                return false;
            }
        });
        linkLay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myScrollView.setScrolling(true);

                return false;
            }
        });
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        circleImageView = (CircleImageView) findViewById(R.id.dr_profile);

        if (sesstionManager.getProfile().get(SesstionManager.KEY_POFILE_PIC) != null) {
            String url;
            url = sesstionManager.getProfile().get(SesstionManager.KEY_POFILE_PIC);
            Glide.with(PostEditActivity.this).load(sesstionManager.getProfile().get(SesstionManager.KEY_POFILE_PIC)).placeholder(R.drawable.user).into(circleImageView);

        }
        dr_nameTextView = (TextView) findViewById(R.id.dr_name);
        dr_nameTextView.setText("Dr. " + sesstionManager.getUserDetails().get(SesstionManager.KEY_FNAME) + " " + sesstionManager.getUserDetails().get(SesstionManager.KEY_LNAME));
        publicTextView = (TextView) findViewById(R.id.public_);
        title_QuestionEditText = (EditText) findViewById(R.id.title_Question);
        title_QuestionEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.title_Question) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);

                    myScrollView.setScrolling(false);
                    return false;

                    // v.getParent().requestDisallowInterceptTouchEvent(false);

                }
                return false;
            }
        });
        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editTextMessage.getText().toString().length() == 0) {
                    linkLay.setVisibility(View.GONE);
                    imagelay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextMessage.getText().toString().contains(" ")) {
                    String strarr[] = editTextMessage.getText().toString().split(" ");
                    for (int i = 0; i < strarr.length; i++) {
                        System.out.print(strarr[i]);
                        if (Patterns.WEB_URL.matcher(strarr[i]).matches()) {
                            // Log.e("URL=", "" + Patterns.WEB_URL.matcher(sampleUrl).matches());
                            if (selectedVideoPath == null && selectedImagePath == null) {
                                linkPostAsynctask = new LinkPostAsynctask(strarr[i], sesstionManager.getUserDetails().get(SesstionManager.USER_ID), sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));
                                linkPostAsynctask.execute();
                            }
                        } else {
                            linkLay.setVisibility(View.GONE);
                            imagelay.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        refernceEditText = (EditText) findViewById(R.id.refernce);
        refernceEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.refernce) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);

                    myScrollView.setScrolling(false);
                    return false;

                    // v.getParent().requestDisallowInterceptTouchEvent(false);

                }
                return false;
            }
        });


/*
        Glide.with(PostingActivity.this).load("https://www.google.com/search?q=nature+image+url&hl=en-US&tbm=isch&source=iu&pf=m&ictx=1&fir=L8qB97yhUQFCnM%253A%252CwMEPW2TZnfw3vM%252C_&usg=__tdpx9ET1W2b6i6SjlmIvIkJYDmo%3D&sa=X&ved=0ahUKEwib7eScsovXAhUFtI8KHYIxCyUQ9QEIPjAE#imgrc=BOuufLthHd4NKM:").into(circleImageView);
*/

        enableCheckBox = (CheckBox) findViewById(R.id.Enable);

      /*  Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ubuntu.regular.ttf");
        enableCheckBox.setTypeface(tf);*/

        imageViewSelected = (ImageView) findViewById(R.id.imgView);
        mEditTextView = (TextView) findViewById(R.id.edit_text_field);
        mEditTextView.setFocusable(false);
        mEditTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myScrollView.setScrolling(true);
                return false;
            }
        });
       // TypeFaceMethods.setRegularTypeFaceForTextView(mEditTextView, PostEditActivity.this);
        mEditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostEditActivity.this, Multi_Select_Search_specialization.class);
                intent.putExtra("list", servicesMultipleSelectedModels);

                intent.putExtra("tags", true);
                startActivityForResult(intent, RESULT_CODE);

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(PostEditActivity.this);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PostEditActivity.this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(4), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        textViewpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Methods.hideKeyboardOfView(editTextMessage, PostEditActivity.this);
                if (validate()) {
                    String check = "2";
                    if (checkBox.isChecked()) {
                        check = "1";
                    }
                    if (NetworkUtill.isNetworkAvailable(PostEditActivity.this)) {
                        String refernce = "";
                        if (refernceEditText.getText().toString().length() == 0) {
                            refernce = refernceEditText.getText().toString();
                        }

                        if (selectedVideoPath != null && selectedVideoPath.length() > 0) {
                            File file = new File(selectedVideoPath);
                            long length = file.length();
                            long length1 = length / 1024 * 1024;

                            if (length1 < 31457280) {

                                if (!isposting) {
                                    isposting = true;
                                    postAsyncTask = new PostAsyncTask(check, sesstionManager.getUserDetails().get(SesstionManager.USER_ID), sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN), title_QuestionEditText.getText().toString(), refernce);
                                    postAsyncTask.execute();

                                }
                            } else {
                                Toast.makeText(PostEditActivity.this, "Sorry! The maximum upload limit is size 30MB.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (!isposting) {
                                isposting = true;
                                postAsyncTask = new PostAsyncTask(check, sesstionManager.getUserDetails().get(SesstionManager.USER_ID), sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN), title_QuestionEditText.getText().toString(), refernce);
                                postAsyncTask.execute();
                            }
                        }
                    } else {
                        NetworkUtill.showNoInternetDialog(PostEditActivity.this);
                    }
                } else {
                    Toast.makeText(PostEditActivity.this, "This post appears to be blank. Please write something or attach a link or photo to post", Toast.LENGTH_LONG).show();
                }
            }
        });

        imageViewSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                myScrollView.setScrolling(true);
            }
        });
        char[] additionalSymbols = new char[]{
                '_',
                '$'
        };
        mEditTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.cardbluebackground), null);
        mEditTextHashTagHelper.handle(mEditTextView);
        mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.cardbluebackground), this, additionalSymbols);
        mTextHashTagHelper.handle(mEditTextView);
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(PostEditActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(PostEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(PostEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
            chooseOption();
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            ActivityCompat.requestPermissions(PostEditActivity.this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.print(requestCode);
        if (requestCode == 1) {
            chooseOption();
        }
    }

    private void chooseOption() {

        final CharSequence[] items = {"Photo", "Video", "Document", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PostEditActivity.this);
        builder.setTitle("Choose One!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Photo")) {
                    selectImage();
                } else if (items[item].equals("Video")) {
                    takeVideo();
                } else if (items[item].equals("Document")) {
                    checkPermissionForDoc();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void takeVideo() {
        selectVideoFromGallery();
    }

    public void selectVideoFromGallery() {
        Intent intent;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
        }
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 102);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PostEditActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    takePicture();
                } else if (items[item].equals("Choose from Library")) {
                   /* Intent intent = new Intent(
                            Intent.ACTION_PICK);
                    intent.setType("image*//*");
                    startActivityForResult(intent, SELECT_FILE);*/
                    strings = new ArrayList<String>();


                    Intent intent = new Intent(PostEditActivity.this, AlbumSelectActivity.class);
                    intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 3);
                    startActivityForResult(intent, Constants.REQUEST_CODE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == Activity.RESULT_OK
                && null != data) {
            // Get the Image from data
            selectedImagePath = null;
            selectedVideoPath = null;
            docpath = null;
            ArrayList<Image> imagesarr = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            for (int i = 0; i < imagesarr.size(); i++) {
                String ContentFilePath = "content://media/external/images/media/";

                Uri selectedImage = Uri.parse(ContentFilePath + imagesarr.get(i).id);
                strings.add(imagesarr.get(i).path);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();


                Log.e("size", "" + imagesarr.size());
            }
            if (imagesarr != null && imagesarr.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                imageViewSelected.setVisibility(View.GONE);
                for (int i = 0; i < strings.size(); i++) {
                    if (strings.get(i).contains("add")) {
                        strings.remove(i);
                    }
                }
                askQuestionForumImagesAdapter = new AskQuestionForumImagesAdapter(strings, PostEditActivity.this);
                recyclerView.setAdapter(askQuestionForumImagesAdapter);
            }
        }
        if (requestCode == 102 && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                Glide.with(PostEditActivity.this).load(uri).into(imageViewSelected);
                try {
                    selectedImagePath = null;
                    docpath = null;
                    selectedVideoPath = PathUtil.getPath(PostEditActivity.this, uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Failed to select video", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
            try {
                final Uri imageUri = data.getData();
                selectedVideoPath = null;
                docpath = null;
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageViewSelected.setImageBitmap(selectedImage);
                Uri selectedImagePath1 = getImageUri(PostEditActivity.this, selectedImage);
                selectedImagePath = getPath(selectedImagePath1, PostEditActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_CAMERA) {
            try {
                Uri uri = Uri.fromFile(photoFile);
                selectedVideoPath = null;
                docpath = null;
                ImageProcess obj = new ImageProcess(PostEditActivity.this);
                mCurrentPhotoPath = obj.getPath(uri);
                selectedImagePath = mCurrentPhotoPath;
                File fff = new File(selectedImagePath);
                Glide.with(PostEditActivity.this)
                        .load(fff) // Uri of the picture
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .crossFade()
                        .override(100, 100)
                        .into(imageViewSelected);

            } catch (Exception e) {
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_DOC) {
                Uri data1 = data.getData();
                // String pathe = data1.getPath();
                // path = getRealPathFromURI_API19(getActivity(), data1);
                try {
                    String path = PathUtil.getPath(PostEditActivity.this, data1);
                    selectedImagePath = null;
                    selectedVideoPath = null;
                    if (path != null) {
                        if (path.contains(".")) {
                            String extension = path.substring(path.lastIndexOf("."));

                            if (extension.equalsIgnoreCase(".doc") || extension.equalsIgnoreCase(".pdf") || extension.equalsIgnoreCase(".docx") || extension.equalsIgnoreCase(".xlsx") || extension.equalsIgnoreCase(".xls") || extension.equalsIgnoreCase(".ppt") || extension.equalsIgnoreCase(".PPTX")) {
                                docpath = path;
                                imageViewSelected.setImageResource(R.drawable.pdf_image);
                                imageViewSelected.setClickable(false);
                            } else {
                                Toast.makeText(PostEditActivity.this, "Not Supported", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(PostEditActivity.this, "Not Supported", Toast.LENGTH_LONG).show();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }

        }
        if (requestCode == RESULT_CODE) {
            if (data != null) {
                String s = data.getStringExtra("friendsCsv");
                mEditTextView.setText(s);
                System.out.print(s);
                servicesMultipleSelectedModels = (ArrayList<SpecializationModel>) data.getSerializableExtra("list");

            }
        }
    }

    public void checkPermissionForDoc() {
        if (
                ContextCompat.checkSelfPermission(PostEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(PostEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
            openFile();
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE_DOCUMENT);
        }
    }

    public class PostDetailAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String feedId, userId;


        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public PostDetailAsyncTask(String feedId, String userId, String authToken) {
            this.feedId = feedId;
            this.authToken = authToken;
            this.userId = userId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            circularProgressBar.setVisibility(View.GONE);

            super.onPostExecute(aVoid);
            try {

                if (jo != null) {
                    int ispostDeletd = 0;
                    if (jo.has("detail") && !jo.isNull("detail")) {
                        JSONArray jsonArray = jo.getJSONArray("detail");
                        if (jsonArray != null) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonObject != null) {
                                if (jsonObject.has("postdeleted") && !jsonObject.isNull("postdeleted")) {
                                    ispostDeletd = jsonObject.getInt("postdeleted");

                                }
                            }

                            ArrayList<FeedModel> feedModels = new ArrayList<>();
                            feedModels.addAll(FeedParser.singlePostFeed(jsonArray.getJSONObject(0)));
                            System.out.print(feedModels.size());
                            getFeedTypeImg(feedModels.get(0));
                        } else {
                            circularProgressBar.setVisibility(View.GONE);
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                System.out.print(e);
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.BaseUrl + "feed/single-post";
                SSLSocketFactory sf = new SSLSocketFactory(SSLContext.getDefault(), SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();
                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                pairs.add(new BasicNameValuePair("userID", userId));
                pairs.add(new BasicNameValuePair("postID", feedId));
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

    public class LinkPostAsynctask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String urlstr, userId;
        HttpClient client;
        Context context;
        private String responseBody;

        public LinkPostAsynctask(String url, String userId, String authToken) {
            this.urlstr = url;
            this.authToken = authToken;
            this.userId = userId;
        }

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObject = jo.getJSONObject("response");
                        if (jsonObject.has("linkdetail") && !jsonObject.isNull("linkdetail")) {
                            linkLay.setVisibility(View.VISIBLE);
                            imagelay.setVisibility(View.GONE);
                            JSONObject linkDetailJsonObject = jsonObject.getJSONObject("linkdetail");
                            if (linkDetailJsonObject.has("description") && !linkDetailJsonObject.isNull("description")) {
                                linkDescription = linkDetailJsonObject.getString("description");
                                descriptionTextView.setText(linkDescription);
                                if (linkDescription.length() == 0) {
                                    linkLay.setVisibility(View.GONE);
                                    imagelay.setVisibility(View.VISIBLE);
                                }
                            }
                            if (linkDetailJsonObject.has("title") && !linkDetailJsonObject.isNull("title")) {
                                linktitle = linkDetailJsonObject.getString("title");
                                titleTextView.setText(linktitle);
                                if (linktitle.length() == 0) {
                                    linkLay.setVisibility(View.GONE);
                                    imagelay.setVisibility(View.VISIBLE);
                                }
                            }
                            if (linkDetailJsonObject.has("images") && !linkDetailJsonObject.isNull("images")) {
                                linkImage = linkDetailJsonObject.getString("images");
                                Glide.with(PostEditActivity.this)
                                        .load(linkImage) // Uri of the picture
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .crossFade()
                                        .override(100, 100)
                                        .into(linkImageView);

                                if (linkImage.length() == 0) {
                                    linkLay.setVisibility(View.GONE);
                                    imagelay.setVisibility(View.VISIBLE);
                                }
                            }
                            if (linkDetailJsonObject.has("url") && !linkDetailJsonObject.isNull("url")) {
                                linkUrl = linkDetailJsonObject.getString("url");
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


                String url = AppUrl.BaseUrl + "pagepreview/preview";
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
                pairs.add(new BasicNameValuePair("url", urlstr));
                httppost.setHeader("Authorization", "Basic " + authToken);

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);

                responseBody = EntityUtils
                        .toString(response.getEntity());
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

    public class PostAsyncTask extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String authToken, userId;
        String isCommented;
        String question;
        String messageText;
        HttpClient client;
        Context context;
        private String responseBody;
        private ProgressDialog pDialog;
        String refrence;
        UrlEncodedFormEntity form;

        public PostAsyncTask(String iscommented, String userid, String authToken, String question, String refernce) {
            this.userId = userid;
            this.isCommented = iscommented;
            this.refrence = refernce;
            this.question = question;
            this.authToken = authToken;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  circularProgressBar.setVisibility(View.VISIBLE);
            pDialog = new ProgressDialog(PostEditActivity.this);
            pDialog.setMessage("Uploading...");
            //pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();

           /* String xx = StringEscapeUtils.escapeJava(editTextMessage.getText().toString());
            messageText = xx.replace("\\", "");*/
            try {
                messageText = URLEncoder.encode(editTextMessage.getText().toString(), "UTF-8");
                Log.e("messageText", "" + messageText);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final int totalProgressTime = 100;
            Thread t = new Thread() {
                @Override
                public void run() {
                    int jumpTime = 0;

                    while (jumpTime < totalProgressTime) {
                        try {
                            sleep(4000);
                            jumpTime += 1;
                            pDialog.setProgress(jumpTime);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };
            t.start();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.BaseUrl + "feed/update";
                SSLSocketFactory sf = new SSLSocketFactory(SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();
                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("Content-Type", "applicaion/json");
                entityBuilder.addTextBody(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST);

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                entityBuilder.addTextBody("userID", userId);
                Log.e("messageText", "inside" + messageText);
                entityBuilder.addTextBody("feedID", feedId);
                entityBuilder.addTextBody("Feed[title]", question);
                entityBuilder.addTextBody("Feed[refrence]", refrence);
                entityBuilder.addTextBody("Feed[message]", messageText);
                entityBuilder.addTextBody("Feed[feed_type]", feedType());
                entityBuilder.addTextBody("Feed[feed_source]", linkUrl);
                entityBuilder.addTextBody("Feed[enable_comment]", isCommented);
                if (!groupId.equalsIgnoreCase("")) {
                    entityBuilder.addTextBody("Feed[community_id]", groupId);

                }
                if (linkLay.isShown()) {
                    if (linkUrl.contains("youtube") || linkUrl.contains("youtu.be")) {
                        entityBuilder.addTextBody("Feed[link_type]", "1");
                    } else {
                        entityBuilder.addTextBody("Feed[link_type]", "2");
                    }
                } else {
                    entityBuilder.addTextBody("Feed[link_type]", "");
                }
                entityBuilder.addTextBody("Feed[url_title]", linktitle);
                entityBuilder.addTextBody("Feed[url_description]", linkDescription);
                entityBuilder.addTextBody("Feed[url_image]", linkImage);
                if (strings != null && strings.size() > 0) {
                    for (int j = 0; j < strings.size(); j++) {
                        if (strings.get(j).equalsIgnoreCase("add")) {
                            strings.remove(j);
                        }

                    }
                    ArrayList<String> files = new ArrayList<>();

                    ArrayList<String> serverImages = new ArrayList<>();
                    for (int i = 0; i < strings.size(); i++) {
                        if (!strings.get(i).equalsIgnoreCase("add")) {
                            if (strings.get(i).startsWith("https://s3-ap-southeast")) {
                               /* URL urlImage = new URL(strings.get(i));

                                String name = getFileName(urlImage);
                                entityBuilder.addTextBody("Feed[mediaName][" + i + "]", name);*/
                                serverImages.add(strings.get(i));

                            } else {
                                files.add(strings.get(i));
                              /*  */
                            }
                        }
                    }
                    for (int m = 0; m < files.size(); m++) {
                        File file = new File(files.get(m));
                        FileBody encFile = new FileBody(file);
                        entityBuilder.addPart("Feed[imageFile][img][" + m + "]", encFile);
                    }
                    for (int m = 0; m < serverImages.size(); m++) {
                        URL urlImage = new URL(serverImages.get(m));

                        String name = getFileName(urlImage);
                        entityBuilder.addTextBody("Feed[mediaName][" + m + "]", name);
                    }
                } else if (selectedImagePath != null && selectedImagePath.toString().trim().length() > 0) {
                    File file = new File(selectedImagePath);
                    FileBody encFile = new FileBody(file);
                    entityBuilder.addPart("Feed[imageFile][img][]", encFile);
                } else if (selectedVideoPath != null && selectedVideoPath.toString().trim().length() > 0) {
                    if (selectedVideoPath.startsWith("https://s3-ap-southeast")) {
                        URL urlImage = new URL(selectedVideoPath);

                        String name = getFileName(urlImage);
                        entityBuilder.addTextBody("Feed[mediaName][]", name);

                    } else {

                        File file = new File(selectedVideoPath);
                        file.getAbsolutePath();
                        ContentBody cbfile = new FileBody(file);
                        entityBuilder.addPart("Feed[imageFile][img][]", cbfile);
                    }
                }
                if (servicesMultipleSelectedModels != null && servicesMultipleSelectedModels.size() > 0) {
                    for (int i = 0; i < servicesMultipleSelectedModels.size(); i++) {
                        if (servicesMultipleSelectedModels.get(i).getId() != null && !servicesMultipleSelectedModels.get(i).getId().equalsIgnoreCase(""))
                            entityBuilder.addTextBody("Tags[id][" + i + "]", servicesMultipleSelectedModels.get(i).getId());
                        entityBuilder.addTextBody("Tags[name][" + i + "]", servicesMultipleSelectedModels.get(i).getSpecializationName());
                    }
                }
                if (docpath != null && docpath.toString().trim().length() > 0) {
                    if (docpath.startsWith("https://s3-ap-southeast")) {
                        URL urlImage = new URL(docpath);

                        String name = getFileName(urlImage);
                        entityBuilder.addTextBody("Feed[mediaName][]", name);

                    } else {
                        File file = new File(docpath);
                        file.getAbsolutePath();
                        ContentBody cbfile = new FileBody(file);
                        entityBuilder.addPart("Feed[imageFile][img][]", cbfile);
                    }
                }

                httppost.setHeader("Authorization", "Basic " + authToken);
                HttpEntity entity = entityBuilder.build();
                httppost.setEntity(entity);
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
            isposting = false;
            pDialog.getProgress();
            for (int k = pDialog.getProgress(); k <= 100; k++) {
                pDialog.setProgress(k);
            }
            pDialog.dismiss();
            try {
                if (jo != null) {
                    if (jo.has("detail") && !jo.isNull("detail")) {
                        JSONArray jsonArray = jo.getJSONArray("detail");
                        if (jsonArray != null) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonObject != null) {
                                if (jsonObject.has("postdeleted") && !jsonObject.isNull("postdeleted")) {

                                }
                            }

                            ArrayList<FeedModel> feedModels = new ArrayList<>();
                            feedModels.addAll(FeedParser.singlePostFeed(jsonArray.getJSONObject(0)));
                            ApplicationSingleton.setPostEditPosition(position);
                            ApplicationSingleton.setFeedModelPostEdited(feedModels.get(0));
                            System.out.print(feedModels.size());
                            finish();
                          //  getFeedTypeImg(feedModels.get(0));
                        } else {
                            circularProgressBar.setVisibility(View.GONE);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String feedType() {
        String type = "0";
        if (linkLay.isShown()) {
            type = "3";
        } else {
            if (selectedImagePath != null) {
                type = "2";
            } else if (selectedVideoPath != null) {
                type = "5";
            } else if (docpath != null) {
                type = "6";
            } else if (strings.size() > 0) {

                type = "2";

            } else {
                if (editTextMessage.getText().toString() != null && !editTextMessage.getText().toString().equals("")) {
                    type = "1";
                }
            }
        }
        return type;

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void openFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"application/pdf", "application/doc", "application/docx", "application/xlsx", "application/xls",
                "application/ppt", "application/PDF", "application/DOCX", "application/DOC", "application/XLSX",
                "application/XLS", "application/PPTX", "application/PPT"};
        intent.setType("documents/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Documents"), REQUEST_CODE_DOC);
    }

    @Override
    public void onHashTagClicked(String hashTag) {
      //  Toast.makeText(PostEditActivity.this, hashTag, Toast.LENGTH_SHORT).show();
    }


    public class AskQuestionForumImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public final int VIEW_TYPE_LIST = 1;
        public final int VIEW_TYPE_ADD_NEW = 2;
        Context context;
        ArrayList<String> askQuestionImagesarr;

        public AskQuestionForumImagesAdapter(ArrayList<String> askQuestionImages, Context context) {
            this.context = context;
            this.askQuestionImagesarr = askQuestionImages;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            switch (viewType) {
                case VIEW_TYPE_ADD_NEW:
                    View v1 = inflater.inflate(R.layout.add_image, parent, false);

                    viewHolder = new AddNewArtistHolder(v1);
                    break;
                case VIEW_TYPE_LIST:
                    View v2 = inflater.inflate(R.layout.grid_image_item, parent, false);
                    viewHolder = new MyHolderView(v2);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            switch (viewHolder.getItemViewType()) {
                case VIEW_TYPE_LIST:
                    final String askQuestionImages = (String) askQuestionImagesarr.get(position);
                    MyHolderView myViewHolder = (MyHolderView) viewHolder;
                  /*  if (!askQuestionImages.isServerImage()) {
                    *//*Picasso.with(context)
                            .load(askQuestionImages.getImage())
                            .placeholder(R.drawable.default_image) //this is optional the image to display while the url image is downloading
                            .into(((MyHolderView) viewHolder).imageViewString);*//*
                        ((MyHolderView) viewHolder).imageViewString.setImageBitmap(BitmapFactory.decodeFile(askQuestionImages.getImage()));
                    } else {
                        Glide.with(co+ntext).load(askQuestionImages.getMediaimage()).into(myViewHolder.imageViewString);+
                        // imageLoader.DisplayImage(context, AppUrl.baseUrlWeb + askQuestionImages.getMediaimage(), myViewHolder.imageViewString, null, 100, 100, R.drawable.default_image);
                    }
                    myViewHolder.cancelImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (((GallaryModel) askQuestionImagesarr.get(position)).isServerImage()) {
                                deletedImagesId.add(((GallaryModel) askQuestionImagesarr.get(position)).getMediaimgid());
                            }
                            askQuestionImagesarr.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, askQuestionImagesarr.size());
                        }
                    });*/
                    Glide.with(context).load(askQuestionImages).into(myViewHolder.imageViewString);
                    myViewHolder.cancelImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //   strings.remove(position);
                            // strings.add(strings.size(),"add");
                            if (position == 0) {
                                recyclerView.setVisibility(View.GONE);
                                imageViewSelected.setVisibility(View.VISIBLE);
                            }
                            askQuestionImagesarr.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, askQuestionImagesarr.size());
                            for (int i = 0; i < askQuestionImagesarr.size(); i++)
                                if (askQuestionImagesarr.get(i).contains("add")) {
                                    askQuestionImagesarr.remove(i);
                                }
                            askQuestionImagesarr.add(askQuestionImagesarr.size(), "add");
                            askQuestionForumImagesAdapter.notifyDataSetChanged();
                          /*  askQuestionImagesarr.add(askQuestionImagesarr.size(),"add");
                            notifyItemInserted(askQuestionImagesarr.size());*/
                        }
                    });
                    break;
                case VIEW_TYPE_ADD_NEW:
                    AddNewArtistHolder addnew = (AddNewArtistHolder) viewHolder;

                    addnew.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(PostEditActivity.this, AlbumSelectActivity.class);

                            switch (strings.size()) {

                                case 1:
                                    intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 3);
                                    break;
                                case 2:
                                    intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 2);
                                    break;
                                case 3:
                                    intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 1);

                            }
                            startActivityForResult(intent, Constants.REQUEST_CODE);

                        }
                    });
                    break;

            }
        }

        @Override
        public int getItemViewType(int position) {
            if (!askQuestionImagesarr.get(position).equals("add")) {
                return VIEW_TYPE_LIST;
            } else {
                return VIEW_TYPE_ADD_NEW;
            }
        }

        @Override
        public int getItemCount() {
            return askQuestionImagesarr.size();
        }

        public class MyHolderView extends RecyclerView.ViewHolder {
            ImageView imageViewString, cancelImageView;

            public MyHolderView(View itemView) {
                super(itemView);
                imageViewString = (ImageView) itemView.findViewById(R.id.gallaryimages);
                cancelImageView = (ImageView) itemView.findViewById(R.id.cancel);
            }
        }

        public class AddNewArtistHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public AddNewArtistHolder(View v) {
                super(v);
                imageView = (ImageView) v.findViewById(R.id.addImage);
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

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = new ImageProcess(PostEditActivity.this).getOutputMediaFile("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Uri photoURI = FileProvider.getUriForFile(PostEditActivity.this,
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = new ImageProcess(PostEditActivity.this).getOutputMediaFile("");
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private boolean validate() {
        boolean check = true;
        List<String> allHashTags = mTextHashTagHelper.getAllHashTags();
        allHashTags.toString();
        if (selectedImagePath == null && selectedVideoPath == null && editTextMessage.getText().toString().length() == 0 && docpath == null && strings.size() == 0 && title_QuestionEditText.getText().length() == 0) {
            Toast.makeText(PostEditActivity.this, "This post appears to be blank. Please write something or attach a link or photo to post", Toast.LENGTH_LONG).show();
            check = false;
        }
        return check;
    }

    public String getFeedTypeImg(FeedModel feedModel) {

        if (feedModel.getFeed_type().equalsIgnoreCase("2") && feedModel.getPost_Type().equalsIgnoreCase("1")) {
            if (feedModel.getFeedSourceArrayList() != null) {
                if (feedModel.getFeedSourceArrayList().size() == 3) {
                    strings = feedModel.getFeedSourceArrayList();

                } else if (feedModel.getFeedSourceArrayList().size() < 3) {
                    strings = feedModel.getFeedSourceArrayList();

                    strings.add("add");
                }
                askQuestionForumImagesAdapter = new AskQuestionForumImagesAdapter(feedModel.getFeedSourceArrayList(), PostEditActivity.this);

                recyclerView.setAdapter(askQuestionForumImagesAdapter);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
        if (feedModel.getFeed_type().equalsIgnoreCase("5") && feedModel.getPost_Type().equalsIgnoreCase("1")) {
            selectedVideoPath = feedModel.getFeed_source();
            imageViewSelected.setBackgroundColor(R.color.gray_2nd);
            imageViewSelected.setImageResource(R.drawable.play_icon);

        }
        if (feedModel.getFeed_type().equalsIgnoreCase("2") && feedModel.getPost_Type().equalsIgnoreCase("2") && feedModel.getLink_type() != null && feedModel.getLink_type().equalsIgnoreCase("2")) {
            if (NetworkUtill.isNetworkAvailable(PostEditActivity.this)) {
                linkPostAsynctask = new LinkPostAsynctask(feedModel.getFeed_source(), sesstionManager.getUserDetails().get(SesstionManager.USER_ID), sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));
                linkPostAsynctask.execute();
            } else {
                NetworkUtill.showNoInternetDialog(PostEditActivity.this);
            }
        }
        if (feedModel.getFeed_type().equalsIgnoreCase("3") && feedModel.getPost_Type().equalsIgnoreCase("1") && feedModel.getLink_type() != null && feedModel.getLink_type().equalsIgnoreCase("1")) {
            if (NetworkUtill.isNetworkAvailable(PostEditActivity.this)) {
                linkPostAsynctask = new LinkPostAsynctask(feedModel.getFeed_source(), sesstionManager.getUserDetails().get(SesstionManager.USER_ID), sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));
                linkPostAsynctask.execute();
            } else {
                NetworkUtill.showNoInternetDialog(PostEditActivity.this);
            }
        }
        if (feedModel.getFeed_type().equalsIgnoreCase("6") && feedModel.getPost_Type().equalsIgnoreCase("1")) {
            docpath = feedModel.getFeed_source();
            imageViewSelected.setImageResource(R.drawable.pdf_image);

        }
        if (feedModel.getTitleQuestion() != null && !feedModel.getTitleQuestion().equalsIgnoreCase("")) {
            title_QuestionEditText.setText(feedModel.getTitleQuestion());
        }
        if (feedModel.getMessage() != null && !feedModel.getMessage().equalsIgnoreCase("")) {
            editTextMessage.setText(feedModel.getMessage());
        }
        return "";

    }

    public static String getFileName(URL extUrl) {
        //URL: "http://photosaaaaa.net/photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg"
        String filename = "";
        //PATH: /photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg
        String path = extUrl.getPath();
        //Checks for both forward and/or backslash
        //NOTE:**While backslashes are not supported in URL's
        //most browsers will autoreplace them with forward slashes
        //So technically if you're parsing an html page you could run into
        //a backslash , so i'm accounting for them here;
        String[] pathContents = path.split("[\\\\/]");
        if (pathContents != null) {
            int pathContentsLength = pathContents.length;
            System.out.println("Path Contents Length: " + pathContentsLength);
            for (int i = 0; i < pathContents.length; i++) {
                System.out.println("Path " + i + ": " + pathContents[i]);
            }
            //lastPart: s659629384_752969_4472.jpg
            String lastPart = pathContents[pathContentsLength - 1];
            String[] lastPartContents = lastPart.split("\\.");
            if (lastPartContents != null && lastPartContents.length > 1) {
                int lastPartContentLength = lastPartContents.length;
                System.out.println("Last Part Length: " + lastPartContentLength);
                //filenames can contain . , so we assume everything before
                //the last . is the name, everything after the last . is the
                //extension
                String name = "";
                for (int i = 0; i < lastPartContentLength; i++) {
                    System.out.println("Last Part " + i + ": " + lastPartContents[i]);
                    if (i < (lastPartContents.length - 1)) {
                        name += lastPartContents[i];
                        if (i < (lastPartContentLength - 2)) {
                            name += ".";
                        }
                    }
                }
                String extension = lastPartContents[lastPartContentLength - 1];
                filename = name + "." + extension;
                System.out.println("Name: " + name);
                System.out.println("Extension: " + extension);
                System.out.println("Filename: " + filename);
            }
        }
        return filename;
    }
}
