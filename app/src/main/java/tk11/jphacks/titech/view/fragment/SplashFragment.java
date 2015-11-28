package tk11.jphacks.titech.view.fragment;

import android.content.Intent;
import android.os.Handler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import tk11.jphacks.titech.R;
import tk11.jphacks.titech.view.activity.HomeActivity_;

@EFragment(R.layout.fragment_splash)
public class SplashFragment extends BaseFragment {

    @AfterViews
    void onAfterViews() {
        startSplash();
    }

    private void startSplash() {
        Handler hdl = new Handler();
        hdl.postDelayed(new splashHandler(), 500);
    }

    class splashHandler implements Runnable {
        public void run() {
            Intent intent = new Intent(getActivity().getApplicationContext(), HomeActivity_.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
