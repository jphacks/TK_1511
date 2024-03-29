package tk11.jphacks.titech.view.activity;

import android.app.Activity;
import android.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import tk11.jphacks.titech.R;
import tk11.jphacks.titech.view.fragment.CallFragment_;

@EActivity(R.layout.activity_call)
public class CallActivity extends Activity {

    @AfterViews
    public void onAfterViews() {
        setFragment();
    }

    private void setFragment() {
        Fragment fragment = CallFragment_.builder().build();
        getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }
}
