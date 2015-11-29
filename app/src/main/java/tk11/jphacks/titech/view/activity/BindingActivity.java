package tk11.jphacks.titech.view.activity;

import android.app.Activity;
import android.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import tk11.jphacks.titech.R;
import tk11.jphacks.titech.view.fragment.BindingFragment_;

@EActivity(R.layout.activity_binding)
public class BindingActivity extends Activity {

    @AfterViews
    public void onAfterViews() {
        setFragment();
    }

    private void setFragment() {
        Fragment fragment = BindingFragment_.builder().build();
        getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }
}
