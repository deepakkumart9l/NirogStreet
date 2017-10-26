package com.app.nirogstreet.activites;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.AskQuestionForumImagesAdapter;
import com.app.nirogstreet.helpers.Constants;
import com.app.nirogstreet.model.Image;
import com.app.nirogstreet.uttil.GridSpacingItemDecoration;
import com.app.nirogstreet.uttil.ImageProcess;
import com.app.nirogstreet.uttil.PathUtil;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.volokh.danylo.hashtaghelper.HashTagHelper;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Preeti on 24-10-2017.
 */

public class PostingActivity extends Activity implements HashTagHelper.OnHashTagClickListener {
    String selectedImagePath = null;
    String selectedVideoPath = null;
    ArrayList<String> strings = new ArrayList<>();
    RecyclerView recyclerView;
    String mCurrentPhotoPath;
    private int STORAGE_PERMISSION_CODE_DOCUMENT = 3;
    CircleImageView circleImageView;
    File photoFile;
    private TextView mEditTextView;
    CheckBox enableCheckBox;
    private int REQUEST_CAMERA = 99;
    int REQUEST_CODE = 4;
    int REQUEST_CODE_DOC = 5;
    private HashTagHelper mTextHashTagHelper;

    private int CAMERA_PERMISSION_CODE = 1;
    private int SELECT_FILE = 999;
    LinearLayoutManager linearLayoutManager;
    String docpath;
    ImageView imageViewSelected;
    private AskQuestionForumImagesAdapter askQuestionForumImagesAdapter;
    private HashTagHelper mEditTextHashTagHelper;
TextView dr_nameTextView,publicTextView;
    ImageView backImageView;
EditText title_QuestionEditText,editTextMessage,refernceEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);
        backImageView=(ImageView)findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        circleImageView = (CircleImageView) findViewById(R.id.dr_profile);
        dr_nameTextView=(TextView)findViewById(R.id.dr_name);
        publicTextView=(TextView)findViewById(R.id.public_) ;
        title_QuestionEditText=(EditText) findViewById(R.id.title_Question);
        editTextMessage=(EditText)findViewById(R.id.editTextMessage);
        refernceEditText=(EditText)findViewById(R.id.refernce) ;
        TypeFaceMethods.setRegularTypeBoldFaceTextView(dr_nameTextView,PostingActivity.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(publicTextView,PostingActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(title_QuestionEditText,PostingActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(editTextMessage,PostingActivity.this);

        TypeFaceMethods.setRegularTypeFaceEditText(refernceEditText,PostingActivity.this);

/*
        Glide.with(PostingActivity.this).load("https://www.google.com/search?q=nature+image+url&hl=en-US&tbm=isch&source=iu&pf=m&ictx=1&fir=L8qB97yhUQFCnM%253A%252CwMEPW2TZnfw3vM%252C_&usg=__tdpx9ET1W2b6i6SjlmIvIkJYDmo%3D&sa=X&ved=0ahUKEwib7eScsovXAhUFtI8KHYIxCyUQ9QEIPjAE#imgrc=BOuufLthHd4NKM:").into(circleImageView);
*/

        enableCheckBox = (CheckBox) findViewById(R.id.Enable);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ubuntu.regular.ttf");
        enableCheckBox.setTypeface(tf);

        imageViewSelected = (ImageView) findViewById(R.id.imgView);
        mEditTextView = (TextView) findViewById(R.id.edit_text_field);
        TypeFaceMethods.setRegularTypeFaceForTextView(mEditTextView,PostingActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(PostingActivity.this);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PostingActivity.this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(4), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imageViewSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
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
        if (ContextCompat.checkSelfPermission(PostingActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(PostingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(PostingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
            chooseOption();
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            ActivityCompat.requestPermissions(PostingActivity.this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
        }
    }

    private void chooseOption() {

        final CharSequence[] items = {"Photo", "Video", "Document", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PostingActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PostingActivity.this);
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


                    Intent intent = new Intent(PostingActivity.this, AlbumSelectActivity.class);
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
                askQuestionForumImagesAdapter = new AskQuestionForumImagesAdapter(strings, PostingActivity.this);
                recyclerView.setAdapter(askQuestionForumImagesAdapter);
            }
        }
        if (requestCode == 102 && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                Glide.with(PostingActivity.this).load(uri).into(imageViewSelected);
                try {
                    selectedImagePath = null;
                    docpath = null;
                    selectedVideoPath = PathUtil.getPath(PostingActivity.this, uri);
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
                Uri selectedImagePath1 = getImageUri(PostingActivity.this, selectedImage);
                selectedImagePath = getPath(selectedImagePath1, PostingActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_CAMERA) {
            try {
                Uri uri = Uri.fromFile(photoFile);
                selectedVideoPath = null;
                docpath = null;
                ImageProcess obj = new ImageProcess(PostingActivity.this);
                mCurrentPhotoPath = obj.getPath(uri);
                selectedImagePath = mCurrentPhotoPath;
                File fff = new File(selectedImagePath);
                Glide.with(PostingActivity.this)
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
                    String path = PathUtil.getPath(PostingActivity.this, data1);
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
                                Toast.makeText(PostingActivity.this, "Not Supported", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(PostingActivity.this, "Not Supported", Toast.LENGTH_LONG).show();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void checkPermissionForDoc() {
        if (
                ContextCompat.checkSelfPermission(PostingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(PostingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
            openFile();
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE_DOCUMENT);
        }
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
        Toast.makeText(PostingActivity.this, hashTag, Toast.LENGTH_SHORT).show();
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
                        Glide.with(context).load(askQuestionImages.getMediaimage()).into(myViewHolder.imageViewString);
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
                            Intent intent = new Intent(PostingActivity.this, AlbumSelectActivity.class);

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
        photoFile = new ImageProcess(PostingActivity.this).getOutputMediaFile("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Uri photoURI = FileProvider.getUriForFile(PostingActivity.this,
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
        photoFile = new ImageProcess(PostingActivity.this).getOutputMediaFile("");
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
    /*    if (selectedImagePath == null && selectedVideoPath == null && editTextMessage.getText().toString().length() == 0 && latitude.equalsIgnoreCase("") && longitude.equalsIgnoreCase("") && docpath == null && strings.size() == 0) {
            Toast.makeText(PostingActivity.this, "This post appears to be blank. Please write something or attach a link or photo to post", Toast.LENGTH_LONG).show();
            check = false;
        }*/
        return check;
    }
}