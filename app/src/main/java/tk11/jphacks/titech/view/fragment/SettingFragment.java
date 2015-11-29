package tk11.jphacks.titech.view.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import io.skyway.Peer.OnCallback;
import io.skyway.Peer.Peer;
import io.skyway.Peer.PeerError;
import io.skyway.Peer.PeerOption;
import tk11.jphacks.titech.R;
import tk11.jphacks.titech.controller.animation.RevealEffect;
import tk11.jphacks.titech.controller.galleryloader.GalleryLoader;

@EFragment(R.layout.fragment_setting)
public class SettingFragment extends BaseFragment {

    private static final String TAG = SettingFragment.class.getSimpleName();
    private static Activity activity;
    // ID登録
    private Peer _peer;
    private String _id;
    // View
    private Uri m_uri;
    private float viewWidth;
    private EditText nameText;
    private EditText idText;
    private ImageView iconImageView;

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
        bindComponent();
        setRegisteredInfo();
    }

    @Click(R.id.main_button_save)
    void saveButtonClicked() {
        mSharedPrefsHelper.saveName(nameText.getText().toString());
        mSharedPrefsHelper.saveKeyStorePid(idText.getText().toString());
        mSharedPrefsHelper.saveImageUri(m_uri.toString());

        saveId(idText.getText().toString());
    }

    private void saveId(String id) {

        // Please check this page. >> https://skyway.io/ds/
        PeerOption options = new PeerOption();

        //Enter your API Key.
        options.key = "20acaf0d-4c8f-4d3b-bfa0-d320db8f283c";
        //Enter your registered Domain.
        options.domain = "tk11.titech.jphacks";

        String receivePeerId;
        receivePeerId = id;

        _peer = new Peer(activity, receivePeerId, options);
        setPeerCallback(_peer);

        Toast.makeText(activity, "完了", Toast.LENGTH_SHORT).show();
    }

    //////////Start:Set Peer callback////////////////
    ////////////////////////////////////////////////
    private void setPeerCallback(Peer peer) {

        // !!!: Event/Open
        peer.on(Peer.PeerEventEnum.OPEN, new OnCallback() {
            @Override
            public void onCallback(Object object) {
                Log.e(TAG, "[On/Open]");

                if (object instanceof String) {
                    _id = (String) object;
                    Log.e(TAG, "自分のID:" + _id);
                }
            }
        });

        // !!!: Event/Error
        peer.on(Peer.PeerEventEnum.ERROR, new OnCallback() {
            @Override
            public void onCallback(Object object) {
                PeerError error = (PeerError) object;

                Log.e(TAG, "[On/Error]" + error.type);

                String strMessage = "" + error;
                String strLabel = getString(android.R.string.ok);

                if (PeerError.PeerErrorEnum.INVALID_ID == error.type) {
                    strMessage = "そのIDはすでに使われています";
                    idText.setCursorVisible(true);
                    idText.setError(strMessage);
                }
            }
        });

    }

    //Unset peer callback
    void unsetPeerCallback(Peer peer) {
        peer.on(Peer.PeerEventEnum.OPEN, null);
        peer.on(Peer.PeerEventEnum.CONNECTION, null);
        peer.on(Peer.PeerEventEnum.CALL, null);
        peer.on(Peer.PeerEventEnum.CLOSE, null);
        peer.on(Peer.PeerEventEnum.DISCONNECTED, null);
        peer.on(Peer.PeerEventEnum.ERROR, null);
    }

    /**
     * Destroy Peer object.
     */
    private void destroyPeer() {
        if (null != _peer) {
            unsetPeerCallback(_peer);

            if (false == _peer.isDisconnected) {
                _peer.disconnect();
            }

            if (false == _peer.isDestroyed) {
                _peer.destroy();
            }
            _peer = null;
        }
    }

    @Override
    public void onDestroy() {
        destroyPeer();
        super.onDestroy();
        // android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Click(R.id.main_button_image_register)
    void imageRegisterButtonClicked() {
        showGallery();
    }

    public void bindComponent() {
        nameText = (EditText) activity.findViewById(R.id.name_text);
        idText = (EditText) activity.findViewById(R.id.id_text);
        iconImageView = (ImageView) activity.findViewById(R.id.imageView);
        m_uri = Uri.parse(mSharedPrefsHelper.loadImageUri());
    }

    public void setRegisteredInfo() {
        nameText.setText(mSharedPrefsHelper.loadName());
        idText.setText(mSharedPrefsHelper.loadKeyStorePid());
        if(mSharedPrefsHelper.loadImageUri()==""){
            iconImageView.setImageResource(R.drawable.no_image_icon);
        }else{
            iconImageView.setImageURI(m_uri);
        }
    }

    private void showGallery() {
        // カメラの起動Intentの用意
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
        startActivityForResult(intent, GalleryLoader.REQUEST_CHOOSER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryLoader.REQUEST_CHOOSER) {
            if (resultCode != activity.RESULT_OK) {// キャンセル時
                return;
            }
            Uri resultUri = (data != null ? data.getData() : m_uri);
            if (resultUri == null) {// 取得失敗
                return;
            }

            // ギャラリーへスキャンを促す
            MediaScannerConnection.scanFile(
                    activity.getApplicationContext(),
                    new String[]{resultUri.getPath()},
                    new String[]{"image/jpeg"},
                    null
            );

            // 画像を設定
            m_uri = resultUri;
            iconImageView.setImageURI(resultUri);

            int orientation = GalleryLoader.getOrientation(m_uri);
            if (orientation > 0 && orientation < 9) {
                setMatrix(iconImageView, orientation, (int) viewWidth);
            }
        }
    }
}
