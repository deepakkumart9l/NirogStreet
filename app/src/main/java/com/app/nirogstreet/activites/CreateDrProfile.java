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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.ImageProcess;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Preeti on 23-08-2017.
 */

public class CreateDrProfile extends AppCompatActivity {
    CircleImageView circleImageView;
    private int REQUEST_CAMERA = 99;
    File photoFile;
    private static final String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};

    String selectedImagePath = null;

    String mCurrentPhotoPath;
    MaterialSpinner spinnerTitle;
    private ArrayAdapter<String> adapter;


    private int CAMERA_PERMISSION_CODE = 1;

    private int SELECT_FILE = 999;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_dr_profile);
        circleImageView=(CircleImageView)findViewById(R.id.pro);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();

            }
        });
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ITEMS);
        initSpinnerScrolling();
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
                        "empwin.com.app.empwin.fileprovider",
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
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateDrProfile.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    takePicture();
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void initSpinnerScrolling() {
        spinnerTitle = (MaterialSpinner) findViewById(R.id.titleLay);
        spinnerTitle.setAdapter(adapter);
        spinnerTitle.setHint("Select an item");
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
}
