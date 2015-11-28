package tk11.jphacks.titech.controller.util;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hirokinaganuma on 15/11/28.
 *
 * 使うかは不明
 *
 */

public class ImageHelper {
    /**
     * 画像のディレクトリパスを取得する
     *
     * @return
     */
    private static String getDirPath(Context context) {
        String dirPath = "";
        File photoDir = null;
        File extStorageDir = Environment.getExternalStorageDirectory();
        if (extStorageDir.canWrite()) {
            photoDir = new File(extStorageDir.getPath() + "/" + context.getPackageName());
        }
        if (photoDir != null) {
            if (!photoDir.exists()) {
                photoDir.mkdirs();
            }
            if (photoDir.canWrite()) {
                dirPath = photoDir.getPath();
            }
        }
        return dirPath;
    }

    /**
     * 画像のUriを取得する
     *
     * @return
     */
    public static Uri getPhotoUri(Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String title = dateFormat.format(today);
        String dirPath = getDirPath(context);
        String fileName = "img_capture_" + title + ".jpg";
        String path = dirPath + "/" + fileName;
        File file = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, path);
        values.put(MediaStore.Images.Media.DATE_TAKEN, currentTimeMillis);
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length());
        }
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return uri;
    }

    public static Bitmap resize(Bitmap bitmap, float parcent) {
        if (bitmap == null || parcent < 0) {
            return null;
        }
        return resize(bitmap, (int) (bitmap.getWidth() * parcent), (int) (bitmap.getHeight() * parcent));
    }

    public static Bitmap resize(Bitmap bitmap, int targetWidth, int targetHeight) {
        if (bitmap == null || targetWidth < 0 || targetHeight < 0) {
            return null;
        }
        int pictureWidth = bitmap.getWidth();
        int pictureHeight = bitmap.getHeight();
        float scale = Math.min((float) targetWidth / pictureWidth, (float) targetHeight / pictureHeight); // (1)

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, pictureWidth, pictureHeight, matrix, true);
    }

    //Convert to multibyte data
//    public static ByteArrayBody toByteArrayBody(Bitmap picture) {
//        if (picture == null) {
//            return null;
//        }
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        picture.compress(Bitmap.CompressFormat.JPEG, 100, bos); // (4)
//        byte[] data = bos.toByteArray();
//        return new ByteArrayBody(data, System.currentTimeMillis() + ".jpg");
//    }

    //画像の保存
    public static String saveImage(ImageView itemImage, Context context) throws IOException {
        Bitmap data = null;
        try {
            // ImageViewをビットマップにキャスト
            BitmapDrawable bd = (BitmapDrawable) itemImage.getDrawable();
            // getBitmapメソッドでビットマップファイルを取り出す。
            data = bd.getBitmap();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (data == null) return null;
        try {
            return saveImage(data, context);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String saveImage(Bitmap bitmap, Context context) throws IOException {
        try {
            File outDir = new File(getDirPath(context));
            if (!outDir.exists()) {
                outDir.mkdir();
            }
            Bitmap data = bitmap;
            File file = null;
            file = new File(getPhotoUri(context).toString());
            if (writeImage(file, data)) {
                return file.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean writeImage(File file, Bitmap mBitmap) {
        try {
            FileOutputStream fo = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fo);
            fo.flush();
            fo.close();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public static Bitmap loadImage(Context context, Uri uri, boolean isLandscape) throws IOException {
        //画面の向きを管理する。
        boolean landscape = false;
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream is = context.getContentResolver().openInputStream(uri);
        BitmapFactory.decodeStream(is, null, options);
        is.close();
        int ow = options.outWidth;
        int oh = options.outHeight;
        //画面が横になっていたら。
        if (ow > oh && isLandscape) {
            landscape = true;
            //縦と横を逆にする。
            oh = options.outWidth;
            ow = options.outHeight;
        }
        int width = (int) (ow * 0.6);
        int height = (int) (oh * 0.6);

        options.inJustDecodeBounds = false;
        options.inSampleSize = Math.max(ow / width, oh / height);
        InputStream is2 = context.getContentResolver().openInputStream(uri);
        bm = BitmapFactory.decodeStream(is2, null, options);
        is2.close();
        if (landscape) {
            Matrix matrix = new Matrix();
            matrix.setRotate(90.0f);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
        }
        bm = Bitmap.createScaledBitmap(bm, (int) width, (int) (width * ((double) oh / (double) ow)), false);
        Bitmap offBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas offCanvas = new Canvas(offBitmap);
        offCanvas.drawBitmap(bm, 0, (height - bm.getHeight()) / 2, null);
        bm = offBitmap;
        return bm;
    }

    public static String encodeImageBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeImageBase64(String bitmapString) {
        byte[] bytes = Base64.decode(bitmapString.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}