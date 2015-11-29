package tk11.jphacks.titech.view.fragment;

/**
 * Created by hirokinaganuma on 15/11/28.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import tk11.jphacks.titech.controller.util.SharedPreferencesHelper;

/**
 * 基底のFragment.
 */
abstract public class BaseFragment extends Fragment {
    protected SharedPreferencesHelper mSharedPrefsHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPrefsHelper = new SharedPreferencesHelper(getActivity().getApplicationContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_CANCELED){
            getActivity().finish();
        }
    }
}
