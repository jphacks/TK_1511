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

    public void saveKeyStorePid(String key_store_pid) {
        mPrefs.edit().putString(KEY_STORE_PID, key_store_pid).apply();
    }

    public String loadKeyStorePid() {
        return mPrefs.getString(KEY_STORE_PID, "");
    }

}