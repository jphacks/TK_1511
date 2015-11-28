package tk11.jphacks.titech.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import tk11.jphacks.titech.R;
import tk11.jphacks.titech.view.activity.BindingActivity;
import tk11.jphacks.titech.view.activity.CallActivity;
import tk11.jphacks.titech.view.activity.HomeActivity_;

@EFragment(R.layout.fragment_splash)
public class SplashFragment extends Fragment {

    @AfterViews
    void onAfterViews() {
        startSplash();
    }

    @Click(R.id.main_button_binding)
    void movePager() {
        Intent intent = new Intent(getActivity().getApplicationContext(), BindingActivity.class);
        startActivity(intent);
    }

    @Click(R.id.main_button_calling)
    void moveRecycler() {
        Intent intent = new Intent(getActivity().getApplicationContext(), CallActivity.class);
        startActivity(intent);
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
