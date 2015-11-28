package tk11.jphacks.titech.view.activity;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import tk11.jphacks.titech.R;
import tk11.jphacks.titech.view.fragment.MainFragment_;

@EActivity(R.layout.activity_home)
public class HomeActivity extends AppCompatActivity{

    @AfterViews
    public void onAfterViews() {
        setFragment();
    }

    private void setFragment() {
        Fragment fragment = MainFragment_.builder().build();
        getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }
}
