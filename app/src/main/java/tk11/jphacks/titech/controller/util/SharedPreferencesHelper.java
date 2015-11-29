package tk11.jphacks.titech.controller.util;

/**
 * Created by hirokinaganuma on 15/11/28.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * SharedPreferenceのHelperクラス
 */
public class SharedPreferencesHelper {

    private static final String KEY_STORE_PID = "key_store_pid";
    private static final String KEY_PARTNER_STORE_PID = "key_partner_store_pid";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_IMAGE_URI = "key_image_uri";
    private static final String KEY_PARTNER_NAME = "key_partner_name";

    /**
     * SharedPreference.
     */
    SharedPreferences mPrefs;
    /**
     * コンテキスト
     */
    Context mContext;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public SharedPreferencesHelper(@NonNull Context context) {
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public void savePartnerKeyStorePid(String key_partner_store_pid) {
        mPrefs.edit().putString(KEY_PARTNER_STORE_PID, key_partner_store_pid).apply();
    }

    public String loadPartnerKeyStorePid() {
        return mPrefs.getString(KEY_PARTNER_STORE_PID, "");
    }

    public void saveKeyStorePid(String key_store_pid) {
        mPrefs.edit().putString(KEY_STORE_PID, key_store_pid).apply();
    }

    public String loadKeyStorePid() {
        return mPrefs.getString(KEY_STORE_PID, "");
    }


    public void savePartnerName(String key_partner_name) {
        mPrefs.edit().putString(KEY_PARTNER_NAME, key_partner_name).apply();
    }

    public String loadPartnerName() {
        return mPrefs.getString(KEY_PARTNER_NAME, "");
    }

    public void saveName(String key_name) {
        mPrefs.edit().putString(KEY_NAME, key_name).apply();
    }

    public String loadName() {
        return mPrefs.getString(KEY_NAME, "");
    }

    public void saveImageUri(String key_image_uri) {
        mPrefs.edit().putString(KEY_IMAGE_URI, key_image_uri).apply();
    }

    public String loadImageUri() {
        return mPrefs.getString(KEY_IMAGE_URI, "");
    }
}