package com.app.nirogstreet.uttil;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.app.nirogstreet.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Preeti on 23-08-2017.
 */

public class ImageProcess {
    private static String TAG = "ImageSending";
    private static final float BITMAP_SCALE = 0.5f;
    private static final float BLUR_RADIUS = 7.5f;

    static Context context;
    //UploadFile fileupload;
    public ImageProcess(Context c)
    {
        context=c;
    }


    public Boolean  isImage(String ext){
        final String[] okFileExtensions =  new String[] {"jpg", "png","jpeg"};
        for (String extension : okFileExtensions)
        {
            if (ext.toLowerCase().endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }

    public Boolean  isImageMime(String mime){
        final String[] okFileExtensions =  new String[] {"image/jpg", "image/png","image/jpeg"};
        for (String extension : okFileExtensions)
        {
            if (mime.toLowerCase().endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path)
    { // BEST QUALITY MATCH
//choose a maximum height
        int reqHeight = 1024;
        //choose a max width
        int reqWidth = 1024;

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.e(TAG, "original height"+height);
        Log.e(TAG, "original width"+width);


        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;
        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }
        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Bitmap OverBitmap(Bitmap main, Bitmap source, int type){
        Bitmap bmOverlay = null;
        if(type == 2)
            bmOverlay = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        else
            bmOverlay = Bitmap.createBitmap(main.getWidth(), main.getHeight(), main.getConfig());

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(main, new Matrix(), null);
        canvas.drawBitmap(source, new Matrix(), null);
        return bmOverlay;
    }

    public  Bitmap overlayBitmapToCenter(Bitmap source) {

        Canvas canvas = new Canvas(source);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_image);
        Bitmap icon_resize=Bitmap.createScaledBitmap(icon,60,60,true);
        float left = (source.getWidth() / 2) - (icon_resize.getWidth() / 2);
        float top = (source.getHeight() / 2) - (icon_resize.getHeight() / 2);
        canvas.drawBitmap(icon_resize, left, top, null);
        return source;

    }

    public File getOutputMediaFile(String extension) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getApplicationContext().getPackageName()
                + "/Files");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName = "";
        if(!Validation.isEmptyString(extension))
            mImageName = "MI_" + timeStamp + extension;
        else
            mImageName = "MI_" + timeStamp + ".jpg";

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;

    }



    public static Bitmap getCircleBitmap(Bitmap bitmap) {

        int targetWidth = 200;
        int targetHeight = 200;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = bitmap;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), new Paint(Paint.FILTER_BITMAP_FLAG));
        return targetBitmap;

    }


    public String getPath(final Uri uri) {


        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri) || isMediaDocument2(uri)) {
                Log.e(TAG, " uri "+uri);

                final String docId = DocumentsContract.getDocumentId(uri);
                Log.e(TAG, " docId "+docId);
                final String[] split = docId.split(":");

                final String type = split[0];
                Log.e(TAG, " type "+type);

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }else if("file".equals(type)){
                    contentUri = MediaStore.Files.getContentUri("external");
                }

                Log.e(TAG, " contentUri "+contentUri);

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {

        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument2(Uri uri) {

        return "com.google.android.apps.docs.storage".equals(uri.getAuthority());
    }

    public String writeImagePath(Context context, Uri uri, String mimeType){
        InputStream in = null;
        OutputStream out = null;
        String extension = ".jpg";
        try {
            if (!Validation.isEmptyString(mimeType))
                extension = "." + mimeType.split("\\/")[1];
        }catch (Exception ex){

        }
        File tempFile = getOutputMediaFile(extension);
        try {
            in = context.getContentResolver().openInputStream(uri);
            // open the output-file:
            out = new FileOutputStream(tempFile);
            // copy the content:
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return tempFile.getAbsolutePath();
            // Contents are copied!
        }catch(Exception ex){
            Log.e(TAG, "writePDFToSdCard: error in main try"+ex.getMessage() );
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "writePDFToSdCard: error while close "+e.getMessage() );
            }
        }
    }


    // Copy your image into specific folder
    public File copyFile(File current_location) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getApplicationContext().getPackageName()
                + "/Files");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {
                //Boolean status = mediaStorageDir.mkdir();
                return null;
            }
        }
        File Copy_sourceLocation = new File("" + current_location);
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss")
                .format(new Date());
        File Paste_Target_Location = new File("" + mediaStorageDir + "/"
                + "DOCQUITY_" + timeStamp + current_location.getName());
        try {
            // 1 = move the file, 2 = copy the file
            int actionChoice = 2;
            // moving the file to another directory
            if (actionChoice == 1) {
                if (Copy_sourceLocation.renameTo(Paste_Target_Location)) {
                    Log.i("Purchase-File", "Move file successful.");
                } else {
                    Log.i("Purchase-File", "Move file failed.");
                }
            }else {
                if (Copy_sourceLocation.exists()) {

                    InputStream in = new FileInputStream(Copy_sourceLocation);
                    OutputStream out = new FileOutputStream(
                            Paste_Target_Location);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.i("copyFile", "Copy file successful.");

                } else {
                    Log.i("copyFile", "Copy file failed. Source file missing.");
                }
            }

        } catch (NullPointerException e) {
            Log.i("copyFile", "" + e);

        } catch (Exception e) {
            Log.i("copyFile", "" + e);
        }
        return Paste_Target_Location;
    }



    public String updateFilewithBitmap(Bitmap bitmap, String uri){
        String extension = ".jpg";
        try {
            String mimeType = ImageProcess.getMimeTypefromUri(Uri.parse(uri));
            if (!Validation.isEmptyString(mimeType))
                extension = "." + mimeType.split("\\/")[1];
        }catch (Exception ex){

        }
        File tempFile = getOutputMediaFile(extension);
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String updateFilewithBitmap(Bitmap bitmap, String uri, Bitmap lineBitmap, int type){
        String extension = ".jpg";
        try {
            String mimeType = ImageProcess.getMimeTypefromUri(Uri.parse(uri));
            if (!Validation.isEmptyString(mimeType))
                extension = "." + mimeType.split("\\/")[1];
        }catch (Exception ex){

        }

        File tempFile = getOutputMediaFile(extension);
        OutputStream outStream = null;
        try {
            Bitmap final_bitmap = null;
            if(lineBitmap != null) {
                final_bitmap = OverBitmap(bitmap, lineBitmap, type);
                Log.e(TAG, "updateFilewithBitmap: "+lineBitmap.getWidth() );
                Log.e(TAG, "updateFilewithBitmap: "+lineBitmap.getHeight() );

            }
            else
                final_bitmap = bitmap;


            Log.e(TAG, "updateFilewithBitmap: "+bitmap.getWidth() );
            Log.e(TAG, "updateFilewithBitmap: "+bitmap.getHeight() );

            Log.e(TAG, "updateFilewithBitmap: "+final_bitmap.getWidth() );
            Log.e(TAG, "updateFilewithBitmap: "+final_bitmap.getHeight() );

            outStream = new FileOutputStream(tempFile);
            final_bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveBitMapToSdCard(Bitmap bitmap) {

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss")
                .format(new Date());

        String mImageName = "docquity_" + timeStamp + ".jpg";

        File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), context.getResources().getString(R.string.app_name));

        if (!imageRoot.exists())//check if file already exists
        {
            imageRoot.mkdirs();//if not, create it
        }

        File file = new File(imageRoot, mImageName) ;


        if (file.exists())
            file.delete ();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            // Global_Setting.displayToastATcenter(context, R.string.saveMsg);


            ContentValues image = new ContentValues();
            image.put(MediaStore.Images.Media.TITLE, R.string.app_name);
            image.put(MediaStore.Images.Media.DISPLAY_NAME, mImageName);
            image.put(MediaStore.Images.Media.DESCRIPTION, "App Image");
            image.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
            image.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            image.put(MediaStore.Images.Media.ORIENTATION, 0);
            File parent = file.getParentFile();
            image.put(MediaStore.Images.ImageColumns.BUCKET_ID, parent.toString()
                    .toLowerCase().hashCode());
            image.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, parent.getName()
                    .toLowerCase());
            image.put(MediaStore.Images.Media.SIZE, file.length());
            image.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            Uri result = context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image);

        } catch (Exception e) {
            e.printStackTrace();
            //  Global_Setting.displayToastATcenter(context, R.string.oppsMsg);
            Log.e(TAG, "error while saveToSdCardGetURI "+e.getMessage());
        }

    }


    public Bitmap processCameraImage(Uri uri){
        Bitmap bitmap;

        try {
            String realPath = getPath(uri);
            bitmap = decodeSampledBitmapFromFile(realPath);
            ExifInterface ei = new ExifInterface(realPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            int angle = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    bitmap = rotateImage(bitmap, 180);
                    // etc.

                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    bitmap = rotateImage(bitmap, 270);
                    // etc.
            }

            return bitmap;
        }catch(Exception ex){
            Log.e(TAG, "error while processCameraImage "+ex.getMessage());

            return null;
        }
    }


    public static String getBase64fromBitmap(Bitmap bitmap){
        String encoded = "";
        byte[] imageData = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageData = baos.toByteArray();
            return android.util.Base64.encodeToString(imageData, android.util.Base64.DEFAULT);
        }catch(Exception ex){
            Log.e(TAG, "error while getBase64fromBitmap"+ex.getMessage());
            return null;
        }

    }


    public static String getMimeTypeOfFile(String pathName) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opt);
        return opt.outMimeType;
    }



    public static String getMimeTypefromUri(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }


}
