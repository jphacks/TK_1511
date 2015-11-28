package tk11.jphacks.titech.controller.galleryloader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by hirokinaganuma on 15/11/29.
 *
 *
 * LegacyCode
 *
 */
public class GalleryLoader {

    public static final int REQUEST_CHOOSER = 1000;
    private static Activity activity;
    private static Uri m_uri;

    public static int calculateDisplaySize(Activity mactivity) {
        activity = mactivity;
        // ウィンドウマネージャのインスタンス取得
        WindowManager wm = (WindowManager) activity.getSystemService(activity.WINDOW_SERVICE);
        // ディスプレイのインスタンス生成
        Display disp = wm.getDefaultDisplay();
        return disp.getWidth();
    }

    public static void showGallery(){
        //カメラの起動Intentの用意
        String photoName = System.currentTimeMillis() + ".jpg";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, photoName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        m_uri = activity.getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, m_uri);

        // ギャラリー用のIntent作成
        Intent intentGallery;
        if (Build.VERSION.SDK_INT < 19) {
            intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
            intentGallery.setType("image/*");
        } else {
            intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
            intentGallery.setType("image/jpeg");
        }
        Intent intent = Intent.createChooser(intentCamera, "画像の選択");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intentGallery});
        activity.startActivityForResult(intent, REQUEST_CHOOSER);
    }

    public static Uri onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri resultUri = null;
        if (requestCode == REQUEST_CHOOSER) {
            if (resultCode != activity.RESULT_OK) {// キャンセル時
                return null;
            }
            resultUri = (data != null ? data.getData() : m_uri);
            if (resultUri == null) {// 取得失敗
                return null;
            }

            // ギャラリーへスキャンを促す
            MediaScannerConnection.scanFile(
                    activity.getApplicationContext(),
                    new String[]{resultUri.getPath()},
                    new String[]{"image/jpeg"},
                    null
            );
        }
        return resultUri;
    }

    public static int getOrientation(Uri uri) {
        //dataからfilepathへの変換
        ContentResolver cr = activity.getContentResolver();
        String[] columns = {MediaStore.Images.ImageColumns.ORIENTATION};
        Cursor c = cr.query(uri, columns, null, null, null);
        c.moveToFirst();
        return c.getInt(0);
    }


    public static void setMatrix(ImageView view, int orientation, int width) {
        view.setScaleType(ImageView.ScaleType.MATRIX);
        int wOrg = view.getWidth();
        int hOrg = view.getHeight();
        ViewGroup.LayoutParams lp = view.getLayoutParams();

        float factor;
        Matrix mat = new Matrix();
        mat.reset();
        switch (orientation) {
            case 1://only scaling
                factor = (float) width / (float) wOrg;
                mat.preScale(factor, factor);
                lp.width = (int) (wOrg * factor);
                lp.height = (int) (hOrg * factor);
                break;
            case 2://flip vertical
                factor = (float) width / (float) wOrg;
                mat.postScale(factor, -factor);
                mat.postTranslate(0, hOrg * factor);
                lp.width = (int) (wOrg * factor);
                lp.height = (int) (hOrg * factor);
                break;
            case 3://rotate 180
                mat.postRotate(180, wOrg / 2f, hOrg / 2f);
                factor = (float) width / (float) wOrg;
                mat.postScale(factor, factor);
                lp.width = (int) (wOrg * factor);
                lp.height = (int) (hOrg * factor);
                break;
            case 4://flip horizontal
                factor = (float) width / (float) wOrg;
                mat.postScale(-factor, factor);
                mat.postTranslate(wOrg * factor, 0);
                lp.width = (int) (wOrg * factor);
                lp.height = (int) (hOrg * factor);
                break;
            case 5://flip vertical rotate270
                mat.postRotate(270, 0, 0);
                factor = (float) width / (float) hOrg;
                mat.postScale(factor, -factor);
                lp.width = (int) (hOrg * factor);
                lp.height = (int) (wOrg * factor);
                break;
            case 6://rotate 90
                mat.postRotate(90, 0, 0);
                factor = (float) width / (float) hOrg;
                mat.postScale(factor, factor);
                mat.postTranslate(hOrg * factor, 0);
                lp.width = (int) (hOrg * factor);
                lp.height = (int) (wOrg * factor);
                break;
            case 7://flip vertical, rotate 90
                mat.postRotate(90, 0, 0);
                factor = (float) width / (float) hOrg;
                mat.postScale(factor, -factor);
                mat.postTranslate(hOrg * factor, wOrg * factor);
                lp.width = (int) (hOrg * factor);
                lp.height = (int) (wOrg * factor);
                break;
            case 8://rotate 270
                mat.postRotate(270, 0, 0);
                factor = (float) width / (float) hOrg;
                mat.postScale(factor, factor);
                mat.postTranslate(0, wOrg * factor);
                lp.width = (int) (hOrg * factor);
                lp.height = (int) (wOrg * factor);
                break;
        }
        view.setLayoutParams(lp);
        view.setImageMatrix(mat);
        view.invalidate();
    }
}
