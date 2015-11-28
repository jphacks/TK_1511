package tk11.jphacks.titech.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import tk11.jphacks.titech.R;
import tk11.jphacks.titech.controller.animation.RevealEffect;
import tk11.jphacks.titech.controller.galleryloader.GalleryLoader;

@EFragment(R.layout.fragment_setting)
public class SettingFragment extends BaseFragment {

    private Uri m_uri;
    private float viewWidth;
    private static Activity activity;

    @AfterViews
    void onAfterViews() {
        activity = getActivity();
        RevealEffect.bindAnimation(
                (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content),
                activity.getIntent(),
                activity.getApplicationContext(),
                activity.getWindow(),
                getResources()
        );
        viewWidth = GalleryLoader.calculateDisplaySize(activity);
    }

    @Click(R.id.main_button_image_register)
    void imageRegisterButtonClicked() {
        Toast.makeText(activity.getApplicationContext(), "SHOWGALLEY", Toast.LENGTH_SHORT).show();
        GalleryLoader.showGallery();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = (ImageView) activity.findViewById(R.id.imageView);
        Uri uri = ImageSetter(requestCode, resultCode, data);
        Toast.makeText(activity.getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        imageView.setImageURI(uri);

        int orientation = GalleryLoader.getOrientation(uri);
        if (orientation > 0 && orientation < 9) {
            GalleryLoader.setMatrix(imageView, orientation, (int) viewWidth);
        }
    }

    public Uri ImageSetter(int requestCode, int resultCode, Intent data) {
        Uri resultUri = null;
        if (requestCode == GalleryLoader.REQUEST_CHOOSER) {
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
}